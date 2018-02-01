package io.flippedclassroom.android.json;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

import io.flippedclassroom.android.bean.Course;

//课程的Json类
public class CourseJson {

    private List<CoursesBean> courses;

    public List<CoursesBean> getCourses() {
        return courses;
    }

    public void setCourses(List<CoursesBean> courses) {
        this.courses = courses;
    }

    public static class CoursesBean {

        private int id;
        private String name;
        private String major;
        private int count;
        private String code;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    //生成course的list
    public List<Course> getCoursesList() {
        List<Course> list = new ArrayList<>();
        for (CoursesBean bean : getCourses()) {
            Course course = new Course();
            course.setId(bean.getId());
            course.setName(bean.getName());
            course.setMajor(bean.getMajor());
            course.setCount(bean.getCount());
            course.setCode(bean.getCode());
            list.add(course);
        }
        return list;
    }
}
