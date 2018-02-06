package io.flippedclassroom.android.base;

import android.app.Activity;
import android.content.Context;

//Presenter基类
public abstract class BasePresenter {
    protected Context mContext;

    public BasePresenter(Context mContext) {
        this.mContext = mContext;
    }
}
