package io.flippedclassroom.android.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.presenter.MainPresenter;

public class MaterialFragment extends Fragment{
    private MainPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_material,container,false);
    }

    public void setPresenter(MainPresenter presenter) {
        this.mPresenter = presenter;
    }
}
