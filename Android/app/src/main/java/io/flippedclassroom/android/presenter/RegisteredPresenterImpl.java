package io.flippedclassroom.android.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.model.RegisteredModel;
import io.flippedclassroom.android.model.RegisteredModelImpl;
import io.flippedclassroom.android.util.HttpUtils;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.util.UrlBuilder;
import io.flippedclassroom.android.activity.RegisteredActivity;
import io.flippedclassroom.android.view.RegisteredView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisteredPresenterImpl extends BasePresenter implements RegisteredPresenter {
    private RegisteredModel mModel;
    private RegisteredView mView;

    public RegisteredPresenterImpl(RegisteredActivity activity, Context context) {
        super(context);
        mView = activity;
        mModel = new RegisteredModelImpl();
    }

    //在Registered界面中各个View的点击逻辑
    @Override
    public void onClick(int viewId) {
        switch (viewId) {
            //如果注册按钮被点击了
            case R.id.btn_registered:
                //注册按钮开始加载
                mView.changeProgressStyle(20);
                //所有View变得不可触摸
                mView.setViewsEnabled(false);

                //获取界面所有的text，包括账号密码
                //texts[0]是账号，texts[1]是密码，texts[2]是密码的重复
                String[] texts = mView.getAllTexts();
                //判断有没有哪个需要输入的为空
                boolean isEmpty = checkEmpty(texts[0], texts[1], texts[2]);
                if (!isEmpty) {
                    //账号密码和再次输入密码都不为空

                    //判断两次输入的密码是否一致
                    if (!texts[1].equals(texts[2])) {
                        //输入不一致
                        ToastUtils.createToast("两次密码不一致");

                        //注册按钮显示注册失败
                        mView.changeProgressStyle(-1);
                        //所有View变得不可触摸
                        mView.setViewsEnabled(true);

                        return;
                    }

                    //判断有没有选角色
                    if (mModel.getRole() == null) {
                        ToastUtils.createToast("请选择身份");

                        //注册按钮显示注册失败
                        mView.changeProgressStyle(-1);
                        //所有View变得可触摸
                        mView.setViewsEnabled(true);

                        return;
                    }

                    //发起注册的网络请求
                    HttpUtils.sendRegisteredRequest(UrlBuilder.getRegisteredUrl(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            try {
                                //解析Json
                                JSONObject jsonObject = new JSONObject(json);
                                String status = jsonObject.optString("status");
                                //判断注册是是否成功
                                if (status.equals("SUCCESS")) {
                                    //注册成功

                                    //注册按钮停止加载
                                    mView.changeProgressStyle(100);
                                    //所有view变得可以触摸
                                    mView.setViewsEnabled(true);
                                    //结束注册界面，返回登录界面
                                    //因为RegisteredActivity是LoginActivity的子Activity
                                    //所以结束自身就会返回LoginActivity
                                    mView.startLoginActivity();
                                } else {
                                    //注册失败

                                    //解析注册失败的信息
                                    String message = jsonObject.optString("message");
                                    //弹出注册失败的信息
                                    ToastUtils.createToast(message);

                                    //注册按钮显示注册失败
                                    mView.changeProgressStyle(-1);
                                    //所有View变得可触摸
                                    mView.setViewsEnabled(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, texts[0], texts[1], mModel.getRole());
                } else {
                    //该填写的信息有为空的

                    //注册按钮显示注册失败
                    mView.changeProgressStyle(-1);
                    //所有View变得可触摸
                    mView.setViewsEnabled(true);
                }
                break;
        }
    }

    //判断传入的Sting是不是null
    //是的话就弹出Toast来提醒，返回true
    //不是的的话返回false
    private boolean checkEmpty(String id, String password, String passwordAgain) {
        if (TextUtils.isEmpty(id)) {
            ToastUtils.createToast("账号不能为空");
            return true;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.createToast("密码不能为空");
            return true;
        }
        if (TextUtils.isEmpty(passwordAgain)) {
            ToastUtils.createToast("密码重复不能为空");
            return true;
        }
        return false;
    }

    //保存用户选择的身份
    //如果不选，这个方法就不会被回调，那么mModel，保存的就是null
    @Override
    public void onCheckedChanged(int checkedId) {
        if (checkedId == R.id.rb_student) {
            mModel.setRole(mContext.getResources().getString(R.string.student));
        } else {
            mModel.setRole(mContext.getResources().getString(R.string.teacher));
        }
    }
}
