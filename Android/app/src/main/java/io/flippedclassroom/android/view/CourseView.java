package io.flippedclassroom.android.view;


import android.graphics.Bitmap;

import io.flippedclassroom.android.adapter.CourseAdapter;

public interface CourseView {
    //展开测栏
    void openDrawer();

    //关闭测栏
    void closeDrawer();

    //加载结束时回调
    void onRefreshFinish();

    //前往主页界面
    void startProfileActivity();

    //前往设置界面
    void startSettingActivity();

    //前往账户界面
    void startAccountActivity();

    //前往添加课程界面
    void startNewCourseActivity();

    //返回登录界面
    void returnLoginActivity();

    //更新列表
    void updateCourseList(CourseAdapter adapter);

    //更新测栏的顶部
    void updateHeaderText(String nickName,String signature);

    //设置头像
    void updateAvatar(Bitmap avatar);
}
