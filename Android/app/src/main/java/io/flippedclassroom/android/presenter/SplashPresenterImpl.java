package io.flippedclassroom.android.presenter;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.model.SplashModel;
import io.flippedclassroom.android.model.SplashModelImpl;
import io.flippedclassroom.android.util.HttpUtils;
import io.flippedclassroom.android.util.LogUtils;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.activity.LoginActivity;
import io.flippedclassroom.android.activity.SplashActivity;
import io.flippedclassroom.android.util.UrlBuilder;
import io.flippedclassroom.android.view.SplashView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
        final String role = mModel.getRole();

        //判断token是不是空
        if (TextUtils.isEmpty(token)) {
            //如果是空，表示本地没有存储Token和role，即不能利用token登录
            //结束SplashActivity，跳转到LoginActivity
            mView.startLoginActivity();
        } else {
            //用token发起网路请求
//            HttpUtils.sendLoginRequest(UrlBuilder.getCheckTokenUrl(), token, new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    LogUtils.show(e.getMessage());
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    String json = response.body().string();
//                    LogUtils.show(json);
//                    try {
//                        //解析json
//                        JSONObject jsonObject = new JSONObject(json);
//                        String status = jsonObject.optString("status");
//
//                        //判断验证是否成功
//                        if (status.equals("SUCCESS")) {
//                            //验证成功
//                            String student = mContext.getString(R.string.student);
//                            //判断身份，决定去往那个Activity
//                            if (role.equals(student)) {
//                                mView.startCourseActivity();
//                            } else {
//                                ToastUtils.createToast("教师登录");
//                            }
//                        } else {
//                            //验证失败
//
//                            //擦除本地的token和role
//
//                            //前往login界面
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
            mView.startCourseActivity();
        }
    }
}
