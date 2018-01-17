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

    public void startAnimation() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mView.tvAppName, "alpha", 0, 1);
        objectAnimator.setDuration(2000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mView.startLoginActivity();
                mView.finish();
            }
        });
        objectAnimator.start();
    }

}
