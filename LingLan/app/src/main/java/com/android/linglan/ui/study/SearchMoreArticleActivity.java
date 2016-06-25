package com.android.linglan.ui.study;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.linglan.adapter.ArticleSearchMoreAdapter;
import com.android.linglan.adapter.SearchArticleAllHotAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllArticleClassifyListBean;
import com.android.linglan.http.bean.RecommendArticles;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.sortlistview.ClearEditText;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import java.util.ArrayList;

public class SearchMoreArticleActivity extends BaseActivity {
    private String key;
    private int page;
    private LinearLayout ll_no_network,ll_hot_search;
    private AllArticleClassifyListBean allArticleClassifyListBean;
    private RecommendArticles recommendArticles;
    private ArrayList<RecommendArticles.RecommendArticle> hotSearch;
    private ArrayList<AllArticleClassifyListBean.ArticleClassifyListBean> article;
    private ArticleSearchMoreAdapter articleSearchAdapter;
    private RecyclerView rec_more_every,rec_hot_search;
    private PtrClassicFrameLayout recycler_view_home_recommend;
    private RecyclerAdapterWithHF mAdapter;
    private SearchArticleAllHotAdapter searchAllHotAdapter;

    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private ClearEditText filter_edit;
    private Button btn_search;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_FAILURE:
                    //原页面GONE掉，提示网络不好的页面出现
                    recycler_view_home_recommend.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);

                    break;
                case REQUEST_SUCCESS:
                    //原页面GONE掉，提示网络不好的页面出现
                    recycler_view_home_recommend.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
            }
        }
    };


    @Override
    protected void setView() {
        setContentView(R.layout.activity_search_more);
    }

    @Override
    protected void initView() {
        key = getIntent().getStringExtra("key");
        recycler_view_home_recommend = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_home_recommend);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        ll_hot_search = (LinearLayout) findViewById(R.id.ll_hot_search);
        rec_more_every = (RecyclerView) findViewById(R.id.rec_more_every);
        rec_hot_search = (RecyclerView) findViewById(R.id.rec_hot_search);
        filter_edit = (ClearEditText) findViewById(R.id.filter_edit);
        btn_search = (Button) findViewById(R.id.btn_search);
        if (key != null && !key.equals("")) {
            rec_more_every.setVisibility(View.VISIBLE);
            ll_hot_search.setVisibility(View.GONE);
            filter_edit.setText(key);
            filter_edit.setSelection(key.length());
        } else {
            filter_edit.setHint("搜索文章");
            rec_more_every.setVisibility(View.GONE);
            ll_hot_search.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        setTitle("搜索文章", "");
        rec_more_every.setLayoutManager(new LinearLayoutManager(this));
        rec_more_every.setHasFixedSize(true);

        rec_hot_search.setLayoutManager(new LinearLayoutManager(this));
        rec_hot_search.setHasFixedSize(true);

        page = 1;
        recommendArticles = new RecommendArticles();
        searchAllHotAdapter = new SearchArticleAllHotAdapter(this);
        rec_hot_search.setAdapter(searchAllHotAdapter);

        hotSearch = new ArrayList<RecommendArticles.RecommendArticle>();
        articleSearchAdapter = new ArticleSearchMoreAdapter(this);
        mAdapter = new RecyclerAdapterWithHF(articleSearchAdapter);
        rec_more_every.setAdapter(mAdapter);
        if (key != null && !key.equals("")) {
            getSearchArticle(key, page);
        }else{
            getArticleHotSearchKey();
        }

    }

    @Override
    protected void setListener() {
        //下拉刷新
        recycler_view_home_recommend.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getSearchArticle(key, page);
            }
        });

        //上拉刷新
        recycler_view_home_recommend.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                getSearchArticle(key, page);
                recycler_view_home_recommend.loadMoreComplete(true);
            }
        });

        ll_no_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

        filter_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = filter_edit.getText().toString().trim();
                if (key != null && !key.equals("")) {
                    rec_more_every.setVisibility(View.VISIBLE);
                    ll_hot_search.setVisibility(View.GONE);
                    page = 1;
                    getSearchArticle(key, page);
                } else {
                    rec_more_every.setVisibility(View.GONE);
                    ll_hot_search.setVisibility(View.VISIBLE);
                    getArticleHotSearchKey();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    //获取文章热门搜索的字段
    private void getArticleHotSearchKey() {
        NetApi.getArticleHotSearchKey(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getArticleHotSearchKey =" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SearchMoreArticleActivity.this)) {
                    return;
                }

                recommendArticles = JsonUtil.json2Bean(result, RecommendArticles.class);
                hotSearch = recommendArticles.data;

                if (hotSearch != null && hotSearch.size() != 0) {
                    LogUtil.e("hotSearch hotSearch hotSearch = " +hotSearch.get(0).title );
                    searchAllHotAdapter.updateArticleAdapter(hotSearch);
                }
                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        });
    }

    //文章关键字搜索
    private void getSearchArticle(String key, final int page) {
        NetApi.getSearchArticle(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                recycler_view_home_recommend.refreshComplete();
                recycler_view_home_recommend.setLoadMoreEnable(true);
                LogUtil.e("getSearchArticle  =" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SearchMoreArticleActivity.this)) {
//                    recycler_view_home_recommend.loadMoreComplete(false);
                    recycler_view_home_recommend.loadPage1MoreComplete();
                    ToastUtil.show("没有搜索到相关内容，换个搜索词吧");
                    return;
                }
                allArticleClassifyListBean = JsonUtil.json2Bean(result, AllArticleClassifyListBean.class);
                if (page == 1) {
                    article = allArticleClassifyListBean.data;
                    if (article != null && article.size() < 10) {
                        recycler_view_home_recommend.loadPage1MoreComplete();
                    } else {
                        recycler_view_home_recommend.loadMoreComplete(true);
                    }
                } else {
                    if (allArticleClassifyListBean.data == null || (allArticleClassifyListBean.data).size() == 0) {
                        ToastUtil.show("没有数据了");
                    } else {
                        article.addAll(allArticleClassifyListBean.data);
                    }
                }

                if (article != null && article.size() != 0) {
                    articleSearchAdapter.update(article);
                }

                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                recycler_view_home_recommend.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }

                , key, page + "");
    }


}
