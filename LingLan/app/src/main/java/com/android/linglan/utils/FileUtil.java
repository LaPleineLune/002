package com.android.linglan.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/1/6 0006.
 */
public class FileUtil {
    // 删除指定文件夹下所有文件
    // param path 文件夹完整绝对路径
    public static boolean delAllFile(File file) {
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                file.delete();
                return true;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return true;
                }
                for (File f : childFile) {
                    delAllFile(f);
                }
                file.delete();
            }
        }
        return true;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inStream.close();
        // 把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    /**
     * <pre>
     * 功能说明：通过文件地址得到byte数组
     * 日期:	2014-7-25
     * 开发者:
     * @return IMEI
     * </pre>
     */
    public static byte[] getBytesByPath(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static boolean canWrite(File file) {
        if (file == null || !file.exists()) {
            return false;
        }

        String testName = "." + System.currentTimeMillis();
        File testFile = new File(file, testName);

        boolean result = testFile.mkdir();
        if (result) {
            result = testFile.delete();
        }
        return result;
    }

    public static long getAvailableBytes(String root) {
        try {
            if (!TextUtils.isEmpty(root) && new File(root).exists()) {
                StatFs stat = new StatFs(root);
                long availableBlocks = (long) stat.getAvailableBlocks();
                return stat.getBlockSize() * availableBlocks;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getExternalContentDirectory(Context context) {
        return getExternalContentDirectory(context, -1);
    }

    public static String getExternalContentDirectory(Context context, long checkSize) {
        return getExternalContentDirectory(StorageManager.getInstance(context)
                .getExternalStorageDirectory(checkSize), "apk");
    }

    public static String getExternalContentDirectory(String storageDirectory, String type) {
        if (!TextUtils.isEmpty(storageDirectory)) {
            // make the absolute path (lowercase the enum value)
            String content =
                    storageDirectory + StorageManager.ROOT_DIR + type.toLowerCase() + "/";

            File contentFile = new File(content);
            if (!contentFile.exists()) {
                if (!contentFile.mkdirs()) {
                    return null;
                }
            }
            return content;
        } else {
            return null;
        }
    }

    public static String generateSaveFile(
            Context context,
            String title,
            String targetFolder,
            long contentLength) throws GenerateSaveFileException {

        if (TextUtils.isEmpty(targetFolder)) {
            if (contentLength > 0) {
                targetFolder = getExternalContentDirectory(context, contentLength);
            } else {
                targetFolder = getExternalContentDirectory(context);
            }
        }
        if (!TextUtils.isEmpty(targetFolder)) {
            targetFolder = targetFolder.replaceFirst("file://", "");
            if (!targetFolder.endsWith("/")) {
                targetFolder += "/";
            }
            File targetFolderFile = new File(targetFolder);
            if (!targetFolderFile.exists()) {
                if (!targetFolderFile.mkdirs()) {
                    throw new GenerateSaveFileException(
                            STATUS_FILE_ERROR, "unable to make folder");
                }
            }

            String filePath = targetFolder + title;

            if (TextUtils.isEmpty(filePath)) {
                throw new GenerateSaveFileException(
                        STATUS_FILE_ERROR,
                        "unable to generate file name");
            }

            if (FileUtil
                    .getAvailableBytes(getFilesystemRoot(filePath).getAbsolutePath()) < contentLength) {
                throw new GenerateSaveFileException(
                        STATUS_INSUFFICIENT_SPACE_ERROR,
                        "insufficient space on external storage");
            }

            return filePath;
        }
        return null;
    }

    /**
     * @return the root of the filesystem containing the given path
     */
    public static File getFilesystemRoot(String path) {
        File cache = Environment.getDownloadCacheDirectory();
        if (path.startsWith(cache.getPath())) {
            return cache;
        }
        File external = new File(FileNameUtil.getFullPath(path));
        if (external.exists()) {
            return external;
        }
        throw new IllegalArgumentException(
                "Cannot determine filesystem root for " + path);
    }

    public static final int STATUS_FILE_ERROR = 492;
    public static final int STATUS_INSUFFICIENT_SPACE_ERROR = 498;

    /**
     * Exception thrown from methods called by generateSaveFile() for any fatal
     * error.
     */
    public static class GenerateSaveFileException extends Exception {
        private static final long serialVersionUID = -7012378141199515715L;

        int mStatus;
        String mMessage;

        public int getStatus() {
            return mStatus;
        }

        public String getMessage() {
            return mMessage;
        }

        public GenerateSaveFileException(int status, String message) {
            mStatus = status;
            mMessage = message;
        }
    }
}
