package io.flippedclassroom.android.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dd.processbutton.iml.ActionProcessButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.RegisteredPresenter;

//注册的Activity，显示注册界面和注册界面的生命周期
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
    ActionProcessButton btnRegistered;

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
        setActionBar(tbToolbar, 0, "注册");
        btnRegistered.setOnClickListener(mPresenter);
        rgRadioGroup.setOnCheckedChangeListener(mPresenter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    //获取所有的Text
    public String[] getAllText() {
        Editable id = tilUserId.getEditText().getText();
        Editable password = tilUserPassword.getEditText().getText();
        Editable passwordAgain = tilPasswordAgain.getEditText().getText();
        String[] texts = new String[]{getEditText(id), getEditText(password), getEditText(passwordAgain)};
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

    //依据progress的数值来决定按钮显示的状态
    //为100显示加载成功
    //1-99显示加载中
    //-1表示加载失败
    public void loading(final int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnRegistered.setProgress(progress);
            }
        });
    }

    //设置各个View的是否可以触摸
    public void setEnabled(final boolean canCheck) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnRegistered.setEnabled(canCheck);
                tilUserPassword.setEnabled(canCheck);
                tilUserId.setEnabled(canCheck);
                tilPasswordAgain.setEnabled(canCheck);
                rgRadioGroup.setEnabled(canCheck);
            }
        });
    }

    //当点击右上角的返回按钮的时候回调该方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }
}
