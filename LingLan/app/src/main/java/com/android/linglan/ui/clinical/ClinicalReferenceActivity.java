package com.android.linglan.ui.clinical;

import android.content.Intent;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 临证参考
 */
public class ClinicalReferenceActivity extends BaseActivity {
    private Intent intent = null;
    @Override
    protected void setView() {
        setContentView(R.layout.activity_clinical_reference);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setTitle("临证参考", "");
        intent = new Intent();

    }

    @Override
    protected void setListener() {

    }
}
