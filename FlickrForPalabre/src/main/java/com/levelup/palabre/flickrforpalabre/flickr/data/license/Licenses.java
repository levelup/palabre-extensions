
package com.levelup.palabre.flickrforpalabre.flickr.data.license;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Licenses {

    @Expose
    private List<License> license = new ArrayList<License>();

    /**
     * 
     * @return
     *     The license
     */
    public List<License> getLicense() {
        return license;
    }

    /**
     * 
     * @param license
     *     The license
     */
    public void setLicense(List<License> license) {
        this.license = license;
    }

    public Licenses withLicense(List<License> license) {
        this.license = license;
        return this;
    }

}
