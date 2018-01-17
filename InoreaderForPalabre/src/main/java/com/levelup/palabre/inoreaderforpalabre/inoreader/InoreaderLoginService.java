package com.levelup.palabre.inoreaderforpalabre.inoreader;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class InoreaderLoginService {


    private static final String TAG = InoreaderLoginService.class.getSimpleName();
    public static final String API_ENDPOINT = "https://www.inoreader.com/accounts/";
    private static InoreaderLoginService INSTANCE;
    private final Context context;
    private Retrofit mRestAdapter;

    private InoreaderLoginService(Context context) {
        this.context = context;
    }


    public static InoreaderLoginService getInstance(Context context) {
        if (INSTANCE == null) INSTANCE = new InoreaderLoginService(context);
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
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(httpClient.build());

            mRestAdapter = builder.build();

        }

        return mRestAdapter.create(InoreaderLoginServiceInterface.class);
    }




    public Call<String> login(String username, String password) {





        return getService().login(username, password);
    }





    public void resetAdapter() {
        mRestAdapter = null;
    }
}
