package io.flippedclassroom.android.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.model.LoginModel;
import io.flippedclassroom.android.util.HttpUtils;
import io.flippedclassroom.android.util.LogUtils;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.util.UrlBuilder;
import io.flippedclassroom.android.view.LoginActivity;
import io.flippedclassroom.android.view.RegisteredActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//Login的Presenter
public class LoginPresenter extends BasePresenter<LoginActivity> implements View.OnClickListener {
    private LoginModel mModel;

    public LoginPresenter(LoginActivity view) {
        super(view);
        mModel = new LoginModel();
    }

    //在Login界面中各个View的点击逻辑
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //如果点击了  显示/隐藏  密码的按钮
            case R.id.iv_login_show_hide_password:
                boolean isSelected = mView.changePasswordType();
                mView.showOrHidePassword(isSelected);
                break;
            //如果点击了登录的按钮
            case R.id.btn_login_button_login:
                //登录按钮开始加载
                mView.loading(20);
                //所有View变得不可触摸
                mView.setEnabled(false);
                //获取界面所有的text，包括账号密码
                //texts[0]是账号，texts[1]是密码
                final String[] texts = mView.getAllText();
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
                                    //登录按钮停止加载
                                    mView.loading(100);
                                    //所有view变得可以触摸
                                    mView.setEnabled(true);
                                } else {
                                    //登陆失败
                                    ToastUtils.createToast("账号密码错误");

                                    //登录按钮显示登陆失败
                                    mView.loading(-1);
                                    //所有View变得可触摸
                                    mView.setEnabled(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, texts[0], texts[1]);
                } else {
                    //该填写的信息有为空的

                    //登录按钮显示登陆失败
                    mView.loading(-1);
                    //所有View变得可触摸
                    mView.setEnabled(true);
                }
                break;
            //如果点击了注册按钮
            case R.id.tv_login_registered:
                mView.getContext().startActivity(new Intent(mView.getContext(), RegisteredActivity.class));
        }
    }

    //判断传入的Sting是不是null
    //是的话就弹出Toast来提醒，返回true
    //不是的的话返回false
    private boolean checkEmpty(String id, String password) {
        if (TextUtils.isEmpty(id)) {
            Toast.makeText(mView.getContext(), "账号不能为空", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(mView.getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


}
