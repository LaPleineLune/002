package com.android.linglan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.http.bean.AllArticleClassifyBean;
import com.android.linglan.ui.R;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/2/27 0027.
 * 首页文章的分类列表的adapter
 */
public class HomepageArticleClassifyAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private String [] foods;
    private int last_item;
    private int selectedPosition = -1;
    private ArrayList<AllArticleClassifyBean.ArticleClassifyBean> ArticleClassify;

    public HomepageArticleClassifyAdapter(Context context){
        this.context = context;
//        this.foods = foods;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return ArticleClassify == null ? 0 : ArticleClassify.size();
    }

    @Override
    public AllArticleClassifyBean.ArticleClassifyBean getItem(int position) {
        return ArticleClassify.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  holder = null;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_homepage_article_classify, null);
            holder = new ViewHolder();
            holder.textView =(TextView)convertView.findViewById(R.id.textview);
//            holder.imageView =(ImageView)convertView.findViewById(R.id.imageview);
            holder.layout=(LinearLayout)convertView.findViewById(R.id.colorlayout);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }
        // 设置选中效果
        if(selectedPosition == position)
        {
//            holder.textView.setTextColor(Color.BLUE);
//            holder.layout.setBackgroundColor(Color.WHITE);
            holder.layout.setBackgroundResource(R.color.white);
            holder.textView.setTextColor(ContextCompat.getColorStateList(context, R.color.carminum));
        } else {
            holder.textView.setTextColor(Color.WHITE);
            holder.layout.setBackgroundColor(Color.TRANSPARENT);
            holder.textView.setTextColor(Color.BLACK);
        }


        holder.textView.setText(getItem(position).catename);
//        holder.textView.setTextColor(Color.BLACK);
//        holder.imageView.setBackgroundResource(images[position]);

        return convertView;
    }

    public static class ViewHolder{
        public TextView textView;
//        public ImageView imageView;
        public LinearLayout layout;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public void update(ArrayList<AllArticleClassifyBean.ArticleClassifyBean> ArticleClassify) {
        this.ArticleClassify = ArticleClassify;
    }

}
