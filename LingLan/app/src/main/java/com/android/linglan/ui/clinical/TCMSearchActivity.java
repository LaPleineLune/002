package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.text.TextUtils;
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
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LeeMy on 2016/5/1 0001.
 * 中医搜搜的搜索界面
 */
public class TCMSearchActivity extends BaseActivity {
    private EditText edt_tcm_search;
    private TextView tv_tcm_search;
    private TextView tv_tcm_instruction;
    @Override
    protected void setView() {
        setContentView(R.layout.activity_tcm_search);

    }

    @Override
    protected void initView() {
        edt_tcm_search = (EditText) findViewById(R.id.edt_tcm_search);
        tv_tcm_search = (TextView) findViewById(R.id.tv_tcm_search);
        tv_tcm_instruction = (TextView) findViewById(R.id.tv_tcm_instruction);

    }

    @Override
    protected void initData() {
        setTitle("中医搜搜", "");
        getPrompt();

    }

    @Override
    protected void setListener() {
        tv_tcm_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(TCMSearchActivity.this, UmengBuriedPointUtil.ClinicalSosoSearch);
                String key = edt_tcm_search.getText().toString().trim();
                if (!TextUtils.isEmpty(key)) {
                    MobclickAgent.onEvent(TCMSearchActivity.this, UmengBuriedPointUtil.ClinicalSelectSoso2);
                    Intent intent = new Intent();
                    intent.putExtra("key", edt_tcm_search.getText().toString().trim());
                    intent.setClass(TCMSearchActivity.this, TCMSearchRequestResultActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show("请输入搜索关键字");
                }
            }
        });
    }

    public void getPrompt() {
        NetApi.getPrompt(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getPrompt=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, TCMSearchActivity.this)) {
                    return;
                }
                try {
                    JSONObject json = new JSONObject(result);
                    String dataInstruction = json.getString("data");
                    tv_tcm_instruction.setText(dataInstruction);
                    LogUtil.e("getPromptMsg=" + dataInstruction);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, "sousou_info");
    }
}
