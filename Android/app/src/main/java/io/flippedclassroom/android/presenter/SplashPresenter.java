package io.flippedclassroom.android.presenter;


public interface SplashPresenter {
    //验证本地保存的token和role，决定去往哪个界面
    void checkTokenAndRole();
}
