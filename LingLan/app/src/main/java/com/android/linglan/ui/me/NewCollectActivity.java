package com.android.linglan.ui.me;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.android.linglan.adapter.RecycleHomeRecommendAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.HomepageRecommendBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/1/7 0007.
 * 我的收藏
 */
public class NewCollectActivity extends BaseActivity implements View.OnClickListener {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private PtrClassicFrameLayout recycler_view_collect;
    private RecyclerView rec_collect;
    private RecyclerAdapterWithHF mAdapter;
    private RecycleHomeRecommendAdapter adapter;
    private LinearLayout ll_no_collect_subject;

//    private CustomPullToRefreshRecyclerView refresh_more_every;
    private RecyclerView RecycleCollection;
    private View rootView;
    private RecyclerView lv_homepage_recommend;
    private LinearLayout ll_no_network;
    public ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> data;
    private Intent intent;
    private int page;//页码

    public static  int position;//取消收藏的那条所在的页
    public static  int isCancle = 0;//是否取消收藏


    public boolean edit = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_FAILURE:
                    //原页面GONE掉，提示网络不好的页面出现
                    ll_no_collect_subject.setVisibility(View.GONE);
                    recycler_view_collect.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);

                    break;
                case REQUEST_SUCCESS:
                    //原页面GONE掉，提示网络不好的页面出现

                    if(data != null && data.size() != 0){
                        recycler_view_collect.setVisibility(View.VISIBLE);
                        ll_no_network.setVisibility(View.GONE);
                        ll_no_collect_subject.setVisibility(View.GONE);
                    }else{
                        recycler_view_collect.setVisibility(View.GONE);
                        ll_no_network.setVisibility(View.GONE);
                        ll_no_collect_subject.setVisibility(View.VISIBLE);
                    }

                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (recycler_view_collect == null) {
            recycler_view_collect = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_collect);
        }
        recycler_view_collect.refreshComplete();
        recycler_view_collect.setLoadMoreEnable(true);
        recycler_view_collect.loadMoreComplete(true);

        if(position > 0 &&  isCancle == 1){
            isCancle = 0;
            int count = data.size();
            LogUtil.e("count = " + count);
            LogUtil.e("position = " + position);
            if(position == 1){
                data.clear();
            }else{
                for(int i  = count -1; i > (position-1)*10 -1;i--){
                    data.remove(i);
                }
            }

            getCollectAll(position);
        }

//        page = 1;
//        getCollectAll(page);
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_new_collect);
    }

    @Override
    protected void initView() {
        recycler_view_collect = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_collect);
        rec_collect = (RecyclerView) findViewById(R.id.rec_collect);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        ll_no_collect_subject = (LinearLayout) findViewById(R.id.ll_no_collect_subject);
}

    @Override
    protected void initData() {
        setTitle("我的收藏", "编辑");
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.edit);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
        data = new ArrayList<HomepageRecommendBean.HomepageRecommendBeanData>();
        page = 1;
        getCollectAll(page);

        rec_collect.setLayoutManager(new LinearLayoutManager(this));
        rec_collect.setHasFixedSize(true);
        adapter = new RecycleHomeRecommendAdapter(this);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        rec_collect.setAdapter(mAdapter);
    }


    @Override
    protected void setListener() {
        right.setOnClickListener(this);
        ll_no_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                initData();
                recycler_view_collect.refreshComplete();
                recycler_view_collect.setLoadMoreEnable(true);
                recycler_view_collect.loadMoreComplete(true);
                page = 1;
                getCollectAll(page);
            }
        });
        //下拉刷新
        recycler_view_collect.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                recycler_view_collect.refreshComplete();
                recycler_view_collect.setLoadMoreEnable(true);
                recycler_view_collect.loadMoreComplete(true);
                page = 1;
                getCollectAll(page);
            }
        });

        //上拉刷新
        recycler_view_collect.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                getCollectAll(page);
                recycler_view_collect.loadMoreComplete(true);
            }
        });

    }

    /**
     * 获取收藏的内容
     */
    private void getCollectAll(final int page) {
        NetApi.getCollectAll(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getHomeRecommend=" + result);
//                refresh_more_every.onRefreshComplete();
                recycler_view_collect.refreshComplete();
                recycler_view_collect.setLoadMoreEnable(true);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, NewCollectActivity.this)) {
                    recycler_view_collect.loadMoreComplete(false);
                    return;
                }

                HomepageRecommendBean homepageRecommendBean = JsonUtil.json2Bean(result, HomepageRecommendBean.class);

                if (page == 1) {
//                    data = homepageRecommendBean.data;
                    data.clear();
                    for (HomepageRecommendBean.HomepageRecommendBeanData hrd : homepageRecommendBean.data) {
                        if (hrd.type.equals(Constants.ARTICLE) || hrd.type.equals(Constants.SUBJECT) || hrd.type.equals(Constants.RADIO_SPECIAL) || hrd.type.equals(Constants.RADIO_SINGLE)) {
                            data.add(hrd);
                        }
                    }
                } else {
                        for (HomepageRecommendBean.HomepageRecommendBeanData hrd : homepageRecommendBean.data) {
                            if (hrd.type.equals(Constants.ARTICLE) || hrd.type.equals(Constants.SUBJECT) || hrd.type.equals(Constants.RADIO_SPECIAL) || hrd.type.equals(Constants.RADIO_SINGLE)) {
                                data.add(hrd);
                            }
                        }
//                    }
                }

                if (homepageRecommendBean.data == null || (homepageRecommendBean.data).size() == 0 || homepageRecommendBean.data.size() <= 9) {
                    if (page == 1) {
                        recycler_view_collect.loadPage1MoreComplete();
                    } else {
                        recycler_view_collect.loadMoreComplete(false);
                    }
                }

                if (data != null && data.size() != 0) {
                    adapter.updateAdapter(data, edit);
                } else {
                    recycler_view_collect.setVisibility(View.GONE);
                }
                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
//                refresh_more_every.onRefreshComplete();
                recycler_view_collect.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, page + "");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right:
                if (right.getText().toString().equals("编辑")) {
                    setTitle("我的收藏", "完成");
                    Drawable collectTopDrawable = getResources().getDrawable(R.drawable.edit_ok);
                    collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                    right.setCompoundDrawables(collectTopDrawable, null, null, null);
                    edit = true;
                    adapter.updateAdapter(data, edit);
                } else {
                    setTitle("我的收藏", "编辑");
                    Drawable collectTopDrawable = getResources().getDrawable(R.drawable.edit);
                    collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                    right.setCompoundDrawables(collectTopDrawable, null, null, null);
                    edit = false;
                    adapter.updateAdapter(data, edit);
                    getCollectAll(1);
                }
                break;
            default:
                break;
        }
    }

}
