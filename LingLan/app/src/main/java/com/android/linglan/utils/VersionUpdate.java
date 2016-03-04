package com.android.linglan.utils;

import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;

/**
 * Created by Administrator on 2016/2/23 0023.
 */
public class VersionUpdate {

    //检查是否有新版本
    public static void versionUpdate(String version){
        NetApi.getCheckUpdate(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
                //如果有新版本，则调下载安装包的方法下载
            }

            @Override
            public void onFailure(String message) {

            }
        },version);
    }

    //下载新版本app安装包
    public static void AppDownLoad(){
        NetApi.getAppDownLoad(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
