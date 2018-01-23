package io.flippedclassroom.android.presenter;


import android.content.Intent;
import android.text.TextUtils;

import java.util.Timer;
import java.util.TimerTask;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.model.SplashModel;
import io.flippedclassroom.android.util.PreferenceUtils;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.view.LoginActivity;
import io.flippedclassroom.android.view.SplashActivity;

public class SplashPresenter extends BasePresenter<SplashActivity> {
    private SplashModel mModel;

    public SplashPresenter(SplashActivity view) {
        super(view);
        mModel = new SplashModel();
    }

    public void goActivity() {
        String token = mModel.getToken();
        String role = mModel.getRole();
        if (TextUtils.isEmpty(token)) {
            mView.getContext().startActivity(new Intent(mView.getContext(), LoginActivity.class));
            mView.finish();
        } else {
            //用token发起网路请求
            //......
            String student = mView.getContext().getString(R.string.student);
            if (role.equals(student)) {
                ToastUtils.createToast("学生登录");
            } else {
                ToastUtils.createToast("教师登录");
            }
        }
    }
}
