package io.flippedclassroom.android.presenterImpl;

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;

import io.flippedclassroom.android.app.AppCache;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.model.AddCourseModel;
import io.flippedclassroom.android.presenter.AddCoursePresenter;
import io.flippedclassroom.android.util.LogUtils;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.view.AddCourseView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCoursePresenterImpl extends BasePresenter implements AddCoursePresenter {

    AddCourseView mView;
    AddCourseModel mModel;

    public AddCoursePresenterImpl(Context context, AddCourseView view) {
        super(context);
        this.mView = view;
        mModel = new AddCourseModel(context);
    }

    @Override
    public void codeAdd(String code) {
        if (TextUtils.isEmpty(code)) {
            ToastUtils.createToast("课程代码为空");
        }

        AppCache.getRetrofitService().addCourse(mModel.getToken(), Integer.parseInt(code), new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //解析并且处理Json
                    parse(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void parse(String json) {
        //判断失败还是成功
    }
}

