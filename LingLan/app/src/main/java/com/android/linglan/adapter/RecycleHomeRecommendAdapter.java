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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.CommonProtocol;
import com.android.linglan.http.bean.HomepageRecommendBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.homepage.ArticleDetailsActivity;
import com.android.linglan.ui.homepage.SubjectDetailsActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class RecycleHomeRecommendAdapter extends
        RecyclerView.Adapter{
    private Context context;
    private Intent intent;
    private boolean edit = false;
    public ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> homepageRecommend;

    public RecycleHomeRecommendAdapter(Context context) {
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_homepage_recommend, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ( (ArticleViewHolder)holder).bindData(homepageRecommend.get(position),position);
    }

    @Override
    public int getItemCount() {
        return this.homepageRecommend != null ? this.homepageRecommend.size() : 0 ;
    }

    public void updateAdapter(ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> homepageRecommend,boolean edit){
        this.homepageRecommend = homepageRecommend;
        this.edit = edit;
        this.notifyDataSetChanged();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;

        private ImageView logo;

        private LinearLayout ll_homepage_article;
        private TextView article_title;
        private TextView article_date;

        private LinearLayout ll_homepage_subject;
        private TextView subject_title;
        private TextView subject_description;
        private TextView subject_flag;
        private TextView subject_date;
        private ImageView img_item_article_delete;
        private HomepageRecommendBean.HomepageRecommendBeanData recommendArticle;

        public ArticleViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            logo = (ImageView) rootView.findViewById(R.id.img_homepage_logo);

            ll_homepage_article = (LinearLayout) rootView.findViewById(R.id.ll_homepage_article);
            article_title = (TextView) rootView.findViewById(R.id.tv_homepage_article_title);
            article_date = (TextView) rootView.findViewById(R.id.tv_homepage_article_date);

            ll_homepage_subject = (LinearLayout) rootView.findViewById(R.id.ll_homepage_subject);
            subject_title = (TextView) rootView.findViewById(R.id.tv_homepage_subject_title);
            subject_description = (TextView) rootView.findViewById(R.id.tv_homepage_subject_description);
            subject_flag = (TextView) rootView.findViewById(R.id.tv_homepage_subject_flag);
            subject_date = (TextView) rootView.findViewById(R.id.tv_homepage_subject_date);

            img_item_article_delete = (ImageView) rootView.findViewById(R.id.img_item_article_delete);

        }
        public void bindData(final HomepageRecommendBean.HomepageRecommendBeanData recommendArticle, final int position) {
            this.recommendArticle = recommendArticle;
            if(edit){
                img_item_article_delete.setVisibility(View.VISIBLE);
                rootView.setOnClickListener(null);
                img_item_article_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ToastUtil.show("删除成功");
                        if (recommendArticle.type.equals("4")) {
                            getSubjectCancleCollect(position,recommendArticle.specialid,"1");
                        } else if (recommendArticle.type.equals("0")) {
                            getArticleCancleCollect(position,recommendArticle.articleid,"1");
                        }
                    }
                });
            }else{
                img_item_article_delete.setVisibility(View.GONE);

                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recommendArticle.type.equals(Constants.SUBJECT)) {// 如果是专题
                            intent = new Intent(context, SubjectDetailsActivity.class);
                            intent.putExtra("specialid", recommendArticle.specialid);
                            intent.putExtra("specialname", recommendArticle.specialname);
                            intent.putExtra("logo", recommendArticle.logo);
                            intent.putExtra("photo", recommendArticle.photo);
                            intent.putExtra("description", recommendArticle.description);
                        } else if (recommendArticle.type.equals(Constants.ARTICLE)) {// 如果是文章
                            intent = new Intent(context, ArticleDetailsActivity.class);
                            intent.putExtra("articleId", recommendArticle.articleid);
                            intent.putExtra("photo", recommendArticle.photo);
                        }
                        context.startActivity(intent);
                    }
                });
            }
            if (recommendArticle.type.equals("4")) {// 专题
                try {
                    ImageUtil.loadImageAsync(logo, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, recommendArticle.logo, null);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
//                ImageUtil.loadImageAsync(logo, recommendArticle.logo, R.drawable.default_image);
                ll_homepage_subject.setVisibility(View.VISIBLE);
                ll_homepage_article.setVisibility(View.GONE);
                subject_title.setText(recommendArticle.specialname);
                subject_description.setText(recommendArticle.content_title);
//                subject_date.setText(recommendArticle.updatetime);// 更新时间
            } else if (recommendArticle.type.equals("0")) {// 文章
                if (!TextUtils.isEmpty(recommendArticle.photo)) {
                    try {
                        ImageUtil.loadImageAsync(logo, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, recommendArticle.photo, null);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else {
                    logo.setVisibility(View.GONE);
                }
//                ImageUtil.loadImageAsync(logo, recommendArticle.photo, R.drawable.default_image);
                ll_homepage_subject.setVisibility(View.GONE);
                ll_homepage_article.setVisibility(View.VISIBLE);
                article_title.setText(recommendArticle.title);
                if (!TextUtils.isEmpty(recommendArticle.authornames)) {
                    Drawable collectTopDrawable = context.getResources().getDrawable(R.drawable.article);
                    collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                    article_date.setCompoundDrawables(collectTopDrawable, null, null, null);
                    article_date.setCompoundDrawablePadding(12);
                    article_date.setText(recommendArticle.authornames);
                }
            }


        }

        @Override
        public void onClick(View v) {

        }
    }
    //取消专题收藏
    private void getSubjectCancleCollect(final int position,String specialid,String iscancel){
        NetApi.getDetailsSubjectCollect(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result=" + result);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,context)){
                    return;
                }
                CommonProtocol data = JsonUtil.json2Bean(result, CommonProtocol.class);
                homepageRecommend.remove(position);
                notifyDataSetChanged();
                ToastUtil.show(data.msg);
            }

            @Override
            public void onFailure(String message) {
                ToastUtil.show(message);
            }
        }, specialid,iscancel);
    }

    //取消文章收藏
    private void getArticleCancleCollect(final int position,String articleid,String iscancel){
        NetApi.getDetailsArticleCollect(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,context)){
                    return;
                }
                CommonProtocol data = JsonUtil.json2Bean(result, CommonProtocol.class);
                ToastUtil.show(data.msg);
                homepageRecommend.remove(position);
                notifyDataSetChanged();
//                ToastUtil.show(result);
            }

            @Override
            public void onFailure(String message) {
                ToastUtil.show(message);
            }
        }, articleid, iscancel);
    }

}
