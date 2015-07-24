
package com.levelup.palabre.inoreaderforpalabre.inoreader.data.streamcontent;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Item {

    @Expose
    private long crawlTimeMsec;
    @Expose
    private String timestampUsec;
    @Expose
    private String id;
    @Expose
    private List<String> categories = new ArrayList<String>();
    @Expose
    private String title;
    @Expose
    private Long published;
    @Expose
    private Long updated;
    @Expose
    private List<Canonical> canonical = new ArrayList<Canonical>();
    @Expose
    private List<Alternate> alternate = new ArrayList<Alternate>();
    @Expose
    private Summary summary;
    @Expose
    private String author;
    @Expose
    private List<Object> likingUsers = new ArrayList<Object>();
    @Expose
    private List<Object> comments = new ArrayList<Object>();
    @Expose
    private Long commentsNum;
    @Expose
    private List<Object> annotations = new ArrayList<Object>();
    @Expose
    private Origin origin;

    /**
     * 
     * @return
     *     The crawlTimeMsec
     */
    public long getCrawlTimeMsec() {
        return crawlTimeMsec;
    }

    /**
     * 
     * @param crawlTimeMsec
     *     The crawlTimeMsec
     */
    public void setCrawlTimeMsec(long crawlTimeMsec) {
        this.crawlTimeMsec = crawlTimeMsec;
    }

    /**
     * 
     * @return
     *     The timestampUsec
     */
    public String getTimestampUsec() {
        return timestampUsec;
    }

    /**
     * 
     * @param timestampUsec
     *     The timestampUsec
     */
    public void setTimestampUsec(String timestampUsec) {
        this.timestampUsec = timestampUsec;
    }

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
     *     The categories
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * 
     * @param categories
     *     The categories
     */
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The published
     */
    public Long getPublished() {
        return published;
    }

    /**
     * 
     * @param published
     *     The published
     */
    public void setPublished(Long published) {
        this.published = published;
    }

    /**
     * 
     * @return
     *     The updated
     */
    public Long getUpdated() {
        return updated;
    }

    /**
     * 
     * @param updated
     *     The updated
     */
    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    /**
     * 
     * @return
     *     The canonical
     */
    public List<Canonical> getCanonical() {
        return canonical;
    }

    /**
     * 
     * @param canonical
     *     The canonical
     */
    public void setCanonical(List<Canonical> canonical) {
        this.canonical = canonical;
    }

    /**
     * 
     * @return
     *     The alternate
     */
    public List<Alternate> getAlternate() {
        return alternate;
    }

    /**
     * 
     * @param alternate
     *     The alternate
     */
    public void setAlternate(List<Alternate> alternate) {
        this.alternate = alternate;
    }

    /**
     * 
     * @return
     *     The summary
     */
    public Summary getSummary() {
        return summary;
    }

    /**
     * 
     * @param summary
     *     The summary
     */
    public void setSummary(Summary summary) {
        this.summary = summary;
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
     *     The likingUsers
     */
    public List<Object> getLikingUsers() {
        return likingUsers;
    }

    /**
     * 
     * @param likingUsers
     *     The likingUsers
     */
    public void setLikingUsers(List<Object> likingUsers) {
        this.likingUsers = likingUsers;
    }

    /**
     * 
     * @return
     *     The comments
     */
    public List<Object> getComments() {
        return comments;
    }

    /**
     * 
     * @param comments
     *     The comments
     */
    public void setComments(List<Object> comments) {
        this.comments = comments;
    }

    /**
     * 
     * @return
     *     The commentsNum
     */
    public Long getCommentsNum() {
        return commentsNum;
    }

    /**
     * 
     * @param commentsNum
     *     The commentsNum
     */
    public void setCommentsNum(Long commentsNum) {
        this.commentsNum = commentsNum;
    }

    /**
     * 
     * @return
     *     The annotations
     */
    public List<Object> getAnnotations() {
        return annotations;
    }

    /**
     * 
     * @param annotations
     *     The annotations
     */
    public void setAnnotations(List<Object> annotations) {
        this.annotations = annotations;
    }

    /**
     * 
     * @return
     *     The origin
     */
    public Origin getOrigin() {
        return origin;
    }

    /**
     * 
     * @param origin
     *     The origin
     */
    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

}
