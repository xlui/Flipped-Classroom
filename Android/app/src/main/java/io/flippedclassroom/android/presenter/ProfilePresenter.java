package io.flippedclassroom.android.presenter;

import android.view.View;

import io.flippedclassroom.android.view.ProfileView;

public interface ProfilePresenter {
    //处理点击事件
    void onClick(int viewId);

    //处理List的点击事件
    void onClick(int view, int position);

    //加载用户数据
    void loadUserInfo();

    //弹窗点击事件
    void onDialogClick(ProfileView.DialogType type,View view);
}
