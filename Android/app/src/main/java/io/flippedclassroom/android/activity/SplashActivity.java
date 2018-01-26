package io.flippedclassroom.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.SplashPresenter;
import io.flippedclassroom.android.presenter.SplashPresenterImpl;
import io.flippedclassroom.android.view.SplashView;

public class SplashActivity extends BaseActivity implements SplashView {
    private SplashPresenter mPresenter;

    @BindView(R.id.iv_splash)
    ImageView ivSplash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.checkTokenAndRole();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SplashPresenterImpl(this, getContext());
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

    @Override
    public void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void startCourseActivity() {
        //startActivity
        finish();
    }
}
