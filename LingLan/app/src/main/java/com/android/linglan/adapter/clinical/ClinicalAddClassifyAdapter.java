package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.linglan.http.bean.ClinicalClassifyBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ClinicalChangeClassifyActivity;
import com.android.linglan.widget.SyLinearLayoutManager;

import java.util.List;

/**
 * Created by LeeMy on 2016/4/28 0028.
 * 添加标签的adapter
 */
public class ClinicalAddClassifyAdapter extends RecyclerView.Adapter {
    static final int VIEW_CLINICAL_COLLATING_TITLE = 0;
    static final int VIEW_CLINICAL_COLLATING_LIST = 1;
    private Activity context;
    private List<ClinicalClassifyBean.ClinicalClassifyData> customizeData;
    private List<ClinicalClassifyBean.ClinicalClassifyData> recommendData;
    public ClinicalClassifyCustomizeAdapter customizeAdapter;
    public ClinicalClassifyRecommendAdapter recommendAdapter;

    public ClinicalAddClassifyAdapter(Activity context) {
        this.context = context;
    }

    public void updateCustomizeAdapter(List<ClinicalClassifyBean.ClinicalClassifyData> customizeData) {
        this.customizeData = customizeData;
        notifyDataSetChanged();
    }

    public void updateRecommendAdapter(List<ClinicalClassifyBean.ClinicalClassifyData> recommendData) {
        this.recommendData = recommendData;
        notifyDataSetChanged();
    }

    // 添加自定义分类的列表
    private View createClinicalCollatingTitleView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classify_customize_list, parent, false);// item_clinical_list
        return view;
    }

    // 添加说明文字
    private View createClinicalCollatingListView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_classify_recommend_list, parent, false);
        return view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case VIEW_CLINICAL_COLLATING_TITLE:
                view = createClinicalCollatingTitleView(parent);
                break;
            case VIEW_CLINICAL_COLLATING_LIST:
                view = createClinicalCollatingListView(parent);
                break;
            default:
                break;
        }
        return new ClinicalCollatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalCollatingViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ClinicalCollatingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View rootView;

        private TextView tv_classify_create;
        private RecyclerView rec_classify_customize_list;
//        private RecyclerView rec_classify_recommend_list;

        public ClinicalCollatingViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            initView(rootView);
        }

        private void initView(View rootView) {
            tv_classify_create = (TextView) rootView.findViewById(R.id.tv_classify_create);
            rec_classify_customize_list = (RecyclerView) rootView.findViewById(R.id.rec_classify_customize_list);

//            rec_classify_recommend_list = (RecyclerView) rootView.findViewById(R.id.rec_classify_recommend_list);
        }

        private void bindData(int index) {
            switch (index) {
                case VIEW_CLINICAL_COLLATING_TITLE:
                    tv_classify_create.setOnClickListener(this);
                    if (customizeData != null) {
                        rec_classify_customize_list.setLayoutManager(new SyLinearLayoutManager(context));
                        rec_classify_customize_list.setHasFixedSize(true);
                        customizeAdapter = new ClinicalClassifyCustomizeAdapter(context, customizeData);
                        rec_classify_customize_list.setAdapter(customizeAdapter);
                    }
                    break;
                case VIEW_CLINICAL_COLLATING_LIST:
//                    if (recommendData != null) {
//                        rec_classify_recommend_list.setLayoutManager(new SyLinearLayoutManager(context));
//                        rec_classify_recommend_list.setHasFixedSize(true);
//
//                        recommendAdapter = new ClinicalClassifyRecommendAdapter(context, recommendData);
//                        rec_classify_recommend_list.setAdapter(recommendAdapter);
//                    }
                    break;
                default:
                    break;
            }


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_classify_create:
                    Intent intent = new Intent();
                    intent.setClass(context, ClinicalChangeClassifyActivity.class);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}