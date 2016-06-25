package com.android.linglan.ui.fm;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
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
 * Created by LeeMy on 2016/6/8 0008.
 * 音频批量下载页面
 */
public class RadioBatchDownloadingActivity extends BaseActivity {
    private PtrClassicFrameLayout recycler_view_radio_special;
    private RecyclerView rec_radio_special;
    private ArrayList<RadioSpecialListBean.RadioSpecialListData> radioSpecialListData;
    private RecyclerAdapterWithHF mAdapter;
    private int page;
    private String albumid;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_radio_batch_download);
    }

    @Override
    protected void initView() {
        recycler_view_radio_special = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_radio_special);
        rec_radio_special = (RecyclerView) findViewById(R.id.rec_radio_special);
    }

    @Override
    protected void initData() {
        setTitle("正在下载", "删除");
        albumid = getIntent().getStringExtra("albumid");
        page = 1;
        radioSpecialListData = new ArrayList<RadioSpecialListBean.RadioSpecialListData>();
        rec_radio_special.setLayoutManager(new LinearLayoutManager(this));
        rec_radio_special.setHasFixedSize(true);
//        mAdapter = new RecyclerAdapterWithHF(adapter);
        rec_radio_special.setAdapter(mAdapter);
        getRadioSpecialList(albumid, page);
    }

    @Override
    protected void setListener() {
        //下拉刷新
        recycler_view_radio_special.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                recycler_view_radio_special.refreshComplete();
                recycler_view_radio_special.setLoadMoreEnable(true);
                recycler_view_radio_special.loadMoreComplete(true);
                page = 1;
                getRadioSpecialList(albumid, page);
            }
        });

        //上拉刷新
        recycler_view_radio_special.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                getRadioSpecialList(albumid, page);
                recycler_view_radio_special.loadMoreComplete(true);
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 专辑详情列表
     *
     * @param cateid
     * @param page
     */
    private void getRadioSpecialList(final String cateid, final int page) {
        NetApi.getRadioSpecialList(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                recycler_view_radio_special.refreshComplete();
                recycler_view_radio_special.setLoadMoreEnable(true);

                LogUtil.e("getRadioSpecialList=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, RadioBatchDownloadingActivity.this)) {
                    recycler_view_radio_special.loadMoreComplete(false);
                    return;
                }
                RadioSpecialListBean radioSpecialListBean = JsonUtil.json2Bean(result, RadioSpecialListBean.class);
                if (page == 1) {
                    radioSpecialListData.clear();
                }
                radioSpecialListData.addAll(radioSpecialListBean.data);
                if (radioSpecialListData != null && radioSpecialListData.size() != 0) {
//                    adapter.updateAdapter(radioSpecialListData);
                }
//                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                recycler_view_radio_special.refreshComplete();
//                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, cateid, page + "");
    }
}
