package io.flippedclassroom.android.model;


import android.content.Context;

import io.flippedclassroom.android.base.BaseModel;
import io.flippedclassroom.android.bean.Course;

public class MainModel extends BaseModel {
    private Course mCurrentCourse;

    public MainModel(Context mContext) {
        super(mContext);
    }

    public Course getCurrentCourse() {
        return mCurrentCourse;
    }

    public void setCurrentCourse(Course currentCourse) {
        mCurrentCourse = currentCourse;
    }
}
