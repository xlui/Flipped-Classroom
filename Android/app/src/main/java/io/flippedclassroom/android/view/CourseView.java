package io.flippedclassroom.android.view;


import android.graphics.Bitmap;

import io.flippedclassroom.android.adapter.CourseAdapter;

public interface CourseView extends SwitchActivityInterface {
    //展开测栏
    void openDrawer();

    //关闭测栏
    void closeDrawer();

    //加载结束时回调
    void onRefreshFinish();

    //更新列表
    void updateCourseList(CourseAdapter adapter);

    //更新测栏的顶部
    void updateHeaderText(String nickName, String signature);

    //设置头像
    void updateAvatar(Bitmap avatar);

    //开始刷新
    void startRefresh();
}
