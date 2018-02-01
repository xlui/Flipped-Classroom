package io.flippedclassroom.android.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.app.AppCache;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.SplashPresenter;
import io.flippedclassroom.android.presenterImpl.SplashPresenterImpl;
import io.flippedclassroom.android.service.RetrofitService;
import io.flippedclassroom.android.view.SplashView;

public class SplashActivity extends BaseActivity implements SplashView {
    private SplashPresenter mPresenter;

    @BindView(R.id.iv_splash)
    ImageView ivSplash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bindService();
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获取Service的实例，保存在AppCache当中
            RetrofitService.MyBinder binder = (RetrofitService.MyBinder) service;
            RetrofitService retrofitService = binder.getService();
            AppCache.setRetrofitService(retrofitService);
            mPresenter.checkTokenAndRole();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    //保存Service
    private void bindService() {
        bindService(new Intent(this, RetrofitService.class), connection, BIND_AUTO_CREATE);
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
        startActivity(new Intent(this, CourseActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
