package io.flippedclassroom.android.app;

import android.app.Application;

import io.flippedclassroom.android.util.PreferenceUtils;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.init(this);
    }
}
