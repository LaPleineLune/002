package com.android.linglan.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.linglan.base.BaseFragment;
import com.android.linglan.ui.R;
import com.android.linglan.utils.LogUtil;

/**
 * Created by LeeMy on 2016/6/15 0015.
 * 播放界面的文字预览界面
 */
public class RadioPlayTextFragment extends BaseFragment {
    private View rootView;
    private String radioPhoto;
    private String radioContent;
    private WebView web_radio_play_text;
    private WebSettings webSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        radioPhoto = args != null ? args.getString("radioPhoto") : "";
        LogUtil.e("radioPhoto=" + radioPhoto);
        radioContent = args != null ? args.getString("radioContent") : "";
        LogUtil.e("radioContent=" + radioContent);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_radio_play_text, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {
        web_radio_play_text = (WebView) rootView.findViewById(R.id.web_radio_play_text);

    }

    @Override
    protected void initData() {
        webSettings = web_radio_play_text.getSettings();
        web_radio_play_text.setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 闪屏问题
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportMultipleWindows(true);// 新加

        webSettings.setJavaScriptEnabled(true);// 支持js
        webSettings.setDefaultTextEncodingName("utf-8") ;
        webSettings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        webSettings.setUseWideViewPort(false);// 将图片调整到适合webview的大小
        webSettings.setLoadsImagesAutomatically(true);// 支持自动加载图片
        webSettings.setPluginState(WebSettings.PluginState.ON);//支持多媒体
        web_radio_play_text.requestFocusFromTouch();

        web_radio_play_text.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black_7f)); // 设置背景色
        web_radio_play_text.getBackground().setAlpha(230); // 设置填充透明度 范围：0-255(230)
        web_radio_play_text.loadDataWithBaseURL(null, "加载中...", "text/html", "utf-8",null);
//        web_radio_play_text.loadDataWithBaseURL(mGetDetail.data.get("hostsUrl"), mGetDetail.data.get("description"), "text/html", "utf-8",null);
        web_radio_play_text.loadDataWithBaseURL(null, radioContent, "text/html", "UTF-8", null);
        web_radio_play_text.setVisibility(View.VISIBLE); // 加载完之后进行设置显示，以免加载时初始化效果不好看

        web_radio_play_text.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                // 当加载到100%的时候 进度条自动消失
                getActivity().setProgress(progress * 100);

            }

        });

    }

    @Override
    protected void setListener() {

    }
}