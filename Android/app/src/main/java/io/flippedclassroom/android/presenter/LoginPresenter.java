package io.flippedclassroom.android.presenter;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.view.LoginActivity;

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
            return false;
        }
        return true;
    }
}
