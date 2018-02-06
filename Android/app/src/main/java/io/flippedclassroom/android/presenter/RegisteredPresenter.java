package io.flippedclassroom.android.presenter;

public interface RegisteredPresenter {
    //注册界面的view被点击时回调
    void onClick(int viewId);

    //注册界面的身份切换时回调
    void onCheckedChanged(int checkedId);
}
