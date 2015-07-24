
package com.levelup.palabre.inoreaderforpalabre.inoreader.data.streamcontent;

import com.google.gson.annotations.Expose;

public class Summary {

    @Expose
    private String direction;
    @Expose
    private String content;

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
     *     The content
     */
    public String getContent() {
        return content;
    }

    /**
     * 
     * @param content
     *     The content
     */
    public void setContent(String content) {
        this.content = content;
    }

}
