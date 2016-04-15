package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.PatientDetailsActivity;
import com.android.linglan.widget.SyLinearLayoutManager;

/**
 * Created by LeeMy on 2016/4/12 0012.
 * 病症详情的Adapter
 */
public class ClinicalDetailsAdapter extends RecyclerView.Adapter {
    static final int VIEW_CLINICAL_DETAILS_TITLE = 0;
    static final int VIEW_CLINICAL_DETAILS_LIST = 1;
    private Activity context;
    private CourseOfDiseaseDetailsListAdapter courseOfDiseaseDetailsListAdapter;

    public ClinicalDetailsAdapter(Activity context) {
        this.context = context;
    }

//    public void upDateAdapter(ArrayList<SubjectDetails.SubjectData> subjectData) {
//        this.subjectData = subjectData;
//        notifyDataSetChanged();
//    }

    // 临证的头部
    private View createClinicalDetailsTitleView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clinical_details_title, parent, false);
        return view;
    }

    // 临证列表
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
        private RelativeLayout rl_patient_info;

        private RecyclerView rec_clinical_details_list;

        public ClinicalDetailsViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            initView(rootView);
        }

        private void initView(View rootView) {
            rl_patient_info = (RelativeLayout) rootView.findViewById(R.id.rl_patient_info);

            rec_clinical_details_list = (RecyclerView) rootView.findViewById(R.id.rec_clinical_details_list);
        }

        private void bindData(int index) {
            intent = new Intent();
            switch (index) {
                case VIEW_CLINICAL_DETAILS_TITLE:
                    rl_patient_info.setOnClickListener(this);
                    break;
                case VIEW_CLINICAL_DETAILS_LIST:
                    rec_clinical_details_list.setLayoutManager(new SyLinearLayoutManager(context));
                    rec_clinical_details_list.setHasFixedSize(true);

//                    clinicalListAdapter = new SubjectDetailsListAdapter(context, subjectData);
                    courseOfDiseaseDetailsListAdapter = new CourseOfDiseaseDetailsListAdapter(context);
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
                    intent.setClass(context, PatientDetailsActivity.class);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
