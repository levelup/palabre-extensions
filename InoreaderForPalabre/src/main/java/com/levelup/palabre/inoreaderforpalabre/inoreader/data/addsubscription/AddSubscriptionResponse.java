
package com.levelup.palabre.inoreaderforpalabre.inoreader.data.addsubscription;

import com.google.gson.annotations.Expose;

public class AddSubscriptionResponse {

    @Expose
    private String query;
    @Expose
    private Long numResults;
    @Expose
    private String streamId;
    @Expose
    private String streamName;

    /**
     * 
     * @return
     *     The query
     */
    public String getQuery() {
        return query;
    }

    /**
     * 
     * @param query
     *     The query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * 
     * @return
     *     The numResults
     */
    public Long getNumResults() {
        return numResults;
    }

    /**
     * 
     * @param numResults
     *     The numResults
     */
    public void setNumResults(Long numResults) {
        this.numResults = numResults;
    }

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
     *     The streamName
     */
    public String getStreamName() {
        return streamName;
    }

    /**
     * 
     * @param streamName
     *     The streamName
     */
    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

}
