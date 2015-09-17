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
import com.levelup.twitterforpalabre.core.twitter.TwitterUtil;
import com.levelup.twitterforpalabre.core.utils.SharedPreferencesKeys;

import java.util.ArrayList;
import java.util.List;

import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
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


    public static final String TAG = TwitterProvider.class.getSimpleName();
    public static final String HOME_UNIQUE_ID = "home";

    @Override
    protected void onUpdateData() {

        if (BuildConfig.DEBUG) Log.d(TAG, "Palabre ask for a refresh");
        publishUpdateStatus(new ExtensionUpdateStatus().start());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String accessTokenString = sharedPreferences.getString(TwitterUtil.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
        String accessTokenSecret = sharedPreferences.getString(TwitterUtil.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
        if (BuildConfig.DEBUG) Log.d(TAG, "User is back " + accessTokenString);

        AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
        TwitterUtil.getInstance().setTwitterFactory(accessToken);

        publishUpdateStatus(new ExtensionUpdateStatus().progress(5));

        try {
            if (BuildConfig.DEBUG) Log.d(TAG, "Getting twitter user info");
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
        final Category homeCat = Category.getByUniqueId(this, HOME_UNIQUE_ID);
        if (homeCat == null) {
            Category category = new Category();
            category.setUniqueId(HOME_UNIQUE_ID);
            category.setTitle("Home");

            // add it to the list
            categories.add(category);
            category.save(this);
        } else {
            categories.add(homeCat);

        }
        List<Category> allCategories = null;
        long lastId = 0;
        ArrayList<Article> articles = new ArrayList<>();



        if (sharedPreferences.getBoolean(SharedPreferencesKeys.SYNC_LISTS, false)) {
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

            allCategories = Category.getAll(this);


            publishUpdateStatus(new ExtensionUpdateStatus().progress(25));

            // used to know where to start from when getting new tweets
            List<Article> oldTweets = Article.getAll(this);
            lastId = -1;
            for (Article article : oldTweets) {
                lastId = Math.max(lastId, Long.valueOf(article.getUniqueId()));
            }



            // Twitter Lists
            for (long id : userListsIds) {
                Paging paging = new Paging().count(200);
                if (lastId > 0) {
                    paging.setSinceId(lastId);
                }
                try {
                    ResponseList<Status> userListStatuses = TwitterUtil.getInstance().getTwitter().getUserListStatuses(id, paging);

                    Category currentCategory = null;
                    for (Category category : allCategories) {
                        if (category.getUniqueId().equals(String.valueOf(id))) {
                            currentCategory = category;
                        }
                    }
                    if (currentCategory == null) throw new IllegalStateException("Category cannot be null");

                    saveSources(currentCategory, userListStatuses);

                    publishUpdateStatus(new ExtensionUpdateStatus().progress(25));
                    List<Source> sourcesInDB = Source.getAll(this);

                    saveTweets(articles, userListStatuses, sourcesInDB);

                    //Article.multipleSave(this, articles);
                    publishUpdateStatus(new ExtensionUpdateStatus().progress(35));
                } catch (TwitterException e) {
                    e.printStackTrace();
                }

            }
        } else {
            allCategories = Category.getAll(this);
            for (Category category : allCategories) {
                if (!category.getUniqueId().equals(HOME_UNIQUE_ID) && !category.getUniqueId().startsWith("s/")) {
                    category.delete(this);
                }
            }
        }
        /*// Wipe the old cats
        for (Category catToRemove : Category.getAll(this)) {
            catToRemove.delete(this);
        }*/

        if (BuildConfig.DEBUG) Log.d(TAG, "Now get the twitter home timeline");

        // Home Timeline



        try {
            Paging paging = new Paging().count(200);
            if (lastId > 0) {
                paging.setSinceId(lastId);
            }
            List<Status> statuses = TwitterUtil.getInstance().getTwitter().getHomeTimeline(paging);
            //ArrayList<Article> articles = new ArrayList<>();

            Category category = Category.getByUniqueId(this, HOME_UNIQUE_ID);
            if (category == null) throw new IllegalStateException("Category cannot be null");

            publishUpdateStatus(new ExtensionUpdateStatus().progress(45));
            saveSources(category, statuses);


            publishUpdateStatus(new ExtensionUpdateStatus().progress(55));



            List<Source> sourcesInDB = Source.getAll(this);
            if (BuildConfig.DEBUG) Log.d(TAG, "Now get the twitter home timeline status and write them in DB");


            saveTweets(articles, statuses, sourcesInDB);



        } catch (TwitterException e) {
            e.printStackTrace();
        }


        for (Category category : allCategories) {
            if (category.getUniqueId().startsWith("s/")) {
                String search = category.getUniqueId().substring(2);
                if (BuildConfig.DEBUG) Log.d(TAG, "Search: "+search);


                try {

                    Query query = new Query(search);
                    query.setCount(15);
                    QueryResult result = TwitterUtil.getInstance().getTwitter().search(query);

                    publishUpdateStatus(new ExtensionUpdateStatus().progress(65));

                    saveSources(category, result.getTweets());
                    //ArrayList<Article> articles = new ArrayList<>();


                    publishUpdateStatus(new ExtensionUpdateStatus().progress(75));



                    List<Source> sourcesInDB = Source.getAll(this);
                    if (BuildConfig.DEBUG) Log.d(TAG, "Now get the twitter home timeline status and write them in DB");
                    saveTweets(articles, result.getTweets(), sourcesInDB);



                } catch (TwitterException e) {
                    e.printStackTrace();
                }


            }
        }






        // save all what we got
        Article.multipleSave(this, articles);
        if (BuildConfig.DEBUG) Log.d(TAG, "All articles saved");

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
                if (BuildConfig.DEBUG) Log.d(TAG, "Found user to remove: " + allSource.getTitle());
                usersToRemove.add(allSource);
            }
        }

        for (Source src : usersToRemove) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Removing: " + src.getTitle());
            src.delete(this);
        }
        publishUpdateStatus(new ExtensionUpdateStatus().progress(100));
        if (BuildConfig.DEBUG) Log.d(TAG, "Done!");

        publishUpdateStatus(new ExtensionUpdateStatus().stop());

    }

    private void saveTweets(ArrayList<Article> articles, List<Status> userListStatuses, List<Source> sourcesInDB) {
        for (Status status : userListStatuses) {

            //System.out.println(status.getUser().getName() + ":" + status.getText());
            Article tweet = new Article();
            tweet.setUniqueId(String.valueOf(status.getId()));
            tweet.setTitle(status.getText());
            tweet.setAuthor("@" + status.getUser().getScreenName());
            tweet.setDate(status.getCreatedAt());

            if (BuildConfig.DEBUG) Log.d(TAG, "List: UniqueID: " + status.getId());
            tweet.setUniqueId(String.valueOf(status.getId()));
            for (Source source : sourcesInDB) {
                if (String.valueOf(status.getUser().getId()).equals(source.getUniqueId())) {
                    if (BuildConfig.DEBUG) Log.d(TAG, "List: User match! " + status.getUser().getName());
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
    }

    private void saveSources(Category category, List<Status> userListStatuses) {
        ArrayList<Source> sources = new ArrayList<>();
        for (Status status : userListStatuses) {
            boolean sourceExistInDb = false;
            Source source;
            source = Source.getByUniqueId(this, String.valueOf(status.getUser().getId()));
            if (source == null) {
                source = new Source();
                //source.getCategories().add(Category.getByUniqueId(this, "home"));
            } else {
                if (BuildConfig.DEBUG) Log.d(TAG, "List: Source is already in db: " + status.getUser().getName());
                sourceExistInDb = true;

            }

            source.getCategories().add(category);
            source.setUniqueId(String.valueOf(status.getUser().getId()));
            source.setIconUrl(status.getUser().getProfileImageURL());
            source.setTitle(status.getUser().getName());

            if (sourceExistInDb) {
                source.save(this);
            } else {
                boolean found = false;
                for (Source newSource : sources) {
                    if (newSource.getUniqueId().equals(String.valueOf(status.getUser().getId()))) {
                        if (BuildConfig.DEBUG) Log.d(TAG, "List: Source is already about to be added and is in temp array: " + status.getUser().getName());
                        found = true;
                    }
                }
                if (!found) {
                    if (BuildConfig.DEBUG) Log.d(TAG, "List: New source was not in temp array, add it: " + status.getUser().getName());
                    sources.add(source);
                }
            }
        }

        Source.multipleSave(this, sources);
    }

    private void getFriends(AccessToken accessToken, ArrayList<Category> categories, ArrayList<Source> sources, long cursor) throws TwitterException {

        PagableResponseList<User> friendsList = TwitterUtil.getInstance().getTwitter().getFriendsList(accessToken.getUserId(), cursor, 50);
        if (BuildConfig.DEBUG) Log.d(TAG, "Friends: " + friendsList.size());
        for (User user : friendsList) {
            Source source = new Source();
            source.getCategories().add(categories.get(0));
            source.setUniqueId(String.valueOf(user.getId()));
            source.setIconUrl(user.getProfileImageURL());
            source.setTitle(user.getName());
            sources.add(source);
            if (BuildConfig.DEBUG) Log.d(TAG, "Add friend " + user.getName());
        }
        if (friendsList.hasNext()) {
            if (BuildConfig.DEBUG) Log.d(TAG, "It's better with more friends, let's continue...");
            getFriends(accessToken, categories, sources, friendsList.getNextCursor());
        } else {
            if (BuildConfig.DEBUG) Log.d(TAG, "No more friends.");
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
