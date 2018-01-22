package com.levelup.palabre.inoreaderforpalabre.inoreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.levelup.palabre.inoreaderforpalabre.BuildConfig;
import com.levelup.palabre.inoreaderforpalabre.core.SharedPreferenceKeys;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.SubscriptionList;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.addsubscription.AddSubscriptionResponse;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.auth.OAuthToken;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.folderlist.FolderList;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.streamcontent.StreamContent;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.userinfo.UserInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class InoreaderService {


    private static final String TAG = InoreaderService.class.getSimpleName();
    public static final String API_ENDPOINT = "https://www.inoreader.com/reader/api/0/";
    private static InoreaderService INSTANCE;
    private final Context context;
    private Retrofit mRestAdapter;
    private Retrofit mRestAdapterStringConverter;
    private String accessToken;
    private String token;

    private InoreaderService(Context context) {
        this.context = context;
    }

    public static InoreaderService getInstance(Context context) {
        if (INSTANCE == null) INSTANCE = new InoreaderService(context.getApplicationContext());
        return INSTANCE;
    }

    public static Uri getAuthCodeUri(String csrf) {
        return Uri.parse("https://www.inoreader.com/oauth2/auth").buildUpon()
                .appendQueryParameter("client_id", InoreaderKeys.APP_ID)
                .appendQueryParameter("redirect_uri", "palabre-inoreader://auth")
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("state", csrf)
                .build();
    }


    private InoreaderServiceInterface getService() {
        if (mRestAdapter == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            buildOkhttpClient(httpClient);


            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(API_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(httpClient.build());


            mRestAdapter = builder.build();

        }

        return mRestAdapter.create(InoreaderServiceInterface.class);
    }

    private InoreaderServiceInterface getServiceString() {
        if (mRestAdapterStringConverter == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            buildOkhttpClient(httpClient);


            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(API_ENDPOINT)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(httpClient.build());


            mRestAdapterStringConverter = builder.build();

        }

        return mRestAdapterStringConverter.create(InoreaderServiceInterface.class);
    }

    private void buildOkhttpClient(OkHttpClient.Builder httpClient) {
        //Logging
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(logging);

        //Auth
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        accessToken = settings.getString(SharedPreferenceKeys.ACCESS_TOKEN, "");
        token = settings.getString(SharedPreferenceKeys.TOKEN, "");


        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder();

                if (!TextUtils.isEmpty(accessToken)) {
                    if (BuildConfig.DEBUG) Log.d(TAG, "Using access token");
                    requestBuilder.addHeader("Authorization", "Bearer " + accessToken);
                } else {
                    //Fallback to old auth system
                    if (BuildConfig.DEBUG) Log.d(TAG, "Using token");
                    requestBuilder.addHeader("Authorization", "GoogleLogin auth=" + token);
                    requestBuilder.addHeader("AppId", InoreaderKeys.APP_ID);
                    requestBuilder.addHeader("AppKey", InoreaderKeys.APP_KEY);
                }


                Response origResponse = chain.proceed(requestBuilder.build());


                if (origResponse.code() == 403) {

                    refreshToken();

                    // make a new request with the new token
                    Request newAuthenticationRequest = originalRequest.newBuilder()
                            .addHeader("Authorization", "Bearer " + accessToken)
                            .build();

                    // try again

                    return chain.proceed(newAuthenticationRequest);
                } else {
                    return origResponse;
                }
            }
        });

        httpClient.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                final String bodyString = response.body().string();


                if (responseCount(response) >= 3) {
                    return null; // If we've failed 3 times, give up.
                }

                if (refreshToken()) {
                    return response.request().newBuilder()
                            .build();
                }
                return null;
            }

        });

    }

    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }


    private boolean refreshToken() {
        if (BuildConfig.DEBUG) Log.d(TAG, "REFRESHING TOKEN");
        try {

            Call<OAuthToken> call = InoreaderLoginService.getInstance(context).refreshToken();
            retrofit2.Response<OAuthToken> response = call.execute();

            if (response.isSuccessful()) {
                if (BuildConfig.DEBUG) Log.d(TAG, "TOKEN REFRESHED");
                final OAuthToken oAuthToken = response.body();
                accessToken = oAuthToken.getAccessToken();


                if (BuildConfig.DEBUG) Log.d(TAG, "Expires in: " + oAuthToken.getExpiresIn());

                if (BuildConfig.DEBUG) Log.d(TAG, "Saving token: " + oAuthToken.getAccessToken());
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "Saving refresh token: " + oAuthToken.getRefreshToken());


                long expiration = System.currentTimeMillis() + (oAuthToken.getExpiresIn() * 1000);
                if (BuildConfig.DEBUG) Log.d(TAG, "Expires: " + new Date(expiration));


                PreferenceManager.getDefaultSharedPreferences(context)
                        .edit()
                        .putString(SharedPreferenceKeys.ACCESS_TOKEN, oAuthToken.getAccessToken())
                        .putString(SharedPreferenceKeys.REFRESH_TOKEN, oAuthToken.getRefreshToken())
                        .putLong(SharedPreferenceKeys.EXPIRES, expiration)
                        .apply();

                return true;
            } else {
                if (BuildConfig.DEBUG) Log.d(TAG, response.errorBody().string());
            }


        } catch (IOException e) {
            if (BuildConfig.DEBUG) Log.w(TAG, e.getMessage(), e);
        }
        return false;

    }


    public Call<FolderList> getFolderList() {


        return getService().getFolderList();
    }

    public Call<SubscriptionList> getSubscriptionList() {


        return getService().getSubscriptionList();
    }

    public Call<String> getStreamPreferenceList() {


        return getServiceString().getStreamPreferenceList();
    }


    public Call<StreamContent> getStreamContent(String continuation) {


        return getService().getStreamContent(continuation);
    }

    public Call<StreamContent> getStreamContentRead() {

        String userid = PreferenceManager.getDefaultSharedPreferences(context).getString(SharedPreferenceKeys.USER_ID, "");

        return getService().getStreamContentRead();
    }

    public Call<StreamContent> getStreamContentStarred() {


        return getService().getStreamContentStarred();
    }


    public void resetAdapters() {
        mRestAdapter = null;
        mRestAdapterStringConverter = null;
    }

    public Call<UserInfo> getUserInformation() {


        return getService().getUserInfo();
    }

    public Call<String> removeSourceFromCat(String sourceId, String categoryId) {


        return getServiceString().removeSourceFromCat(sourceId, categoryId);
    }

    public Call<String> unsubscribeSource(String sourceId) {


        return getServiceString().unsubscribeSource(sourceId);
    }

    public Call<String> deleteCategory(String categoryId) {


        return getServiceString().deleteCategory(categoryId);
    }

    public Call<String> moveSource(String sourceId, String removeCategory, String addActegory) {


        return getServiceString().moveSource(sourceId, removeCategory, addActegory);
    }

    public Call<String> markAsRead(List<String> articles) {


        return getServiceString().markAsRead(getParams(articles));

    }

    private List<String> getParams(List<String> articles) {
        List<String> params = new ArrayList<>();
        for (String article : articles) {
            String[] ids = article.split("/");
            params.add(ids[ids.length - 1]);

        }
        return params;
    }

    public Call<String> markAsUnread(List<String> articles) {

        return getServiceString().markAsUnread(getParams(articles));
    }

    public Call<String> markAsReadBefore(long timestamp, String uniqueId) {

        return getServiceString().markAsReadBefore(timestamp, uniqueId);
    }

    public Call<String> markAsSaved(List<String> articles) {

        return getServiceString().markAsSaved(getParams(articles));
    }

    public Call<String> markAsUnsaved(List<String> articles) {

        return getServiceString().markAsUnsaved(getParams(articles));
    }

    public Call<AddSubscriptionResponse> addSubscription(String sourceId) {


        return getService().addSubscription(sourceId);
    }
}
