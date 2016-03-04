package com.android.linglan.ui.homepage;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.linglan.base.AllSubjectBaseActivity;
import com.android.linglan.base.BaseActivity;
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
import com.android.linglan.http.Constants;
import com.android.linglan.widget.UpdateDialog;
import com.umeng.socialize.sso.UMSsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LeeMy on 2016/1/13.
 * 文章详情页
 */
public class ArticleDetailsActivity extends BaseActivity implements View.OnClickListener {
    protected static final int FONTSIZECHANG = 1;
    protected static final int REQUEST_SUCCESS = 2;
    protected static final int REQUEST_FAIL = 3;
    protected static final int REQUEST_LIKE = 4;
    protected static final int REQUEST_COLLECT = 5;

    private Intent intent = null;
    //    private PopupWindowHelper popupWindowHelper;
    private PopupWindow popupWindow;
    private View popView;
    private RelativeLayout rl_author;
    private SeekBar fontseek;
    private TextView textFont;
    private int fontsize = 16; // 字体大小
    private TextView article_title;
    private TextView article_author;
    private TextView article_publishtime;
    private TextView article_publisher;
    private TextView article_details_digest;
    private TextView article_details_content;
    private TextView article_details_copyright;
    private TextView article_write_note;
    private TextView article_write_like;
    private TextView article_write_collect;
    private TextView article_write_share;
    private String articleId;
    private String isLike = "1";
    private String isCollect = "1";
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
                    LogUtil.d("第二次" + data.toString());
                    setData(data);
                    break;
                case REQUEST_FAIL:

                    break;
                case REQUEST_LIKE:
                    if (isLike.equals("0")) {
                        article_write_like.setTextColor(getResources().getColor(R.color.gray_orange));
                        Drawable likeTopDrawable = getResources().getDrawable(R.drawable.article_write_like_ok);
                        likeTopDrawable.setBounds(0, 0, likeTopDrawable.getMinimumWidth(), likeTopDrawable.getMinimumHeight());
                        article_write_like.setCompoundDrawables(null, likeTopDrawable, null, null);
                        isLike = "1";
                    } else {
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
                    } else {
                        article_write_collect.setTextColor(getResources().getColor(R.color.french_grey));
                        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.article_write_collect);
                        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                        article_write_collect.setCompoundDrawables(null, collectTopDrawable, null, null);
                        isCollect = "0";
                    }
                    break;
            }
        }
    };

    private void setData(ArticleDetails.ArticleData data) {
        article_title.setText(data.title);
        article_author.setText("作者：" + data.authornames);
//        article_publishtime.setText(getDate(data.publishtime));
        article_publishtime.setText(data.publishtime);
        article_publisher.setText(data.publishername);
        if (!data.digest.isEmpty()) {
            article_details_digest.setVisibility(View.VISIBLE);
            article_details_digest.setText(data.digest);
        }
        Spanned content = Html.fromHtml(data.content);
        article_details_content.setText(content);
        article_details_copyright.setText(Html.fromHtml(data.copyright));
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_article_details);
//        View layout = View.inflate(mContext, R.layout.pop_text_size, null);
        popView = LayoutInflater.from(this).inflate(R.layout.article_popupview, null);
    }

    @Override
    protected void initView() {
        rl_author = (RelativeLayout) findViewById(R.id.rl_author);
        article_title = (TextView) findViewById(R.id.article_title);
        article_author = (TextView) findViewById(R.id.article_author);
        article_publishtime = (TextView) findViewById(R.id.article_publishtime);
        article_publisher = (TextView) findViewById(R.id.article_publisher);
        article_details_digest = (TextView) findViewById(R.id.article_details_digest);
        article_details_content = (TextView) findViewById(R.id.article_details_content);
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
        articleId = getIntent().getStringExtra("articleId");
        fontsize = SharedPreferencesUtil.getInt(Constants.FONT_SIZE, 16);// 初始化文字大小
        article_details_content.setTextSize(fontsize);
        article_publisher.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        article_publisher.getPaint().setAntiAlias(true);//抗锯齿
//        order.setVisibility(View.GONE);
//        popupWindowHelper = new PopupWindowHelper(popView);
        popupWindow = new PopupWindow(this);

        getDetailsArticle();

        //设置打印友盟log日志
        com.umeng.socialize.utils.Log.LOG = true;
        //关闭友盟toast提示
        ShareUtil.mController.getConfig().closeToast();
        // 配置需要分享的相关平台
        ShareUtil.configPlatforms(ArticleDetailsActivity.this);
        // 设置分享的内容
        ShareUtil.setShareContent(this);
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
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
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
                jumpActivity(ArticleDetailsActivity.this, AuthorDetailsActivity.class);
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
                ShareUtil.umengPlatforms(ArticleDetailsActivity.this);
                break;
            default:
                break;
        }
    }

    private void showExitDialog() {
        exitLoginDialog = new UpdateDialog(this, "确定登录后查看更多精彩内容吗", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferencesUtil.removeValue("token");
//                SharedPreferencesUtil.removeValue("phone");
//                SharedPreferencesUtil.removeValue("username");
//                logout(SHARE_MEDIA.SINA);
//                logout(SHARE_MEDIA.QQ);
//                logout(SHARE_MEDIA.WEIXIN);
//                AuthenticationActivity.show(SettingActivity.this);
                startActivity(new Intent(ArticleDetailsActivity.this, RegisterActivity.class));
                exitLoginDialog.dismiss();
//                finish();
            }
        });
        exitLoginDialog.setTitle("登录后更精彩");
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
                LogUtil.d("getDetailsArticle=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }

                ArticleDetails articleDetails = JsonUtil.json2Bean(result, ArticleDetails.class);
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
                handler.sendEmptyMessage(REQUEST_FAIL);
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

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
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

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
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
