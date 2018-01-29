package io.flippedclassroom.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.zip.Inflater;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.bean.Course;

//课程列表的Adapter
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private static final int ITEM_TYPE_NORMAL = 1;
    private static final int ITEM_TYPE_END = 0;
    private List<Course> mCourseList;

    public CourseAdapter(List<Course> mCourseList) {
        this.mCourseList = mCourseList;
    }

    //根据位置判断显示哪种item的样式
    //除了最后一个,其余的样式都是正常课程的样子
    //最后一个item因该是加号的样式
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return ITEM_TYPE_END;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }

    //创建ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == ITEM_TYPE_NORMAL) {
            //选择课程的样式
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_course_list, parent, false);
        } else {
            //选择加号的样式
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_new_course, parent, false);
        }
        return new ViewHolder(view);
    }

    //当item进入前台被显示的时候回调
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mCourseList.size()+1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        //初始化item内部的组件
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
