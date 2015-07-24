package com.levelup.palabre.inoreaderforpalabre;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by nicolas on 23/07/15.
 */
public class InoreaderApplication extends Application {

    @Override
    public void onCreate() {
        if (BuildConfig.USE_CRASHLYTICS) {
            Fabric.with(this, new Crashlytics());
        }
        super.onCreate();
    }
}
