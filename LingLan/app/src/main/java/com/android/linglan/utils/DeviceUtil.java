package com.android.linglan.utils;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.MainActivity;
import com.android.linglan.ui.StartActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by LeeMy on 2016/3/30 0030.
 */
public class DeviceUtil {

    private static String uuid = "";

    public static  void getAppKey(final Context context) {
        String mode = android.os.Build.MODEL + ",API:" + android.os.Build.VERSION.SDK + ",RELEASE:" + android.os.Build.VERSION.RELEASE;
        NetApi.getAppKey(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
//                {"code":0,"msg":"成功","data":{"appkey":"app唯一值"}}
                LogUtil.e(context.getPackageName(), "getAppKey=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, context)) {
                    return;
                }

                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject json = new JSONObject(result);
                        SharedPreferencesUtil.saveString("appkey", json.getJSONObject("data").getString("appkey"));
                        LogUtil.e("获取AppKey保存成功" + json.getJSONObject("data").getString("appkey"));
                        LogUtil.e("设备型号：" + android.os.Build.MODEL + "\n设备API:" + android.os.Build.VERSION.SDK_INT + "\n设备RELEASE:" + android.os.Build.VERSION.RELEASE);
//                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, getDeviceId(context), mode);
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
            String uuid = getUUID();
            if(!TextUtils.isEmpty(uuid)){
                deviceId.append("id");
                deviceId.append(uuid);
                LogUtil.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID());
        }

        LogUtil.e("getDeviceId : ", deviceId.toString());

        return deviceId.toString();

    }

    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(){
        uuid = SharedPreferencesUtil.getString("uuid", "");

        if(TextUtils.isEmpty(uuid)){
            uuid = UUID.randomUUID().toString();
            SharedPreferencesUtil.saveString("uuid", uuid);
        }

        LogUtil.e("getUUID : " + uuid);
        return uuid;
    }
}
