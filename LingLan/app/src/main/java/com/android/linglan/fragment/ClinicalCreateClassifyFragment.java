package com.android.linglan.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.linglan.adapter.clinical.ClinicalCreateClassifyAdapter;
import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalClassifyBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeMy on 2016/4/27 0027.
 * 新建分类页面的Fragment
 */
public class ClinicalCreateClassifyFragment extends BaseFragment {
    private View rootView;
    private RecyclerView rec_manage_classify;
    private ClinicalCreateClassifyAdapter adapter;
    private List<ClinicalClassifyBean.ClinicalClassifyData> clinicalClassifyData;
    private List<ClinicalClassifyBean.ClinicalClassifyData> data = new ArrayList<ClinicalClassifyBean.ClinicalClassifyData>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_manage_classify, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {
        rec_manage_classify = (RecyclerView) rootView.findViewById(R.id.rec_manage_classify);

    }

    @Override
    protected void initData() {
        getClinicalClassify();
        rec_manage_classify.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec_manage_classify.setHasFixedSize(true);
        adapter = new ClinicalCreateClassifyAdapter(getActivity());
        rec_manage_classify.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
//        switch (v.getId()) {
//            case R.id.tv_create_classify:
//                ((ClinicalManageClassifyActivity) getActivity()).showCreateFragment();
//                break;
//        }
    }

    private void getClinicalClassify() {
        NetApi.getClinicalClassify(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalClassify=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, getActivity())) {
                    return;
                }

                ClinicalClassifyBean clinicalClassifyBean = JsonUtil.json2Bean(result, ClinicalClassifyBean.class);

                clinicalClassifyData = clinicalClassifyBean.data;

                data.clear();
                for(ClinicalClassifyBean.ClinicalClassifyData datas : clinicalClassifyData) {
                    if (!datas.userid.equals("0")) {
                        data.add(datas);
                    }
                }

                adapter.updateAdapter(data);
            }

            @Override
            public void onFailure(String message) {
            }
        });
    }

    public String getClassifyName() {
        return adapter.getEditContent();
    }
}
