
package com.levelup.palabre.inoreaderforpalabre.inoreader.data;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionList {

    @Expose
    private List<Subscription> subscriptions = new ArrayList<Subscription>();

    /**
     * 
     * @return
     *     The subscriptions
     */
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    /**
     * 
     * @param subscriptions
     *     The subscriptions
     */
    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

}
