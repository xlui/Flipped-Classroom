package io.flippedclassroom.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.bean.Course;
import io.flippedclassroom.android.config.ConstantConfig;

public class CourseInfoActivity extends BaseActivity {

    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @BindView(R.id.tv_course_name)
    TextView tvCourseName;
    @BindView(R.id.tv_course_major)
    TextView tvCourseMajor;
    @BindView(R.id.tv_course_number)
    TextView tvCourseNumber;
    @BindView(R.id.tv_course_code)
    TextView tvCourseCode;

    @Override
    protected int getLayout() {
        return R.layout.activity_course_info;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initViews() {
        setActionBar(tbToolbar, 0, "课程详情");
        getSwipeBackLayout().setEnableGesture(true);
        Intent intent = getIntent();
        Course course = intent.getParcelableExtra(ConstantConfig.NEW_INTENT);
        tvCourseCode.setText("课程代码 : " + course.getCode());
        tvCourseMajor.setText("专业 : " + course.getMajor());
        tvCourseName.setText("课程 : " + course.getName());
        tvCourseNumber.setText("班级人数 : " + course.getCount());
    }

    @Override
    public Context getContext() {
        return this;
    }

}
