package io.flippedclassroom.android.view;

import android.content.Context;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.SplashPresenter;

public class SplashActivity extends BaseActivity<SplashPresenter> {

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SplashPresenter(this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected Context getContext() {
        return this;
    }
}
