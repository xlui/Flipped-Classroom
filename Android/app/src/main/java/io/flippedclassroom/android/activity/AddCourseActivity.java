package io.flippedclassroom.android.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acker.simplezxing.activity.CaptureActivity;

import butterknife.BindView;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenterImpl.AddCoursePresenterImpl;
import io.flippedclassroom.android.view.AddCourseView;

public class AddCourseActivity extends BaseActivity implements View.OnClickListener,AddCourseView{
    private static final String TAG = "AddCourseActivity";

    AddCoursePresenterImpl presenter;

    @BindView(R.id.ac_choose_fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.ac_choose_add)Button abtAdd;
    @BindView(R.id.ac_choose_ed)EditText editText;

    @Override
    protected int getLayout() {
        return R.layout.activity_add_course;
    }

    @Override
    protected void initPresenter() {
        presenter = new AddCoursePresenterImpl(this,this);
    }

    @Override
    protected void initViews() {
        abtAdd.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ac_choose_add:
                presenter.codeAdd(editText.getText().toString());
                break;
            case R.id.ac_choose_fab:
                startActivityForResult(new Intent(this, CaptureActivity.class), CaptureActivity.REQ_CODE);
                break;
        }
    }

    @Override
    public void AddSucess() {
        Toast.makeText(this,"sucess",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void AddError(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        editText.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        presenter.codeAdd(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            editText.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                            presenter.codeAdd(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        }
                        break;
                }
                break;
        }
    }
}
