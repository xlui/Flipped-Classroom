package io.flippedclassroom.android.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.LoginPresenter;

public class LoginActivity extends BaseActivity<LoginPresenter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
