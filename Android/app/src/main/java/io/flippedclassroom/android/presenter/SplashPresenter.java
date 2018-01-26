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

//SplashActivity的Presenter
public class SplashPresenter extends BasePresenter<SplashActivity> {
    private SplashModel mModel;

    public SplashPresenter(SplashActivity view) {
        super(view);
        mModel = new SplashModel();
    }

    public void goActivity() {
        //读取本地存储的token和角色信息
        String token = mModel.getToken();
        String role = mModel.getRole();

        //判断token是不是空
        if (TextUtils.isEmpty(token)) {
            //如果是空，表示本地没有存储Token和role，即不能利用token登录
            //结束SplashActivity，跳转到LoginActivity
            mView.getContext().startActivity(new Intent(mView.getContext(), LoginActivity.class));
            mView.finish();
        } else {
            //用token发起网路请求
            //......
            String student = mView.getContext().getString(R.string.student);
            //判断身份，决定去往那个Activity
            if (role.equals(student)) {
                ToastUtils.createToast("学生登录");
            } else {
                ToastUtils.createToast("教师登录");
            }
        }
    }
}
