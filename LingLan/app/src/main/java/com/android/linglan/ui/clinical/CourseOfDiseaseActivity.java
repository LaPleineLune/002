package com.android.linglan.ui.clinical;

import android.content.Intent;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 添加或编辑病程
 */
public class CourseOfDiseaseActivity extends BaseActivity {
    private Intent intent = null;
    @Override
    protected void setView() {
        setContentView(R.layout.activity_course_of_disease);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        setTitle("病程", "添加/保存");
        intent = new Intent();
    }

    @Override
    protected void setListener() {

    }
}
