package io.flippedclassroom.android.util;

import org.json.JSONException;
import org.json.JSONObject;

import io.flippedclassroom.android.json.CourseJson;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

//Url的构造工具类
public class RetrofitUtils {
    private final static String BASE_URL = "https://fc.xd.style/";

    public interface AccountService {
        @POST("login")
        Call<ResponseBody> login(@Body RequestBody body);

        @POST("register")
        Call<ResponseBody> register(@Body RequestBody body);

        @GET("check")
        Call<ResponseBody> check(@Header("Authorization") String token);
    }

    public static RequestBody getLoginBody(String id, String password) {
        JSONObject jsonObject = new JSONObject();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        try {
            jsonObject.put("username", id);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return RequestBody.create(JSON, jsonObject.toString());
    }

    public static RequestBody getRegisteredBody(String id, String password, String role) {
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
        return RequestBody.create(JSON, json.toString());
    }

    public interface CourseService {
        @GET("course")
        Observable<CourseJson> getCourseList(@Header("Authorization") String token);

        @GET("course/delete/{courseId}")
        Call<ResponseBody> deleteCourse(@Header("Authorization") String token, @Path("courseId") int courseId);
    }
}
