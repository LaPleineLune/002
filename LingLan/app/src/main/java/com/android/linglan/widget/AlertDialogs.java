package com.android.linglan.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/4/21 0021.
 * 新建病历返回时弹的窗口
 */
public class AlertDialogs {
    private static Button leftButton;
    private static Button rightButton;
    private static TextView titleText;
    private static TextView bodyContent;
//    private static CheckBox cbkaochangsetallkaochang; // 提示信息
//    private static Spinner spalertkaochangshowallkaochang; // 提示信息
    public static AlertDialog aDialog;

//    private static JfPreference jfPreference;
//
//    private static Checkinfo checkinfo;
    /**
     *kaochang@param context
     * @param title
     *            弹出框 上面显示的文字
     * @param leftStr
     *            左边按钮的名字
     * @param rightStr
     *            右边按钮的名字
     * @param leftAction
     *            左边按钮的点击事件 * @param rightAction 右边按钮的点击事件
     * @param activity
     *            activity
     */
    public static void alert(final Context context, String title, String content, String leftStr, String rightStr) {
        final View view;
        view = LayoutInflater.from(context).inflate(R.layout.alert_clinical_cancle_dialog, null); // 自定义alertDialog布局
        titleText = (TextView) view.findViewById(R.id.title_text);
        bodyContent = (TextView) view.findViewById(R.id.dialog_body_text);
        leftButton = (Button) view.findViewById(R.id.dialog_cancel);
        rightButton = (Button) view.findViewById(R.id.dialog_enter);
        titleText.setText(title);
        bodyContent.setText(content);
        if (TextUtils.isEmpty(leftStr)) {
            leftButton.setVisibility(View.GONE);
        } else {
            leftButton.setVisibility(View.VISIBLE);
            leftButton.setText(leftStr);
        }
        if (TextUtils.isEmpty(rightStr)) {
            leftButton.setVisibility(View.GONE);
        } else {
            rightButton.setVisibility(View.VISIBLE);
            rightButton.setText(rightStr);
        }

        if (aDialog != null && aDialog.isShowing()) { // 如果在显示，先关闭，防止弹出多个Dialog
            aDialog.dismiss();
        }
        aDialog = new AlertDialog.Builder(context).create(); // 创建一个AlertDialog对象
        try {
            aDialog.show(); // 显示AlertDialog
        } catch (Exception e) {
        }
        WindowManager.LayoutParams params = aDialog.getWindow().getAttributes(); // 得到AlertDialog属性
        params.gravity = Gravity.CENTER; // 显示其显示在中间
//        params.width = (int) (JFScreen.getscreenHight(context) * 0.45); // 设置对话框的宽度为手机屏幕的0.8
//        params.height = (int) (JFScreen.getscreenHight(context) * 0.45);// 设置对话框的高度为手机屏幕的0.25
//        aDialog.getWindow().setAttributes(params); // 設置屬性
        aDialog.getWindow().setContentView(view); // 把自定義view加上去

        leftButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    v.setBackgroundResource(R.drawable.alert_cancel);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    v.setBackgroundResource(R.drawable.alert_cancel1);
                }
                return false;
            }

        });

        rightButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    v.setBackgroundResource(R.drawable.alert_ok);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    v.setBackgroundResource(R.drawable.alert_ok1);
                }
                return false;
            }

        });

        leftButton.setOnClickListener(new View.OnClickListener() { // 左边按钮的点击事件
            /*
             * 这里根据你自己的需求，自己定义和处理了
             *
             * @see
             * android.view.View.OnClickListener#onClick(android.view
             * .View)
             */
            @Override
            public void onClick(View v) {

                ((AlertDialoginter) context).Altert_btleftdo();
                aDialog.dismiss();
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((AlertDialoginter) context).Altert_btrightdo();
                aDialog.dismiss();
            }
        });
        aDialog.setCancelable(false);
    }
}
