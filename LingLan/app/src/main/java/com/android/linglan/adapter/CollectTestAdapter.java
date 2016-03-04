package com.android.linglan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.ui.R;

/**
 * Created by Administrator on 2016/1/14 0014.
 */
public class CollectTestAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;

    public CollectTestAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
//        return this.draw!=null? this.draw.length: 0 ;
        return 11;
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
        convertView = layoutInflater.inflate(R.layout.item_collect_test, null);
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
