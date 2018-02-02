package io.flippedclassroom.android.presenterImpl;


import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.app.AppCache;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.config.PermissionConfig;
import io.flippedclassroom.android.model.SplashModel;
import io.flippedclassroom.android.presenter.SplashPresenter;
import io.flippedclassroom.android.util.RetrofitManager;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.activity.SplashActivity;
import io.flippedclassroom.android.view.SplashView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
            AppCache.getRetrofitService().check(mModel.getToken(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String json = response.body().string();
                        parse(json, role);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void requestPermissions() {
        //权限申请
        List<String> permissions = new ArrayList<>();
        for (String permission : PermissionConfig.permissions) {
            //逐条权限核对
            if (ContextCompat.checkSelfPermission(mContext.getApplicationContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(permission);
            }
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions((SplashActivity) mContext,
                    permissions.toArray(new String[permissions.size()]), 1);
        } else {
            mView.bindService();
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
