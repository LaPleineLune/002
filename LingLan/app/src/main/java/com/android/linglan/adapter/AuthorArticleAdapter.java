package com.android.linglan.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.Constants;
import com.android.linglan.http.bean.SubjectDetailsBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.ImageUtil;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/1/11 0011.
 */
public class AuthorArticleAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<SubjectDetailsBean.SubjectList> subjectData;

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
    public SubjectDetailsBean.SubjectList getItem(int position) {
        return subjectData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void upDateAdapter(ArrayList<SubjectDetailsBean.SubjectList> subjectData){
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
//        if (subjectData.get(position).type.equals(Constants.ARTICLE)) {// 文章
            try {
                ImageUtil.loadImageAsync(viewHolder.iv_item_article_image, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, getItem(position).photo, null);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
//        viewHolder.iv_item_article_image.setBackgroundResource(draw[position]);
            viewHolder.ll_item_article_title.setText(getItem(position).title);
            viewHolder.ll_item_article_time.setText(getItem(position).publishtime);
            if (!TextUtils.isEmpty(getItem(position).authornames)) {
                Drawable collectTopDrawable = context.getResources().getDrawable(R.drawable.article);
                collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                viewHolder.ll_item_article_name.setCompoundDrawables(collectTopDrawable, null, null, null);
                viewHolder.ll_item_article_name.setCompoundDrawablePadding(12);
                viewHolder.ll_item_article_name.setText(getItem(position).authornames);
            } else {
                viewHolder.ll_item_article_name.setVisibility(View.GONE);
            }
//        }
        return convertView;
    }

    static class ViewHolder {
        public TextView ll_item_article_title;
        public TextView ll_item_article_time;
        public TextView ll_item_article_name;
        public ImageView iv_item_article_image;

    }
}
