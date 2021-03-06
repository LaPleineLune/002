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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.CommonProtocol;
import com.android.linglan.http.bean.HomepageRecommendBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.fm.RadioPlayActivity;
import com.android.linglan.ui.fm.RadioSpecialActivity;
import com.android.linglan.ui.study.ArticleDetailsActivity;
import com.android.linglan.ui.study.SubjectDetailsActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import me.nereo.multi_image_selector.bean.Image;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class RecycleHomeRecommendAdapter extends
        RecyclerView.Adapter {
    private Context context;
    private Intent intent;
    private boolean edit = false;
    public ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> homepageRecommend;

    public RecycleHomeRecommendAdapter(Context context) {
        this.context = context;
    }

    public RecycleHomeRecommendAdapter(Context context, ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> homepageRecommend, boolean edit) {
        this.context = context;
        this.homepageRecommend = homepageRecommend;
        this.edit = edit;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_homepage_recommend, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ArticleViewHolder) holder).bindData(homepageRecommend.get(position), position);
    }

    @Override
    public int getItemCount() {
        return this.homepageRecommend != null ? this.homepageRecommend.size() : 0;
    }

    public void updateAdapter(ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> homepageRecommend, boolean edit) {
        this.homepageRecommend = homepageRecommend;
        this.edit = edit;
        this.notifyDataSetChanged();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;

        private ImageView logo;
        private ImageView play_logo;

        private LinearLayout ll_homepage_article;
        private TextView article_title;
        private TextView article_date;
        private TextView article_new;

        private LinearLayout ll_homepage_subject;
        private TextView subject_title;
        private TextView subject_description;
        private ImageView subject_flag;
        private TextView subject_date;
        private TextView subject_new;
        private ImageView img_item_article_delete;
        private LinearLayout ll_no_collect_subject;
        private HomepageRecommendBean.HomepageRecommendBeanData recommendArticle;

        public ArticleViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            logo = (ImageView) rootView.findViewById(R.id.img_homepage_logo);
            play_logo = (ImageView) rootView.findViewById(R.id.img_homepage_play_logo);

            ll_homepage_article = (LinearLayout) rootView.findViewById(R.id.ll_homepage_article);
            article_title = (TextView) rootView.findViewById(R.id.tv_homepage_article_title);
            article_date = (TextView) rootView.findViewById(R.id.tv_homepage_article_date);
            article_new = (TextView) rootView.findViewById(R.id.tv_homepage_article_new);

            ll_homepage_subject = (LinearLayout) rootView.findViewById(R.id.ll_homepage_subject);
            subject_title = (TextView) rootView.findViewById(R.id.tv_homepage_subject_title);
            subject_description = (TextView) rootView.findViewById(R.id.tv_homepage_subject_description);
            subject_flag = (ImageView) rootView.findViewById(R.id.tv_homepage_subject_flag);

            subject_date = (TextView) rootView.findViewById(R.id.tv_homepage_subject_date);
            subject_new = (TextView) rootView.findViewById(R.id.tv_homepage_subject_new);

            img_item_article_delete = (ImageView) rootView.findViewById(R.id.img_item_article_delete);
            ll_no_collect_subject = (LinearLayout) rootView.findViewById(R.id.ll_no_collect_subject);
        }

        public void bindData(final HomepageRecommendBean.HomepageRecommendBeanData recommendArticle, final int position) {
            this.recommendArticle = recommendArticle;
            if (edit) {
                img_item_article_delete.setVisibility(View.VISIBLE);
                rootView.setOnClickListener(null);
                img_item_article_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ToastUtil.show("删除成功");
                        if (recommendArticle.type.equals(Constants.SUBJECT)) {
                            getSubjectCancleCollect(position, recommendArticle.specialid, "1");
                        } else if (recommendArticle.type.equals(Constants.ARTICLE)) {
                            getArticleCancleCollect(position, recommendArticle.articleid, "1");
                        }else if (recommendArticle.type.equals(Constants.RADIO_SPECIAL)) {
                            getArticleCancleCollect(position, recommendArticle.articleid, "1");
                        }else if (recommendArticle.type.equals(Constants.RADIO_SINGLE)) {
                            getArticleCancleCollect(position, recommendArticle.articleid, "1");
                        }
                    }
                });
            } else {
                img_item_article_delete.setVisibility(View.GONE);
                ll_no_collect_subject.setVisibility(View.GONE);
                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recommendArticle.type.equals(Constants.SUBJECT)) {// 如果是专题
                            MobclickAgent.onEvent(context, UmengBuriedPointUtil.StudySubjectContent);
                            intent = new Intent(context, SubjectDetailsActivity.class);
                            intent.putExtra("specialid", recommendArticle.specialid);
                            intent.putExtra("specialname", recommendArticle.specialname);
                            intent.putExtra("logo", recommendArticle.logo);
                            intent.putExtra("photo", recommendArticle.photo);
                            intent.putExtra("description", recommendArticle.description);
                            intent.putExtra("page", recommendArticle.page);
                            intent.putExtra("NewCollectActivity", "NewCollectActivity");
                        } else if (recommendArticle.type.equals(Constants.ARTICLE)) {// 如果是文章
                            MobclickAgent.onEvent(context, UmengBuriedPointUtil.StudyClickCharacter);
                            intent = new Intent(context, ArticleDetailsActivity.class);
                            intent.putExtra("articleId", recommendArticle.articleid);
                            intent.putExtra("photo", recommendArticle.photo);
                            intent.putExtra("page", recommendArticle.page);
                        }else if (recommendArticle.type.equals(Constants.RADIO_SPECIAL)) {// 如果是合辑
//                                MobclickAgent.onEvent(context, UmengBuriedPointUtil.StudyClickCharacter);
                            SharedPreferencesUtil.saveString("albumid" + recommendArticle.albumid, recommendArticle.albumid);
                            subject_title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                            intent = new Intent(context, RadioSpecialActivity.class);
                            intent.putExtra("albumid", recommendArticle.albumid);
                            intent.putExtra("albumname", recommendArticle.albumname);
                        }else if (recommendArticle.type.equals(Constants.RADIO_SINGLE)) {// 如果是音频
//                                MobclickAgent.onEvent(context, UmengBuriedPointUtil.StudyClickCharacter);
                            SharedPreferencesUtil.saveString("audioid" + recommendArticle.audioid, recommendArticle.audioid);
                            subject_title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                            intent = new Intent(context, RadioPlayActivity.class);
                            intent.putExtra("audioid", recommendArticle.audioid);
                        }
                        context.startActivity(intent);
                    }
                });
            }
            if (recommendArticle.type.equals(Constants.SUBJECT)) {// 专题
                play_logo.setVisibility(View.GONE);
                subject_flag.setVisibility(View.VISIBLE);
                subject_flag.setImageResource(R.drawable.homepage_subject);
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
                if (recommendArticle.isnew != null && recommendArticle.isnew.equals("1")) {
                    subject_new.setVisibility(View.VISIBLE);
                } else {
                    subject_new.setVisibility(View.GONE);
                }
            } else if (recommendArticle.type.equals(Constants.ARTICLE)) {// 文章
                try {
                    ImageUtil.loadImageAsync(logo, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, recommendArticle.photo, null);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                play_logo.setVisibility(View.GONE);
                ll_homepage_subject.setVisibility(View.GONE);
                ll_homepage_article.setVisibility(View.VISIBLE);
                article_title.setText(recommendArticle.title);
                if (recommendArticle.isnew != null && recommendArticle.isnew.equals("1")) {
                    article_new.setVisibility(View.VISIBLE);
                } else {
                    article_new.setVisibility(View.GONE);
                }
                if (recommendArticle.authornames != null && !recommendArticle.authornames.equals("")) {
                    article_date.setVisibility(View.VISIBLE);
                    Drawable collectTopDrawable = context.getResources().getDrawable(R.drawable.article);
                    collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                    article_date.setCompoundDrawables(collectTopDrawable, null, null, null);
                    article_date.setCompoundDrawablePadding(12);
                    article_date.setText(recommendArticle.authornames);
                } else {
                    article_date.setVisibility(View.GONE);
                }
            }else if(recommendArticle.type.equals(Constants.RADIO_SPECIAL)){
                try {
                    ImageUtil.loadImageAsync(logo, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, recommendArticle.logo, null);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                play_logo.setVisibility(View.VISIBLE);
                subject_flag.setVisibility(View.VISIBLE);
                subject_flag.setImageResource(R.drawable.homepage_album);
                ll_homepage_subject.setVisibility(View.VISIBLE);
                ll_homepage_article.setVisibility(View.GONE);
                subject_title.setText(recommendArticle.albumname);
                subject_description.setText(recommendArticle.description);

                if (recommendArticle.isnew != null && recommendArticle.isnew.equals("1")) {
                    subject_new.setVisibility(View.VISIBLE);
                } else {
                    subject_new.setVisibility(View.GONE);
                }
            }else if(recommendArticle.type.equals(Constants.RADIO_SINGLE)){
                play_logo.setVisibility(View.VISIBLE);
                subject_flag.setVisibility(View.GONE);
                try {
                    ImageUtil.loadImageAsync(logo, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, recommendArticle.photo, null);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                ll_homepage_subject.setVisibility(View.VISIBLE);
                ll_homepage_article.setVisibility(View.GONE);
                subject_title.setText(recommendArticle.title);
                subject_description.setText(recommendArticle.description);

                if (recommendArticle.isnew != null && recommendArticle.isnew.equals("1")) {
                    subject_new.setVisibility(View.VISIBLE);
                } else {
                    subject_new.setVisibility(View.GONE);
                }
            }

        }

        @Override
        public void onClick(View v) {

        }
    }

    //取消专题收藏
    private void getSubjectCancleCollect(final int position, String specialid, String iscancel) {
        NetApi.getDetailsSubjectCollect(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, context)) {
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
        }, specialid, iscancel);
    }

    //取消文章收藏
    private void getArticleCancleCollect(final int position, String articleid, String iscancel) {
        NetApi.getDetailsArticleCollect(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, context)) {
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
