package io.flippedclassroom.android.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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
        mPresenter = new RegisteredPresenter(this);
    }

    @Override
    protected void initViews() {
        setActionBar(tbToolbar, -1, "注册");
        btnRegistered.setOnClickListener(mPresenter);
        rgRadioGroup.setOnCheckedChangeListener(mPresenter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    public String[] getAllText() {
        Editable id = tilUserId.getEditText().getText();
        Editable password = tilUserPassword.getEditText().getText();
        Editable passwordAgain = tilPasswordAgain.getEditText().getText();
        String[] texts = new String[]{getEditText(id), getEditText(password),getEditText(passwordAgain)};
        return texts;
    }

    private String getEditText(Editable editable) {
        if (editable == null) {
            return null;
        }
        return editable.toString();
    }


}
