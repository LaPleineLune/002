package com.android.linglan.ui.homepage;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.linglan.adapter.SearchAdapter;
import com.android.linglan.adapter.SearchAllAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllSearchListBean;
import com.android.linglan.http.bean.HistorySearcherBean;
import com.android.linglan.http.bean.HotAndHistorySearcherBean;
import com.android.linglan.http.bean.HotSearcherBean;
import com.android.linglan.http.bean.SearchArticleBean;
import com.android.linglan.http.bean.SearchSubjectBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.SyLinearLayoutManager;
import com.android.linglan.widget.sortlistview.ClearEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    protected static final int ALL_REQUEST_SUCCESS = 2;
    private int SEARCHFLAG = 0;
    private LinearLayout ll_no_network,ll_search;
    private ClearEditText filter_edit;
    private RecyclerView rec_search;
    private SearchAdapter searchAdapter;
    private HotAndHistorySearcherBean hotAndHistorySearcherBean;
    private String[] hotsearch;
    public String[] historysearch;
    private AllSearchListBean allSearchListBean;
    private ArrayList<SearchArticleBean.ArticleClassifyListBean> article;
    private ArrayList<SearchSubjectBean.SubjectClassifyListBean> special;
    private SearchArticleBean searchArticleBean;
    private SearchSubjectBean searchSubjectBean;
    private HistorySearcherBean historySearcherBean;
    private HotSearcherBean hotSearcherBean;

    private SearchAllAdapter allArticleAdapter;
    private RecyclerView rec_all_search;
    private Button btn_search;

    private int page;
    private String key;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ALL_REQUEST_SUCCESS:
                    rec_search.setVisibility(View.GONE);
                    rec_all_search.setVisibility(View.VISIBLE);
                    if ((filter_edit.getText().toString().trim()) == null || (filter_edit.getText().toString().trim()).equals("")) {
                        if(key != null && key.length() != 0){
                            filter_edit.setText(key);
                            filter_edit.setSelection(key.length());
                        }
                    }

                    ll_search.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_SUCCESS:
//                    LogUtil.d("第二次" + data.toString());
                    ll_search.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    ll_search.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

//    @Override
//    public void onResume() {
//        super.onResume();
//        getHistoryHotSearchKey();
//    }

    @Override
    protected void setView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initView() {
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        filter_edit = (ClearEditText) findViewById(R.id.filter_edit);
        btn_search = (Button) findViewById(R.id.btn_search);
        rec_search = (RecyclerView) findViewById(R.id.rec_search);
        rec_all_search = (RecyclerView) findViewById(R.id.rec_all_search);
        rec_all_search.setLayoutManager(new SyLinearLayoutManager(this));
        rec_all_search.setHasFixedSize(true);
        allArticleAdapter = new SearchAllAdapter(this);
        rec_all_search.setAdapter(allArticleAdapter);

        page = 1;
    }

    @Override
    protected void initData() {
        setTitle("搜索", "");
//        SEARCHFLAG = (int) getIntent().getExtras().get("searchEdit");
        filter_edit.setHint("搜索文章、专题、作者");
        //热门搜索，历史搜索，填充数据
        getHistoryHotSearchKey();

        rec_search.setLayoutManager(new SyLinearLayoutManager(this));
        rec_search.setHasFixedSize(true);
        searchAdapter = new SearchAdapter(this);
        rec_search.setAdapter(searchAdapter);

    }

    @Override
    protected void setListener() {

        ll_no_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String key = filter_edit.getText().toString().trim();
                if(key == null || key.equals("")){
                    ToastUtil.show("请输入要查询的内容");
                }else{
                    getSearchAll(key, page);
                }
            }
        });

        filter_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_edit.setTextIsSelectable(false);
            }
        });

        filter_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String key = filter_edit.getText().toString().trim();
                switch (SEARCHFLAG) {
                    case Constants.HOME:
                        if (key == null || key.equals("")) {
                            rec_search.setVisibility(View.VISIBLE);
                            rec_all_search.setVisibility(View.GONE);
                        }
                        getHistoryHotSearchKey();
                        break;
                    case Constants.ALLSUBJECT:
                        if (key != null && !key.equals("")) {
                            getSearchSubject(key);
                        }
                        break;
                    case Constants.ALLARTICLE:
                        if (key != null && !key.equals("")) {
                            getSearchArticle(key);
                        }
                        break;
                }
            }
        });
    }

    //获取全局历史，热门搜索的字段
    private void getHistoryHotSearchKey() {
        NetApi.getHistoryHotSearchKey(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("url=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result,SearchActivity.this)) {
                    return;
                }
                //解析数据，先填充热门搜索数据
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray hot = data.getJSONArray("hotsearch");
                    JSONArray history = data.getJSONArray("historysearch");

                    if ((hot.toString()) != null && !(hot.toString()).equals("")) {
                        hotSearcherBean = JsonUtil.json2Bean(data.toString(), HotSearcherBean.class);
                        hotsearch = hotSearcherBean.hotsearch;
                    }

                    if ((history.toString()) != null && !(history.toString()).equals("")) {
                        historySearcherBean = JsonUtil.json2Bean(data.toString(), HistorySearcherBean.class);
                        historysearch = historySearcherBean.historysearch;
                    }

                    searchAdapter.upDate(hotsearch, historysearch);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                hotAndHistorySearcherBean = JsonUtil.json2Bean(result, HotAndHistorySearcherBean.class);
//
                handler.sendEmptyMessage(REQUEST_SUCCESS);

            }

            @Override
            public void onFailure(String message) {
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        });
    }

    //全局搜索
    public void getSearchAll(final String key, int page) {
        NetApi.getSearchAll(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getSearchAll=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result,SearchActivity.this)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray articleArray = data.getJSONArray("article");
                    JSONArray specialArray = data.getJSONArray("special");

                    if ((specialArray.toString()) != null && !(specialArray.toString()).equals("")) {
                        searchSubjectBean = JsonUtil.json2Bean(data.toString(), SearchSubjectBean.class);
                        special = searchSubjectBean.special;
                        allArticleAdapter.updateSpecial(searchSubjectBean);
                        for (SearchSubjectBean.SubjectClassifyListBean recommendArticle : special) {
                            LogUtil.e(recommendArticle.toString());
                        }
                    }

                    if ((articleArray.toString()) != null && !(articleArray.toString()).equals("")) {
                        searchArticleBean = JsonUtil.json2Bean(data.toString(), SearchArticleBean.class);
                        article = searchArticleBean.article;
                        allArticleAdapter.updateArticle(article, key);
//                        for (SearchArticleBean.ArticleClassifyListBean recommendArticle : article) {
//                            LogUtil.e("我要看的===" + recommendArticle.toString());
//                        }
                        handler.sendEmptyMessage(ALL_REQUEST_SUCCESS);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                for (AllSearchListBean.SubjectClassifyListBean recommendArticle : special) {
//                    LogUtil.e(recommendArticle.toString());
//                }
            }

            @Override
            public void onFailure(String message) {
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, key, page + "");
    }

    //获取专题历史，热门搜索的字段
    private void getSubjectHistoryHotSearchKey() {
        NetApi.getSubjectHistoryHotSearchKey(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("url=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result,SearchActivity.this)) {
                    return;
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    //专题搜索
    private void getSearchSubject(String key) {
        NetApi.getSearchSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("url=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result,SearchActivity.this)) {
                    return;
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, key, "1");
    }

    //获取文章历史，热门搜索的字段
    private void getArticleHistoryHotSearchKey() {
        NetApi.getArticleHistoryHotSearchKey(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("url=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result,SearchActivity.this)) {
                    return;
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    //文章搜索
    public void getSearchArticle(String key) {
        NetApi.getSearchArticle(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("url=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result,SearchActivity.this)) {
                    return;
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, key, "1");
    }

    public void getKey(String key) {
        this.key = key;
        getSearchAll(key, 1);
    }
}
