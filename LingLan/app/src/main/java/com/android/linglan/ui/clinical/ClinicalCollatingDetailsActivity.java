package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.adapter.clinical.CourseOfDiseaseImageAdapter;
import com.android.linglan.adapter.clinical.CourseOfDiseaseMouldAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalCollatingBean;
import com.android.linglan.http.bean.ClinicalDetailsBean;
import com.android.linglan.http.bean.PatientDetailsBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.android.linglan.widget.AlertDialogs;
import com.android.linglan.widget.AlertDialoginter;
import com.android.linglan.widget.SyLinearLayoutManager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/30 0030.
 * 未命名病历详情页
 */
public class ClinicalCollatingDetailsActivity extends BaseActivity implements AlertDialoginter {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private int editClinicalFlag ;

//    private PopupWindow popupWindow;
//    private View popView;
//    private TextView tv_picture_contrast, tv_add_classify, tv_clinical_delete;

    private LinearLayout ll_clinical_Collating_details;
    private LinearLayout ll_no_network;
    private LinearLayout ll_clinical_collating;
    private View view_clinical_collating;
    private Button bt_patient_select;
    private TextView tv_no_name;
    private TextView tv_visit_time;
    private TextView tv_course_disease_compile;
    private TextView tv_feature;
    private RecyclerView rec_img;
    private RecyclerView rec_mould;
    private CourseOfDiseaseImageAdapter imageAdapter;
    private CourseOfDiseaseMouldAdapter mouldAdapter;
    private ArrayList<ClinicalDetailsBean.ClinicalDetailsData> clinicalDetailsData;
    private PatientDetailsBean patientDetailsBean;
    private ArrayList<ClinicalCollatingBean.ClinicalCollatingData> clinicalCollatingData;
    private PatientDetailsBean.PatientDetailsData patientDetailsData;
    private Intent intent = null;
    private String patientid = "";// 病历信息ID
    private String illnesscaseid = "";// 病历ID
    private String sort = "desc";// {visittime:asc}-------(asc升序 desc降序）（visittime:就诊时间）
    private int page;//页码
    private int addCourseFlag = 0;//页码
    private LinearLayoutManager linearLayoutManagerImg;
    private int position;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            ArrayList<ClinicalDetailsBean.ClinicalDetailsData> data = (ArrayList<ClinicalDetailsBean.ClinicalDetailsData>) msg.getData().getSerializable("data");
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ll_clinical_Collating_details.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    ll_clinical_collating.setVisibility(View.VISIBLE);
                    view_clinical_collating.setVisibility(View.VISIBLE);

                    if (clinicalDetailsData.get(0).img != null && clinicalDetailsData.get(0).img.size() != 0) {
//                    linearLayoutManagerImg.setOrientation(LinearLayoutManager.HORIZONTAL);
//                    rec_img.setLayoutManager(linearLayoutManagerImg);
//                    rec_img.setHasFixedSize(true);
                        LinearLayout.LayoutParams lp;
                        lp = (android.widget.LinearLayout.LayoutParams) rec_img.getLayoutParams();
                        int statusBarHeight = getResources().getDimensionPixelSize(R.dimen.dp76);
                        lp.height = statusBarHeight;
                        rec_img.setLayoutParams(lp);
                        imageAdapter.updateAdapter(ClinicalCollatingDetailsActivity.this, clinicalDetailsData.get(0).img);
                    } else {
                        LinearLayout.LayoutParams lp;
                        lp = (android.widget.LinearLayout.LayoutParams) rec_img.getLayoutParams();
                        int statusBarHeight = getResources().getDimensionPixelSize(R.dimen.dp0);
                        lp.height = statusBarHeight;
                        rec_img.setLayoutParams(lp);
                    }

                    if (clinicalDetailsData.get(0).pathography == null) {
                        rec_mould.setHasFixedSize(false);
                        rec_mould.setVisibility(View.GONE);
                    } else {
                        rec_mould.setVisibility(View.VISIBLE);
                        mouldAdapter = new CourseOfDiseaseMouldAdapter(ClinicalCollatingDetailsActivity.this, null);
                        rec_mould.setAdapter(mouldAdapter);
                        mouldAdapter.updateAdapter(ClinicalCollatingDetailsActivity.this, clinicalDetailsData.get(0).pathography);
                    }
                    setData(clinicalDetailsData);
                    break;
                case REQUEST_FAILURE:
                    ll_clinical_Collating_details.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private void setData(ArrayList<ClinicalDetailsBean.ClinicalDetailsData> data) {
        if (data != null && data.get(0) != null && data.size() != 0) {
            ll_clinical_collating.setVisibility(View.VISIBLE);
            view_clinical_collating.setVisibility(View.VISIBLE);
            tv_visit_time.setText(data.get(0).visittime);
            if (!TextUtils.isEmpty(data.get(0).feature)) {
                tv_feature.setText(data.get(0).feature);
            } else {
                tv_feature.setText("无内容");
            }
        } else {
            ll_clinical_collating.setVisibility(View.GONE);
            view_clinical_collating.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        sort = "desc";
        getClinicalDetailsList(sort, page);
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_clinical_collating_details);
//        popView = LayoutInflater.from(this).inflate(R.layout.popupview_clinical_details_function, null);

    }

    @Override
    protected void initView() {
        ll_clinical_Collating_details = (LinearLayout) findViewById(R.id.ll_clinical_Collating_details);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        ll_clinical_collating = (LinearLayout) findViewById(R.id.ll_clinical_collating);
        view_clinical_collating = findViewById(R.id.view_clinical_collating);
        bt_patient_select = (Button) findViewById(R.id.bt_patient_select);
        tv_no_name = (TextView) findViewById(R.id.tv_no_name);
        tv_visit_time = (TextView) findViewById(R.id.tv_visit_time);
        tv_course_disease_compile = (TextView) findViewById(R.id.tv_course_disease_compile);
        tv_feature = (TextView) findViewById(R.id.tv_feature);
        rec_img = (RecyclerView) findViewById(R.id.rec_img);
        rec_mould = (RecyclerView) findViewById(R.id.rec_mould);

    }

    @Override
    protected void initData() {
        setTitle("未命名病历详情", "");
        editClinicalFlag = 0;
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.delete_clinical_collating);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
        illnesscaseid = getIntent().getStringExtra("illnesscaseid");
        position = getIntent().getIntExtra("page", -1);
//        getClinicalList();
        getPatientDetails();
        page = 1;
        sort = "desc";
        getClinicalDetailsList(sort, page);

        linearLayoutManagerImg = new LinearLayoutManager(this);
        linearLayoutManagerImg.setOrientation(LinearLayoutManager.HORIZONTAL);
        rec_img.setLayoutManager(linearLayoutManagerImg);
        rec_img.setHasFixedSize(true);
//        if (clinicalDetailsData != null && clinicalDetailsData.get(0).img != null) {
//            rec_img.setVisibility(View.VISIBLE);
            imageAdapter = new CourseOfDiseaseImageAdapter(this, null,null);
            rec_img.setAdapter(imageAdapter);
//        } else {
//            rec_img.setVisibility(View.GONE);
//        }

        LinearLayoutManager linearLayoutManagerMould = new SyLinearLayoutManager(this);
        linearLayoutManagerMould.setOrientation(LinearLayoutManager.VERTICAL);
        rec_mould.setLayoutManager(linearLayoutManagerMould);
//        if (clinicalDetailsData != null && clinicalDetailsData.get(0).pathography != null) {
//            rec_mould.setVisibility(View.VISIBLE);
            mouldAdapter = new CourseOfDiseaseMouldAdapter(this, null);
            rec_mould.setAdapter(mouldAdapter);
//        } else {
//            rec_mould.setVisibility(View.GONE);
//        }

//        rec_clinical_details.setLayoutManager(new LinearLayoutManager(this));
//        rec_clinical_details.setHasFixedSize(true);
//        clinicalDetailsAdapter = new ClinicalDetailsAdapter(this);
//        mAdapter = new RecyclerAdapterWithHF(clinicalDetailsAdapter);
//        rec_clinical_details.setAdapter(mAdapter);

    }

    @Override
    protected void setListener() {
        right.setOnClickListener(this);
        bt_patient_select.setOnClickListener(this);
        tv_course_disease_compile.setOnClickListener(this);
        tv_no_name.setOnClickListener(this);
        ll_no_network.setOnClickListener(this);
//        bt_weichat_flup.setOnClickListener(this);
//        tv_add_classify.setOnClickListener(this);
//        tv_clinical_delete.setOnClickListener(this);
        //下拉刷新
//        recycler_view_clinical_details.setPtrHandler(new PtrDefaultHandler() {
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                page = 1;
//                getClinicalDetailsList(sort, page);
//                clinicalDetailsAdapter = new ClinicalDetailsAdapter(ClinicalCollatingDetailsActivity.this);
//                mAdapter = new RecyclerAdapterWithHF(clinicalDetailsAdapter);
//                rec_clinical_details.setAdapter(mAdapter);
//            }
//        });

        //上拉刷新
//        recycler_view_clinical_details.setOnLoadMoreListener(new OnLoadMoreListener() {
//
//            @Override
//            public void loadMore() {
//                page++;
//                getClinicalDetailsList(sort, page);
//                recycler_view_clinical_details.loadMoreComplete(true);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right:
//                showPopup(v);
                //删除病历
                AlertDialogs.alert(this, "提示", "是否删除该未命名病历？", "取消", "删除");
                break;
            case R.id.ll_no_network:
                initData();
                break;
            case R.id.bt_patient_select:// 关联患者
//                if (clinicalCollatingData != null) {
                MobclickAgent.onEvent(this, UmengBuriedPointUtil.ClinicalUnnamedRelevance);
                intent.setClass(this, PatientSelectActivity.class);
                    intent.putExtra("illnesscaseid", illnesscaseid);
                    startActivity(intent);
//                } else {
//                    ToastUtil.show("关联列表为空");
//                }
                break;
            case R.id.tv_course_disease_compile:
                editClinicalFlag = 1;
                intent.setClass(this, CourseOfDiseaseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("clinicalDetailsData", clinicalDetailsData.get(0));
                intent.putExtras(bundle);
                intent.putExtra("illnesscaseid", illnesscaseid);
                intent.putExtra("clinicalCollatingDetailsActivity","clinicalCollatingDetailsActivity");
                startActivity(intent);
                break;
            case R.id.bt_weichat_flup:
                intent.setClass(this, WeichatFlupActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_picture_contrast:
                intent.setClass(this, PictureContrastActivity.class);
                intent.putExtra("illnesscaseid", illnesscaseid);
                startActivity(intent);
//                popupWindow.dismiss();
                break;
            case R.id.tv_add_classify:
                intent.setClass(this, ClinicalAddClassifyActivity.class);
                intent.putExtra("illnesscaseid", illnesscaseid);
                startActivity(intent);
//                popupWindow.dismiss();
                break;
            case R.id.tv_clinical_delete:
                MobclickAgent.onEvent(this, UmengBuriedPointUtil.ClinicalUnnamedDelete);
                //删除病历
                clinicalDelete(illnesscaseid);
                break;
            case R.id.tv_no_name:
                intent.setClass(this, PatientDetailsActivity.class);
//                intent.putExtra("patientid", patientid);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("patientDetailsData", patientDetailsData);
                intent.putExtras(bundle1);
                intent.putExtra("illnesscaseid", illnesscaseid);
                startActivity(intent);
                break;
        }
    }


    public void getPatientDetails() {
        NetApi.getPatientDetails(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getPatientDetails=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalCollatingDetailsActivity.this)) {
                    return;
                }
                patientDetailsBean = JsonUtil.json2Bean(result, PatientDetailsBean.class);
                patientDetailsData = patientDetailsBean.data;
//                clinicalDetailsAdapter.upDateTitleAdapter(patientDetailsBean.data);
            }

            @Override
            public void onFailure(String message) {

            }
        }, illnesscaseid);
    }

    private void getClinicalDetailsList(String sort, final int page) {
        NetApi.getClinicalDetailsList(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalDetailsList=" + result);
//                recycler_view_clinical_details.refreshComplete();
//                recycler_view_clinical_details.setLoadMoreEnable(true);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalCollatingDetailsActivity.this)) {
//                    recycler_view_clinical_details.loadMoreComplete(false);
                    ll_clinical_collating.setVisibility(View.GONE);
                    view_clinical_collating.setVisibility(View.GONE);
                    return;
                }

                ClinicalDetailsBean clinicalDetailsBean = JsonUtil.json2Bean(result, ClinicalDetailsBean.class);

//                if (page == 1) {
                    clinicalDetailsData = clinicalDetailsBean.data;
//                } else {
//                    clinicalDetailsData.addAll(clinicalDetailsBean.data);
//                }

//                if (clinicalDetailsData.get(0).img != null && clinicalDetailsData.get(0).img.size() != 0) {
////                    linearLayoutManagerImg.setOrientation(LinearLayoutManager.HORIZONTAL);
////                    rec_img.setLayoutManager(linearLayoutManagerImg);
////                    rec_img.setHasFixedSize(true);
//                    LinearLayout.LayoutParams lp;
//                    lp = (android.widget.LinearLayout.LayoutParams) rec_img.getLayoutParams();
//                    int statusBarHeight = getResources().getDimensionPixelSize(R.dimen.dp76);
//                    lp.height = statusBarHeight;
//                    rec_img.setLayoutParams(lp);
//                    imageAdapter.updateAdapter(ClinicalCollatingDetailsActivity.this, clinicalDetailsData.get(0).img);
//                } else {
//                    LinearLayout.LayoutParams lp;
//                    lp = (android.widget.LinearLayout.LayoutParams) rec_img.getLayoutParams();
//                    int statusBarHeight = getResources().getDimensionPixelSize(R.dimen.dp0);
//                    lp.height = statusBarHeight;
//                    rec_img.setLayoutParams(lp);
//                }
//
//                if (clinicalDetailsData.get(0).pathography == null) {
//                    rec_mould.setHasFixedSize(false);
//                    rec_mould.setVisibility(View.GONE);
//                } else {
//                    mouldAdapter.updateAdapter(ClinicalCollatingDetailsActivity.this, clinicalDetailsData.get(0).pathography);
//                }

//                Message message = new Message();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("data", clinicalDetailsData);
//                message.setData(bundle);// bundle传值，耗时，效率低
//                handler.sendMessage(message);// 发送message信息
//                message.what = REQUEST_SUCCESS;// 标志是哪个线程传数据
                handler.sendEmptyMessage(REQUEST_SUCCESS);

//                clinicalDetailsAdapter.upDateAdapter(clinicalDetailsData);

            }

            @Override
            public void onFailure(String message) {
//                recycler_view_clinical_details.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, illnesscaseid, sort, page + "");
    }

    private void clinicalDelete(String illnesscaseid) {
        NetApi.clinicalDelete(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalCollatingDetailsActivity.this)) {
                    return;
                }
                ToastUtil.show("已删除");
                ClinicalCollatingActivity.ISREFRESHDATA = 2;
                ClinicalCollatingActivity.position = position;
                finish();
            }

            @Override
            public void onFailure(String message) {

            }
        }, illnesscaseid);
    }

//    private void getClinicalList() {
//        NetApi.getClinicalList(new PasserbyClient.HttpCallback() {
//            @Override
//            public void onSuccess(String result) {
//                LogUtil.e("getClinicalList=" + result);
//                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalCollatingDetailsActivity.this)){
//                    return;
//                }
//
//                ClinicalCollatingBean clinicalCollatingBean = JsonUtil.json2Bean(result, ClinicalCollatingBean.class);
//                clinicalCollatingData = clinicalCollatingBean.data;
//
//            }
//
//            @Override
//            public void onFailure(String message) {
//            }
//        }, null, null, null,null);
//    }

    @Override
    public int Altert_btleftdo() {
        return 0;
    }

    @Override
    public int Altert_btrightdo() {
        clinicalDelete(illnesscaseid);
        return 0;
    }
}
