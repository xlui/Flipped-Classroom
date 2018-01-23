package io.flippedclassroom.android.presenter;


import android.text.TextUtils;

import java.util.Timer;
import java.util.TimerTask;

import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.util.PreferenceUtils;
import io.flippedclassroom.android.view.SplashActivity;

public class SplashPresenter extends BasePresenter<SplashActivity> {

    public SplashPresenter(SplashActivity view) {
        super(view);
    }

    // TODO: 获取token和是学生还是老师
    public void goActivity(){
        final String token = PreferenceUtils.getToken();
        //创建Timer对象
        Timer timer = new Timer();
        //创建TimerTask对象

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(token)){
                    mView.startLoginActivity();
                }else{
                    mView.startMainActivity();
                }
            }
        };
        //使用timer.schedule（）方法调用timerTask，定时3秒后执行run
        timer.schedule(timerTask, 3000);
    }
}
