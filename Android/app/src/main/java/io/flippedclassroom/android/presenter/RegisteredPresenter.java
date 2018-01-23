package io.flippedclassroom.android.presenter;

import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.model.RegisteredModel;
import io.flippedclassroom.android.util.HttpUtils;
import io.flippedclassroom.android.util.LogUtils;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.util.UrlBuilder;
import io.flippedclassroom.android.view.RegisteredActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisteredPresenter extends BasePresenter<RegisteredActivity>
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private RegisteredModel mModel;

    public RegisteredPresenter(RegisteredActivity view) {
        super(view);
        mModel = new RegisteredModel();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_registered:
                String[] texts = mView.getAllText();
                boolean isEmpty = checkEmpty(texts[0], texts[1], texts[2]);
                if (!isEmpty) {
                    if (!texts[1].equals(texts[2])) {
                        ToastUtils.createToast("两次密码不一致");
                        return;
                    }
                    if (mModel.getRole() == null) {
                        ToastUtils.createToast("请选择身份");
                        return;
                    }
                    HttpUtils.sendRegisteredRequest(UrlBuilder.getRegisteredUrl(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String json = response.body().string();
                            LogUtils.show(json);
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                String status = jsonObject.optString("status");
                                if (status.equals("SUCCESS")) {
                                    ToastUtils.createToast("注册成功！");
                                    if (mView.isLife()) {
                                        mView.finish();
                                    }
                                } else {
                                    String message = jsonObject.optString("message");
                                    ToastUtils.createToast(message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, texts[0], texts[1], mModel.getRole());
                }
                break;
        }
    }

    private boolean checkEmpty(String id, String password, String passwordAgain) {
        if (TextUtils.isEmpty(id)) {
            Toast.makeText(mView.getContext(), "账号不能为空", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(mView.getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (TextUtils.isEmpty(passwordAgain)) {
            Toast.makeText(mView.getContext(), "密码重复不能为空", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_student) {
            mModel.setRole(mView.getContext().getResources().getString(R.string.student));
        } else {
            mModel.setRole(mView.getContext().getResources().getString(R.string.teacher));
        }
    }


}
