package com.android.linglan.ui.study;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.linglan.adapter.ArticleSearchMoreAdapter;
import com.android.linglan.adapter.SubjectSearchMoreAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllArticleClassifyListBean;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import java.util.ArrayList;

public class SearchMoreActivity extends BaseActivity {
    private String moreArticle;
    private String moreSubject;
    private String key;
    private int page;
    private LinearLayout ll_no_network;
    private AllArticleClassifyListBean allArticleClassifyListBean;
    private ArrayList<AllArticleClassifyListBean.ArticleClassifyListBean> article;
    public ArrayList<RecommendSubjects.RecommendSubject> subject;
    private ArticleSearchMoreAdapter articleSearchAdapter;
    private SubjectSearchMoreAdapter subjectSearchAdapter;
//    private CustomPullToRefreshRecyclerView refresh_more_every;
    private RecyclerView rec_more_every;
    private PtrClassicFrameLayout recycler_view_home_recommend;
    private RecyclerAdapterWithHF mAdapter;

    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;

    private RecommendSubjects recommendSubjects;

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
        moreArticle = getIntent().getStringExtra("moreArticle");
        moreSubject = getIntent().getStringExtra("moreSubject");
        key = getIntent().getStringExtra("key");
        recycler_view_home_recommend = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_home_recommend);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        rec_more_every = (RecyclerView) findViewById(R.id.rec_more_every);
    }

    @Override
    protected void initData() {
        setTitle("搜索", "");
        rec_more_every.setLayoutManager(new LinearLayoutManager(this));
        rec_more_every.setHasFixedSize(true);
        page = 1;
        if (moreArticle != null && !moreArticle.equals("")) {
            articleSearchAdapter = new ArticleSearchMoreAdapter(this);
            mAdapter = new RecyclerAdapterWithHF(articleSearchAdapter);
            rec_more_every.setAdapter(mAdapter);
            getSearchArticle(key, page);

        } else if (moreSubject != null && !moreSubject.equals("")) {
            subjectSearchAdapter = new SubjectSearchMoreAdapter(this);
            mAdapter = new RecyclerAdapterWithHF(subjectSearchAdapter);
            rec_more_every.setAdapter(mAdapter);
            getSearchSubject(key, page);

        }

    }

    @Override
    protected void setListener() {
        //下拉刷新
        recycler_view_home_recommend.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                if (moreArticle != null && !moreArticle.equals("")) {
                    getSearchArticle(key, page);
                } else if (moreSubject != null && !moreSubject.equals("")) {
                    getSearchSubject(key, page);
                }
            }
        });

        //上拉刷新
        recycler_view_home_recommend.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                if (moreArticle != null && !moreArticle.equals("")) {
                    getSearchArticle(key, page);
                } else if (moreSubject != null && !moreSubject.equals("")) {
                    getSearchSubject(key, page);
                }
                recycler_view_home_recommend.loadMoreComplete(true);
            }
        });

        ll_no_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
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
                LogUtil.e("url=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,SearchMoreActivity.this)){
                    recycler_view_home_recommend.loadMoreComplete(false);
                    return;
                }
                allArticleClassifyListBean = JsonUtil.json2Bean(result, AllArticleClassifyListBean.class);
                if (page == 1) {
                    article = allArticleClassifyListBean.data;
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

    //专题搜索
    private void getSearchSubject(String key, final int page) {
        NetApi.getSearchSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                recycler_view_home_recommend.refreshComplete();
                recycler_view_home_recommend.setLoadMoreEnable(true);
                LogUtil.e("url=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,SearchMoreActivity.this)){
                    recycler_view_home_recommend.loadMoreComplete(false);
                    return;
                }
                recommendSubjects = JsonUtil.json2Bean(result, RecommendSubjects.class);

                if (page == 1) {
                    subject = recommendSubjects.data;
                    if(subject.size() <10){
                        recycler_view_home_recommend.loadMoreComplete(false);
                    }
                } else {
                    if (recommendSubjects.data == null || (recommendSubjects.data).size() == 0) {
                        ToastUtil.show("没有数据了");
                    } else {
                        subject.addAll(recommendSubjects.data);
                    }
                }

                if (subject != null && subject.size() != 0) {
                    subjectSearchAdapter.update(subject);
                }

                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                recycler_view_home_recommend.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, key,page+"");
    }

}
