package io.flippedclassroom.android.model;

public interface SplashModel {
    //从本地读取token
    String getToken();

    //从本地读取role
    String getRole();

    //删除本地token
    void deleteToken();

    //删除本地保存的role
    void deleteRole();
}
