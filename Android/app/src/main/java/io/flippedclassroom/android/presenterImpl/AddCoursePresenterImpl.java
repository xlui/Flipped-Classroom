package io.flippedclassroom.android.presenterImpl;

import android.content.Context;
import android.text.TextUtils;

import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.config.APIs;
import io.flippedclassroom.android.util.PreferenceUtils;
import io.flippedclassroom.android.util.RetrofitManager;
import io.flippedclassroom.android.view.AddCourseView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by wanhao on 2018/2/3.
 */

public class AddCoursePresenterImpl extends BasePresenter implements AddCoursePresenter{

    AddCourseView view;

    public AddCoursePresenterImpl(Context mContext,AddCourseView view) {
        super(mContext);
        this.view = view;
    }
    @Override
    public void codeAdd(String code){
        if(TextUtils.isEmpty(code)){
            view.AddError("error");
            return;
        }


        APIs.CourseService service = RetrofitManager.getRetrofit().create(APIs.CourseService.class);
        String token = PreferenceUtils.getToken();
        if(TextUtils.isEmpty(token)){
            view.AddError("eror");
        }else{
            service.addCourse(token,Integer.parseInt(code))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Response<ResponseBody>>() {
                        @Override
                        public void accept(Response<ResponseBody> response) throws Exception {
                            if(response.isSuccessful()){
                                view.AddSucess();
                            }else {
                                view.AddError("error");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            view.AddError("error");
                        }
                    });
        }
    }


}

interface AddCoursePresenter{
    void codeAdd(String code);
}
