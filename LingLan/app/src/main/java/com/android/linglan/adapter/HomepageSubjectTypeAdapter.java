package com.android.linglan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.linglan.http.bean.AllSubjectClassifyListBean;
import com.android.linglan.ui.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/29 0029.
 */
public class HomepageSubjectTypeAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private int selectedPosition = -1;
    public ArrayList<AllSubjectClassifyListBean.SubjectClassifyListBean> subjectClassifyListBean;

    public HomepageSubjectTypeAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return this.subjectClassifyListBean != null ? this.subjectClassifyListBean.size() : 0 ;
    }

    @Override
    public Object getItem(int position) {
        return this.subjectClassifyListBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_homepage_subject_type, null);
            viewHolder = new ViewHolder();
            viewHolder.type = (TextView) convertView.findViewById(R.id.tv_homepage_subject_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        // 设置选中效果
//        if(selectedPosition == position)
//        {
////            holder.textView.setTextColor(Color.BLUE);
////            holder.layout.setBackgroundColor(Color.WHITE);
//            viewHolder.type.setTextColor(Color.BLUE);
//        } else {
//            viewHolder.type.setTextColor(Color.WHITE);
////            viewHolder.layout.setBackgroundColor(Color.TRANSPARENT);
//        }

        setView(viewHolder, position);
        return convertView;
    }

    public void setView(ViewHolder viewHolder, int position) {
        viewHolder.type.setText(subjectClassifyListBean.get(position).catename);// getItem(position).specialname
    }

    static class ViewHolder {
        public TextView type;

    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public void updateAdapter(ArrayList<AllSubjectClassifyListBean.SubjectClassifyListBean> subjectClassifyListBean){
        this.subjectClassifyListBean = subjectClassifyListBean;
        this.notifyDataSetChanged();
//        this.notifyDataSetInvalidated();
    }
}
