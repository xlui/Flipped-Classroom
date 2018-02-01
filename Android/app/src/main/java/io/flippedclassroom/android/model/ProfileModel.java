package io.flippedclassroom.android.model;


import android.content.Context;

import io.flippedclassroom.android.base.BaseModel;
import io.flippedclassroom.android.bean.User;

public class ProfileModel extends BaseModel {
    private User mUser;

    public ProfileModel(Context mContext) {
        super(mContext);
    }

    public void saveUser(User user) {
        mUser = user;
    }

    public User getUser() {
        return mUser;
    }
}
