package com.android.linglan.ui.homepage;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ArticleDetails;
import com.android.linglan.ui.R;
import com.android.linglan.ui.me.NoteWritePreviewActivity;
import com.android.linglan.ui.me.RegisterActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ShareUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.UpdateDialog;
import com.umeng.socialize.sso.UMSsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by LeeMy on 2016/1/13.
 * 文章详情页
 */
public class ArticleDetailsActivity extends BaseActivity implements View.OnClickListener {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int FONTSIZECHANG = 1;
    protected static final int REQUEST_SUCCESS = 2;
    protected static final int REQUEST_LIKE = 3;
    protected static final int REQUEST_COLLECT = 4;

    private Intent intent = null;
    //    private PopupWindowHelper popupWindowHelper;
    private PopupWindow popupWindow;
    private View popView;
    private LinearLayout ll_no_network;
    private RelativeLayout rl_author, rl_article_details;
    private SeekBar fontseek;
    private TextView textFont;
    private int fontsize = 18; // 字体大小
    private TextView article_title;
    private TextView article_author;
    private TextView article_publishtime;
    private TextView article_publisher;
    private TextView article_details_digest;
    private TextView article_details_content;
    private WebView web_article_details_content;
    private WebSettings webSettings;
    private TextView article_details_copyright;
    private TextView article_write_note;
    private TextView article_write_like;
    private TextView article_write_collect;
    private TextView article_write_share;
    private String articleId;
    private String photo;// 小图
    private String isLike = "1";// 原：1
    private String isCollect = "1";// 原：1
    private String isShare = "1";// 原：1
    private ArticleDetails articleDetails;
    private UpdateDialog exitLoginDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ArticleDetails.ArticleData data = (ArticleDetails.ArticleData) msg.getData().getSerializable("data");
            switch (msg.what) {
                case FONTSIZECHANG:
                    article_details_content.setTextSize(fontsize);
                    break;
                case REQUEST_SUCCESS:
//                    LogUtil.d("第二次" + data.toString());
                    rl_article_details.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    setData(data);
                    break;
                case REQUEST_LIKE:
                    if (isLike.equals("0")) {
                        article_write_like.setTextColor(getResources().getColor(R.color.gray_orange));
                        Drawable likeTopDrawable = getResources().getDrawable(R.drawable.article_write_like_ok);
                        likeTopDrawable.setBounds(0, 0, likeTopDrawable.getMinimumWidth(), likeTopDrawable.getMinimumHeight());
                        article_write_like.setCompoundDrawables(null, likeTopDrawable, null, null);
                        isLike = "1";
                    } else if (isLike.equals("1")) {
                        article_write_like.setTextColor(getResources().getColor(R.color.french_grey));
                        Drawable likeTopDrawable = getResources().getDrawable(R.drawable.article_write_like);
                        likeTopDrawable.setBounds(0, 0, likeTopDrawable.getMinimumWidth(), likeTopDrawable.getMinimumHeight());
                        article_write_like.setCompoundDrawables(null, likeTopDrawable, null, null);
                        isLike = "0";
                    }
                    break;
                case REQUEST_COLLECT:
                    if (isCollect.equals("0")) {
                        article_write_collect.setTextColor(getResources().getColor(R.color.gray_orange));
                        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.article_write_collect_ok);
                        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                        article_write_collect.setCompoundDrawables(null, collectTopDrawable, null, null);
                        isCollect = "1";
                    } else if (isCollect.equals("1")) {
                        article_write_collect.setTextColor(getResources().getColor(R.color.french_grey));
                        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.article_write_collect);
                        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                        article_write_collect.setCompoundDrawables(null, collectTopDrawable, null, null);
                        isCollect = "0";
                    }
                    break;
                case REQUEST_FAILURE:
                    rl_article_details.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private void setData(ArticleDetails.ArticleData data) {
        article_title.setText(data.title);
        if (!TextUtils.isEmpty(data.authornames)) {
            article_author.setText("作者：" + data.authornames);
        } else {
            article_author.setVisibility(View.GONE);
        }
//        article_publishtime.setText(getDate(data.publishtime));
        article_publishtime.setText(data.publishtime);
        if (!TextUtils.isEmpty(data.publishername)) {
            article_publisher.setText(data.publishername);
        } else {
            article_publisher.setText("中医书友会");
        }
//        if (!data.digest.isEmpty()) {// 摘要
//            article_details_digest.setVisibility(View.VISIBLE);
//            article_details_digest.setText(data.digest);
//        }

        Spanned content = Html.fromHtml(data.content, imgGetter, null);
        article_details_content.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
        article_details_content.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
        article_details_content.setText(content);

        web_article_details_content.loadDataWithBaseURL(null, data.content, "text/html", "UTF-8", null);

//        article_details_copyright.setText(Html.fromHtml(data.copyright, imgGetter, null));// 版权声明

        // 设置分享的内容String articleORsubject, String id, String shareTitle, String imgUrl, String shareContent
        ShareUtil.setShareContent(this, Constants.ARTICLE, articleId, data.title, photo, data.digest);
    }

    private Html.ImageGetter imgGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {
            LogUtil.i("RG", "source---?>>>" + source);
            Drawable drawable = null;
            URL url;
            try {
                url = new URL(source);
                LogUtil.i("RG", "url---?>>>" + url);
                drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            LogUtil.i("RG", "url---?>>>" + url);
            return drawable;
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        articleId = getIntent().getStringExtra("articleId");
        photo = getIntent().getStringExtra("photo");
        if (isShare.equals("1")) {
            getDetailsArticle();
        }
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_article_details);
//        View layout = View.inflate(mContext, R.layout.pop_text_size, null);
        popView = LayoutInflater.from(this).inflate(R.layout.article_popupview, null);
    }

    @Override
    protected void initView() {
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        rl_article_details = (RelativeLayout) findViewById(R.id.rl_article_details);
        rl_author = (RelativeLayout) findViewById(R.id.rl_author);
        article_title = (TextView) findViewById(R.id.article_title);
        article_author = (TextView) findViewById(R.id.article_author);
        article_publishtime = (TextView) findViewById(R.id.article_publishtime);
        article_publisher = (TextView) findViewById(R.id.article_publisher);
        article_details_digest = (TextView) findViewById(R.id.article_details_digest);
        article_details_content = (TextView) findViewById(R.id.article_details_content);
        web_article_details_content = (WebView) findViewById(R.id.web_article_details_content);
        article_details_copyright = (TextView) findViewById(R.id.article_details_copyright);
        article_write_note = (TextView) findViewById(R.id.article_write_note);
        article_write_like = (TextView) findViewById(R.id.article_write_like);
        article_write_collect = (TextView) findViewById(R.id.article_write_collect);
        article_write_share = (TextView) findViewById(R.id.article_write_share);
        fontseek = (SeekBar) popView.findViewById(R.id.settings_font);
        textFont = (TextView) popView.findViewById(R.id.fontSub);
    }

    @Override
    protected void initData() {
        setTitle("文章详情", "");
        intent = new Intent();

//        right.setImageResource(R.drawable.font_size);
//        articleId = getIntent().getStringExtra("articleId");
//        getDetailsArticle();
        webSettings = web_article_details_content.getSettings();

//        setJavaScriptEnabled(true);  //支持js
//        setPluginsEnabled(true);  //支持插件
//        setUseWideViewPort(false);  //将图片调整到适合webview的大小
//        setSupportZoom(true);  //支持缩放
//        setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
//        supportMultipleWindows();  //多窗口
//        setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
//        setAllowFileAccess(true);  //设置可以访问文件
//        setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
//        webview webSettings.setBuiltInZoomControls(true); //设置支持缩放
//        setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//        setLoadsImagesAutomatically(true);  //支持自动加载图片
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);//排版适应屏幕



//        webSettings.setPluginsEnabled(true);
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
        web_article_details_content.requestFocusFromTouch();

        web_article_details_content.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                // 当加载到100%的时候 进度条自动消失
                ArticleDetailsActivity.this.setProgress(progress * 100);

            }

        });

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


        fontsize = SharedPreferencesUtil.getInt(Constants.FONT_SIZE, 18);// 初始化文字大小
        article_details_content.setTextSize(fontsize);
//        article_publisher.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
//        article_publisher.getPaint().setAntiAlias(true);//抗锯齿

//        order.setVisibility(View.GONE);
//        popupWindowHelper = new PopupWindowHelper(popView);
        popupWindow = new PopupWindow(this);


        //设置打印友盟log日志
        com.umeng.socialize.utils.Log.LOG = true;
        //关闭友盟toast提示
        ShareUtil.mController.getConfig().closeToast();
        // 配置需要分享的相关平台
        ShareUtil.configPlatforms(ArticleDetailsActivity.this);
//        // 设置分享的内容String articleORsubject, String id, String shareTitle, String imgUrl, String shareContent
//        ShareUtil.setShareContent(this, Constants.ARTICLE, articleId, data.title, null, "");
    }

    @Override
    protected void setListener() {
//        right.setOnClickListener(this);
        rl_author.setOnClickListener(this);
        article_publisher.setOnClickListener(this);
        article_write_note.setOnClickListener(this);
        article_write_like.setOnClickListener(this);
        article_write_collect.setOnClickListener(this);
        article_write_share.setOnClickListener(this);

        ll_no_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
            }
        });

//        web_article_details_content.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                ToastUtil.show("哈哈哈哈");
//                return false;
//            }
//        });

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
                    // URL 正确的情况下
                    String type = Uri.parse(url).getQueryParameter("type");
//                    String type = "0";
                    if (!TextUtils.isEmpty(type) && Constants.ARTICLE.equals(type)) {// 这是文章
                        intent.setClass(ArticleDetailsActivity.this, ArticleDetailsActivity.class);
                        intent.putExtra("articleId", Uri.parse(url).getQueryParameter("articleId"));
//                        intent.putExtra("articleId", "1904");
//                        intent.putExtra("photo", "http://img.zhongyishuyou.com/Public/data/article/logo/_2016030915444588699.jpg");
                        startActivity(intent);
                    } else if (!TextUtils.isEmpty(type) && Constants.SUBJECT.equals(type)) {// 这是专题
                        intent.setClass(ArticleDetailsActivity.this, SubjectDetailsActivity.class);
                        intent.putExtra("specialid", Uri.parse(url).getQueryParameter("specialid"));
//                        intent.putExtra("specialid", "15");
//                        intent.putExtra("photo", "http://img.zhongyishuyou.com/Public/data/special/logo/201603102330066.png");
//                        intent.putExtra("logo", "http://img.zhongyishuyou.com/Public/data/special/logo/2016031022112547.png");
//                        intent.putExtra("description", "赵绍琴（1918-2001年），北京市人，三代御医之后。赵氏幼承家学，后又拜师于太医院御医韩一斋、瞿文楼和北京四大名医之一汪逢春，尽得三家真传。1934年，悬壶北京。1956年，到北京中医学院任教，曾任北京中医学院温病教研室主任，中国中医药学会内科学会顾问。本专题内涉及赵老从理论到临床的学术成果。");
                        startActivity(intent);
                    }
//                    intent.setClass(ArticleDetailsActivity.this, ArticleUrlDetailsActivity.class);
//                    intent.putExtra("url", url);
//                    startActivity(intent);
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
        if (keyCode == KeyEvent.KEYCODE_BACK && web_article_details_content.canGoBack()) {
            web_article_details_content.goBack();
            return true;
        }
        return super.onKeyUp(keyCode, event);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        isShare = "1";
        switch (v.getId()) {
            case R.id.right:
//                ToastUtil.show("我是右边");
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setWidth(getWindowManager().getDefaultDisplay().getWidth());
                popupWindow.setHeight(getWindowManager().getDefaultDisplay().getHeight() / 6);
                popupWindow.setAnimationStyle(R.style.AnimationPreview);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);// 响应回退按钮事件
                popupWindow.setContentView(popView);

                int[] location = new int[2];
                v.getLocationOnScreen(location);
                popupWindow.showAtLocation(v, Gravity.BOTTOM, location[0], location[1] - popupWindow.getHeight());


                fontseek.setMax(20);
                fontseek.setProgress(fontsize - 10);
                fontseek.setSecondaryProgress(0);
                textFont.setText(fontsize + "");
                fontseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        fontsize = progress + 10;
                        textFont.setText("" + fontsize);
                        handler.sendEmptyMessage(FONTSIZECHANG);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        SharedPreferencesUtil.saveInt(Constants.FONT_SIZE, fontsize);
                    }
                });

                break;
            case R.id.rl_author:
            case R.id.article_publisher:
//                jumpActivity(ArticleDetailsActivity.this, AuthorDetailsActivity.class);
                break;
            case R.id.article_write_note:
                intent.putExtra("note", "write");
                intent.setClass(ArticleDetailsActivity.this, NoteWritePreviewActivity.class);
                startActivity(intent);
                break;
            case R.id.article_write_like:
//                ToastUtil.show("这是点赞");
                if (SharedPreferencesUtil.getString("token", null) != null) {
                    getDetailsArticleLike();
                } else {
                    showExitDialog();
                }
                break;
            case R.id.article_write_collect:
//                ToastUtil.show("这是收藏");
                if (SharedPreferencesUtil.getString("token", null) != null) {
                    getDetailsArticleCollect();
                } else {
                    showExitDialog();
                }
                break;
            case R.id.article_write_share:
//                ToastUtil.show("这是分享");
                isShare = "0";
                ShareUtil.umengPlatforms(ArticleDetailsActivity.this);
                break;
            default:
                break;
        }
    }

    private void showExitDialog() {
        exitLoginDialog = new UpdateDialog(this, "登录后可体验更多功能", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ArticleDetailsActivity.this, RegisterActivity.class));
                exitLoginDialog.dismiss();
            }
        });
        exitLoginDialog.setTitle("提示");
        exitLoginDialog.setCancelText("我先看看");
        exitLoginDialog.setEnterText("马上登录");
        exitLoginDialog.show();
    }

    /**
     * 如果有使用任一平台的SSO授权, 则必须在对应的activity中实现onActivityResult方法, 并添加如下代码
     */

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 根据requestCode获取对应的SsoHandler
        UMSsoHandler ssoHandler = ShareUtil.mController.getConfig().getSsoHandler(resultCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 获取文章详情
     */
    private void getDetailsArticle() {
        NetApi.getDetailsArticle(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getDetailsArticle=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ArticleDetailsActivity.this)) {
                    return;
                }
                articleDetails = JsonUtil.json2Bean(result, ArticleDetails.class);

                isLike = articleDetails.favouriscancel;
                handler.sendEmptyMessage(REQUEST_LIKE);
                isCollect = articleDetails.collectiscancel;
                handler.sendEmptyMessage(REQUEST_COLLECT);

//                if (articleDetails.favouriscancel.equals("0")) {
//                    handler.sendEmptyMessage(REQUEST_LIKE);
//                }
//                if (articleDetails.collectiscancel.equals("0")) {
//                    handler.sendEmptyMessage(REQUEST_COLLECT);
//                }
                LogUtil.d("第一次" + articleDetails.data.toString());

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

    /**
     * 文章点赞
     */
    private void getDetailsArticleLike() {
        NetApi.getDetailsArticleLike(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("文章点赞" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ArticleDetailsActivity.this)) {
                    return;
                }

                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("code").equals("0")) {
                        handler.sendEmptyMessage(REQUEST_LIKE);
                    }
                    String msg = json.getString("msg");
                    ToastUtil.show(msg);
                    LogUtil.e("文章点赞" + msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
//                ToastUtil.show(message);
                LogUtil.e("文章点赞错误" + message);

            }
        }, articleId, isLike);

    }

    /**
     * 文章收藏
     */
    private void getDetailsArticleCollect() {
        NetApi.getDetailsArticleCollect(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("文章收藏" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ArticleDetailsActivity.this)) {
                    return;
                }

                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("code").equals("0")) {
                        handler.sendEmptyMessage(REQUEST_COLLECT);
//                        article_write_collect.setTextColor(getResources().getColor(R.color.gray_orange));
//                        article_write_collect.setCompoundDrawables(null, getResources().getDrawable(R.drawable.article_write_collect_ok), null, null);
                    }
                    String msg = json.getString("msg");
                    ToastUtil.show(msg);
                    LogUtil.e("文章收藏" + msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
//                ToastUtil.show(message);
                LogUtil.e("文章收藏错误" + message);

            }
        }, articleId, isCollect);

    }


}
