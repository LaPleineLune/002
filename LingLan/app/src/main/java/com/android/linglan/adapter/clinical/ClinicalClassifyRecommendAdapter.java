package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.linglan.http.bean.ClinicalClassifyBean;
import com.android.linglan.http.bean.ClinicalMouldBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ClinicalAddClassifyActivity;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LeeMy on 2016/4/29 0029.
 * 自定义分类的Adapter
 */
public class ClinicalClassifyRecommendAdapter extends RecyclerView.Adapter {
    private Activity context;
    public  List<String> recommendChecked;
    private List<ClinicalClassifyBean.ClinicalClassifyData> recommendData;
    private String flag;

    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    public ClinicalClassifyRecommendAdapter(Activity context, List<ClinicalClassifyBean.ClinicalClassifyData> recommendData) {
        this.context = context;
        this.recommendData = recommendData;
        isSelected = new HashMap<Integer, Boolean>();
        recommendChecked = new ArrayList<String>();
        for(int i = 0; i < recommendData.size(); i++) {//clinicalMouldData.size()
            recommendChecked.add("");
            getIsSelected().put(i, false);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_classify_customize, parent, false);
        return new ClinicalMouldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalMouldViewHolder)holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.recommendData != null ? this.recommendData.size() : 0 ;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateAdapter(List<ClinicalClassifyBean.ClinicalClassifyData> recommendData){
        this.recommendData = recommendData;
        this.notifyDataSetChanged();
    }

    class ClinicalMouldViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView tv_classify_customize_name;
        private CheckBox checkbox_classify_customize;
        public ClinicalMouldViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_classify_customize_name = (TextView) rootView.findViewById(R.id.tv_classify_customize_name);
            checkbox_classify_customize = (CheckBox) rootView.findViewById(R.id.checkbox_classify_customize);

        }

        public void bindData(final int position) {
            tv_classify_customize_name.setText(recommendData.get(position).tagname);

            // 根据isSelected来设置checkbox的选中状况
            rootView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isSelected.get(position)) {
                        isSelected.put(position, false);
//                        setIsSelected(isSelected);

                        flag = "";
                        recommendChecked.set(position, "");
                        ClinicalAddClassifyActivity.activityFlag--;
                        LogUtil.e("去掉了了了" + recommendChecked.get(position));
                    } else if (ClinicalAddClassifyActivity.activityFlag < 2) {
                        isSelected.put(position, true);
//                        setIsSelected(isSelected);

                        flag = recommendData.get(position).tagname;
                        recommendChecked.set(position, flag);
                        ClinicalAddClassifyActivity.activityFlag++;
                        LogUtil.e(recommendChecked.get(position));
                    } else if (ClinicalAddClassifyActivity.activityFlag >= 2) {
                        ToastUtil.show("最多只能选择两个分类");
                    }

                    checkbox_classify_customize.setChecked(getIsSelected().get(position));

                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

//    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
//        ClinicalMouldAdapter.isSelected = isSelected;
//    }

}