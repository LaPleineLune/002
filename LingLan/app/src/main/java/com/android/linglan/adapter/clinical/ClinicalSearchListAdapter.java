package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.http.bean.ClinicalCollatingBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ClinicalCollatingDetailsActivity;
import com.android.linglan.ui.clinical.ClinicalDetailsActivity;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/8 0008.
 * 搜索临证患者列表
 */
public class ClinicalSearchListAdapter extends RecyclerView.Adapter {
    private Activity context;
    private ClinicalCollatingBean.ClinicalCollatingData clinicalCollatingData;
    private ArrayList<ClinicalCollatingBean.ClinicalCollatingData.ClinicalCollatingList> clinicalCollatingList;

    public ClinicalSearchListAdapter(Activity context) {
        this.context = context;
//        this.clinicalCollatingData = clinicalCollatingData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_clinical_details, parent, false);
        return new ClinicalListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalListViewHolder)holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.clinicalCollatingData != null ? this.clinicalCollatingList.size() : 0 ;
//        return this.clinicalCollatingData != null ? this.clinicalCollatingData.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateAdapter(ClinicalCollatingBean.ClinicalCollatingData clinicalCollatingData, ArrayList<ClinicalCollatingBean.ClinicalCollatingData.ClinicalCollatingList> clinicalCollatingList){
        this.clinicalCollatingData = clinicalCollatingData;
        this.clinicalCollatingList = clinicalCollatingList;
        this.notifyDataSetChanged();
    }

    class ClinicalListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private LinearLayout ll_item_clinical;
        private TextView tv_item_patient_name;
        private TextView tv_item_patient_feature;
        private TextView tv_item_visit_time;
        private View item_view;
        private TextView tv_item_clinical_no_content;
        public ClinicalListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            ll_item_clinical = (LinearLayout) rootView.findViewById(R.id.ll_item_clinical);
            tv_item_patient_name = (TextView) rootView.findViewById(R.id.tv_item_patient_name);
            tv_item_patient_feature = (TextView) rootView.findViewById(R.id.tv_item_patient_feature);
            tv_item_visit_time = (TextView) rootView.findViewById(R.id.tv_item_visit_time);
            item_view = rootView.findViewById(R.id.item_view);
            tv_item_clinical_no_content = (TextView) rootView.findViewById(R.id.tv_item_clinical_no_content);
        }
        public void bindData(final int position) {
//            if (clinicalCollatingData != null) {
            if (clinicalCollatingList.get(position).patientname != null && !clinicalCollatingList.get(position).patientname.equals("")) {
                tv_item_patient_name.setText(clinicalCollatingList.get(position).patientname);
            } else {
                tv_item_patient_name.setText("未命名");
            }
            String feature = clinicalCollatingList.get(position).feature != null && !clinicalCollatingList.get(position).feature.equals("") ? clinicalCollatingList.get(position).feature  : "无内容";
            tv_item_patient_feature.setText("病证：" + feature);
                tv_item_visit_time.setText(clinicalCollatingList.get(position).lastvisittime);
//                if (clinicalCollatingData.get(position).usercasecon <= 0) {
//                    tv_item_clinical_no_content.setVisibility(View.VISIBLE);
//                } else {
//                    tv_item_clinical_no_content.setVisibility(View.GONE);
//                }
//            } else {
//                ll_item_clinical.setVisibility(View.GONE);
//                item_view.setVisibility(View.GONE);
//                tv_item_clinical_no_content.setVisibility(View.VISIBLE);
//            }
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clinicalCollatingData != null) {
                        Intent intent = new Intent();
                        intent.putExtra("illnesscaseid", clinicalCollatingList.get(position).illnesscaseid);
                        if (clinicalCollatingList.get(position).patientname != null && !clinicalCollatingList.get(position).patientname.equals("")) {
                            intent.putExtra("isdemo", clinicalCollatingList.get(position).isdemo);
                            intent.putExtra("clinicalSearch", true);
                            intent.setClass(context, ClinicalDetailsActivity.class);
                        } else {
                            intent.setClass(context, ClinicalCollatingDetailsActivity.class);
                        }
                        context.startActivity(intent);
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
