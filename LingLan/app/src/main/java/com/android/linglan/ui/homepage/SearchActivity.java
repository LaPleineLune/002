package com.android.linglan.ui.homepage;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;

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
import com.android.linglan.widget.SyLinearLayoutManager;
import com.android.linglan.widget.sortlistview.ClearEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
    private int SEARCHFLAG = 0;

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

    private int page;
    private String key;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    rec_search.setVisibility(View.GONE);
                    rec_all_search.setVisibility(View.VISIBLE);
                    if((filter_edit.getText().toString().trim()) == null || (filter_edit.getText().toString().trim()).equals("")){
                        filter_edit.setText(key);
                        filter_edit.setSelection(key.length());
                    }
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initView() {
        filter_edit = (ClearEditText) findViewById(R.id.filter_edit);
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
        SEARCHFLAG = (int) getIntent().getExtras().get("searchEdit");
        switch (SEARCHFLAG) {
            case Constants.HOME:
                filter_edit.setHint("搜索文章、专题、作者");
                //热门搜索，历史搜索，填充数据
                getHistoryHotSearchKey();
                break;
            case Constants.ALLSUBJECT:
                filter_edit.setHint("搜索专题");
                getSubjectHistoryHotSearchKey();
                break;
            case Constants.ALLARTICLE:
                filter_edit.setHint("搜索文章");
                getArticleHistoryHotSearchKey();
                break;
        }

        rec_search.setLayoutManager(new SyLinearLayoutManager(this));
        rec_search.setHasFixedSize(true);
        searchAdapter = new SearchAdapter(this);
        rec_search.setAdapter(searchAdapter);

    }

    @Override
    protected void setListener() {
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
                        if (key != null && !key.equals("")) {
                            getSearchAll(key, page);
                        } else {
                            rec_search.setVisibility(View.VISIBLE);
                            rec_all_search.setVisibility(View.GONE);
                        }
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
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
                //解析数据，先填充热门搜索数据
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray hot = data.getJSONArray("hotsearch");
                    JSONArray history = data.getJSONArray("historysearch");

                    if((hot.toString()) != null && !(hot.toString()).equals("")){
                        hotSearcherBean = JsonUtil.json2Bean(data.toString(), HotSearcherBean.class);
                        hotsearch = hotSearcherBean.hotsearch;
                    }

                    if((history.toString()) != null && !(history.toString()).equals("")){
                        historySearcherBean = JsonUtil.json2Bean(data.toString(), HistorySearcherBean.class);
                        historysearch = historySearcherBean.historysearch;
                    }

                    searchAdapter.upDate(hotsearch,historysearch);
//                    LogUtil.e(historysearch[0]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                hotAndHistorySearcherBean = JsonUtil.json2Bean(result, HotAndHistorySearcherBean.class);
//


            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    //全局搜索
    public void getSearchAll(final String key, int page) {
        NetApi.getSearchAll(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("url=" + result);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
                //解析数据
//                allSearchListBean = JsonUtil.json2Bean(result, AllSearchListBean.class);
//                article = allSearchListBean.data.article;
//                special = allSearchListBean.data.special;
//                allArticleAdapter.insertArticlesData(article);
//                allArticleAdapter.insertSubjectData(special);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray articleArray = data.getJSONArray("article");
                    JSONArray specialArray = data.getJSONArray("special");
                    if ((articleArray.toString()) != null && !(articleArray.toString()).equals("")) {
                        searchArticleBean = JsonUtil.json2Bean(data.toString(), SearchArticleBean.class);
                        article = searchArticleBean.article;
                        allArticleAdapter.updateArticle(searchArticleBean, key);
                        for (SearchArticleBean.ArticleClassifyListBean recommendArticle : article) {
                            LogUtil.e(recommendArticle.toString());
                        }
                        handler.sendEmptyMessage(0);
                    }

                    if((specialArray.toString()) != null && !(specialArray.toString()).equals("")){
                        searchSubjectBean = JsonUtil.json2Bean(data.toString(), SearchSubjectBean.class);
                        special = searchSubjectBean.special;
                        allArticleAdapter.updateSpecial(searchSubjectBean);
                        for (SearchSubjectBean.SubjectClassifyListBean recommendArticle : special) {
                            LogUtil.e(recommendArticle.toString());
                        }
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

            }
        }, key, page + "");
    }

    //获取专题历史，热门搜索的字段
    private void getSubjectHistoryHotSearchKey() {
        NetApi.getSubjectHistoryHotSearchKey(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("url=" + result);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
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
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, key,"1");
    }

    //获取文章历史，热门搜索的字段
    private void getArticleHistoryHotSearchKey() {
        NetApi.getArticleHistoryHotSearchKey(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("url=" + result);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
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
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, key,"1");
    }
    public void getKey(String key){
        this.key = key;
        getSearchAll(key, 1);
    }
}
