package io.flippedclassroom.android.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.adapter.MoreSectionAdapter;
import io.flippedclassroom.android.adapter.SectionedSpanSizeLookup;
import io.flippedclassroom.android.base.LazyLoadFragment;
import io.flippedclassroom.android.bean.TextImageBean;
import io.flippedclassroom.android.presenter.MainPresenter;

public class MoreFragment extends LazyLoadFragment{
    private MainPresenter mPresenter;
   private RecyclerView recyclerView;
    private MoreSectionAdapter adapter;
    private List<List<TextImageBean>> list;

    @Override
    protected int setContentView() {
        return R.layout.fragment_more;
    }

    @Override
    protected void lazyLoad() {
        initData();
        init();
    }

    private void initData() {
        recyclerView = findViewById(R.id.fg_more_recycler);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),4);
        adapter = new MoreSectionAdapter(getActivity());
        manager.setSpanSizeLookup(new SectionedSpanSizeLookup(adapter,manager));
        recyclerView.setLayoutManager(manager);
    }

    private void init() {
        list = new ArrayList<>();
        for(int x=0;x<3;x++){
            List<TextImageBean> tempList = new ArrayList<>();
            for(int y=0;y<6;y++){
                TextImageBean bean = new TextImageBean();
                bean.setImageID(R.drawable.icon_ceshi);
                bean.setText(String.valueOf(x)+"  "+String.valueOf(y));
                tempList.add(bean);
            }
            list.add(tempList);
        }

        adapter.setList(list);
        recyclerView.setAdapter(adapter);
    }

    public void setPresenter(MainPresenter presenter) {
        this.mPresenter = presenter;
    }
}
