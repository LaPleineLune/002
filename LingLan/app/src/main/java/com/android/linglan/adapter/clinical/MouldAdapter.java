package com.android.linglan.adapter.clinical;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.AllArticleClassifyListBean;
import com.android.linglan.http.bean.ClinicalDetailsBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/25 0025.
 * 测试
 */
public class MouldAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsPathography> pathography;
    private boolean edit = false;

    public MouldAdapter(Context context, ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsPathography> pathography) {
        this.context = context;
        this.pathography = pathography;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
//        return this.draw!=null? this.draw.length: 0 ;
        return this.pathography != null ? this.pathography.size() : 0;
    }

    @Override
    public Object getItem(int position) {
//        return this.list.get(position);
        return this.pathography.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.edit_item_mould, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_clinical_mould = (TextView) convertView.findViewById(R.id.tv_clinical_mould);
            viewHolder.tv_clinical_mould_content = (EditText) convertView.findViewById(R.id.tv_clinical_mould_content);
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
        if (TextUtils.isEmpty(pathography.get(position).templetname)) {
            viewHolder.tv_clinical_mould.setVisibility(View.GONE);
        } else {
            viewHolder.tv_clinical_mould.setText(context.getString(R.string.clinical_mould, pathography.get(position).templetname));
        }
        viewHolder.tv_clinical_mould_content.setText(pathography.get(position).content);
    }

    static class ViewHolder {
        public TextView tv_clinical_mould;
        public EditText tv_clinical_mould_content;

    }
}
