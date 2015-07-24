
package com.levelup.palabre.flickrforpalabre.flickr.data.photoinfo;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Urls {

    @Expose
    private List<Url> url = new ArrayList<Url>();

    /**
     * 
     * @return
     *     The url
     */
    public List<Url> getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(List<Url> url) {
        this.url = url;
    }

}
