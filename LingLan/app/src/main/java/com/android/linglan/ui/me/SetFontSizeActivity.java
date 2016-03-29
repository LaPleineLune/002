package com.android.linglan.ui.me;

import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.Constants;
import com.android.linglan.http.bean.ArticleDetails;
import com.android.linglan.ui.R;
import com.android.linglan.utils.SharedPreferencesUtil;

/**
 * Created by LeeMy on 2016/3/2 0002.
 * 字体大小设置界面
 */
public class SetFontSizeActivity extends BaseActivity {
    protected static final int FONTSIZECHANG = 1;
    private int fontsize = 18; // 字体大小
    private int webTextSize = 4; // 字体大小
    private TextView tv_font_size_show;
    private SeekBar fontseek;
    private TextView textFont;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ArticleDetails.ArticleData data = (ArticleDetails.ArticleData) msg.getData().getSerializable("data");
            switch (msg.what) {
                case FONTSIZECHANG:
                    tv_font_size_show.setTextSize(fontsize);
                    break;
                }
            }
        };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_set_font_size);
    }

    @Override
    protected void initView() {
        tv_font_size_show = (TextView) findViewById(R.id.tv_font_size_show);
        fontseek = (SeekBar) findViewById(R.id.settings_font);
        textFont = (TextView) findViewById(R.id.fontSub);

    }

    @Override
    protected void initData() {
        setTitle("字体大小", "");
        fontsize = SharedPreferencesUtil.getInt(Constants.FONT_SIZE, 18);// 初始化文字大小
        tv_font_size_show.setTextSize(fontsize);
        String textSize = SharedPreferencesUtil.getString("webTextSize", "正常");// 初始化文字大小
        if (textSize.equals("较小")) {
            webTextSize = 0;
        } else if (textSize.equals("小")) {
            webTextSize = 2;
        } else if (textSize.equals("正常")) {
            webTextSize = 3;
        } else if (textSize.equals("大")) {
            webTextSize = 4;
        } else if (textSize.equals("较大")) {
            webTextSize = 7;
        }

        initSetFontSeek();
    }

    @Override
    protected void setListener() {

    }

    private void initSetFontSeek() {
        fontseek.setMax(6);
        fontseek.setProgress(webTextSize);
        fontseek.setSecondaryProgress(0);
        textFont.setText("正常");
        fontseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 0:
                    case 1:
                        textFont.setText("较小");
                        fontsize = 10;
                        break;
                    case 2:
                        textFont.setText("小");
                        fontsize = 14;
                        break;
                    case 3:
                        textFont.setText("正常");
                        fontsize = 18;
                        break;
                    case 4:
                        textFont.setText("大");
                        fontsize = 24;
                        break;
                    case 5:
                    case 6:
                        textFont.setText("较大");
                        fontsize = 30;
                        break;
                }
//                fontsize = progress + 10;
//                textFont.setText("" + fontsize);
                handler.sendEmptyMessage(FONTSIZECHANG);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferencesUtil.saveInt(Constants.FONT_SIZE, fontsize);
                SharedPreferencesUtil.saveString("webTextSize", textFont.getText().toString());
            }
        });
    }
}
