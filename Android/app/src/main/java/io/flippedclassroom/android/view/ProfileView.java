package io.flippedclassroom.android.view;

public interface ProfileView {
    //刷新用户信息的指定一条
    void updateListItem(String value, int position);

    //获取页面中所有的文字
    String[] getAllTexts();

    //显示加载中
    void showProgressDialog();

    //取消加载框
    void hideProgressDialog();

    //显示编辑弹窗
    void showEditDialog();
}
