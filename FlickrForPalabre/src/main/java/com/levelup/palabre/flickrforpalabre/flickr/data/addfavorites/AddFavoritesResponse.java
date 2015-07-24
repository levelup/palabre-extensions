
package com.levelup.palabre.flickrforpalabre.flickr.data.addfavorites;

import com.google.gson.annotations.Expose;

public class AddFavoritesResponse {

    @Expose
    private String stat;

    /**
     * 
     * @return
     *     The stat
     */
    public String getStat() {
        return stat;
    }

    /**
     * 
     * @param stat
     *     The stat
     */
    public void setStat(String stat) {
        this.stat = stat;
    }

}
