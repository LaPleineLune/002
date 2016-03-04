package com.android.linglan.download;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.preference.PreferenceActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.linglan.LinglanApplication;
import com.android.linglan.utils.Md5Util;
import com.android.linglan.utils.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by LeeMy on 2016/1/6 0006.
 */
public class DownloadFileTask {
    private static final String TAG = DownloadFileTask.class.getSimpleName();

//    private static AsyncHttpClient client = new AsyncHttpClient();

//    static {
//        client.setEnableRedirects(true);
//    }


    // Set a constant value for identifying downloading notification.
    private static int notificationId = 123;

    Context context;
    DownloadItem downloadItem;
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;

    InnerDownloadProgressCallback innerDownloadProgressCallback = new InnerDownloadProgressCallback();

    public DownloadFileTask(DownloadItem downloadItem) {
        context = LinglanApplication.getsApplicationContext();
        this.downloadItem = downloadItem;
        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void execute() {
        if (tryToInstallDirectly(context, downloadItem)) {
            return;
        }

        start();
//        client.get(downloadItem.downloadUrl, new RangeFileAsyncHttpResponseHandler(new File(downloadItem.getDownloadAppFilePath())) {
//            @Override
//            public void onFailure(int statusCode, PreferenceActivity.Header[] headers, Throwable throwable, File file) {
//                innerDownloadProgressCallback.onCancelled(downloadItem);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, File file) {
//                innerDownloadProgressCallback.onFinished(downloadItem);
//            }
//
//            @Override
//            public void onProgress(int bytesWritten, int totalSize) {
//                super.onProgress(bytesWritten, totalSize);
//                updateLoading(bytesWritten, totalSize);
//            }
//        });
    }

    private void start() {
        notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setContentTitle(downloadItem.title)
                .setContentText("开始下载...")
                        // .setSmallIcon(android.R.drawable.stat_sys_download_done)
                        // Avoid to step into Detail for reducing to cancel download.
                        // For working on pre-Android 2.3 system, set a empty intent.
                .setOngoing(true)
                        // It´s necessary put a resource here. If you don´t put any resource,
                        // then the Notification Bar is not show  even when you setLargeIcon.
                .setSmallIcon(android.R.drawable.stat_sys_download_done);
    /*if (downloadItem.icon != null) {
      notificationBuilder.setLargeIcon(downloadItem.icon);
    }*/
    }

    CountDownTimer loadingUpdateCountDown;
    static final int COUNT_DOWN_LENGTH = 2000;
    boolean coolDown = false;

    private void updateLoading(int bytesWritten, int totalSize) {
        if (coolDown) {
            Log.d(TAG, "Update CD... don't update UI too frequently.");
            return;
        }

        downloadItem.progress = (int) ((bytesWritten * 1.0 / totalSize) * 100);
        innerDownloadProgressCallback.onLoading(downloadItem);

        Log.d(TAG, "Start to update CD.");
        coolDown = true;
        if (loadingUpdateCountDown == null) {
            loadingUpdateCountDown = new CountDownTimer(COUNT_DOWN_LENGTH, COUNT_DOWN_LENGTH) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    coolDown = false;
                    Log.d(TAG, "CD end.");
                }
            };
        }
        loadingUpdateCountDown.start();
    }

    class InnerDownloadProgressCallback implements DownloadProgressCallback {

        @Override
        public void onCancelled(DownloadItem downloadItem) {
            notificationManager.cancel(notificationId);
        }

        @Override
        public void onLoading(DownloadItem downloadItem) {
            notificationBuilder.setProgress(100, downloadItem.progress, false).setContentText(
                    "已下载 " + downloadItem.progress + "%");
            notificationManager.notify(notificationId, notificationBuilder.build());
        }

        @Override
        public void onFinished(DownloadItem downloadItem) {
            notificationBuilder
                    .setAutoCancel(true)
                    .setContentText("下载完成! 点击安装")
                    .setProgress(0, 0, false)
                    .setContentIntent(
                            PendingIntent.getActivity(context, notificationId,
                                    buildInstallIntent(downloadItem.getDownloadAppFilePath()), 0)
                    )
                            // .setOngoing(false)
                    .setAutoCancel(true);
            notificationManager.notify(notificationId, notificationBuilder.build());
            if (!viewApkToInstall(context, downloadItem)) {
                ToastUtil.show("下载失败，请重试！");
            }
        }
    }

    public static interface DownloadProgressCallback {
        public void onCancelled(DownloadItem downloadItem);

        public void onLoading(DownloadItem downloadItem);

        public void onFinished(DownloadItem downloadItem);
    }

    public static boolean tryToInstallDirectly(Context context, DownloadItem apkItem) {
        String apkPath;
        PackageInfo apkInfo;
        try {
            apkPath = apkItem.getDownloadAppFilePath();
            apkInfo = context.getPackageManager().getPackageArchiveInfo(apkPath,
                    PackageManager.GET_META_DATA);
        } catch (NullPointerException ex) {
            // No file existing would throw this exception by File.class in Android pre-2.3 devices.
            return false;
        }
        if (apkInfo != null && apkInfo.versionName.equals(apkItem.versionName)) {
            return viewApkToInstall(context, apkItem);
        }
        return false;
    }

    static boolean apkVerified(String apkPath, String md5) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(apkPath);
            String apkMd5 = Md5Util.md5Digest(fis);
            Log.d(TAG, "Downloaded MD5: " + apkMd5 + " : Online MD5: " + md5);
            return md5.equals(apkMd5);
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean viewApkToInstall(Context context, DownloadItem downloadItem) {
        if (!apkVerified(downloadItem.getDownloadAppFilePath(), downloadItem.md5)) {
            return false;
        }
        context.startActivity(buildInstallIntent(downloadItem.getDownloadAppFilePath()));
        return true;
    }

    public static Intent buildInstallIntent(String filePath) {
        chmod("777", filePath);
        Intent intent = new Intent(
                Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(
                Uri.parse("file://" + filePath),
                "application/vnd.android.package-archive");
        return intent;
    }

    private static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
