package io.flippedclassroom.android.presenter;


import io.flippedclassroom.android.adapter.CourseAdapter;

public interface CoursePresenter {

    //处理点击事件
    void onClick(int ViewId);

    //处理刷新事件
    void onRefresh();

    //处理搜索事件
    void onQuery(String queryText);

    //当用户点击返回按键，处理返回事件
    void onBack();
}
