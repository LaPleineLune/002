package com.android.linglan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.utils.TimeStampConversionUtil;
import com.android.linglan.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/2/28 0028.
 * 首页专题adapter
 */
public class HomepageSubjectAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    public ArrayList<RecommendSubjects.RecommendSubject> recommendSubject;

    public HomepageSubjectAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return this.recommendSubject != null ? this.recommendSubject.size() : 0 ;
    }

    @Override
    public RecommendSubjects.RecommendSubject getItem(int position) {
        return this.recommendSubject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_homepage_subject, null);
            viewHolder = new ViewHolder();
            viewHolder.logo = (ImageView) convertView.findViewById(R.id.img_homepage_subject_logo);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_homepage_subject_title);
            viewHolder.description = (TextView) convertView.findViewById(R.id.tv_homepage_subject_description);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tv_homepage_subject_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setView(viewHolder, position);
        return convertView;
    }

    public void setView(ViewHolder viewHolder, int position) {
        viewHolder.title.setText(recommendSubject.get(position).specialname);// getItem(position).specialname
        viewHolder.description.setText(recommendSubject.get(position).description);
        viewHolder.date.setText(recommendSubject.get(position).addtime);
    }

    static class ViewHolder {
        public ImageView logo;
        public TextView title;
        public TextView description;
        public TextView date;

    }

    public void updateAdapter(ArrayList<RecommendSubjects.RecommendSubject> recommendSubject){
        this.recommendSubject = recommendSubject;
        this.notifyDataSetChanged();
    }
}
