package io.flippedclassroom.android.model;

import io.flippedclassroom.android.util.PreferenceUtils;

public class SplashModel {

    public String getToken() {
        return PreferenceUtils.getToken();
    }

    public String getRole() {
        return PreferenceUtils.getRole();
    }
}
