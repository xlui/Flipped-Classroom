package io.flippedclassroom.android.model;

import android.content.Context;

import java.util.List;

import io.flippedclassroom.android.base.BaseModel;
import io.flippedclassroom.android.bean.Course;

public class CourseModel extends BaseModel{
    private List<Course> mCourseList;

    public CourseModel(Context mContext) {
        super(mContext);
    }

    public void saveCourseList(List<Course> courseList) {
        mCourseList = courseList;
    }

    public List<Course> getCourseList() {
        return mCourseList;
    }
}
