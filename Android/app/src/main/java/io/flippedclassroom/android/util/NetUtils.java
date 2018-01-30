package io.flippedclassroom.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//网络工具类
public class NetUtils {
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    //判断是不是Wifi
    public static boolean isWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) sContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
}
