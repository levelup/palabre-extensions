package com.levelup.palabre.inoreaderforpalabre.inoreader;

import android.content.Context;

import com.levelup.palabre.inoreaderforpalabre.BuildConfig;
import com.levelup.palabre.inoreaderforpalabre.retrofit.StringConverter;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class InoreaderLoginService {


    private static final String TAG = InoreaderLoginService.class.getSimpleName();
    public static final String API_ENDPOINT = "https://www.inoreader.com/accounts/ClientLogin";
    private static InoreaderLoginService INSTANCE;
    private final Context context;
    private RestAdapter mRestAdapter;

    private InoreaderLoginService(Context context) {
        this.context = context;
    }


    public static InoreaderLoginService getInstance(Context context) {
        if (INSTANCE == null) INSTANCE = new InoreaderLoginService(context);
        return INSTANCE;
    }

    private InoreaderLoginServiceInterface getService() {
        if (mRestAdapter == null) {

            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(API_ENDPOINT)
                    .setConverter(new StringConverter())
                    .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE);

            mRestAdapter = builder.build();

        }

        return mRestAdapter.create(InoreaderLoginServiceInterface.class);
    }




    public void login(String username, String password, final InoreaderLoginServiceInterface.IRequestListener<String> listener) {


        getService().login(username, password, new Callback<String>() {
            @Override
            public void success(String photosResponse, Response response) {
                listener.onSuccess(photosResponse);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                listener.onFailure();
            }
        });
    }





    public void resetAdapter() {
        mRestAdapter = null;
    }
}
