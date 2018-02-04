package io.flippedclassroom.android.config;

import io.flippedclassroom.android.bean.User;
import io.flippedclassroom.android.json.CourseJson;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

//API配置
public class APIs {
    public interface AccountService {
        @POST("login")
        Call<ResponseBody> login(@Body RequestBody body);

        @POST("register")
        Call<ResponseBody> register(@Body RequestBody body);

        @GET("check")
        Call<ResponseBody> check(@Header("Authorization") String token);

        @GET("profile")
        Observable<User> getProfile(@Header("Authorization") String token);

        @POST("profile")
        Call<ResponseBody> postProfile(@Header("Authorization") String token, @Body RequestBody body);

        @GET("avatar")
        Call<ResponseBody> getAvatar(@Header("Authorization") String token);

        @Multipart
        @POST("avatar")
        Call<ResponseBody> uploadAvatar(@Header("Authorization") String token, @Part MultipartBody.Part part);
    }

    public interface CourseService {
        @GET("course")
        Observable<CourseJson> getCourseList(@Header("Authorization") String token);

        @GET("course/{courseId}/delete")
        Call<ResponseBody> deleteCourse(@Header("Authorization") String token, @Path("courseId") int courseId);

        @GET("course/{courseId}/join")

        Call<ResponseBody> addCourse(@Header("Authorization") String token, @Path("courseId") int courseId);
    }

}
