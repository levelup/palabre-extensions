package com.levelup.palabre.flickrforpalabre;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.levelup.palabre.flickrforpalabre.flickr.Config;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import io.fabric.sdk.android.Fabric;

/**
 * Created by nicolas on 14/04/15.
 */
public class FlickrForPalabreApplication extends Application {

    private static OAuthService oAuthService;
    private static Token mRequestToken;

    public static OAuthService getoAuthService() {
        if (oAuthService != null) {
            return oAuthService;
        }
        final String apiKey = Config.CONSUMER_KEY;
        final String apiSecret = Config.CONSUMER_SECRET;
        oAuthService = new ServiceBuilder().provider(FlickrApi.class).apiKey(apiKey).apiSecret(apiSecret).callback("flickrforpalabre://oauth").build();
        return oAuthService;
    }

    public static Token getRequestToken() {
        if (mRequestToken != null) {
            return mRequestToken;
        }
        try {
            mRequestToken = getoAuthService().getRequestToken();

        } catch (Exception e) {

        }

        return mRequestToken;
    }

    public static void resetRequestToken() {
        mRequestToken = null;
    }

    @Override
    public void onCreate() {
        if (BuildConfig.USE_CRASHLYTICS) {
            Fabric.with(this, new Crashlytics());
        }
        super.onCreate();
    }
}
