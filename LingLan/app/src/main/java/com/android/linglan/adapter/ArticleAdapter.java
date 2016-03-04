package com.android.linglan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.RecommendArticles;
import com.android.linglan.ui.R;
import com.android.linglan.ui.homepage.ArticleDetailsActivity;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class ArticleAdapter extends
        RecyclerView.Adapter{
    private Context context;
    private ArrayList<RecommendArticles.RecommendArticle> recommendArticles;

    void update(ArrayList<RecommendArticles.RecommendArticle> recommendArticles) {
        this.recommendArticles = recommendArticles;
        notifyDataSetChanged();
    }

    public ArticleAdapter(Context context) {
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ( (ArticleViewHolder)holder).bindData(recommendArticles.get(position));
    }

    @Override
    public int getItemCount() {
        return recommendArticles.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView ll_item_article_title,ll_item_article_addtime;
        private ImageView iv_item_article_image;
        private RecommendArticles.RecommendArticle recommendArticle;

        public ArticleViewHolder(View rootView) {
            super(rootView);
            initView(rootView);

        }

        private void initView(View rootView) {
            this.rootView = rootView;
            ll_item_article_title = (TextView)rootView.findViewById(R.id.ll_item_article_title);
            ll_item_article_addtime = (TextView)rootView.findViewById(R.id.ll_item_article_addtime);

        }
        public void bindData(final RecommendArticles.RecommendArticle recommendArticle) {
            this.recommendArticle = recommendArticle;
            ll_item_article_title.setText(recommendArticle.title);
            ll_item_article_addtime.setText(recommendArticle.addtime);
            if(recommendArticle.photo != null && recommendArticle.photo.equals("")){
//                iv_item_article_image.setVisibility(View.GONE);
            }

            iv_item_article_image = (ImageView)rootView.findViewById(R.id.iv_item_article_image);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,
                            ArticleDetailsActivity.class);
                    intent.putExtra("articleId", recommendArticle.articleid);
                    context.startActivity(intent);
                }
            });

            }

        @Override
        public void onClick(View v) {

        }
    }
}
