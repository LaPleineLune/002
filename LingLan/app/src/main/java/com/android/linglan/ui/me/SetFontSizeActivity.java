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
    private int fontsize = 16; // 字体大小
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
        fontsize = SharedPreferencesUtil.getInt(Constants.FONT_SIZE, 16);// 初始化文字大小
        tv_font_size_show.setTextSize(fontsize);

        initSetFontSeek();
    }

    @Override
    protected void setListener() {

    }

    private void initSetFontSeek() {
        fontseek.setMax(20);
        fontseek.setProgress(fontsize - 10);
        fontseek.setSecondaryProgress(0);
        textFont.setText(fontsize + "");
        fontseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fontsize = progress + 10;
                textFont.setText("" + fontsize);
                handler.sendEmptyMessage(FONTSIZECHANG);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferencesUtil.saveInt(Constants.FONT_SIZE, fontsize);
            }
        });
    }
}
