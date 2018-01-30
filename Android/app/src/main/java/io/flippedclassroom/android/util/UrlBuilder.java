package io.flippedclassroom.android.util;

import io.flippedclassroom.android.json.CourseJson;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

//Url的构造工具类
public class UrlBuilder {
    private final static String BASE_URL = "https://fc.xd.style/";

    public static String getLoginTokenUrl() {
        return BASE_URL + "login";
    }

    public static String getRegisteredUrl() {
        return BASE_URL + "register";
    }

    public static String getCheckTokenUrl() {
        return BASE_URL + "check";
    }


    public interface CourseService {
        @GET("course")
        Observable<CourseJson> getCourseList(@Header("Authorization") String token);

        @GET("course/delete/{courseId}")
        Call<ResponseBody> deleteCourse(@Header("Authorization") String token, @Path("courseId") int courseId);
    }
}
