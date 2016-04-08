package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ClinicalDetailsActivity;

/**
 * Created by LeeMy on 2016/4/8 0008.
 * 临证患者列表
 */
public class ClinicalListAdapter extends RecyclerView.Adapter {
    private Activity context;

    public ClinicalListAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_clinical_details_list, parent, false);
        return new ClinicalListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalListViewHolder)holder).bindData(position);
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

    class ClinicalListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
//        private TextView tv_item_history_search;
        public ClinicalListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
//            tv_item_history_search = (TextView) rootView.findViewById(R.id.tv_item_history_search);

        }
        public void bindData(final int position) {
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, ClinicalDetailsActivity.class);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
