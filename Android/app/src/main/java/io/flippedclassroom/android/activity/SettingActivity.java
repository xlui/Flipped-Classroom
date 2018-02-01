package io.flippedclassroom.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initViews() {
        getSwipeBackLayout().setEnableGesture(true);
        setActionBar(tbToolbar,0,"设置");
    }

    @Override
    public Context getContext() {
        return this;
    }

}
