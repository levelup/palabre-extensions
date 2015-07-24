
package com.levelup.palabre.flickrforpalabre.flickr.data.favorites;

import com.google.gson.annotations.Expose;
import com.levelup.palabre.flickrforpalabre.flickr.data.groupphotoresponse.Photo;

import java.util.ArrayList;
import java.util.List;

public class Photos {

    @Expose
    private Long page;
    @Expose
    private Long pages;
    @Expose
    private Long perpage;
    @Expose
    private Long total;
    @Expose
    private List<Photo> photo = new ArrayList<Photo>();

    /**
     * 
     * @return
     *     The page
     */
    public Long getPage() {
        return page;
    }

    /**
     * 
     * @param page
     *     The page
     */
    public void setPage(Long page) {
        this.page = page;
    }

    /**
     * 
     * @return
     *     The pages
     */
    public Long getPages() {
        return pages;
    }

    /**
     * 
     * @param pages
     *     The pages
     */
    public void setPages(Long pages) {
        this.pages = pages;
    }

    /**
     * 
     * @return
     *     The perpage
     */
    public Long getPerpage() {
        return perpage;
    }

    /**
     * 
     * @param perpage
     *     The perpage
     */
    public void setPerpage(Long perpage) {
        this.perpage = perpage;
    }

    /**
     * 
     * @return
     *     The total
     */
    public Long getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    public void setTotal(Long total) {
        this.total = total;
    }

    /**
     * 
     * @return
     *     The photo
     */
    public List<Photo> getPhoto() {
        return photo;
    }

    /**
     * 
     * @param photo
     *     The photo
     */
    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }

}
