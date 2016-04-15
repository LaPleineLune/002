package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/4/14 0014.
 * 病情模板
 */
public class ClinicalMouldAdapter extends RecyclerView.Adapter {
    private Activity context;

    public ClinicalMouldAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_clinical_mould, parent, false);
        return new ClinicalMouldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalMouldViewHolder)holder).bindData(position);
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

    class ClinicalMouldViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
//        private RelativeLayout rl_clinical_mould;
        private CheckBox checkbox_clinical_mould;
        public ClinicalMouldViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            checkbox_clinical_mould = (CheckBox) rootView.findViewById(R.id.checkbox_clinical_mould);

        }
        public void bindData(final int position) {
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.setClass(context, ClinicalDetailsActivity.class);
//                    context.startActivity(intent);
                    if (checkbox_clinical_mould.isChecked()) {
                        checkbox_clinical_mould.setChecked(false);
                    } else if (!checkbox_clinical_mould.isChecked()) {
                        checkbox_clinical_mould.setChecked(true);
                    }

                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
