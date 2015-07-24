
package com.levelup.palabre.inoreaderforpalabre.inoreader.data.streamcontent;

import com.google.gson.annotations.Expose;

public class Canonical {

    @Expose
    private String href;

    /**
     * 
     * @return
     *     The href
     */
    public String getHref() {
        return href;
    }

    /**
     * 
     * @param href
     *     The href
     */
    public void setHref(String href) {
        this.href = href;
    }

}
