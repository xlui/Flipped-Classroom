package io.flippedclassroom.android.presenterImpl;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

import io.flippedclassroom.android.activity.CourseActivity;
import io.flippedclassroom.android.adapter.CourseAdapter;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.bean.Course;
import io.flippedclassroom.android.json.CourseJson;
import io.flippedclassroom.android.model.CourseModel;
import io.flippedclassroom.android.presenter.CoursePresenter;
import io.flippedclassroom.android.util.LogUtils;
import io.flippedclassroom.android.util.RetrofitUtils;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.util.UrlBuilder;
import io.flippedclassroom.android.view.CourseView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CoursePresenterImpl extends BasePresenter implements CoursePresenter {
    private Handler mHandler = new Handler(Looper.myLooper());
    private boolean canQuit = false;
    private CourseView mView;
    private CourseModel mModel;

    public CoursePresenterImpl(CourseActivity activity, Context mContext) {
        super(mContext);
        mView = activity;
        mModel = new CourseModel(mContext);
    }

    @Override
    public void onClick(int ViewId) {

    }

    @Override
    public void onRefresh() {
        //发起网络请求,拉取课程列表
        Retrofit retrofit = RetrofitUtils.getRetrofit();
        UrlBuilder.CourseService courseService = retrofit.create(UrlBuilder.CourseService.class);
        courseService.getCourseList(mModel.getToken())
                //map变换
                .map(new Function<CourseJson, List<Course>>() {
                    @Override
                    public List<Course> apply(CourseJson courseJson) throws Exception {
                        return courseJson.getCoursesList();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Course>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Course> courseList) {
                        LogUtils.show(courseList.get(0).getName());
                        ToastUtils.createToast("加载成功");
                        //存入model
                        mModel.saveCourseList(courseList);

                        //updateList
                        CourseAdapter adapter = new CourseAdapter(courseList, CoursePresenterImpl.this);
                        mView.updateCourseList(adapter);
                        //结束刷新
                        mView.onRefreshFinish();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onQuery(String queryText) {

    }

    @Override
    public void onSearchClose() {
        //如果关闭了搜索，恢复之前的样子
        CourseAdapter adapter = new CourseAdapter(mModel.getCourseList(), this);
        mView.updateCourseList(adapter);
    }

    @Override
    public void onBack() {
        //两次点击退出应用的逻辑
        if (canQuit) {
            //如果快速点击两次就会到这里
            System.exit(0);
        } else {
            //单击一次
            canQuit = true;
            ToastUtils.createToast("再次点击退出应用");
            //两秒以后把标志重新置为false
            //如果两秒以内再次点击了返回，if判断成功，整个应用结束
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    canQuit = false;
                }
            }, 2000);
        }
    }
}
