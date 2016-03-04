package com.android.linglan.download;

import android.text.TextUtils;
import android.widget.Toast;

import com.android.linglan.LinglanApplication;
import com.android.linglan.utils.FileNameUtil;
import com.android.linglan.utils.FileUtil;
import com.android.linglan.utils.ToastUtil;

/**
 * Created by LeeMy on 2016/1/6 0006.
 */
public class DownloadItem {
    public String title;
    public String packageName;
    public String versionName;
    public int versionCode;
    public String md5;

    public int size;
    public int progress;
    public String downloadUrl;

    private String downloadFilePath;

    public String getDownloadAppFilePath() {
        if (TextUtils.isEmpty(downloadFilePath)) {
            String title = (this.title + "_" + this.versionName).replace(".", "_") + ".apk";
            try {
               downloadFilePath = FileUtil.generateSaveFile(LinglanApplication.getsApplicationContext(), title, null, 0);
            } catch (FileUtil.GenerateSaveFileException e) {
                if (FileUtil.STATUS_INSUFFICIENT_SPACE_ERROR == e.getStatus()) {
                    ToastUtil.show( "没有足够的存储空间!");
                }
                downloadFilePath = null;
                e.printStackTrace();
            }
        }

        return downloadFilePath;
    }
}
