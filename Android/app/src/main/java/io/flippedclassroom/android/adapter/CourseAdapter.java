package io.flippedclassroom.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.bean.Course;
import io.flippedclassroom.android.presenter.CoursePresenter;
import io.flippedclassroom.android.util.LogUtils;

//课程列表的Adapter
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private List<Course> mCourseList;
    private CoursePresenter mPresenter;

    public CourseAdapter(List<Course> mCourseList, CoursePresenter presenter) {
        this.mCourseList = mCourseList;
        mPresenter = presenter;
    }

    //创建ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_list, parent, false);
        return new ViewHolder(view);
    }

    //当item进入前台被显示的时候回调
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //正常的课程显示
        //把拉取的数据显示出来
        holder.tvStudentNumber.setText("班级人数" + mCourseList.get(position).getCount());
        holder.tvCourseName.setText(mCourseList.get(position).getName());
        holder.tvClass.setText(mCourseList.get(position).getMajor());

        //创建PopMenu
        final PopupMenu popupMenu = new PopupMenu(mPresenter.getContext(), holder.ivManager);
        popupMenu.inflate(R.menu.menu_pop);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mPresenter.onClick(item.getItemId(), position);
                return true;
            }
        });

        //把点击事件迁移到Presenter
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.iv_manager) {
                    popupMenu.show();
                }
                mPresenter.onClick(v.getId(), position);
            }
        };
        holder.ivManager.setOnClickListener(listener);
        holder.ivCardBg.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mCourseList.size() ;
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
