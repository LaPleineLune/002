package com.android.linglan.adapter;

import android.app.Activity;
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

import com.android.linglan.http.Constants;
import com.android.linglan.http.bean.SubjectDetailsBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.study.ArticleDetailsActivity;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/3/23 0023.
 */
public class SubjectDetailsListAdapter extends RecyclerView.Adapter {
    private Activity context;
    private ArrayList<SubjectDetailsBean.SubjectList> subjectData;

    public SubjectDetailsListAdapter(Activity context, ArrayList<SubjectDetailsBean.SubjectList> subjectData) {
        this.context = context;
        this.subjectData = subjectData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_subject_details_article, parent, false);
        return new SubjectDetailsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ( (SubjectDetailsListViewHolder)holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.subjectData != null ? this.subjectData.size() : 0 ;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateAdapter(ArrayList<SubjectDetailsBean.SubjectList> subjectData){
        this.subjectData = subjectData;
        this.notifyDataSetChanged();
    }

    class SubjectDetailsListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView ll_item_article_title;
        private TextView ll_item_article_time;
        private TextView ll_item_article_name;
        private ImageView iv_item_article_image;
//        private TextView tv_item_history_search;
        public SubjectDetailsListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            ll_item_article_title = (TextView) rootView
                    .findViewById(R.id.ll_item_article_title);
            ll_item_article_time = (TextView) rootView
                    .findViewById(R.id.ll_item_article_time);
            ll_item_article_name = (TextView) rootView
                    .findViewById(R.id.ll_item_article_name);
            iv_item_article_image = (ImageView) rootView.findViewById(R.id.iv_item_article_image);
//            tv_item_history_search = (TextView) rootView.findViewById(R.id.tv_item_history_search);

        }
        public void bindData(final int position) {
                try {
                    ImageUtil.loadImageAsync(iv_item_article_image, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, subjectData.get(position).photo, null);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                ll_item_article_title.setText(subjectData.get(position).title);
                String oldArticleid = SharedPreferencesUtil.getString("articleid" + subjectData.get(position).articleid, null);
                if (oldArticleid != null && oldArticleid.equals(subjectData.get(position).articleid)) {
                    ll_item_article_title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                } else {
                    ll_item_article_title.setTextColor(ContextCompat.getColor(context, R.color.text_title_color));
                }

                ll_item_article_time.setText(subjectData.get(position).publishtime);
                if (!TextUtils.isEmpty(subjectData.get(position).authornames)) {
                    Drawable collectTopDrawable = context.getResources().getDrawable(R.drawable.article);
                    collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                    ll_item_article_name.setCompoundDrawables(collectTopDrawable, null, null, null);
                    ll_item_article_name.setCompoundDrawablePadding(12);
                    ll_item_article_name.setText(subjectData.get(position).authornames);
                } else {
                    ll_item_article_name.setVisibility(View.GONE);
                }
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        MobclickAgent.onEvent(context, UmengBuriedPointUtil.StudyClickCharacter);
                        SharedPreferencesUtil.saveString("articleid" + subjectData.get(position).articleid, subjectData.get(position).articleid);
                        ll_item_article_title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                        Intent intent = new Intent();
                        String subjectActivity = context.getIntent().getStringExtra("subjectActivity");
                        if (subjectActivity != null && subjectActivity.equals("subjectActivity")) {
                            intent.putExtra("subjectActivity", "subjectActivity");
                        }
                        intent.putExtra("articleId", subjectData.get(position).articleid);
                        intent.putExtra("photo", subjectData.get(position).photo);
                        intent.setClass(context, ArticleDetailsActivity.class);
                        context.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
