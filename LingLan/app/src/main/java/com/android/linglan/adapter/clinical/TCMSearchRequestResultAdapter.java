package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.TCMSearchRequestResultActivity;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.android.linglan.widget.SyLinearLayoutManager;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LeeMy on 2016/5/1 0001.
 * 中医搜搜中的结果返回列表
 */
public class TCMSearchRequestResultAdapter extends RecyclerView.Adapter {
    static final int VIEW_CLINICAL_COLLATING_TITLE = 0;
    static final int VIEW_CLINICAL_COLLATING_LIST = 1;
    private Activity context;
    private List<Object> mTCMSearchData;
    private String key;
//    Map<String, Object> d = (Map<String, Object>) data.get(0);
    private TCMSearchRequestResultListAdapter adapter;

    public TCMSearchRequestResultAdapter(Activity context) {
        this.context = context;
    }

    public void updateAdapter(List<Object> mTCMSearchData,String key) {
        this.mTCMSearchData = mTCMSearchData;
        this.key = key;
        notifyDataSetChanged();
    }

    // 结果返回列表的头部
    private View createClinicalCollatingTitleView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tcm_search_title, parent, false);// item_clinical_list
        return view;
    }

    // 搜索返回的结果列表
    private View createClinicalCollatingListView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_clinical_details_list, parent, false);
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

        private EditText edt_tcm_search;
        private TextView tv_tcm_search;
        private TextView tv_tcm_search_request;
        private int page = 1;
        private Map<String, Object> data = new HashMap<>();

        private RecyclerView rec_clinical_details_list;

        public ClinicalCollatingViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            initView(rootView);
        }

        private void initView(View rootView) {
            edt_tcm_search = (EditText) rootView.findViewById(R.id.edt_tcm_search);
            tv_tcm_search = (TextView) rootView.findViewById(R.id.tv_tcm_search);
            tv_tcm_search_request = (TextView) rootView.findViewById(R.id.tv_tcm_search_request);

            rec_clinical_details_list = (RecyclerView) rootView.findViewById(R.id.rec_clinical_details_list);
        }

        private void bindData(int index) {
            switch (index) {
                case VIEW_CLINICAL_COLLATING_TITLE:
//                    key = ((TCMSearchRequestResultActivity) context).key;
                    tv_tcm_search.setOnClickListener(this);
//                    (Map<String, Object>) data.get(0)
//                    data = (Map<String, Object>) mTCMSearchData.get(mTCMSearchData.size() - 1);Object mData : mTCMSearchData
                    if(mTCMSearchData != null){
                        data = (Map)mTCMSearchData.get(mTCMSearchData.size() -1);
                        tv_tcm_search_request.setVisibility(View.VISIBLE);
                        tv_tcm_search_request.setText(context.getString(R.string.tcm_search_request, data.get("rec_count"), data.get("time")));
                    } else {
                        tv_tcm_search_request.setVisibility(View.GONE);
                    }
                    edt_tcm_search.setText(((TCMSearchRequestResultActivity) context).key);
                    edt_tcm_search.setSelection(((TCMSearchRequestResultActivity) context).key.length());
                    break;
                case VIEW_CLINICAL_COLLATING_LIST:
                    rec_clinical_details_list.setLayoutManager(new SyLinearLayoutManager(context));
                    rec_clinical_details_list.setHasFixedSize(true);
                    adapter = new TCMSearchRequestResultListAdapter(context, mTCMSearchData,key);
                    rec_clinical_details_list.setAdapter(adapter);
                    break;
                default:
                    break;
            }


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_tcm_search:
                    MobclickAgent.onEvent(context, UmengBuriedPointUtil.ClinicalSosoSearch);
                    ((TCMSearchRequestResultActivity) context).key = edt_tcm_search.getText().toString().trim();
                    ((TCMSearchRequestResultActivity) context).getTCMSearchKey(((TCMSearchRequestResultActivity) context).key, page);
                    break;
            }
        }
    }
}