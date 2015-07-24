package com.levelup.palabre.flickrforpalabre.flickr.data.photoinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag {

    @Expose
    private String id;
    @Expose
    private String author;
    @Expose
    private String authorname;
    @Expose
    private String raw;
    @SerializedName("_content")
    @Expose
    private String Content;
    @SerializedName("machine_tag")
    @Expose
    private Integer machineTag;

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
     *     The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 
     * @param author
     *     The author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 
     * @return
     *     The authorname
     */
    public String getAuthorname() {
        return authorname;
    }

    /**
     * 
     * @param authorname
     *     The authorname
     */
    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    /**
     * 
     * @return
     *     The raw
     */
    public String getRaw() {
        return raw;
    }

    /**
     * 
     * @param raw
     *     The raw
     */
    public void setRaw(String raw) {
        this.raw = raw;
    }

    /**
     * 
     * @return
     *     The Content
     */
    public String getContent() {
        return Content;
    }

    /**
     * 
     * @param Content
     *     The _content
     */
    public void setContent(String Content) {
        this.Content = Content;
    }

    /**
     * 
     * @return
     *     The machineTag
     */
    public Integer getMachineTag() {
        return machineTag;
    }

    /**
     * 
     * @param machineTag
     *     The machine_tag
     */
    public void setMachineTag(Integer machineTag) {
        this.machineTag = machineTag;
    }

}