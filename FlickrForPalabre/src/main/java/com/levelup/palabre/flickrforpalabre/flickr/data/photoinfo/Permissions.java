
package com.levelup.palabre.flickrforpalabre.flickr.data.photoinfo;

import com.google.gson.annotations.Expose;

public class Permissions {

    @Expose
    private Integer permcomment;
    @Expose
    private Integer permaddmeta;

    /**
     * 
     * @return
     *     The permcomment
     */
    public Integer getPermcomment() {
        return permcomment;
    }

    /**
     * 
     * @param permcomment
     *     The permcomment
     */
    public void setPermcomment(Integer permcomment) {
        this.permcomment = permcomment;
    }

    /**
     * 
     * @return
     *     The permaddmeta
     */
    public Integer getPermaddmeta() {
        return permaddmeta;
    }

    /**
     * 
     * @param permaddmeta
     *     The permaddmeta
     */
    public void setPermaddmeta(Integer permaddmeta) {
        this.permaddmeta = permaddmeta;
    }

}
