package io.flippedclassroom.android.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import butterknife.ButterKnife;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    protected T mPresenter;
    private boolean isLife = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        initPresenter();
        initViews();
    }

    protected abstract int getLayout();

    public boolean isLife() {
        return isLife;
    }

    protected abstract void initPresenter();

    protected abstract void initViews();

    public abstract Context getContext();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isLife = false;
    }

    //设置actionbar
    //imageId传入0就是显示返回箭头，-1就是什么都不现实
    protected void setActionBar(Toolbar toolbar, int imageId, String title) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        if (imageId != -1) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (imageId != 0) {
                actionBar.setHomeAsUpIndicator(imageId);
            }
        }
    }

    protected void hideStateBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
