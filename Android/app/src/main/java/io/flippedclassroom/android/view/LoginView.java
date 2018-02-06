package io.flippedclassroom.android.view;


public interface LoginView {
    //获取界面内所有的Text
    String[] getAllTexts();

    //按照点击的情况选择显示还是隐藏密码的icon
    //设置为setSelected(false)，icon为闭着眼睛
    //设置为setSelected(true)，icon为张开眼睛
    boolean changePasswordType();

    //依据传入的boolean参数决定是不是显示密码为可见
    void showOrHidePassword(boolean needShow);

    //依据progress的数值来决定按钮显示的状态
    //为100显示加载成功
    //1-99显示加载中
    //-1表示加载失败
    void changeProgressStyle(int progress);

    //设置各个View的是否可以触摸
    void setViewsEnabled(boolean canCheck);

    //前往注册界面
    void startRegisteredActivity();

    //前往课程界面
    void startCourseActivity();
}
