package com.android.linglan.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/1/6 0006.
 */
public class UpdateDialog extends Dialog {
    private View.OnClickListener onClickListener;
    private String title;
    private String content;
    private String cancelText;
    private String enterText;
    private int negativeVisible = -1;
    private TextView titleView;
    private TextView contentView;
    private Button enterButton;
    private Button negativeButton;

    public UpdateDialog(Context context) {
        super(context);
    }

    public UpdateDialog(Context context, String content,
                        View.OnClickListener onClickListener) {
        super(context, R.style.dialog);//R.style.dialog
        this.content = content;
        this.onClickListener = onClickListener;
    }

    public UpdateDialog(Context context, String title, String content,
                        int negativeVisible, View.OnClickListener onClickListener) {
        this(context, content, onClickListener);
        this.title = title;
        this.negativeVisible = negativeVisible;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public void setEnterText(String enterText) {
        this.enterText = enterText;
    }

    private void setNegativeVisible(int visible) {
        switch (visible) {
            case View.INVISIBLE:
                negativeButton.setVisibility(View.INVISIBLE);
                break;
            case View.GONE:
                negativeButton.setVisibility(View.GONE);
                break;
            case View.VISIBLE:
                negativeButton.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.app_update_dialog);
        this.setCancelable(false);
        enterButton = (Button) findViewById(R.id.dialog_enter);
        negativeButton = (Button) findViewById(R.id.dialog_cancel);
        titleView = (TextView) findViewById(R.id.title_text);
        contentView = (TextView) findViewById(R.id.dialog_body_text);
        if (!TextUtils.isEmpty(title))
            titleView.setText(title);
        if (!TextUtils.isEmpty(cancelText)) {
            negativeButton.setText(cancelText);
        } else {
            negativeButton.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(enterText))
            enterButton.setText(enterText);
        setNegativeVisible(negativeVisible);
        contentView.setText(content);
        enterButton.setOnClickListener(onClickListener);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                UpdateDialog.this.dismiss();
            }
        });
    }
}
