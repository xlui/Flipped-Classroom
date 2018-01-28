package io.flippedclassroom.android.presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import io.flippedclassroom.android.activity.CourseActivity;
import io.flippedclassroom.android.adapter.CourseAdapter;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.view.CourseView;

public class CoursePresenterImpl extends BasePresenter implements CoursePresenter {
    private CourseView mView;

    public CoursePresenterImpl(CourseActivity activity, Context mContext) {
        super(mContext);
        mView = activity;
    }

    @Override
    public void onClick(int ViewId) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onQuery(String queryText) {

    }

    @Override
    public CourseAdapter createAdapter() {
        CourseAdapter adapter = new CourseAdapter();
        return adapter;
    }
}
