package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.adapter.SubjectDetailsListAdapter;
import com.android.linglan.http.bean.ClinicalDetailsBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.CourseOfDiseaseActivity;
import com.android.linglan.ui.clinical.PatientDetailsActivity;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.SyLinearLayoutManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/12 0012.
 * 病程列表的adapter
 */
public class CourseOfDiseaseListAdapter extends RecyclerView.Adapter {
    private Activity context;
    private ArrayList<ClinicalDetailsBean.ClinicalDetailsData> clinicalDetailsData;
    private CourseOfDiseaseImageAdapter imageAdapter;
    private CourseOfDiseaseMouldAdapter mouldAdapter;
    public static int flag = 0;

    public CourseOfDiseaseListAdapter(Activity context, ArrayList<ClinicalDetailsBean.ClinicalDetailsData> clinicalDetailsData) {
        this.context = context;
        this.clinicalDetailsData = clinicalDetailsData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_course_disease_details, parent, false);
        return new CourseOfDiseaseDetailsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CourseOfDiseaseDetailsListViewHolder)holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.clinicalDetailsData != null ? this.clinicalDetailsData.size() : 0 ;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

//    public void updateAdapter(ArrayList<ClinicalDetailsBean.ClinicalDetailsData> clinicalDetailsData){
//        this.clinicalDetailsData = clinicalDetailsData;
//        this.notifyDataSetChanged();
//    }

    class CourseOfDiseaseDetailsListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private Intent intent = null;
        private TextView tv_visit_time;
        private TextView tv_course_disease_compile;
        private TextView tv_feature;
        private RecyclerView rec_img;
        private RecyclerView rec_mould;
        private int isdemo = 0;
        public CourseOfDiseaseDetailsListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_visit_time = (TextView) rootView.findViewById(R.id.tv_visit_time);
            tv_course_disease_compile = (TextView) rootView.findViewById(R.id.tv_course_disease_compile);
            tv_feature = (TextView) rootView.findViewById(R.id.tv_feature);
            rec_img = (RecyclerView) rootView.findViewById(R.id.rec_img);
            rec_mould = (RecyclerView) rootView.findViewById(R.id.rec_mould);

        }
        public void bindData(final int position) {
            intent = new Intent();
            isdemo = context.getIntent().getIntExtra("isdemo", 0);
            tv_course_disease_compile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (1 == isdemo) {
                        ToastUtil.show("示例病历无法修改");
                    } else {
                        intent.setClass(context, CourseOfDiseaseActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("clinicalDetailsData", clinicalDetailsData.get(position));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        flag = 1;
                    }
                }
            });
            tv_visit_time.setText(clinicalDetailsData.get(position).visittime);
            String feature = clinicalDetailsData.get(position).feature;
            tv_feature.setText(feature != null && !feature.equals("") ? feature : "无内容");
//            tv_feature.setText(clinicalDetailsData.get(position).feature);

            LinearLayoutManager linearLayoutManagerImg = new LinearLayoutManager(context);
            linearLayoutManagerImg.setOrientation(LinearLayoutManager.HORIZONTAL);
            rec_img.setLayoutManager(linearLayoutManagerImg);
            rec_img.setHasFixedSize(true);
            if (clinicalDetailsData.get(position).img != null && clinicalDetailsData.get(position).img.size() != 0) {
                rec_img.setLayoutManager(linearLayoutManagerImg);
                rec_img.setHasFixedSize(true);
                imageAdapter = new CourseOfDiseaseImageAdapter(context, clinicalDetailsData.get(position).img,clinicalDetailsData.get(position).courseid);
                rec_img.setAdapter(imageAdapter);
            } else {
                LinearLayout.LayoutParams lp;
                lp = (android.widget.LinearLayout.LayoutParams) rec_img.getLayoutParams();
                int statusBarHeight = context.getResources().getDimensionPixelSize(R.dimen.dp0);
                lp.height = statusBarHeight;
                rec_img.setLayoutParams(lp);
            }

            LinearLayoutManager linearLayoutManagerMould = new SyLinearLayoutManager(context);
            linearLayoutManagerMould.setOrientation(LinearLayoutManager.VERTICAL);
            rec_mould.setLayoutManager(linearLayoutManagerMould);
            if (clinicalDetailsData.get(position).pathography != null) {
//                rec_mould.setVisibility(View.VISIBLE);
                mouldAdapter = new CourseOfDiseaseMouldAdapter(context, clinicalDetailsData.get(position).pathography);
                rec_mould.setAdapter(mouldAdapter);
//            } else {
//                LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) rec_mould.getLayoutParams(); //取控件textView当前的布局参数
//                linearParams.height = 1;// 控件的高强制设成20
////                linearParams.width = 30;// 控件的宽强制设成30
//                rec_mould.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
//
//                rec_mould.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(View v) {

        }
    }
}
