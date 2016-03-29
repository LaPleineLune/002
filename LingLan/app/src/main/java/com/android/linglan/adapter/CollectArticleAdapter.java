package com.android.linglan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.AllArticleClassifyListBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/1/13 0013.
 */
public class CollectArticleAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<AllArticleClassifyListBean.ArticleClassifyListBean> articleClassifyList;
    private boolean edit = false;

    public CollectArticleAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
//        return this.draw!=null? this.draw.length: 0 ;
        return this.articleClassifyList != null ? this.articleClassifyList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
//        return this.list.get(position);
        return this.articleClassifyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_collect_article, null);
            viewHolder = new ViewHolder();
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.img_item_article_delete);
            viewHolder.ll_item_article_title = (TextView) convertView.findViewById(R.id.ll_item_article_title);
            viewHolder.ll_item_article_time = (TextView) convertView.findViewById(R.id.ll_item_article_time);
            viewHolder.iv_item_article_image = (ImageView) convertView.findViewById(R.id.iv_item_article_image);
//            viewHolder.ll_item_article_time = (TextView) convertView
//                    .findViewById(R.id.ll_item_article_time);
//            viewHolder.iv_item_article_image = (ImageView) convertView.findViewById(R.id.iv_item_article_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.iv_item_article_image.setBackgroundResource(draw[position]);
        setView(viewHolder, position);
        return convertView;
    }

    public void setView(ViewHolder viewHolder, int position) {
        if (edit) {
            viewHolder.delete.setVisibility(View.VISIBLE);
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show("我是删除的叉叉");
                }
            });
        } else {
            viewHolder.delete.setVisibility(View.GONE);
        }
        viewHolder.ll_item_article_title.setText(articleClassifyList.get(position).title);
        viewHolder.ll_item_article_time.setText(articleClassifyList.get(position).publishtime);
    }

    static class ViewHolder {
        public ImageView delete;
        public TextView ll_item_article_title;
        private TextView ll_item_article_time;
        private ImageView iv_item_article_image;

    }

    public void updateAdapter(boolean edit, ArrayList<AllArticleClassifyListBean.ArticleClassifyListBean> articleClassifyList) {
        this.edit = edit;
        this.articleClassifyList = articleClassifyList;
        this.notifyDataSetChanged();
    }
}
