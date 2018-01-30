package io.flippedclassroom.android.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.presenter.ProfilePresenter;

public class ProfileActivity extends BaseActivity {
    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.tv_commit_post)
    TextView tvCommitPost;
    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @BindView(R.id.lv_user_info_list)
    ListView lvUserInfoList;
    private ProfilePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPresenter.loadUserInfo();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initViews() {
        getSwipeBackLayout().setEnableGesture(true);
        setActionBar(tbToolbar, 0, "我的主页");
    }

    @Override
    public Context getContext() {
        return this;
    }
}
