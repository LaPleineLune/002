package com.android.linglan.ui.fm;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.linglan.adapter.fm.RadioPlaySpecialAdapter;
import com.android.linglan.adapter.fm.RadioPlaySpecialListAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.RadioClassifyBean;
import com.android.linglan.http.bean.RadioSpecialInfoBean;
import com.android.linglan.http.bean.RadioSpecialListBean;
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
 * Created by LeeMy on 2016/6/8 0008.
 * 专辑列表页面
 */
public class RadioSpecialActivity extends BaseActivity {
    private TextView tv_radio_go_play;
    private PtrClassicFrameLayout recycler_view_radio_special;
    private RecyclerView rec_radio_special;
    private RecyclerAdapterWithHF mAdapter;
    private RadioPlaySpecialAdapter adapter;
    private RadioSpecialInfoBean.RadioSpecialInfoData radioSpecialInfoData;
    private ArrayList<RadioSpecialListBean.RadioSpecialListData> radioSpecialListData;
    private String albumid;
    private int page;
    public boolean isLogin = false;

    @Override
    public void onResume() {
        super.onResume();
        if (isLogin) {
            page = 1;
            getRadioSpecialList(albumid, page);
            isLogin = false;
        }
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_radio_special);
    }

    @Override
    protected void initView() {
        tv_radio_go_play = (TextView) findViewById(R.id.tv_radio_go_play);
        recycler_view_radio_special = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_radio_special);
        rec_radio_special = (RecyclerView) findViewById(R.id.rec_radio_special);
    }

    @Override
    protected void initData() {
        String albumname = getIntent().getStringExtra("albumname");
        albumid = getIntent().getStringExtra("albumid");
        setTitle(albumname == null || albumname.equals("")? "专辑列表" : albumname, "");
        radioSpecialListData = new ArrayList<>();
        page = 1;
        getRadioSpecialInfo();
        getRadioSpecialList(albumid, page);
        rec_radio_special.setLayoutManager(new LinearLayoutManager(this));
        rec_radio_special.setHasFixedSize(true);
        adapter = new RadioPlaySpecialAdapter(this, albumid);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        rec_radio_special.setAdapter(mAdapter);
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
//                getCollectAll(page);
            }
        });

        //上拉刷新
        recycler_view_radio_special.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
//                getCollectAll(page);
                getRadioSpecialList(albumid, page);
                recycler_view_radio_special.loadMoreComplete(true);
            }
        });

        adapter.setCollectOnClickListener(new RadioPlaySpecialAdapter.CollectOnClickListener() {
            @Override
            public void collectOnClickListener(View view) {
                isLogin = true;
//                page = 1;
//                getRadioSpecialList(albumid, page);
            }
        });

        tv_radio_go_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = "http://syh-audio-video.bj.bcebos.com/Beyond%20-%20%E5%96%9C%E6%AC%A2%E4%BD%A0.mp3?authorization=bce-auth-v1%2Fa8b3944c4fda4921a837f9de6ff9941a%2F2016-06-16T06%3A48%3A24Z%2F300000%2Fhost%2F594d4a0ad57c1413ac322a535bf48c7699f9a664333fa0c48f4ac515a95fc82a";
                if(source == null || source.equals("")){
                    /**
                     * 简单检测播放源的合法性,不合法不播放
                     */
                    ToastUtil.show(RadioSpecialActivity.this, "please input your video source", 500);
                    source = "http://devimages.apple.com/iphone/samples/bipbop/gear4/prog_index.m3u8";
                    Intent intent = new Intent(RadioSpecialActivity.this, RadioPlayActivity.class);
                    intent.setData(Uri.parse(source));
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(RadioSpecialActivity.this, RadioPlayActivity.class);
                    intent.setData(Uri.parse(source));
                    startActivity(intent);
                }
//                startActivity(new Intent(RadioSpecialActivity.this, RadioPlayActivity.class));
            }
        });

    }

    /**
     * 专辑详情头部
     */
    private void getRadioSpecialInfo() {
        NetApi.getRadioSpecialInfo(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getRadioSpecialInfo:" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, RadioSpecialActivity.this)) {
                    return;
                }

                RadioSpecialInfoBean radioSpecialInfoBean = JsonUtil.json2Bean(result, RadioSpecialInfoBean.class);
                radioSpecialInfoData = radioSpecialInfoBean.data;
                adapter.updateAdapterTitle(radioSpecialInfoData);
            }

            @Override
            public void onFailure(String message) {

            }
        }, albumid);
    }

    /**
     * 专辑详情列表
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

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, RadioSpecialActivity.this)) {
                    recycler_view_radio_special.loadMoreComplete(false);
                    return;
                }
                RadioSpecialListBean radioSpecialListBean = JsonUtil.json2Bean(result, RadioSpecialListBean.class);
                if (page == 1) {
                    radioSpecialListData.clear();
                }
                radioSpecialListData.addAll(radioSpecialListBean.data);
                if (radioSpecialListData != null && radioSpecialListData.size() != 0) {
                    adapter.updateAdapter(radioSpecialListData);
                }
////                recommendSubjects = JsonUtil.json2Bean(result, RecommendSubjects.class);

//                for (RadioListBean.RadioListData data : radioListBean.data) {
//                    if (data.type.equals(Constants.RADIO_SPECIAL)) {
//                        radioSpecialListData.add(data);
//                    }
//                }
//
//                if (page == 1) {
////                    radioListData = radioListBean.data;
//                    if (radioListData != null && radioListData.size() < 10) {
//                        recycler_view_radio_special.loadPage1MoreComplete();
//                    } else {
//                        recycler_view_radio_special.loadMoreComplete(true);
//                    }
//
////                } else {
////                    if (radioListData == null || radioListData.size() == 0) {
////                        ToastUtil.show("没有数据了");
////                    } else {
////                        radioListData.addAll(radioListBean.data);
////                    }
//                }
//
//                if (radioListData != null && radioListData.size() != 0) {
//                    adapter.updateAdapter(radioListData);
//                }
//
//                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                recycler_view_radio_special.refreshComplete();
//                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, cateid ,page + "");
    }
}
