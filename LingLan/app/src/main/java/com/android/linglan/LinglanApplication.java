package com.android.linglan;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.linglan.ui.R;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by LeeMy on 2016/1/6 0006.
 */
public class LinglanApplication extends Application {
    private static Context sApplicationContext;
    private static final String TAG = LinglanApplication.class.getName();
    private PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("passerby-application", "Application created...");
        sApplicationContext = getApplicationContext();
//        String version = null;
        try {
            String version = getVersionName();
            SharedPreferencesUtil.saveString("version", version);
            LogUtil.e("版本号：" + version);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);

        UmengMessageHandler messageHandler = new UmengMessageHandler(){
            /**
             * 参考集成文档的1.6.3
             * http://dev.umeng.com/push/android/integration#1_6_3
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if(isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            /**
             * 参考集成文档的1.6.4
             * http://dev.umeng.com/push/android/integration#1_6_4
             * */
            @Override
            public Notification getNotification(Context context,
                                                UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//                    RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
//                    myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//                    myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//                    myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//                    myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
//                    builder.setContent(myNotificationView)
//                            .setContentTitle(msg.title)
//                            .setSmallIcon(getSmallIconId(context, msg))
//                            .setContentText(msg.text)
//                            .setTicker(msg.ticker)
//                            .setAutoCancel(true);
//                    Notification mNotification = builder.build();
//                    //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
//                    mNotification.contentView = myNotificationView;
//                    return mNotification;
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

    }

    public static Context getsApplicationContext() {
        return sApplicationContext;
    }

    public String getVersionName() throws Exception {
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }
}
