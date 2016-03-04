package com.android.linglan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.RecommendArticles;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StartActivity extends ActionBarActivity {
    private TextView splash;
    private RecommendArticles recommendArticles;
    private RecommendSubjects recommendSubjects;
    public ArrayList<RecommendArticles.RecommendArticle> ArticlesData;
    public ArrayList<RecommendSubjects.RecommendSubject> SubjectsData;

    protected static final int REQUEST_VISIT_TOKEN = 0;
    protected static final int REQUEST_SUCCESS = 1;
    protected static final int REQUEST_FAIL = 2;
    private boolean isSuccessArticles;
    private boolean isSuccessSubjects;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_VISIT_TOKEN:
//                    getRecommendSubject();
//                    getRecommendArticle("1");
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    intent.putExtra("ArticlesData", ArticlesData);
                    intent.putExtra("SubjectsData", SubjectsData);
                    startActivity(intent);
                    finish();
                break;
                case REQUEST_SUCCESS:
                    if (isSuccessArticles && isSuccessSubjects) {
//                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
//                        intent.putExtra("ArticlesData", ArticlesData);
//                        intent.putExtra("SubjectsData", SubjectsData);
//                        startActivity(intent);
//                        finish();
                    } else {
                        splash.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                intent.putExtra("ArticlesData", ArticlesData);
                                intent.putExtra("SubjectsData", SubjectsData);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    break;
                case REQUEST_FAIL:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        splash = (TextView) findViewById(R.id.tv_splash);
        String token2 =
                (SharedPreferencesUtil.getString("token", null) != null) ? // token 的值为null 吗？
                SharedPreferencesUtil.getString("token", null) : // 不为空则为token 的值
                SharedPreferencesUtil.getString("visittoken", null);// 为空则为 visittoken 的值
        LogUtil.e("token 的值：" + token2);
        String visittoken = SharedPreferencesUtil.getString("visittoken", null);
        String token = SharedPreferencesUtil.getString("token", null);
        isSuccessArticles = false;
        isSuccessSubjects = false;
        if (visittoken == null && token == null) {
            getVisitToken();
//            getRecommendSubject();
//            getRecommendArticle("1");
        }else{
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            intent.putExtra("ArticlesData", ArticlesData);
            intent.putExtra("SubjectsData", SubjectsData);
            startActivity(intent);
            finish();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//R.menu.menu_main
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
   * 获取推荐文章
   * */
    private void getRecommendArticle(String page) {
        NetApi.getRecommendArticle(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
//                LogUtil.e(result);
                recommendArticles = JsonUtil.json2Bean(result, RecommendArticles.class);
                ArticlesData = recommendArticles.data;
//                    LogUtil.e(recommendArticles.data.get(0).title);

                if (ArticlesData != null && !ArticlesData.equals("")) {
//                    homePageAdapter.insertArticlesData(ArticlesData);
                    isSuccessArticles = true;
                    handler.sendEmptyMessage(REQUEST_SUCCESS);
                    for (RecommendArticles.RecommendArticle recommendArticle : ArticlesData) {
                        LogUtil.e(recommendArticle.toString());
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                LogUtil.e(message);
            }
        },page);
    }

    /*
   * 获取推荐专题
   * */
    private void getRecommendSubject() {
        NetApi.getRecommendSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
//                LogUtil.e(result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }

                recommendSubjects = JsonUtil.json2Bean(result, RecommendSubjects.class);
                SubjectsData = recommendSubjects.data;

//                    LogUtil.e(recommendArticles.data.get(0).title);

                if (SubjectsData != null && !SubjectsData.equals("")) {
//                    homePageAdapter.insertSubjectData(SubjectsData);
                    isSuccessSubjects = true;
                    handler.sendEmptyMessage(REQUEST_SUCCESS);
                    for (RecommendSubjects.RecommendSubject recommendSubject : SubjectsData) {
                        LogUtil.e(recommendSubject.toString());
                    }
                }


//                ToastUtil.show(result1);
            }

            @Override
            public void onFailure(String message) {
                LogUtil.e(message);
            }
        });
    }

    private void getVisitToken() {
        NetApi.getVisitToken(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("获取游客token" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }

                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject json = new JSONObject(result);
                        if (json.getString("code").equals("0")) {
                            SharedPreferencesUtil.saveString("visittoken", json.getJSONObject("data").getString("token"));
                            LogUtil.e("获取游客token保存成功" + json.getJSONObject("data").getString("token"));
                            handler.sendEmptyMessage(REQUEST_VISIT_TOKEN);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
