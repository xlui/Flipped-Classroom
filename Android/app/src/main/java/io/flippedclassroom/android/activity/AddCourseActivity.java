package io.flippedclassroom.android.activity;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acker.simplezxing.activity.CaptureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.AddCoursePresenter;
import io.flippedclassroom.android.presenter.CoursePresenter;
import io.flippedclassroom.android.presenterImpl.AddCoursePresenterImpl;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.view.AddCourseView;

public class AddCourseActivity extends BaseActivity implements View.OnClickListener, AddCourseView {

    AddCoursePresenter mPresenter;

    @BindView(R.id.ac_choose_fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.ac_choose_add)
    Button abtAdd;
    @BindView(R.id.ac_choose_ed)
    EditText editText;
    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @Override
    protected int getLayout() {
        return R.layout.activity_add_course;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new AddCoursePresenterImpl(this.getContext(), this);
    }

    @Override
    protected void initViews() {
        getSwipeBackLayout().setEnableGesture(true);
        setActionBar(tbToolbar, 0, "添加课程");
        abtAdd.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ac_choose_add:
                mPresenter.codeAdd(editText.getText().toString());
                break;
            case R.id.ac_choose_fab:
                startActivityForResult(new Intent(this, CaptureActivity.class), CaptureActivity.REQ_CODE);
                break;
        }
    }

    @Override
    public void AddSuccess() {
        ToastUtils.createToast("添加成功");
        //返回主界面应该自动刷新
        Intent intent = new Intent(this, CourseActivity.class);
        intent.putExtra(CoursePresenter.NEW_INTENT, CoursePresenter.ADD_COURSE);
        startActivity(intent);
        finish();
    }

    @Override
    public void AddError(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        editText.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        mPresenter.codeAdd(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            editText.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                            mPresenter.codeAdd(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        }
                        break;
                }
                break;
        }
    }
}
