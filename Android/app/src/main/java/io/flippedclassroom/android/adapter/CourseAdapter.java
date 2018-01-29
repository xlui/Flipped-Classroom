package io.flippedclassroom.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.bean.Course;
import io.flippedclassroom.android.presenter.CoursePresenter;
import io.flippedclassroom.android.util.LogUtils;

//课程列表的Adapter
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private static final int ITEM_TYPE_NORMAL = 1;
    private static final int ITEM_TYPE_END = 0;
    private List<Course> mCourseList;
    private CoursePresenter mPresenter;

    public CourseAdapter(List<Course> mCourseList, CoursePresenter presenter) {
        this.mCourseList = mCourseList;
        mPresenter = presenter;
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_list, parent, false);
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
        if (getItemViewType(position) == ITEM_TYPE_NORMAL) {
            holder.tvStudentNumber.setText("班级人数"+mCourseList.get(position).getCount());
            holder.tvCourseName.setText(mCourseList.get(position).getName());
            holder.tvClass.setText(mCourseList.get(position).getMajor());
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return mCourseList.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCardBg;
        TextView tvClass;
        TextView tvCourseName;
        ImageView ivManager;
        TextView tvStudentNumber;

        //初始化item内部的组件
        public ViewHolder(View itemView) {
            super(itemView);
            ivCardBg = itemView.findViewById(R.id.iv_card_bg);
            tvStudentNumber = itemView.findViewById(R.id.tv_student_number);
            tvClass = itemView.findViewById(R.id.tv_class);
            tvCourseName = itemView.findViewById(R.id.tv_course_name);
            ivManager = itemView.findViewById(R.id.iv_manager);

        }
    }
}
