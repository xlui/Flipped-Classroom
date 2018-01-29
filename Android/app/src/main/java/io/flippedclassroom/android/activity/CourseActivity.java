package io.flippedclassroom.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.adapter.CourseAdapter;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.CoursePresenter;
import io.flippedclassroom.android.presenterImpl.CoursePresenterImpl;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.view.CourseView;

public class CourseActivity extends BaseActivity implements
        SearchView.OnQueryTextListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnCloseListener,
        CourseView {

    @BindView(R.id.dl_drawer)
    DrawerLayout dlDrawer;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRefresh();
    }

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
        srlRefresh.setOnRefreshListener(this);
    }

    //初始化RecycleView
    private void initCourseList() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCoursesList.setLayoutManager(manager);
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
        svSearchView.setOnCloseListener(this);
    }

    //当确定搜索之后回调
    @Override
    public boolean onQueryTextSubmit(String query) {
        mPresenter.onQuery(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClick(v.getId());
    }

    @Override
    public void onRefresh() {
        srlRefresh.setRefreshing(true);
        mPresenter.onRefresh();

    }

    @Override
    public void openDrawer() {
        dlDrawer.openDrawer(Gravity.START);
    }

    @Override
    public void closeDrawer() {
        dlDrawer.closeDrawers();
    }

    @Override
    public void onRefreshFinish() {
        srlRefresh.setRefreshing(false);
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

    @Override
    public void returnLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void updateCourseList(CourseAdapter adapter) {
        rvCoursesList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (dlDrawer.isDrawerOpen(Gravity.START)) {
            closeDrawer();
        } else {
            mPresenter.onBack();
        }
    }

    @Override
    public boolean onClose() {
        mPresenter.onSearchClose();
        return false;
    }
}
