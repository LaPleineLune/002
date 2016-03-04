package com.android.linglan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.FollowedListBean;
import com.android.linglan.ui.R;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/1/15 0015.
 * 全部关注的Adapter
 */
public class FollowedAllAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
//    private ArrayList<SubjectDetails.SubjectData> subjectData;
    private ArrayList<FollowedListBean.FollowedListData> followedListData;

    public FollowedAllAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

//    public void upDateAdapter(ArrayList<SubjectDetails.SubjectData> subjectData){
//        this.subjectData = subjectData;
//        notifyDataSetChanged();
//    }

    public void upDateAdapter(ArrayList<FollowedListBean.FollowedListData> followedListData){
        this.followedListData = followedListData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
//        return this.draw!=null? this.draw.length: 0 ;
//        this.subjectData != null ? this.subjectData.size(): 0
        return followedListData != null ? this.followedListData.size() : 0;
//        return 11;
    }

    @Override
    public Object getItem(int position) {
//        return this.list.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_followed_all, null);
//        ViewHolder viewHolder = null;
//        if(convertView == null) {
//            convertView = layoutInflater.inflate(R.layout.item_all_article, null);
//            viewHolder = new ViewHolder();
//            viewHolder.ll_item_article_title = (TextView) convertView
//                    .findViewById(R.id.ll_item_article_title);
//            viewHolder.ll_item_article_time = (TextView) convertView
//                    .findViewById(R.id.ll_item_article_time);
//            viewHolder.iv_item_article_image = (ImageView) convertView.findViewById(R.id.iv_item_article_image);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        viewHolder.iv_item_article_image.setBackgroundResource(draw[position]);
        return convertView;
    }

    static class ViewHolder {
        public TextView ll_item_article_title;
        public TextView ll_item_article_time;
        public ImageView iv_item_article_image;

    }
}