package com.android.linglan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.AllArticleClassifyListBean;
import com.android.linglan.http.bean.SearchArticleBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.homepage.ArticleDetailsActivity;
import com.android.linglan.utils.ImageUtil;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class ArticleSearchMoreAdapter extends
        RecyclerView.Adapter{
    private Context context;
    private ArrayList<AllArticleClassifyListBean.ArticleClassifyListBean> recommendArticles;

    public void update(ArrayList<AllArticleClassifyListBean.ArticleClassifyListBean> recommendArticles) {
        this.recommendArticles = recommendArticles;
        notifyDataSetChanged();
    }

    public ArticleSearchMoreAdapter(Context context) {
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
        return recommendArticles == null ? 0 :recommendArticles.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView ll_item_article_title,ll_item_article_addtime;
        private ImageView iv_item_article_image;
        private AllArticleClassifyListBean.ArticleClassifyListBean recommendArticle;

        public ArticleViewHolder(View rootView) {
            super(rootView);
            initView(rootView);

        }

        private void initView(View rootView) {
            this.rootView = rootView;
            ll_item_article_title = (TextView)rootView.findViewById(R.id.ll_item_article_title);
            ll_item_article_addtime = (TextView)rootView.findViewById(R.id.ll_item_article_addtime);
            iv_item_article_image = (ImageView) rootView.findViewById(R.id.iv_item_article_image);

        }
        public void bindData(final AllArticleClassifyListBean.ArticleClassifyListBean recommendArticle) {
            this.recommendArticle = recommendArticle;
            ll_item_article_title.setText(recommendArticle.title);
            if (!TextUtils.isEmpty(recommendArticle.authornames)) {
                Drawable collectTopDrawable = context.getResources().getDrawable(R.drawable.article);
                collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                ll_item_article_addtime.setCompoundDrawables(collectTopDrawable, null, null, null);
                ll_item_article_addtime.setCompoundDrawablePadding(12);
                ll_item_article_addtime.setText(recommendArticle.authornames);
            }
            if(recommendArticle.photo != null && !recommendArticle.photo.equals("")){
                try {
                    ImageUtil.loadImageAsync(iv_item_article_image, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, recommendArticle.photo, null);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else {
                iv_item_article_image.setVisibility(View.GONE);
            }

            iv_item_article_image = (ImageView)rootView.findViewById(R.id.iv_item_article_image);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,
                            ArticleDetailsActivity.class);
                    intent.putExtra("articleId", recommendArticle.articleid);
                    intent.putExtra("photo", recommendArticle.photo);
                    context.startActivity(intent);
                }
            });

            }

        @Override
        public void onClick(View v) {

        }
    }
}
