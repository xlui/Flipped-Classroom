package io.flippedclassroom.android.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.SplashPresenter;

public class SplashActivity extends BaseActivity<SplashPresenter> {
    @BindView(R.id.iv_splash)
    public ImageView ivSplash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SplashPresenter(this);
    }

    @Override
    protected void initViews() {
        hideStateBar();
        Glide.with(this).load(R.mipmap.splash).into(ivSplash);
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }


}
