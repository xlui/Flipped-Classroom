package io.flippedclassroom.android.presenterImpl;


import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.model.SplashModel;
import io.flippedclassroom.android.presenter.SplashPresenter;
import io.flippedclassroom.android.util.LogUtils;
import io.flippedclassroom.android.util.RetrofitManager;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.activity.SplashActivity;
import io.flippedclassroom.android.util.RetrofitUtils;
import io.flippedclassroom.android.view.SplashView;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class SplashPresenterImpl extends BasePresenter implements SplashPresenter {
    private SplashModel mModel;
    private SplashView mView;

    public SplashPresenterImpl(SplashActivity activity, Context context) {
        super(context);
        mView = activity;
        mModel = new SplashModel(mContext);
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
            Retrofit retrofit = RetrofitManager.getRetrofit();
            RetrofitUtils.AccountService accountService = retrofit.create(RetrofitUtils.AccountService.class);
            accountService.check(token).enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        String json = response.body().string();
                        parse(json, role);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    private void parse(String json, String role) {
        try {
            //解析json
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.optString("status");

            //判断验证是否成功
            if (status.equals("SUCCESS")) {
                //验证成功
                String student = mContext.getString(R.string.student);
                //判断身份，决定去往那个Activity
                if (role.equals(student)) {
                    mView.startCourseActivity();
                } else {
                    ToastUtils.createToast("教师登录");
                }
            } else {
                //验证失败

                //擦除本地的token,role,id
                mModel.deleteToken();
                mModel.deleteRole();
                mModel.deleteId();
                //前往login界面
                mView.startLoginActivity();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
