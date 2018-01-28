package io.flippedclassroom.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.CoursePresenter;
import io.flippedclassroom.android.presenter.CoursePresenterImpl;
import io.flippedclassroom.android.view.CourseView;

public class CourseActivity extends BaseActivity implements
        SearchView.OnQueryTextListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        CourseView {

    private CoursePresenter mPresenter;
    SearchView svSearchView;
    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @BindView(R.id.rv_courses_list)
    RecyclerView rvCoursesList;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.nv_navigation_view)
    NavigationView nvNavigationView;

    @Override
    protected int getLayout() {
        return R.layout.activity_course;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new CoursePresenterImpl(this, getContext());
    }

    @Override
    protected void initViews() {
        setActionBar(tbToolbar, R.mipmap.menu, "我的课程");
        initCourseList();
    }

    //初始化RecycleView
    private void initCourseList() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCoursesList.setLayoutManager(manager);
        rvCoursesList.setAdapter(mPresenter.createAdapter());
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_view, menu);

        //找到searchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        svSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        initSearchView();

        return super.onCreateOptionsMenu(menu);
    }

    //初始化SearchView
    private void initSearchView() {
        svSearchView.setQueryHint("搜索已选课程");
        svSearchView.setSubmitButtonEnabled(true);
        svSearchView.setOnQueryTextListener(this);
    }

    //当确定搜索之后回调
    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        srlRefresh.setRefreshing(true);

        srlRefresh.setRefreshing(false);
    }

    @Override
    public void openDrawer() {

    }

    @Override
    public void closeDrawer() {

    }

    @Override
    public void startProfileActivity() {

    }

    @Override
    public void startSettingActivity() {

    }

    @Override
    public void startAccountActivity() {

    }

    @Override
    public void startNewCourseActivity() {

    }

}
