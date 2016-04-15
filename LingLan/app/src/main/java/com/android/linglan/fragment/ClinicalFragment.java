package com.android.linglan.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.adapter.clinical.ClinicalAdapter;
import com.android.linglan.adapter.RecycleHomeSubjectAdapter;
import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ClinicalCreateActivity;
import com.android.linglan.ui.clinical.ClinicalDetailsActivity;
import com.android.linglan.ui.clinical.ClinicalPhotographActivity;
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
 * Created by LeeMy on 2016/4/6 0006.
 * 临证界面
 */
public class ClinicalFragment extends BaseFragment {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;

    private PopupWindow popupWindow;
    private View popView;

    private Intent intent = null;
    private RelativeLayout rl_clinical;
    private LinearLayout ll_no_network;
    private Button bt_clinical_create;
    private Button bt_clinical_photograph;
    private TextView tv_clinical_order;
    private PtrClassicFrameLayout recycler_view_clinical;
    private RecyclerView rec_clinical;
    private RecyclerAdapterWithHF mAdapter;
    private ClinicalAdapter clinicalAdapter;

    private RecycleHomeSubjectAdapter adapter;
    private RecommendSubjects recommendSubjects;
    public ArrayList<RecommendSubjects.RecommendSubject> data;
    private int page;//页码
    private String orderid;//排序 传参数 addtime按时间排序, count_view按统计排序，""为全部
    private String cateid;//分类 传参数分类id(subjectClassifyListBeans.cateid)，传""则返回全部
    private int location;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    rl_clinical.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    rl_clinical.setVisibility(View.GONE);
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
            rootView = inflater.inflate(R.layout.fragment_clinical, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();

        popView = LayoutInflater.from(getActivity()).inflate(R.layout.popupview_clinical_order, null);

        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {
        rl_clinical = (RelativeLayout) rootView.findViewById(R.id.rl_clinical);
        ll_no_network = (LinearLayout) rootView.findViewById(R.id.ll_no_network);
        bt_clinical_create = (Button) rootView.findViewById(R.id.bt_clinical_create);
        bt_clinical_photograph = (Button) rootView.findViewById(R.id.bt_clinical_photograph);
        tv_clinical_order = (TextView) rootView.findViewById(R.id.tv_clinical_order);
        recycler_view_clinical = (PtrClassicFrameLayout) rootView.findViewById(R.id.recycler_view_clinical);
        rec_clinical = (RecyclerView) rootView.findViewById(R.id.rec_clinical);

//        rootView.findViewById(R.id.go_clinical_details).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent.setClass(getActivity(), ClinicalDetailsActivity.class);
//                startActivity(intent);
//
//            }
//        });
    }

    @Override
    protected void initData() {
        intent = new Intent();
        popupWindow = new PopupWindow(getActivity());
        rec_clinical.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec_clinical.setHasFixedSize(true);
        page = 1;
//        getAllSubject(page, orderid, cateid);

//        adapter = new RecycleHomeSubjectAdapter(getActivity());
//        mAdapter = new RecyclerAdapterWithHF(adapter);
        clinicalAdapter = new ClinicalAdapter(getActivity());
        mAdapter = new RecyclerAdapterWithHF(clinicalAdapter);
        rec_clinical.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        ll_no_network.setOnClickListener(this);
        bt_clinical_create.setOnClickListener(this);
        tv_clinical_order.setOnClickListener(this);
        bt_clinical_photograph.setOnClickListener(this);

        //下拉刷新
        recycler_view_clinical.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
//                getAllSubject(page, orderid, cateid);
            }
        });

        //上拉刷新
        recycler_view_clinical.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
//                getAllSubject(page, orderid, cateid);
                recycler_view_clinical.loadMoreComplete(true);
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
            case R.id.bt_clinical_create:
                intent.setClass(getActivity(), ClinicalCreateActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_clinical_photograph:
                intent.setClass(getActivity(), ClinicalPhotographActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_clinical_order:
                showPopup(v);
                ToastUtil.show("henghengehneg");
                break;
        }
    }

    private void showPopup(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        popView.measure(w, h);

        int height = popView.getMeasuredHeight();
        int width = popView.getMeasuredWidth();
            popupWindow = new PopupWindow(popView, width, (int)height);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            int[] location = new int[2];
            v.getLocationOnScreen(location);

        int x = getResources().getDimensionPixelSize(R.dimen.dp14);
        int y = getResources().getDimensionPixelSize(R.dimen.dp14);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]-v.getWidth()+x, location[1] + v.getHeight()-y);

    }


    private void getAllSubject(final int page, String addtime, String cateid) {
        NetApi.getAllSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                recycler_view_clinical.refreshComplete();
                recycler_view_clinical.setLoadMoreEnable(true);

                LogUtil.d(getActivity().getPackageName(), "getDetailsArticle=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, getActivity())) {
                    recycler_view_clinical.loadMoreComplete(false);
                    return;
                }
                recommendSubjects = JsonUtil.json2Bean(result, RecommendSubjects.class);
//                data = recommendSubjects.data;
//                adapter.updateAdapter(data);

                if (page == 1) {
                    data = recommendSubjects.data;
                } else {
                    if (recommendSubjects.data == null || (recommendSubjects.data).size() == 0) {
                        ToastUtil.show("没有数据了");
                    } else {
                        data.addAll(recommendSubjects.data);
                    }
                }

                if (data != null && data.size() != 0) {
                    adapter.updateAdapter(data);
                }

                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                recycler_view_clinical.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, page + "", addtime, cateid);
    }
}
