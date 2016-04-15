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
import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/4/13 0013.
 * 病情模板
 */
public class ClinicalMouldActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private Intent intent = null;
    private RecyclerView rec_clinical_mould;
    private ClinicalMouldAdapter clinicalMouldAdapter;
    private LinearLayout ll_course_mould;
    private LinearLayout ll_no_network;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ll_course_mould.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
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
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.save);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
        rec_clinical_mould.setLayoutManager(new LinearLayoutManager(this));
        rec_clinical_mould.setHasFixedSize(true);
        clinicalMouldAdapter = new ClinicalMouldAdapter(this);
        rec_clinical_mould.setAdapter(clinicalMouldAdapter);
    }

    @Override
    protected void setListener() {
        ll_no_network.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_no_network:
                initData();
                break;
        }
    }
}
