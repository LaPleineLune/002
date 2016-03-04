package com.android.linglan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.ui.R;
import com.android.linglan.utils.ToastUtil;

/**
 * Created by LeeMy on 2016/1/16 0016.
 * 全部关注的Adapter
 */
public class NoteAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    public boolean edit = false;

    public NoteAdapter(Context context) {
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
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_note, null);
            viewHolder = new ViewHolder();
//            viewHolder.ll_item_article_title = (TextView) convertView
//                    .findViewById(R.id.ll_item_article_title);
//            viewHolder.ll_item_article_time = (TextView) convertView
//                    .findViewById(R.id.ll_item_article_time);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.img_item_note_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.iv_item_article_image.setBackgroundResource(draw[position]);
        setView(viewHolder);
        return convertView;
    }

    public void setView(ViewHolder viewHolder) {
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
    }

    static class ViewHolder {
        public TextView ll_item_article_title;
        public TextView ll_item_article_time;
        public ImageView delete;

    }

    public void updateAdapter(boolean edit){
        this.edit = edit;
        this.notifyDataSetChanged();
    }
}