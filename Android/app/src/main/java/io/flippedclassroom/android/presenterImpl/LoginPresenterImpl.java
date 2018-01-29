package io.flippedclassroom.android.presenterImpl;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.activity.LoginActivity;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.model.LoginModel;
import io.flippedclassroom.android.presenter.LoginPresenter;
import io.flippedclassroom.android.util.HttpUtils;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.util.UrlBuilder;
import io.flippedclassroom.android.view.LoginView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//Login的Presenter
public class LoginPresenterImpl extends BasePresenter implements LoginPresenter {
    private LoginModel mModel;
    private LoginView mView;

    public LoginPresenterImpl(LoginActivity activity, Context context) {
        super(context);
        mView = activity;
        mModel = new LoginModel(mContext);
    }

    //在Login界面中各个View的点击逻辑
    @Override
    public void onClick(int viewId) {
        switch (viewId) {
            //如果点击了  显示/隐藏  密码的按钮
            case R.id.iv_login_show_hide_password:
                boolean isSelected = mView.changePasswordType();
                mView.showOrHidePassword(isSelected);
                break;
            //如果点击了登录的按钮
            case R.id.btn_login_button_login:
                //登录按钮开始加载
                mView.changeProgressStyle(20);
                //所有View变得不可触摸
                mView.setViewsEnabled(false);
                //获取界面所有的text，包括账号密码
                //texts[0]是账号，texts[1]是密码
                final String[] texts = mView.getAllTexts();
                //判断有没有哪个需要输入的为空
                boolean isEmpty = checkEmpty(texts[0], texts[1]);
                if (!isEmpty) {
                    //如果账号密码都不为空

                    //发起登录的网络请求
                    HttpUtils.sendLoginRequest(UrlBuilder.getLoginTokenUrl(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                String status = jsonObject.optString("status");
                                if (status.equals("SUCCESS")) {
                                    //登陆成功
                                    //解析JSON
                                    String token = jsonObject.optString("token");
                                    String role = jsonObject.optString("role");
                                    //保存token和role
                                    mModel.saveToken(token);
                                    mModel.saveRole(role);
                                    mModel.saveId(texts[0]);
                                    //登录按钮停止加载
                                    mView.changeProgressStyle(100);
                                    //所有view变得可以触摸
                                    mView.setViewsEnabled(true);

                                    if (role.equals(mContext.getString(R.string.student))) {
                                        //前往学生界面
                                        mView.startCourseActivity();
                                    } else {

                                    }
                                } else {
                                    //登陆失败
                                    ToastUtils.createToast("账号密码错误");

                                    //登录按钮显示登陆失败
                                    mView.changeProgressStyle(-1);
                                    //所有View变得可触摸
                                    mView.setViewsEnabled(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, texts[0], texts[1]);
                } else {
                    //该填写的信息有为空的

                    //登录按钮显示登陆失败
                    mView.changeProgressStyle(-1);
                    //所有View变得可触摸
                    mView.setViewsEnabled(true);
                }
                break;
            //如果点击了注册按钮
            case R.id.tv_login_registered:
                mView.startRegisteredActivity();
        }
    }

    //判断传入的Sting是不是null
    //是的话就弹出Toast来提醒，返回true
    //不是的的话返回false
    private boolean checkEmpty(String id, String password) {
        if (TextUtils.isEmpty(id)) {
            ToastUtils.createToast("账号不能为空");
            return true;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.createToast("密码不能为空");
            return true;
        }
        return false;
    }


}
