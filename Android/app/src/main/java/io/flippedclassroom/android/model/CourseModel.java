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

        //保存到数据库
        //保存到数据库是为了便于搜索，还有就是每次先从本地数据库读取，不必要每次都加载
    }

    public List<Course> getCourseList() {
        return mCourseList;
    }
}
