package io.flippedclassroom.android.presenterImpl;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.bean.Course;
import io.flippedclassroom.android.config.ConstantConfig;
import io.flippedclassroom.android.fragment.MaterialFragment;
import io.flippedclassroom.android.fragment.MoreFragment;
import io.flippedclassroom.android.fragment.TestFragment;
import io.flippedclassroom.android.model.MainModel;
import io.flippedclassroom.android.presenter.MainPresenter;
import io.flippedclassroom.android.view.MainView;

public class MainPresenterImpl extends BasePresenter implements MainPresenter {
    private MainView mView;
    private MainModel mModel;

    public MainPresenterImpl(Context context, MainView view) {
        super(context);
        mView = view;
        mModel = new MainModel(context);
    }

    @Override
    public void onClick(int viewId) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void parseIntent(Intent intent) {
        Course course = intent.getParcelableExtra(ConstantConfig.NEW_INTENT);
        //暂存当前课程
        mModel.setCurrentCourse(course);
        //设置toolbar的title
        mView.setToolbar(course.getName());
    }

    @Override
    public void createContent(FragmentManager fragmentManager) {
        //主界面3块fragment
        MaterialFragment materialFragment = new MaterialFragment();
        materialFragment.setPresenter(this);

        TestFragment testFragment = new TestFragment();
        testFragment.setPresenter(this);

        MoreFragment moreFragment = new MoreFragment();
        moreFragment.setPresenter(this);

        //生成fragment的list
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(materialFragment);
        fragments.add(testFragment);
        fragments.add(moreFragment);

        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
//        //构建viewpager的adapter
//        FragmentPagerAdapter adapter = new FragmentPagerAdapter(fragmentManager) {
//            @Override
//            public Fragment getItem(int position) {
//                return fragments.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return fragments.size();
//            }
//        };

        mView.updateContent(adapter);
    }
}
