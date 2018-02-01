package io.flippedclassroom.android.app;


import io.flippedclassroom.android.service.RetrofitService;

public class AppCache {
    private static RetrofitService mRetrofitService;

    public static RetrofitService getRetrofitService() {
        return mRetrofitService;
    }

    public static void setRetrofitService(RetrofitService retrofitService) {
        mRetrofitService = retrofitService;
    }
}
