package com.android.linglan.ui.me;

import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LeeMy on 2016/3/2 0002.
 * 服务条款界面
 */
public class TermsOfServiceActivity extends BaseActivity {
    private TextView terms_of_service;

    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String termsOfService = (String) msg.getData().getString("termsOfService");
            switch (msg.what) {
                case 1:
                    terms_of_service.setText(termsOfService);
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_terms_of_service);
    }

    @Override
    protected void initView() {
        terms_of_service = (TextView) findViewById(R.id.terms_of_service);

    }

    @Override
    protected void initData() {
        setTitle("服务条款", "");
        getAppAgreement();
    }

    @Override
    protected void setListener() {

    }

    private void getAppAgreement() {
        NetApi.getAppAgreement(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
                try {
                    JSONObject resultJson =new JSONObject(result);
                    JSONObject data = resultJson.getJSONObject("data");
                    String termsOfService = data.getString("content");
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("termsOfService",termsOfService);
                    message.setData(bundle);
                    handler.sendMessage(message);
                    message.what = 1;// 标志是哪个线程传数据
//                    ToastUtil.show(about);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
