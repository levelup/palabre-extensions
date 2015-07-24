
package com.levelup.palabre.flickrforpalabre.flickr.data.license;

import com.google.gson.annotations.Expose;

public class LicenseResponse {

    @Expose
    private Licenses licenses;
    @Expose
    private String stat;

    /**
     * 
     * @return
     *     The licenses
     */
    public Licenses getLicenses() {
        return licenses;
    }

    /**
     * 
     * @param licenses
     *     The licenses
     */
    public void setLicenses(Licenses licenses) {
        this.licenses = licenses;
    }

    public LicenseResponse withLicenses(Licenses licenses) {
        this.licenses = licenses;
        return this;
    }

    /**
     * 
     * @return
     *     The stat
     */
    public String getStat() {
        return stat;
    }

    /**
     * 
     * @param stat
     *     The stat
     */
    public void setStat(String stat) {
        this.stat = stat;
    }

    public LicenseResponse withStat(String stat) {
        this.stat = stat;
        return this;
    }

}
