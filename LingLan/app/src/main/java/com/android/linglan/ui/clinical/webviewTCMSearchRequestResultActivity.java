package com.android.linglan.ui.clinical;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.android.linglan.adapter.clinical.TCMSearchRequestResultAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonHelper;
import com.android.linglan.utils.LogUtil;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LeeMy on 2016/5/1 0001.
 * 中医搜搜的搜索结果界面
 */
public class webviewTCMSearchRequestResultActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private LinearLayout ll_tcm_search_request;
    private LinearLayout ll_no_network;
    public List<Object> data = null;
    public String key = "";
    private int page;

    private WebView web_search_request_result;
    private WebSettings webSettings;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ll_tcm_search_request.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    ll_tcm_search_request.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_webview_tcm_search_request_result);

    }

    @Override
    protected void initView() {
        ll_tcm_search_request = (LinearLayout) findViewById(R.id.ll_tcm_search_request);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        web_search_request_result = (WebView) findViewById(R.id.web_search_request_result);
    }

    @Override
    protected void initData() {
//        setTitle("中医搜搜", "");
        key = getIntent().getStringExtra("key");
        data = new ArrayList<>();
        page = 1;
//        getTCMSearchKey(key, page);

        webSettings = web_search_request_result.getSettings();
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
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        web_search_request_result.requestFocusFromTouch();

        web_search_request_result.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;

            }
        });

//        web_search_request_result.setWebChromeClient(new HelloWebViewClient());
        web_search_request_result.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                // 当加载到100%的时候 进度条自动消失
                webviewTCMSearchRequestResultActivity.this.setProgress(progress * 100);

            }

        });
        String url = String.format(Constants.URL_TCM_SEARCH_KEY, NetApi.aesCryptUtil.encrypt(NetApi.getAppKey()), NetApi.aesCryptUtil.encrypt(NetApi.getToken()), NetApi.aesCryptUtil.encrypt(key), NetApi.aesCryptUtil.encrypt(1+""));
//        web_search_request_result.loadUrl(url);
        web_search_request_result.loadUrl("http://192.168.1.111:8082/Api/Sousou/searchkey?pwd=123456&key=%E4%B8%AD%E5%8C%BB");
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

    public void getTCMSearchKey(final String key, final int page) {
        NetApi.getTCMSearchKey(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getTCMSearchKey=" + result);
//                recycler_view_tcm_search_request.refreshComplete();
//                recycler_view_tcm_search_request.setLoadMoreEnable(true);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, webviewTCMSearchRequestResultActivity.this)) {
//                    recycler_view_tcm_search_request.loadMoreComplete(false);
                    return;
                }
                if (page == 1) {
                    data.clear();
                }

                try {
                    Map<String, Object> map = JsonHelper.toMap(result);
                    data.add(map);
                    if (data != null && data.size() != 0) {
//                        adapter.updateAdapter(data, key);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(REQUEST_SUCCESS);

            }

            @Override
            public void onFailure(String message) {
//                recycler_view_tcm_search_request.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, key, page + "");
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LogUtil.d("onKeyUp keyCode: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK && web_search_request_result.canGoBack()) {
            web_search_request_result.goBack();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
