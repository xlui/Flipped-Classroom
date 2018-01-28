package io.flippedclassroom.android.modelImpl;

import io.flippedclassroom.android.model.SplashModel;
import io.flippedclassroom.android.util.PreferenceUtils;

public class SplashModelImpl implements SplashModel {
    @Override
    public String getToken() {
        return PreferenceUtils.getToken();
    }

    @Override
    public String getRole() {
        return PreferenceUtils.getRole();
    }

    @Override
    public void deleteToken() {
        PreferenceUtils.saveToken(null);
    }

    @Override
    public void deleteRole() {
        PreferenceUtils.saveRole(null);
    }
}
