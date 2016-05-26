package com.android.linglan.ui.wxapi;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onReq(BaseReq req) {
        super.onReq(req);
    }

    //微信回调
    @Override
    public void onResp(BaseResp resp) {   //分享之后的回调
        switch ( resp.errCode  ) {
            case  BaseResp.ErrCode.ERR_OK : //正确返回
                Toast.makeText(this, "微信分享成功", Toast.LENGTH_SHORT).show();
                break;
        }
        super.onResp(resp);
    }
}
//public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
//    private IWXAPI api;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        api = WXAPIFactory.createWXAPI(this, Constants.WEICHAT_APP_ID);
//        api.handleIntent(getIntent(), this);
//
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        api.handleIntent(intent, this);
//    }
//
//    @Override
//    public void onReq(BaseReq arg0) {
//
//    }
//
//    @Override
//    public void onResp(BaseResp resp) {
//        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            System.out.println("resp.errCode==="+resp.errCode);
//        }
//    }
//
//}
