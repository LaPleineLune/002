package com.android.linglan.ui.clinical;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.linglan.adapter.clinical.TCMSearchRequestResultAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.R;
import com.android.linglan.utils.AESCryptUtil;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonHelper;
import com.android.linglan.utils.LogUtil;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LeeMy on 2016/5/1 0001.
 * 中医搜搜的搜索结果界面
 */
public class TCMSearchRequestResultActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private LinearLayout ll_tcm_search_request;
    private LinearLayout ll_no_network;
    private PtrClassicFrameLayout recycler_view_tcm_search_request;
    private RecyclerView rec_tcm_search_request;
    private RecyclerAdapterWithHF mAdapter;
    private TCMSearchRequestResultAdapter adapter;
    public List<Object> data = null;
    public String key = "";
    private int page;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ll_tcm_search_request.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    ll_tcm_search_request.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_tcm_search_request_result);

    }

    @Override
    protected void initView() {
        ll_tcm_search_request = (LinearLayout) findViewById(R.id.ll_tcm_search_request);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        recycler_view_tcm_search_request = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_tcm_search_request);
        rec_tcm_search_request = (RecyclerView) findViewById(R.id.rec_tcm_search_request);

    }

    @Override
    protected void initData() {
        setTitle("中医搜搜", "");
        key = getIntent().getStringExtra("key");
//        data = new ArrayList<TCMSearchBean.TCMSearchData>();
        data = new ArrayList<>();
        page = 1;
        getTCMSearchKey(key, page);
        rec_tcm_search_request.setLayoutManager(new LinearLayoutManager(this));
        rec_tcm_search_request.setHasFixedSize(true);
        adapter = new TCMSearchRequestResultAdapter(this);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        rec_tcm_search_request.setAdapter(mAdapter);

    }

    @Override
    protected void setListener() {
        ll_no_network.setOnClickListener(this);
        //下拉刷新
        recycler_view_tcm_search_request.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getTCMSearchKey(key, page);
            }
        });

        //上拉刷新
        recycler_view_tcm_search_request.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                getTCMSearchKey(key, page);
                recycler_view_tcm_search_request.loadMoreComplete(true);
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_no_network:
                initData();
                break;
        }
    }

    public void getTCMSearchKey(final String key, final int page) {
        NetApi.getTCMSearchKey(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getTCMSearchKey=" + result);
                recycler_view_tcm_search_request.refreshComplete();
                recycler_view_tcm_search_request.setLoadMoreEnable(true);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, TCMSearchRequestResultActivity.this)) {
                    recycler_view_tcm_search_request.loadMoreComplete(false);
                    return;
                }
                if (page == 1) {
                    data.clear();
                }

                try {
                    Map<String, Object> map = JsonHelper.toMap(result);
                    data.add(map);

                    List<Map<String, String>> rowsT = (List<Map<String, String>>) map.get("rows");
                    if (rowsT == null || rowsT.size() == 0) {
                        recycler_view_tcm_search_request.loadMoreComplete(false);
                    }
//                    if (rowsT == null || rowsT.size() == 0 || rowsT.size() < 9) {
//                        if (page == 1) {
//                            recycler_view_tcm_search_request.refreshComplete();
//                            recycler_view_tcm_search_request.loadPage1MoreComplete();
//                        } else {
//                            recycler_view_tcm_search_request.loadMoreComplete(false);
////                            recycler_view_tcm_search_request.refreshComplete();
////                            recycler_view_tcm_search_request.setLoadMoreEnable(true);
//                        }
//
////                        recycler_view_tcm_search_request.setPinContent(false);
//                    } else {
//                        recycler_view_tcm_search_request.refreshComplete();
//                        recycler_view_tcm_search_request.setLoadMoreEnable(true);
//                    }

                    if (data != null && data.size() != 0) {
                        adapter.updateAdapter(data, key);
//                    } else {
//                        recycler_view_tcm_search_request.setLoadMoreEnable(false);
//                        recycler_view_tcm_search_request.setNoMoreData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(REQUEST_SUCCESS);

            }

            @Override
            public void onFailure(String message) {
                recycler_view_tcm_search_request.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, key, page + "");
    }

}
