package io.flippedclassroom.android.presenter;

public interface ProfilePresenter {
    //处理点击事件
    void onClick(int viewId);

    //处理List的点击事件
    void onClick(int view, int position);

    //处理选择的性别
    void onSelectGender(int checkedId);

    //加载用户数据
    void loadUserInfo();
}
