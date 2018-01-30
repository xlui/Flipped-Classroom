package io.flippedclassroom.android.app;

import android.app.Application;

import io.flippedclassroom.android.util.PreferenceUtils;
import io.flippedclassroom.android.util.RetrofitManager;
import io.flippedclassroom.android.util.ToastUtils;

//初始化类。一些工具类在这里完成初始化，获得全局的context
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitManager.init();
        PreferenceUtils.init(this);
        ToastUtils.init(this);
    }
}
