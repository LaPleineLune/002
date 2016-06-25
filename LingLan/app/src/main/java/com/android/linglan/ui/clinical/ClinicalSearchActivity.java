package com.android.linglan.ui.clinical;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.linglan.adapter.clinical.ClinicalSearchListAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalCollatingBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.android.linglan.widget.sortlistview.ClearEditText;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 病历搜索页
 */
public class ClinicalSearchActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;

    private LinearLayout ll_clinical_details;
    private LinearLayout ll_no_network;
    private ClearEditText filter_edit;
    private Button btn_search;
    private PtrClassicFrameLayout recycler_view_clinical_details;
    private RecyclerView rec_clinical_details;
    private RecyclerAdapterWithHF mAdapter;
    private ClinicalSearchListAdapter clinicalListAdapter;
    private ClinicalCollatingBean.ClinicalCollatingData clinicalCollatingData;
    private ArrayList<ClinicalCollatingBean.ClinicalCollatingData.ClinicalCollatingList> clinicalCollatingList;
    private int page;//页码
    String key = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ll_clinical_details.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(filter_edit.getWindowToken(),0);
                    ll_clinical_details.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_clinical_search);

    }

    @Override
    protected void initView() {
        ll_clinical_details = (LinearLayout) findViewById(R.id.ll_clinical_details);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        filter_edit = (ClearEditText) findViewById(R.id.filter_edit);
        btn_search = (Button) findViewById(R.id.btn_search);
        recycler_view_clinical_details = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_clinical_search);
        rec_clinical_details = (RecyclerView) findViewById(R.id.rec_clinical_details);
    }

    @Override
    protected void initData() {
        setTitle("搜索", "");
        rec_clinical_details.setLayoutManager(new LinearLayoutManager(this));
        rec_clinical_details.setHasFixedSize(true);
//        clinicalListAdapter = new ClinicalSearchListAdapter(this, clinicalCollatingData);
        clinicalListAdapter = new ClinicalSearchListAdapter(this);
        mAdapter = new RecyclerAdapterWithHF(clinicalListAdapter);
        rec_clinical_details.setAdapter(mAdapter);
//        page = 1;
//        key = getIntent().getStringExtra("key");
//        getClinicalIllnesscaseearch(key, page+"");
    }

    @Override
    protected void setListener() {
        ll_no_network.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        filter_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MobclickAgent.onEvent(ClinicalSearchActivity.this, UmengBuriedPointUtil.ClinicalSearchMedicalHistory);
                page = 1;
                key = filter_edit.getText().toString().trim();
                if (!key.equals(""))
                    getClinicalIllnesscaseearch(key, page);
            }
        });
        //下拉刷新
        recycler_view_clinical_details.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getClinicalIllnesscaseearch(key, page);
//                getClinicalDetailsList(sort, page);
//                clinicalDetailsAdapter = new ClinicalDetailsAdapter(ClinicalSearchActivity.this);
//                mAdapter = new RecyclerAdapterWithHF(clinicalDetailsAdapter);
//                rec_clinical_details.setAdapter(mAdapter);
            }
        });

        //上拉刷新
        recycler_view_clinical_details.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                getClinicalIllnesscaseearch(key, page);
                recycler_view_clinical_details.loadMoreComplete(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_no_network:
//                initData();
                if (!key.equals(""))
                getClinicalIllnesscaseearch(key, page);
                break;
            case R.id.btn_search:
                MobclickAgent.onEvent(ClinicalSearchActivity.this, UmengBuriedPointUtil.ClinicalSearchMedicalHistory);
                page = 1;
                if (!key.equals(""))
                getClinicalIllnesscaseearch(key, page);
                break;
        }
    }

    private void getClinicalIllnesscaseearch(String key, final int page){
        NetApi.getClinicalIllnesscaseearch(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalIllnesscaseearch=" + result);
                recycler_view_clinical_details.refreshComplete();
                recycler_view_clinical_details.setLoadMoreEnable(true);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalSearchActivity.this)){
                    recycler_view_clinical_details.loadMoreComplete(false);
                    if (HttpCodeJugementUtil.code == 1 && page == 1) {
                        clinicalListAdapter.updateAdapter(null, null);
                        ToastUtil.show("抱歉，暂未搜到匹配的病历");
                    }
                    return;
                }

                ClinicalCollatingBean bean = JsonUtil.json2Bean(result, ClinicalCollatingBean.class);
                clinicalCollatingData = bean.data;
                if (page == 1) {
                    clinicalCollatingList = bean.data.list;
                } else {
                    clinicalCollatingList.addAll(bean.data.list);
                }
                clinicalListAdapter.updateAdapter(clinicalCollatingData, clinicalCollatingList);
                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                recycler_view_clinical_details.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, key, page + "");
    }
}
