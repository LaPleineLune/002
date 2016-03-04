package com.android.linglan.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.linglan.LinglanApplication;

/**
 * Created by LeeMy on 2016/1/6 0006.
 */
public class ToastUtil {

    private static final int SPACE_SIZE = 3;
    private static final long TOAST_CD = 3 * 1000;

    private static String lastToast;
    private static boolean inCounting;

    private static int space_count = SPACE_SIZE;



    public static final int LENGTH_SHORT = android.widget.Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = android.widget.Toast.LENGTH_LONG;

    private static android.widget.Toast toast;
    private static Handler handler = new Handler();

    private static Runnable run = new Runnable() {
        public void run() {
            toast.cancel();
        }
    };

    private static void toast(Context ctx, CharSequence msg, int duration) {
        handler.removeCallbacks(run);
        // handler的duration不能直接对应Toast的常量时长，在此针对Toast的常量相应定义时长
        switch (duration) {
            case LENGTH_SHORT:// Toast.LENGTH_SHORT值为0，对应的持续时间大概为1s
                duration = 1000;
                break;
            case LENGTH_LONG:// Toast.LENGTH_LONG值为1，对应的持续时间大概为3s
                duration = 3000;
                break;
            default:
                break;
        }
        if (null != toast) {
            toast.setText(msg);
        } else {
            toast = android.widget.Toast.makeText(ctx, msg, duration);
        }
        handler.postDelayed(run, duration);
        toast.show();
    }

    /**
     * 弹出Toast
     *
     * @param ctx
     *            弹出Toast的上下文
     * @param msg
     *            弹出Toast的内容
     * @param duration
     *            弹出Toast的持续时间
     */
    public static void show(Context ctx, CharSequence msg, int duration)
            throws NullPointerException {
        if (null == ctx) {
            throw new NullPointerException("The ctx is null!");
        }
        if (0 > duration) {
            duration = LENGTH_SHORT;
        }
        toast(ctx, msg, duration);
    }

    /**
     * 弹出Toast
     *
     * @param ctx
     *            弹出Toast的上下文
     * @param duration
     *            弹出Toast的持续时间
     */
    public static void show(Context ctx, int resId, int duration)
            throws NullPointerException {
        if (null == ctx) {
            throw new NullPointerException("The ctx is null!");
        }
        if (0 > duration) {
            duration = LENGTH_SHORT;
        }
        toast(ctx, ctx.getResources().getString(resId), duration);
    }



    public static void show(String message) {
        if (TextUtils.isEmpty(message) || message.equals(lastToast) || !hasToastSpace()) {
            return;
        }
        lastToast = message;

        Toast.makeText(LinglanApplication.getsApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

    private static CountDownTimer toastCountDown = new CountDownTimer(TOAST_CD, TOAST_CD) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            resetCD();
        }
    };

    private static boolean hasToastSpace() {
        if (!inCounting) {
            inCounting = true;
            toastCountDown.start();
        }
        return space_count-- > 0;
    }

    private static void resetCD() {
        lastToast = "";
        space_count = SPACE_SIZE;
        inCounting = false;
    }
}
