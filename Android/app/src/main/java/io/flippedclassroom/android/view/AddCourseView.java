package io.flippedclassroom.android.view;


public interface AddCourseView {
    //添加成功的时候回调
    void AddSuccess();

    //添加失败的售后回调
    void AddError(String msg);
}
