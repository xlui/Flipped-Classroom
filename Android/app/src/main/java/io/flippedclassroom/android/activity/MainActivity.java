package io.flippedclassroom.android.activity;

import android.content.Context;
import android.os.Bundle;

import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import butterknife.BindView;
import io.flippedclassroom.android.R;
import io.flippedclassroom.android.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @BindView(R.id.ac_main_bmb)
    BoomMenuButton bmb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    //.normalImageRes(R.drawable.main_icon_1)
                    .normalText("Butter Doesn't fly!");
            bmb.addBuilder(builder);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    public Context getContext() {
        return null;
    }
}
