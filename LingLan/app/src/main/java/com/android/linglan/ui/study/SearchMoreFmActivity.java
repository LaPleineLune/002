package com.android.linglan.ui.study;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.adapter.SearchAllRadioAdapter;
import com.android.linglan.adapter.SearchFmAllHotAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllArticleClassifyListBean;
import com.android.linglan.http.bean.RadioListBean;
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

public class SearchMoreFmActivity extends BaseActivity {
    private String key;
    private int page;
    private LinearLayout ll_no_network, ll_hot_search;
    private TextView tv_hot_title;
    private SearchAllRadioAdapter fmSearchAdapter;
    private RecyclerView rec_more_every,rec_hot_search;
    private PtrClassicFrameLayout recycler_view_home_recommend;
    private RecyclerAdapterWithHF mAdapter;
    private ClearEditText filter_edit;
    private ArrayList<RadioListBean.RadioListData> radioListData;
    private RadioListBean radioListBean;
    private ArrayList<RadioListBean.RadioListData> hotSearch;
    private SearchFmAllHotAdapter searchFmAllHotAdapter;

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
        key = getIntent().getStringExtra("key");
        recycler_view_home_recommend = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_home_recommend);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        ll_hot_search = (LinearLayout) findViewById(R.id.ll_hot_search);
        tv_hot_title = (TextView) findViewById(R.id.tv_hot_title);
        rec_more_every = (RecyclerView) findViewById(R.id.rec_more_every);
        rec_hot_search = (RecyclerView) findViewById(R.id.rec_hot_search);
        filter_edit = (ClearEditText) findViewById(R.id.filter_edit);
        tv_hot_title.setText("热搜音频");
        if (key != null && !key.equals("")) {
            filter_edit.setText(key);
            filter_edit.setSelection(key.length());
            rec_more_every.setVisibility(View.VISIBLE);
            ll_hot_search.setVisibility(View.GONE);
        } else {
            filter_edit.setHint("搜索音频");
            rec_more_every.setVisibility(View.GONE);
            ll_hot_search.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        setTitle("搜索音频", "");
        radioListBean = new RadioListBean();
        hotSearch = new ArrayList<RadioListBean.RadioListData>();
        rec_hot_search.setLayoutManager(new LinearLayoutManager(this));
        rec_hot_search.setHasFixedSize(true);
        rec_more_every.setLayoutManager(new LinearLayoutManager(this));
        rec_more_every.setHasFixedSize(true);
        page = 1;
        radioListData = new ArrayList<>();
        searchFmAllHotAdapter = new SearchFmAllHotAdapter(this);
        rec_hot_search.setAdapter(searchFmAllHotAdapter);
        fmSearchAdapter = new SearchAllRadioAdapter(this);
        mAdapter = new RecyclerAdapterWithHF(fmSearchAdapter);
        rec_more_every.setAdapter(mAdapter);
        if (key != null && !key.equals("")) {
            getSearchFm(key, page);
        }else {
            getFmHotSearchKey();
        }
    }

    @Override
    protected void setListener() {
        //下拉刷新
        recycler_view_home_recommend.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getSearchFm(key, page);
            }
        });

        //上拉刷新
        recycler_view_home_recommend.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                getSearchFm(key, page);
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
                    getSearchFm(key, page);
                } else {
                    rec_more_every.setVisibility(View.GONE);
                    ll_hot_search.setVisibility(View.VISIBLE);
                    getFmHotSearchKey();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }


    //获取音频历史，热门搜索的字段
    private void getFmHotSearchKey() {
        NetApi.getFmHotSearchKey(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getFmHotSearchKey getFmHotSearchKey getFmHotSearchKey =" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SearchMoreFmActivity.this)) {
                    return;
                }
                radioListBean = JsonUtil.json2Bean(result, RadioListBean.class);
                hotSearch = radioListBean.data;

                if (hotSearch != null && hotSearch.size() != 0) {
                    LogUtil.e("hotSearch hotSearch hotSearch = " + hotSearch.get(0).title);
                    searchFmAllHotAdapter.updateFmAdapter(hotSearch);
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
    private void getSearchFm(String key, final int page) {
        NetApi.getSearchFm(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                recycler_view_home_recommend.refreshComplete();
                recycler_view_home_recommend.setLoadMoreEnable(true);
                LogUtil.e("url=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SearchMoreFmActivity.this)) {
                    recycler_view_home_recommend.loadPage1MoreComplete();
                    ToastUtil.show("没有搜索到相关内容，换个搜索词吧");
                    return;
                }
                RadioListBean radioListBean = JsonUtil.json2Bean(result, RadioListBean.class);
                if (page == 1) {
                    radioListData.clear();
                }
                for (RadioListBean.RadioListData data : radioListBean.data) {
                        radioListData.add(data);
                }

                if (page == 1) {
                    if (radioListData != null && radioListData.size() < 10) {
                        recycler_view_home_recommend.loadPage1MoreComplete();
                    } else {
                        recycler_view_home_recommend.loadMoreComplete(true);
                    }
                }
                    if (radioListData != null && radioListData.size() != 0) {
                        fmSearchAdapter.updateAdapter(radioListData);
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
