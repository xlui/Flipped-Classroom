package io.flippedclassroom.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import io.flippedclassroom.android.bean.Course;
import io.flippedclassroom.android.bean.User;
import io.flippedclassroom.android.config.APIs;
import io.flippedclassroom.android.json.CourseJson;
import io.flippedclassroom.android.util.RetrofitManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class RetrofitService extends Service {
    public final static String BASE_URL = "https://fc.xd.style/";

    APIs.AccountService mAccountService;
    APIs.CourseService mCourseService;

    public RetrofitService() {
        Retrofit retrofit = RetrofitManager.getRetrofit();
        mAccountService = retrofit.create(APIs.AccountService.class);
        mCourseService = retrofit.create(APIs.CourseService.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public RetrofitService getService() {
            return RetrofitService.this;
        }
    }

    //登录请求
    public void login(String id, String password, Callback<ResponseBody> callback) {
        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        try {
            jsonObject.put("username", id);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        mAccountService.login(body).enqueue(callback);
    }

    //注册请求
    public void register(String id, String password, String role, Callback<ResponseBody> callback) {
        JSONObject json = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        try {
            //生成post需要的json
            json.put("username", id);
            json.put("password", password);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("role", role);
            json.putOpt("role", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, json.toString());
        mAccountService.register(body).enqueue(callback);
    }

    //核对token请求
    public void check(String token, Callback<ResponseBody> callback) {
        mAccountService.check(token).enqueue(callback);
    }

    //获取个人信息
    public void getProfile(String token, Consumer<User> consumer) {
        mAccountService.getProfile(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    //上传用户信息
    public void postProfile(User user, String token, Callback<ResponseBody> callback) {
        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        try {
            jsonObject.put("nick_name", user.getNickName());
            jsonObject.put("gender", user.getGender());
            jsonObject.put("signature", user.getSignature());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        mAccountService.postProfile(token, body).enqueue(callback);
    }

    //拉取头像
    public void getAvatar(String token, Callback<ResponseBody> callback) {
        mAccountService.getAvatar(token).enqueue(callback);
    }

    //上传头像
    public void uploadAvatar(String token, File file, Callback<ResponseBody> callback) {
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", "avatar.png", body);
        mAccountService.uploadAvatar(token, part).enqueue(callback);
    }

    //课程列表请求
    public void getCourseList(String token, Function<CourseJson, List<Course>> function, Consumer<List<Course>> consumer) {
        mCourseService.getCourseList(token)
                .subscribeOn(Schedulers.newThread())
                .map(function)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
    }

    //删除课程请求
    public void deleteCourse(String token, int courseId, Callback<ResponseBody> callback) {
        mCourseService.deleteCourse(token, courseId).enqueue(callback);
    }


}
