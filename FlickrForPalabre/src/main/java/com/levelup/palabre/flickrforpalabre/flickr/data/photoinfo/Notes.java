
package com.levelup.palabre.flickrforpalabre.flickr.data.photoinfo;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Notes {

    @Expose
    private List<Object> note = new ArrayList<Object>();

    /**
     * 
     * @return
     *     The note
     */
    public List<Object> getNote() {
        return note;
    }

    /**
     * 
     * @param note
     *     The note
     */
    public void setNote(List<Object> note) {
        this.note = note;
    }

}
