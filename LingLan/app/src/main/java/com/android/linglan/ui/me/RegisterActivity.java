package com.android.linglan.ui.me;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.broadcast.SMSBroadcastReceiver;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.RegisterBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.AESCryptUtil;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ProgressUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.TelephonyUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.utils.UmengSnsUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;

import java.util.Map;

/**
 * Created by LeeMy on 2016/2/2 0002.
 * 登录界面
 */
public class RegisterActivity extends BaseActivity {
    private EditText inputPhonenumber;
    private EditText inputCode;
    private Button requestCode;
    private Button submit;
    private ImageView weichat_login_icon;
    private LinearLayout registerDeal;
    private String registerPhone;
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public RegisterBean.RegisterData data;
    private AESCryptUtil aesCryptUtil = new AESCryptUtil();

    @Override
    protected void setView() {
        setContentView(R.layout.activity_register);

    }

    @Override
    protected void initView() {
        inputPhonenumber = (EditText) findViewById(R.id.input_phonenumber);
        inputCode = (EditText) findViewById(R.id.input_code);
        requestCode = (Button) findViewById(R.id.request_code);
        submit = (Button) findViewById(R.id.submit);
//        weichat_login_icon = (ImageView) findViewById(R.id.weichat_login_icon);
        View snsButton = findViewById(R.id.weichat_login_icon);
        snsButton.setTag(SHARE_MEDIA.WEIXIN.ordinal());
        snsButton.setOnClickListener(this);
        registerDeal = (LinearLayout) findViewById(R.id.register_deal);

    }

    @Override
    protected void initData() {
//        setTitle("注册", "");
        setTitle("登录", "");
        UmengSnsUtil.init(this);
    }

    @Override
    protected void setListener() {
        requestCode.setOnClickListener(this);
        submit.setOnClickListener(this);
//        weichat_login_icon.setOnClickListener(this);
        registerDeal.setOnClickListener(null);
//        String str = aesCryptUtil.decrypt("hdpxUvAjTfNO7z7g6WU9EScmSJuDU4zUXeNeosW5whHvcgMVWOHfe79FV/2bQ8CG10gEX5JXc0CMbi1qxOAndnqF6E8EMZlPVHcILEkSJmE=");
//        LogUtil.e("=======" + str);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                final String code = inputCode.getText().toString();
                registerPhone = inputPhonenumber.getText().toString().trim();
//                if (NetworkUtil.isNetworkConnected(getActivity())) {
                if (TextUtils.isEmpty(registerPhone)) {
                    ToastUtil.show("手机号不能为空");
                    inputPhonenumber.requestFocus();
                    return;
                }
                if (registerPhone.length() != 11) {
                    ToastUtil.show("手机号必须是11位");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.show("请输入验证码!");
                    inputCode.requestFocus();
                    return;
                }
                checkCode(registerPhone, code, null, null);

//                } else {
//                    ToastUtil.show(PasserbyClient.NETWORK_ERROR_MESSAGE);
//                }

                break;
            case R.id.request_code:
                registerPhone = inputPhonenumber.getText().toString().trim();
                if (checkMobileLocally(registerPhone)) {
                    getUserCode(registerPhone);
                    launchBroadcast();
                    return;
//                    } else {
//                        ToastUtil.show("请检查网络连接!");
//                    }
                } else if (TextUtils.isEmpty(registerPhone)) {
                    ToastUtil.show("手机号不能为空");
                    inputPhonenumber.requestFocus();
                } else {
                    ToastUtil.show("手机号必须是11位");
                    inputPhonenumber.requestFocus();
                }
                break;
            case R.id.register_deal:
                startActivity(new Intent(RegisterActivity.this, TermsOfServiceActivity.class));
                break;
//            case R.id.qq_login_icon:
//            case R.id.sina_login_icon:
            case R.id.weichat_login_icon:
                SHARE_MEDIA platform = SHARE_MEDIA.values()[(Integer) v.getTag()];
                UmengSnsUtil.authorize(this, platform, new SocializeListeners.UMAuthListener() {
                    @Override
                    public void onError(SocializeException e, SHARE_MEDIA platform) {
                        ToastUtil.show("授权失败");
                    }

                    @Override
                    public void onComplete(final Bundle value, final SHARE_MEDIA platform) {
                        if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                            ProgressUtil.show(RegisterActivity.this, "登录验证中...");
                            ToastUtil.show("授权完成");
                            UmengSnsUtil.getUserInfo(RegisterActivity.this, platform,
                                    new SocializeListeners.UMDataListener() {
                                        @Override
                                        public void onStart() {
                                        }

                                        @Override
                                        public void onComplete(int status, final Map<String, Object> info) {
                                            if (status == 200 && info != null) {
//                                                NetApi.snsLogin(platform, value, info, loginCallback);
                                            } else {
                                                ProgressUtil.dismiss();
                                                ToastUtil.show("授权失败");
                                            }
                                        }
                                    });
                        } else {
                            ToastUtil.show("授权失败");
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA platform) {
                        ToastUtil.show("授权取消");
                    }

                    @Override
                    public void onStart(SHARE_MEDIA platform) {
                        // ToastUtil.show("授权开始");
                    }
                });
                break;
        }
    }

    private void checkCode(final String registerPhone, String code, String username, String pwd) {
        NetApi.getRegisterCheckCode(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("登录请求成功" + result);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,RegisterActivity.this)){
                    return;
                }
                if (!TextUtils.isEmpty(result)) {
                    RegisterBean register = JsonUtil.json2Bean(result, RegisterBean.class);
                    String code = register.code;
                    data = register.data;
                    if ("0".equals(code)) {
                        SharedPreferencesUtil.saveString("token", data.token);
                        SharedPreferencesUtil.saveString("userid", data.userid);
                        SharedPreferencesUtil.saveString("phone", registerPhone);
                        SharedPreferencesUtil.saveString("username", data.username);

                        SharedPreferencesUtil.saveString("face", data.face);// 头像
                        SharedPreferencesUtil.saveString("alias", data.alias);// 用户昵称
                        SharedPreferencesUtil.saveString("isfamilymember", data.isfamilymember);// 亲情会员

//                        ToastUtil.show("登录成功");

                        Intent intent = new Intent();
                        intent.putExtra("face", data.face == null || TextUtils.isEmpty(data.face) ? "" : data.face);
                        setResult(RESULT_OK, intent);

//                        Intent intent2 = new Intent(RegisterActivity.this, MainActivity.class);
//                        startActivity(intent2);
                        finish();
                    } else {
                        ToastUtil.show(register.msg);
                    }

                }


//                if (!TextUtils.isEmpty(result)) {
//                    Body checkCode = JsonUtil.json2Bean(
//                            result, Body.class);
//                    if (checkCode.code.equals("1")) {
//                        // Intent intent = new Intent(getActivity(),
//                        // RegisterPasswordFragment.class);
//                        // intent.putExtra("phoneNumber", registerPhone);
//                        // startActivity(intent);
//                        RegisterPasswordFragment.show(getActivity(), registerPhone);
//                    } else {
//                        ToastUtil.show(checkCode.message);
//                    }
//                }

            }

            @Override
            public void onFailure(String message) {
                LogUtil.e("登录请求失败" + message);
            }
        }, registerPhone, code, username, pwd);
    }

    private boolean checkMobileLocally(String mobileNumber) {
        return !TextUtils.isEmpty(mobileNumber) && mobileNumber.length() == 11;
    }

    private void getUserCode(String registerPhone) {
        startSendingVerifyCodeCD();
        NetApi.getUserCode(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getUserCode=" + result);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,RegisterActivity.this)){
                    return;
                }
                sendVerifyCodeCountDown.start();

//                VerifyCode verifyCode = JsonUtil
//                        .json2Bean(result, VerifyCode.class);
//                if (!"1".equals(verifyCode.code)) {
//                    resetCD();
//                }
//                ToastUtil.show(verifyCode.message);
            }

            @Override
            public void onFailure(String message) {
                resetCD();
            }
        }, registerPhone);
    }

    private void startSendingVerifyCodeCD() {
        sendVerifyCodeCountDown.cancel();
//        sendVerifyCodeCountDown.start();
        String requesContent = requestCode.getText().toString().trim();
        if(requesContent.equals("获取验证码")){
            requestCode.setEnabled(true);
        }else {
            requestCode.setEnabled(false);
        }
    }

    private void resetCD() {
        sendVerifyCodeCountDown.cancel();
        requestCode.setTextColor(getResources().getColor(R.color.carminum));
//        requestCode.setBackgroundResource(R.drawable.input_phone_number);
        requestCode.setEnabled(true);
        requestCode.setText("重获验证码");
    }

    private void resetCDWrong() {
        sendVerifyCodeCountDown.cancel();
        requestCode.setEnabled(true);
        requestCode.setText("获取验证码");
    }

    private CountDownTimer sendVerifyCodeCountDown = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            requestCode.setTextColor(getResources().getColor(R.color.french_grey));
//            requestCode.setBackgroundResource(R.drawable.get_code_later);
            requestCode.setText("重新发送(" + ((int) millisUntilFinished / 1000) + ")");
        }

        @Override
        public void onFinish() {
            requestCode.setTextColor(getResources().getColor(R.color.carminum));
//            requestCode.setBackgroundResource(R.drawable.input_phone_number);
            requestCode.setEnabled(true);
            requestCode.setText("重获验证码");
        }
    };

//    private PasserbyClient.HttpCallback loginCallback = new PasserbyClient.HttpCallback() {
//        @Override
//        public void onSuccess(String result) {
//            ProgressUtil.dismiss();
//            Login login = JsonUtil.json2Bean(result, Login.class);
//            if (login.code.equals("1")) {
//                Login.UserInfo userInfo = login.userinfo;
//                SharedPreferencesUtil.saveString("uid", userInfo.id);
//                SharedPreferencesUtil.saveString(
//                        "nickname", userInfo.nickname);
//                SharedPreferencesUtil.saveString(
//                        "platid", userInfo.platid);
//                SharedPreferencesUtil.saveString(
//                        "gender", userInfo.sex);
//                SharedPreferencesUtil.saveString(
//                        "province", userInfo.province);
//                SharedPreferencesUtil.saveString(
//                        "city", userInfo.city);
//                SharedPreferencesUtil.saveString(
//                        "district", userInfo.district);
//                SharedPreferencesUtil.saveString(
//                        "birthday", userInfo.birthyear + "-" + userInfo.birthmonth + "-"
//                                + userInfo.birthday);
//                SharedPreferencesUtil.saveString(
//                        "token", userInfo.token);
//                SharedPreferencesUtil.saveBoolean(
//                        "isLogin", true);
//                SharedPreferencesUtil.saveString(
//                        "phoneNumber", inputPhonenumber.getText()
//                                .toString());
//                SharedPreferencesUtil.saveString(
//                        "avatar",
//                        userInfo.headpath.W180);
//                getActivity().finish();
//                return;
//
//            }
//        }
//
//        @Override
//        public void onFailure(String message) {
//            ProgressUtil.dismiss();
//        }
//    };

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        sendVerifyCodeCountDown.cancel();
//    }

    private void launchBroadcast() {
        if (TelephonyUtil.isCanUseSim()) {
            mSMSBroadcastReceiver = new SMSBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter(ACTION);
            intentFilter.setPriority(Integer.MAX_VALUE);
            registerReceiver(mSMSBroadcastReceiver, intentFilter);
            mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
                @Override
                public void onReceived(String message) {
                    inputCode.setText(message);
                    unregisterReceiver(mSMSBroadcastReceiver);
                }
            });
        }
    }

}
