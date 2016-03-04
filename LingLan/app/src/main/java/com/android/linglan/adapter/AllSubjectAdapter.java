package com.android.linglan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.linglan.ui.R;
import com.android.linglan.ui.homepage.ArticleDetailsActivity;
import com.android.linglan.ui.homepage.SubjectDetailsActivity;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class AllSubjectAdapter extends
        RecyclerView.Adapter{
    private Context context;
    private int [] draw = {R.drawable.subject,R.drawable.subject,R.drawable.subject,
            R.drawable.subject,R.drawable.subject,R.drawable.subject,
            R.drawable.subject,R.drawable.subject,R.drawable.subject};
    public AllSubjectAdapter(Context context) {
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_all_subject, parent, false);
        return new AllArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return draw.length;
    }

    class AllArticleViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        public AllArticleViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,
                            SubjectDetailsActivity.class);
//                    intent.putExtra("product", products.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}
