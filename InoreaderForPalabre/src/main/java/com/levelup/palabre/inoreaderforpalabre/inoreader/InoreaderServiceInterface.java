package com.levelup.palabre.inoreaderforpalabre.inoreader;

import com.levelup.palabre.inoreaderforpalabre.inoreader.data.SubscriptionList;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.addsubscription.AddSubscriptionResponse;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.folderlist.FolderList;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.streamcontent.StreamContent;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.userinfo.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface InoreaderServiceInterface {

    @GET("subscription/list")
    Call<SubscriptionList> getSubscriptionList();

    @GET("preference/stream/list")
    Call<String> getStreamPreferenceList();

    @GET("tag/list")
    Call<FolderList> getFolderList();

    @GET("stream/contents/user%2F-%2Fstate%2Fcom.google%2Freading-list?n=1000")
    Call<StreamContent> getStreamContent(@Query("ot") long limitDate, @Query("xt") String exclude);

    @GET("stream/contents/user%2F-%2Fstate%2Fcom.google%2Freading-list?n=1000")
    Call<StreamContent> getStreamContent(@Query("c") String continuation);

    @GET("stream/contents/user%2F-%2Fstate%2Fcom.google%2Freading-list?n=1000&it=user/-/state/com.google/read")
    Call<StreamContent> getStreamContentRead();

    @GET("stream/contents/user%2F-%2Fstate%2Fcom.google%2Freading-list?n=1000&it=user/-/state/com.google/starred")
    Call<StreamContent> getStreamContentStarred();

    @GET("user-info")
    Call<UserInfo> getUserInfo();

    @POST("edit-tag?a=user/-/state/com.google/read")
    Call<String> markAsRead(@Query("i") List<String> parameters);

    @POST("mark-all-as-read")
    Call<String> markAsReadBefore(@Query("ts") long timestamp, @Query("s") String stream);

    @POST("edit-tag?r=user/-/state/com.google/read")
    Call<String> markAsUnread(@Query("i") List<String> parameters);

    @POST("edit-tag?a=user/-/state/com.google/starred")
    Call<String> markAsSaved(@Query("i") List<String> parameters);

    @POST("edit-tag?r=user/-/state/com.google/starred")
    Call<String> markAsUnsaved(@Query("i") List<String> parameters);

    @POST("subscription/edit?ac=edit")
    Call<String> removeSourceFromCat(@Query("s") String source, @Query("r") String category);

    @POST("subscription/edit?ac=edit")
    Call<String> moveSource(@Query("s") String source, @Query("r") String removeCategory, @Query("a") String addCategory);

    @POST("subscription/edit?ac=unsubscribe")
    Call<String> unsubscribeSource(@Query("s") String source);

    @POST("disable-tag")
    Call<String> deleteCategory(@Query("s") String source);

    @POST("subscription/quickadd")
    Call<AddSubscriptionResponse> addSubscription(@Query("quickadd") String source);
//
//    @GET("/?&method=flickr.people.getInfo&format=json&api_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
//    Call<> getUser(@Query("user_id") String owner, Callback<UserResponse> callback);
//

}
