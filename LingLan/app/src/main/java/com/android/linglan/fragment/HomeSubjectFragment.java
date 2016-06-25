package com.android.linglan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.linglan.adapter.RecycleHomeSubjectAdapter;
import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.SyLinearLayoutManager;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/2/26 0026.
 * 专题
 */
public class HomeSubjectFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private LinearLayout ll_no_network;
    private RelativeLayout rl_home_subject;
    private LinearLayout ll_no_collect_subject;
    private Intent intent;
    private int page;//页码
    private String orderid = "";//排序 传参数 addtime按时间排序, count_view按统计排序，""为全部
    private String cateid = "";//分类 传参数分类id(subjectClassifyListBeans.cateid)，传""则返回全部
    private int count = 0;
    private RecycleHomeSubjectAdapter adapter;
    public ArrayList<RecommendSubjects.RecommendSubject> data;
    public ArrayList<RecommendSubjects.RecommendSubject> colectionData;

    private PtrClassicFrameLayout recycler_view_home_recommend;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView recy_homepage_subject;
    private RecommendSubjects recommendSubjects;
    private int location;
    public static int position;
    public static int isCancle = 0;

    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    protected static final int COLLECT_SUBJECT_REQUEST_SUCCESS = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_FAILURE:
                    //原页面GONE掉，提示网络不好的页面出现
                    ll_no_collect_subject.setVisibility(View.GONE);
                    rl_home_subject.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
                case COLLECT_SUBJECT_REQUEST_SUCCESS:
                    //没有收藏时，出现没有收藏的页面
                    //原页面GONE掉，提示网络不好的页面出现
                    if (colectionData == null || colectionData.size() == 0) {
                        rl_home_subject.setVisibility(View.GONE);
                        ll_no_network.setVisibility(View.GONE);
                        ll_no_collect_subject.setVisibility(View.VISIBLE);
                    } else {
                        ll_no_collect_subject.setVisibility(View.GONE);
                        rl_home_subject.setVisibility(View.VISIBLE);
                        ll_no_network.setVisibility(View.GONE);
                    }

                    break;
                case REQUEST_SUCCESS:
                    //原页面GONE掉，提示网络不好的页面出现
                    ll_no_collect_subject.setVisibility(View.GONE);
                    rl_home_subject.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (isCancle == 2) {
                isCancle = 0;
                page = 1;
                getCollectSubject(page);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        orderid = args != null ? args.getString("orderid") : "";
        cateid = args != null ? args.getString("cateid") : "";
//        count = args != null ? args.getInt("count", 0) : 0;
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home_subject, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (position > 0 && cateid.equals("-2") && isCancle == 1) {
            isCancle = 0;
            int count = colectionData.size();
            if (position == 1) {
                colectionData.clear();
            } else {
                for (int i = count - 1; i > (position - 1) * 10 - 1; i--) {
                    colectionData.remove(i);
                }
            }
            getCollectSubject(position);
        }
        page = position;
    }

    @Override
    protected void initView() {
        rl_home_subject = (RelativeLayout) rootView.findViewById(R.id.rl_home_subject);
        ll_no_network = (LinearLayout) rootView.findViewById(R.id.ll_no_network);
        ll_no_collect_subject = (LinearLayout) rootView.findViewById(R.id.ll_no_collect_subject);
        if (recycler_view_home_recommend == null) {
            recycler_view_home_recommend = (PtrClassicFrameLayout) rootView.findViewById(R.id.recycler_view_home_recommend);
        }

        if (recy_homepage_subject == null) {
            recy_homepage_subject = (RecyclerView) rootView.findViewById(R.id.recy_homepage_subject);
        }

        recy_homepage_subject.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy_homepage_subject.setHasFixedSize(true);
    }

    @Override
    protected void initData() {
        page = 1;
        intent = new Intent();
        getNetApi(1);
        if (adapter == null) {
            adapter = new RecycleHomeSubjectAdapter(getActivity());
        }
        if (mAdapter == null) {
            mAdapter = new RecyclerAdapterWithHF(adapter);
            recy_homepage_subject.setAdapter(mAdapter);
        }

    }

    private void getNetApi(int newPage) {
//        page = 1;
        if (cateid != null && cateid.equals("-1")) {
            orderid = "";
            cateid = "";
            getAllSubject(newPage, orderid, cateid);
        } else if (SharedPreferencesUtil.getString("token", null) != null && cateid != null && cateid.equals("-2")) {
            getCollectSubject(newPage);
        } else {
            getAllSubject(newPage, orderid, cateid);
        }
    }

    @Override
    protected void setListener() {
        recycler_view_home_recommend = (PtrClassicFrameLayout) rootView.findViewById(R.id.recycler_view_home_recommend);
        //下拉刷新
        recycler_view_home_recommend.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getNetApi(page);
            }
        });

        //上拉刷新
        recycler_view_home_recommend.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                getNetApi(page);
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        }
    }


    /**
     * 获取全部专题
     */
    private void getAllSubject(final int page, String addtime, final String cateid) {
        NetApi.getAllSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                recycler_view_home_recommend.refreshComplete();
                recycler_view_home_recommend.setLoadMoreEnable(true);

                LogUtil.e(getActivity().getPackageName(), "page = " + page + "  cateid = " + cateid + "  getAllSubject=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, getActivity())) {
                    recycler_view_home_recommend.loadMoreComplete(false);
                    return;
                }
                recommendSubjects = JsonUtil.json2Bean(result, RecommendSubjects.class);

                if (page == 1) {
                    data = recommendSubjects.data;
                    if (data != null && data.size() < 10) {
                        recycler_view_home_recommend.loadPage1MoreComplete();
                    } else {
                        recycler_view_home_recommend.loadMoreComplete(true);
                    }

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
                recycler_view_home_recommend.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, page + "", addtime, cateid);
    }

    /**
     * 获取我的收藏-专题  加分页page
     */
    public void getCollectSubject(final int page) {
        NetApi.getCollectSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                recycler_view_home_recommend.refreshComplete();
                recycler_view_home_recommend.setLoadMoreEnable(true);

                LogUtil.e(getActivity().getPackageName(), "getCollectSubject=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, getActivity())) {
                    recycler_view_home_recommend.loadMoreComplete(false);
                    if (HttpCodeJugementUtil.code == 1 && page == 1) {
//                        ToastUtil.show("没有收藏的专题");
                        colectionData = null;
                        adapter.updateAdapter(null);
                        handler.sendEmptyMessage(COLLECT_SUBJECT_REQUEST_SUCCESS);
                    }
                    return;
                }

                if (!TextUtils.isEmpty(result)) {
                    RecommendSubjects recommendSubjects = JsonUtil.json2Bean(result, RecommendSubjects.class);
                    if (page == 1) {
                        colectionData = recommendSubjects.data;
                        if (colectionData != null && colectionData.size() < 10) {
                            recycler_view_home_recommend.loadPage1MoreComplete();
                        } else {
                            recycler_view_home_recommend.loadMoreComplete(true);
                        }
                    } else {
                        if (recommendSubjects.data == null || (recommendSubjects.data).size() == 0) {
//                            ToastUtil.show("没有数据了");
                        } else {
                            colectionData.addAll(recommendSubjects.data);
                        }
                    }
                    adapter.updateAdapter(colectionData);
                }

                handler.sendEmptyMessage(COLLECT_SUBJECT_REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                recycler_view_home_recommend.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, page + "");
    }
}
