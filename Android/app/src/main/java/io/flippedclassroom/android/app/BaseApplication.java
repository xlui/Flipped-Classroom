package io.flippedclassroom.android.app;

import android.app.Application;

import io.flippedclassroom.android.util.PreferenceUtils;
import io.flippedclassroom.android.util.RetrofitUtils;
import io.flippedclassroom.android.util.ToastUtils;

//初始化类。一些工具类在这里完成初始化，获得全局的context
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitUtils.init();
        PreferenceUtils.init(this);
        ToastUtils.init(this);
    }
}
