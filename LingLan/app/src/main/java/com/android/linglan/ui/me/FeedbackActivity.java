package com.android.linglan.ui.me;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SoftKeyboardUtil;
import com.android.linglan.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LeeMy on 2016/1/7 0007.
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity {

    private View view1;
    private EditText contentEdit, emailEdit;
    private ImageView iv_image;
    private LinearLayout rl_feedback_email;
    private FeedbackActivity ctx;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {


            view1.requestLayout();
            LogUtil.v("ccc", "invalidate");
        }
    };

    @Override
    protected void setView() {
//        setContentView(R.layout.activity_feedback);
        view1 = View.inflate(this, R.layout.activity_feedback, null);
        setContentView(view1);
    }

    @Override
    protected void initView() {
        iv_image = (ImageView) findViewById(R.id.iv_image);
        contentEdit = (EditText) findViewById(R.id.edt_feedback_massage);
        rl_feedback_email = (LinearLayout) findViewById(R.id.rl_feedback_email);
        emailEdit = (EditText) findViewById(R.id.edt_feedback_email);
        emailEdit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    @Override
    protected void initData() {
        setTitle("意见反馈", "提交");
//        right.setTextColor(R.color.font_deep_color);
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.submit);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
//        initKeyboard();

//        /* 获取屏幕宽高 */
//        if (Constants.isShow) {
//            DisplayMetrics dm=new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(dm);
//            ToastUtil.show(Integer.toString(dm.heightPixels) + ":" + Integer.toString(dm.widthPixels));
//            System.out.println(Integer.toString(dm.heightPixels)+":"+Integer.toString(dm.widthPixels));
//        }
    }

    private void initKeyboard() {
        SoftKeyboardUtil.observeSoftKeyboard(FeedbackActivity.this, new SoftKeyboardUtil.OnSoftKeyboardChangeListener() {
            @Override
            public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {
                LogUtil.v("ccc", softKeybardHeight + "visible====" + visible);
                int tempsoftKeybardHeight = 0;
                if (visible) {
                    LogUtil.v("ccc", "00000000000");

                    tempsoftKeybardHeight = (softKeybardHeight - 50);
                    TranslateAnimation rotateAnima = new TranslateAnimation(
                            0f, 0f, 0f, -tempsoftKeybardHeight);
                    rotateAnima.setDuration(200);
                    rotateAnima.setFillAfter(true); // 设置动画执行完毕时, 停留在完毕的状态下.
                    iv_image.startAnimation(rotateAnima);
//                    contentEdit.setBackgroundColor(FeedbackActivity.this.getResources().getColor(R.color.transgry));
//                    rl_feedback_email.setBackgroundColor(FeedbackActivity.this.getResources().getColor(R.color.transgry));
                    contentEdit.setBackgroundResource(R.drawable.dialog_login_edit_keyup_bg);
                    rl_feedback_email.setBackgroundResource(R.drawable.dialog_login_edit_keyup_bg);

                } else {
                    LogUtil.v("ccc", "1111111111111");

                    TranslateAnimation rotateAnima = new TranslateAnimation(
                            0f, 0f, -tempsoftKeybardHeight, 0);
                    rotateAnima.setDuration(200);
                    rotateAnima.setFillAfter(true); // 设置动画执行完毕时, 停留在完毕的状态下.
                    iv_image.startAnimation(rotateAnima);
//                    contentEdit.setBackgroundColor(FeedbackActivity.this.getResources().getColor(R.color.trans));
//                    rl_feedback_email.setBackgroundColor(FeedbackActivity.this.getResources().getColor(R.color.trans));
                    contentEdit.setBackgroundResource(R.drawable.dialog_login_edit_keydown_bg);
                    rl_feedback_email.setBackgroundResource(R.drawable.dialog_login_edit_keydown_bg);

                }
            }
        });
    }

    @Override
    protected void setListener() {
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String suggestioncontent = contentEdit.getText().toString().trim();
                String email = emailEdit.getText().toString().trim();
                if (suggestioncontent != null && !suggestioncontent.equals("")) {
                    sendFeedback(suggestioncontent, email);
                } else {
                    ToastUtil.show("发送内容不能为空");
                }
            }
        });
    }

    private void sendFeedback(String content, String email){
       NetApi.sendFeedback(new PasserbyClient.HttpCallback() {

           @Override
           public void onSuccess(String result) {

               if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,FeedbackActivity.this)){
                   return;
               }

               try {
                   JSONObject resultJson = new JSONObject(result);
                   String msg = resultJson.getString("msg");
                   ToastUtil.show("发送" + msg);
                   finish();
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }

           @Override
           public void onFailure(String message) {

           }
       }, content, email);
    }

}
