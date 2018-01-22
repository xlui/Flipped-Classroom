package io.flippedclassroom.android.model;

import io.flippedclassroom.android.app.AppCache;
import io.flippedclassroom.android.util.PreferenceUtils;

public class LoginModel {
    public void saveToken(String token) {
        PreferenceUtils.saveToken(token);
    }

    public void saveId(String id) {
        AppCache.saveId(id);
    }
}
