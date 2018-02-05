package io.flippedclassroom.android.view;

import android.content.Intent;

public interface SwitchActivityInterface {
    //打开新的Activity
    void switchActivity(Intent intent, boolean needFinish);
}
