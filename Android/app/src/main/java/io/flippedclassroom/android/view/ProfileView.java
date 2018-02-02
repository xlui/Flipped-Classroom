package io.flippedclassroom.android.view;

import android.graphics.Bitmap;
import android.widget.ListAdapter;

public interface ProfileView {
    enum DialogType {
        DIALOG_TYPE_TEXT_EDIT, DIALOG_TYPE_CHOOSE
    }

    //显示加载中
    void showProgressDialog();

    //取消加载框
    void hideProgressDialog();

    //显示编辑弹窗
    void showEditDialog();

    //更新list的数据
    void updateList(ListAdapter adapter);

    //显示选择弹窗
    void showChooseDialog();

    //设置头像
    void setAvatar(Bitmap bitmap);

    //打开图库
    void openGallery();
}
