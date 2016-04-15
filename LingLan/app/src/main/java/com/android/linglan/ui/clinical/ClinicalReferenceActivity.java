package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.linglan.adapter.RecycleHomeRecommendAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.HomepageRecommendBean;
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
import java.util.List;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 临证参考
 */
public class ClinicalReferenceActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private PtrClassicFrameLayout recycler_view_home_recommend;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView lv_homepage_recommend;
    private LinearLayout ll_no_network;
    //    private RecyclerAdapter adapter;
    private RecycleHomeRecommendAdapter adapter;
    public ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> data;
    private Intent intent;
    private int page;//页码
//    private CustomPullToRefreshRecyclerView refresh_more_every;

    private List<String> mData = new ArrayList<String>();

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
        setContentView(R.layout.activity_clinical_reference);
    }

    @Override
    protected void initView() {
        lv_homepage_recommend = (RecyclerView) findViewById(R.id.lv_homepage_recommend);
        recycler_view_home_recommend = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_home_recommend);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
    }

    @Override
    protected void initData() {
        setTitle("临证参考", "");
        intent = new Intent();
        page = 1;

        getHomeRecommend(page);
        adapter = new RecycleHomeRecommendAdapter(this);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        lv_homepage_recommend.setLayoutManager(new LinearLayoutManager(this));
        lv_homepage_recommend.setHasFixedSize(true);
        lv_homepage_recommend.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        //下拉刷新
        recycler_view_home_recommend.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getHomeRecommend(page);

            }
        });

        //上拉刷新
        recycler_view_home_recommend.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                getHomeRecommend(page);
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

    /**
     * 获取首页推荐
     */
    private void getHomeRecommend(final int page) {
        NetApi.getHomeRecommend(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                recycler_view_home_recommend.refreshComplete();
                recycler_view_home_recommend.setLoadMoreEnable(true);
                LogUtil.e("getHomeRecommend=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalReferenceActivity.this)){
                    recycler_view_home_recommend.loadMoreComplete(false);
                    return;
                }

                HomepageRecommendBean homepageRecommendBean = JsonUtil.json2Bean(result, HomepageRecommendBean.class);

                if (page == 1) {
                    data = homepageRecommendBean.data;
                } else {
                    if (homepageRecommendBean.data == null || (homepageRecommendBean.data).size() == 0) {
                        ToastUtil.show("没有数据了");
                    } else {
                        data.addAll(homepageRecommendBean.data);
                    }
                }

                if (data != null && data.size() != 0) {
                    adapter.updateAdapter(data,false);

                } else {
//                    refresh_more_every.setVisibility(View.GONE);
                }

                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                recycler_view_home_recommend.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, page + "");
    }

}
