
package com.levelup.palabre.flickrforpalabre.flickr.data.photoinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comments {

    @SerializedName("_content")
    @Expose
    private Integer Content;

    /**
     * 
     * @return
     *     The Content
     */
    public Integer getContent() {
        return Content;
    }

    /**
     * 
     * @param Content
     *     The _content
     */
    public void setContent(Integer Content) {
        this.Content = Content;
    }

}
