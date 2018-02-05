package io.flippedclassroom.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.MainPresenter;
import io.flippedclassroom.android.presenterImpl.MainPresenterImpl;
import io.flippedclassroom.android.view.MainView;

public class MainActivity extends BaseActivity implements MainView {
    private MainPresenter mPresenter;

    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @BindView(R.id.vp_view_pager)
    ViewPager vpViewPager;
    @BindView(R.id.tl_tab_layout)
    TabLayout tlTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.parseIntent(getIntent());
        mPresenter.createContent(getSupportFragmentManager());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MainPresenterImpl(getContext(), this);
    }

    @Override
    protected void initViews() {}

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setToolbar(String title) {
        setActionBar(tbToolbar, 0, title);
    }

    @Override
    public void updateContent(FragmentStatePagerAdapter adapter) {
        vpViewPager.setAdapter(adapter);
        //关联viewpager和tablayout
        tlTabLayout.setupWithViewPager(vpViewPager);
        //设置底栏
        tlTabLayout.getTabAt(0).setIcon(R.drawable.material_tab);
        tlTabLayout.getTabAt(1).setIcon(R.drawable.test_tab);
        tlTabLayout.getTabAt(2).setIcon(R.drawable.more_tab);
    }
}
