package com.android.linglan.fragment;

import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.base.BaseFragment;
import com.android.linglan.broadcast.SMSBroadcastReceiver;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.TelephonyUtil;
import com.android.linglan.utils.ToastUtil;

/**
 * Created by LeeMy on 2016/1/7 0007.
 * 注册界面(现用作登录界面)
 */
public class RegisterFragment extends BaseFragment {

    private View rootView;

    private RelativeLayout back;

    private EditText inputPhonenumber;
    private EditText inputCode;
    private Button requestCode;
    private Button submit;
    private TextView registerDeal;
    private String registerPhone;
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public static void show(FragmentActivity activity) {
        Fragment fragment = new RegisterFragment();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_register, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {
        back = (RelativeLayout) rootView.findViewById(R.id.back);
        inputPhonenumber = (EditText) rootView.findViewById(R.id.input_phonenumber);
        inputCode = (EditText) rootView.findViewById(R.id.input_code);
        requestCode = (Button) rootView.findViewById(R.id.request_code);
        submit = (Button) rootView.findViewById(R.id.submit);
        registerDeal = (TextView) rootView.findViewById(R.id.register_deal);
//        View snsButton = rootView.findViewById(R.id.qq_login_icon);
//        snsButton.setTag(SHARE_MEDIA.QQ.ordinal());
//        snsButton.setOnClickListener(this);
//        snsButton = rootView.findViewById(R.id.sina_login_icon);
//        snsButton.setTag(SHARE_MEDIA.SINA.ordinal());
//        snsButton.setOnClickListener(this);
//        snsButton = rootView.findViewById(R.id.weichat_login_icon);
//        snsButton.setTag(SHARE_MEDIA.WEIXIN.ordinal());
//        snsButton.setOnClickListener(this);
//        SnsUtil.init(getActivity());

    }

    @Override
    protected void initData() {
//        setTitle("注册", "");
//        setTitle("登录", "");
    }

    @Override
    protected void setListener() {
        requestCode.setOnClickListener(this);
        submit.setOnClickListener(this);
        registerDeal.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
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
                    checkCode(code, registerPhone);
//                } else {
//                    ToastUtil.show(PasserbyClient.NETWORK_ERROR_MESSAGE);
//                }

                break;
            case R.id.request_code:
                registerPhone = inputPhonenumber.getText().toString().trim();
                if (checkMobileLocally(registerPhone)) {
//                    if (NetworkUtil.isNetworkConnected(getActivity())) {
                        sendVerifyCode(registerPhone);
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
//                UserProtocolDelegate.showWebView(getActivity());
                break;
            case R.id.qq_login_icon:
            case R.id.sina_login_icon:
            case R.id.weichat_login_icon:
//                SHARE_MEDIA platform = SHARE_MEDIA.values()[(Integer) v.getTag()];
//                SnsUtil.authorize(getActivity(), platform, new SocializeListeners.UMAuthListener() {
//                    @Override
//                    public void onError(SocializeException e, SHARE_MEDIA platform) {
//                        ToastUtil.show("授权失败");
//                    }
//
//                    @Override
//                    public void onComplete(final Bundle value, final SHARE_MEDIA platform) {
//                        if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
//                            ProgressUtil.show(getActivity(), "登录验证中...");
//                            ToastUtil.show("授权完成");
//                            SnsUtil.getUserInfo(getActivity(), platform,
//                                    new SocializeListeners.UMDataListener() {
//                                        @Override
//                                        public void onStart() {}
//
//                                        @Override
//                                        public void onComplete(int status, final Map<String, Object> info) {
//                                            if (status == 200 && info != null) {
//                                                NetApi.snsLogin(platform, value, info, loginCallback);
//                                            } else {
//                                                ProgressUtil.dismiss();
//                                                ToastUtil.show("授权失败");
//                                            }
//                                        }
//                                    });
//                        } else {
//                            ToastUtil.show("授权失败");
//                        }
//                    }
//
//                    @Override
//                    public void onCancel(SHARE_MEDIA platform) {
//                        ToastUtil.show("授权取消");
//                    }
//
//                    @Override
//                    public void onStart(SHARE_MEDIA platform) {
//                        // ToastUtil.show("授权开始");
//                    }
//                });
                break;
        }
    }

    private void checkCode(String code, final String registerPhone) {
//        NetApi.getRegisterCheckCode(code, registerPhone, new PasserbyClient.HttpCallback() {
//            @Override
//            public void onSuccess(String result) {
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
//            }
//
//            @Override
//            public void onFailure(String message) {
//
//            }
//        });
    }

    private boolean checkMobileLocally(String mobileNumber) {
        return !TextUtils.isEmpty(mobileNumber) && mobileNumber.length() == 11;
    }

    private void sendVerifyCode(String registerPhone) {
        startSendingVerifyCodeCD();
//        NetApi.getRegisterCodeResult(registerPhone, new PasserbyClient.HttpCallback() {
//            @Override
//            public void onSuccess(String result) {
//                VerifyCode verifyCode = JsonUtil
//                        .json2Bean(result, VerifyCode.class);
//                if (!"1".equals(verifyCode.code)) {
//                    resetCD();
//                }
//                ToastUtil.show(verifyCode.message);
//            }
//
//            @Override
//            public void onFailure(String message) {
//                resetCD();
//            }
//        });
    }

    private void startSendingVerifyCodeCD() {
        sendVerifyCodeCountDown.cancel();
        sendVerifyCodeCountDown.start();
        requestCode.setEnabled(false);
    }

    private void resetCD() {
        sendVerifyCodeCountDown.cancel();
        requestCode.setTextColor(0xfffe6829);
        requestCode.setBackgroundResource(R.drawable.input_phone_number);
        requestCode.setEnabled(true);
        requestCode.setText("重新发送验证码");
    }

    private void resetCDWrong() {
        sendVerifyCodeCountDown.cancel();
        requestCode.setEnabled(true);
        requestCode.setText("免费获取验证码");
    }

    private CountDownTimer sendVerifyCodeCountDown = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            requestCode.setTextColor(Color.GRAY);
            requestCode.setBackgroundResource(R.drawable.get_code_later);
            requestCode.setText(((int) millisUntilFinished / 1000) + "秒后重新发送");
        }

        @Override
        public void onFinish() {
            requestCode.setTextColor(0xfffe6829);
            requestCode.setBackgroundResource(R.drawable.input_phone_number);
            requestCode.setEnabled(true);
            requestCode.setText("重新发送验证码");
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sendVerifyCodeCountDown.cancel();
    }
    private void launchBroadcast(){
        if(TelephonyUtil.isCanUseSim()){
            mSMSBroadcastReceiver = new SMSBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter(ACTION);
            intentFilter.setPriority(Integer.MAX_VALUE);
            getActivity().registerReceiver(mSMSBroadcastReceiver, intentFilter);
            mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
                @Override
                public void onReceived(String message) {
                    inputCode.setText(message);
                    getActivity().unregisterReceiver(mSMSBroadcastReceiver);
                }
            });
        }
    }
    //获取服务条款
    private void getAppAgreement(){
        NetApi.getAppAgreement(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result=" + result);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    //获得验证码
    private void getUserCode(String phone){
        NetApi.getUserCode(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result=" + result);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
            }

            @Override
            public void onFailure(String message) {

            }
        },phone);
    }

}
