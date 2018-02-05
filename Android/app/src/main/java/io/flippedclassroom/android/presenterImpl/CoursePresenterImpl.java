package io.flippedclassroom.android.presenterImpl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.activity.AddCourseActivity;
import io.flippedclassroom.android.activity.CourseActivity;
import io.flippedclassroom.android.activity.CourseInfoActivity;
import io.flippedclassroom.android.activity.LoginActivity;
import io.flippedclassroom.android.activity.MainActivity;
import io.flippedclassroom.android.activity.ProfileActivity;
import io.flippedclassroom.android.activity.SettingActivity;
import io.flippedclassroom.android.adapter.CourseAdapter;
import io.flippedclassroom.android.app.AppCache;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.bean.Course;
import io.flippedclassroom.android.bean.User;
import io.flippedclassroom.android.config.ConstantConfig;
import io.flippedclassroom.android.json.CourseJson;
import io.flippedclassroom.android.model.CourseModel;
import io.flippedclassroom.android.presenter.CoursePresenter;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.view.CourseView;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoursePresenterImpl extends BasePresenter implements CoursePresenter {
    private static final String TAG = "CoursePresenterImpl";
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
    public void onClick(int viewId) {
        Intent intent;
        switch (viewId) {
            case R.id.menu_login_out:
                loginOut();
                break;
            case R.id.menu_setting:
                intent = new Intent(mContext, SettingActivity.class);
                mView.switchActivity(intent, false);
                break;
            case R.id.menu_profile:
                intent = new Intent(mContext, ProfileActivity.class);
                mView.switchActivity(intent, false);
                break;
            case R.id.fab_add_course:
                intent = new Intent(mContext, AddCourseActivity.class);
                mView.switchActivity(intent, false);
                break;
        }
    }

    private void loginOut() {
        mModel.deleteId();
        mModel.deleteRole();
        mModel.deleteToken();
        Intent intent = new Intent(mContext, LoginActivity.class);
        mView.switchActivity(intent, true);
    }

    @Override
    public void onRefresh() {
        getCourseList();
        getAvatar();
        getUserInfo();
    }

    private void getAvatar() {
        AppCache.getRetrofitService().getAvatar(mModel.getToken(), new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //解析图片
                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                //设置头像
                mView.updateAvatar(bitmap);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    //从服务器拉取用户信息
    private void getUserInfo() {
        AppCache.getRetrofitService().getProfile(mModel.getToken(), new Consumer<User>() {
            @Override
            public void accept(User user) throws Exception {
                //处理返回结果
                parseUserInfo(user);
            }
        });
    }

    //对用户信息拉取结果的处理
    private void parseUserInfo(User user) {
        String nickName = user.getNickName();
        if (TextUtils.isEmpty(user.getNickName())) {
            nickName = mContext.getString(R.string.default_user_name);
        }

        String signature = user.getSignature();
        if (TextUtils.isEmpty(user.getSignature())) {
            signature = mContext.getString(R.string.default_user_description);
        }

        //更新顶部的信息
        mView.updateHeaderText(nickName, signature);
    }


    //从服务器拉取课程
    private void getCourseList() {
        AppCache.getRetrofitService().getCourseList(mModel.getToken(), new Function<CourseJson, List<Course>>() {
            @Override
            public List<Course> apply(CourseJson courseJson) throws Exception {
                return courseJson.getCoursesList();
            }
        }, new Consumer<List<Course>>() {
            @Override
            public void accept(List<Course> courseList) throws Exception {
                //处理返回的课程列表
                parseCourseList(courseList);
            }
        });
    }

    private void parseCourseList(List<Course> courseList) {
        //存入model
        mModel.saveCourseList(courseList);

        //updateList
        CourseAdapter adapter = new CourseAdapter(courseList, CoursePresenterImpl.this);
        mView.updateCourseList(adapter);
        //结束刷新
        mView.onRefreshFinish();
    }

    @Override
    public void onQuery(String queryText) {
        List<Course> list = mModel.getCourseList();
        List<Course> newList = new ArrayList<>();
        for (Course course : list) {
            //匹配课程名字
            if (queryText.equals(course.getName())) {
                newList.add(course);
            }
        }
        //刷新课程列表
        CourseAdapter adapter = new CourseAdapter(newList, this);
        mView.updateCourseList(adapter);
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

    @Override
    public void onClick(int viewId, int position) {
        Log.i(TAG, "onClick: ");
        switch (viewId) {
            //如果点击了查看课程的item
            case R.id.pop_menu_look:
                startCourseInfoActivity(position);
                break;
            //如果点击了删除课程的item
            case R.id.pop_menu_delete:
                deleteCourse(position);
                break;
            case R.id.iv_card_bg:
                startMainActivity(position);
                break;
        }
    }

    private void startMainActivity(int position) {
        Course course = mModel.getCourseList().get(position);
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(ConstantConfig.NEW_INTENT, course);
        mView.switchActivity(intent, true);
    }

    @Override
    public void parseIntent(Intent intent) {
        int result = intent.getIntExtra(ConstantConfig.NEW_INTENT, ConstantConfig.INTENT_NO_VALUE);
        if (result == -1) {
            return;
        }
        //处理新的Intent
        switch (result) {
            case ConstantConfig.INTENT_ADD_COURSE:
                mView.startRefresh();
                break;

        }
    }

    //删除课程的方法
    private void deleteCourse(final int position) {
        //获得选择课程额Id
        int courseId = mModel.getCourseList().get(position).getId();
        AppCache.getRetrofitService().deleteCourse(mModel.getToken(), courseId, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mModel.getCourseList().remove(position);
                CourseAdapter adapter = new CourseAdapter(mModel.getCourseList(), CoursePresenterImpl.this);
                mView.updateCourseList(adapter);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    //前往课程信息界面
    private void startCourseInfoActivity(int position) {
        Intent intent = new Intent(mContext, CourseInfoActivity.class);
        //把课程序列化进去
        //在创建Activity的时候重新读取数据
        intent.putExtra(ConstantConfig.NEW_INTENT, mModel.getCourseList().get(position));
        mView.switchActivity(intent, false);
    }


    @Override
    public Context getContext() {
        return mContext;
    }
}
