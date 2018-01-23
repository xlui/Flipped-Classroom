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

public class LoginPresenter extends BasePresenter<LoginActivity> implements View.OnClickListener {
    private LoginModel mModel;

    public LoginPresenter(LoginActivity view) {
        super(view);
        mModel = new LoginModel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login_show_hide_password:
                boolean isSelected = mView.changePasswordType();
                mView.showOrHidePassword(isSelected);
                break;
            case R.id.btn_login_button_login:
                mView.loading(20);
                mView.setEnabled(false);

                final String[] texts = mView.getAllText();
                boolean isEmpty = checkEmpty(texts[0], texts[1]);
                if (!isEmpty) {
                    HttpUtils.sendLoginRequest(UrlBuilder.getLoginTokenUrl(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            LogUtils.show(json);
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                String status = jsonObject.optString("status");
                                if (status.equals("SUCCESS")) {
                                    //保存token和role
                                    String token = jsonObject.optString("token");
                                    String role = jsonObject.optString("role");

                                    mModel.saveToken(token);
                                    mModel.saveRole(role);

                                    mView.loading(100);
                                    mView.setEnabled(true);
                                } else {
                                    ToastUtils.createToast("账号密码错误");

                                    mView.loading(-1);
                                    mView.setEnabled(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, texts[0], texts[1]);
                } else {
                    mView.loading(-1);
                    mView.setEnabled(true);
                }
                break;
            case R.id.tv_login_registered:
                mView.getContext().startActivity(new Intent(mView.getContext(), RegisteredActivity.class));
        }
    }

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
