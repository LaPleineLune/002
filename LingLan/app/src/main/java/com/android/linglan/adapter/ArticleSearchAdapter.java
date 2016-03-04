package com.android.linglan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.AllSearchListBean;
import com.android.linglan.http.bean.SearchArticleBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.homepage.ArticleDetailsActivity;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class ArticleSearchAdapter extends
        RecyclerView.Adapter{
    private Context context;
    private ArrayList<SearchArticleBean.ArticleClassifyListBean> recommendArticles;

    void update(ArrayList<SearchArticleBean.ArticleClassifyListBean> recommendArticles) {
        this.recommendArticles = recommendArticles;
        notifyDataSetChanged();
    }

    public ArticleSearchAdapter(Context context) {
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
        if(recommendArticles == null){
            return 0;
        }else if(recommendArticles.size() < 3){
            return recommendArticles.size();
        }else{
            return 3;
        }
//        return recommendArticles == null ? 0 :3;
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView ll_item_article_title,ll_item_article_addtime;
        private ImageView iv_item_article_image;
        private SearchArticleBean.ArticleClassifyListBean recommendArticle;

        public ArticleViewHolder(View rootView) {
            super(rootView);
            initView(rootView);

        }

        private void initView(View rootView) {
            this.rootView = rootView;
            ll_item_article_title = (TextView)rootView.findViewById(R.id.ll_item_article_title);
            ll_item_article_addtime = (TextView)rootView.findViewById(R.id.ll_item_article_addtime);

        }
        public void bindData(final SearchArticleBean.ArticleClassifyListBean recommendArticle) {
            this.recommendArticle = recommendArticle;
            ll_item_article_title.setText(recommendArticle.title);
            ll_item_article_addtime.setText(recommendArticle.publishtime);
            if(recommendArticle.photo != null && !recommendArticle.photo.equals("")){
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
