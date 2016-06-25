package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.http.bean.ClinicalDetailsBean;
import com.android.linglan.ui.R;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/25 0025.
 * 病程列表中的Mould的Adapter
 */
public class CourseOfDiseaseMouldAdapter extends RecyclerView.Adapter {
    private Activity context;
    private ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsPathography> pathography;

    public CourseOfDiseaseMouldAdapter(Activity context, ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsPathography> pathography) {
        this.context = context;
        this.pathography = pathography;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_mould, parent, false);
        return new ClinicalListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalListViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.pathography != null ? this.pathography.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateAdapter(Activity context, ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsPathography> pathograph) {
        this.context = context;
        this.pathography = pathograph;
        this.notifyDataSetChanged();
    }

    class ClinicalListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private LinearLayout ll_clinical_mould;
        private TextView tv_clinical_mould;
        private TextView tv_clinical_mould_content;

        public ClinicalListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            ll_clinical_mould = (LinearLayout) rootView.findViewById(R.id.ll_clinical_mould);
            tv_clinical_mould = (TextView) rootView.findViewById(R.id.tv_clinical_mould);
            tv_clinical_mould_content = (TextView) rootView.findViewById(R.id.tv_clinical_mould_content);

        }

        public void bindData(final int position) {
            if (pathography.get(position).templetname == null && pathography.get(position).content.equals("")) {
                ll_clinical_mould.setVisibility(View.GONE);
                tv_clinical_mould.setVisibility(View.GONE);
                tv_clinical_mould_content.setVisibility(View.GONE);
            } else {
                if (pathography.get(position).templetname == null) {
                    tv_clinical_mould.setVisibility(View.GONE);
                } else if (pathography.get(position).content == null || pathography.get(position).content.equals("")) {
                    ll_clinical_mould.setVisibility(View.GONE);
                    tv_clinical_mould.setVisibility(View.GONE);
                    tv_clinical_mould_content.setVisibility(View.GONE);
                } else {
                    tv_clinical_mould.setText(context.getString(R.string.clinical_mould, pathography.get(position).templetname));
                }
            }

            if(pathography.get(position).content != null && !pathography.get(position).content.equals("")){
                tv_clinical_mould_content.setText(pathography.get(position).content);
            }

        }

        @Override
        public void onClick(View v) {

        }
    }
}
