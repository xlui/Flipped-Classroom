package io.flippedclassroom.android.view;

import android.support.v4.app.FragmentStatePagerAdapter;

public interface MainView {
    //设置toolbar
    void setToolbar(String title);

    //构建Viewpager
    void updateContent(FragmentStatePagerAdapter adapter);
}
