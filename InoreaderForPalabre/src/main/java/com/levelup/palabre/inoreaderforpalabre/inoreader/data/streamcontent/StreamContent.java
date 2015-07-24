
package com.levelup.palabre.inoreaderforpalabre.inoreader.data.streamcontent;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class StreamContent {

    @Expose
    private String direction;
    @Expose
    private String id;
    @Expose
    private String title;
    @Expose
    private String description;
    @Expose
    private Self self;
    @Expose
    private Long updated;
    @Expose
    private String updatedUsec;
    @Expose
    private List<Item> items = new ArrayList<Item>();
    @Expose
    private String continuation;

    /**
     * 
     * @return
     *     The direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * 
     * @param direction
     *     The direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
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
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The self
     */
    public Self getSelf() {
        return self;
    }

    /**
     * 
     * @param self
     *     The self
     */
    public void setSelf(Self self) {
        this.self = self;
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
     *     The updatedUsec
     */
    public String getUpdatedUsec() {
        return updatedUsec;
    }

    /**
     * 
     * @param updatedUsec
     *     The updatedUsec
     */
    public void setUpdatedUsec(String updatedUsec) {
        this.updatedUsec = updatedUsec;
    }

    /**
     * 
     * @return
     *     The items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * 
     * @param items
     *     The items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * 
     * @return
     *     The continuation
     */
    public String getContinuation() {
        return continuation;
    }

    /**
     * 
     * @param continuation
     *     The continuation
     */
    public void setContinuation(String continuation) {
        this.continuation = continuation;
    }

}
