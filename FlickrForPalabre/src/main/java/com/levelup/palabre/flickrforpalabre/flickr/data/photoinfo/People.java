
package com.levelup.palabre.flickrforpalabre.flickr.data.photoinfo;

import com.google.gson.annotations.Expose;

public class People {

    @Expose
    private Integer haspeople;

    /**
     * 
     * @return
     *     The haspeople
     */
    public Integer getHaspeople() {
        return haspeople;
    }

    /**
     * 
     * @param haspeople
     *     The haspeople
     */
    public void setHaspeople(Integer haspeople) {
        this.haspeople = haspeople;
    }

}
