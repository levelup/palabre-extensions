package com.levelup.palabre.flickrforpalabre.flickr;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.levelup.palabre.flickrforpalabre.BuildConfig;
import com.levelup.palabre.flickrforpalabre.flickr.data.Interestingness;
import com.levelup.palabre.flickrforpalabre.flickr.data.UserLoginResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.contactresponse.ContactResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.favorites.FavoritesResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.groupphotoresponse.GroupPhotoResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.groupresponse.GroupResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.license.LicenseResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.photoinfo.PhotoInfo;
import com.levelup.palabre.flickrforpalabre.flickr.data.userphoto.UserPhoto;
import com.levelup.palabre.flickrforpalabre.flickr.data.userresponse.UserResponse;
import com.levelup.palabre.flickrforpalabre.flickr.utils.SharedPreferenceKeys;
import com.levelup.palabre.flickrforpalabre.network.retrofitsigner.RetrofitHttpOAuthConsumer;
import com.levelup.palabre.flickrforpalabre.network.retrofitsigner.SigningOkClient;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FlickrService {


    private static final String TAG = FlickrService.class.getSimpleName();
    private static FlickrService INSTANCE;
    private final Context context;
    private RestAdapter mRestAdapter;

    private FlickrService(Context context) {
        this.context = context;
    }


    public static FlickrService getInstance(Context context) {
        if (INSTANCE == null) INSTANCE = new FlickrService(context);
        return INSTANCE;
    }

    private FlickrServiceInterface getService() {
        if (mRestAdapter == null) {

            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint("https://api.flickr.com/services/rest")
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE);

            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            String token = settings.getString(SharedPreferenceKeys.LOGIN_TOKEN, "");
            String secret = settings.getString(SharedPreferenceKeys.LOGIN_SECRET, "");
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(secret)) {
                RetrofitHttpOAuthConsumer oAuthConsumer = new RetrofitHttpOAuthConsumer(Config.CONSUMER_KEY, Config.CONSUMER_SECRET);
                oAuthConsumer.setTokenWithSecret(token, secret);
                builder.setClient(new SigningOkClient(oAuthConsumer));
                if (BuildConfig.DEBUG) Log.d(TAG, "Using signed request");
            }


            mRestAdapter = builder.build();

        }

        return mRestAdapter.create(FlickrServiceInterface.class);
    }


    public void getPopularPhotos(String text, int page, final FlickrServiceInterface.IRequestListener<FlickrApiData.PhotosResponse> listener) {


        getService().getPopularPhotos(text, page, new Callback<FlickrApiData.PhotosResponse>() {
            @Override
            public void success(FlickrApiData.PhotosResponse photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }

    public void getPopularPhotosByUser(String text, int page, final FlickrServiceInterface.IRequestListener<UserPhoto> listener) {
        getService().getPopularPhotosByUser(text, page, new Callback<UserPhoto>() {
            @Override
            public void success(UserPhoto photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }

    public void getUser(String id, final FlickrServiceInterface.IRequestListener<UserResponse> listener) {
        getService().getUser(id, new Callback<UserResponse>() {
            @Override
            public void success(UserResponse userResponse, Response response) {
                listener.onSuccess(userResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }

    public void getSize(String photo, final FlickrServiceInterface.IRequestListener<FlickrApiData.SizeResponse> listener) {
        getService().getSize(photo, new Callback<FlickrApiData.SizeResponse>() {
            @Override
            public void success(FlickrApiData.SizeResponse sizeResponse, Response response) {
                listener.onSuccess(sizeResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }


    public void getUserByName(String user, final FlickrServiceInterface.IRequestListener<FlickrApiData.UserByNameResponse> listener) {
        getService().getUserByName(user, new Callback<FlickrApiData.UserByNameResponse>() {
            @Override
            public void success(FlickrApiData.UserByNameResponse sizeResponse, Response response) {
                listener.onSuccess(sizeResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }


    public void getPopularPhotosByTag(String text, int page, final FlickrServiceInterface.IRequestListener<FlickrApiData.PhotosResponse> listener) {
        getService().getPopularPhotosByTag(text, page, new Callback<FlickrApiData.PhotosResponse>() {
            @Override
            public void success(FlickrApiData.PhotosResponse photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }

    public void getGroups(String text, final FlickrServiceInterface.IRequestListener<FlickrApiData.GroupsResponse> listener) {
        getService().getGroups(text, new Callback<FlickrApiData.GroupsResponse>() {
            @Override
            public void success(FlickrApiData.GroupsResponse photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }

    public void getGroupPhotos(String groupId, int page, final FlickrServiceInterface.IRequestListener<GroupPhotoResponse> listener) {
        getService().getGroupPhotos(groupId, page, new Callback<GroupPhotoResponse>() {
            @Override
            public void success(GroupPhotoResponse photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }

    public void getLogin( final FlickrServiceInterface.IRequestListener<UserLoginResponse> listener) {
        getService().getLogin(new Callback<UserLoginResponse>() {
            @Override
            public void success(UserLoginResponse userLoginResponse, Response response) {
                listener.onSuccess(userLoginResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }

    public void getInterestingness(final FlickrServiceInterface.IRequestListener<Interestingness> listener) {
        getService().getInterestingNess(new Callback<Interestingness>() {
            @Override
            public void success(Interestingness photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }

    public void getContacts(final FlickrServiceInterface.IRequestListener<ContactResponse> listener) {
        getService().getContacts(new Callback<ContactResponse>() {
            @Override
            public void success(ContactResponse contactResponse, Response response) {
                listener.onSuccess(contactResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }

    public void getGroupsByUser(String nsid, final FlickrServiceInterface.IRequestListener<GroupResponse> listener) {
        getService().getGroupsByUser(nsid, new Callback<GroupResponse>() {
            @Override
            public void success(GroupResponse groupsResponse, Response response) {
                listener.onSuccess(groupsResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }


    public void getPhotoInfo(String nsid, final FlickrServiceInterface.IRequestListener<PhotoInfo> listener) {
        getService().getPhotoInfo(nsid, new Callback<PhotoInfo>() {
            @Override
            public void success(PhotoInfo photoInfo, Response response) {
                listener.onSuccess(photoInfo);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }

    public void getLicenses(final FlickrServiceInterface.IRequestListener<LicenseResponse> listener) {
        getService().getLicenceInfo( new Callback<LicenseResponse>() {
            @Override
            public void success(LicenseResponse photoInfo, Response response) {
                listener.onSuccess(photoInfo);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }

    public void getFavorites(final FlickrServiceInterface.IRequestListener<FavoritesResponse> listener) {
        getService().getFavorites(new Callback<FavoritesResponse>() {
            @Override
            public void success(FavoritesResponse favoritesResponse, Response response) {
                listener.onSuccess(favoritesResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }


    public void addFavorite(String article) {
        getService().addFavorite(article, new Callback<PhotoInfo>() {
            @Override
            public void success(PhotoInfo photoInfo, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void removeFavorite(String article) {
        getService().removeFavorite(article, new Callback<PhotoInfo>() {
            @Override
            public void success(PhotoInfo photoInfo, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    public void resetAdapter() {
        mRestAdapter = null;
    }
}
