package com.levelup.palabre.inoreaderforpalabre.inoreader;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

public interface InoreaderLoginServiceInterface {

    @POST("/")
    void login(@Query("Email") String username, @Query("Passwd") String password, Callback<String> callback);

    public interface IRequestListener<T> {
        void onFailure();

        void onSuccess(T response);
    }
}
