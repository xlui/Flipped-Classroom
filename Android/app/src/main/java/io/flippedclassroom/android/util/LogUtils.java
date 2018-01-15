package io.flippedclassroom.android.util;


import android.util.Log;

public class LogUtils {
    private static String TAG = "FlippedClassroom";

    public static void show() {
        Log.e(TAG, "this");
    }

    public static void show(String message) {
        Log.e(TAG, message);
    }
}
