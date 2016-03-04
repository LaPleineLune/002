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
import com.android.linglan.ui.homepage.ArticleDetailsActivity;
import com.android.linglan.ui.homepage.SubjectDetailsActivity;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class SubjectAdapter  extends
        RecyclerView.Adapter{
    private Context context;
    public ArrayList<RecommendSubjects.RecommendSubject> RecommendSubjects;

    public void update(ArrayList<RecommendSubjects.RecommendSubject> RecommendSubjects) {
        this.RecommendSubjects = RecommendSubjects;
        notifyDataSetChanged();
    }
    public SubjectAdapter(Context context) {
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ( (SubjectViewHolder)holder).bindData(RecommendSubjects.get(position));
    }

    @Override
    public int getItemCount() {
        return RecommendSubjects.size();
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private TextView tv_subject_title;
        private View rootView;
        public SubjectViewHolder(View rootView) {
            super(rootView);
            init(rootView);
        }

        public void init(View rootView){
            this.rootView = rootView;
            tv_subject_title = (TextView)rootView.findViewById(R.id.tv_subject_title);

        }
        public void bindData(final RecommendSubjects.RecommendSubject recommendSubjects) {
//            this.recommendSubjects = recommendSubjects;
            tv_subject_title.setText(recommendSubjects.specialname);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,
                            SubjectDetailsActivity.class);
                    intent.putExtra("specialid", recommendSubjects.specialid);
                    context.startActivity(intent);
                }
            });

//            ll_item_article_addtime.setText(recommendArticle.addtime);
//            if (recommendArticle.photo != null && recommendArticle.photo.equals("")) {
////                iv_item_article_image.setVisibility(View.GONE);
//            }
        }
        @Override
        public void onClick(View v) {

        }
    }
}
