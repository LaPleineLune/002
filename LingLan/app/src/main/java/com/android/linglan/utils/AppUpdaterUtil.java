package com.android.linglan.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.view.View;

import com.android.linglan.download.DownloadApp;
import com.android.linglan.download.DownloadFileTask;
import com.android.linglan.download.DownloadItem;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.Body;
import com.android.linglan.ui.R;
import com.android.linglan.widget.UpdateDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LeeMy on 2016/1/6 0006.
 */
public class AppUpdaterUtil {
    private static final long ONE_DAY_TIME_MILLIS = 1000 * 60 * 60 * 24;

    private String description;// 版本的主要信息
    private String title;// 版本的主要信息
    private String downloadUrl;// 版本APK的路径
    private UpdateDialog updateDialog;//UpdateDialog
    private UpdateDialog noUpdateDialog;
    private String versionName;
    private String version = "";
    private String[] marketWhiteList = new String[] {"com.baidu.appsearch",
            "com.wandoujia.phoenix2", "com.xiaomi.market", "com.qihoo.appstore",
            "com.tencent.android.qqdownloader", "com.huawei.appmarket"};
    private String enter;
    private boolean hasUpdate = false;
    private Activity mContext;
    private Body.BodyData data;
    private UpdateDialog exitLoginDialog;

    private boolean checkVersionAutomatically;

    public void checkToUpdate(Activity context) {
        checkVersionAutomatically = true;
        getUpdate(context);
    }

    private boolean shouldUpdate(int forceUpdate) {
        return forceUpdate == 1 || !checkVersionAutomatically || noCheckForOneDay();
//        return forceUpdate == 0 || !checkVersionAutomatically || noCheckForOneDay();
    }

    private boolean noCheckForOneDay() {
        Long lastTimestamp = 0l;
        try {
            lastTimestamp =
                    Long.parseLong(SharedPreferencesUtil.getString("last_check_update_timestamp", "0"));
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
        }
        return (System.currentTimeMillis() - lastTimestamp) > ONE_DAY_TIME_MILLIS;
    }

    public void getUpdate(final Activity context) {
        mContext = context;
        try {
            version = getVersionName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetApi.getCheckUpdate(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {

                LogUtil.e("getCheckUpdate=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,context)){
                    return;
                }

                Body bean = JsonUtil.json2Bean(result, Body.class);
                data = bean.data;


                if (data.isupdate == 0) {
                    if (checkVersionAutomatically) {
                        return;
                    }

                    if (NetworkUtil.isNetworkConnected(mContext)) {
                        hasUpdate = false;
                        title = "暂无更新";
                        description = "您使用的已经是最新版本";
                        noUpdateDialog = new UpdateDialog(mContext, description, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                safeDismissingDialog(noUpdateDialog);
                            }
                        });
                        noUpdateDialog.setEnterText("确定");
                        safeShowingDialog(noUpdateDialog);
                    }
                } else {
//                    title = bean.title;
                    title = "有新版本了！";
                    versionName = data.number;

                    if (!shouldUpdate(data.isupdate)) {
                        return;
                    }
                    SharedPreferencesUtil.saveString("last_check_update_timestamp",
                            String.valueOf(System.currentTimeMillis()));

                    description = data.description;
                    downloadUrl = Constants.URL_APP_DOWNLOAD;

                    if (0 == data.isforce) {// 非强制更新
                        hasUpdate = true;
                        showDialog(bean);
                    } else {// 强制更新

                        exitLoginDialog = new UpdateDialog(mContext, description, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DownloadApp app = new DownloadApp(mContext, downloadUrl, "linglan" + versionName + ".apk", title, description, data.isforce+"");
                                exitLoginDialog.dismiss();
                            }
                        });
                        exitLoginDialog.setTitle(title);
//                        exitLoginDialog.setCancelText("取消");
                        exitLoginDialog.setEnterText("立即更新");
                        exitLoginDialog.show();
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                if (checkVersionAutomatically) {
                    return;
                }

                if (NetworkUtil.isNetworkConnected(mContext)) {
                    hasUpdate = false;
                    title = "暂无更新";
                    description = "您使用的已经是最新版本";
                    noUpdateDialog = new UpdateDialog(mContext, description, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            safeDismissingDialog(noUpdateDialog);
                        }
                    });
                    noUpdateDialog.setEnterText("确定");
                    safeShowingDialog(noUpdateDialog);
                }
            }

        }, version);
    }

    private String getVersionName() throws Exception {
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        return packInfo.versionName;
    }

    // 弹出对话框
    private void showDialog(final Body bean) {
//    private void showDialog() {
        int backVisible = hasUpdate ? View.VISIBLE : View.GONE;
        updateDialog = new UpdateDialog(mContext, title, description, backVisible,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!hasUpdate) {
                            safeDismissingDialog(updateDialog);
                        } else {
//                            String str = "market://details?id=" + mContext.getPackageName();
//                            Intent intent = new Intent();
//                            intent.setAction("android.intent.action.VIEW");
//                            Uri content_url = Uri.parse(str);
//                            intent.setData(content_url);
//                            if(!generateCustomChooserIntent(intent, marketWhiteList)) {
//                                DownloadItem downloadItem = new DownloadItem();
//                                downloadItem.title = mContext.getString(R.string.app_name);
//                                downloadItem.downloadUrl = downloadUrl;
//                                downloadItem.versionName = versionName;
////                                downloadItem.md5 = bean.mdv;
//                                new DownloadFileTask(downloadItem).execute();
//
//                            }
                            DownloadApp app = new DownloadApp(mContext, downloadUrl, "linglan" + versionName + ".apk", title, description, "1");
                            safeDismissingDialog(updateDialog);
                        }
                    }
                });
        updateDialog.setEnterText(enter);
        safeShowingDialog(updateDialog);
    }

    private boolean generateCustomChooserIntent(Intent prototype, String[] whiteList) {
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        List<HashMap<String, String>> intentMetaInfo = new ArrayList<HashMap<String, String>>();
        Intent chooserIntent;

        List<ResolveInfo> resInfo =
                mContext.getPackageManager().queryIntentActivities(prototype,
                        PackageManager.MATCH_DEFAULT_ONLY);

        if (!resInfo.isEmpty()) {
            for (ResolveInfo resolveInfo : resInfo) {
                if (resolveInfo.activityInfo == null
                        || !Arrays.asList(whiteList).contains(resolveInfo.activityInfo.packageName))
                    continue;

                HashMap<String, String> info = new HashMap<String, String>();
                info.put("packageName", resolveInfo.activityInfo.packageName);
                info.put("className", resolveInfo.activityInfo.name);
                info.put("simpleName",
                        String.valueOf(resolveInfo.activityInfo.loadLabel(mContext.getPackageManager())));
                intentMetaInfo.add(info);
            }

            if (!intentMetaInfo.isEmpty()) {
                // sorting for nice readability
                Collections.sort(intentMetaInfo, new Comparator<HashMap<String, String>>() {
                    @Override
                    public int compare(HashMap<String, String> map, HashMap<String, String> map2) {
                        return map.get("simpleName").compareTo(map2.get("simpleName"));
                    }
                });

                // create the custom intent list
                for (HashMap<String, String> metaInfo : intentMetaInfo) {
                    Intent targetedShareIntent = (Intent) prototype.clone();
                    targetedShareIntent.setPackage(metaInfo.get("packageName"));
                    targetedShareIntent.setClassName(metaInfo.get("packageName"), metaInfo.get("className"));
                    targetedShareIntents.add(targetedShareIntent);
                }
                // Remove and put the item as the first element in chooser intent.
                chooserIntent =
                        Intent.createChooser(targetedShareIntents.remove(targetedShareIntents.size() - 1),
                                "应用升级");
                // Append the others.
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                        targetedShareIntents.toArray(new Parcelable[] {}));
                mContext.startActivity(chooserIntent);
                return true;
            }
        }
        return false;
    }

    private void safeShowingDialog(Dialog dialog) {
        if (dialog != null && mContext != null) {
            try {
                dialog.show();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void safeDismissingDialog(Dialog dialog) {
        if (dialog == null || mContext == null || mContext.isDestroyed()) {
            return;
        }
        try {
            dialog.dismiss();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
