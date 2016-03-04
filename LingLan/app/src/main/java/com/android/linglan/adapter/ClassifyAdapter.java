package com.android.linglan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.linglan.ui.R;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class ClassifyAdapter extends
        RecyclerView.Adapter{
    private Context context;
    private int [] draw = {R.drawable.subject, R.drawable.subject,R.drawable.subject,
            R.drawable.subject,R.drawable.subject,R.drawable.subject,
            R.drawable.subject,R.drawable.subject,R.drawable.subject};
    public ClassifyAdapter(Context context) {
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_classify, parent, false);
        return new ClassifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return draw.length;
    }

    class ClassifyViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public ClassifyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
