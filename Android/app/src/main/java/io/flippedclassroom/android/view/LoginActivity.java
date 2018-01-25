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
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;

import butterknife.BindView;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.LoginPresenter;

//登录的Activity，显示登录界面和登录界面的生命周期
public class LoginActivity extends BaseActivity<LoginPresenter> {
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
        mPresenter = new LoginPresenter(this);
    }

    @Override
    protected void initViews() {
        setActionBar(tbLoginToolbar, -1, "登录");
        btnLoginButtonLogin.setOnClickListener(mPresenter);
        ivLoginShowHidePassword.setOnClickListener(mPresenter);
        tvLoginForgetPassword.setOnClickListener(mPresenter);
        tvLoginRegistered.setOnClickListener(mPresenter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    //获取所有的Text
    public String[] getAllText() {
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

    //按照点击的情况选择显示还是隐藏密码的icon
    //设置为setSelected(false)，icon为闭着眼睛
    //设置为setSelected(true)，icon为张开眼睛
    public boolean changePasswordType() {
        if (ivLoginShowHidePassword.isSelected()) {
            ivLoginShowHidePassword.setSelected(false);
            return false;
        } else {
            ivLoginShowHidePassword.setSelected(true);
            return true;
        }
    }

    //依据传入的boolean参数决定是不是显示密码为可见
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

    //依据progress的数值来决定按钮显示的状态
    //为100显示加载成功
    //1-99显示加载中
    //-1表示加载失败
    public void loading(final int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnLoginButtonLogin.setProgress(progress);
            }
        });
    }

    //设置各个View的是否可以触摸
    public void setEnabled(final boolean canCheck) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnLoginButtonLogin.setEnabled(canCheck);
                tilLoginAccountText.setEnabled(canCheck);
                tilLoginPasswordText.setEnabled(canCheck);
            }
        });
    }

}
