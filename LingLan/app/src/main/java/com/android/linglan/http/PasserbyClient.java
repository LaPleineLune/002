package com.android.linglan.http;

import android.text.TextUtils;
import android.util.Log;

import com.android.linglan.LinglanApplication;
import com.android.linglan.utils.AESCryptUtil;
import com.android.linglan.utils.NetworkUtil;
import com.android.linglan.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.net.URLEncoder;

/**
 * Created by changyizhang on 12/10/14.
 */
public class PasserbyClient {
    private static AESCryptUtil aesCryptUtil = new AESCryptUtil();

    private static final String TAG = PasserbyClient.class.getSimpleName();

    public static final String NETWORK_ERROR_MESSAGE = "网络不太好，休息一会...";
    private static AsyncHttpClient client = new AsyncHttpClient();

    private static String getCompleteUrl(String url, boolean appendDefaultParams, Object... params) {
        url = String.format(url, params);
        return url;
    }

    private static String postCompleteUrl(String url, boolean appendDefaultParams) {
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
//        final String completeUrl = getCompleteUrl(url, appendDefaultParams, params);
        client.get(url, null,
                new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                          Throwable throwable) {
                        Log.w("Request Failed: " + url, "Network error: " + responseString);
                        ToastUtil.show(NETWORK_ERROR_MESSAGE);
                        try {
                            if (callback != null) {
                                callback.onFailure(TextUtils.isEmpty(responseString)
                                        ? "Network error."
//                                    : responseString);
                                        : aesCryptUtil.decrypt(responseString));
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Log.w(TAG, exception.toString());
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                        Log.e(TAG, "Request Finished| " + url + " : " + responseString);
                        Log.e(TAG,  responseString);
                        Log.e(TAG,  aesCryptUtil.decrypt(responseString));

                        if (callback != null) {
//                        callback.onSuccess(responseString);
                            callback.onSuccess(aesCryptUtil.decrypt(responseString));
                        }
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
        client.post(completeUrl, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                  Throwable throwable) {
                Log.e("Post Request Failed: " + completeUrl, "Network error: " + responseString);
                ToastUtil.show(NETWORK_ERROR_MESSAGE);
                try {
                    if (callback != null) {
                        callback.onFailure(TextUtils.isEmpty(responseString)
                                ? "Network error."
//                            : responseString);
                                : aesCryptUtil.decrypt(responseString));
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Log.w(TAG, exception.toString());
                }
            }

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, String responseString) {
                Log.e(TAG, "Post Request Finished| " + completeUrl + " : " + responseString);
                Log.e(TAG, "Post Request Finished| " + completeUrl + " : " + aesCryptUtil.decrypt(responseString));
                if (callback != null) {
//                callback.onSuccess(responseString);
                    callback.onSuccess(aesCryptUtil.decrypt(responseString));
                }
            }
        });

    }

    public static interface HttpCallback {
        public void onSuccess(String result);

        public void onFailure(String message);
    }

}
