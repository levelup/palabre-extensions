
package com.levelup.palabre.inoreaderforpalabre.inoreader.data.folderlist;

import com.google.gson.annotations.Expose;

public class Tag {

    @Expose
    private String id;
    @Expose
    private String sortid;

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

}
