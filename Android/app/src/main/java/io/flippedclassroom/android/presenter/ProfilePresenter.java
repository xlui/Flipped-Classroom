package io.flippedclassroom.android.presenter;

import android.net.Uri;
import android.view.View;

import io.flippedclassroom.android.view.ProfileView;

public interface ProfilePresenter {
    int SELECT_IMAGE = 1;

    //处理点击事件
    void onClick(int viewId);

    //处理List的点击事件
    void onClick(int view, int position);

    //加载用户数据
    void loadUserInfo();

    //弹窗点击事件
    void onDialogClick(ProfileView.DialogType type, View view);

    //图片选择完毕
    void onSelectImage(Uri uri);
}
