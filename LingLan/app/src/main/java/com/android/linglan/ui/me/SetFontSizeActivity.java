package com.android.linglan.ui.me;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ArticleDetails;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;

/**
 * Created by LeeMy on 2016/3/2 0002.
 * 字体大小设置界面
 */
public class SetFontSizeActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int FONTSIZECHANG = 1;
    protected static final int REQUEST_SUCCESS = 2;
    private int fontsize = 18; // 字体大小
    private int webTextSize = 3; // 初始化字体大小
    private TextView tv_font_size_show;
    private SeekBar fontseek;
    private TextView textFont;
    private ArticleDetails articleDetails;
    private WebView web_article_details_content;
    private WebSettings webSettings;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ArticleDetails.ArticleData data = (ArticleDetails.ArticleData) msg.getData().getSerializable("data");
            switch (msg.what) {
                case FONTSIZECHANG:
                    String textSize = textFont.getText().toString().trim();
                    setWebViewFontSize(textSize);
//                    web_article_details_content.setTextSize(fontsize);
                    break;
                case REQUEST_SUCCESS:

                    web_article_details_content.loadDataWithBaseURL(null, data.content, "text/html", "UTF-8", null);

//                    Spanned content = Html.fromHtml(data.content, null, null);
//                    tv_font_size_show.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
////                    tv_font_size_show.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
//                    tv_font_size_show.setText(content);
//                    LogUtil.d("第二次" + data.toString());
//                    rl_article_details.setVisibility(View.VISIBLE);
//                    ll_no_network.setVisibility(View.GONE);
//                    setData(data);
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_set_font_size);
    }

    @Override
    protected void initView() {
        tv_font_size_show = (TextView) findViewById(R.id.tv_font_size_show);
        fontseek = (SeekBar) findViewById(R.id.settings_font);
        textFont = (TextView) findViewById(R.id.fontSub);
        web_article_details_content = (WebView) findViewById(R.id.web_article_details_content);
    }

    @Override
    protected void initData() {
        setTitle("字体大小", "");
        getDetailsArticle(1776 + "");
        webSettings = web_article_details_content.getSettings();
        fontsize = SharedPreferencesUtil.getInt(Constants.FONT_SIZE, 18);// 初始化文字大小
//        tv_font_size_show.setTextSize(fontsize);
        String textSize = SharedPreferencesUtil.getString("webTextSize", "正常");// 初始化文字大小
        textFont.setText(textSize);
        setWebViewFontSize(textSize);
        initSetFontSeek();
    }

    @Override
    protected void setListener() {

    }

    private void initSetFontSeek() {
        fontseek.setMax(6);
        fontseek.setProgress(webTextSize);
        fontseek.setSecondaryProgress(0);
//        textFont.setText("正常");
        fontseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 0:
                    case 1:
                        textFont.setText("较小");
                        fontsize = 10;
                        break;
                    case 2:
                        textFont.setText("小");
                        fontsize = 14;
                        break;
                    case 3:
                        textFont.setText("正常");
                        fontsize = 18;
                        break;
                    case 4:
                        textFont.setText("大");
                        fontsize = 24;
                        break;
                    case 5:
                    case 6:
                        textFont.setText("较大");
                        fontsize = 30;
                        break;
                }
//                fontsize = progress + 10;
//                textFont.setText("" + fontsize);
                handler.sendEmptyMessage(FONTSIZECHANG);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferencesUtil.saveInt(Constants.FONT_SIZE, fontsize);
                SharedPreferencesUtil.saveString("webTextSize", textFont.getText().toString());
            }
        });
    }

    public void setWebViewFontSize(String textSize) {
        if (textSize.equals("较小")) {
            webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
            webTextSize = 0;
        } else if (textSize.equals("小")) {
            webSettings.setTextSize(WebSettings.TextSize.SMALLER);
            webTextSize = 2;
        } else if (textSize.equals("正常")) {
            webSettings.setTextSize(WebSettings.TextSize.NORMAL);
            webTextSize = 3;
        } else if (textSize.equals("大")) {
            webSettings.setTextSize(WebSettings.TextSize.LARGER);
            webTextSize = 4;
        } else if (textSize.equals("较大")) {
            webSettings.setTextSize(WebSettings.TextSize.LARGEST);
            webTextSize = 6;
        }
    }

    /**
     * 获取文章详情
     */
    private void getDetailsArticle(String articleId) {
        NetApi.getDetailsArticle(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getDetailsArticle=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SetFontSizeActivity.this)) {
                    return;
                }
                articleDetails = JsonUtil.json2Bean(result, ArticleDetails.class);

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", articleDetails.data);
                message.setData(bundle);// bundle传值，耗时，效率低
                handler.sendMessage(message);// 发送message信息
                message.what = REQUEST_SUCCESS;// 标志是哪个线程传数据
            }

            @Override
            public void onFailure(String message) {
                LogUtil.e(message);
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, articleId);
    }

}
