package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 新建病历
 */
public class ClinicalCreateActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private Intent intent = null;
    private LinearLayout ll_clinical_create;
    private LinearLayout ll_no_network;
    private LinearLayout ll_create_patient;
    private TextView tv_create_spread;
    private TextView tv_create_retract;
    private TextView tv_clinical_mould;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ll_clinical_create.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    ll_clinical_create.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_clinical_create);

    }

    @Override
    protected void initView() {
        ll_clinical_create = (LinearLayout) findViewById(R.id.ll_clinical_create);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        ll_create_patient = (LinearLayout) findViewById(R.id.ll_create_patient);
        tv_create_spread = (TextView) findViewById(R.id.tv_create_spread);
        tv_create_retract = (TextView) findViewById(R.id.tv_create_retract);
        tv_clinical_mould = (TextView) findViewById(R.id.tv_clinical_mould);

    }

    @Override
    protected void initData() {
        setTitle("新建病历", "保存");
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.save);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
    }

    @Override
    protected void setListener() {
        tv_create_spread.setOnClickListener(this);
        tv_create_retract.setOnClickListener(this);
        tv_clinical_mould.setOnClickListener(this);
        ll_no_network.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_create_spread:
                ll_create_patient.setVisibility(View.VISIBLE);
                tv_create_spread.setVisibility(View.GONE);
                break;
            case R.id.tv_create_retract:
                ll_create_patient.setVisibility(View.GONE);
                tv_create_spread.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_clinical_mould:
                intent.setClass(this, ClinicalMouldActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_no_network:
                initData();
                break;
        }
    }
}
