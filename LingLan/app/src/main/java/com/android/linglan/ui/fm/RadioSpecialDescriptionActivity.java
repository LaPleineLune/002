package com.android.linglan.ui.fm;

import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/6/23 0023.
 * 音频专辑简介页面
 */
public class RadioSpecialDescriptionActivity extends BaseActivity {
    private LinearLayout ll_description;
    private ImageView img_description;
    private TextView tv_description;
    @Override
    protected void setView() {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_radio_special_description);
    }

    @Override
    protected void initView() {
        ll_description = (LinearLayout) findViewById(R.id.ll_description);
        img_description = (ImageView) findViewById(R.id.img_description);
        tv_description = (TextView) findViewById(R.id.tv_description);

    }

    @Override
    protected void initData() {
        String description = getIntent().getStringExtra("description");
        tv_description.setText(description);
    }

    @Override
    protected void setListener() {
        ll_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        img_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
