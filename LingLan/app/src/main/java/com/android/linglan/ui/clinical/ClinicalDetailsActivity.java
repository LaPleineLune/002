package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.adapter.clinical.ClinicalDetailsAdapter;
import com.android.linglan.adapter.clinical.CourseOfDiseaseListAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.ClinicalFragment;
import com.android.linglan.http.GsonTools;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalClassifyBean;
import com.android.linglan.http.bean.ClinicalDetailsBean;
import com.android.linglan.http.bean.PatientDetailsBean;
import com.android.linglan.ui.MainActivity;
import com.android.linglan.ui.R;
import com.android.linglan.ui.me.RegisterActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.android.linglan.widget.AlertDialoginter;
import com.android.linglan.widget.AlertDialogs;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 病历详情页
 */
public class ClinicalDetailsActivity extends BaseActivity implements AlertDialoginter {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;

    private PopupWindow popupWindow;
    private View popView;
    private TextView tv_picture_contrast, tv_add_classify, tv_clinical_sort, tv_clinical_delete;

    private RelativeLayout rl_clinical_details;
    private LinearLayout ll_no_network;
    private Button bt_course_of_disease;
    private Button bt_weichat_flup;
    private PtrClassicFrameLayout recycler_view_clinical_details;
    private RecyclerView rec_clinical_details;
    private RecyclerAdapterWithHF mAdapter;
    private ClinicalDetailsAdapter clinicalDetailsAdapter;
    private PatientDetailsBean patientDetailsBean;
    private ClinicalDetailsBean clinicalDetailsBean;
    private  ArrayList<ClinicalDetailsBean.ClinicalDetailsData> clinicalDetailsData;
    private List<ClinicalClassifyBean.ClinicalClassifyData> clinicalClassifyData;
    private Intent intent = null;
    private String illnesscaseid = "";// 病历ID
    private String sort = "desc";// {visittime:asc}-------(asc升序 desc降序）（visittime:就诊时间）
    private int page;//页码
    private int addCourseFlag = 0;//页码
    private int classifyFlag = 0;
    public String courseid;// 病程id
    private int isdemo = 0;
    private int position;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    rl_clinical_details.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    rl_clinical_details.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        if (classifyFlag == 1) {
            classifyFlag = 0;
            getClinicalDetailsCalssify(illnesscaseid);
        }

        if (clinicalDetailsAdapter.flag == 1) {
            clinicalDetailsAdapter.flag = 0;
            getPatientDetails();
        }


        if(CourseOfDiseaseListAdapter.flag == 1 || addCourseFlag == 1){
            CourseOfDiseaseListAdapter.flag = 0;
            addCourseFlag = 0;
            page = 1;
            sort = "desc";
            getClinicalDetailsList(sort, page);
//            clinicalDetailsAdapter = new ClinicalDetailsAdapter(this);
//            mAdapter = new RecyclerAdapterWithHF(clinicalDetailsAdapter);
//            rec_clinical_details.setAdapter(mAdapter);
//            getPatientDetails();
        }
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_clinical_details);
        popView = LayoutInflater.from(this).inflate(R.layout.popupview_clinical_details_function, null);

    }

    @Override
    protected void initView() {
        rl_clinical_details = (RelativeLayout) findViewById(R.id.rl_clinical_details);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        bt_course_of_disease = (Button) findViewById(R.id.bt_course_of_disease);
        bt_weichat_flup = (Button) findViewById(R.id.bt_weichat_flup);
        recycler_view_clinical_details = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_clinical_details);
        rec_clinical_details = (RecyclerView) findViewById(R.id.rec_clinical_details);

        tv_picture_contrast = (TextView) popView.findViewById(R.id.tv_picture_contrast);
        tv_add_classify = (TextView) popView.findViewById(R.id.tv_add_classify);
        tv_clinical_sort = (TextView) popView.findViewById(R.id.tv_clinical_sort);
        tv_clinical_delete = (TextView) popView.findViewById(R.id.tv_clinical_delete);

    }

    @Override
    protected void initData() {
        setTitle("病历详情", "");
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.function);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
        position = getIntent().getIntExtra("page",-1);
        illnesscaseid = getIntent().getStringExtra("illnesscaseid");
        isdemo = getIntent().getIntExtra("isdemo", 0);
        if (1 == isdemo) {
            MobclickAgent.onEvent(ClinicalDetailsActivity.this, UmengBuriedPointUtil.ClinicalMedicalHistoryClickExample);
            bt_course_of_disease.setVisibility(View.GONE);
        } else {
            bt_course_of_disease.setVisibility(View.VISIBLE);
        }
        getClinicalDetailsCalssify(illnesscaseid);
        getPatientDetails();
        page = 1;
        sort = "desc";
        getClinicalDetailsList(sort, page);
        popupWindow = new PopupWindow(this);
        rec_clinical_details.setLayoutManager(new LinearLayoutManager(this));
        rec_clinical_details.setHasFixedSize(true);
        clinicalDetailsAdapter = new ClinicalDetailsAdapter(this);
        mAdapter = new RecyclerAdapterWithHF(clinicalDetailsAdapter);
        rec_clinical_details.setAdapter(mAdapter);
        
    }

    @Override
    protected void setListener() {
        right.setOnClickListener(this);
        back.setOnClickListener(this);
        bt_course_of_disease.setOnClickListener(this);
        bt_weichat_flup.setOnClickListener(this);
        tv_picture_contrast.setOnClickListener(this);
        tv_add_classify.setOnClickListener(this);
        tv_clinical_sort.setOnClickListener(this);
        tv_clinical_delete.setOnClickListener(this);
        ll_no_network.setOnClickListener(this);
        //下拉刷新
        recycler_view_clinical_details.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getClinicalDetailsList(sort, page);
//                clinicalDetailsAdapter = new ClinicalDetailsAdapter(ClinicalDetailsActivity.this);
//                mAdapter = new RecyclerAdapterWithHF(clinicalDetailsAdapter);
//                rec_clinical_details.setAdapter(mAdapter);

//                getPatientDetails();
            }
        });

        //上拉刷新
        recycler_view_clinical_details.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                getClinicalDetailsList(sort, page);
                recycler_view_clinical_details.loadMoreComplete(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right:
                showPopup(v);
                break;
            case R.id.back:
                if (getIntent().getBooleanExtra("clinicalSearch", false)) {
                    finish();
                } else {
                    intent.setClass(ClinicalDetailsActivity.this, MainActivity.class);
//                intent.putExtra("index", 1);
                    intent.putExtra("clinicalDetails", "clinicalDetails");
                    startActivity(intent);
                }
                break;
            case R.id.ll_no_network:
                initData();
                break;
            case R.id.bt_course_of_disease:
                MobclickAgent.onEvent(ClinicalDetailsActivity.this, UmengBuriedPointUtil.ClinicalMedicalHistoryAdd);
                addCourseFlag = 1;
                intent.setClass(this, CourseOfDiseaseActivity.class);
                intent.putExtra("illnesscaseid",illnesscaseid);
                startActivity(intent);
                break;
            case R.id.bt_weichat_flup:
                intent.setClass(this, WeichatFlupActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_picture_contrast:
                MobclickAgent.onEvent(ClinicalDetailsActivity.this, UmengBuriedPointUtil.ClinicalMedicalHistoryPhotoContrast);
                intent.setClass(this, PictureContrastActivity.class);
                intent.putExtra("illnesscaseid",illnesscaseid);
                startActivity(intent);
                popupWindow.dismiss();
                break;
            case R.id.tv_add_classify:
                MobclickAgent.onEvent(ClinicalDetailsActivity.this, UmengBuriedPointUtil.ClinicalMedicalHistoryClass);
                if (NetApi.getToken() != null) {
                    classifyFlag = 1;
                    intent.setClass(this, ClinicalAddClassifyActivity.class);
                    intent.putExtra("illnesscaseid", illnesscaseid);
                    if (clinicalClassifyData != null) {
                        intent.putExtra("clinicalClassifyData", (Serializable) clinicalClassifyData);
                    }
                    startActivity(intent);
                } else {
                    intent.setClass(this, RegisterActivity.class);
                    startActivity(intent);
                }
                popupWindow.dismiss();
                break;
            case R.id.tv_clinical_sort:
                MobclickAgent.onEvent(ClinicalDetailsActivity.this, UmengBuriedPointUtil.ClinicalMedicalHistorySort);
//                ToastUtil.show("我是时间排序");
                if (sort.equals("desc")) {
                    page = 1;
                    sort = "asc";
                    getClinicalDetailsList(sort, page);
                } else {
                    page = 1;
                    sort = "desc";
                    getClinicalDetailsList(sort, page);
                }
                popupWindow.dismiss();
                break;
            case R.id.tv_clinical_delete:
                if (isdemo == 1) {
                    MobclickAgent.onEvent(ClinicalDetailsActivity.this, UmengBuriedPointUtil.ClinicalMedicalHistoryDeleteExample);
                } else {
                    MobclickAgent.onEvent(ClinicalDetailsActivity.this, UmengBuriedPointUtil.ClinicalMedicalHistoryDelete);
                }
                if (NetApi.getToken() != null) {
                    AlertDialogs.alert(ClinicalDetailsActivity.this, "提示", "确认删除此病历？", "取消", "确定");
                } else {
                    intent.setClass(ClinicalDetailsActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                popupWindow.dismiss();
                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LogUtil.d("onKeyUp keyCode: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getIntent().getBooleanExtra("clinicalSearch", false)) {
                finish();
            } else {
                intent.setClass(ClinicalDetailsActivity.this, MainActivity.class);
//            intent.putExtra("index", 1);
                intent.putExtra("clinicalDetails", "clinicalDetails");
                startActivity(intent);
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void showPopup(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        popView.measure(w, h);

        int height = popView.getMeasuredHeight() * 3 / 4;
        int width = popView.getMeasuredWidth() * 3 / 4;
        popupWindow = new PopupWindow(popView, width, height);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        int x = getResources().getDimensionPixelSize(R.dimen.dp80);
        int y = getResources().getDimensionPixelSize(R.dimen.dp12);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]-x, location[1] + v.getHeight()-y);
    }

    public void getPatientDetails() {
        NetApi.getPatientDetails(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getPatientDetails=" + result);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalDetailsActivity.this)){
                    return;
                }
                patientDetailsBean = JsonUtil.json2Bean(result, PatientDetailsBean.class);
                clinicalDetailsAdapter.upDateTitleAdapter(patientDetailsBean.data);
            }

            @Override
            public void onFailure(String message) {

            }
        }, illnesscaseid);
    }

    private void getClinicalDetailsList(String sort, final int page) {
        Map<String, Object> map = new HashMap<String, Object>();
		map.put("visittime", sort);
        String gsonString = GsonTools.createGsonString(map);
        LogUtil.e("我是排序啊="+ gsonString + "页码=" + page + "");
        NetApi.getClinicalDetailsList(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalDetailsList=" + result);
                recycler_view_clinical_details.refreshComplete();
                recycler_view_clinical_details.setLoadMoreEnable(true);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalDetailsActivity.this)) {
                    recycler_view_clinical_details.loadMoreComplete(false);
                    if (HttpCodeJugementUtil.code == 1 && page == 1) {
                        clinicalDetailsAdapter.upDateAdapter(null);
                    }
                    return;
                }
                clinicalDetailsBean = JsonUtil.json2Bean(result, ClinicalDetailsBean.class);

                if (page == 1) {
                    clinicalDetailsData = clinicalDetailsBean.data;
                } else {
                    clinicalDetailsData.addAll(clinicalDetailsBean.data);
                }

                clinicalDetailsAdapter.upDateAdapter(clinicalDetailsData);
                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                recycler_view_clinical_details.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, illnesscaseid, gsonString, page + "");
    }

    private void clinicalDelete(String illnesscaseid){
        NetApi.clinicalDelete(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalDetailsActivity.this)){
                    return;
                }
                ToastUtil.show("已删除");
                ClinicalFragment.ISREFRESHDATA = 2;
                ClinicalFragment.position = position;
                finish();
            }

            @Override
            public void onFailure(String message) {

            }
        },illnesscaseid);
    }

    private void getClinicalDetailsCalssify(String illnesscaseid){
        NetApi.getClinicalDetailsCalssify(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalDetailsCalssify=" + result);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalDetailsActivity.this)){
                    if (HttpCodeJugementUtil.code == 1) {
                        clinicalDetailsAdapter.upDateClassifyAdapter(null);
                    }
                    return;
                }

                ClinicalClassifyBean clinicalClassifyBean = JsonUtil.json2Bean(result, ClinicalClassifyBean.class);
                clinicalClassifyData = clinicalClassifyBean.data;
                clinicalDetailsAdapter.upDateClassifyAdapter(clinicalClassifyData);
            }

            @Override
            public void onFailure(String message) {

            }
        },illnesscaseid);
    }

    private ArrayList<String> mDataMediaId = new ArrayList<String>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            mDataMediaId = data.getStringArrayListExtra("mDataMediaId");
            courseid = data.getStringExtra("courseid");
        }
    }

    public void sortClinical() {
        MobclickAgent.onEvent(ClinicalDetailsActivity.this, UmengBuriedPointUtil.ClinicalMedicalHistorySort);
//                ToastUtil.show("我是时间排序");
        if (sort.equals("desc")) {
            page = 1;
            sort = "asc";
            getClinicalDetailsList(sort, page);
        } else {
            page = 1;
            sort = "desc";
            getClinicalDetailsList(sort, page);
        }
    }

    @Override
    public int Altert_btleftdo() {
        return 0;
    }

    @Override
    public int Altert_btrightdo() {
        //删除病历
        clinicalDelete(illnesscaseid);
        return 0;
    }
}
