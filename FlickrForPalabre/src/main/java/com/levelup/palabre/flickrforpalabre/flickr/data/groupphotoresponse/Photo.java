
package com.levelup.palabre.flickrforpalabre.flickr.data.groupphotoresponse;

import com.google.gson.annotations.Expose;

public class Photo {

    @Expose
    private String id;
    @Expose
    private String owner;
    @Expose
    private String secret;
    @Expose
    private String server;
    @Expose
    private String farm;
    @Expose
    private String title;
    @Expose
    private Integer ispublic;
    @Expose
    private Integer isfriend;
    @Expose
    private Integer isfamily;
    @Expose
    private String ownername;
    @Expose
    private String dateadded;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner The owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return The secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * @param secret The secret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * @return The server
     */
    public String getServer() {
        return server;
    }

    /**
     * @param server The server
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @return The farm
     */
    public String getFarm() {
        return farm;
    }

    /**
     * @param farm The farm
     */
    public void setFarm(String farm) {
        this.farm = farm;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The ispublic
     */
    public Integer getIspublic() {
        return ispublic;
    }

    /**
     * @param ispublic The ispublic
     */
    public void setIspublic(Integer ispublic) {
        this.ispublic = ispublic;
    }

    /**
     * @return The isfriend
     */
    public Integer getIsfriend() {
        return isfriend;
    }

    /**
     * @param isfriend The isfriend
     */
    public void setIsfriend(Integer isfriend) {
        this.isfriend = isfriend;
    }

    /**
     * @return The isfamily
     */
    public Integer getIsfamily() {
        return isfamily;
    }

    /**
     * @param isfamily The isfamily
     */
    public void setIsfamily(Integer isfamily) {
        this.isfamily = isfamily;
    }

    /**
     * @return The ownername
     */
    public String getOwnername() {
        return ownername;
    }

    /**
     * @param ownername The ownername
     */
    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    /**
     * @return The dateadded
     */
    public String getDateadded() {
        return dateadded;
    }

    /**
     * @param dateadded The dateadded
     */
    public void setDateadded(String dateadded) {
        this.dateadded = dateadded;
    }

}
