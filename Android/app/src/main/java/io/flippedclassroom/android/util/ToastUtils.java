package io.flippedclassroom.android.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {
    private static Context sContext;
    private static Handler mHandler = new Handler(Looper.myLooper());

    public static void init(Context context) {
        sContext = context;
    }

    public static void createToast(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(sContext, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
