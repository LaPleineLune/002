package com.android.linglan.adapter.clinical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.linglan.http.bean.ClinicalCollatingPatientSelectBean;
import com.android.linglan.http.bean.SortModel;
import com.android.linglan.ui.R;

import java.util.List;

/**
 * Created by LeeMy on 2016/4/20 0020.
 * 患者关联的Adapter
 */
public class PatientSelectAdapter extends BaseAdapter implements SectionIndexer{
    private List<SortModel> list = null;
    private SortModel sortModel;
    private Context mContext;

    private ClinicalCollatingPatientSelectBean clinicalCollatingBean;

    public PatientSelectAdapter(Context mContext) {
        this.mContext = mContext;
        sortModel = new SortModel();
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param clinicalCollatingBean
     */
    public void updateListView(ClinicalCollatingPatientSelectBean clinicalCollatingBean, List<SortModel> list){
        this.clinicalCollatingBean = clinicalCollatingBean;
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.clinicalCollatingBean == null ? 0  : this.clinicalCollatingBean.data.list.size();
    }

    public ClinicalCollatingPatientSelectBean.ClinicalCollatingData.ClinicalCollatingList getItem(int position) {
        return clinicalCollatingBean.data.list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_patient_select, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.tv_item_patient_feature = (TextView) view.findViewById(R.id.tv_item_patient_feature);
            viewHolder.tv_item_visit_time = (TextView) view.findViewById(R.id.tv_item_visit_time);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        }else{
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvTitle.setText(this.list.get(position).getName());
//        String feature = getItem(position).feature != null && !getItem(position).feature.equals("") ? getItem(position).feature : "无内容";
        String feature = this.list.get(position).getFeature() != null && !this.list.get(position).getFeature().equals("") ? this.list.get(position).getFeature() : "无内容";
//        String feature = getItem(position).feature.equals("") ? "无内容" : getItem(position).feature;
//        viewHolder.tv_item_patient_feature.setText(getItem(position).feature);
        viewHolder.tv_item_patient_feature.setText("病证：" + feature);
//        viewHolder.tv_item_visit_time.setText(getItem(position).lastvisittime);
        viewHolder.tv_item_visit_time.setText(this.list.get(position).getLastvisittime());
        return view;

    }



    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        TextView tv_item_patient_feature;
        TextView tv_item_visit_time;
    }


    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String  sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}