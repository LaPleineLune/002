package com.android.linglan.ui.clinical;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.TCMSearchBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;

/**
 * Created by LeeMy on 2016/5/1 0001.
 * 中医搜搜详情界面
 */
public class webViewTCMSearchDetailsActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;

    private LinearLayout ll_tcm_search_details;
    private LinearLayout ll_no_network;
//    private TextView tv_tcm_search_title;
//    private TextView tv_tcm_search_position;
//    private TextView tv_tcm_search_content;
//    private TextView tv_tcm_search_source;
    private String key = "";
    private String page = "";
    private String itemid = "";
    private TCMSearchBean.TCMSearchData mTCMSearchData;
    private WebView web_search_details;
    private WebSettings webSettings;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TCMSearchBean.TCMSearchData data = (TCMSearchBean.TCMSearchData) msg.getData().getSerializable("data");
            switch (msg.what) {
                case REQUEST_SUCCESS:
//                    LogUtil.d("第二次" + data.toString());
                    ll_tcm_search_details.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    if (data != null) {
                        setData(data);
                    } else {
                        ToastUtil.show("暂无数据");
                    }
                    break;
                case REQUEST_FAILURE:
                    ll_tcm_search_details.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_webview_tcm_search_details);

    }

    @Override
    protected void initView() {
        ll_tcm_search_details = (LinearLayout) findViewById(R.id.ll_tcm_search_details);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        web_search_details = (WebView) findViewById(R.id.web_search_details);
//        tv_tcm_search_title = (TextView) findViewById(R.id.tv_tcm_search_title);
//        tv_tcm_search_position = (TextView) findViewById(R.id.tv_tcm_search_position);
//        tv_tcm_search_content = (TextView) findViewById(R.id.tv_tcm_search_content);
//        tv_tcm_search_source = (TextView) findViewById(R.id.tv_tcm_search_source);

    }

    @Override
    protected void initData() {
        setTitle("中医搜搜", "");
        key = getIntent().getStringExtra("key");
        page = getIntent().getStringExtra("page");
        itemid = getIntent().getStringExtra("itemid");
//        getTCMSearchDetails(key, 1+"", itemid);

        webSettings = web_search_details.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportMultipleWindows(true);// 新加

        webSettings.setJavaScriptEnabled(true);// 支持js
        webSettings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        webSettings.setUseWideViewPort(false);// 将图片调整到适合webview的大小
        webSettings.setLoadsImagesAutomatically(true);// 支持自动加载图片
        webSettings.setPluginState(WebSettings.PluginState.ON);//支持多媒体
        web_search_details.requestFocusFromTouch();

        web_search_details.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                // 当加载到100%的时候 进度条自动消失
                webViewTCMSearchDetailsActivity.this.setProgress(progress * 100);

            }

        });
        String url = String.format(Constants.URL_TCM_SEARCH_DETAILS, NetApi.aesCryptUtil.encrypt(NetApi.getAppKey()), NetApi.aesCryptUtil.encrypt(NetApi.getToken()), NetApi.aesCryptUtil.encrypt(key), NetApi.aesCryptUtil.encrypt(page), NetApi.aesCryptUtil.encrypt(itemid));
        web_search_details.loadUrl(url);

    }

    @Override
    protected void setListener() {
        ll_no_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

    }

    private void getTCMSearchDetails(String key, String page, String itemid) {
        NetApi.getTCMSearchDetails(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getTCMSearchDetails=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, webViewTCMSearchDetailsActivity.this)) {
                    if (HttpCodeJugementUtil.code == 1) {
                        ToastUtil.show("暂无数据");
                    }
                    return;
                }

                TCMSearchBean mTCMSearchBean = JsonUtil.json2Bean(result, TCMSearchBean.class);
                mTCMSearchData = mTCMSearchBean.data;
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", mTCMSearchData);
                message.setData(bundle);// bundle传值，耗时，效率低
                handler.sendMessage(message);// 发送message信息
                message.what = REQUEST_SUCCESS;// 标志是哪个线程传数据
            }

            @Override
            public void onFailure(String message) {
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, key, page, itemid);
    }

    private void setData(TCMSearchBean.TCMSearchData data) {
//        if (data != null) {
//            tv_tcm_search_title.setText(data.title);
//            tv_tcm_search_content.setText(Html.fromHtml(data.content, null, null));
//            tv_tcm_search_source.setText(data.source);
//        }
    }
}
