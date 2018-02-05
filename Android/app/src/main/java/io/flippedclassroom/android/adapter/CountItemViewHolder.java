package io.flippedclassroom.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.flippedclassroom.android.R;

/**
 * Created by wanhao on 2017/10/21.
 */

class CountItemViewHolder extends RecyclerView.ViewHolder {
    TextView txTitle;
    CircleImageView image;
    LinearLayout layout;


    private int section;
    private int position;
    private OnClickListener onClickListener = null;
    private OnLongClickListener onLongClickListener = null;


    public CountItemViewHolder(View view) {
        super(view);
        txTitle = view.findViewById(R.id.item_more_title);
        image = view.findViewById(R.id.item_more_image);
        layout = view.findViewById(R.id.item_more_layout);
    }

    public void render(final int section, final int position, String title, int colorId) {
        this.position = position;
        this.section = section;
        image.setImageResource(colorId);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onClick(section,position);
                }
            }
        });

        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onLongClickListener!=null)
                    onLongClickListener.onLongClick(section,position);
                return true;
            }
        });

        txTitle.setText(title);
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        public void onClick(int section, int position);
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener){
        this.onLongClickListener = onLongClickListener;
    }

    public interface OnLongClickListener {
        public void onLongClick(int section, int position);
    }
}
