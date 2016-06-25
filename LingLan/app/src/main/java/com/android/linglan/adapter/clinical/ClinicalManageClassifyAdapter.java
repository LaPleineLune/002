package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.http.bean.ClinicalClassifyBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ClinicalChangeClassifyActivity;
import com.android.linglan.ui.clinical.ClinicalManageClassifyActivity;

import java.util.List;

/**
 * Created by Leemy on 2016/4/27 0027.
 * 管理分类列表的Adapter
 */
public class ClinicalManageClassifyAdapter extends RecyclerView.Adapter {
    private Activity context;
    private List<ClinicalClassifyBean.ClinicalClassifyData> clinicalClassifyData;

    public ClinicalManageClassifyAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_classify, parent, false);
        return new ClinicalListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalListViewHolder)holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.clinicalClassifyData != null ? this.clinicalClassifyData.size() + 1 : 1 ;
//        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateAdapter(List<ClinicalClassifyBean.ClinicalClassifyData> clinicalClassifyData){
        this.clinicalClassifyData = clinicalClassifyData;
        this.notifyDataSetChanged();
    }

    class ClinicalListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private LinearLayout ll_classify;
        private TextView tv_classify;
        private EditText edt_create_classify;
        private TextView tv_declare;
        public ClinicalListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            ll_classify = (LinearLayout) rootView.findViewById(R.id.ll_classify);
            tv_classify = (TextView) rootView.findViewById(R.id.tv_classify);
            edt_create_classify = (EditText) rootView.findViewById(R.id.edt_create_classify);
            tv_declare = (TextView) rootView.findViewById(R.id.tv_declare);
        }
        public void bindData(final int position) {
            if (position == 0) {
                edt_create_classify.setVisibility(View.VISIBLE);
                ll_classify.setVisibility(View.GONE);
                tv_classify.setVisibility(View.GONE);
                edt_create_classify.setFocusable(false);

                if(clinicalClassifyData != null ){
                    tv_declare.setVisibility(View.GONE);
                }else{
                    tv_declare.setVisibility(View.VISIBLE);
                }

            } else {
                edt_create_classify.setVisibility(View.GONE);
                ll_classify.setVisibility(View.VISIBLE);
                tv_classify.setVisibility(View.VISIBLE);
                tv_classify.setText(clinicalClassifyData.get(position - 1).tagname);

                if(clinicalClassifyData.size() != position){
                    tv_declare.setVisibility(View.GONE);
                }else{
                    tv_declare.setVisibility(View.VISIBLE);
                }

            }

            edt_create_classify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ((ClinicalManageClassifyActivity) context).showCreateFragment();
                    Intent intent = new Intent();
                    intent.setClass(context, ClinicalChangeClassifyActivity.class);
                    context.startActivity(intent);
                }
            });

            ll_classify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, ClinicalChangeClassifyActivity.class);
                    intent.putExtra("classifyId", clinicalClassifyData.get(position-1).tagid);
                    intent.putExtra("classifyName", clinicalClassifyData.get(position-1).tagname);
                    intent.putExtra("cont", clinicalClassifyData.get(position-1).cont);
                    context.startActivity(intent);
                }
            });
//            tv_classify.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.setClass(context, ClinicalChangeClassifyActivity.class);
//                    intent.putExtra("classifyId", clinicalClassifyData.get(position-1).tagid);
//                    intent.putExtra("classifyName", clinicalClassifyData.get(position-1).tagname);
//                    context.startActivity(intent);
//                }
//            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}