package com.android.linglan.ui.study;

import android.content.Intent;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.ui.R;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;

import java.util.regex.Pattern;

/**
 * Created by LeeMy on 2016/3/15 0015.
 */
public class ArticleUrlDetailsActivity extends BaseActivity {
    private WebView web_article_details_content;
    private WebSettings webSettings;
    private Intent intent = null;
    @Override
    protected void setView() {
        setContentView(R.layout.activity_article_url_details);

    }

    @Override
    protected void initView() {
        web_article_details_content = (WebView) findViewById(R.id.web_article_details_content);

    }

    @Override
    protected void initData() {
        setTitle("文章详情", "");
        intent = new Intent();
        webSettings = web_article_details_content.getSettings();

        webSettings.setJavaScriptEnabled(true);// 支持js
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        webSettings.setUseWideViewPort(false);// 将图片调整到适合webview的大小
        webSettings.setLoadsImagesAutomatically(true);// 支持自动加载图片
        web_article_details_content.requestFocusFromTouch();
        String textSize = SharedPreferencesUtil.getString("webTextSize", "正常");// 初始化文字大小
        if (textSize.equals("较小")) {
            webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
        } else if (textSize.equals("小")) {
            webSettings.setTextSize(WebSettings.TextSize.SMALLER);
        } else if (textSize.equals("正常")) {
            webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        } else if (textSize.equals("大")) {
            webSettings.setTextSize(WebSettings.TextSize.LARGER);
        } else if (textSize.equals("较大")) {
            webSettings.setTextSize(WebSettings.TextSize.LARGEST);
        }
        web_article_details_content.loadUrl(getIntent().getStringExtra("url"));
//        web_article_details_content.loadDataWithBaseURL(null, getIntent().getStringExtra("url"), "text/html", "UTF-8", null);

    }

    @Override
    protected void setListener() {
        web_article_details_content.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.e("这是文章页的超链接=" + url);
//                if (!Pattern.compile("^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$")
                if (!Pattern.compile("^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$")
                        .matcher(url).matches()) {// 此处需要判断URL 超链接是否正确，以及需要跳转到的页面
                    // URL 不正确的情况下
                    ToastUtil.show("地址错误");
                } else {
                    // URL 不正确的情况下
                    web_article_details_content.loadUrl(url);
                }

//                return super.shouldOverrideUrlLoading(view, url);
                return true;
            }
        });
    }

    /**
     * WebView对于Back按键的响应是整个退出，如果希望一级一级退出，则需要监听Back按键
     * 监听按键弹起的状态
     * 返回值 true表示在此拦截，不再向下传递
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LogUtil.d("onKeyUp keyCode: " + keyCode);
        if(keyCode == KeyEvent.KEYCODE_BACK && web_article_details_content.canGoBack()){
            web_article_details_content.goBack();
            return true;
        }
        return super.onKeyUp(keyCode, event);

    }
}
