package io.flippedclassroom.android.util;


import android.util.Log;

//打印log的工具类，方便打印
public class LogUtils {
    private static String TAG = "FlippedClassroom";

    public static void show() {
        Log.e(TAG, "this");
    }

    public static void show(String message) {
        Log.e(TAG, message);
    }
}
