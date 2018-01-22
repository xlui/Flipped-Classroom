package io.flippedclassroom.android.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.LoginPresenter;

public class LoginActivity extends BaseActivity<LoginPresenter> {
    @BindView(R.id.tb_login_toolbar)
    public Toolbar tbLoginToolbar;
    @BindView(R.id.btn_login_button_login)
    public Button btnLoginButtonLogin;
    @BindView(R.id.til_login_account_text)
    public TextInputLayout tilLoginAccountText;
    @BindView(R.id.til_login_password_text)
    public TextInputLayout tilLoginPasswordText;
    @BindView(R.id.tv_login_forget_password)
    public TextView tvLoginForgetPassword;
    @BindView(R.id.tv_login_registered)
    public TextView tvLoginRegistered;
    @BindView(R.id.iv_login_show_hide_password)
    public ImageView ivLoginShowHidePassword;


    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter(this);
    }

    @Override
    protected void initViews() {
        setActionBar(tbLoginToolbar, -1, "登录");
        btnLoginButtonLogin.setOnClickListener(mPresenter);
        ivLoginShowHidePassword.setOnClickListener(mPresenter);
        tvLoginForgetPassword.setOnClickListener(mPresenter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    public String[] getAllText() {
        Editable id = tilLoginAccountText.getEditText().getText();
        Editable password = tilLoginPasswordText.getEditText().getText();
        String[] texts = new String[]{getEditText(id), getEditText(password)};
        return texts;
    }

    private String getEditText(Editable editable) {
        if (editable == null) {
            return null;
        }
        return editable.toString();
    }

    public boolean changePasswordType() {
        if (ivLoginShowHidePassword.isSelected()) {
            ivLoginShowHidePassword.setSelected(false);
            return false;
        } else {
            ivLoginShowHidePassword.setSelected(true);
            return true;
        }
    }

    public void showOrHidePassword(boolean needShow) {
        EditText editText = tilLoginPasswordText.getEditText();
        if (needShow) {
            tilLoginPasswordText.getEditText().setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editText.setSelection(editText.getText().toString().length());
        } else {
            tilLoginPasswordText.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText.setSelection(editText.getText().toString().length());
        }
    }

    public void startRegisteredActivity() {
        startActivity(new Intent(this, RegisteredActivity.class));
    }
}
