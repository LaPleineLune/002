package com.android.linglan.utils;

import android.text.TextUtils;

import com.android.linglan.http.bean.CommonProtocol;

/**
 * Created by Administrator on 2016/3/4 0004.
 */
public class HttpCodeJugementUtil {

    public static boolean HttpCodeJugementUtil(String responseString){

        if (!TextUtils.isEmpty(responseString)) {
            try {
                CommonProtocol result = JsonUtil.json2Bean(responseString, CommonProtocol.class);
//                if ("0".equals(result.code)) {
//
//                    return;
//                } else
                if ("1".equals(result.code)) {// 没有数据
                    ToastUtil.show("暂无数据");
                    return false;
                }else if ("2".equals(result.code)) {// 需要登录，可以提示未登录，提示用户登录
                    ToastUtil.show("需要登录");
                    return false;
                }else if ("3".equals(result.code)) {// 登录超时，需要重新登录，提示用户登录
                    ToastUtil.show("登录超时");
                    return false;
                }else if ("4".equals(result.code)) {//登录异常
                    ToastUtil.show("登录异常");
                    return false;
                }else if ("6".equals(result.code)) {//token为空，需要重新get token
                    ToastUtil.show("token为空");
                    return false;
                }else if ("701".equals(result.code)) {//没有权限，需要登录
                    ToastUtil.show("没有权限，需要登录");
                    return false;
                }else if ("702".equals(result.code)) {//没有权限，需升级会员
                    ToastUtil.show("没有权限，需升级会员");
                    return false;
                }else if ("702".equals(result.code)) {//没有权限，需升级会员
                    ToastUtil.show("没有权限，需升级会员");
                    return false;
                }else if ("10001".equals(result.code)) {//系统异常
                    ToastUtil.show("系统异常");
                    return false;
                }else if ("11001".equals(result.code)) {//系统异常
                    ToastUtil.show("业务异常");
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
//                Log.w(TAG, exception.toString());
            }
        }
        return true;
    }
}
