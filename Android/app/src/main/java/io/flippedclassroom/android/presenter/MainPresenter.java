package io.flippedclassroom.android.presenter;

import android.content.Intent;
import android.support.v4.app.FragmentManager;

public interface MainPresenter {
    //点击事件处理
    void onClick(int viewId);

    //处理刷新事件
    void onRefresh();

    //解析Intent
    void parseIntent(Intent intent);

    //搭建主界面
    void createContent(FragmentManager fragmentManager);
}
