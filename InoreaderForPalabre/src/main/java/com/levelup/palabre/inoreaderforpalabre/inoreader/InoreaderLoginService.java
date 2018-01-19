package com.levelup.palabre.inoreaderforpalabre.inoreader;

import android.content.Context;
import android.preference.PreferenceManager;

import com.levelup.palabre.inoreaderforpalabre.core.SharedPreferenceKeys;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.auth.OAuthToken;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class InoreaderLoginService {


    private static final String TAG = InoreaderLoginService.class.getSimpleName();
    public static final String API_ENDPOINT = "https://www.inoreader.com/oauth2/";
    private static InoreaderLoginService INSTANCE;
    private final Context context;
    private Retrofit mRestAdapter;

    private InoreaderLoginService(Context context) {
        this.context = context;
    }


    public static InoreaderLoginService getInstance(Context context) {
        if (INSTANCE == null) INSTANCE = new InoreaderLoginService(context.getApplicationContext());
        return INSTANCE;
    }

    private InoreaderLoginServiceInterface getService() {
        if (mRestAdapter == null) {


            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line!




            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(API_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build());

            mRestAdapter = builder.build();

        }

        return mRestAdapter.create(InoreaderLoginServiceInterface.class);
    }




    public Call<OAuthToken> login(String code) {
        return getService().login(code, "palabre-inoreader://auth", InoreaderKeys.APP_ID, InoreaderKeys.APP_KEY, "", "authorization_code");
    }

    public Call<OAuthToken> refreshToken() {
        String refreshToken = PreferenceManager.getDefaultSharedPreferences(context).getString(SharedPreferenceKeys.REFRESH_TOKEN, "");


        return getService().refreshToken(refreshToken, InoreaderKeys.APP_ID, InoreaderKeys.APP_KEY, "refresh_token");
    }





    public void resetAdapter() {
        mRestAdapter = null;
    }
}
