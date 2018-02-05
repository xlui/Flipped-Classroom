package io.flippedclassroom.android.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.bean.TextImageBean;


public class MoreSectionAdapter extends SectionedRecyclerViewAdapter<CountHeaderViewHolder,
        CountItemViewHolder,
        CountFooterViewHolder> {

    private static final String TAG = "CountSectionAdapter";

    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private List<List<TextImageBean>> list;

    private String[] strs = {"上课","课后","其他"};

    public MoreSectionAdapter(Context context){
        this.context = context;
    }

    public MoreSectionAdapter(Context context,  List<List<TextImageBean>> list){
        this.context = context;
        this.list = list;
    }

    public void setList(List<List<TextImageBean>> list){
        this.list = list;
    }

    //  控制每个section显示的item数量
    @Override
    protected int getItemCountForSection(int section) {
        Log.i(TAG, "getItemCountForSection: "+section);
        return list.get(section).size();
    }
    //控制section数量
    @Override
    protected int getSectionCount() {
        return list.size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    protected LayoutInflater getLayoutInflater(){
        return LayoutInflater.from(context);
    }

    @Override
    protected CountHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_more_head, parent, false);
        return new CountHeaderViewHolder(view);
    }

    @Override
    protected CountFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_more_footer, parent, false);
        return new CountFooterViewHolder(view);
    }

    @Override
    protected CountItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_more_item, parent, false);
        return new CountItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(CountHeaderViewHolder holder, int section) {
        holder.render(strs[section]);
    }

    @Override
    protected void onBindSectionFooterViewHolder(CountFooterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(CountItemViewHolder holder, int section, int position) {
        holder.render(section,position,
                list.get(section).get(position).getText(),
                list.get(section).get(position).getImageID());

        holder.setOnClickListener(new CountItemViewHolder.OnClickListener() {
            @Override
            public void onClick(int section, int position) {
                if(onItemClickListener!=null)
                    onItemClickListener.onItemClick(section,position);
            }
        });

        holder.setOnLongClickListener(new CountItemViewHolder.OnLongClickListener() {
            @Override
            public void onLongClick(int section, int position) {
                if(onItemLongClickListener!=null)
                    onItemLongClickListener.onLongItemClick(section,position);
            }
        });
    }


    public interface OnItemClickListener {
        public void onItemClick(int section, int position);
    }

    public void setOnItemClickListener(OnItemClickListener on){
        this.onItemClickListener = on;
    }

    public interface OnItemLongClickListener {
        public void onLongItemClick(int section, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener on){
        this.onItemLongClickListener = on;
    }
}