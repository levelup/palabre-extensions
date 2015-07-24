
package com.levelup.palabre.inoreaderforpalabre.inoreader.data;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Subscription {

    @Expose
    private String id;
    @Expose
    private String title;
    @Expose
    private List<Category> categories = new ArrayList<Category>();
    @Expose
    private String sortid;
    @Expose
    private long firstitemmsec;
    @Expose
    private String url;
    @Expose
    private String htmlUrl;
    @Expose
    private String iconUrl;

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
     *     The categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * 
     * @param categories
     *     The categories
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * 
     * @return
     *     The sortid
     */
    public String getSortid() {
        return sortid;
    }

    /**
     * 
     * @param sortid
     *     The sortid
     */
    public void setSortid(String sortid) {
        this.sortid = sortid;
    }

    /**
     * 
     * @return
     *     The firstitemmsec
     */
    public long getFirstitemmsec() {
        return firstitemmsec;
    }

    /**
     * 
     * @param firstitemmsec
     *     The firstitemmsec
     */
    public void setFirstitemmsec(Integer firstitemmsec) {
        this.firstitemmsec = firstitemmsec;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The htmlUrl
     */
    public String getHtmlUrl() {
        return htmlUrl;
    }

    /**
     * 
     * @param htmlUrl
     *     The htmlUrl
     */
    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    /**
     * 
     * @return
     *     The iconUrl
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * 
     * @param iconUrl
     *     The iconUrl
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

}
