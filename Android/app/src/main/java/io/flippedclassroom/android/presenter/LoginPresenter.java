package io.flippedclassroom.android.presenter;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.util.HttpUtils;
import io.flippedclassroom.android.util.LogUtils;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.util.UrlBuilder;
import io.flippedclassroom.android.view.LoginActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginPresenter extends BasePresenter<LoginActivity> implements View.OnClickListener {

    public LoginPresenter(LoginActivity view) {
        super(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_login_show_hide_password:
                boolean isSelected = mView.changePasswordType();
                mView.showOrHidePassword(isSelected);
                break;
            case R.id.btn_login_button_login:
                final String account = mView.getAccountText();
                final String password = mView.getPasswordText();
                boolean isEmpty = checkEmpty(account, password);
                if (!isEmpty) {
                    HttpUtils.sendLoginRequest(UrlBuilder.getLoginTokenUrl(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                jsonObject.getString("status");
                                ToastUtils.createToast("登陆失败");
                            } catch (JSONException e) {
                                //获取了正确的token
                                ToastUtils.createToast("获取了token" + json);
                            }
                        }
                    }, account, password);
                }
        }
    }

    private boolean checkEmpty(String account, String password) {
        if (TextUtils.isEmpty(account)) {
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
