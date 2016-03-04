package com.android.linglan.ui.me;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.R;
import com.android.linglan.utils.AppUpdaterUtil;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.UpdateDialog;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by LeeMy on 2016/1/6 0006.
 * 设置界面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout clean_buffer_item;// 清除缓存
    private RelativeLayout text_size_item;// 字体大小

    private RelativeLayout check_update_item;// 检查更新
    private RelativeLayout about_item;// 关于我们

    private Button exit_btn;// 退出登录

    private TextView clean_buffer;
    private TextView text_size;
    private TextView check_update;

    private UpdateDialog exitLoginDialog;

    @Override
    public void onResume() {
        super.onResume();
        text_size.setText(SharedPreferencesUtil.getInt(Constants.FONT_SIZE, 16) + "号");
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initView() {
        clean_buffer_item = (RelativeLayout) findViewById(R.id.clean_buffer_item);
        text_size_item = (RelativeLayout) findViewById(R.id.text_size_item);
        check_update_item = (RelativeLayout) findViewById(R.id.check_update_item);
        about_item = (RelativeLayout) findViewById(R.id.about_item);
        exit_btn = (Button) findViewById(R.id.exit_btn);
        clean_buffer = (TextView) findViewById(R.id.clean_buffer);
        text_size = (TextView) findViewById(R.id.text_size);
        check_update = (TextView) findViewById(R.id.check_update);

    }

    @Override
    protected void initData() {
        setTitle("设置", "");
//        fontsize = SharedPreferencesUtil.getInt(Constants.FONT_SIZE, 16);
        text_size.setText(SharedPreferencesUtil.getInt(Constants.FONT_SIZE, 16) + "号");// 初始化文字大小
        try {
            check_update.setText(getVersionName());// 初始化文字大小
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (SharedPreferencesUtil.getString("token", null) != null) {
            exit_btn.setVisibility(View.VISIBLE);
        } else {
            exit_btn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setListener() {
        clean_buffer_item.setOnClickListener(this);
        text_size_item.setOnClickListener(this);
        check_update_item.setOnClickListener(this);
        about_item.setOnClickListener(this);
        exit_btn.setOnClickListener(this);
//        if (SharedPreferencesUtil.getString("token", null) != null) {
//            exit_btn.setOnClickListener(this);
//        } else {
//            exit_btn.setBackgroundResource(R.color.gainsboro);
//            exit_btn.setOnClickListener(null);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        clean_buffer.setText(getCache());

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.clean_buffer_item:
                clear();
                clean_buffer.setText(getCache());
                clean_buffer_item.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show("清除缓存成功");
                    }
                }, 300);
                break;
            case R.id.text_size_item:
                startActivity(new Intent(SettingActivity.this, SetFontSizeActivity.class));
                break;
            case R.id.check_update_item:
                new AppUpdaterUtil().getUpdate(SettingActivity.this);
                break;
            case R.id.about_item:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;
            case R.id.exit_btn:
                showExitDialog();
                break;
            default:
                break;
        }
    }

    private String getVersionName() throws Exception {
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }

    private void showExitDialog() {
        exitLoginDialog = new UpdateDialog(this, "确定退出当前账号吗", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferencesUtil.removeValue("token");
//                SharedPreferencesUtil.removeValue("phone");
//                SharedPreferencesUtil.removeValue("username");
//                logout(SHARE_MEDIA.SINA);
//                logout(SHARE_MEDIA.QQ);
//                logout(SHARE_MEDIA.WEIXIN);
//                AuthenticationActivity.show(SettingActivity.this);
                getUserExit();
//                startActivity(new Intent(SettingActivity.this, RegisterActivity.class));
                exitLoginDialog.dismiss();
//                finish();
            }
        });
        exitLoginDialog.setTitle("退出登录");
        exitLoginDialog.setCancelText("取消");
        exitLoginDialog.setEnterText("确定");
        exitLoginDialog.show();
    }

//    public void logout(final SHARE_MEDIA platform) {
//        SnsUtil.logout(SettingActivity.this, platform,
//                new SocializeListeners.SocializeClientListener() {
//                    @Override
//                    public void onStart() {
//
//                    }
//
//                    @Override
//                    public void onComplete(int i, SocializeEntity socializeEntity) {
//
//                    }
//                });
//    }

    private void clear() {
//        SharedPreferencesUtil.saveString("ad_image", "");
        try {
            File cache = getCacheDir();
            if (cache != null && cache.isDirectory()) {
                deleteDir(cache);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir != null ? dir.delete() : false;
    }

    public String getCache() {
        long cacheSize = 0;
        File cacheDirectory = getCacheDir();
        File[] files = cacheDirectory.listFiles();
        for (File f : files) {
            cacheSize = cacheSize + f.length();
        }
        return formatSizeInfo(cacheSize);
    }

    static String formatSizeInfo(double size) {
        StringBuilder infoText = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.0");
        if (size <= 0) {
            infoText.append("0KB");
            return infoText.toString();
        }
        if (size > StorageSize.GIGA) {
            infoText.append(df.format((size / StorageSize.GIGA)) + " GB");
            return infoText.toString();
        } else if (size > StorageSize.MEGA) {
            infoText.append(df.format((size / StorageSize.MEGA)) + " MB");
            return infoText.toString();
        }
        infoText.append(df.format((size / StorageSize.KILO)) + " KB");
        return infoText.toString();
    }

    static final class StorageSize {
        public static final int KILO = 1024;
        public static final int MEGA = 1024 * 1024;
        public static final int GIGA = 1024 * 1024 * 1024;
    }

    //退出登录
    private void getUserExit(){
        NetApi.getUserExit(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getUserExit=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }

                SharedPreferencesUtil.removeValue("token");
                SharedPreferencesUtil.removeValue("phone");
                SharedPreferencesUtil.removeValue("username");
//                logout(SHARE_MEDIA.SINA);
//                logout(SHARE_MEDIA.QQ);
//                logout(SHARE_MEDIA.WEIXIN);
//                AuthenticationActivity.show(SettingActivity.this);
                startActivity(new Intent(SettingActivity.this, RegisterActivity.class));
//                exitLoginDialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(String message) {
                ToastUtil.show("退出失败");

            }
        });
    }
}
