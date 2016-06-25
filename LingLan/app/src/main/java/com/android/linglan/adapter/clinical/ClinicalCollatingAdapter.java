package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.linglan.http.bean.ClinicalCollatingBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ClinicalCollatingDetailsActivity;
import com.android.linglan.ui.clinical.ClinicalDetailsActivity;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/20 0020.
 * 未命名病历列表的Adapter
 */
public class ClinicalCollatingAdapter extends RecyclerView.Adapter {
    private Activity context;
    private ClinicalCollatingBean.ClinicalCollatingData clinicalCollatingData;
    private ArrayList<ClinicalCollatingBean.ClinicalCollatingData.ClinicalCollatingList> clinicalCollatingList;

    public ClinicalCollatingAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_clinical_collating, parent, false);
        return new ClinicalListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalListViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.clinicalCollatingData != null ? this.clinicalCollatingList.size() : 0 ;
//        return 10;
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
        private TextView tv_item_patient_name;
        private TextView tv_item_patient_feature;
        private TextView tv_item_visit_time;

        public ClinicalListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_item_patient_name = (TextView) rootView.findViewById(R.id.tv_item_patient_name);
            tv_item_patient_feature = (TextView) rootView.findViewById(R.id.tv_item_patient_feature);
            tv_item_visit_time = (TextView) rootView.findViewById(R.id.tv_item_visit_time);

        }

        public void bindData(final int position) {
//            tv_item_patient_name.setText(clinicalCollatingData.get(position).patientname);
            String feature = clinicalCollatingList.get(position).feature != null && !clinicalCollatingList.get(position).feature.equals("") ? clinicalCollatingList.get(position).feature : "无内容";
            tv_item_patient_feature.setText("病证：" + feature);
            tv_item_visit_time.setText(clinicalCollatingList.get(position).lastvisittime);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("illnesscaseid", clinicalCollatingList.get(position).illnesscaseid);
                    intent.putExtra("page", clinicalCollatingList.get(position).page);
//                    intent.setClass(context, ClinicalDetailsActivity.class);
                    intent.setClass(context, ClinicalCollatingDetailsActivity.class);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}