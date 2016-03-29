package com.android.linglan.ui;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
//import com.umeng.message.IUmengRegisterCallback;
//import com.umeng.message.PushAgent;
//import com.umeng.message.UmengRegistrar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

public class StartActivity extends BaseActivity {

    private static String uuid = "";
    public String token;
    public String appkey;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //友盟推送
//        PushAgent mPushAgent = PushAgent.getInstance(this);
//        mPushAgent.enable(new IUmengRegisterCallback() {
//            @Override
//            public void onRegistered(String registrationId) {
//                //onRegistered方法的参数registrationId即是device_token
////                Log.d("device_token", registrationId);
//                LogUtil.e("device_token" + registrationId);
//                LogUtil.e("device_token哈哈哈哈哈");
//            }
//        });

//        String device_token = UmengRegistrar.getRegistrationId(this);
//        LogUtil.e("device_token哈哈哈哈哈?????" +device_token );

//        if (SharedPreferencesUtil.getString("appkey", null) != null) {
//            getAppKey();
//        }

//        String token2 =
//                (SharedPreferencesUtil.getString("token", null) != null) ? // token 的值为null 吗？
//                        SharedPreferencesUtil.getString("token", null) : // 不为空则为token 的值
//                        SharedPreferencesUtil.getString("visittoken", null);// 为空则为 visittoken 的值
//        LogUtil.e("token 的值：" + token2);
//        String visittoken = SharedPreferencesUtil.getString("visittoken", null);
        token = SharedPreferencesUtil.getString("token", null);
        appkey = SharedPreferencesUtil.getString("appkey", null);
        LogUtil.e("token=" + token + ":::appkey=" + appkey);
//        String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        LogUtil.e("android_id=" + m_szAndroidID);
//        LogUtil.e("自生成ID=" + id(this));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferencesUtil.getString("appkey", null) != null) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    getAppKey();
                }
            }
        }, 3000);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            if (token != null) {
//                Intent intent = new Intent(StartActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            } else {
//                Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
//                startActivity(intent);
//                finish();
//            }
//            }
//        }, 3000);
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
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, getDeviceId(this));
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


//    private static String sID = null;
//    private static final String INSTALLATION = "INSTALLATION";
//
//    public synchronized static String id(Context context) {
//        if (sID == null) {
//            File installation = new File(context.getFilesDir(), INSTALLATION);
//            try {
//                if (!installation.exists())
//                    writeInstallationFile(installation);
//                sID = readInstallationFile(installation);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return sID;
//    }
//
//    private static String readInstallationFile(File installation) throws IOException {
//        RandomAccessFile f = new RandomAccessFile(installation, "r");
//        byte[] bytes = new byte[(int) f.length()];
//        f.readFully(bytes);
//        f.close();
//        return new String(bytes);
//    }
//
//    private static void writeInstallationFile(File installation) throws IOException {
//        FileOutputStream out = new FileOutputStream(installation);
//        String id = UUID.randomUUID().toString();
//        out.write(id.getBytes());
//        out.close();
//    }

}
