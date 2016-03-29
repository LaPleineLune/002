package com.android.linglan.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.CommonProtocol;
import com.android.linglan.ui.me.RegisterActivity;
import com.android.linglan.widget.UpdateDialog;

/**
 * Created by Administrator on 2016/3/4 0004.
 */
public class HttpCodeJugementUtil {

    public static UpdateDialog exitLoginDialog;

    public static boolean HttpCodeJugementUtil(String responseString,Context context){

        if (!TextUtils.isEmpty(responseString)) {
            try {
                CommonProtocol result = JsonUtil.json2Bean(responseString, CommonProtocol.class);
                if ("1".equals(result.code)) {// 没有数据
                    ToastUtil.show(result.msg);
                    return false;
                }else if ("2".equals(result.code)) {// 需要登录，可以提示未登录，提示用户登录      用户未登陆（用户需要登录）
                    ToastUtil.show(result.msg);
                    getUserExit(context);
                    return false;
                }else if ("3".equals(result.code)) {// 登录异常，需要重新登录，提示用户登录       登录超时（token过期，用户需要重新登录）
                    ToastUtil.show(result.msg);
                    getUserExit(context);
                    return false;
                }else if ("4".equals(result.code)) {//登录异常    需要重新登录，提示用户登录
                    ToastUtil.show(result.msg);
                    return false;
                }else if ("5".equals(result.code)) {// （appkey无效）
                    ToastUtil.show(result.msg);
                    return false;
                }else if ("6".equals(result.code)) {//token为空，需要重新get token
                    ToastUtil.show(result.msg);
                    return false;
                }else if ("701".equals(result.code)) {//没有权限，需要登录       没有权限，需要登录（用户需要登录）
                    ToastUtil.show(result.msg);
                    return false;
                }else if ("702".equals(result.code)) {//没有权限，需升级会员
                    ToastUtil.show(result.msg);
                    return false;
                }else if ("10001".equals(result.code)) {//系统异常
                    ToastUtil.show(result.msg);
                    return false;
                }else if ("11001".equals(result.code)) {//系统异常
//                       if(((result.msg).trim()).equals("您的手机号未能通过身份认证，请等待正式的发布。若您是亲情会员，请联系客服。")){
                           showIsVipDialog(context,result.msg);
//                       }else{
//                           ToastUtil.show(result.msg);
//                       }
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
//                Log.w(TAG, exception.toString());
            }
        }
        return true;
    }
    //退出登录
    public static void getUserExit(Context context){

                SharedPreferencesUtil.removeValue("token");
                SharedPreferencesUtil.removeValue("face");// 头像

                SharedPreferencesUtil.removeValue("phone");
                SharedPreferencesUtil.removeValue("username");

                SharedPreferencesUtil.removeValue("alias");// 用户昵称
                SharedPreferencesUtil.removeValue("isfamilymember");// 亲情会员

        context.startActivity(new Intent(context, RegisterActivity.class));
    }
//    "您的手机号未能通过身份认证，请等待正式版发布。若您是亲情会员，请联系客服。"
    public static void showIsVipDialog(Context context,String msg) {
        if(exitLoginDialog == null){
            exitLoginDialog = new UpdateDialog(context, msg, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exitLoginDialog.dismiss();
                    exitLoginDialog = null;
                }
            });
            exitLoginDialog.setTitle("提示");
            exitLoginDialog.setEnterText("确定");
            exitLoginDialog.show();
        }
    }

}
