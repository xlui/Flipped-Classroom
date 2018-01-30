package io.flippedclassroom.android.util;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

//网络加载工具类
public class HttpUtils {

    //登录的http请求
    public static void sendLoginRequest(String url, Callback callback, String id, String password) {
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        try {
            //生成post需要的json
            jsonObject.put("username", id);
            jsonObject.put("password", password);
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder().url(url).post(body).build();
            client.newCall(request).enqueue(callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //注册的http请求
    public static void sendRegisteredRequest(String url, Callback callback, String id, String password, String role) {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        try {
            //生成post需要的json
            json.put("username", id);
            json.put("password", password);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("role", role);
            json.putOpt("role", jsonObject);
            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder().url(url).post(body).build();
            client.newCall(request).enqueue(callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //token验证的http请求
    public static void sendRequest(String url, String token, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Authorization", token).build();
        client.newCall(request).enqueue(callback);
    }
}
