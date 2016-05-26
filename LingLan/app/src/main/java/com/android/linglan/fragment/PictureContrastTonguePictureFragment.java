package com.android.linglan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.adapter.clinical.PictureContrastAdapter;
import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.PictureContrastBean;
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
 * Created by LeeMy on 2016/4/15 0015.
 * 图片对比中的舌象对比
 */
public class PictureContrastTonguePictureFragment extends BaseFragment {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    protected static final int REQUEST_SUCCESS_NO_CONTENT = 2;
    protected static final int REQUEST_SUCCESS_CONTENT = 3;
    private View rootView;
    private LinearLayout ll_picture_contrast_all;
    private LinearLayout ll_no_network;
    private PtrClassicFrameLayout recycler_view_picture_contrast_all;
    private RecyclerView rec_picture_contrast_all;
    private TextView tv_picture_no_content;
    private RecyclerAdapterWithHF mAdapter;
    private PictureContrastAdapter pictureContrastAdapter;
    private ArrayList<PictureContrastBean.PictureContrastData> pictureContrastData;
    private Intent intent = null;
    private String category = "2"; //分类（处方默认1、患处3、舌象2、其他4）
    private String illnesscaseid = "";//病历id
    private int page;//页码

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ll_picture_contrast_all.setVisibility(View.VISIBLE);
                    recycler_view_picture_contrast_all.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    ll_picture_contrast_all.setVisibility(View.GONE);
                    recycler_view_picture_contrast_all.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_SUCCESS_CONTENT:
                    ll_picture_contrast_all.setVisibility(View.VISIBLE);
                    recycler_view_picture_contrast_all.setVisibility(View.VISIBLE);
                    tv_picture_no_content.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_SUCCESS_NO_CONTENT:
                    ll_picture_contrast_all.setVisibility(View.VISIBLE);
                    recycler_view_picture_contrast_all.setVisibility(View.GONE);
                    tv_picture_no_content.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_picture_contrast_all, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {
        ll_picture_contrast_all = (LinearLayout) rootView.findViewById(R.id.ll_picture_contrast_all);
        ll_no_network = (LinearLayout) rootView.findViewById(R.id.ll_no_network);
        recycler_view_picture_contrast_all = (PtrClassicFrameLayout) rootView.findViewById(R.id.recycler_view_picture_contrast_all);
        rec_picture_contrast_all = (RecyclerView) rootView.findViewById(R.id.rec_picture_contrast_all);
        tv_picture_no_content = (TextView) rootView.findViewById(R.id.tv_picture_no_content);

    }

    @Override
    protected void initData() {
        intent = new Intent();
        illnesscaseid = getActivity().getIntent().getStringExtra("illnesscaseid");
        page = 1;
        getComparePicture(category, illnesscaseid, page);
        rec_picture_contrast_all.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec_picture_contrast_all.setHasFixedSize(true);
        pictureContrastAdapter = new PictureContrastAdapter(getActivity());
        mAdapter = new RecyclerAdapterWithHF(pictureContrastAdapter);
        rec_picture_contrast_all.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        ll_no_network.setOnClickListener(this);
        //下拉刷新
        recycler_view_picture_contrast_all.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getComparePicture(category, illnesscaseid, page);
            }
        });

        //上拉刷新
        recycler_view_picture_contrast_all.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                getComparePicture(category, illnesscaseid, page);
                recycler_view_picture_contrast_all.loadMoreComplete(true);
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

    //获取对比图片
    private void getComparePicture(String category, String illnesscaseid, final int page){
        NetApi.getComparePicture(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getPatientDetails=" + result);
                recycler_view_picture_contrast_all.refreshComplete();
                recycler_view_picture_contrast_all.setLoadMoreEnable(true);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result, getActivity())){
                    recycler_view_picture_contrast_all.loadMoreComplete(false);
                    if (HttpCodeJugementUtil.code == 1 && page == 1) {
                        handler.sendEmptyMessage(REQUEST_SUCCESS_NO_CONTENT);
                    }
                    return;
                }

                handler.sendEmptyMessage(REQUEST_SUCCESS_CONTENT);
                handler.sendEmptyMessage(REQUEST_SUCCESS);
                PictureContrastBean pictureContrastBean = JsonUtil.json2Bean(result, PictureContrastBean.class);

                if (page == 1) {
                    pictureContrastData = pictureContrastBean.data;
                }else{
                    pictureContrastData.addAll(pictureContrastBean.data);
                }

//                PictureContrastBean pictureContrastBean = JsonUtil.json2Bean(result, PictureContrastBean.class);
//                pictureContrastData = pictureContrastBean.data;
                pictureContrastAdapter.updateAdapter(pictureContrastData);
//                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                recycler_view_picture_contrast_all.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, category, illnesscaseid, page + "");
    }
}
