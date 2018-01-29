package io.flippedclassroom.android.model;

import android.content.Context;

import io.flippedclassroom.android.base.BaseModel;

public class RegisteredModel extends BaseModel {
    private String role;

    public RegisteredModel(Context mContext) {
        super(mContext);
    }

    @Override
    public void saveRole(String role) {
        this.role = role;
    }
}
