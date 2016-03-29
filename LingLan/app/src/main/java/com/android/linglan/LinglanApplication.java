package com.android.linglan;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;

/**
 * Created by LeeMy on 2016/1/6 0006.
 */
public class LinglanApplication extends Application {
    private static Context sApplicationContext;

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
