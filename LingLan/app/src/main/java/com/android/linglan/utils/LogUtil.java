package com.android.linglan.utils;

import android.util.Log;

/**
 * Created by LeeMy on 2016/1/6 0006.
 */
public class LogUtil {
    /**控制log的打印*/
    public static boolean isShow = true;
    /** 日志输出级别NONE */
    public static final int LEVEL_NONE = 0;
    /** 日志输出级别V */
    public static final int LEVEL_VERBOSE = 1;
    /** 日志输出级别D */
    public static final int LEVEL_DEBUG = 2;
    /** 日志输出级别I */
    public static final int LEVEL_INFO = 3;
    /** 日志输出级别W */
    public static final int LEVEL_WARN = 4;
    /** 日志输出级别E */
    public static final int LEVEL_ERROR = 5;

    /** 日志输出时的TAG */
    private static String mTag = "LogUtil";
    /** 是否允许输出log */
    private static int mDebuggable = LEVEL_ERROR;

    /** 用于记时的变量 */
    private static long mTimestamp = 0;
    /** 写文件的锁对象 */
    private static final Object mLogLock = new Object();


    /** 以级别为 v 的形式输出LOG */
    public static void v(String msg) {
        if(isShow){
            if (mDebuggable >= LEVEL_VERBOSE) {
                Log.v(mTag, msg);
            }
        }
    }

    /** 以级别为 v 的形式输出LOG */
    public static void v(String tag, String msg) {
        if(isShow){
            if (mDebuggable >= LEVEL_VERBOSE && null != msg) {
                Log.v(tag, msg);
            }
        }
    }

    /** 以级别为 d 的形式输出LOG */
    public static void d(String msg) {
        if(isShow){
            if (mDebuggable >= LEVEL_DEBUG) {
                Log.d(mTag, msg);
            }
        }
    }

    /** 以级别为 d 的形式输出LOG */
    public static void d(String tag, String msg) {
        if(isShow){
            if (mDebuggable >= LEVEL_DEBUG && null != msg) {
                Log.d(tag, msg);
            }
        }
    }

    /** 以级别为 i 的形式输出LOG */
    public static void i(String msg) {
        if(isShow){
            if (mDebuggable >= LEVEL_INFO) {
                Log.i(mTag, msg);
            }
        }
    }

    /** 以级别为 i 的形式输出LOG */
    public static void i(String tag, String msg) {
        if(isShow){
            if (mDebuggable >= LEVEL_INFO && null != msg) {
                Log.i(tag, msg);
            }
        }
    }

    /** 以级别为 w 的形式输出LOG */
    public static void w(String msg) {
        if(isShow){
            if (mDebuggable >= LEVEL_WARN) {
                Log.w(mTag, msg);
            }
        }
    }

    /** 以级别为 w 的形式输出Throwable */
    public static void w(Throwable tr) {
        if(isShow){
            if (mDebuggable >= LEVEL_WARN) {
                Log.w(mTag, "", tr);
            }
        }
    }

    /** 以级别为 w 的形式输出LOG信息和Throwable */
    public static void w(String msg, Throwable tr) {
        if(isShow){
            if (mDebuggable >= LEVEL_WARN && null != msg) {
                Log.w(mTag, msg, tr);
            }
        }
    }

    /** 以级别为 w 的形式输出LOG */
    public static void w(String tag, String msg) {
        if(isShow){
            if (mDebuggable >= LEVEL_WARN && null != msg) {
                Log.w(tag, msg);
            }
        }
    }

    /** 以级别为 w 的形式输出LOG */
    public static void w(String tag, String msg, Throwable tr) {
        if(isShow){
            if (mDebuggable >= LEVEL_WARN && null != msg) {
                Log.w(tag, msg, tr);
            }
        }
    }

    /** 以级别为 e 的形式输出LOG */
    public static void e(String msg) {
        if(isShow){
            if (mDebuggable >= LEVEL_ERROR) {
                Log.e(mTag, msg);
            }
        }
    }

    /** 以级别为 e 的形式输出Throwable */
    public static void e(Throwable tr) {
        if(isShow){
            if (mDebuggable >= LEVEL_ERROR) {
                Log.e(mTag, "", tr);
            }
        }
    }

    /** 以级别为 e 的形式输出LOG信息和Throwable */
    public static void e(String msg, Throwable tr) {
        if(isShow){
            if (mDebuggable >= LEVEL_ERROR && null != msg) {
                Log.e(mTag, msg, tr);
            }
        }
    }

    /** 以级别为e 的形式输出LOG */
    public static void e(String tag, String msg) {
        if(isShow){
            if (mDebuggable >= LEVEL_ERROR && null != msg) {
                Log.e(tag, msg);
            }
        }
    }

    /** 以级别为 e 的形式输出LOG */
    public static void e(String tag, String msg, Throwable tr) {
        if(isShow){
            if (mDebuggable >= LEVEL_ERROR && null != msg) {
                Log.e(tag, msg, tr);
            }
        }
    }
}
