package io.flippedclassroom.android.model;

import io.flippedclassroom.android.util.PreferenceUtils;

public interface LoginModel {
    //保存token到本地
    void saveToken(String token) ;

    //保存角色信息到本地
    void saveRole(String role);
}
