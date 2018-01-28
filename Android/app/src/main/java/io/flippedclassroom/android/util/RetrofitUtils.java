package io.flippedclassroom.android.util;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//Retrofit工具类
public class RetrofitUtils {
    private static Retrofit sRetrofit;

    //单例模式，避免反复重新创造
    public static void init() {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder().baseUrl(UrlBuilder.getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }

    public static Retrofit getRetrofit() {
        return sRetrofit;
    }
}
