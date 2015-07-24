package com.levelup.palabre.flickrforpalabre.flickr;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.levelup.palabre.flickrforpalabre.flickr.data.license.License;
import com.levelup.palabre.flickrforpalabre.flickr.data.license.LicenseResponse;
import com.levelup.palabre.flickrforpalabre.flickr.utils.SharedPreferenceKeys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 29/04/15.
 */
public class FlickrUtils {

    //Cache fore licenses
    private static List<License> licencesCache = new ArrayList<>();

    public static String photoUrl(String farm, String server, String id) {
        return "http://farm" + farm + ".staticflickr.com/" + server + "/buddyicons/" + id + ".jpg";

    }

    public static String photoUrl(String farm, String server, String id, String secret) {
        return "https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + ".jpg\n";

    }

    public static String getLicenseText(Context context, Integer license) {
        for (License licence : licencesCache) {
            if (licence.getId().equals(license)) {
                String startA = "";
                String endA = "";
                if (!TextUtils.isEmpty(licence.getUrl())) {
                    startA = "<a href=\"" + licence.getUrl() + "\">";
                    endA = "</a>";
                }

                return "<br/><br/>" + startA + licence.getName() + endA;
            }
        }

        //Nothing in the cache
        String licensesPrefs = PreferenceManager.getDefaultSharedPreferences(context).getString(SharedPreferenceKeys.LICENSES, "");
        if (!TextUtils.isEmpty(licensesPrefs)) {
            Gson gson = new Gson();
            LicenseResponse licensesFromPrefs = gson.fromJson(licensesPrefs, LicenseResponse.class);
            for (License license1 : licensesFromPrefs.getLicenses().getLicense()) {
                licencesCache.add(license1);
            }
            return getLicenseText(context, license);
        }

        //Nothing in the prefs


        loadLicences(context);
        return "";
    }

    public static void loadLicences(final Context context) {
        FlickrService.getInstance(context).getLicenses(new FlickrServiceInterface.IRequestListener<LicenseResponse>() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(LicenseResponse response) {

                PreferenceManager.getDefaultSharedPreferences(context).edit().putString(SharedPreferenceKeys.LICENSES, new Gson().toJson(response)).apply();
            }
        });
    }
}
