package com.levelup.palabre.inoreaderforpalabre.inoreader;

import com.levelup.palabre.inoreaderforpalabre.inoreader.data.SubscriptionList;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.addsubscription.AddSubscriptionResponse;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.folderlist.FolderList;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.streamcontent.StreamContent;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.userinfo.UserInfo;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface InoreaderServiceInterface {

    @GET("/subscription/list")
    void getSubscriptionList(Callback<SubscriptionList> callback);

    @GET("/preference/stream/list")
    void getStreamPreferenceList(Callback<String> callback);

    @GET("/tag/list")
    void getFolderList(Callback<FolderList> callback);

    @GET("/stream/contents/user%2F-%2Fstate%2Fcom.google%2Freading-list?n=1000")
    void getStreamContent(@Query("ot") long limitDate, @Query("xt") String exclude, Callback<StreamContent> callback);

    @GET("/stream/contents/user%2F-%2Fstate%2Fcom.google%2Freading-list?n=1000")
    StreamContent getStreamContent(@Query("c") String continuation);

    @GET("/stream/contents/user%2F-%2Fstate%2Fcom.google%2Freading-list?n=1000&it=user/-/state/com.google/read")
    void getStreamContentRead(Callback<StreamContent> callback);

    @GET("/stream/contents/user%2F-%2Fstate%2Fcom.google%2Freading-list?n=1000&it=user/-/state/com.google/starred")
    void getStreamContentStarred(Callback<StreamContent> callback);

    @GET("/user-info")
    void getUserInfo(Callback<UserInfo> callback);

    @POST("/edit-tag?a=user/-/state/com.google/read")
    void markAsRead(@Query("i") List<String> parameters, Callback<String> callback);

    @POST("/edit-tag?r=user/-/state/com.google/read")
    void markAsUnread(@Query("i") List<String> parameters, Callback<String> callback);

    @POST("/edit-tag?a=user/-/state/com.google/starred")
    void markAsSaved(@Query("i") List<String> parameters, Callback<String> callback);

    @POST("/edit-tag?r=user/-/state/com.google/starred")
    void markAsUnsaved(@Query("i") List<String> parameters, Callback<String> callback);

    @POST("/subscription/edit?ac=edit")
    void removeSourceFromCat(@Query("s") String source, @Query("r") String category, Callback<String> callback);

    @POST("/subscription/edit?ac=edit")
    void moveSource(@Query("s") String source, @Query("r") String removeCategory, @Query("a") String addCategory, Callback<String> callback);

    @POST("/subscription/edit?ac=unsubscribe")
    void unsubscribeSource(@Query("s") String source, Callback<String> callback);

    @POST("/disable-tag")
    void deleteCategory(@Query("s") String source, Callback<String> callback);

    @POST("/subscription/quickadd")
    void addSubscription(@Query("quickadd") String source, Callback<AddSubscriptionResponse> callback);
//
//    @GET("/?&method=flickr.people.getInfo&format=json&api_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
//    void getUser(@Query("user_id") String owner, Callback<UserResponse> callback);
//

    public interface IRequestListener<T> {
        void onFailure(RetrofitError retrofitError);

        void onSuccess(T response);
    }
}
