package io.flippedclassroom.android.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {
    protected T mPresenter;
    private boolean isLife = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initPresenter();
        initViews();
    }

    protected abstract int getLayout();

    public boolean isLife() {
        return isLife;
    }

    protected abstract void initPresenter();

    protected abstract void initViews();

    protected abstract Context getContext();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isLife = false;
    }
}
