package com.android.linglan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.http.bean.HomepageRecommendBean;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/2/29 0029.
 * 首页推荐adapter
 */
public class HomepageRecommendAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    public ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> homepageRecommend;

    public HomepageRecommendAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return this.homepageRecommend != null ? this.homepageRecommend.size() : 0 ;
    }

    @Override
    public Object getItem(int position) {
        return this.homepageRecommend.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_homepage_recommend, null);
            viewHolder = new ViewHolder();
            viewHolder.logo = (ImageView) convertView.findViewById(R.id.img_homepage_logo);

            viewHolder.ll_homepage_article = (LinearLayout) convertView.findViewById(R.id.ll_homepage_article);
            viewHolder.article_title = (TextView) convertView.findViewById(R.id.tv_homepage_article_title);
            viewHolder.article_date = (TextView) convertView.findViewById(R.id.tv_homepage_article_date);

            viewHolder.ll_homepage_subject = (LinearLayout) convertView.findViewById(R.id.ll_homepage_subject);
            viewHolder.subject_title = (TextView) convertView.findViewById(R.id.tv_homepage_subject_title);
            viewHolder.subject_description = (TextView) convertView.findViewById(R.id.tv_homepage_subject_description);
            viewHolder.subject_flag = (TextView) convertView.findViewById(R.id.tv_homepage_subject_flag);
            viewHolder.subject_date = (TextView) convertView.findViewById(R.id.tv_homepage_subject_date);
//            viewHolder.description = (TextView) convertView.findViewById(R.id.tv_homepage_subject_description);
//            viewHolder.date = (TextView) convertView.findViewById(R.id.tv_homepage_subject_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setView(viewHolder, position);
        return convertView;
    }

    public void setView(ViewHolder viewHolder, int position) {
//        viewHolder.logo.setBackgroundResource(homepageRecommend.get(position).photo);// getItem(position).specialname
        if (homepageRecommend.get(position).type.equals("4")) {
            viewHolder.ll_homepage_subject.setVisibility(View.VISIBLE);
            viewHolder.ll_homepage_article.setVisibility(View.GONE);
            viewHolder.subject_title.setText(homepageRecommend.get(position).specialname);
            viewHolder.subject_description.setText(homepageRecommend.get(position).description);
            viewHolder.subject_date.setText(homepageRecommend.get(position).addtime);
        } else if (homepageRecommend.get(position).type.equals("0")) {
            viewHolder.ll_homepage_subject.setVisibility(View.GONE);
            viewHolder.ll_homepage_article.setVisibility(View.VISIBLE);
            viewHolder.article_title.setText(homepageRecommend.get(position).title);
            viewHolder.article_date.setText(homepageRecommend.get(position).publishtime);
        }

//        viewHolder.description.setText(recommendSubject.get(position).description);
//        viewHolder.date.setText(recommendSubject.get(position).addtime);
    }

    static class ViewHolder {
        public ImageView logo;

        public LinearLayout ll_homepage_article;
        public TextView article_title;
        public TextView article_date;

        public LinearLayout ll_homepage_subject;
        public TextView subject_title;
        public TextView subject_description;
        public TextView subject_flag;
        public TextView subject_date;

    }

    public void updateAdapter(ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> homepageRecommend){
        this.homepageRecommend = homepageRecommend;
        this.notifyDataSetChanged();
    }
}
