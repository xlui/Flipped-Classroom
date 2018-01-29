package io.flippedclassroom.android.base;

import android.content.Context;

import io.flippedclassroom.android.util.PreferenceUtils;

//model的基类
public abstract class BaseModel {
    protected Context mContext;

    public BaseModel(Context mContext) {
        this.mContext = mContext;
    }

    public void saveToken(String token) {
        PreferenceUtils.saveToken(token);
    }

    public String getToken() {
        return PreferenceUtils.getToken();
    }

    public String getRole() {
        return PreferenceUtils.getRole();
    }

    public void saveRole(String role) {
        PreferenceUtils.saveRole(role);
    }

    public void saveId(String id) {
        PreferenceUtils.saveId(id);
    }

    public String getId() {
        return PreferenceUtils.getId();
    }

    public void deleteRole() {
        PreferenceUtils.saveRole(null);
    }

    public void deleteId() {
        PreferenceUtils.saveId(null);
    }

    public void deleteToken() {
        PreferenceUtils.saveToken(null);
    }
}
