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
import com.android.linglan.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class TestAllSubjectAdapter extends
        BaseAdapter{
    private Context context;
    private LayoutInflater layoutInflater;
    public ArrayList<RecommendSubjects.RecommendSubject> data;

    public TestAllSubjectAdapter(Context context,ArrayList<RecommendSubjects.RecommendSubject> data){
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RecommendSubjects.RecommendSubject getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_all_subject, null);
        ImageView iv_item_article_image1 = (ImageView)convertView.findViewById(R.id.iv_item_article_image1);
        ImageView iv_item_article_image2 = (ImageView)convertView.findViewById(R.id.iv_item_article_image2);
        iv_item_article_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("我是火星的，你呢？");
            }
        });

        iv_item_article_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("我是火星的，你呢？");
            }
        });
        return convertView;
    }

    public void updata(ArrayList<RecommendSubjects.RecommendSubject> data){
        this.data = data;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        public TextView ll_item_article_title;
        public ImageView iv_item_article_image1;
        public ImageView iv_item_article_image2;

    }
}
