package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.PatientDetailsBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 患者信息详情
 */
public class PatientDetailsActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private Intent intent = null;
    private LinearLayout ll_patient_details;
    private LinearLayout ll_no_network;
    private EditText edt_patient_name;
    private RadioGroup rdg_patient_sex;
    private RadioButton rdb_patient_boy;
    private RadioButton rdb_patient_girl;
    private EditText edt_patient_age;
    private EditText edt_patient_age_month;
    private EditText edt_patient_phone;
    private String sexType = "";
    private String illnesscaseid = "";
    private boolean isCollating = false;
    private PatientDetailsBean.PatientDetailsData patientDetailsData;
    private String patientid = "";// 病历信息ID;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ll_patient_details.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    ll_patient_details.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_patient_details);
    }

    @Override
    protected void initView() {
        ll_patient_details = (LinearLayout) findViewById(R.id.ll_patient_details);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        edt_patient_name = (EditText) findViewById(R.id.edt_patient_name);
        rdg_patient_sex = (RadioGroup) findViewById(R.id.rdg_patient_sex);
        rdb_patient_boy = (RadioButton) findViewById(R.id.rdb_patient_boy);
        rdb_patient_girl = (RadioButton) findViewById(R.id.rdb_patient_girl);
        edt_patient_age = (EditText) findViewById(R.id.edt_patient_age);
        edt_patient_age_month = (EditText) findViewById(R.id.edt_patient_age_month);
        edt_patient_phone = (EditText) findViewById(R.id.edt_patient_phone);

    }

    @Override
    protected void initData() {
        setTitle("患者信息", "保存");
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.save);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
        patientDetailsData = (PatientDetailsBean.PatientDetailsData) getIntent().getSerializableExtra("patientDetailsData");
        illnesscaseid = getIntent().getStringExtra("illnesscaseid");
        if (patientDetailsData != null) {
            bindData();
        }
        if (illnesscaseid != null && !illnesscaseid.equals("")) {
            isCollating = true;
        } else {
            isCollating = false;
        }
    }

    @Override
    protected void setListener() {
        ll_no_network.setOnClickListener(this);
        right.setOnClickListener(this);
        rdg_patient_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rdb_patient_boy) {
//                    sexType = rdb_patient_boy.getText().toString();
                    sexType = "0";
                } else if (i == R.id.rdb_patient_girl) {
//                    sexType = rdb_patient_girl.getText().toString();
                    sexType = "1";
                } else {
                    sexType = "";
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_no_network:
                initData();
                break;
            case R.id.right:
                String patientName = edt_patient_name.getText().toString().trim();
                String patientAge = edt_patient_age.getText().toString().trim();
                String patientAgeMonth = edt_patient_age_month.getText().toString().trim();
                String patientPhone = edt_patient_phone.getText().toString().trim();
                if (patientName.equals("")) {
                    ToastUtil.show("请填写患者姓名");
                    edt_patient_name.setFocusable(true);
                    return;
                } else if (!TextUtils.isEmpty(patientAgeMonth) && Integer.parseInt(patientAgeMonth) >= 13) {
                    ToastUtil.show("月份不能大于12个月");
                    edt_patient_age_month.setFocusable(true);
                    return;
                }
//                if (isCollating) {
                    patientid = patientDetailsData.patientid;
//                } else {
//                    patientid = getIntent().getStringExtra("patientid");
//                }
                savePatientDetails(patientid, patientName, patientAge, patientAgeMonth, patientPhone);
                break;
        }
    }

    private void bindData() {
        if (!TextUtils.isEmpty(patientDetailsData.patientname)) {
            edt_patient_name.setText(patientDetailsData.patientname);
            edt_patient_name.setSelection(patientDetailsData.patientname.length());
        }
        if (patientDetailsData.sex.equals("0")) {
            rdb_patient_boy.setChecked(true);
            sexType = "0";
        } else if (patientDetailsData.sex.equals("1")) {
            rdb_patient_girl.setChecked(true);
            sexType = "1";
        }
        if (!patientDetailsData.ageyear.equals("-1")) {
            edt_patient_age.setText(patientDetailsData.ageyear);
            edt_patient_age.setSelection(patientDetailsData.ageyear.length());
        }
        if (!patientDetailsData.agemonth.equals("-1")) {
            edt_patient_age_month.setText(patientDetailsData.agemonth);
            edt_patient_age_month.setSelection(patientDetailsData.agemonth.length());
        }
        if (!TextUtils.isEmpty(patientDetailsData.phone)) {
            edt_patient_phone.setText(patientDetailsData.phone);
            edt_patient_phone.setSelection(patientDetailsData.phone.length());
        }
    }

    private void savePatientDetails(String patientid, String patientName, String patientAge, String patientAgeMonth, String patientPhone) {

        LogUtil.e("个人信息=" + patientName + patientAge + patientAgeMonth + patientPhone);
        NetApi.savePatientDetails(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("savePatientDetails" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, PatientDetailsActivity.this)) {
                    return;
                }
                ToastUtil.show("已保存");
                if (!isCollating) {
                    finish();
                } else {
                    intent.setClass(PatientDetailsActivity.this, ClinicalDetailsActivity.class);
                    intent.putExtra("illnesscaseid", illnesscaseid);
                    startActivity(intent);
//                    finish();
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, patientid, patientName, sexType, patientAge, patientAgeMonth, patientPhone);
    }
}
