package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.linglan.adapter.clinical.ClinicalAddClassifyAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.ClinicalFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalClassifyBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LeeMy on 2016/4/28 0028.
 * 给病历添加分类页面
 */
public class ClinicalAddClassifyActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private Intent intent = null;
    private RecyclerView rec_clinical_mould;
    private LinearLayout ll_add_classify;
    private LinearLayout ll_no_network;
    private ClinicalAddClassifyAdapter adapter;
    private List<ClinicalClassifyBean.ClinicalClassifyData> clinicalClassifyData;
    private List<ClinicalClassifyBean.ClinicalClassifyData> mClinicalClassifyData;
    private List<ClinicalClassifyBean.ClinicalClassifyData> customizeData = new ArrayList<ClinicalClassifyBean.ClinicalClassifyData>();
    private List<ClinicalClassifyBean.ClinicalClassifyData> recommendData = new ArrayList<ClinicalClassifyBean.ClinicalClassifyData>();

    private String[] nameString;
    private String[] idString;
    private Map<String, Object> map;
    private String illnesscaseid;
    public static int activityFlag = 0;
    private List<Integer> listItemID1 = new ArrayList<Integer>();
    private List<Integer> listItemID2 = new ArrayList<Integer>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ll_add_classify.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    ll_add_classify.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        activityFlag = 0;
        getClinicalClassify();
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_clinical_add_classify);
    }

    @Override
    protected void initView() {
        rec_clinical_mould = (RecyclerView) findViewById(R.id.rec_clinical_mould);
        ll_add_classify = (LinearLayout) findViewById(R.id.ll_add_classify);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);

    }

    @Override
    protected void initData() {
        setTitle("添加分类", "保存");
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.save);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
        illnesscaseid = getIntent().getStringExtra("illnesscaseid");
        mClinicalClassifyData = (List<ClinicalClassifyBean.ClinicalClassifyData>) getIntent().getSerializableExtra("clinicalClassifyData");
        activityFlag = 0;

        rec_clinical_mould.setLayoutManager(new LinearLayoutManager(this));
        rec_clinical_mould.setHasFixedSize(true);
        adapter = new ClinicalAddClassifyAdapter(this);
        rec_clinical_mould.setAdapter(adapter);

        getClinicalClassify();
    }

    @Override
    protected void setListener() {
//        back.setOnClickListener(this);
        right.setOnClickListener(this);
        ll_no_network.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_no_network:
                initData();
                break;
            case R.id.right:
                listItemID1.clear();
                listItemID2.clear();
                StringBuilder str = new StringBuilder();
                for (int i = 0; adapter.customizeAdapter != null && i < adapter.customizeAdapter.customizeChecked.size(); i++) {
                    if (!adapter.customizeAdapter.customizeChecked.get(i).equals("")) {
                        listItemID1.add(i);
//                        str.append(customizeData.get(listItemID1.get(i)).tagid);
                        str.append(customizeData.get(i).tagid);
                        str.append(",");
                    }
                }

                for (int i = 0; adapter.recommendAdapter != null && i < adapter.recommendAdapter.recommendChecked.size(); i++) {
                    if (!adapter.recommendAdapter.recommendChecked.get(i).equals("")) {
                        listItemID2.add(i);
//                        str.append(recommendData.get(listItemID2.get(i)).tagid);
                        str.append(recommendData.get(i).tagid);
                        str.append(",");
                    }
                }
                int itemidLength = listItemID1.size() + listItemID2.size();
                LogUtil.e("i的长度" + itemidLength);
                LogUtil.e("选择的分类=" + str.toString());
                addClassify2ClinicalDetails(str.toString());
                break;
        }
    }

    //获取所有分类
    private void getClinicalClassify() {
        NetApi.getClinicalClassify(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalClassify=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalAddClassifyActivity.this)) {
                    return;
                }

                ClinicalClassifyBean clinicalClassifyBean = JsonUtil.json2Bean(result, ClinicalClassifyBean.class);

                clinicalClassifyData = clinicalClassifyBean.data;

                customizeData.clear();
                recommendData.clear();
                for (ClinicalClassifyBean.ClinicalClassifyData datas : clinicalClassifyData) {
                    if (!datas.userid.equals("0")) {
                        customizeData.add(datas);
                    } else {
                        recommendData.add(datas);
                    }
                }
                adapter.updateCustomizeAdapter(customizeData);
                adapter.updateRecommendAdapter(recommendData);
                handler.sendEmptyMessage(REQUEST_SUCCESS);

            }

            @Override
            public void onFailure(String message) {
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        });
    }

    public void addClassify2ClinicalDetails(String tagid) {// illnesscaseid
        NetApi.addClassify2ClinicalDetails(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("saveClinicalMould" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalAddClassifyActivity.this)) {
                    return;
                }
                MobclickAgent.onEvent(ClinicalAddClassifyActivity.this, UmengBuriedPointUtil.ClinicalMedicalHistoryClassSucceed);


//                intent.putExtra("nameString", nameString);
//                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(String message) {

            }
        }, illnesscaseid, tagid);
    }
}