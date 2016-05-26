package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.linglan.adapter.clinical.ClinicalCollatingAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalCollatingBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 待整理病历（病历整理）
 */
public class ClinicalCollatingActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private LinearLayout ll_clinical_collating;
    private LinearLayout ll_no_network;
    private PtrClassicFrameLayout recycler_view_clinical_collating;
    private RecyclerView rec_clinical_collating;
    private RecyclerAdapterWithHF mAdapter;
    private ClinicalCollatingAdapter clinicalCollatingListAdapter;
    private ClinicalCollatingBean clinicalCollatingBean;
    private ClinicalCollatingBean.ClinicalCollatingData clinicalCollatingData;
    private ArrayList<ClinicalCollatingBean.ClinicalCollatingData.ClinicalCollatingList> clinicalCollatingList;
    private Intent intent = null;
    private int page;//页码
    private String collatingNum = "0";// 未命名个数

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ll_clinical_collating.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    ll_clinical_collating.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        getClinicalCollatingList(page);
        getClinicalCollatingNum();
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_clinical_collating);

    }

    @Override
    protected void initView() {
        ll_clinical_collating = (LinearLayout) findViewById(R.id.ll_clinical_collating);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        recycler_view_clinical_collating = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_clinical_collating);
        rec_clinical_collating = (RecyclerView) findViewById(R.id.rec_clinical_collating);

    }

    @Override
    protected void initData() {
//        collatingNum = getIntent().getStringExtra("collatingNum");
        setTitle("未命名病历(" + collatingNum + ")", "");
        intent = new Intent();
        getClinicalCollatingNum();
        page = 1;
        getClinicalCollatingList(page);
        rec_clinical_collating.setLayoutManager(new LinearLayoutManager(this));
        rec_clinical_collating.setHasFixedSize(true);
        clinicalCollatingListAdapter = new ClinicalCollatingAdapter(this);
        mAdapter = new RecyclerAdapterWithHF(clinicalCollatingListAdapter);
        rec_clinical_collating.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        ll_no_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        //下拉刷新
        recycler_view_clinical_collating.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getClinicalCollatingList(page);
            }
        });

        //上拉刷新
        recycler_view_clinical_collating.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                getClinicalCollatingList(page);
                recycler_view_clinical_collating.loadMoreComplete(true);
            }
        });
    }

    private void getClinicalCollatingList(final int page) {
        NetApi.getClinicalCollatingList(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalCollatingList=" + result);
                recycler_view_clinical_collating.refreshComplete();
                recycler_view_clinical_collating.setLoadMoreEnable(true);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalCollatingActivity.this)){
                    recycler_view_clinical_collating.loadMoreComplete(false);
                    return;
                }
                clinicalCollatingBean = JsonUtil.json2Bean(result, ClinicalCollatingBean.class);
                clinicalCollatingData = clinicalCollatingBean.data;
                if (page == 1) {
                    clinicalCollatingList = clinicalCollatingBean.data.list;
                } else {
                    clinicalCollatingList.addAll(clinicalCollatingBean.data.list);
                }

                clinicalCollatingListAdapter.updateAdapter(clinicalCollatingData, clinicalCollatingList);
                handler.sendEmptyMessage(REQUEST_SUCCESS);

            }

            @Override
            public void onFailure(String message) {
//                LogUtil.e(message);
                recycler_view_clinical_collating.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, null, page + "");
    }

    private void getClinicalCollatingNum() {
        NetApi.getClinicalCollatingNum(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalCollatingNum" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalCollatingActivity.this)) {
                    return;
                }
                try {
                    JSONObject json = new JSONObject(result);
                    collatingNum = json.getJSONObject("data").getString("unnamedcaseflag");
                    if (!collatingNum.equals("0")) {
                        setTitle("未命名病历(" + collatingNum + ")", "");
                    } else {
                        finish();
                    }
                    LogUtil.e("未命名病历的个数=" + collatingNum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
