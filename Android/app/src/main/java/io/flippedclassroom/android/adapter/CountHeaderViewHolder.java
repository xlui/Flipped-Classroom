package io.flippedclassroom.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.flippedclassroom.android.R;


class CountHeaderViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    public CountHeaderViewHolder(View view) {
        super(view);
        textView = view.findViewById(R.id.item_more_head);
    }

    public void render(String str) {
        textView.setText(str);
    }
}
