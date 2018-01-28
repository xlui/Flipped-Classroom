package io.flippedclassroom.android.model;


import java.util.List;

import io.flippedclassroom.android.bean.Course;

public interface CourseModel extends SplashModel{
    //保存课程
    void saveCourseList(List<Course> courseList);

    //对外提供列表
    List<Course> getCourseList();
}
