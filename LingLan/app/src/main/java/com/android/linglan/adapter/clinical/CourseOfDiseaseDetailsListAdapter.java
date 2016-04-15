package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.CourseOfDiseaseActivity;
import com.android.linglan.ui.clinical.PatientDetailsActivity;

/**
 * Created by LeeMy on 2016/4/12 0012.
 * 病程详情列表的adapter
 */
public class CourseOfDiseaseDetailsListAdapter extends RecyclerView.Adapter {
    private Activity context;

    public CourseOfDiseaseDetailsListAdapter(Activity context) {
        this.context = context;
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
//        return this.subjectData != null ? this.subjectData.size() : 0 ;
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

//    public void updateAdapter(ArrayList<SubjectDetails.SubjectData> subjectData){
//        this.subjectData = subjectData;
//        this.notifyDataSetChanged();
//    }

    class CourseOfDiseaseDetailsListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private Intent intent = null;
        private TextView tv_course_disease_compile;
        public CourseOfDiseaseDetailsListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_course_disease_compile = (TextView) rootView.findViewById(R.id.tv_course_disease_compile);

        }
        public void bindData(final int position) {
            intent = new Intent();
            tv_course_disease_compile.setOnClickListener(this);
//            rootView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    intent.setClass(context, CourseOfDiseaseActivity.class);
//                    context.startActivity(intent);
//                }
//            });

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_course_disease_compile:
                    intent.setClass(context, CourseOfDiseaseActivity.class);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}
