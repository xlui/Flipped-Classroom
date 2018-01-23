package io.flippedclassroom.android.presenter;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.animation.Animation;

import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.view.SplashActivity;

public class SplashPresenter extends BasePresenter<SplashActivity> {

    public SplashPresenter(SplashActivity view) {
        super(view);
    }

    // TODO: 获取token和是学生还是老师
}
