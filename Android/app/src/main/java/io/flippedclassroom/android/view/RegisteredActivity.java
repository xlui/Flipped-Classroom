package io.flippedclassroom.android.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.RegisteredPresenter;

public class RegisteredActivity extends BaseActivity<RegisteredPresenter> {
    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @BindView(R.id.til_user_id)
    TextInputLayout tilUserId;
    @BindView(R.id.til_user_password)
    TextInputLayout tilUserPassword;
    @BindView(R.id.til_password_again)
    TextInputLayout tilPasswordAgain;
    @BindView(R.id.rb_student)
    RadioButton rbStudent;
    @BindView(R.id.rb_teacher)
    RadioButton rbTeacher;
    @BindView(R.id.rg_radioGroup)
    RadioGroup rgRadioGroup;
    @BindView(R.id.btn_registered)
    Button btnRegistered;

    @Override
    protected int getLayout() {
        return R.layout.activity_registered;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initViews() {
        setActionBar(tbToolbar, -1, "注册");
    }

    @Override
    public Context getContext() {
        return this;
    }

    // TODO: 获取所有textinput的text，loginActivity中有类似的代码
}
