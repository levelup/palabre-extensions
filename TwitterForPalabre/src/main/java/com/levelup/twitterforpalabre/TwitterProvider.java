package com.levelup.twitterforpalabre;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.levelup.palabre.api.ExtensionAccountInfo;
import com.levelup.palabre.api.ExtensionUpdateStatus;
import com.levelup.palabre.api.PalabreExtension;
import com.levelup.palabre.api.datamapping.Article;
import com.levelup.palabre.api.datamapping.Category;
import com.levelup.palabre.api.datamapping.Source;

import java.util.ArrayList;
import java.util.List;



import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.auth.AccessToken;

/**
 * Created by ludo on 16/07/15.
 */
public class TwitterProvider extends PalabreExtension {



    @Override
    protected void onUpdateData() {

        Log.d("T4P", "Palabre ask for a refresh");
        publishUpdateStatus(new ExtensionUpdateStatus().start());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String accessTokenString = sharedPreferences.getString(TwitterUtil.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
        String accessTokenSecret = sharedPreferences.getString(TwitterUtil.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
        Log.d("T4P", "User is back " + accessTokenString);

        AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
        TwitterUtil.getInstance().setTwitterFactory(accessToken);

        publishUpdateStatus(new ExtensionUpdateStatus().progress(5));

        try {
            Log.d("T4P", "Getting twitter user info");
            User user = TwitterUtil.getInstance().getTwitter().showUser(accessToken.getUserId());
            ExtensionAccountInfo account = new ExtensionAccountInfo();
            account.accountName(user.getName());
            account.accountEmail("@" + user.getScreenName());
            account.accountAvatar(user.getMiniProfileImageURL());
            publishAccountInfo(account);

        } catch (TwitterException e) {
            e.printStackTrace();
        }

        publishUpdateStatus(new ExtensionUpdateStatus().progress(15));

        // create pseudo categories
        ArrayList<Category> categories = new ArrayList<>();
        if (Category.getByUniqueId(this, "home") == null) {
            Category category = new Category();
            category.setUniqueId("home");
            category.setTitle("Home");

            // add it to the list
            categories.add(category);
        }

        // create categories for the list
        ArrayList<Long> userListsIds = new ArrayList<>();
        try {
            ResponseList<UserList> userLists = TwitterUtil.getInstance().getTwitter().getUserLists(accessToken.getUserId());

            for (UserList userList : userLists) {
                Category listCategory = new Category();
                listCategory.setTitle(userList.getFullName());
                listCategory.setUniqueId(String.valueOf(userList.getId()));
                categories.add(listCategory);
                userListsIds.add(userList.getId());
            }

        } catch (TwitterException e) {
            e.printStackTrace();
        }

        Category.multipleSave(this, categories);

        List<Category> allCategories = Category.getAll(this);


        publishUpdateStatus(new ExtensionUpdateStatus().progress(25));

        // used to know where to start from when getting new tweets
        List<Article> oldTweets = Article.getAll(this);
        long lastId = -1;
        for (Article article : oldTweets) {
            lastId = Math.max(lastId, Long.valueOf(article.getUniqueId()));
        }


        ArrayList<Article> articles = new ArrayList<>();

        // Twitter Lists
        for (long id : userListsIds) {
            Paging paging = new Paging().count(200);
            if (lastId > 0) {
                paging.setSinceId(lastId);
            }
            try {
                ArrayList<Source> sources = new ArrayList<>();
                ResponseList<Status> userListStatuses = TwitterUtil.getInstance().getTwitter().getUserListStatuses(id, paging);

                for (Status status : userListStatuses) {
                    boolean sourceExistInDb = false;
                    Source source;
                    source = Source.getByUniqueId(this, String.valueOf(status.getUser().getId()));
                    if (source == null) {
                        source = new Source();
                        for (Category category : allCategories) {
                            if (category.getUniqueId().equals(String.valueOf(id))) {
                                source.getCategories().add(category);
                            }
                        }
                        //source.getCategories().add(Category.getByUniqueId(this, "home"));
                    } else {
                        Log.d("T4P", "List: Source is already in db: " + status.getUser().getName());
                        sourceExistInDb = true;

                    }

                    source.setUniqueId(String.valueOf(status.getUser().getId()));
                    source.setIconUrl(status.getUser().getProfileImageURL());
                    source.setTitle(status.getUser().getName());

                    if (sourceExistInDb) {
                        source.save(this);
                    } else {
                        boolean found = false;
                        for (Source newSource : sources) {
                            if (newSource.getUniqueId().equals(String.valueOf(status.getUser().getId()))) {
                                Log.d("T4P", "List: Source is already about to be added and is in temp array: " + status.getUser().getName());
                                found = true;
                            }
                        }
                        if (!found) {
                            Log.d("T4P", "List: New source was not in temp array, add it: " + status.getUser().getName());
                            sources.add(source);
                        }
                    }
                }

                Source.multipleSave(this, sources);
                publishUpdateStatus(new ExtensionUpdateStatus().progress(35));
                List<Source> sourcesInDB = Source.getAll(this);

                for (Status status : userListStatuses) {

                    //System.out.println(status.getUser().getName() + ":" + status.getText());
                    Article tweet = new Article();
                    tweet.setUniqueId(String.valueOf(status.getId()));
                    tweet.setTitle(status.getText());
                    tweet.setAuthor("@" + status.getUser().getScreenName());
                    tweet.setDate(status.getCreatedAt());

                    Log.d("T4P", "List: UniqueID: " + status.getId());
                    tweet.setUniqueId(String.valueOf(status.getId()));
                    for (Source source : sourcesInDB) {
                        if (String.valueOf(status.getUser().getId()).equals(source.getUniqueId())) {
                            Log.d("T4P", "List: User match! " + status.getUser().getName());
                            tweet.setSourceId(source.getId());

                        }
                    }

                    String fullContent = status.getText();
                    for (URLEntity urlEntity : status.getURLEntities()) {
                        fullContent += "<a href='"+ urlEntity.getURL() +"'>" +  urlEntity.getExpandedURL() +  "</a></br></br>";
                    }

                    tweet.setFullContent(fullContent);

                    //tweet.setSourceId(this, String.valueOf(status.getUser().getId()));
                    if (status.getMediaEntities().length > 0) {
                        tweet.setImage(status.getMediaEntities()[0].getMediaURL());
                    }
                    //tweet.setFullContent(status.getMediaEntities()[0].);
                    tweet.setLinkUrl("http://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId());

                    articles.add(tweet);
                }

                //Article.multipleSave(this, articles);
                publishUpdateStatus(new ExtensionUpdateStatus().progress(45));
            } catch (TwitterException e) {
                e.printStackTrace();
            }

        }
        /*// Wipe the old cats
        for (Category catToRemove : Category.getAll(this)) {
            catToRemove.delete(this);
        }*/

        Log.d("T4P", "Now get the twitter home timeline");

        // Home Timeline



        try {
            ArrayList<Source> sources = new ArrayList<>();
            Paging paging = new Paging().count(200);
            if (lastId > 0) {
                paging.setSinceId(lastId);
            }
            List<Status> statuses = TwitterUtil.getInstance().getTwitter().getHomeTimeline(paging);
            //ArrayList<Article> articles = new ArrayList<>();

            for (Status status : statuses) {
                boolean sourceExistInDb = false;
                Source source;
                source = Source.getByUniqueId(this, String.valueOf(status.getUser().getId()));
                if (source == null) {
                    source = new Source();
                    source.getCategories().add(Category.getByUniqueId(this, "home"));
                } else {
                    Log.d("T4P", "Source is already in db: " + status.getUser().getName());
                    sourceExistInDb = true;

                }

                source.setUniqueId(String.valueOf(status.getUser().getId()));
                source.setIconUrl(status.getUser().getProfileImageURL());
                source.setTitle(status.getUser().getName());

                if (sourceExistInDb) {
                    source.save(this);
                } else {
                    boolean found = false;
                    for (Source newSource : sources) {
                        if (newSource.getUniqueId().equals(String.valueOf(status.getUser().getId()))) {
                            Log.d("T4P", "Source is already about to be added and is in temp array: " + status.getUser().getName());
                            found = true;
                        }
                    }
                    if (!found) {
                        Log.d("T4P", "New source was not in temp array, add it: " + status.getUser().getName());
                        sources.add(source);
                    }
                }
            }

            Source.multipleSave(this, sources);

            publishUpdateStatus(new ExtensionUpdateStatus().progress(60));



            List<Source> sourcesInDB = Source.getAll(this);
            Log.d("T4P", "Now get the twitter home timeline status and write them in DB");

            for (Status status : statuses) {
                //System.out.println(status.getUser().getName() + ":" + status.getText());
                Article tweet = new Article();
                tweet.setUniqueId(String.valueOf(status.getId()));
                tweet.setTitle(status.getText());
                tweet.setAuthor("@" + status.getUser().getScreenName());
                tweet.setDate(status.getCreatedAt());

                Log.d("T4P", "UniqueID: " + status.getId());
                tweet.setUniqueId(String.valueOf(status.getId()));
                for (Source source : sourcesInDB) {
                    if (String.valueOf(status.getUser().getId()).equals(source.getUniqueId())) {
                        Log.d("T4P", "User match! " + status.getUser().getName());
                        tweet.setSourceId(source.getId());

                    }
                }

                String fullContent = "";
                for (URLEntity urlEntity : status.getURLEntities()) {
                    fullContent += "<a href='"+ urlEntity.getURL() +"'>" +  urlEntity.getExpandedURL() +  "</a></br></br>";
                }

                tweet.setFullContent(fullContent);

                //tweet.setSourceId(this, String.valueOf(status.getUser().getId()));
                if (status.getMediaEntities().length > 0) {
                    tweet.setImage(status.getMediaEntities()[0].getMediaURL());
                }
                //tweet.setFullContent(status.getMediaEntities()[0].);
                tweet.setLinkUrl("http://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId());

                articles.add(tweet);
            }


        } catch (TwitterException e) {
            e.printStackTrace();
        }
        // save all what we got
        Article.multipleSave(this, articles);
        Log.d("T4P", "All articles saved");

        publishUpdateStatus(new ExtensionUpdateStatus().progress(80));

        // remove useless sources/users
        final List<Source> allSources = Source.getAll(this);
        final List<Source> usersToRemove = new ArrayList<>();
        List<Article> allTweets = Article.getAll(this);

        for (Source allSource : allSources) {

            boolean found = false;
            for (Article tweet : allTweets) {
                if (tweet.getSourceId() == allSource.getId()) {
                    found = true;
                }
            }

            if (!found) {
                Log.d("T4P", "Found user to remove: " + allSource.getTitle());
                usersToRemove.add(allSource);
            }
        }

        for (Source src : usersToRemove) {
            Log.d("T4P", "Removing: " + src.getTitle());
            src.delete(this);
        }
        publishUpdateStatus(new ExtensionUpdateStatus().progress(100));
        Log.d("T4P", "Done!");

        publishUpdateStatus(new ExtensionUpdateStatus().stop());

    }

    private void getFriends(AccessToken accessToken, ArrayList<Category> categories, ArrayList<Source> sources, long cursor) throws TwitterException {

        PagableResponseList<User> friendsList = TwitterUtil.getInstance().getTwitter().getFriendsList(accessToken.getUserId(), cursor, 50);
        Log.d("T4P", "Friends: " + friendsList.size());
        for (User user : friendsList) {
            Source source = new Source();
            source.getCategories().add(categories.get(0));
            source.setUniqueId(String.valueOf(user.getId()));
            source.setIconUrl(user.getProfileImageURL());
            source.setTitle(user.getName());
            sources.add(source);
            Log.d("T4P", "Add friend " + user.getName());
        }
        if (friendsList.hasNext()) {
            Log.d("T4P", "It's better with more friends, let's continue...");
            getFriends(accessToken, categories, sources, friendsList.getNextCursor());
        } else {
            Log.d("T4P", "No more friends.");
        }
    }

    @Override
    protected void onReadArticles(List<String> list, boolean b) {

    }

    @Override
    protected void onReadArticlesBefore(String s, String s1, long l) {

    }

    @Override
    protected void onSavedArticles(List<String> list, boolean b) {

    }

    private void getTimeline() throws TwitterException {

    }


}
