package com.android.linglan.utils;

import android.content.Context;
import android.os.CountDownTimer;

import com.android.linglan.widget.ProgressHUD;

/**
 * Created by changyizhang on 12/21/14.
 */
public class ProgressUtil {

  private static final int DEFAULT_CLOSE_TIME = 20 * 1000;

  private static ProgressHUD sProgressHUD;

  private static CountDownTimer autoCloseCD = new CountDownTimer(DEFAULT_CLOSE_TIME,
      DEFAULT_CLOSE_TIME) {
    @Override
    public void onTick(long millisUntilFinished) {}

    @Override
    public void onFinish() {
      dismiss();
    }
  };

  public static void show(Context context, String message) {
    if (sProgressHUD != null && sProgressHUD.isShowing()) {
      sProgressHUD.setMessage(message);
    } else {
      sProgressHUD = ProgressHUD.show(context, message, false, null);
    }

    resetCloseCD();
  }

  public static void dismiss() {
    if (sProgressHUD != null && sProgressHUD.isShowing()) {
      sProgressHUD.dismiss();
      autoCloseCD.cancel();
    }
    sProgressHUD = null;
  }

  private static void resetCloseCD() {
    autoCloseCD.cancel();
    autoCloseCD.start();
  }
}
