package com.levelup.palabre.flickrforpalabre;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;

import com.levelup.palabre.api.ExtensionAccountInfo;
import com.levelup.palabre.api.ExtensionUpdateStatus;
import com.levelup.palabre.api.PalabreExtension;
import com.levelup.palabre.api.datamapping.Article;
import com.levelup.palabre.api.datamapping.Category;
import com.levelup.palabre.api.datamapping.Source;
import com.levelup.palabre.api.utils.DBUtils;
import com.levelup.palabre.flickrforpalabre.data.SourceType;
import com.levelup.palabre.flickrforpalabre.data.UniqueIds;
import com.levelup.palabre.flickrforpalabre.flickr.FlickrService;
import com.levelup.palabre.flickrforpalabre.flickr.FlickrServiceInterface;
import com.levelup.palabre.flickrforpalabre.flickr.FlickrUtils;
import com.levelup.palabre.flickrforpalabre.flickr.data.Interestingness;
import com.levelup.palabre.flickrforpalabre.flickr.data.contactresponse.Contact;
import com.levelup.palabre.flickrforpalabre.flickr.data.contactresponse.ContactResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.favorites.FavoritesResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.groupphotoresponse.GroupPhotoResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.groupphotoresponse.Photo;
import com.levelup.palabre.flickrforpalabre.flickr.data.groupresponse.Group;
import com.levelup.palabre.flickrforpalabre.flickr.data.groupresponse.GroupResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.photoinfo.PhotoInfo;
import com.levelup.palabre.flickrforpalabre.flickr.data.photoinfo.Url;
import com.levelup.palabre.flickrforpalabre.flickr.data.userphoto.UserPhoto;
import com.levelup.palabre.flickrforpalabre.flickr.utils.SharedPreferenceKeys;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by nicolas on 10/04/15.
 */
public class FlickrExtension extends PalabreExtension {
    private static final String TAG = FlickrExtension.class.getSimpleName();
    private static final String UNIQUE_ID_CONTACT = "Contacts";
    private static final String UNIQUE_ID_GROUPS = "Groups";
    private int remainingTasks = 0;
    private int totalTaskSource;
    private HashMap<Source, Pair<Integer, Integer>> sourceProgress = new HashMap<>();

    @Override
    protected void onUpdateData() {

        if (BuildConfig.DEBUG) Log.d(TAG, "onUpdateData: ");
        if (remainingTasks == 0) {
            loadData();
        }

    }

    @Override
    protected void onReadArticles(List<String> articles, boolean value) {
        //We do nothing as there is no "read" option on the server
    }

    @Override
    protected void onReadArticlesBefore(String s, String s1, long l) {
        //We do nothing as there is no "read" option on the server
    }

    @Override
    protected void onSavedArticles(List<String> articles, boolean value) {

        if (BuildConfig.DEBUG) Log.d(TAG, "Marking as saved: " + value + " - " + articles.size());
        for (String article : articles) {
            if (value) {
                FlickrService.getInstance(FlickrExtension.this).addFavorite(article);
            } else {
                FlickrService.getInstance(FlickrExtension.this).removeFavorite(article);
            }
        }
    }

    private void loadData() {
        publishUpdateStatus(new ExtensionUpdateStatus().start());

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        publishAccountInfo(new ExtensionAccountInfo()
                        .accountAvatar(sp.getString(SharedPreferenceKeys.LOGIN_AVATAR, ""))
                        .accountName(sp.getString(SharedPreferenceKeys.LOGIN_USERNAME, ""))
        );

        loadSources(this, new OnTaskFinishedListener() {
            @Override
            public void onTaskFinished() {
                loadContent();
            }
        });
//        publishUpdateStatus(new ExtensionUpdateStatus().refreshStatus(RefreshStatus.STOP).progress(100));
//
//        DBUtils.exportDBToFile(this);
    }

    private void loadContent() {
        List<Source> allSources = Source.getAllWithCategories(this);

        remainingTasks = 0;
        totalTaskSource = allSources.size();

        for (final Source allSource : allSources) {
            sourceProgress.put(allSource, new Pair<>(0, 0));

            Set<Category> cats = allSource.getCategories();
            SourceType sourceType = SourceType.CONTACT;
            for (Category cat : cats) {
                if (cat.getUniqueId().equals(UniqueIds.CATEGORY_GROUPS)) {
                    sourceType = SourceType.GROUP;
                }
                if (cat.getUniqueId().equals(UniqueIds.CATEGORY_OTHERS)) {
                    sourceType = SourceType.OTHERS;
                }
            }

            remainingTasks++;

            if (sourceType == SourceType.CONTACT) {


                FlickrService.getInstance(this).getPopularPhotosByUser(allSource.getUniqueId(), 1, new FlickrServiceInterface.IRequestListener<UserPhoto>() {
                    @Override
                    public void onFailure() {
                        removeTaskAndFinishIfNeeded();
                        sourceProgress.get(allSource);
                        sendProgress();

                    }

                    @Override
                    public void onSuccess(final UserPhoto response) {
                        if (response == null || response.getPhotos() == null || response.getPhotos().getPhoto().length == 0) {
                            removeTaskAndFinishIfNeeded();
                            sourceProgress.put(allSource, new Pair<>(1, 1));

                            sendProgress();

                            return;
                        }

                        sourceProgress.put(allSource, new Pair<>(response.getPhotos().getPhoto().length, 0));

                        for (final Photo photo : response.getPhotos().getPhoto()) {

                            loadPhoto(photo, allSource, response.getPhotos().getPhoto().length, false);

                            if (BuildConfig.DEBUG)
                                Log.d(TAG, "Response: " + FlickrUtils.photoUrl(photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret()));
                        }

                        removeTaskAndFinishIfNeeded();
                    }
                });
            } else if (sourceType == SourceType.GROUP) {
                FlickrService.getInstance(this).getGroupPhotos(allSource.getUniqueId(), 1, new FlickrServiceInterface.IRequestListener<GroupPhotoResponse>() {
                    @Override
                    public void onFailure() {
                        removeTaskAndFinishIfNeeded();
                        sourceProgress.get(allSource);
                        sendProgress();

                    }

                    @Override
                    public void onSuccess(final GroupPhotoResponse response) {
                        if (response == null || response.getPhotos() == null || response.getPhotos().getPhoto().size() == 0) {
                            removeTaskAndFinishIfNeeded();
                            sourceProgress.put(allSource, new Pair<>(1, 1));

                            sendProgress();

                            return;
                        }

                        sourceProgress.put(allSource, new Pair<>(response.getPhotos().getPhoto().size(), 0));

                        for (final Photo photo : response.getPhotos().getPhoto()) {

                            loadPhoto(photo, allSource, response.getPhotos().getPhoto().size(), false);


                            if (BuildConfig.DEBUG)
                                Log.d(TAG, "Response: " + FlickrUtils.photoUrl(photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret()));
                        }

                        removeTaskAndFinishIfNeeded();
                    }
                });
            } else if (sourceType == SourceType.OTHERS) {
                FlickrService.getInstance(this).getInterestingness(new FlickrServiceInterface.IRequestListener<Interestingness>() {
                    @Override
                    public void onFailure() {
                        removeTaskAndFinishIfNeeded();
                        sourceProgress.get(allSource);
                        sendProgress();

                    }

                    @Override
                    public void onSuccess(final Interestingness response) {
                        if (response == null || response.getPhotos() == null || response.getPhotos().getPhoto().length == 0) {
                            removeTaskAndFinishIfNeeded();
                            sourceProgress.put(allSource, new Pair<>(1, 1));

                            sendProgress();

                            return;
                        }

                        sourceProgress.put(allSource, new Pair<>(response.getPhotos().getPhoto().length, 0));

                        for (final Photo photo : response.getPhotos().getPhoto()) {

                            loadPhoto(photo, allSource, response.getPhotos().getPhoto().length, false);


                            if (BuildConfig.DEBUG)
                                Log.d(TAG, "Response: " + FlickrUtils.photoUrl(photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret()));
                        }

                        removeTaskAndFinishIfNeeded();
                    }
                });
            }
        }

        //load favorites
        remainingTasks++;
        FlickrService.getInstance(this).getFavorites(new FlickrServiceInterface.IRequestListener<FavoritesResponse>() {
            @Override
            public void onFailure() {

                removeTaskAndFinishIfNeeded();
            }

            @Override
            public void onSuccess(FavoritesResponse response) {


                Source favoriteSource = new Source().title("Favorites").uniqueId("favs");
                favoriteSource.save(FlickrExtension.this);

                if (response == null || response.getPhotos() == null || response.getPhotos().getPhoto().size() == 0) {
                    removeTaskAndFinishIfNeeded();
                    sourceProgress.put(favoriteSource, new Pair<>(1, 1));

                    sendProgress();

                    return;
                }

                sourceProgress.put(favoriteSource, new Pair<>(response.getPhotos().getPhoto().size(), 0));

                for (final Photo photo : response.getPhotos().getPhoto()) {

                    loadPhoto(photo, favoriteSource, response.getPhotos().getPhoto().size(), true);


                }

                removeTaskAndFinishIfNeeded();
            }
        });

    }

    public void loadPhoto(final Photo photo, final Source allSource, final int sourceLength, final boolean saved) {
        remainingTasks++;
        FlickrService.getInstance(FlickrExtension.this).getPhotoInfo(photo.getId(), new FlickrServiceInterface.IRequestListener<PhotoInfo>() {
            @Override
            public void onFailure() {
                removeTaskAndFinishIfNeeded();
                sourceProgress.put(allSource, new Pair<>(sourceLength, sourceProgress.get(allSource).second + 1));
                sendProgress();
            }

            @Override
            public void onSuccess(PhotoInfo response) {

                String linkUrl = "";
                for (Url url : response.getPhoto().getUrls().getUrl()) {
                    if (url.getType().equals("photopage")) {
                        linkUrl = url.getContent();
                    }
                }

                String licenseText = FlickrUtils.getLicenseText(FlickrExtension.this, response.getPhoto().getLicense());

                new Article()
                        .author(photo.getOwner())
                        .content(response.getPhoto().getDescription().getContent() + licenseText)
                        .title(photo.getTitle())
                        .date(new Date(Long.valueOf(response.getPhoto().getDates().getPosted())))
                        .image(FlickrUtils.photoUrl(String.valueOf(response.getPhoto().getFarm()), response.getPhoto().getServer(), response.getPhoto().getId(), response.getPhoto().getSecret()))
                        .sourceId(allSource.getId())
                        .uniqueId(photo.getId())
                        .linkUrl(linkUrl)
                        .saved(saved)
                        .save(FlickrExtension.this);
                sourceProgress.put(allSource, new Pair<>(sourceProgress.get(allSource).first, sourceProgress.get(allSource).second + 1));
                sendProgress();
                removeTaskAndFinishIfNeeded();

            }
        });
    }

    public static void loadSources(final Context context, final OnTaskFinishedListener listener) {

        List<Category> categories = Category.getAll(context);

        Category contactsCategory = null;
        Category groupsCategory = null;
        for (Category category : categories) {
            if (category.getUniqueId().equals(UNIQUE_ID_CONTACT)) {
                contactsCategory = category;
            } else if (category.getUniqueId().equals(UNIQUE_ID_GROUPS)) {
                groupsCategory = category;
            }
        }


        if (groupsCategory == null) {
            groupsCategory = new Category().title(context.getString(R.string.groups)).uniqueId(UNIQUE_ID_GROUPS);
            groupsCategory.save(context);

        }
        if (contactsCategory == null) {
            contactsCategory = new Category().title("Contacts").uniqueId(UNIQUE_ID_CONTACT);
            contactsCategory.save(context);

        }


        final int[] remaining = {2};

        //Load contacts
        final Category finalContactsCategory = contactsCategory;
        FlickrService.getInstance(context).getContacts(new FlickrServiceInterface.IRequestListener<ContactResponse>() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(ContactResponse response) {


                Set<Category> categories = new HashSet<Category>();
                categories.add(finalContactsCategory);


                Set<Source> sources = new HashSet<>();

                for (Contact contact : response.getContacts().getContact()) {

                    Source source = new Source()
                            .title(contact.getUsername())
                            .iconUrl("http://farm" + contact.getIconfarm() + ".staticflickr.com/" + contact.getIconserver() + "/buddyicons/" + contact.getNsid() + ".jpg")
                            .uniqueId(contact.getNsid())
                            .categories(categories);
                    sources.add(source);

                }

                Source.multipleSave(context, sources);
                remaining[0]--;
                if (remaining[0] == 0) {
                    listener.onTaskFinished();
                }

            }
        });

        //Load groups
        final Category finalGroupsCategory = groupsCategory;
        FlickrService.getInstance(context).getGroupsByUser(PreferenceManager.getDefaultSharedPreferences(context).getString(SharedPreferenceKeys.LOGIN_NSID, ""), new FlickrServiceInterface.IRequestListener<GroupResponse>() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(GroupResponse response) {
                if (response == null || response.getGroups() == null) {
                    return;
                }


                Set<Category> categories = new HashSet<>();
                categories.add(finalGroupsCategory);


                Set<Source> sources = new HashSet<>();


                for (Group group : response.getGroups().getGroup()) {

                    Source source = new Source()
                            .title(group.getName())
                            .iconUrl("http://farm" + group.getIconfarm() + ".staticflickr.com/" + group.getIconserver() + "/buddyicons/" + group.getNsid() + ".jpg")
                            .uniqueId(group.getNsid())
                            .categories(categories);
                    sources.add(source);
                }

                Source.multipleSave(context, sources);
                remaining[0]--;
                if (remaining[0] == 0) {
                    listener.onTaskFinished();
                }
            }
        });
    }

    private void sendProgress() {

        int finished = 0;
        for (Pair<Integer, Integer> integerIntegerPair : sourceProgress.values()) {
            if (integerIntegerPair.first != 0 && integerIntegerPair.first.equals(integerIntegerPair.second)) {
                finished++;
            }
        }

        int progress = (int) (finished * 100f / (float) sourceProgress.size());
        publishUpdateStatus(new ExtensionUpdateStatus().progress(progress));

    }

    private void removeTaskAndFinishIfNeeded() {
        remainingTasks--;
        if (BuildConfig.DEBUG) Log.d(TAG, "Remaining tasks: " + remainingTasks);
        if (remainingTasks == 0) {
            publishUpdateStatus(new ExtensionUpdateStatus().stop());
            DBUtils.exportDBToFile(FlickrExtension.this);
        }
    }


    public interface OnTaskFinishedListener {
        void onTaskFinished();
    }
}
