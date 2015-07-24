
package com.levelup.palabre.inoreaderforpalabre.inoreader.data.preferencestreamlist;

import com.google.gson.annotations.Expose;

public class PreferenceStreamList {

    @Expose
    private Streamprefs streamprefs;

    /**
     * 
     * @return
     *     The streamprefs
     */
    public Streamprefs getStreamprefs() {
        return streamprefs;
    }

    /**
     * 
     * @param streamprefs
     *     The streamprefs
     */
    public void setStreamprefs(Streamprefs streamprefs) {
        this.streamprefs = streamprefs;
    }

}
