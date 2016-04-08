package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.linglan.http.bean.SubjectDetails;
import com.android.linglan.ui.R;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.SyLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/8 0008.
 * 临症首页的Adapter
 */
public class ClinicalAdapter extends RecyclerView.Adapter {
    static final int VIEW_CLINICAL_TITLE = 0;
    static final int VIEW_CLINICAL_LIST = 1;
    private Activity context;
    private ClinicalListAdapter clinicalListAdapter;

    public ClinicalAdapter(Activity context) {
        this.context = context;
    }

//    public void upDateAdapter(ArrayList<SubjectDetails.SubjectData> subjectData) {
//        this.subjectData = subjectData;
//        notifyDataSetChanged();
//    }

    // 临证的头部
    private View createClinicalTitleView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clinical_title, parent, false);
        return view;
    }

    // 临证列表
    private View createClinicalListView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clinical_list, parent, false);
        return view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case VIEW_CLINICAL_TITLE:
                view = createClinicalTitleView(parent);
                break;
            case VIEW_CLINICAL_LIST:
                view = createClinicalListView(parent);
                break;
            default:
                break;
        }
        return new ClinicalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ClinicalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button btn_search;

        private RecyclerView rec_clinical_list;
        private View rootView;

        public ClinicalViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            initView(rootView);
        }

        private void initView(View rootView) {
            btn_search = (Button) rootView.findViewById(R.id.btn_search);

            rec_clinical_list = (RecyclerView) rootView.findViewById(R.id.rec_clinical_list);
        }

        private void bindData(int index) {
            switch (index) {
                case VIEW_CLINICAL_TITLE:
                    btn_search.setOnClickListener(this);
                    break;
                case VIEW_CLINICAL_LIST:
                    rec_clinical_list.setLayoutManager(new SyLinearLayoutManager(context));
                    rec_clinical_list.setHasFixedSize(true);

//                    clinicalListAdapter = new SubjectDetailsListAdapter(context, subjectData);
                    clinicalListAdapter = new ClinicalListAdapter(context);
                    rec_clinical_list.setAdapter(clinicalListAdapter);
                    break;
                default:
                    break;
            }


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_search:
                    ToastUtil.show("我是搜索");
                    break;
            }
        }
    }
}
