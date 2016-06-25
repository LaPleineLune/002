package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.linglan.adapter.clinical.ClinicalMouldAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.GsonTools;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalDetailsBean;
import com.android.linglan.http.bean.ClinicalMouldBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.AlertDialoginter;
import com.android.linglan.widget.AlertDialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LeeMy on 2016/4/13 0013.
 * 病情模板
 */
public class EditClinicalMouldActivity extends BaseActivity implements AlertDialoginter {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private Intent intent = null;
    private RecyclerView rec_clinical_mould;
    private ClinicalMouldAdapter clinicalMouldAdapter;
    private LinearLayout ll_course_mould;
    private LinearLayout ll_no_network;
    private ClinicalMouldBean clinicalMouldBean;
    private ClinicalMouldBean mClinicalMouldBean;
    private ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsPathography> pathography;
    //    private ClinicalMouldBean msClinicalMouldBean;
    private ClinicalMouldBean.ClinicalMouldData clinicalMouldData;
    private String[] msClinicalMouldBean;
    private String[] nameString;
    private String[] idString;
    private Map<String, Object> map;
    private String gsonString;
    private ArrayList<Integer> deleteCount;
    public static int isSave;
    private int alertflag = 1000;
    private int count;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ll_course_mould.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);

                    if (clinicalMouldBean != null && clinicalMouldBean.data != null && mClinicalMouldBean != null && mClinicalMouldBean.data != null) {
                        clinicalMouldAdapter.updateAdapter(clinicalMouldBean.data, mClinicalMouldBean.data);
//                        count = mClinicalMouldBean.data.size();
                        msClinicalMouldBean = new String[mClinicalMouldBean.data.size()];
//                        msClinicalMouldBean.data.clear();
                        for (int i = 0; i < mClinicalMouldBean.data.size(); i++) {
                            msClinicalMouldBean[i] = mClinicalMouldBean.data.get(i).templetname;
//                            msClinicalMouldBean.data = (ArrayList<ClinicalMouldBean.ClinicalMouldData>) mClinicalMouldBean.data.clone();
//                            msClinicalMouldBean.data.get(i).templetname = mClinicalMouldBean.data.get(i).templetname;
//                            msClinicalMouldBean.data.get(i).templetid = mClinicalMouldBean.data.get(i).templetid;
                        }
//                        msClinicalMouldBean.data.addAll(mClinicalMouldBean.data);
                    } else if (clinicalMouldBean != null && clinicalMouldBean.data != null && mClinicalMouldBean == null) {
                        clinicalMouldAdapter.updateAdapter(clinicalMouldBean.data, null);
                    } else {
                        ll_course_mould.setVisibility(View.GONE);
                        ll_no_network.setVisibility(View.VISIBLE);
                    }

                    break;
                case REQUEST_FAILURE:
                    ll_course_mould.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_clinical_mould);
    }

    @Override
    protected void initView() {
        rec_clinical_mould = (RecyclerView) findViewById(R.id.rec_clinical_mould);
        ll_course_mould = (LinearLayout) findViewById(R.id.ll_course_mould);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);

    }

    @Override
    protected void initData() {
        setTitle("病情模板", "保存");
        isSave = 0;
        deleteCount = new ArrayList<>();
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.save);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
        count = 0;
        mClinicalMouldBean = (ClinicalMouldBean) getIntent().getSerializableExtra("clinicalMouldBean");
        pathography = (ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsPathography>) getIntent().getSerializableExtra("pathography");

        if (mClinicalMouldBean == null && pathography != null) {
            mClinicalMouldBean = new ClinicalMouldBean();
            mClinicalMouldBean.data = new ArrayList<>();
                for (int i = 0; i < pathography.size(); i++) {
                    clinicalMouldData = new ClinicalMouldBean.ClinicalMouldData();
                    clinicalMouldData.templetid = pathography.get(i).templetid;
                    clinicalMouldData.templetname = pathography.get(i).templetname;
                    mClinicalMouldBean.data.add(clinicalMouldData);
            }
        }
        rec_clinical_mould.setLayoutManager(new LinearLayoutManager(this));
        rec_clinical_mould.setHasFixedSize(true);
        clinicalMouldAdapter = new ClinicalMouldAdapter(this);
        rec_clinical_mould.setAdapter(clinicalMouldAdapter);

//        getClinicalPersonMould();
        getClinicalAllMould();
    }

    @Override
    protected void setListener() {
        right.setOnClickListener(this);
        ll_no_network.setOnClickListener(this);
    }

    List<Integer> listItemID = new ArrayList<Integer>();

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_no_network:
                initData();
                break;
            case R.id.right:
                listItemID.clear();
                count = 0;


                if (clinicalMouldAdapter.mChecked != null) {
                    for (int i = 0; i < clinicalMouldAdapter.mChecked.size(); i++) {
                        if (!clinicalMouldAdapter.mChecked.get(i).equals("")) {
                            listItemID.add(i);
                        }
                    }
                    if (listItemID.size() == 0) {
                        alertflag = 2;
                        deleteCount.clear();
                        StringBuilder name = new StringBuilder();
                        for (int i = 0; i < mClinicalMouldBean.data.size(); i++) {
                            if (SharedPreferencesUtil.getString(msClinicalMouldBean[i], null) != null) {
                                deleteCount.add(i);
                                name.append(" " +
                                        "[");
                                name.append(msClinicalMouldBean[i]);
                                name.append("] ");
                            }
                        }

                        if (deleteCount.size() == 0) {
                            saveClinicalMould("");
                        }else{
                            AlertDialogs.alert(this, "提示", "若取消选择" +name + "，已有内容将被清空，请慎重选择", "取消", "确定");
                        }
//                    if (listItemID.size() == 0) {
//                        saveClinicalMould("");
                    } else {
                        StringBuilder sb = new StringBuilder();
                        gsonString = "";
                        map = new HashMap<String, Object>();
                        nameString = new String[listItemID.size()];
                        idString = new String[listItemID.size()];
//                        LogUtil.e("一开始的长度msClinicalMouldBean.data.size()" + msClinicalMouldBean.data.size());
//                        LogUtil.e("一开始的长度ClinicalMouldBean.data.size()" + mClinicalMouldBean.data.size());
                        for (int i = 0; i < listItemID.size(); i++) {
                            String id = clinicalMouldBean.data.get(listItemID.get(i)).templetid;
                            String name = clinicalMouldBean.data.get(listItemID.get(i)).templetname;
//                            LogUtil.e("现在勾选主键msClinicalMouldBean.data.get(j).templetname" + name);
                            if (mClinicalMouldBean != null && mClinicalMouldBean.data.size() != 0) {
                                for (int j = 0; j < mClinicalMouldBean.data.size(); j++) {
//                                    LogUtil.e("没有勾选掉的主键msClinicalMouldBean.data.get(j).templetname" + msClinicalMouldBean.data.get(j).templetname);
                                    if (id.equals(mClinicalMouldBean.data.get(j).templetid)) {
//                                        msClinicalMouldBean.data.get(j).templetid = "-1";
                                        msClinicalMouldBean[j] = "清空";
//                                        LogUtil.e("没有勾选掉的id号msClinicalMouldBean.data.get(j).templetid" + id);
                                    }
                                }
                            }


                            nameString[i] = name;
                            idString[i] = id;
                            map.put(id, name);
                            gsonString = GsonTools.createGsonString(map);
                        }


                        if (msClinicalMouldBean != null) {
                            alertflag = 1;
                            deleteCount.clear();
                            StringBuilder name = new StringBuilder();
                            for (int i = 0; i < msClinicalMouldBean.length; i++) {
                                if (msClinicalMouldBean[i] != null && !msClinicalMouldBean[i].equals("清空") && SharedPreferencesUtil.getString(msClinicalMouldBean[i], null) != null) {
//                                if (!msClinicalMouldBean[i].equals("清空")) {
//                                    count = 1;
                                    deleteCount.add(i);
                                    name.append(" " +
                                            "[");
                                    name.append(msClinicalMouldBean[i]);
                                    name.append("] ");
//                                    LogUtil.e("勾选掉的并且有内容的选项 = " + msClinicalMouldBean[i]);
                                }
                            }

                            if (deleteCount.size() == 0) {
                                saveClinicalMould(gsonString);
                            } else {
//                                LogUtil.e("deleteCount.size() = " + deleteCount.size());
                                AlertDialogs.alert(this, "提示", "若取消选择" + name + "，已有内容将被清空，请慎重选择", "取消", "确定");
                            }
                        } else {
                            saveClinicalMould(gsonString);
                        }
                    }
                } else {
                    ToastUtil.show("请选择模板");
                }
                break;
        }
    }

    //获取所有模板
    public void getClinicalAllMould() {
        NetApi.getClinicalAllMould(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalMould" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, EditClinicalMouldActivity.this)) {
                    return;
                }

                clinicalMouldBean = JsonUtil.json2Bean(result, ClinicalMouldBean.class);
                handler.sendEmptyMessage(REQUEST_SUCCESS);

            }

            @Override
            public void onFailure(String message) {
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        });
    }

    //获取个人模板
    public void getClinicalPersonMould() {
        NetApi.getClinicalPersonMould(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalMould" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, EditClinicalMouldActivity.this)) {
                    mClinicalMouldBean = null;
                    return;
                }
                mClinicalMouldBean = JsonUtil.json2Bean(result, ClinicalMouldBean.class);
                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        });
    }

    public void saveClinicalMould(String templet) {
        NetApi.saveClinicalMould(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
//                LogUtil.e("saveClinicalMould" + result);
                isSave = 1;
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, EditClinicalMouldActivity.this)) {
                    return;
                }
                for (int i = 0; i < deleteCount.size(); i++) {
                    SharedPreferencesUtil.saveString(msClinicalMouldBean[deleteCount.get(i)], null);
//                    LogUtil.e("清掉的内容更msClinicalMouldBean.data.get(deleteCount.get(i)).templetname = " + msClinicalMouldBean[deleteCount.get(i)] + "deleteCount.get(i) = " + deleteCount.get(i));
                }

//                intent.putExtra("nameString", nameString);
//                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(String message) {

            }
        }, templet);
    }

    @Override
    public int Altert_btleftdo() {
        switch (alertflag) {
            case 1:
                msClinicalMouldBean = new String[mClinicalMouldBean.data.size()];
                for (int i = 0; i < mClinicalMouldBean.data.size(); i++) {
                    msClinicalMouldBean[i] = mClinicalMouldBean.data.get(i).templetname;
                }
                AlertDialogs.aDialog.dismiss();
                break;

            case 2:
                msClinicalMouldBean = new String[mClinicalMouldBean.data.size()];
                for(int i = 0; i < mClinicalMouldBean.data.size(); i++){
                    msClinicalMouldBean[i] = mClinicalMouldBean.data.get(i).templetname;
                }
                AlertDialogs.aDialog.dismiss();
                break;
        }
        return 0;
    }

    @Override
    public int Altert_btrightdo() {
        switch (alertflag) {
            case 1:
                saveClinicalMould(gsonString);
                break;

            case 2:
                saveClinicalMould("");
                break;
        }
        return 0;
    }
}
