package io.flippedclassroom.android.model;

import io.flippedclassroom.android.util.PreferenceUtils;

public class LoginModelImpl implements LoginModel{
    public void saveToken(String token) {
        PreferenceUtils.saveToken(token);
    }

    public void saveRole(String role) {
        PreferenceUtils.saveRole(role);
    }
}
