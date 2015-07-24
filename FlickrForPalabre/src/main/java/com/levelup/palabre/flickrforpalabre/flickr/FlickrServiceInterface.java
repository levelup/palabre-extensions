package com.levelup.palabre.flickrforpalabre.flickr;

import com.levelup.palabre.flickrforpalabre.flickr.data.Interestingness;
import com.levelup.palabre.flickrforpalabre.flickr.data.UserLoginResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.contactresponse.ContactResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.favorites.FavoritesResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.groupphotoresponse.GroupPhotoResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.groupresponse.GroupResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.license.LicenseResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.photoinfo.PhotoInfo;
import com.levelup.palabre.flickrforpalabre.flickr.data.userphoto.UserPhoto;
import com.levelup.palabre.flickrforpalabre.flickr.data.userresponse.UserResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface FlickrServiceInterface {

    @GET("/?&method=flickr.people.getPhotos&per_page=5&format=json&privacy_filter=1&api_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getPopularPhotosByUser(@Query("user_id") String userId, @Query("page") int page, Callback<UserPhoto> callback);

    @GET("/?&method=flickr.people.getInfo&format=json&api_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getUser(@Query("user_id") String owner, Callback<UserResponse> callback);

    @GET("/?&method=flickr.photos.search&per_page=5&safe_search=1&sort=interestingness-desc&format=json&license=1,2,3,4,5,6,7&privacy_filter=1&api_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getPopularPhotos(@Query("text") String text, @Query("page") int page, Callback<FlickrApiData.PhotosResponse> callback);

    @GET("/?&method=flickr.photos.search&per_page=5&safe_search=1&sort=interestingness-desc&format=json&license=1,2,3,4,5,6,7&privacy_filter=1&api_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getPopularPhotosByTag(@Query("tags") String text, @Query("page") int page, Callback<FlickrApiData.PhotosResponse> callback);

    @GET("/?&method=flickr.groups.search&format=json&per_page=50&api_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getGroups(@Query("text") String text, Callback<FlickrApiData.GroupsResponse> callback);

    @GET("/?&method=flickr.groups.pools.getPhotos&format=json&per_page=5&api_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getGroupPhotos(@Query("group_id") String groupId, @Query("page") int page, Callback<GroupPhotoResponse> callback);

    @GET("/?&method=flickr.test.login&format=json&oauth_consumer_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getLogin(Callback<UserLoginResponse> callback);

    @GET("/?&method=flickr.photos.getSizes&format=json&api_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getSize(@Query("photo_id") String photo, Callback<FlickrApiData.SizeResponse> callback);

    @GET("/?&method=flickr.people.findByUsername&format=json&api_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getUserByName(@Query("username") String user, Callback<FlickrApiData.UserByNameResponse> callback);

    @GET("/?&method=flickr.contacts.getList&format=json&oauth_consumer_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getContacts(Callback<ContactResponse> callback);

    @GET("/?&method=flickr.favorites.getList&per_page=500&format=json&oauth_consumer_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getFavorites(Callback<FavoritesResponse> callback);

    @POST("/?&method=flickr.favorites.add&format=json&oauth_consumer_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void addFavorite(@Query("photo_id") String article,Callback<PhotoInfo> callback);

    @POST("/?&method=flickr.favorites.remove&format=json&oauth_consumer_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void removeFavorite(@Query("photo_id") String article,Callback<PhotoInfo> callback);

    @GET("/?&method=flickr.people.getGroups&format=json&oauth_consumer_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getGroupsByUser(@Query("user_id") String nsid, Callback<GroupResponse> callback);

    @GET("/?&method=flickr.interestingness.getList&format=json&api_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getInterestingNess(Callback<Interestingness> callback);

    @GET("/?&method=flickr.photos.getInfo&format=json&api_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getPhotoInfo(@Query("photo_id") String photo,Callback<PhotoInfo> callback);

    @GET("/?&method=flickr.photos.licenses.getInfo&format=json&api_key=" + Config.CONSUMER_KEY + "&nojsoncallback=1")
    void getLicenceInfo(Callback<LicenseResponse> callback);

    public interface IRequestListener<T> {
        void onFailure();

        void onSuccess(T response);
    }
}
