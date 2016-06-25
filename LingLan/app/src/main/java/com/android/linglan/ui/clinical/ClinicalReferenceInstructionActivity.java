package com.android.linglan.ui.clinical;

import android.view.View;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/5/6 0006.
 * 临证参考说明
 */
public class ClinicalReferenceInstructionActivity extends BaseActivity {
    private TextView tv_instruction;
    private String instruction;
    @Override
    protected void setView() {
        setContentView(R.layout.activity_clinical_reference_instruction);
    }

    @Override
    protected void initView() {
        findViewById(R.id.ll_instruction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_instruction = (TextView) findViewById(R.id.tv_instruction);

    }

    @Override
    protected void initData() {
        instruction = getIntent().getStringExtra("instruction");
        if (instruction != null && !instruction.equals("")) {
            tv_instruction.setText(instruction);
        } else {
            tv_instruction.setText(R.string.clinical_instruction);
        }
    }

    @Override
    protected void setListener() {

    }
}
