package io.flippedclassroom.android.model;

import io.flippedclassroom.android.util.PreferenceUtils;

public class RegisteredModelImpl implements RegisteredModel{
    private String role;

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }
}
