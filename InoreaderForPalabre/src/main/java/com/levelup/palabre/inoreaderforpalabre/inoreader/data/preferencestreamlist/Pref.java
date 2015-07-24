package com.levelup.palabre.inoreaderforpalabre.inoreader.data.preferencestreamlist;

import com.google.gson.annotations.Expose;

/**
 * Created by nicolas on 02/06/15.
 */
public class Pref {

    @Expose
    private String id;

    @Expose
    private String value;

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
