package com.android.linglan.download;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.linglan.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by LeeMy on 2016/3/3 0003.
 */
public class DownloadApp {
    private Activity activity;
    //	public static HomeAddActivity homeAddActivity;// 改 lee：2015.10.27  17:00
    private ProgressDialog mpDialog;
    private String url;
    private String title;
    private String msg;
    private String isforceupdate;
    int fileSize;
    int downLoadFileSize;

    String downloadPath = Environment.getExternalStorageDirectory().getPath()
            + "/linglan/downloadapp";
    String apkName ;
    public static int  returnResturn = 0;

    public DownloadApp(Activity activity, String url, String apkName, String title, String msg, String isforceupdate){
        this.activity = activity;
        this.url = url;
        this.title = title;
        this.msg = msg;
        this.apkName = "/"+apkName;
        this.isforceupdate = isforceupdate;
        File file1 = new File(downloadPath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        dialog();
    }

    public void dialog() {
        if (isforceupdate.equals("1")) {
//			if (homeAddActivity != null) {
//				if ("home".equals(activity.getIntent().getStringExtra("home"))) {
//					homeAddActivity.finish();
//				}
//			}
            forceupdate();
        } else {
//            if (SpfUtils.getIsFirst()) {// 如果是第一次打开软件，且是提示更新

                Builder builder = new Builder(activity);
                builder.setMessage(msg);
                builder.setTitle(title);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        forceupdate();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

                builder.create().show();

//                SpfUtils.setIsFirst(false);// 设置为已经提示过了更新		//lee 2015-09-28 15:15
//            }
        }
    }
    private void sendMsg(int flag) {
        Message msg = new Message();
        msg.what = flag;
        handler.sendMessage(msg);
    }
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
//				Log.i("msg what", String.valueOf(msg.what));// 原   2015.11.19  15:09:01
                LogUtil.i("msg what", String.valueOf(msg.what));// 改   lee   2015.11.19  15:09:01
                switch (msg.what) {
                    case 0:
                        mpDialog.setMax(100);
                        break;
                    case 1:
                        int result = downLoadFileSize * 100 / fileSize;
                        mpDialog.setProgress(result);
                        break;
                    case 2:
                        returnResturn = 1;
                        mpDialog.setMessage("文件下载完成");
                        installApk(downloadPath + apkName);
                        break;
                    case -1:
                        String error = msg.getData().getString("error");
                        mpDialog.setMessage(error);
                        break;
                    default:
                        break;
                }
            }

            super.handleMessage(msg);

        }
    };
    // 安装apk方法
    private void installApk(String filename) {
        File file = new File(filename);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(Uri.fromFile(file), type);
        activity.startActivity(intent);
        if (mpDialog != null) {
            mpDialog.cancel();
        }
    }

    public void forceupdate() {
        mpDialog = new ProgressDialog(activity);
        mpDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mpDialog.setTitle(title);
        mpDialog.setMessage("正在下载中，请稍后");
        mpDialog.setIndeterminate(false);
        mpDialog.setCancelable(false);
        mpDialog.setCanceledOnTouchOutside(false);
        mpDialog.setProgress(0);
        mpDialog.incrementProgressBy(1);
        mpDialog.show();
        new Thread() {
            public void run() {
                String apkUrl = url;
                URL url = null;
                try {
                    url = new URL(apkUrl);
                    HttpURLConnection con = (HttpURLConnection) url
                            .openConnection();
                    InputStream in = con.getInputStream();
                    fileSize = con.getContentLength();
                    File fileOut = new File(downloadPath
                            + apkName);
                    FileOutputStream out = new FileOutputStream(fileOut);
                    byte[] bytes = new byte[1024];
                    downLoadFileSize = 0;
                    sendMsg(0);
                    int c;
                    while ((c = in.read(bytes)) != -1) {
                        out.write(bytes, 0, c);
                        downLoadFileSize += c;
                        sendMsg(1);
                    }
                    in.close();
                    out.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                sendMsg(2);
                try {

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }
}