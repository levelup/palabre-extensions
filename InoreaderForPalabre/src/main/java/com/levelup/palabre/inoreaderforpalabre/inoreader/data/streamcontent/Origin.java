
package com.levelup.palabre.inoreaderforpalabre.inoreader.data.streamcontent;

import com.google.gson.annotations.Expose;

public class Origin {

    @Expose
    private String streamId;
    @Expose
    private String title;
    @Expose
    private String htmlUrl;

    /**
     * 
     * @return
     *     The streamId
     */
    public String getStreamId() {
        return streamId;
    }

    /**
     * 
     * @param streamId
     *     The streamId
     */
    public void setStreamId(String streamId) {
        this.streamId = streamId;
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

}
