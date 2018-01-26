package io.flippedclassroom.android.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

//Toast工具类，方便Toast的显示
public class ToastUtils {
    private static Context sContext;
    private static Handler mHandler = new Handler(Looper.myLooper());

    public static void init(Context context) {
        sContext = context;
    }

    //考虑到很多时候Toast都是在子线程中需要弹出，所以使用Handler来切换到主线程显示Toast
    //如果本身就在主线程中，Handler亦不会影响性能
    public static void createToast(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(sContext, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
