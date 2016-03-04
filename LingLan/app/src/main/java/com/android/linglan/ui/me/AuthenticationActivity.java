package com.android.linglan.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.RegisterFragment;
import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/1/7 0007.
 * 作用：跳转到fragment登录界面
 */
public class AuthenticationActivity extends BaseActivity {

  public final static String REGISTER = "1";
  public final static String LOGIN = "2";

  public static void show(Context context) {
    show(context, LOGIN);
  }

  public static void show(Context context, String type) {
    Intent intent = new Intent(context, AuthenticationActivity.class);
    intent.putExtra("type", type);
//    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    context.startActivity(intent);
  }

  @Override
  protected void setView() {
    setContentView(R.layout.activity_authentication);
  }

  @Override
  protected void initView() {

  }

  @Override
  protected void initData() {
    getIntentData();
  }

  @Override
  protected void setListener() {

  }

  public void getIntentData() {
//    Intent intent = getIntent();
//    String type = intent.getStringExtra("type");
//    LoginFragment.show(this);
//    if (REGISTER.equals(type)) {
      RegisterFragment.show(this);
//    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

//    UMSsoHandler ssoHandler = SnsUtil.getAuthController().getConfig().getSsoHandler(requestCode);
//    if (ssoHandler != null) {
//      ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//    }
  }
}
