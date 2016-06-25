package com.android.linglan.ui.fm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.RadioPlayImageFragment;
import com.android.linglan.fragment.RadioPlayTextFragment;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.RadioPlayInfoBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.me.RegisterActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ShareUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.HintSeekBar;
import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;
import com.baidu.cyberplayer.core.BVideoView.OnTotalCacheUpdateListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by LeeMy on 2016/5/23 0023.
 * 音频播放界面
 */
public class RadioPlayActivity extends BaseActivity implements OnPreparedListener, OnCompletionListener,
        OnErrorListener, OnInfoListener, OnPlayingBufferCacheListener, OnTotalCacheUpdateListener {

    protected static final int REQUEST_COLLECT_INFO = 0;
    protected static final int REQUEST_COLLECT = 1;
    protected static final int REQUEST_SUCCESS = 2;

    private FragmentManager fragmentManager;
    private RadioPlayImageFragment radioPlayImageFragment;
    private RadioPlayTextFragment radioPlayTextFragment;

    private final String TAG = "RadioPlayActivity";

    /**
     * 您的AK
     * 请到http://console.bce.baidu.com/iam/#/iam/accesslist获取
     */
//    private String AK = "f818d332115b4ba0aa6b33d10b9af19d";   //请录入您的AK !!!

    private PopupWindow popupWindow;
    private View popView;

    private LinearLayout ll_controller_root;
    private LinearLayout controller_root1;
    private RelativeLayout controller_root;
//    private LinearLayout controller_play_root;
    private HintSeekBar sb_progress = null;
    private TextView tv_curtime = null;
    private TextView tv_totaltime = null;
    private ImageView img_radio_play_bg = null;
    private ImageView img_radio_play_change = null;
    private ImageView img_play, img_play1;
    private ImageView img_play_previous, img_play_previous1;
    private ImageView img_play_next, img_play_next1;
    private ImageView img_radio_like, img_radio_like1;
    private ImageView img_radio_special, img_radio_special1;
    private TextView tv_weixin, tv_weixin_circle, tv_download, tv_cancel;

    private String mVideoSource = null;
    private String title  = "";
    private String audioid = "";
    private int page;
    private String isCollect = "";

    /**
     * 记录播放位置
     */
    private int mLastPos = 0;

    /**
     * 播放状态
     */
    private  enum PLAYER_STATUS {
        PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
    }

    private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;

    private BVideoView mVV = null;

    private EventHandler mEventHandler;
    private HandlerThread mHandlerThread;

    private final Object SYNC_Playing = new Object();

    private PowerManager.WakeLock mWakeLock = null;// 唤醒锁
    private static final String POWER_LOCK = "VideoViewPlayingActivity";// 电源锁

    private boolean mIsHwDecode = false;

    private final int EVENT_PLAY = 0;
    private final int UI_EVENT_UPDATE_CURRPOSITION = 1;
    private long mTouchTime;
    private boolean barShow = true;
    private RadioPlayInfoBean.RadioPlayInfoData radioPlayInfoData;
    private Intent intent;
    private boolean isLogin = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RadioPlayInfoBean.RadioPlayInfoData data = (RadioPlayInfoBean.RadioPlayInfoData) msg.getData().getSerializable("data");
            switch (msg.what) {
                case REQUEST_COLLECT_INFO:
//                    isCollect = radioSpecialInfoData.iscollect;
                    if (isCollect.equals("0")) {
                        img_radio_like.setBackgroundResource(R.drawable.radio_like);
                        img_radio_like1.setBackgroundResource(R.drawable.radio_like);
                    } else {
                        img_radio_like.setBackgroundResource(R.drawable.radio_like_ok);
                        img_radio_like1.setBackgroundResource(R.drawable.radio_like_ok);
                    }
                    break;
                case REQUEST_COLLECT:
                    if (isCollect.equals("0")) {
                        isCollect = "1";
                        img_radio_like.setBackgroundResource(R.drawable.radio_like_ok);
                        img_radio_like1.setBackgroundResource(R.drawable.radio_like_ok);
                    } else if(isCollect.equals("1")) {
                        isCollect = "0";
                        img_radio_like.setBackgroundResource(R.drawable.radio_like);
                        img_radio_like1.setBackgroundResource(R.drawable.radio_like);
                    }
                    break;
                case REQUEST_SUCCESS:
                    isCollect = data.iscollect;

//                    if (data.photo != null && !data.photo.equals("")) {
//                        Bitmap bitmap = GetLocalOrNetBitmap(data.photo);
////                    img_radio_play_bg.setImageBitmap(bitmap);
//                        Drawable drawable = new BitmapDrawable(bitmap);
//                        img_radio_play_bg.setBackground(drawable);
//                    } else {
//                        img_radio_play_bg.setBackgroundResource(R.drawable.radio_play_bg);
//                    }

                    try {
                        ImageUtil.loadImageAsync(img_radio_play_bg, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, R.drawable.radio_play_bg, data.photo, null);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    LogUtil.e("我要看的图片URL=" + data.photo);
                    if (isCollect.equals("0")) {
                        img_radio_like.setBackgroundResource(R.drawable.radio_like);
                        img_radio_like1.setBackgroundResource(R.drawable.radio_like);
                    } else {
                        img_radio_like.setBackgroundResource(R.drawable.radio_like_ok);
                        img_radio_like1.setBackgroundResource(R.drawable.radio_like_ok);
                    }
                    // 设置分享的内容String articleORsubject, String id, String shareTitle, String imgUrl, String shareContent
                    ShareUtil.setShareContent(RadioPlayActivity.this, Constants.RADIO_SINGLE, audioid, title, data.logo, data.description);// data.photo
                    break;
            }
        }
    };

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        LogUtil.e(TAG, "onPause");
        /**
         * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
         */
        mLastPos = mVV.getCurrentPosition();
        mVV.stopPlayback();
        /*if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
            mLastPos = mVV.getCurrentPosition();
            mVV.stopPlayback();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e(TAG, "onResume");
        if (null != mWakeLock && (!mWakeLock.isHeld())) {
            mWakeLock.acquire();
        }
        /**
         * 发起一次播放任务,当然您不一定要在这发起
         */
//        mEventHandler.sendEmptyMessage(EVENT_PLAY);
        if (isLogin) {
            getRadioPlayInfo();
            isLogin = false;
        }
    }

    @Override
    protected void onDestroy(){
        LogUtil.e(TAG, "onDestroy");
        super.onDestroy();
        /**
         * 退出后台事件处理线程
         */
//        mHandlerThread.quit();
    }

    class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_PLAY:
                    /**
                     * 如果已经播放了，等待上一次播放结束
                     */
                    if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
                        synchronized (SYNC_Playing) {
                            try {
                                SYNC_Playing.wait();
                                LogUtil.e(TAG, "wait player status to idle");
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }

                    /**
                     * 设置播放url
                     */
                    mVV.setVideoPath(mVideoSource);

                    /**
                     * 续播，如果需要如此
                     */
                    if (mLastPos > 0) {

                        mVV.seekTo(mLastPos);
                        mLastPos = 0;
                    }

                    /**
                     * 显示或者隐藏缓冲提示
                     */
                    mVV.showCacheInfo(true);

                    /**
                     * 开始播放
                     */
                    mVV.start();

                    mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
                    break;
                default:
                    break;
            }
        }
    }

    Handler mUIHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /**
                 * 更新进度及时间
                 */
                case UI_EVENT_UPDATE_CURRPOSITION:
                    int currPosition = mVV.getCurrentPosition();
                    int duration = mVV.getDuration();
                    updateTextViewWithTimeFormat(tv_curtime, currPosition);
                    updateTextViewWithTimeFormat(tv_totaltime, duration);
                    sb_progress.setMax(duration);
                    if (mVV.isPlaying()){
                        sb_progress.setProgress(currPosition);
                    }
                    mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 200);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_radio_play);
        popView = LayoutInflater.from(this).inflate(R.layout.popupview_radio_play_more, null);
    }

    @Override
    protected void initView() {
        ll_controller_root = (LinearLayout) findViewById(R.id.ll_controller_root);
        controller_root1 = (LinearLayout) findViewById(R.id.controller_root1);
        controller_root = (RelativeLayout) findViewById(R.id.controller_root);
        sb_progress = (HintSeekBar) findViewById(R.id.sb_progress);
        tv_curtime = (TextView)findViewById(R.id.tv_curtime);
        tv_totaltime = (TextView)findViewById(R.id.tv_totaltime);
        img_radio_play_bg = (ImageView) findViewById(R.id.img_radio_play_bg);
        img_play_previous = (ImageView) findViewById(R.id.img_play_previous);
        img_play_previous1 = (ImageView) findViewById(R.id.img_play_previous1);
        img_play = (ImageView) findViewById(R.id.img_play);
        img_play1 = (ImageView) findViewById(R.id.img_play1);
        img_play_next = (ImageView) findViewById(R.id.img_play_next);
        img_play_next1 = (ImageView) findViewById(R.id.img_play_next1);
        img_radio_play_change = (ImageView) findViewById(R.id.img_radio_play_change);
        img_radio_like = (ImageView) findViewById(R.id.img_radio_like);
        img_radio_like1 = (ImageView) findViewById(R.id.img_radio_like1);
        img_radio_special = (ImageView) findViewById(R.id.img_radio_special);
        img_radio_special1 = (ImageView) findViewById(R.id.img_radio_special1);

        tv_weixin = (TextView) popView.findViewById(R.id.tv_weixin);
        tv_weixin_circle = (TextView) popView.findViewById(R.id.tv_weixin_circle);
        tv_download = (TextView) popView.findViewById(R.id.tv_download);
        tv_cancel = (TextView) popView.findViewById(R.id.tv_cancel);

        /**
         *获取BVideoView对象
         */
        mVV = (BVideoView) findViewById(R.id.video_view);
    }

    @Override
    protected void initData() {
        title = getIntent().getStringExtra("title");
        setTitle(title != null && !title.equals("") ? title : "音频详情", "");
        Drawable collectTopDrawable = ContextCompat.getDrawable(this, R.drawable.radio_more);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
        audioid = getIntent().getStringExtra("audioid");

        popupWindow = new PopupWindow(this);

        fragmentManager = getSupportFragmentManager();

        // 第一次启动时选中第0个tab
//        setTabSelection(0);

        /**
         * 设置解码模式
         */
        mVV.setDecodeMode(mIsHwDecode ? BVideoView.DECODE_HW : BVideoView.DECODE_SW);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, POWER_LOCK);

        mIsHwDecode = getIntent().getBooleanExtra("isHW", false);
//        Uri uriPath = getIntent().getData();
//        if (null != uriPath) {
//            String scheme = uriPath.getScheme();
//            if (null != scheme) {
//                mVideoSource = uriPath.toString();
//            } else {
//                mVideoSource = uriPath.getPath();
//            }
//        }

        /**
         * 设置ak
         */
        BVideoView.setAK(Constants.AK);

        /**
         * 开启后台事件处理线程
         */
        mHandlerThread = new HandlerThread("event handler thread",
                Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mEventHandler = new EventHandler(mHandlerThread.getLooper());

        radioPlayInfoData = new RadioPlayInfoBean().data;
//        page = 1;
        getRadioPlayInfo();
//        getRadioPlayList(page);

        //设置打印友盟log日志
        com.umeng.socialize.utils.Log.LOG = true;
        //关闭友盟toast提示
        ShareUtil.mController.getConfig().closeToast();
        // 配置需要分享的相关平台
        ShareUtil.configPlatforms(RadioPlayActivity.this);
//        // 设置分享的内容String articleORsubject, String id, String shareTitle, String imgUrl, String shareContent
//        ShareUtil.setShareContent(this, Constants.RADIO_SINGLE, audioid, title, null, "");

    }

    @Override
    protected void setListener() {

        /**
         * 注册listener
         */
        mVV.setOnPreparedListener(this);
        mVV.setOnCompletionListener(this);
        mVV.setOnErrorListener(this);
        mVV.setOnInfoListener(this);
        mVV.setOnTotalCacheUpdateListener(this);

        right.setOnClickListener(this);
        img_play.setOnClickListener(this);
        img_play1.setOnClickListener(this);
        img_play_previous.setOnClickListener(this);
        img_play_previous1.setOnClickListener(this);
        img_play_next.setOnClickListener(this);
        img_play_next1.setOnClickListener(this);
        img_radio_special.setOnClickListener(this);
        img_radio_special1.setOnClickListener(this);
        img_radio_play_change.setOnClickListener(this);
        img_radio_like.setOnClickListener(this);
        img_radio_like1.setOnClickListener(this);

        tv_weixin.setOnClickListener(this);
        tv_weixin_circle.setOnClickListener(this);
        tv_download.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        SeekBar.OnSeekBarChangeListener osbc1 = new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                //Log.v(TAG, "progress: " + progress);
                updateTextViewWithTimeFormat(tv_curtime, progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                /**
                 * SeekBar开始seek时停止更新
                 */
                mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                int iseekPos = seekBar.getProgress();
                /**
                 * SeekBark完成seek时执行seekTo操作并更新界面
                 *
                 */
                mVV.seekTo(iseekPos);
                LogUtil.e(TAG, "seek to " + iseekPos);
                mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
            }
        };
        sb_progress.setOnSeekBarChangeListener(osbc1);
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        LogUtil.e(TAG, "onCreate");
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
        wl.acquire();
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right:
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setWidth(getWindowManager().getDefaultDisplay().getWidth());
                popupWindow.setHeight(getWindowManager().getDefaultDisplay().getHeight() * 2 / 10);
                popupWindow.setAnimationStyle(R.style.AnimationPreview);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);// 响应回退按钮事件
                popupWindow.setContentView(popView);

                int[] location = new int[2];
                v.getLocationOnScreen(location);
                popupWindow.showAtLocation(v, Gravity.BOTTOM, location[0], location[1] - popupWindow.getHeight());

                break;
            case R.id.img_play:
            case R.id.img_play1:
                if (mVV.isPlaying()) {
                    img_play.setImageResource(R.drawable.radio_play_go);
                    img_play1.setImageResource(R.drawable.radio_play_go);
                    /**
                     * 暂停播放
                     */
                    mVV.pause();
                } else {
                    img_play.setImageResource(R.drawable.radio_play_stop);
                    img_play1.setImageResource(R.drawable.radio_play_stop);
                    /**
                     * 继续播放
                     */
                    mVV.resume();
                }
                break;
            case R.id.img_play_previous:// 实现切换示例
            case R.id.img_play_previous1:// 实现切换示例
                /**
                 * 如果已经播放了，等待上一次播放结束
                 */
                if(mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE){
                    mVV.stopPlayback();
                }

                /**
                 * 发起一次新的播放任务
                 */
                if(mEventHandler.hasMessages(EVENT_PLAY))
                    mEventHandler.removeMessages(EVENT_PLAY);
                mEventHandler.sendEmptyMessage(EVENT_PLAY);
                break;
            case R.id.img_play_next:
            case R.id.img_play_next1:
                break;
            case R.id.img_radio_play_change:
                if (controller_root.getVisibility() == View.VISIBLE && controller_root1.getVisibility() == View.GONE) {
                    setTabSelection(1);
                    controller_root.setVisibility(View.GONE);
                    controller_root1.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams lp = ll_controller_root.getLayoutParams();
                    lp.height = getResources().getDimensionPixelSize(R.dimen.dp97);
                    ll_controller_root.setBackgroundResource(R.drawable.radio_play_text_bg);
                    img_radio_play_change.setImageResource(R.drawable.radio_play_change_image);
                } else if (controller_root.getVisibility() == View.GONE && controller_root1.getVisibility() == View.VISIBLE) {
                    setTabSelection(0);
                    controller_root.setVisibility(View.VISIBLE);
                    controller_root1.setVisibility(View.GONE);
                    ViewGroup.LayoutParams lp = ll_controller_root.getLayoutParams();
                    lp.height = getResources().getDimensionPixelSize(R.dimen.dp171);
                    ll_controller_root.setBackgroundResource(R.drawable.radio_play_image_bg);
                    img_radio_play_change.setImageResource(R.drawable.radio_play_change_text);
                }
                break;
            case R.id.img_radio_like:
            case R.id.img_radio_like1:
//                ToastUtil.show("我是收藏");
                if (NetApi.getToken() != null) {
                    getRadioPlayCollect();
                } else {
                    intent.setClass(this, RegisterActivity.class);
                    startActivity(intent);
                    isLogin = true;
                }
                break;
            case R.id.img_radio_special:
            case R.id.img_radio_special1:
                int version = Integer.valueOf(Build.VERSION.SDK_INT);
                intent.setClass(this, RadioPlaySpecialListActivity.class);
                intent.putExtra("audioid", audioid);
                intent.putExtra("albumid", radioPlayInfoData.albumid);
                startActivity(intent);
                if(version > 5 ){
                    overridePendingTransition(R.anim.alpha_in, 0);
                }
                break;
            case R.id.tv_weixin:
                popupWindow.dismiss();
                ShareUtil.performShare(this, SHARE_MEDIA.WEIXIN);
//                ToastUtil.show("我是微信好友");
                break;
            case R.id.tv_weixin_circle:
                popupWindow.dismiss();
                ShareUtil.performShare(this, SHARE_MEDIA.WEIXIN_CIRCLE);
//                ToastUtil.show("我是朋友圈");
                break;
            case R.id.tv_download:
                popupWindow.dismiss();
                ToastUtil.show("我是下载");
                break;
            case R.id.tv_cancel:
                popupWindow.dismiss();
//                ToastUtil.show("我是取消");
                break;
        }
    }

    /**
     * 准备播放就绪
     * OnPreparedListener
     */
    @Override
    public void onPrepared() {
        // TODO Auto-generated method stub
        LogUtil.e(TAG, "onPrepared");
        mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
        mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
    }

    /**
     * 播放完成
     * OnCompletionListener
     */
    @Override
    public void onCompletion() {
        // TODO Auto-generated method stub
        LogUtil.e(TAG, "onCompletion");
        synchronized (SYNC_Playing) {
            SYNC_Playing.notify();
        }
        mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
        mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
    }

    /**
     * 播放出错
     * OnErrorListener
     */
    @Override
    public boolean onError(int what, int extra) {
        // TODO Auto-generated method stub
        LogUtil.e(TAG, "onError");
        synchronized (SYNC_Playing) {
            SYNC_Playing.notify();
        }
        mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
        mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
        return true;
    }

    // OnInfoListener
    @Override
    public boolean onInfo(int what, int extra) {
        switch(what){
            /**
             * 开始缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_START:
                break;
            /**
             * 结束缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_END:
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 当前缓冲的百分比， 可以配合onInfo中的开始缓冲和结束缓冲来显示百分比到界面
     * OnPlayingBufferCacheListener
     */
    @Override
    public void onPlayingBufferCache(int i) {

    }

    // OnTotalCacheUpdateListener
    @Override
    public void onTotalCacheUpdate(long pos) {
        LogUtil.e(TAG, "Totally cached "+ pos);
    }

    private void updateTextViewWithTimeFormat(TextView view, int second){
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String strTemp = null;
        if (0 != hh) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%02d:%02d", mm, ss);
        }
        view.setText(strTemp);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (radioPlayImageFragment != null) {
            transaction.hide(radioPlayImageFragment);
        }
        if (radioPlayTextFragment != null) {
            transaction.hide(radioPlayTextFragment);
        }
    }

    private void setTabSelection(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.setCustomAnimations(R.anim.alpha_in, R.anim.alpha_out, R.anim.alpha_in, R.anim.alpha_out);
        Bundle data = new Bundle();
        switch (index) {
            case 0:
                img_radio_play_change.setImageResource(R.drawable.radio_play_change_text);
                if (radioPlayImageFragment == null) {
                    radioPlayImageFragment = new RadioPlayImageFragment();
//                    if (radioPlayInfoData != null)
//                    data.putString("radioPhoto", radioPlayInfoData.photo);
//                    radioPlayImageFragment.setArguments(data);
                    transaction.add(R.id.radio_play_content, radioPlayImageFragment,"tag1");
                } else {
                    transaction.show(radioPlayImageFragment);
                }
                break;
            case 1:
                img_radio_play_change.setImageResource(R.drawable.radio_play_change_image);
                if (radioPlayTextFragment == null) {
                    radioPlayTextFragment = new RadioPlayTextFragment();
                    if (radioPlayInfoData != null) {
//                        data.putString("radioPhoto", radioPlayInfoData.photo);
                        data.putString("radioContent", radioPlayInfoData.content);
                    }
                    radioPlayTextFragment.setArguments(data);
                    transaction.add(R.id.radio_play_content, radioPlayTextFragment,"tag2");
                } else {
                    transaction.show(radioPlayTextFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 音频播放详情(头部)
     */
    private void getRadioPlayInfo() {
        NetApi.getRadioPlayInfo(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getRadioPlayInfo=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, RadioPlayActivity.this)) {
                    return;
                }
                RadioPlayInfoBean radioPlayInfoBean = JsonUtil.json2Bean(result, RadioPlayInfoBean.class);
                radioPlayInfoData = radioPlayInfoBean.data;
                mVideoSource = radioPlayInfoData.url;
                isCollect = radioPlayInfoData.iscollect;

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", radioPlayInfoBean.data);
                message.setData(bundle);// bundle传值，耗时，效率低
                handler.sendMessage(message);// 发送message信息
                message.what = REQUEST_SUCCESS;// 标志是哪个线程传数据

                mEventHandler.sendEmptyMessage(EVENT_PLAY);
                setTabSelection(0);
            }

            @Override
            public void onFailure(String message) {

            }
        }, audioid);
    }

    private void getRadioPlayCollect() {
        NetApi.getRadioPlayCollect(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getRadioPlayCollect=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, RadioPlayActivity.this)) {
                    return;
                }

                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("code").equals("0")) {
                        handler.sendEmptyMessage(REQUEST_COLLECT);
                    }
                    String msg = json.getString("msg");
                    ToastUtil.show(msg);
                    LogUtil.e("音频点赞" + msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, audioid, isCollect);
    }

    public static Bitmap GetLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try
        {
            in = new BufferedInputStream(new URL(url).openStream(), 2*1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 2*1024);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[2*1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
}
