package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.linglan.http.bean.ClinicalDetailsBean;
import com.android.linglan.ui.R;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/25 0025.
 * 病程列表中的Mould的Adapter
 */
public class EditCourseOfDiseaseMouldAdapter extends RecyclerView.Adapter {
    private Activity context;
    private ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsPathography> pathography;

    public EditCourseOfDiseaseMouldAdapter(Activity context, ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsPathography> pathography) {
        this.context = context;
        this.pathography = pathography;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.edit_item_mould, parent, false);
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

//    public void updateAdapter(ArrayList<Bitmap> photoes, String text) {
//        this.photoes = photoes;
//        this.strs.add(text);
//        this.notifyDataSetChanged();
//    }

    class ClinicalListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView tv_clinical_mould;
        private EditText tv_clinical_mould_content;

        public ClinicalListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_clinical_mould = (TextView) rootView.findViewById(R.id.tv_clinical_mould);
            tv_clinical_mould_content = (EditText) rootView.findViewById(R.id.tv_clinical_mould_content);

        }

        public void bindData(final int position) {
            if (TextUtils.isEmpty(pathography.get(position).templetname)) {
                tv_clinical_mould.setVisibility(View.GONE);
            } else {
                tv_clinical_mould.setText(context.getString(R.string.clinical_mould, pathography.get(position).templetname));
            }
            tv_clinical_mould_content.setText(pathography.get(position).content);

            tv_clinical_mould_content.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
//                     notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}
