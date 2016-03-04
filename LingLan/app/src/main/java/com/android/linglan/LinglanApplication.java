package com.android.linglan;

import android.app.Application;
import android.content.Context;
import android.util.Log;

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
//        readConfigFromManifest();
//        JPushInterface.init(this);
    }

//    private void readConfigFromManifest() {
//        try {
//            ApplicationInfo ai =
//                    getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
//            Bundle bundle = ai.metaData;
//            int pCode = bundle.getInt("CHANNEL_CODE");
//            if (pCode > 0) {
//                Constants.SPCODE = "0" + pCode;
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//    }


    public static Context getsApplicationContext() {
        return sApplicationContext;
    }
}
