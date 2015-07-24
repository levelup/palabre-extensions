package com.levelup.palabre.flickrforpalabre.flickr.data.photoinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {

    @Expose
    private String id;
    @Expose
    private String secret;
    @Expose
    private String server;
    @Expose
    private Integer farm;
    @Expose
    private String dateuploaded;
    @Expose
    private Integer isfavorite;
    @Expose
    private Integer license;
    @SerializedName("safety_level")
    @Expose
    private Integer safetyLevel;
    @Expose
    private Integer rotation;
    @Expose
    private String originalsecret;
    @Expose
    private String originalformat;
    @Expose
    private Owner owner;
    @Expose
    private Title title;
    @Expose
    private Description description;
    @Expose
    private Visibility visibility;
    @Expose
    private Dates dates;
    @Expose
    private Permissions permissions;
    @Expose
    private Integer views;
    @Expose
    private Editability editability;
    @Expose
    private Publiceditability publiceditability;
    @Expose
    private Usage usage;
    @Expose
    private Comments comments;
    @Expose
    private Notes notes;
    @Expose
    private People people;
    @Expose
    private Tags tags;
    @Expose
    private Urls urls;
    @Expose
    private String media;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The secret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * 
     * @param secret
     *     The secret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * 
     * @return
     *     The server
     */
    public String getServer() {
        return server;
    }

    /**
     * 
     * @param server
     *     The server
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * 
     * @return
     *     The farm
     */
    public Integer getFarm() {
        return farm;
    }

    /**
     * 
     * @param farm
     *     The farm
     */
    public void setFarm(Integer farm) {
        this.farm = farm;
    }

    /**
     * 
     * @return
     *     The dateuploaded
     */
    public String getDateuploaded() {
        return dateuploaded;
    }

    /**
     * 
     * @param dateuploaded
     *     The dateuploaded
     */
    public void setDateuploaded(String dateuploaded) {
        this.dateuploaded = dateuploaded;
    }

    /**
     * 
     * @return
     *     The isfavorite
     */
    public Integer getIsfavorite() {
        return isfavorite;
    }

    /**
     * 
     * @param isfavorite
     *     The isfavorite
     */
    public void setIsfavorite(Integer isfavorite) {
        this.isfavorite = isfavorite;
    }

    /**
     * 
     * @return
     *     The license
     */
    public Integer getLicense() {
        return license;
    }

    /**
     * 
     * @param license
     *     The license
     */
    public void setLicense(Integer license) {
        this.license = license;
    }

    /**
     * 
     * @return
     *     The safetyLevel
     */
    public Integer getSafetyLevel() {
        return safetyLevel;
    }

    /**
     * 
     * @param safetyLevel
     *     The safety_level
     */
    public void setSafetyLevel(Integer safetyLevel) {
        this.safetyLevel = safetyLevel;
    }

    /**
     * 
     * @return
     *     The rotation
     */
    public Integer getRotation() {
        return rotation;
    }

    /**
     * 
     * @param rotation
     *     The rotation
     */
    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    /**
     * 
     * @return
     *     The originalsecret
     */
    public String getOriginalsecret() {
        return originalsecret;
    }

    /**
     * 
     * @param originalsecret
     *     The originalsecret
     */
    public void setOriginalsecret(String originalsecret) {
        this.originalsecret = originalsecret;
    }

    /**
     * 
     * @return
     *     The originalformat
     */
    public String getOriginalformat() {
        return originalformat;
    }

    /**
     * 
     * @param originalformat
     *     The originalformat
     */
    public void setOriginalformat(String originalformat) {
        this.originalformat = originalformat;
    }

    /**
     * 
     * @return
     *     The owner
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * 
     * @param owner
     *     The owner
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * 
     * @return
     *     The title
     */
    public Title getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(Title title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The description
     */
    public Description getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(Description description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The visibility
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * 
     * @param visibility
     *     The visibility
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * 
     * @return
     *     The dates
     */
    public Dates getDates() {
        return dates;
    }

    /**
     * 
     * @param dates
     *     The dates
     */
    public void setDates(Dates dates) {
        this.dates = dates;
    }

    /**
     * 
     * @return
     *     The permissions
     */
    public Permissions getPermissions() {
        return permissions;
    }

    /**
     * 
     * @param permissions
     *     The permissions
     */
    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    /**
     * 
     * @return
     *     The views
     */
    public Integer getViews() {
        return views;
    }

    /**
     * 
     * @param views
     *     The views
     */
    public void setViews(Integer views) {
        this.views = views;
    }

    /**
     * 
     * @return
     *     The editability
     */
    public Editability getEditability() {
        return editability;
    }

    /**
     * 
     * @param editability
     *     The editability
     */
    public void setEditability(Editability editability) {
        this.editability = editability;
    }

    /**
     * 
     * @return
     *     The publiceditability
     */
    public Publiceditability getPubliceditability() {
        return publiceditability;
    }

    /**
     * 
     * @param publiceditability
     *     The publiceditability
     */
    public void setPubliceditability(Publiceditability publiceditability) {
        this.publiceditability = publiceditability;
    }

    /**
     * 
     * @return
     *     The usage
     */
    public Usage getUsage() {
        return usage;
    }

    /**
     * 
     * @param usage
     *     The usage
     */
    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    /**
     * 
     * @return
     *     The comments
     */
    public Comments getComments() {
        return comments;
    }

    /**
     * 
     * @param comments
     *     The comments
     */
    public void setComments(Comments comments) {
        this.comments = comments;
    }

    /**
     * 
     * @return
     *     The notes
     */
    public Notes getNotes() {
        return notes;
    }

    /**
     * 
     * @param notes
     *     The notes
     */
    public void setNotes(Notes notes) {
        this.notes = notes;
    }

    /**
     * 
     * @return
     *     The people
     */
    public People getPeople() {
        return people;
    }

    /**
     * 
     * @param people
     *     The people
     */
    public void setPeople(People people) {
        this.people = people;
    }

    /**
     * 
     * @return
     *     The tags
     */
    public Tags getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *     The tags
     */
    public void setTags(Tags tags) {
        this.tags = tags;
    }

    /**
     * 
     * @return
     *     The urls
     */
    public Urls getUrls() {
        return urls;
    }

    /**
     * 
     * @param urls
     *     The urls
     */
    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    /**
     * 
     * @return
     *     The media
     */
    public String getMedia() {
        return media;
    }

    /**
     * 
     * @param media
     *     The media
     */
    public void setMedia(String media) {
        this.media = media;
    }

}