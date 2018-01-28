package io.flippedclassroom.android.view;


public interface CourseView {
    //展开测栏
    void openDrawer();

    //关闭测栏
    void closeDrawer();

    //前往主页界面
    void startProfileActivity();

    //前往设置界面
    void startSettingActivity();

    //前往账户界面
    void startAccountActivity();

    //前往添加课程界面
    void startNewCourseActivity();
}
