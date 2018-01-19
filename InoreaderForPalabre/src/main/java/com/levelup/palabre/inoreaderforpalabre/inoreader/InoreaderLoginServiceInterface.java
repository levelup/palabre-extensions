package com.levelup.palabre.inoreaderforpalabre.inoreader;


import com.levelup.palabre.inoreaderforpalabre.inoreader.data.auth.OAuthToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface InoreaderLoginServiceInterface {

    @FormUrlEncoded
    @POST("token")
    Call<OAuthToken> login(@Field("code") String code, @Field("redirect_uri") String redirectUri, @Field("client_id") String clientId, @Field("client_secret") String clientSecret, @Field("scope") String scope, @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("token")
    Call<OAuthToken> refreshToken(@Field("refresh_token") String refreshToken, @Field("client_id") String clientId, @Field("client_secret") String clientSecret, @Field("grant_type") String grantType);

}
