
package com.levelup.palabre.flickrforpalabre.flickr.data.photoinfo;

import com.google.gson.annotations.Expose;

public class Dates {

    @Expose
    private String posted;
    @Expose
    private String taken;
    @Expose
    private Integer takengranularity;
    @Expose
    private Integer takenunknown;
    @Expose
    private String lastupdate;

    /**
     * 
     * @return
     *     The posted
     */
    public String getPosted() {
        return posted;
    }

    /**
     * 
     * @param posted
     *     The posted
     */
    public void setPosted(String posted) {
        this.posted = posted;
    }

    /**
     * 
     * @return
     *     The taken
     */
    public String getTaken() {
        return taken;
    }

    /**
     * 
     * @param taken
     *     The taken
     */
    public void setTaken(String taken) {
        this.taken = taken;
    }

    /**
     * 
     * @return
     *     The takengranularity
     */
    public Integer getTakengranularity() {
        return takengranularity;
    }

    /**
     * 
     * @param takengranularity
     *     The takengranularity
     */
    public void setTakengranularity(Integer takengranularity) {
        this.takengranularity = takengranularity;
    }

    /**
     * 
     * @return
     *     The takenunknown
     */
    public Integer getTakenunknown() {
        return takenunknown;
    }

    /**
     * 
     * @param takenunknown
     *     The takenunknown
     */
    public void setTakenunknown(Integer takenunknown) {
        this.takenunknown = takenunknown;
    }

    /**
     * 
     * @return
     *     The lastupdate
     */
    public String getLastupdate() {
        return lastupdate;
    }

    /**
     * 
     * @param lastupdate
     *     The lastupdate
     */
    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

}
