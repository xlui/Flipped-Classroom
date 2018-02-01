package io.flippedclassroom.android.util;

import io.flippedclassroom.android.service.RetrofitService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//Retrofit工具类
public class RetrofitManager {
    private static Retrofit sRetrofit;

    //单例模式，避免反复重新创造
    public static void init() {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder().baseUrl(RetrofitService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }

    public static Retrofit getRetrofit() {
        return sRetrofit;
    }
}
