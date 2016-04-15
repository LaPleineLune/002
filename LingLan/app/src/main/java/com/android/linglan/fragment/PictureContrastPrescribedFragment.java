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

import com.android.linglan.adapter.clinical.ClinicalDetailsAdapter;
import com.android.linglan.adapter.clinical.PictureContrastAdapter;
import com.android.linglan.base.BaseFragment;
import com.android.linglan.ui.R;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

/**
 * Created by LeeMy on 2016/4/15 0015.
 * 图片对比中的处方对比
 */
public class PictureContrastPrescribedFragment extends BaseFragment {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private View rootView;
    private LinearLayout ll_picture_contrast_all;
    private LinearLayout ll_no_network;
    private PtrClassicFrameLayout recycler_view_picture_contrast_all;
    private RecyclerView rec_picture_contrast_all;
    private RecyclerAdapterWithHF mAdapter;
    private PictureContrastAdapter pictureContrastAdapter;
    private Intent intent = null;
    private int page;//页码

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ll_picture_contrast_all.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    ll_picture_contrast_all.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
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

    }

    @Override
    protected void initData() {
        intent = new Intent();
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
//                getAllSubject(page, orderid, cateid);
            }
        });

        //上拉刷新
        recycler_view_picture_contrast_all.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
//                getAllSubject(page, orderid, cateid);
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
}
