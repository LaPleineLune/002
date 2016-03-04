package com.android.linglan.ui.me;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;

/**
 * Created by LeeMy on 2016/1/16 0016.
 * 个人简介
 */
public class DescriptionActivity extends BaseActivity {
    private EditText description_edt;
    private TextView description_num;
    private String about;
    @Override
    protected void setView() {
        setContentView(R.layout.activity_description);
    }

    @Override
    protected void initView() {
        description_edt = (EditText) findViewById(R.id.description_edt);
        description_num = (TextView) findViewById(R.id.description_num);

    }

    @Override
    protected void initData() {
        setTitle("个人简介", "保存");
        about = getIntent().getStringExtra("about");
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.save);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        description_edt.setText(about);
        description_edt.setSelection(about.length());
        description_num.setText((100 - about.length()) + "");

    }

    int num = 100;
    @Override
    protected void setListener() {
        description_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                num =  100 - description_edt.getText().toString().length();
                if (num > 0) {
                    description_num.setText("" + num);
                } else if (num <= 0) {
                    description_num.setText("0");
                } else {
                    description_num.setText("100");
                }
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = description_edt.getText().toString().trim();
                if (!text.isEmpty()) {
                    getUserInfoEdit(text);
                } else {
                    ToastUtil.show("请输入个人简介，一百字以内");
                }
            }
        });

    }

    /**
     * 用户信息修改
     * @param about 个人简介(post)
     */
    private void getUserInfoEdit(final String about){
        NetApi.getUserInfoEdit(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
                ToastUtil.show("保存成功");
                Intent intent = new Intent();
                if (about != null) {
                    intent.putExtra("about", about);
                }
                setResult(RESULT_OK, intent);
                finish();

            }

            @Override
            public void onFailure(String message) {

            }
        }, null, null, about, null, null, null);
    }
}
