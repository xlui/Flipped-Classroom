package io.flippedclassroom.android.view;


public interface RegisteredView{
    //获取界面内所有的Text
    String[] getAllTexts();

    //依据progress的数值来决定按钮显示的状态
    //为100显示加载成功
    //1-99显示加载中
    //-1表示加载失败
    void changeProgressStyle(int progress);

    //设置各个View的是否可以触摸
    void setViewsEnabled(boolean canCheck);

    //前往注册界面
    void startLoginActivity();
}
