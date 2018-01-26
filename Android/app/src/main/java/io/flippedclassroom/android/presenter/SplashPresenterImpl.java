package io.flippedclassroom.android.presenter;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.model.SplashModel;
import io.flippedclassroom.android.model.SplashModelImpl;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.activity.LoginActivity;
import io.flippedclassroom.android.activity.SplashActivity;
import io.flippedclassroom.android.view.SplashView;

public class SplashPresenterImpl extends BasePresenter implements SplashPresenter {
    private SplashModel mModel;
    private SplashView mView;

    public SplashPresenterImpl(SplashActivity activity, Context context) {
        super(context);
        mView = activity;
        mModel = new SplashModelImpl();
    }

    @Override
    public void checkTokenAndRole() {
        //读取本地存储的token和角色信息
        String token = mModel.getToken();
        String role = mModel.getRole();

        //判断token是不是空
        if (TextUtils.isEmpty(token)) {
            //如果是空，表示本地没有存储Token和role，即不能利用token登录
            //结束SplashActivity，跳转到LoginActivity
            mView.startLoginActivity();
        } else {
            //用token发起网路请求
            //......
            String student = mContext.getString(R.string.student);
            //判断身份，决定去往那个Activity
            if (role.equals(student)) {
                ToastUtils.createToast("学生登录");
            } else {
                ToastUtils.createToast("教师登录");
            }
        }
    }
}
