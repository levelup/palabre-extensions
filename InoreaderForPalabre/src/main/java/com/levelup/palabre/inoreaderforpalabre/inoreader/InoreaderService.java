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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    private InoreaderService(Context context) {
        this.context = context;
    }

    public static InoreaderService getInstance(Context context) {
        if (INSTANCE == null) INSTANCE = new InoreaderService(context);
        return INSTANCE;
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
        final String token = settings.getString(SharedPreferenceKeys.TOKEN, "");
        if (!TextUtils.isEmpty(token)) {

            httpClient.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder requestBuilder = chain.request().newBuilder();



                    requestBuilder.addHeader("Authorization", "GoogleLogin auth=" + token);
                    requestBuilder.addHeader("AppId", InoreaderKeys.APP_ID);
                    requestBuilder.addHeader("AppKey", InoreaderKeys.APP_KEY);
                    return chain.proceed(requestBuilder.build());
                }
            });

            if (BuildConfig.DEBUG) Log.d(TAG, "Using signed request");
        }
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

    public Call<String> removeSourceFromCat( String sourceId, String categoryId) {


        return getServiceString().removeSourceFromCat(sourceId, categoryId);
    }

    public Call<String> unsubscribeSource( String sourceId) {


        return getServiceString().unsubscribeSource(sourceId);
    }

    public Call<String> deleteCategory( String categoryId) {


        return getServiceString().deleteCategory(categoryId);
    }

    public Call<String> moveSource( String sourceId, String removeCategory, String addActegory) {


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
