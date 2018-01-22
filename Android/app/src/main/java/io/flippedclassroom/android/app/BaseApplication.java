package io.flippedclassroom.android.app;

import android.app.Application;

import io.flippedclassroom.android.util.PreferenceUtils;
import io.flippedclassroom.android.util.ToastUtils;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.init(this);
        ToastUtils.init(this);
    }
}
