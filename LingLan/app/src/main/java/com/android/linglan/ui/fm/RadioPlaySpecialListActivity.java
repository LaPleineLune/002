package com.android.linglan.ui.fm;

import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.android.linglan.adapter.fm.RadioPlaySpecialListAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.RadioPlayListBean;
import com.android.linglan.http.bean.RadioSpecialListBean;
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
 * Created by LeeMy on 2016/6/15 0015.
 * 音频播放中的专辑列表页面
 */
public class RadioPlaySpecialListActivity extends BaseActivity {
    private RelativeLayout rl_radio_special_list;
    private PtrClassicFrameLayout recycler_view_radio_play;
    private RecyclerView rec_radio_play;
    private RecyclerAdapterWithHF mAdapter;
    private RadioPlaySpecialListAdapter adapter;
    private ArrayList<RadioPlayListBean.RadioPlayListData> radioPlayListData;
    private String audioid = "";
    private String albumid = "";
    private int page;

    @Override
    protected void setView() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_radio_special_list);
    }

    @Override
    protected void initView() {
        rl_radio_special_list = (RelativeLayout) findViewById(R.id.rl_radio_special_list);
        recycler_view_radio_play = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_radio_play);
        rec_radio_play = (RecyclerView) findViewById(R.id.rec_radio_play);

    }

    @Override
    protected void initData() {
        setTitle("播放列表", "");
        back.setVisibility(View.GONE);
        audioid = getIntent().getStringExtra("audioid");
        albumid = getIntent().getStringExtra("albumid");
        radioPlayListData = new ArrayList<>();
        page = 1;
        getRadioPlayList(page);
        rec_radio_play.setLayoutManager(new LinearLayoutManager(this));
        rec_radio_play.setHasFixedSize(true);
        adapter = new RadioPlaySpecialListAdapter(this, audioid);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        rec_radio_play.setAdapter(mAdapter);

    }

    @Override
    protected void setListener() {
        rl_radio_special_list.setOnClickListener(this);
        //下拉刷新
        recycler_view_radio_play.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                recycler_view_radio_play.refreshComplete();
                recycler_view_radio_play.setLoadMoreEnable(true);
                recycler_view_radio_play.loadMoreComplete(true);
                page = 1;
                getRadioPlayList(page);
            }
        });

        //上拉刷新
        recycler_view_radio_play.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
//                getRadioPlayList(page);
//                recycler_view_radio_play.loadMoreComplete(true);
                recycler_view_radio_play.loadMoreComplete(false);
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_radio_special_list:
                finish();
                int version = Integer.valueOf(Build.VERSION.SDK_INT);
                if (version > 5) {
                    overridePendingTransition(R.anim.alpha_out, 0);
                }
                break;
        }
    }

    private void getRadioPlayList(final int page) {
        NetApi.getRadioPlayList(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                recycler_view_radio_play.refreshComplete();
                recycler_view_radio_play.setLoadMoreEnable(true);

                LogUtil.e("getRadioPlayList=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, RadioPlaySpecialListActivity.this)) {
                    recycler_view_radio_play.loadMoreComplete(false);
                    return;
                }
                RadioPlayListBean radioPlayListBean = JsonUtil.json2Bean(result, RadioPlayListBean.class);
                if (page == 1) {
                    radioPlayListData.clear();
                }
                radioPlayListData.addAll(radioPlayListBean.data);
                if (radioPlayListData != null && radioPlayListData.size() != 0) {
                    adapter.updateAdapter(radioPlayListData);
                }
//                adapter.updateAdapter();
            }

            @Override
            public void onFailure(String message) {
                recycler_view_radio_play.refreshComplete();

            }
        }, albumid, page + "");
    }
}
