package io.flippedclassroom.android.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import butterknife.ButterKnife;

//Activity的基类
public abstract class BaseActivity extends AppCompatActivity {
    private boolean isLife = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        initPresenter();
        initViews();
    }

    //考虑到子类可能直接super OnCreate这个方法，所以用这个方法来完成对ContenetView的设置
    protected abstract int getLayout();

    //判断Activity的生命周期是否存活。
    //在网络加载中可能用到，如果一个Activity在发起网络请求的途中结束，那么网络请求回来的数据就不会处理
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

    //隐藏状态栏
    protected void hideStateBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
