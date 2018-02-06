package io.flippedclassroom.android.presenter;


import android.content.Context;

import io.flippedclassroom.android.adapter.CourseAdapter;

public interface CoursePresenter {

    //处理点击事件
    void onClick(int viewId);

    //处理刷新事件
    void onRefresh();

    //处理搜索事件
    void onQuery(String queryText);

    //当搜索关闭的时候处理某些操作
    void onSearchClose();

    //当用户点击返回按键，处理返回事件
    void onBack();

    //Adapter的点击事件
    void onClick(int viewId, int position);


    Context getContext();
}
