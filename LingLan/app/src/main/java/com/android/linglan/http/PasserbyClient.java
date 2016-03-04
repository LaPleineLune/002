package com.android.linglan.http;

import android.content.Context;
import android.service.carrier.CarrierMessagingService;
import android.text.TextUtils;
import android.util.Log;

import com.android.linglan.LinglanApplication;
import com.android.linglan.http.bean.CommonProtocol;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.NetworkUtil;
import com.android.linglan.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by changyizhang on 12/10/14.
 */
public class PasserbyClient {

  private static boolean invalidToken;

  public static String key;
  public static String time;

  private static final String TAG = PasserbyClient.class.getSimpleName();
  private static final String OS = "Linux";
  private static final String OS_TYPE = "Android";

  public static final String NETWORK_ERROR_MESSAGE = "网络在旁观...";
  private static AsyncHttpClient client = new AsyncHttpClient();

  public static AsyncHttpClient getClient() {
    return client;
  }

  private static String getCompleteUrl(String url, boolean appendDefaultParams, Object... params) {
    url = String.format(url, params);
//    if (appendDefaultParams) {
//      url = appendDefaultGetParams(url);
//    }
    return url;
  }

  private static String postCompleteUrl(String url, boolean appendDefaultParams) {
//    if (appendDefaultParams) {
//      url = appendDefaultPostParams(url);
//    }
    return url;
  }

  public static void get(final String url, final HttpCallback callback, Object... params) {
    get(url, true, callback, params);
  }

  public static void get(final String url, boolean appendDefaultParams,
      final HttpCallback callback, Object... params) {
    if (!NetworkUtil.isNetworkConnected(LinglanApplication.getsApplicationContext())) {
      ToastUtil.show(PasserbyClient.NETWORK_ERROR_MESSAGE);
      if (callback != null) {
        callback.onFailure(NETWORK_ERROR_MESSAGE);
      }
      return;
    }
    final String completeUrl = getCompleteUrl(url, appendDefaultParams, params);
    client.get(completeUrl, null,
            new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                      Throwable throwable) {
                    Log.w("Request Failed: " + completeUrl, "Network error: " + responseString);
                    ToastUtil.show(NETWORK_ERROR_MESSAGE);
                    try {
                        if (callback != null) {
                            callback.onFailure(TextUtils.isEmpty(responseString)
                                    ? "Network error."
                                    : responseString);
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        Log.w(TAG, exception.toString());
                    }
                }

                @Override
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                    Log.e(TAG, "Request Finished| " + completeUrl + " : " + responseString);

                    if (callback != null) {
                        callback.onSuccess(responseString);
                    }

//            if (!TextUtils.isEmpty(responseString)) {
//                  try {
//                    CommonProtocol result = JsonUtil.json2Bean(responseString, CommonProtocol.class);
//                    if ("0".equals(result.code)) {
////                      invalidToken = false;
//                      if (callback != null) {
////                        callback.onSuccess(new JSONObject(responseString).optString("data"));
//                          callback.onSuccess(responseString);
//                      }
//                      return;
//                    } else if ("1".equals(result.code)) {// 没有数据
//                     ToastUtil.show("暂无数据");
//                      return;
//                    }else if ("2".equals(result.code)) {// 需要登录，可以提示未登录，提示用户登录
//                        ToastUtil.show("需要登录");
//                        return;
//                    }else if ("3".equals(result.code)) {// 登录超时，需要重新登录，提示用户登录
//                        ToastUtil.show("登录超时");
//                        return;
//                    }else if ("4".equals(result.code)) {//登录异常
//                        ToastUtil.show("登录异常");
//                        return;
//                    }else if ("6".equals(result.code)) {//token为空，需要重新get token
//                        ToastUtil.show("token为空");
//                        return;
//                    }else if ("701".equals(result.code)) {//没有权限，需要登录
//                        ToastUtil.show("没有权限，需要登录");
//                        return;
//                    }else if ("702".equals(result.code)) {//没有权限，需升级会员
//                        ToastUtil.show("没有权限，需升级会员");
//                        return;
//                    }else if ("702".equals(result.code)) {//没有权限，需升级会员
//                        ToastUtil.show("没有权限，需升级会员");
//                        return;
//                    }else if ("10001".equals(result.code)) {//系统异常
//                        ToastUtil.show("系统异常");
//                        return;
//                    }else if ("11001".equals(result.code)) {//系统异常
//                        ToastUtil.show("业务异常");
//                        return;
//                    }
//
//                    if (callback != null) {
//                      callback.onFailure(responseString);
//                    }
//                  } catch (Exception exception) {
//                    exception.printStackTrace();
//                    Log.w(TAG, exception.toString());
//
//                    onFailure(statusCode, headers, responseString, null);
//                  }
//            }
        }
    });
  }

  public static void post(final String url, RequestParams params,
      final HttpCallback callback) {
    post(url, params, true, callback);
  }

  public static void post(final String url, RequestParams params, boolean appendDefaultParams,
      final HttpCallback callback) {
    if (!NetworkUtil.isNetworkConnected(LinglanApplication.getsApplicationContext())) {
      ToastUtil.show(PasserbyClient.NETWORK_ERROR_MESSAGE);
      if (callback != null) {
        callback.onFailure(NETWORK_ERROR_MESSAGE);
      }
      return;
    }

    final String completeUrl = postCompleteUrl(url, appendDefaultParams);
    Log.d("Post request", "URL: " + completeUrl + " | with post data: " + params.toString());
//    client.post(completeUrl, params, new TextHttpResponseHandler() {
    client.post(completeUrl, params, new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                              Throwable throwable) {
            Log.w("Post Request Failed: " + completeUrl, "Network error: " + responseString);
            ToastUtil.show(NETWORK_ERROR_MESSAGE);
            try {
                if (callback != null) {
                    callback.onFailure(TextUtils.isEmpty(responseString)
                            ? "Network error."
                            : responseString);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                Log.w(TAG, exception.toString());
            }
        }

        @Override
        public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
            Log.i(TAG, "Post Request Finished| " + completeUrl + " : " + responseString);
            if (callback != null) {
                callback.onSuccess(responseString);
            }
//        if (!TextUtils.isEmpty(responseString)) {
//          try {
//            CommonProtocol result = JsonUtil.json2Bean(responseString, CommonProtocol.class);
//            if ("1".equals(result.header.status)) {
//              invalidToken = false;
//
//              if (callback != null) {
//                callback.onSuccess(responseString);
//              }
//              return;
//            }
//            else if ("6".equals(result.header.status)) {
//              if (!invalidToken) {
//                /*ntent intent = new Intent(PasserbyApplication.getsApplicationContext(), AuthenticationActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                PasserbyApplication.getsApplicationContext().startActivity(intent);*/
//                AuthenticationActivity.show(LinglanApplication.getsApplicationContext());
//                invalidToken = true;
//              }

//              return;
//            }
//            if (callback != null) {
//              callback.onFailure(responseString);
//            }
//          } catch (Exception exception) {
//            exception.printStackTrace();
//            Log.w(TAG, exception.toString());
//            onFailure(statusCode, headers, responseString, null);
//          }
//        }

        /*
         * ToastUtil.show(NETWORK_ERROR_MESSAGE);
         * try {
         * if (callback != null) {
         * callback.onFailure(responseString);
         * }
         * } catch (Exception exception) {
         * exception.printStackTrace();
         * Log.w(TAG, exception.toString());
         * }
         */
        }
    });

    /*
     * String json = toEntityJson(parameters);
     * if (!TextUtils.isEmpty(json)) {
     * StringEntity entity = null;
     * try {
     * if (json != null) {
     * entity = new StringEntity(json, "UTF-8");
     * Log.d(TAG, "URL: " + url + " | with params: " + parameters.toString());
     * client.pos
     * client.post(null, url, entity, "application/json", new TextHttpResponseHandler() {
     * @Override
     * public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
     * throwable) {
     * Log.w("Request Failed: " + url, "Network error: " + responseString);
     * ToastUtil.show(NETWORK_ERROR_MESSAGE);
     * try {
     * if (callback != null) {
     * callback.onFailure(TextUtils.isEmpty(responseString)? "Network error." : responseString);
     * }
     * } catch (Exception exception) {
     * exception.printStackTrace();
     * Log.w(TAG, exception.toString());
     * }
     * }
     * @Override
     * public void onSuccess(int statusCode, Header[] headers, String responseString) {
     * Log.i(TAG, "Request Finished| " + url + " : " + responseString);
     * if (!TextUtils.isEmpty(responseString)) {
     * try {
     * CommonProtocol result = GsonUtil.json2Bean(responseString, CommonProtocol.class);
     * if ( "1".equals(result.header.status)) {
     * if (callback != null) {
     * callback.onSuccess(new JSONObject(responseString).optString("body"));
     * }
     * return;
     * }
     * } catch (Exception exception) {
     * exception.printStackTrace();
     * Log.w(TAG, exception.toString());
     * }
     * }
     * ToastUtil.show(NETWORK_ERROR_MESSAGE);
     * try {
     * if (callback != null) {
     * callback.onFailure(responseString);
     * }
     * } catch (Exception exception) {
     * exception.printStackTrace();
     * Log.w(TAG, exception.toString());
     * }
     * }
     * });
     * return;
     * }
     * } catch (UnsupportedEncodingException ex) {
     * ex.printStackTrace();
     * if (callback != null) {
     * callback.onFailure("UnsupportedEncodingException");
     * }
     * }
     * }
     */
  }

  private static String toEntityJson(Map<String, Object> params) {
    Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
    String ret = null;
    if (params == null)
      return null;
    if (0 < params.size()) {
      ret = new Gson().toJson(params, mapType);
    }
    return ret;
  }

  /*public static String appendDefaultPostParams(String url) {
    if(TextUtils.isEmpty(GloableParams.versionName)){
      GloableParams.versionName = PackageUtil
              .getVersion();
    }
    if(TextUtils.isEmpty(GloableParams.deviceID)){
      GloableParams.deviceID = TelephonyUtil
              .getUniqueDeviceId();
    }
    return url + "&devid=" + GloableParams.deviceID + "&pcode="
            + Constants.SPCODE + "&version="
            + GloableParams.versionName;
  }*/

 /* public static String appendDefaultGetParams(String url) {
    if(TextUtils.isEmpty(GloableParams.versionName)){
      GloableParams.versionName = PackageUtil
              .getVersion();
    }
    if(TextUtils.isEmpty(GloableParams.deviceID)){
      GloableParams.deviceID = TelephonyUtil
              .getUniqueDeviceId();
    }
    return url + "&devid=" + GloableParams.deviceID + "&pcode="
        + Constants.SPCODE + "&version="
        + GloableParams.versionName + "&token="
        + SharedPreferencesUtil.getString("token", "");
  }*/

  public static interface HttpCallback {
    public void onSuccess(String result);

    public void onFailure(String message);
  }

}
