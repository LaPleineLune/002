package com.android.linglan.ui.homepage;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.linglan.adapter.ArticleSearchMoreAdapter;
import com.android.linglan.adapter.SubjectSearchMoreAdapter;
import com.android.linglan.base.AllSubjectBaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllArticleClassifyListBean;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.CustomPullToRefreshRecyclerView;
import com.android.linglan.widget.SyLinearLayoutManager;

import java.util.ArrayList;

public class SearchMoreActivity extends AllSubjectBaseActivity {
    private String moreArticle;
    private String moreSubject;
    private String key;
    private int page;
    private AllArticleClassifyListBean allArticleClassifyListBean;
    private ArrayList<AllArticleClassifyListBean.ArticleClassifyListBean> article;
    public ArrayList<RecommendSubjects.RecommendSubject> subject;
    private ArticleSearchMoreAdapter articleSearchAdapter;
    private SubjectSearchMoreAdapter subjectSearchAdapter;
    private CustomPullToRefreshRecyclerView refresh_more_every;
    private RecyclerView rec_more_every;
    private RecommendSubjects recommendSubjects;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_search_more);
    }

    @Override
    protected void initView() {
        moreArticle = getIntent().getStringExtra("moreArticle");
        moreSubject = getIntent().getStringExtra("moreSubject");
        key = getIntent().getStringExtra("key");
        refresh_more_every = (CustomPullToRefreshRecyclerView) findViewById(R.id.refresh_more_every);
    }

    @Override
    protected void initData() {
        setTitle("搜索", "");
        order.setVisibility(View.GONE);
        right.setVisibility(View.GONE);
        rec_more_every = refresh_more_every.getRefreshableView();
        rec_more_every.setLayoutManager(new SyLinearLayoutManager(this));
        rec_more_every.setHasFixedSize(true);
        page = 1;
        if (moreArticle != null && !moreArticle.equals("")) {
            articleSearchAdapter = new ArticleSearchMoreAdapter(this);
            rec_more_every.setAdapter(articleSearchAdapter);
            getSearchArticle(key, page);

        } else if (moreSubject != null && !moreSubject.equals("")) {
            subjectSearchAdapter = new SubjectSearchMoreAdapter(this);
            rec_more_every.setAdapter(subjectSearchAdapter);
            getSearchSubject(key, page);

        }

    }

    @Override
    protected void setListener() {
        refresh_more_every.setRefreshCallback(new CustomPullToRefreshRecyclerView.RefreshCallback() {
            //上拉
            @Override
            public void onPullDownToRefresh() {
                page = 1;
                if (moreArticle != null && !moreArticle.equals("")) {
                    getSearchArticle(key, page);
                } else if (moreSubject != null && !moreSubject.equals("")) {
                    getSearchSubject(key, page);
                }
            }

            //下拉
            @Override
            public void onPullUpToLoadMore() {
                page++;
                if (moreArticle != null && !moreArticle.equals("")) {
                    getSearchArticle(key, page);
                } else if (moreSubject != null && !moreSubject.equals("")) {
                    getSearchSubject(key, page);
                }
            }
        });
    }

    //文章关键字搜索
    private void getSearchArticle(String key, final int page) {
        NetApi.getSearchArticle(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                refresh_more_every.onRefreshComplete();
                LogUtil.e("url=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
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
                } else {
                    refresh_more_every.setVisibility(View.GONE);
                }

                for (AllArticleClassifyListBean.ArticleClassifyListBean recommendArticle : article) {
                    LogUtil.e(recommendArticle.toString());
                }

            }

            @Override
            public void onFailure(String message) {
                refresh_more_every.onRefreshComplete();
            }
        }

                , key, page + "");
    }

    //专题搜索
    private void getSearchSubject(String key, final int page) {
        NetApi.getSearchSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                refresh_more_every.onRefreshComplete();
                LogUtil.e("url=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
                recommendSubjects = JsonUtil.json2Bean(result, RecommendSubjects.class);

                if (page == 1) {
                    subject = recommendSubjects.data;
                } else {
                    if (recommendSubjects.data == null || (recommendSubjects.data).size() == 0) {
                        ToastUtil.show("没有数据了");
                    } else {
                        subject.addAll(recommendSubjects.data);
                    }
                }

                if (subject != null && subject.size() != 0) {
                    subjectSearchAdapter.update(subject);
                } else {
                    refresh_more_every.setVisibility(View.GONE);
                }

                for (RecommendSubjects.RecommendSubject recommendArticle : subject) {
                    LogUtil.e(recommendArticle.toString());
                }

            }

            @Override
            public void onFailure(String message) {
                refresh_more_every.onRefreshComplete();
            }
        }, key,page+"");
    }

}
