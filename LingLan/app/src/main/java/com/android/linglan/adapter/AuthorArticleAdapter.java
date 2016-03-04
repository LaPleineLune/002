package com.android.linglan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.SubjectDetails;
import com.android.linglan.ui.R;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/1/11 0011.
 */
public class AuthorArticleAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<SubjectDetails.SubjectData> subjectData;

    public AuthorArticleAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return this.subjectData != null ? this.subjectData.size(): 0 ;
//        return 5;
    }

    @Override
    public SubjectDetails.SubjectData getItem(int position) {
        return subjectData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void upDateAdapter(ArrayList<SubjectDetails.SubjectData> subjectData){
        this.subjectData = subjectData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_author_article, null);
            viewHolder = new ViewHolder();
            viewHolder.ll_item_article_title = (TextView) convertView
                    .findViewById(R.id.ll_item_article_title);
            viewHolder.ll_item_article_time = (TextView) convertView
                    .findViewById(R.id.ll_item_article_time);
            viewHolder.ll_item_article_name = (TextView) convertView
                    .findViewById(R.id.ll_item_article_name);
            viewHolder.iv_item_article_image = (ImageView) convertView.findViewById(R.id.iv_item_article_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.iv_item_article_image.setBackgroundResource(draw[position]);
        viewHolder. ll_item_article_title.setText(getItem(position).content_title);
        viewHolder.ll_item_article_time.setText(getItem(position).addtime);
        if (!getItem(position).author.equals("")) {
            viewHolder.ll_item_article_name.setText(getItem(position).author);
        } else {
            viewHolder.ll_item_article_name.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        public TextView ll_item_article_title;
        public TextView ll_item_article_time;
        public TextView ll_item_article_name;
        public ImageView iv_item_article_image;

    }
}
