package io.flippedclassroom.android.modelImpl;

import java.util.List;

import io.flippedclassroom.android.bean.Course;
import io.flippedclassroom.android.model.CourseModel;
import io.flippedclassroom.android.util.PreferenceUtils;

public class CourseModelImpl implements CourseModel {
    private List<Course> mCourseList;

    @Override
    public String getToken() {
        return PreferenceUtils.getToken();
    }

    @Override
    public String getRole() {
        return PreferenceUtils.getRole();
    }

    @Override
    public void deleteToken() {
        PreferenceUtils.saveToken(null);
    }

    @Override
    public void deleteRole() {
        PreferenceUtils.saveRole(null);
    }

    @Override
    public void saveCourseList(List<Course> courseList) {
        mCourseList = courseList;
    }

    @Override
    public List<Course> getCourseList() {
        return mCourseList;
    }
}
