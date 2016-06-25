package com.android.linglan.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.android.linglan.fragment.ClinicalFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.CommonProtocol;
import com.android.linglan.ui.MainActivity;
import com.android.linglan.ui.me.RegisterActivity;
import com.android.linglan.widget.UpdateDialog;

/**
 * Created by Administrator on 2016/3/4 0004.
 */
public class HttpCodeJugementUtil {

    public static UpdateDialog exitLoginDialog;
    public static int code;
    private static boolean response = false;

    public static boolean HttpCodeJugementUtil(String responseString,Context context){
        if (!TextUtils.isEmpty(responseString)) {
            try {
                CommonProtocol result = JsonUtil.json2Bean(responseString, CommonProtocol.class);
                code = result.code;
                switch (code) {
                    case 1:// 没有数据
                        response = false;
                        break;
                    case 2:// 需要登录，可以提示未登录，提示用户登录      用户未登陆（用户需要登录）
                        response = false;
                        getUserExit(context, result.msg);
                        break;
                    case 3:// 登录异常，需要重新登录，提示用户登录       登录超时（token过期，用户需要重新登录）
                        response = false;
                        getUserExit(context, result.msg);
                        break;
                    case 4://登录异常    需要重新登录，提示用户登录
                        response = false;
                        getUserExit(context, result.msg);
                        break;
                    case 5:// （appkey无效）
                        response = false;
                        DeviceUtil.getAppKey(context);
                        ToastUtil.show("请重试...");
                        break;
                    case 6:
                        response = false;
                        getUserExit(context, result.msg);
                        break;
                    case 7:// 已登录，但是被挤下去了
                        response = false;
                        getUserExit(context, result.msg);
                        break;
                    case 701://没有权限，需要登录       没有权限，需要登录（用户需要登录）
                        response = false;
                        ToastUtil.show(result.msg);
                        break;
                    case 702://没有权限，需升级会员
                        response = false;
                        ToastUtil.show(result.msg);
                        break;
                    case 10001://系统异常
                        response = false;
                        ToastUtil.show(result.msg);
                        break;
                    case 11001://系统异常
                        response = false;
                        ToastUtil.show(result.msg);
//                        AlertDialogs.alert(context, "提示", result.msg, "取消", "确定");
//                        showIsVipDialog(context,result.msg);
                        break;
                    default:
                        response = true;
                        break;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return response;
    }
    //退出登录
    public static void getUserExit(Context context, String msg){

        SharedPreferencesUtil.removeValue("token");
        SharedPreferencesUtil.removeValue("face");// 头像

        SharedPreferencesUtil.removeValue("phone");
        SharedPreferencesUtil.removeValue("username");

        SharedPreferencesUtil.removeValue("alias");// 用户昵称
        SharedPreferencesUtil.removeValue("isfamilymember");// 亲情会员

        if (code == 7) {
            markotherlogin(context);
            showIsVipDialog(context, msg);
        } else {
            showExitDialog(context, msg);
        }

    }

    private static void showExitDialog(final Context context, String msg) {
        if(exitLoginDialog == null) {
            exitLoginDialog = new UpdateDialog(context, msg, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exitLoginDialog.dismiss();
                    Intent intent = new Intent();
                    intent.setClass(context, RegisterActivity.class);
                    context.startActivity(intent);
                }
            });
            exitLoginDialog.setTitle("提示");
            exitLoginDialog.setCancelText("随便看看");
            exitLoginDialog.setEnterText("重新登录");
            exitLoginDialog.show();
        } else {
            exitLoginDialog.dismiss();
            exitLoginDialog = new UpdateDialog(context, msg, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exitLoginDialog.dismiss();
                    Intent intent = new Intent();
                    intent.setClass(context, RegisterActivity.class);
                    context.startActivity(intent);
                }
            });
            exitLoginDialog.setTitle("提示");
            exitLoginDialog.setCancelText("随便看看");
            exitLoginDialog.setEnterText("重新登录");
            exitLoginDialog.show();
        }
    }
//    "您的手机号未能通过身份认证，请等待正式版发布。若您是亲情会员，请联系客服。"
    private static void showIsVipDialog(final Context context, String msg) {
        if(exitLoginDialog == null){
            exitLoginDialog = new UpdateDialog(context, msg, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exitLoginDialog.dismiss();
                    markotherlogin(context);
                    ClinicalFragment.ISREFRESHDATA = 1;
                    Intent intent = new Intent();
                    intent.setClass(context, MainActivity.class);
                    context.startActivity(intent);
                }
            });
            exitLoginDialog.setTitle("提示");
            exitLoginDialog.setEnterText("确定");
            exitLoginDialog.show();
        } else {
            exitLoginDialog.dismiss();
            exitLoginDialog = new UpdateDialog(context, msg, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exitLoginDialog.dismiss();
                    markotherlogin(context);
                    ClinicalFragment.ISREFRESHDATA = 1;
                    Intent intent = new Intent();
                    intent.setClass(context, MainActivity.class);
                    context.startActivity(intent);
                }
            });
            exitLoginDialog.setTitle("提示");
            exitLoginDialog.setEnterText("确定");
            ClinicalFragment.ISREFRESHDATA = 1;
            exitLoginDialog.show();
        }
    }

    private static void markotherlogin(final Context context) {
//    private void markotherlogin() {
        NetApi.markotherlogin(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("markotherlogin=" + result);
//                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, LinglanApplication.getsApplicationContext())) {
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, context)) {
                    return;
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
