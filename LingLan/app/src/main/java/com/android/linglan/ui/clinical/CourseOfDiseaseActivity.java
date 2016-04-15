package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 添加或编辑病程
 */
public class CourseOfDiseaseActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private Intent intent = null;
    private RelativeLayout rl_course_disease;
    private LinearLayout ll_no_network;
    private TextView tv_clinical_mould;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    rl_course_disease.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    rl_course_disease.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_course_of_disease);

    }

    @Override
    protected void initView() {
        rl_course_disease = (RelativeLayout) findViewById(R.id.rl_course_disease);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        tv_clinical_mould = (TextView) findViewById(R.id.tv_clinical_mould);
    }

    @Override
    protected void initData() {
        setTitle("添加/编辑病程", "保存");
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.save);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
    }

    @Override
    protected void setListener() {
        tv_clinical_mould.setOnClickListener(this);
        ll_no_network.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
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
