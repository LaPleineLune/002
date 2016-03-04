package com.android.linglan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/2/27 0027.
 * 首页文章具体分类列表的adapter
 */
public class HomepageArticleListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater layoutInflater;
    String[][] cities;
    public int foodpoition;

    public HomepageArticleListAdapter(Context context, String[][] cities,int position) {
        this.context = context;
        this.cities = cities;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.foodpoition = position;
    }

    @Override
    public int getCount() {
        return cities.length;
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final int location=position;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_homepage_article_list, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView
                    .findViewById(R.id.textview1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(cities[foodpoition][position]);
        viewHolder.textView.setTextColor(Color.BLACK);

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }
}
