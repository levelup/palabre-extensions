package com.levelup.palabre.inoreaderforpalabre.inoreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.levelup.palabre.inoreaderforpalabre.BuildConfig;
import com.levelup.palabre.inoreaderforpalabre.core.SharedPreferenceKeys;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.SubscriptionList;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.addsubscription.AddSubscriptionResponse;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.folderlist.FolderList;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.streamcontent.StreamContent;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.userinfo.UserInfo;
import com.levelup.palabre.inoreaderforpalabre.retrofit.SigningOkClient;
import com.levelup.palabre.inoreaderforpalabre.retrofit.StringConverter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class InoreaderService {


    private static final String TAG = InoreaderService.class.getSimpleName();
    public static final String API_ENDPOINT = "https://www.inoreader.com/reader/api/0";
    private static InoreaderService INSTANCE;
    private final Context context;
    private RestAdapter mRestAdapter;
    private RestAdapter mRestAdapterStringConverter;

    private InoreaderService(Context context) {
        this.context = context;
    }

    public static InoreaderService getInstance(Context context) {
        if (INSTANCE == null) INSTANCE = new InoreaderService(context);
        return INSTANCE;
    }

    private InoreaderServiceInterface getService() {
        if (mRestAdapter == null) {

            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(API_ENDPOINT)
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE);

            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            String token = settings.getString(SharedPreferenceKeys.TOKEN, "");
//            String secret = settings.getString(SharedPreferenceKeys.LOGIN_SECRET, "");
            if (!TextUtils.isEmpty(token)) {
                builder.setClient(new SigningOkClient(token));
                if (BuildConfig.DEBUG) Log.d(TAG, "Using signed request");
            }


            mRestAdapter = builder.build();

        }

        return mRestAdapter.create(InoreaderServiceInterface.class);
    }

    private InoreaderServiceInterface getServiceStringConverter() {
        if (mRestAdapterStringConverter == null) {

            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(API_ENDPOINT)
                    .setConverter(new StringConverter())
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE);

            final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            String token = settings.getString(SharedPreferenceKeys.TOKEN, "");
//            String secret = settings.getString(SharedPreferenceKeys.LOGIN_SECRET, "");
            if (!TextUtils.isEmpty(token)) {
                builder.setClient(new SigningOkClient(token));
                if (BuildConfig.DEBUG) Log.d(TAG, "Using signed request");
            }


            mRestAdapterStringConverter = builder.build();

        }

        return mRestAdapterStringConverter.create(InoreaderServiceInterface.class);
    }


    public void getFolderList(final InoreaderServiceInterface.IRequestListener<FolderList> listener) {


        getService().getFolderList(new Callback<FolderList>() {
            @Override
            public void success(FolderList photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure(retrofitError);
            }
        });
    }

    public void getSubscriptionList(final InoreaderServiceInterface.IRequestListener<SubscriptionList> listener) {


        getService().getSubscriptionList(new Callback<SubscriptionList>() {
            @Override
            public void success(SubscriptionList photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure(retrofitError);
            }
        });
    }

    public void getStreamPreferenceList(final InoreaderServiceInterface.IRequestListener<String> listener) {


        getServiceStringConverter().getStreamPreferenceList(new Callback<String>() {
            @Override
            public void success(String photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure(retrofitError);
            }
        });
    }


    public StreamContent getStreamContent(String continuation) {


        return getService().getStreamContent(continuation);
    }

    public void getStreamContentRead(final InoreaderServiceInterface.IRequestListener<StreamContent> listener) {

        String userid = PreferenceManager.getDefaultSharedPreferences(context).getString(SharedPreferenceKeys.USER_ID, "");

        getService().getStreamContentRead(new Callback<StreamContent>() {
            @Override
            public void success(StreamContent photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure(retrofitError);
            }
        });
    }

    public void getStreamContentStarred(final InoreaderServiceInterface.IRequestListener<StreamContent> listener) {


        getService().getStreamContentStarred(new Callback<StreamContent>() {
            @Override
            public void success(StreamContent photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure(retrofitError);
            }
        });
    }



    public void resetAdapters() {
        mRestAdapter = null;
        mRestAdapterStringConverter = null;
    }

    public void getUserInformation(final InoreaderServiceInterface.IRequestListener<UserInfo> listener) {


        getService().getUserInfo(new Callback<UserInfo>() {
            @Override
            public void success(UserInfo photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure(retrofitError);
            }
        });
    }

    public void removeSourceFromCat( String sourceId, String categoryId, final InoreaderServiceInterface.IRequestListener<String> listener) {


        getServiceStringConverter().removeSourceFromCat(sourceId, categoryId, new Callback<String>() {
            @Override
            public void success(String photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure(retrofitError);
            }
        });
    }

    public void unsubscribeSource( String sourceId, final InoreaderServiceInterface.IRequestListener<String> listener) {


        getServiceStringConverter().unsubscribeSource(sourceId, new Callback<String>() {
            @Override
            public void success(String photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure(retrofitError);
            }
        });
    }

    public void deleteCategory( String categoryId, final InoreaderServiceInterface.IRequestListener<String> listener) {


        getServiceStringConverter().deleteCategory(categoryId, new Callback<String>() {
            @Override
            public void success(String photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure(retrofitError);
            }
        });
    }

    public void moveSource( String sourceId, String removeCategory, String addActegory, final InoreaderServiceInterface.IRequestListener<String> listener) {


        getServiceStringConverter().moveSource(sourceId, removeCategory, addActegory, new Callback<String>() {
            @Override
            public void success(String photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure(retrofitError);
            }
        });
    }

    public void markAsRead(List<String> articles) {



        getServiceStringConverter().markAsRead(getParams(articles), new Callback<String>() {
            @Override
            public void success(String photosResponse, Response response) {
            }

            @Override
            public void failure(RetrofitError retrofitError) {
            }
        });

    }

    private List<String> getParams(List<String> articles) {
        List<String> params = new ArrayList<>();
        for (String article : articles) {
            String[] ids = article.split("/");
            params.add(ids[ids.length - 1]);

        }
        return params;
    }

    public void markAsUnread(List<String> articles) {

        getServiceStringConverter().markAsUnread(getParams(articles), new Callback<String>() {
            @Override
            public void success(String photosResponse, Response response) {
            }

            @Override
            public void failure(RetrofitError retrofitError) {
            }
        });
    }

    public void markAsReadBefore(long timestamp, String uniqueId) {

        getServiceStringConverter().markAsReadBefore(timestamp, uniqueId, new Callback<String>() {
            @Override
            public void success(String photosResponse, Response response) {
            }

            @Override
            public void failure(RetrofitError retrofitError) {
            }
        });
    }

    public void markAsSaved(List<String> articles) {

        getServiceStringConverter().markAsSaved(getParams(articles), new Callback<String>() {
            @Override
            public void success(String photosResponse, Response response) {
            }

            @Override
            public void failure(RetrofitError retrofitError) {
            }
        });
    }

    public void markAsUnsaved(List<String> articles) {

        getServiceStringConverter().markAsUnsaved(getParams(articles), new Callback<String>() {
            @Override
            public void success(String photosResponse, Response response) {
            }

            @Override
            public void failure(RetrofitError retrofitError) {
            }
        });
    }

    public void addSubscription(String sourceId, final InoreaderServiceInterface.IRequestListener<AddSubscriptionResponse> listener) {


        getService().addSubscription(sourceId, new Callback<AddSubscriptionResponse>() {
            @Override
            public void success(AddSubscriptionResponse addSubscriptionResponse, Response response) {
                listener.onSuccess(addSubscriptionResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure(retrofitError);
            }
        });
    }
}
