package com.android.linglan.ui.clinical;

import android.content.Intent;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 患者信息详情
 */
public class PatientDetailsActivity extends BaseActivity {
    private Intent intent = null;
    @Override
    protected void setView() {
        setContentView(R.layout.activity_patient_details);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setTitle("患者信息", "编辑/保存");
        intent = new Intent();

    }

    @Override
    protected void setListener() {

    }
}
