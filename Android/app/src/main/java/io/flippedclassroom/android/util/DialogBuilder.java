package io.flippedclassroom.android.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;

//弹窗工具类
public class DialogBuilder {
    public static AlertDialog createProgressDialog(Context context, View view) {
        return new AlertDialog.Builder(context).setView(view).setCancelable(false).create();
    }

    public static AlertDialog createDialog(Context context, View view, String title,
                                           DialogInterface.OnClickListener listener) {
        return new AlertDialog.Builder(context).setView(view)
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null)
                .setTitle(title)
                .create();
    }
}
