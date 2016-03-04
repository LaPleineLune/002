package com.android.linglan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.AllArticleClassifyListBean;
import com.android.linglan.http.bean.AllSearchListBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.homepage.ArticleDetailsActivity;
import com.android.linglan.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class AllArticleSearchAdapter extends RecyclerView.Adapter{
    private Context context;

    public ArrayList<AllSearchListBean.ArticleClassifyListBean> articleClassifyList;

    public AllArticleSearchAdapter(Context context) {
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_all_article, parent, false);
        return new AllArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((AllArticleViewHolder)holder).bindData(articleClassifyList.get(position));
    }

    @Override
    public int getItemCount() {
        return (this.articleClassifyList == null) ? 0 : this.articleClassifyList.size();
    }

    public void updateAdapter(ArrayList<AllSearchListBean.ArticleClassifyListBean> articleClassifyList) {
        this.articleClassifyList = articleClassifyList;
        LogUtil.d("我是全部文章分类列表中的adapter" + this.articleClassifyList.toString());
        notifyDataSetChanged();
    }

    class AllArticleViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView ll_item_article_title;
        private TextView ll_item_article_time;
        private ImageView iv_item_article_image;
        public AllArticleViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            initView(rootView);
        }

        public void initView(View rootView){// 获得控件对象
            this.rootView = rootView;
            ll_item_article_title = (TextView)rootView.findViewById(R.id.ll_item_article_title);
            ll_item_article_time = (TextView)rootView.findViewById(R.id.ll_item_article_time);
            iv_item_article_image = (ImageView) rootView.findViewById(R.id.iv_item_article_image);

        }

        public void bindData(final AllSearchListBean.ArticleClassifyListBean articleClassifyList) {
            ll_item_article_title.setText(articleClassifyList.title);
            ll_item_article_time.setText(articleClassifyList.publishtime);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ArticleDetailsActivity.class);
                    intent.putExtra("articleId", articleClassifyList.articleid);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {

        }

    }

}
