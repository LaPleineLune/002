package com.android.linglan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.linglan.http.bean.RecommendArticles;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.ui.study.ArticleDetailsActivity;
import com.android.linglan.ui.study.SubjectDetailsActivity;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/6/20 0020.
 * 热搜词数据适配器
 */
public class SearchSubjectAllHotAdapter extends RecyclerView.Adapter {
    private Context context;
    private Intent intent;
    private  ArrayList<RecommendSubjects.RecommendSubject> hotSearch;

    public SearchSubjectAllHotAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_hot_search_single, parent, false);
        return new RadioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RadioViewHolder) holder).bindData(hotSearch.get(position));
    }

    @Override
    public int getItemCount() {
        return this.hotSearch != null ? this.hotSearch.size() : 0;
    }

    public void updateSubjectAdapter( ArrayList<RecommendSubjects.RecommendSubject> hotSearch) {
        this.hotSearch = hotSearch;
        this.notifyDataSetChanged();
    }

    class RadioViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView tv_hot_search_title;
        public RadioViewHolder(View rootView) {
            super(rootView);
            initView(rootView);

        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_hot_search_title = (TextView)rootView.findViewById(R.id.tv_hot_search_title);
        }

        public void bindData(final RecommendSubjects.RecommendSubject subject) {
            tv_hot_search_title.setText(subject.specialname);
            tv_hot_search_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   intent = new Intent(context, SubjectDetailsActivity.class);
                    intent.putExtra("specialid",subject.specialid);
                    intent.putExtra("specialname", subject.specialname);
                    intent.putExtra("photo", subject.photo);
                    intent.putExtra("logo", subject.logo);
                    intent.putExtra("description", subject.description);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}
