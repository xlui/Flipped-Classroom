package io.flippedclassroom.android.base;


public abstract class BasePresenter<T extends BaseActivity> {
    protected T mView;

    public BasePresenter(T view) {
        mView = view;
    }
}
