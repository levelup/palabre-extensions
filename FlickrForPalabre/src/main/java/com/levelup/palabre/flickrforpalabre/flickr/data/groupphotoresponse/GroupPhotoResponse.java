
package com.levelup.palabre.flickrforpalabre.flickr.data.groupphotoresponse;

import com.google.gson.annotations.Expose;

public class GroupPhotoResponse {

    @Expose
    private Photos photos;
    @Expose
    private String stat;

    /**
     * @return The photos
     */
    public Photos getPhotos() {
        return photos;
    }

    /**
     * @param photos The photos
     */
    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    /**
     * @return The stat
     */
    public String getStat() {
        return stat;
    }

    /**
     * @param stat The stat
     */
    public void setStat(String stat) {
        this.stat = stat;
    }

}
