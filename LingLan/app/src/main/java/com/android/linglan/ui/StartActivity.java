package com.android.linglan.ui;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.linglan.Service.MyPushIntentService;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.utils.DeviceUtil;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class StartActivity extends BaseActivity {

    private static String uuid = "";
    public String token;
    public String appkey;
    String deviceId;

    protected static final int REQUEST_VISIT_TOKEN = 0;
    protected static final int REQUEST_SUCCESS = 1;
    protected static final int REQUEST_FAIL = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_VISIT_TOKEN:
                    break;
                case REQUEST_SUCCESS:
                    break;
                case REQUEST_FAIL:
                    break;
            }
        }
    };

    private void initUMeng() {

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                do {
                    deviceId = UmengRegistrar.getRegistrationId(StartActivity.this);
//                    LogUtil.e("device_token哈哈哈哈哈?????" +deviceId );
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    }
                } while (TextUtils.isEmpty(deviceId));
            }
        }, 1000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //友盟推送
//        initUMeng();
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();

//
        mPushAgent.enable(new IUmengRegisterCallback() {
            @Override
            public void onRegistered(String registrationId) {
                //onRegistered方法的参数registrationId即是device_token
//                Log.d("device_token", registrationId);
                LogUtil.e("device_token" + registrationId);//
            }
        });

//        mPushAgent.enable();
        mPushAgent.setPushCheck(false);
        String device_token = UmengRegistrar.getRegistrationId(this);

        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
        token = SharedPreferencesUtil.getString("token", null);
        appkey = SharedPreferencesUtil.getString("appkey", null);
        LogUtil.e("token=" + token + ":::appkey=" + appkey);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferencesUtil.getString("appkey", null) != null) {
                    if (!SharedPreferencesUtil.getBoolean("isGuideShowed", false)) {
                        Intent intent = new Intent(StartActivity.this, GuideImageIndicatorActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                } else {
//                    DeviceUtil.getAppKey(StartActivity.this);
                    getAppKey();
                }
            }
        }, 3000);

    }



    @Override
    protected void setView() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    private void getAppKey() {
        String mode = android.os.Build.MODEL + ",API:" + android.os.Build.VERSION.SDK_INT + ",RELEASE:" + android.os.Build.VERSION.RELEASE;
        NetApi.getAppKey(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
//                {"code":0,"msg":"成功","data":{"appkey":"app唯一值"}}
                LogUtil.e(getPackageName(), "getAppKey=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, StartActivity.this)) {
                    return;
                }

                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject json = new JSONObject(result);
                        SharedPreferencesUtil.saveString("appkey", json.getJSONObject("data").getString("appkey"));
                        LogUtil.e("获取AppKey保存成功" + json.getJSONObject("data").getString("appkey"));
                        LogUtil.e("设备型号：" + android.os.Build.MODEL + "\n设备API:" + android.os.Build.VERSION.SDK + "\n设备RELEASE:" + android.os.Build.VERSION.RELEASE);
                        if (!SharedPreferencesUtil.getBoolean("isGuideShowed", false)) {
//                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                            Intent intent = new Intent(StartActivity.this, GuideImageIndicatorActivity.class);
                            startActivity(intent);
                        } else {
//                        Intent intent = new Intent(StartActivity.this, GuideImageIndicatorActivity.class);
                            Intent intent = new Intent(StartActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, DeviceUtil.getDeviceId(this), mode);
    }

    /**
     * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符
     *
     * 渠道标志为：
     * 1，andriod（a）
     *
     * 识别符来源标志：
     * 1， wifi mac地址（wifi）；
     * 2， IMEI（imei）；
     * 3， 序列号（sn）；
     * 4， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {

        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("android");

        try {

            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if(!TextUtils.isEmpty(wifiMac)){
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                LogUtil.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }

            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if(!TextUtils.isEmpty(imei)){
                deviceId.append("imei");
                deviceId.append(imei);
                LogUtil.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }

            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if(!TextUtils.isEmpty(sn)){
                deviceId.append("sn");
                deviceId.append(sn);
                LogUtil.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }

            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if(!TextUtils.isEmpty(uuid)){
                deviceId.append("id");
                deviceId.append(uuid);
                LogUtil.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID(context));
        }

        LogUtil.e("getDeviceId : ", deviceId.toString());

        return deviceId.toString();

    }

    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context){
        uuid = SharedPreferencesUtil.getString("uuid", "");

        if(TextUtils.isEmpty(uuid)){
            uuid = UUID.randomUUID().toString();
            SharedPreferencesUtil.saveString("uuid", uuid);
        }

        LogUtil.e("getUUID : " + uuid);
        return uuid;
    }

}
