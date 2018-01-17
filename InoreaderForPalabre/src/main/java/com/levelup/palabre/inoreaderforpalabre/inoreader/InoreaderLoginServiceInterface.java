package com.levelup.palabre.inoreaderforpalabre.inoreader;


import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InoreaderLoginServiceInterface {

    @POST("ClientLogin")
    Call<String> login(@Query("Email") String username, @Query("Passwd") String password);

    public interface IRequestListener<T> {
        void onFailure();

        void onSuccess(T response);
    }
}
