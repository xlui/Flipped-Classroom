package io.flippedclassroom.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.util.ToastUtils;

public class CourseActivity extends BaseActivity implements SearchView.OnQueryTextListener{

    SearchView svSearchView;
    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;

    @Override
    protected int getLayout() {
        return R.layout.activity_course;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initViews() {
        setActionBar(tbToolbar, R.mipmap.menu, "我的课程");
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

    //由于SearchView的获取在onCreate之后
    //所以初始化只能单独写在这里
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

}
