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
import android.widget.TextView;

import com.android.linglan.adapter.SearchArticleAllHotAdapter;
import com.android.linglan.adapter.SearchSubjectAllHotAdapter;
import com.android.linglan.adapter.SubjectSearchMoreAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.RecommendSubjects;
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

public class SearchMoreSubjectActivity extends BaseActivity {
    private String key;
    private int page;
    private LinearLayout ll_no_network, ll_hot_search;
    private TextView tv_hot_title;
    private RecommendSubjects recommendSubject;
    private ArrayList<RecommendSubjects.RecommendSubject> hotSearch;
    public ArrayList<RecommendSubjects.RecommendSubject> subject;
    private SubjectSearchMoreAdapter subjectSearchAdapter;
    private RecyclerView rec_more_every, rec_hot_search;
    private PtrClassicFrameLayout recycler_view_home_recommend;
    private RecyclerAdapterWithHF mAdapter;
    private SearchSubjectAllHotAdapter searchAllHotAdapter;

    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;

    private ClearEditText filter_edit;
    private Button btn_search;

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
        key = getIntent().getStringExtra("key");
        recycler_view_home_recommend = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_home_recommend);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        ll_hot_search = (LinearLayout) findViewById(R.id.ll_hot_search);
        tv_hot_title = (TextView) findViewById(R.id.tv_hot_title);
        rec_more_every = (RecyclerView) findViewById(R.id.rec_more_every);
        rec_hot_search = (RecyclerView) findViewById(R.id.rec_hot_search);
        filter_edit = (ClearEditText) findViewById(R.id.filter_edit);
        btn_search = (Button) findViewById(R.id.btn_search);

        tv_hot_title.setText("热搜专题");
        if (key != null && !key.equals("")) {
            filter_edit.setText(key);
            filter_edit.setSelection(key.length());
        } else {
            filter_edit.setHint("搜索专题");
        }
    }

    @Override
    protected void initData() {
        setTitle("搜索专题", "");
        recommendSubject = new RecommendSubjects();
        hotSearch = new ArrayList<RecommendSubjects.RecommendSubject>();
        rec_more_every.setLayoutManager(new LinearLayoutManager(this));
        rec_more_every.setHasFixedSize(true);

        rec_hot_search.setLayoutManager(new LinearLayoutManager(this));
        rec_hot_search.setHasFixedSize(true);
        page = 1;
        searchAllHotAdapter = new SearchSubjectAllHotAdapter(this);
        rec_hot_search.setAdapter(searchAllHotAdapter);
        subjectSearchAdapter = new SubjectSearchMoreAdapter(this);
        mAdapter = new RecyclerAdapterWithHF(subjectSearchAdapter);
        rec_more_every.setAdapter(mAdapter);
        if (key != null && !key.equals("")) {
            getSearchSubject(key, page);
            rec_more_every.setVisibility(View.VISIBLE);
            ll_hot_search.setVisibility(View.GONE);
        } else {
            getSubjectHotSearchKey();
            rec_more_every.setVisibility(View.GONE);
            ll_hot_search.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void setListener() {
        //下拉刷新
        recycler_view_home_recommend.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;

                getSearchSubject(key, page);

            }
        });

        //上拉刷新
        recycler_view_home_recommend.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                getSearchSubject(key, page);
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
                    getSearchSubject(key, page);
                } else {
                    rec_more_every.setVisibility(View.GONE);
                    ll_hot_search.setVisibility(View.VISIBLE);
                    getSubjectHotSearchKey();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    //获取专题热门搜索的字段
    private void getSubjectHotSearchKey() {
        NetApi.getSubjectHotSearchKey(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getSubjectHotSearchKey getSubjectHotSearchKey getSubjectHotSearchKey =" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SearchMoreSubjectActivity.this)) {
                    return;
                }

                recommendSubject = JsonUtil.json2Bean(result, RecommendSubjects.class);
                hotSearch = recommendSubject.data;

                if (hotSearch != null && hotSearch.size() != 0) {
                    LogUtil.e("hotSearch hotSearch hotSearch = " + hotSearch.get(0).specialname);
                    searchAllHotAdapter.updateSubjectAdapter(hotSearch);
                }
                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        });
    }

    //专题搜索
    private void getSearchSubject(String key, final int page) {
        NetApi.getSearchSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                recycler_view_home_recommend.refreshComplete();
                recycler_view_home_recommend.setLoadMoreEnable(true);
                LogUtil.e("url=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SearchMoreSubjectActivity.this)) {
//                    recycler_view_home_recommend.loadMoreComplete(false);
                    recycler_view_home_recommend.loadPage1MoreComplete();
                    ToastUtil.show("没有搜索到相关内容，换个搜索词吧");
                    return;
                }
                recommendSubjects = JsonUtil.json2Bean(result, RecommendSubjects.class);

                if (page == 1) {
                    subject = recommendSubjects.data;
                    if (subject != null && subject.size() < 10) {
                        recycler_view_home_recommend.loadPage1MoreComplete();
                    } else {
                        recycler_view_home_recommend.loadMoreComplete(true);
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
        }, key, page + "");
    }

}
