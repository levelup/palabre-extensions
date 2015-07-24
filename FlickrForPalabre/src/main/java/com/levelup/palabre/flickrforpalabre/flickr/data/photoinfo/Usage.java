
package com.levelup.palabre.flickrforpalabre.flickr.data.photoinfo;

import com.google.gson.annotations.Expose;

public class Usage {

    @Expose
    private Integer candownload;
    @Expose
    private Integer canblog;
    @Expose
    private Integer canprint;
    @Expose
    private Integer canshare;

    /**
     * 
     * @return
     *     The candownload
     */
    public Integer getCandownload() {
        return candownload;
    }

    /**
     * 
     * @param candownload
     *     The candownload
     */
    public void setCandownload(Integer candownload) {
        this.candownload = candownload;
    }

    /**
     * 
     * @return
     *     The canblog
     */
    public Integer getCanblog() {
        return canblog;
    }

    /**
     * 
     * @param canblog
     *     The canblog
     */
    public void setCanblog(Integer canblog) {
        this.canblog = canblog;
    }

    /**
     * 
     * @return
     *     The canprint
     */
    public Integer getCanprint() {
        return canprint;
    }

    /**
     * 
     * @param canprint
     *     The canprint
     */
    public void setCanprint(Integer canprint) {
        this.canprint = canprint;
    }

    /**
     * 
     * @return
     *     The canshare
     */
    public Integer getCanshare() {
        return canshare;
    }

    /**
     * 
     * @param canshare
     *     The canshare
     */
    public void setCanshare(Integer canshare) {
        this.canshare = canshare;
    }

}
