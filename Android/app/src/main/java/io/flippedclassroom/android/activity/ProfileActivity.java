package io.flippedclassroom.android.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.ProfilePresenter;
import io.flippedclassroom.android.presenterImpl.ProfilePresenterImpl;
import io.flippedclassroom.android.util.DialogBuilder;
import io.flippedclassroom.android.view.ProfileView;

public class ProfileActivity extends BaseActivity implements ProfileView, ListView.OnItemClickListener, View.OnClickListener {
    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.btn_commit_post)
    Button btnCommitPost;
    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @BindView(R.id.lv_user_info_list)
    ListView lvUserInfoList;
    private ProfilePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.loadUserInfo();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ProfilePresenterImpl(this, getContext());
    }

    @Override
    protected void initViews() {
        getSwipeBackLayout().setEnableGesture(true);
        setActionBar(tbToolbar, 0, "我的主页");
        //初始化一些弹窗
        initDialogs();
        lvUserInfoList.setOnItemClickListener(this);
        btnCommitPost.setOnClickListener(this);
    }

    //加载Dialog
    private AlertDialog mProgressDialog;
    private View mProgressDialogView;
    //文字编辑Dialog
    private AlertDialog mTextEditDialog;
    private View mTextEditDialogView;
    //选择Dialog
    private AlertDialog mChooseDialog;
    private View mChooseDialogView;

    private void initDialogs() {
        mProgressDialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading, null);
        mProgressDialog = DialogBuilder.createProgressDialog(getContext(), mProgressDialogView);

        mTextEditDialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_text_edit, null);
        mTextEditDialog = DialogBuilder.createDialog(getContext(), mTextEditDialogView, "编辑",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.onDialogClick(DialogType.DIALOG_TYPE_TEXT_EDIT, mTextEditDialogView);
                    }
                });

        mChooseDialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_choose, null);
        mChooseDialog = DialogBuilder.createDialog(getContext(), mChooseDialogView, "选择",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.onDialogClick(DialogType.DIALOG_TYPE_CHOOSE, mChooseDialogView);
                    }
                });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.cancel();
            }
        });
    }

    @Override
    public void showEditDialog() {
        EditText editText = mTextEditDialogView.findViewById(R.id.et_info_edit);
        editText.setText("");
        mTextEditDialog.show();
    }

    @Override
    public void updateList(ListAdapter adapter) {
        lvUserInfoList.setAdapter(adapter);
    }

    @Override
    public void showChooseDialog() {
        mChooseDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPresenter.onClick(view.getId(), position);
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClick(v.getId());
    }
}
