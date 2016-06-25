package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.http.bean.ClinicalClassifyBean;
import com.android.linglan.http.bean.ClinicalDetailsBean;
import com.android.linglan.http.bean.PatientDetailsBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ClinicalDetailsActivity;
import com.android.linglan.ui.clinical.PatientDetailsActivity;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.SyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeMy on 2016/4/12 0012.
 * 病症详情的Adapter
 */
public class ClinicalDetailsAdapter extends RecyclerView.Adapter {
    static final int VIEW_CLINICAL_DETAILS_TITLE = 0;
    static final int VIEW_CLINICAL_DETAILS_LIST = 1;
    public int flag = 0;
    private Activity context;
    private List<ClinicalClassifyBean.ClinicalClassifyData> clinicalClassifyData;
    private PatientDetailsBean.PatientDetailsData patientDetailsData;
    private CourseOfDiseaseListAdapter courseOfDiseaseDetailsListAdapter;
    private ArrayList<ClinicalDetailsBean.ClinicalDetailsData> clinicalDetailsData;

    public ClinicalDetailsAdapter(Activity context) {
        this.context = context;
    }

    public void upDateClassifyAdapter(List<ClinicalClassifyBean.ClinicalClassifyData> clinicalClassifyData) {
        this.clinicalClassifyData = clinicalClassifyData;
        notifyDataSetChanged();
    }

    public void upDateTitleAdapter(PatientDetailsBean.PatientDetailsData patientDetailsData) {
        this.patientDetailsData = patientDetailsData;
        notifyDataSetChanged();
    }

    public void upDateAdapter(ArrayList<ClinicalDetailsBean.ClinicalDetailsData> clinicalDetailsData) {
        this.clinicalDetailsData = clinicalDetailsData;
        notifyDataSetChanged();
    }

    // 病症详情的头部
    private View createClinicalDetailsTitleView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clinical_details_title, parent, false);
        return view;
    }

    // 病症详情列表
    private View createClinicalDetailsListView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clinical_details_list, parent, false);
        return view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case VIEW_CLINICAL_DETAILS_TITLE:
                view = createClinicalDetailsTitleView(parent);
                break;
            case VIEW_CLINICAL_DETAILS_LIST:
                view = createClinicalDetailsListView(parent);
                break;
            default:
                break;
        }
        return new ClinicalDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalDetailsViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ClinicalDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View rootView;
        private Intent intent = null;
        private LinearLayout ll_classify;
        private TextView tv_classify1;
        private TextView tv_classify2;
        private RelativeLayout rl_patient_info;
        private TextView tv_name;
        private TextView tv_sex;
        private TextView tv_age;
        private TextView tv_clinical_sort;
        private int isdemo = 0;
        private String sort = "desc";// {visittime:asc}-------(asc升序 desc降序）（visittime:就诊时间）

        private RecyclerView rec_clinical_details_list;

        public ClinicalDetailsViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            initView(rootView);
        }

        private void initView(View rootView) {
            ll_classify = (LinearLayout) rootView.findViewById(R.id.ll_classify);
            tv_classify1 = (TextView) rootView.findViewById(R.id.tv_classify1);
            tv_classify2 = (TextView) rootView.findViewById(R.id.tv_classify2);
            rl_patient_info = (RelativeLayout) rootView.findViewById(R.id.rl_patient_info);
            tv_name = (TextView) rootView.findViewById(R.id.tv_name);
            tv_sex = (TextView) rootView.findViewById(R.id.tv_sex);
            tv_age = (TextView) rootView.findViewById(R.id.tv_age);
            tv_clinical_sort = (TextView) rootView.findViewById(R.id.tv_clinical_sort);

            rec_clinical_details_list = (RecyclerView) rootView.findViewById(R.id.rec_clinical_details_list);
        }

        private void bindData(int index) {
            intent = new Intent();
            isdemo = context.getIntent().getIntExtra("isdemo", 0);
            switch (index) {
                case VIEW_CLINICAL_DETAILS_TITLE:
                    ll_classify.setVisibility(View.GONE);
                    tv_classify1.setVisibility(View.GONE);
                    tv_classify2.setVisibility(View.GONE);
                    if (clinicalClassifyData != null) {
                        ll_classify.setVisibility(View.VISIBLE);
                        LogUtil.e("clinicalClassifyData的长度=" + clinicalClassifyData.size());
                        if (clinicalClassifyData.size() == 1) {
                            tv_classify1.setVisibility(View.VISIBLE);
                            tv_classify1.setText(clinicalClassifyData.get(0).tagname);
                        } else if (clinicalClassifyData.size() == 2) {
                            tv_classify1.setVisibility(View.VISIBLE);
                            tv_classify2.setVisibility(View.VISIBLE);
                            tv_classify1.setText(clinicalClassifyData.get(0).tagname);
                            tv_classify2.setText(clinicalClassifyData.get(1).tagname);
                        }
                    } else {
                        ll_classify.setVisibility(View.GONE);
                        tv_classify1.setVisibility(View.GONE);
                        tv_classify2.setVisibility(View.GONE);
                    }
                    tv_clinical_sort.setOnClickListener(this);
                    rl_patient_info.setOnClickListener(this);
                    if (patientDetailsData != null) {
                        tv_name.setText(patientDetailsData.patientname);
                        if (patientDetailsData.sex.equals("0")) {
                            tv_sex.setText("男");
                        } else if (patientDetailsData.sex.equals("1")) {
                            tv_sex.setText("女");
                        }
                        String patientAge;
                        if (!patientDetailsData.ageyear.equals("-1") && !patientDetailsData.agemonth.equals("-1")) {
                            patientAge = patientDetailsData.ageyear + "岁" + patientDetailsData.agemonth + "个月";
                        } else if (!patientDetailsData.ageyear.equals("-1") && patientDetailsData.agemonth.equals("-1")) {
                            patientAge = patientDetailsData.ageyear + "岁";
                        } else if (patientDetailsData.ageyear.equals("-1") && !patientDetailsData.agemonth.equals("-1")) {
                            patientAge = patientDetailsData.agemonth + "个月";
                        } else {
                            patientAge = "";
                        }
                        tv_age.setText(patientAge);
                    }
                    break;
                case VIEW_CLINICAL_DETAILS_LIST:
                    rec_clinical_details_list.setLayoutManager(new SyLinearLayoutManager(context));
                    rec_clinical_details_list.setHasFixedSize(true);
                    if (clinicalDetailsData != null) {
                        courseOfDiseaseDetailsListAdapter = new CourseOfDiseaseListAdapter(context, clinicalDetailsData);
                    } else {
                        courseOfDiseaseDetailsListAdapter = new CourseOfDiseaseListAdapter(context, null);
                    }
                    rec_clinical_details_list.setAdapter(courseOfDiseaseDetailsListAdapter);
                    break;
                default:
                    break;
            }


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_patient_info:
                    if (1 == isdemo) {
                        ToastUtil.show("示例病历无法修改");
                    } else {
                        flag = 1;
                        intent.setClass(context, PatientDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("patientDetailsData", patientDetailsData);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                    break;
                case R.id.tv_clinical_sort:
//                    ToastUtil.show("我是排序啊。。。");
                    ((ClinicalDetailsActivity) context).sortClinical();
                    if (sort.equals("desc")) {
                        sort = "asc";
                        Drawable collectTopDrawable = context.getResources().getDrawable(R.drawable.clinical_sort_asc);
                        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                        tv_clinical_sort.setCompoundDrawables(null, null, collectTopDrawable, null);
                    } else if (sort.equals("asc")) {
                        sort = "desc";
                        Drawable collectTopDrawable = context.getResources().getDrawable(R.drawable.clinical_sort_desc);
                        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                        tv_clinical_sort.setCompoundDrawables(null, null, collectTopDrawable, null);
                    }
                    break;
            }
        }
    }
}
