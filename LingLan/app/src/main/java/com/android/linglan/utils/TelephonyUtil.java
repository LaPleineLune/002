package com.android.linglan.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.android.linglan.LinglanApplication;

import java.util.UUID;

/**
 * Created by LeeMy on 2016/1/7 0007.
 */
public class TelephonyUtil {
    private static Context getContext() {
        return LinglanApplication.getsApplicationContext();
    }

    public static String getUniqueDeviceId() {
        final TelephonyManager tm = (TelephonyManager) getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, /* tmPhone, */androidId;
        tmDevice = "" + tm.getDeviceId();
        if (tm.getSimState() == 1) {
            tmSerial = "";
        } else {
            tmSerial = "" + tm.getSimSerialNumber();
        }
        androidId = ""
                + android.provider.Settings.Secure.getString(
                getContext().getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }

    public static String getIMEI() {
        final TelephonyManager tm = (TelephonyManager) getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = "";
        imei = tm.getDeviceId();
        return imei;
    }

    public static String getIMSI() {
        final TelephonyManager tm = (TelephonyManager) getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = "";
        if (tm.getSimState() == 1) {
            imsi = tm.getSimSerialNumber();
        }
        return imsi;
    }

    public static String getMacAddress() {
        WifiManager wifiManager = (WifiManager) getContext()
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (connectionInfo != null) {
            return connectionInfo.getMacAddress();
        }
        return "";
    }

    //sim卡是否可读
    public static boolean isCanUseSim() {
        TelephonyManager mgr =
                (TelephonyManager) (getContext().getSystemService(Context.TELEPHONY_SERVICE));
        int s = mgr.getSimState();
        boolean isSim = !(TelephonyManager.SIM_STATE_ABSENT == mgr
                .getSimState());
        return isSim;
    }

    public static String getDeviceSoftwareVersion(){
        TelephonyManager tm = (TelephonyManager) getContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }
}
