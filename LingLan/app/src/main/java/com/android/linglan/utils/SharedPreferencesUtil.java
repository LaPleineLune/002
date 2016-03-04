package com.android.linglan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.android.linglan.LinglanApplication;

/**
 * Created by LeeMy on 2016/1/6 0006.
 */
public class SharedPreferencesUtil {
    private final static String SP_NAME = "pb_config";
    private static SharedPreferences sharedPreferences = null;
    private static boolean shouldCommit = Integer.parseInt(Build.VERSION.SDK) < 9;

    private static SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences =
                    LinglanApplication.getsApplicationContext().getSharedPreferences(SP_NAME,
                            Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    private static void save(SharedPreferences.Editor editor) {
        if (shouldCommit) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public static void saveBoolean(String key, boolean value) {
        save(getSharedPreferences().edit().putBoolean(key, value));
    }

    public static void saveInt(String key, int value) {
        save(getSharedPreferences().edit().putInt(key, value));
    }

    public static void saveString(String key, String value) {
        save(getSharedPreferences().edit().putString(key, value));
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public static String getString(String key, String defValue) {
        return getSharedPreferences().getString(key, defValue);
    }

    public static void removeValue(String key) {
        save(getSharedPreferences().edit().remove(key));
    }

}
