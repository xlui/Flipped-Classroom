package io.flippedclassroom.android.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;

import butterknife.BindView;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.LoginPresenter;
import io.flippedclassroom.android.presenterImpl.LoginPresenterImpl;
import io.flippedclassroom.android.view.LoginView;

public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener {
    private LoginPresenter mPresenter;

    @BindView(R.id.tb_login_toolbar)
    Toolbar tbLoginToolbar;
    @BindView(R.id.btn_login_button_login)
    ActionProcessButton btnLoginButtonLogin;
    @BindView(R.id.til_login_account_text)
    TextInputLayout tilLoginAccountText;
    @BindView(R.id.til_login_password_text)
    TextInputLayout tilLoginPasswordText;
    @BindView(R.id.tv_login_forget_password)
    TextView tvLoginForgetPassword;
    @BindView(R.id.tv_login_registered)
    TextView tvLoginRegistered;
    @BindView(R.id.iv_login_show_hide_password)
    ImageView ivLoginShowHidePassword;


    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenterImpl(this, getContext());
    }

    @Override
    protected void initViews() {
        setActionBar(tbLoginToolbar, -1, "登录");
        btnLoginButtonLogin.setOnClickListener(this);
        ivLoginShowHidePassword.setOnClickListener(this);
        tvLoginForgetPassword.setOnClickListener(this);
        tvLoginRegistered.setOnClickListener(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String[] getAllTexts() {
        Editable id = tilLoginAccountText.getEditText().getText();
        Editable password = tilLoginPasswordText.getEditText().getText();
        String[] texts = new String[]{getEditText(id), getEditText(password)};
        return texts;
    }

    //判断传入的editable是不是null
    //不为null的话，返回editable的String
    //为null的话，返回null
    private String getEditText(Editable editable) {
        if (editable == null) {
            return null;
        }
        return editable.toString();
    }

    @Override
    public boolean changePasswordType() {
        if (ivLoginShowHidePassword.isSelected()) {
            ivLoginShowHidePassword.setSelected(false);
            return false;
        } else {
            ivLoginShowHidePassword.setSelected(true);
            return true;
        }
    }

    @Override
    public void showOrHidePassword(boolean needShow) {
        EditText editText = tilLoginPasswordText.getEditText();
        if (needShow) {
            //显示密码
            tilLoginPasswordText.getEditText().setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editText.setSelection(editText.getText().toString().length());
        } else {
            //不显示密码
            tilLoginPasswordText.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText.setSelection(editText.getText().toString().length());
        }
    }

    @Override
    public void changeProgressStyle(final int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnLoginButtonLogin.setProgress(progress);
            }
        });
    }

    @Override
    public void setViewsEnabled(final boolean canCheck) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnLoginButtonLogin.setEnabled(canCheck);
                tilLoginAccountText.setEnabled(canCheck);
                tilLoginPasswordText.setEnabled(canCheck);
            }
        });
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClick(v.getId());
    }

    @Override
    public void switchActivity(Intent intent, boolean needFinish) {
        startActivity(intent);
        if(needFinish){
            finish();
        }
    }
}
