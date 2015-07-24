
package com.levelup.palabre.inoreaderforpalabre.inoreader.data.folderlist;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class FolderList {

    @Expose
    private List<Tag> tags = new ArrayList<Tag>();

    /**
     * 
     * @return
     *     The tags
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *     The tags
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
