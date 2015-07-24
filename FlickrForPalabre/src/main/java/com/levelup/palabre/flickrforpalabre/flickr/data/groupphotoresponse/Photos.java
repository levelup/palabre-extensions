
package com.levelup.palabre.flickrforpalabre.flickr.data.groupphotoresponse;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Photos {

    @Expose
    private Integer page;
    @Expose
    private String pages;
    @Expose
    private Integer perpage;
    @Expose
    private String total;
    @Expose
    private List<Photo> photo = new ArrayList<Photo>();

    /**
     * @return The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return The pages
     */
    public String getPages() {
        return pages;
    }

    /**
     * @param pages The pages
     */
    public void setPages(String pages) {
        this.pages = pages;
    }

    /**
     * @return The perpage
     */
    public Integer getPerpage() {
        return perpage;
    }

    /**
     * @param perpage The perpage
     */
    public void setPerpage(Integer perpage) {
        this.perpage = perpage;
    }

    /**
     * @return The total
     */
    public String getTotal() {
        return total;
    }

    /**
     * @param total The total
     */
    public void setTotal(String total) {
        this.total = total;
    }

    /**
     * @return The photo
     */
    public List<Photo> getPhoto() {
        return photo;
    }

    /**
     * @param photo The photo
     */
    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }

}
