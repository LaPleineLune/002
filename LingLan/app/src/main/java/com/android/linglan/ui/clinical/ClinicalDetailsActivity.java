package com.android.linglan.ui.clinical;

import android.content.Intent;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 病历详情页
 */
public class ClinicalDetailsActivity extends BaseActivity {
    private Intent intent = null;
    @Override
    protected void setView() {
        setContentView(R.layout.activity_clinical_details);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setTitle("病历详情", "");
        intent = new Intent();
    }

    @Override
    protected void setListener() {

    }
}
