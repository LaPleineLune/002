package com.android.linglan.ui.study;

import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LeeMy on 2016/5/24 0024.
 */
public class StudyMoreActivity extends BaseActivity {
    private WebView web_study_more;
    private WebSettings webSettings;
    @Override
    protected void setView() {
        setContentView(R.layout.activity_study_more);
    }

    @Override
    protected void initView() {
        web_study_more = (WebView) findViewById(R.id.web_study_more);

    }

    @Override
    protected void initData() {
        setTitle("更多", "");
        getPrompt();
        webSettings = web_study_more.getSettings();
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
        web_study_more.requestFocusFromTouch();

        web_study_more.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                // 当加载到100%的时候 进度条自动消失
                StudyMoreActivity.this.setProgress(progress * 100);
            }
        });
    }

    @Override
    protected void setListener() {

    }

    public void getPrompt() {
        NetApi.getPrompt(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getPrompt=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, StudyMoreActivity.this)) {
                    return;
                }
                try {
                    JSONObject json = new JSONObject(result);
                    String dataInstruction = json.getString("data");
                    web_study_more.loadDataWithBaseURL(null, dataInstruction, "text/html", "UTF-8", null);
                    LogUtil.e("getPromptData=" + dataInstruction);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, "study_more");// study_more
    }
}
