package com.android.linglan.ui.study;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.linglan.adapter.AllArticleAdapter;
import com.android.linglan.adapter.HomepageArticleClassifyAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllArticleClassifyBean;
import com.android.linglan.http.bean.AllArticleClassifyListBean;
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

/**
 * Created by LeeMy on 2016/2/26 0026.
 * 首页文章
 */
public class ArticleActivity extends BaseActivity {
    private ListView lv_article_classify;
    private TextView tv_classify_order;
    private LinearLayout ll_all_article;
    private LinearLayout ll_no_network;
    private HomepageArticleClassifyAdapter articleClassifyAdapter;
    private PtrClassicFrameLayout recycler_view_home_recommend;
    private RecyclerView rec_all_article;
    private AllArticleAdapter allArticleAdapter;
    private RecyclerAdapterWithHF mAdapter;
    private int page;
    private int location;
    private String order;//排序方式('addtime按时间排序' ,'count_view按统计排序')

    private AllArticleClassifyBean AllArticleClassify;
    public ArrayList<AllArticleClassifyBean.ArticleClassifyBean> ArticleClassify;

    private AllArticleClassifyListBean AllArticleClassifyList;
    private ArrayList<AllArticleClassifyListBean.ArticleClassifyListBean> ArticleClassifyList;
    private int oldPosition = -1;

//    protected static final int REQUEST_SUCCESS = 0;
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;

    private Intent intent;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                   selectDefult();// 刚刚进来时的默认加载\
                    ll_all_article.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    //原页面GONE掉，提示网络不好的页面出现
                    ll_all_article.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_study_article);
    }

    @Override
    protected void initView() {
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        ll_all_article = (LinearLayout) findViewById(R.id.ll_all_article);
        lv_article_classify = (ListView) findViewById(R.id.lv_article_classify);

        recycler_view_home_recommend = (PtrClassicFrameLayout) findViewById(R.id. recycler_view_home_recommend);
        rec_all_article = (RecyclerView) findViewById(R.id.rec_all_article);
        rec_all_article.setLayoutManager(new LinearLayoutManager(this));
        rec_all_article .setHasFixedSize(true);
        allArticleAdapter = new AllArticleAdapter(this);
        mAdapter = new RecyclerAdapterWithHF(allArticleAdapter);
        rec_all_article.setAdapter(mAdapter);

        articleClassifyAdapter = new HomepageArticleClassifyAdapter(this);
        lv_article_classify.setAdapter(articleClassifyAdapter);

    }

    @Override
    protected void initData() {
        setTitle("文章", "");
        intent = new Intent();
        Drawable collectTopDrawable = ContextCompat.getDrawable(this, R.drawable.search1);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);

        getAllArticleClassify();
    }

    @Override
    protected void setListener() {
        //下拉刷新
        recycler_view_home_recommend.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                if ((ArticleClassify == null || ArticleClassify.size() == 0)) {
                    getAllArticleClassify();
                }else{
                    getAllArticleClassifyList(ArticleClassify.get(location).cateid, page, order);
                }

            }
        });

        //上拉刷新
        recycler_view_home_recommend.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                if ((ArticleClassify == null || ArticleClassify.size() == 0)) {
                    getAllArticleClassify();
                }else{
                    getAllArticleClassifyList(ArticleClassify.get(location).cateid, page, order);
                }
                recycler_view_home_recommend.loadMoreComplete(true);
            }
        });

        lv_article_classify.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                location = position;
                page = 1;
                if (oldPosition != position) {
                    articleClassifyAdapter.setSelectedPosition(position);
                    articleClassifyAdapter.notifyDataSetInvalidated();

                    getAllArticleClassifyList(ArticleClassify.get(location).cateid, page, order);

                    oldPosition = position;
                }
            }
        });

        ll_no_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(ArticleActivity.this, SearchActivity.class);
                intent.putExtra("searchEdit", Constants.ALLARTICLE);
                startActivity(intent);
            }
        });
    }

    private void selectDefult(){
        articleClassifyAdapter.setSelectedPosition(0);
        articleClassifyAdapter.notifyDataSetInvalidated();
        order = "";
        page =1;

        getAllArticleClassifyList(ArticleClassify.get(location).cateid, page, order);
    }

    private void getAllArticleClassify() {
        NetApi.getAllArticleClassify(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.d(ArticleActivity.this.getPackageName(), "getAllArticleClassify=" + result);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ArticleActivity.this)){
                    return;
                }
                AllArticleClassify = JsonUtil.json2Bean(result, AllArticleClassifyBean.class);
                ArticleClassify = AllArticleClassify.data;
                if (ArticleClassify != null && !ArticleClassify.equals("")) {
                    ArticleClassify.get(0).cateid = "";
                    articleClassifyAdapter.update(ArticleClassify);
                    handler.sendEmptyMessage(REQUEST_SUCCESS);
                }
            }

            @Override
            public void onFailure(String message) {
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        });
    }

    //得到所有的文章分类
    private void getAllArticleClassifyList(String cateid, final int page,String order) {
        NetApi.getAllArticleClassifyList(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
            LogUtil.d(ArticleActivity.this.getPackageName(), "getAllArticleClassifyList=" + result);

                recycler_view_home_recommend.refreshComplete();
                recycler_view_home_recommend.setLoadMoreEnable(true);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ArticleActivity.this)){
                    recycler_view_home_recommend.loadMoreComplete(false);
                    return;
                }

                AllArticleClassifyList = JsonUtil.json2Bean(result, AllArticleClassifyListBean.class);
                if (page == 1) {
                    ArticleClassifyList = AllArticleClassifyList.data;
                } else {
                    if (AllArticleClassifyList.data == null || (AllArticleClassifyList.data).size() == 0) {
                        ToastUtil.show("没有数据了");
                    } else {
                        ArticleClassifyList.addAll(AllArticleClassifyList.data);
                    }
                }
//
                if (ArticleClassifyList != null && !ArticleClassifyList.equals("")) {
                    allArticleAdapter.updateAdapter(ArticleClassifyList);
                }
            }

            @Override
            public void onFailure(String message) {
                recycler_view_home_recommend.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, cateid, page + "", order);
    }
}
