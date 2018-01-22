package io.flippedclassroom.android.util;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtils {

    public static void sendLoginRequest(String url, Callback callback, String id, String password) {
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        try {
            jsonObject.put("username",id);
            jsonObject.put("password",password);
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder().url(url).post(body).build();
            client.newCall(request).enqueue(callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
