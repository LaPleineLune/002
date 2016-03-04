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
 * Created by LeeMy on 2016/1/13 0013.
 */
public class CollectSubjectAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    public boolean edit = false;
    public ArrayList<RecommendSubjects.RecommendSubject> recommendSubject;

    public CollectSubjectAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return this.recommendSubject != null ? this.recommendSubject.size() : 0 ;
    }

    @Override
    public Object getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.item_collect_subject, null);
            viewHolder = new ViewHolder();
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.img_item_subject_picture);
            viewHolder.subjectName = (TextView) convertView.findViewById(R.id.tv_item_subject_name);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.img_item_subject_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
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
        viewHolder.subjectName.setText(recommendSubject.get(position).specialname);// getItem(position).specialname
    }

    static class ViewHolder {
        public ImageView picture;
        public TextView subjectName;
        public ImageView delete;

    }

    public void updateAdapter(boolean edit, ArrayList<RecommendSubjects.RecommendSubject> recommendSubject){
        this.edit = edit;
        this.recommendSubject = recommendSubject;
        this.notifyDataSetChanged();
    }
}
