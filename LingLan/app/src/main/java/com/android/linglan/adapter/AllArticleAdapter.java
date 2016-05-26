package com.android.linglan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.AllArticleClassifyListBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.study.ArticleDetailsActivity;
import com.android.linglan.utils.SharedPreferencesUtil;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class AllArticleAdapter extends RecyclerView.Adapter{
    private Context context;

    public ArrayList<AllArticleClassifyListBean.ArticleClassifyListBean> articleClassifyList;

    public AllArticleAdapter(Context context) {
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

    public void updateAdapter(ArrayList<AllArticleClassifyListBean.ArticleClassifyListBean> articleClassifyList) {
        this.articleClassifyList = articleClassifyList;
        notifyDataSetChanged();
    }

    class AllArticleViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView tv_item_article_title;
        private TextView ll_item_article_time;
        private TextView tv_item_article_new;
        private ImageView iv_item_article_image;
        public AllArticleViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            initView(rootView);
        }

        public void initView(View rootView){// 获得控件对象
            this.rootView = rootView;
            tv_item_article_title = (TextView)rootView.findViewById(R.id.tv_item_article_title);
            ll_item_article_time = (TextView)rootView.findViewById(R.id.ll_item_article_time);
            tv_item_article_new = (TextView)rootView.findViewById(R.id.tv_item_article_new);
            iv_item_article_image = (ImageView) rootView.findViewById(R.id.iv_item_article_image);

        }

        public void bindData(final AllArticleClassifyListBean.ArticleClassifyListBean articleClassifyList) {
            tv_item_article_title.setText(articleClassifyList.title);
            String oldArticleid = SharedPreferencesUtil.getString("articleid" + articleClassifyList.articleid, null);
            if (oldArticleid != null && oldArticleid.equals(articleClassifyList.articleid)) {
                tv_item_article_title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
            } else {
                tv_item_article_title.setTextColor(ContextCompat.getColor(context, R.color.text_title_color));
            }
            if (articleClassifyList.isnew != null && articleClassifyList.isnew.equals("1")) {
                tv_item_article_new.setVisibility(View.VISIBLE);
            } else {
                tv_item_article_new.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(articleClassifyList.authornames)) {
                ll_item_article_time.setVisibility(View.VISIBLE);
                Drawable collectTopDrawable = context.getResources().getDrawable(R.drawable.article);
                collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                ll_item_article_time.setCompoundDrawables(collectTopDrawable, null, null, null);
                ll_item_article_time.setCompoundDrawablePadding(12);
                ll_item_article_time.setText(articleClassifyList.authornames);
            } else {
                ll_item_article_time.setVisibility(View.GONE);
            }
//            ll_item_article_time.setText(articleClassifyList.authornames);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferencesUtil.saveString("articleid" + articleClassifyList.articleid, articleClassifyList.articleid);
                    tv_item_article_title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                    Intent intent = new Intent(context, ArticleDetailsActivity.class);
                    intent.putExtra("articleId", articleClassifyList.articleid);
                    intent.putExtra("photo", articleClassifyList.photo);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {

        }

    }

}
