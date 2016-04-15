package com.android.linglan.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.android.linglan.LinglanApplication;
import com.android.linglan.adapter.HomepageSubjectTypeAdapter;
import com.android.linglan.adapter.RecycleHomeSubjectAdapter;
import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllSubjectClassifyListBean;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.NetworkUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.CustomPullToRefreshRecyclerView;
import com.android.linglan.widget.SyLinearLayoutManager;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/2/26 0026.
 * 首页专题
 */
public class HomeSubjectFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private PopupWindow popupWindow;
    private View popView;
    private ListView lv_homepage_subject_type;
    private ListView lv_homepage_subject;
    private Button bt_homepage_subject_type;
    private LinearLayout ll_no_network;
    private RelativeLayout rl_home_subject;
    private Intent intent;
    private int page;//页码
    private String orderid;//排序 传参数 addtime按时间排序, count_view按统计排序，""为全部
    private String cateid;//分类 传参数分类id(subjectClassifyListBeans.cateid)，传""则返回全部
    private HomepageSubjectTypeAdapter typeAdapter;
    private RecycleHomeSubjectAdapter adapter;
    public ArrayList<RecommendSubjects.RecommendSubject> data;
    public ArrayList<RecommendSubjects.RecommendSubject> colectionData;
    public ArrayList<AllSubjectClassifyListBean.SubjectClassifyListBean> subjectClassifyListBean;

    private PtrClassicFrameLayout recycler_view_home_recommend;
    private RecyclerAdapterWithHF mAdapter;
//    private CustomPullToRefreshRecyclerView refresh_more_every;
    private RecyclerView recy_homepage_subject;
    private RecommendSubjects recommendSubjects;
    private int location;

    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_FAILURE:
                    //原页面GONE掉，提示网络不好的页面出现
                    rl_home_subject.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);

                    break;
                case REQUEST_SUCCESS:
                    //原页面GONE掉，提示网络不好的页面出现
                    rl_home_subject.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        bt_homepage_subject_type.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home_subject, null);
            popView = LayoutInflater.from(getActivity()).inflate(R.layout.popupview_home_subject, null);
//            popView = inflater.inflate(R.layout.popupview_home_subject, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {
        rl_home_subject = (RelativeLayout) rootView.findViewById(R.id.rl_home_subject);
        ll_no_network = (LinearLayout) rootView.findViewById(R.id.ll_no_network);
        recycler_view_home_recommend = (PtrClassicFrameLayout) rootView.findViewById(R.id.recycler_view_home_recommend);
        bt_homepage_subject_type = (Button) rootView.findViewById(R.id.bt_homepage_subject_type);
        lv_homepage_subject_type = (ListView) popView.findViewById(R.id.lv_homepage_subject_type);
        recy_homepage_subject = (RecyclerView) rootView.findViewById(R.id.recy_homepage_subject);
        recy_homepage_subject.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy_homepage_subject.setHasFixedSize(true);
    }

    @Override
    protected void initData() {
//        initializeSizesExpandableSelector();
        getAllSubjectClassifyList();
        popupWindow = new PopupWindow(getActivity());
        intent = new Intent();
        orderid = "";
        cateid = "";
        page = 1;
        location = 0;
        getAllSubject(page, orderid, cateid);

        typeAdapter = new HomepageSubjectTypeAdapter(getActivity());
        lv_homepage_subject_type.setAdapter(typeAdapter);

        adapter = new RecycleHomeSubjectAdapter(getActivity());
        mAdapter = new RecyclerAdapterWithHF(adapter);
        recy_homepage_subject.setAdapter(mAdapter);

    }

    @Override
    protected void setListener() {
        bt_homepage_subject_type.setOnClickListener(this);
        location = 0;
        lv_homepage_subject_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                location = position;
                page = 1;
                if (0 == position) {
                    orderid = "";
                    cateid = "";
//                    page = 1;
                    getAllSubject(page, orderid, cateid);
                } else if (SharedPreferencesUtil.getString("token", null) != null && 1 == position) {
                    getCollectSubject(page);
                } else {
//                    page = 1;
                    cateid = subjectClassifyListBean.get(position).cateid;
                    getAllSubject(page, orderid, cateid);
                    bt_homepage_subject_type.setVisibility(View.VISIBLE);
                }
                popupWindow.dismiss();
                bt_homepage_subject_type.setVisibility(View.VISIBLE);
            }
        });

        recy_homepage_subject.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                        popupWindow.dismiss();
                        bt_homepage_subject_type.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        popupWindow.dismiss();
                        bt_homepage_subject_type.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                        popupWindow.dismiss();
                        bt_homepage_subject_type.setVisibility(View.VISIBLE);
                        break;
                }

                return false;
            }
        });

        //下拉刷新
        recycler_view_home_recommend.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                bt_homepage_subject_type.setVisibility(View.VISIBLE);
                if (subjectClassifyListBean == null || subjectClassifyListBean.size() == 0) {
                    getAllSubjectClassifyList();
                }

                page = 1;
                if (SharedPreferencesUtil.getString("token", null) != null && 1 == location && colectionData != null && colectionData.size() > 0) {
                    getCollectSubject(page);
                } else {
                    getAllSubject(page, orderid, cateid);
                }
            }
        });

        //上拉刷新
        recycler_view_home_recommend.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                bt_homepage_subject_type.setVisibility(View.VISIBLE);

                if (subjectClassifyListBean == null || subjectClassifyListBean.size() == 0) {
                    getAllSubjectClassifyList();
                }

                page++;

                if (SharedPreferencesUtil.getString("token", null) != null && 1 == location && colectionData != null && colectionData.size() > 0) {
                    getCollectSubject(page);
                } else {
                    getAllSubject(page, orderid, cateid);
                }

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
            case R.id.bt_homepage_subject_type:
                if (!NetworkUtil.isNetworkConnected(LinglanApplication.getsApplicationContext())) {
                    ToastUtil.show(PasserbyClient.NETWORK_ERROR_MESSAGE);
                    return;
                }
                getAllSubjectClassifyList();
                if (subjectClassifyListBean == null || subjectClassifyListBean.size() == 0) {
                    ToastUtil.show("暂无数据，请重试...");
                    return;
                }
                showPopUp(v);
                bt_homepage_subject_type.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void showPopUp(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        popView.measure(w, h);

        Resources r=getResources();

        float height =r.getDimension(R.dimen.dp288);


//        int height = popView.getMeasuredHeight();
        int width = popView.getMeasuredWidth();
        if (subjectClassifyListBean != null && subjectClassifyListBean.size() != 0) {
            popupWindow = new PopupWindow(popView, width, (int)height);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            int[] location = new int[2];
            v.getLocationOnScreen(location);

//        popupWindow.showAtLocation(v, Gravity.RIGHT|Gravity.TOP,0,0);
            popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.getHeight());

//            popupWindow.setOutsideTouchable(false);
//            popupWindow.setBackgroundDrawable(new BitmapDrawable());
//            popupWindow.setTouchable(true);
//            popupWindow.setFocusable(true);
//            popupWindow.update();
        }
    }

    /**
     * 获取全部专题
     */
    private void getAllSubject(final int page, String addtime, String cateid) {
        NetApi.getAllSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                recycler_view_home_recommend.refreshComplete();
                recycler_view_home_recommend.setLoadMoreEnable(true);

                LogUtil.d(getActivity().getPackageName(), "getDetailsArticle=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result,getActivity())) {
                    recycler_view_home_recommend.loadMoreComplete(false);
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
        }, page + "", addtime, cateid);
    }

    /**
     * 全部专题分类列表
     */
    private void getAllSubjectClassifyList() {
        NetApi.getAllSubjectClassifyList(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result,getActivity())) {
                    return;
                }

                AllSubjectClassifyListBean recommendSubjects = JsonUtil.json2Bean(result, AllSubjectClassifyListBean.class);
                subjectClassifyListBean = recommendSubjects.data;
                subjectClassifyListBean.add(0, new AllSubjectClassifyListBean.SubjectClassifyListBean("", "全部专题", "", ""));
                if (SharedPreferencesUtil.getString("token", null) != null) {
                    subjectClassifyListBean.add(1, new AllSubjectClassifyListBean.SubjectClassifyListBean("", "已收藏", "", ""));
                }
                typeAdapter.updateAdapter(subjectClassifyListBean);
            }

            @Override
            public void onFailure(String message) {

            }
        });
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

                LogUtil.d(getActivity().getPackageName(), "getCollectSubject=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result,getActivity())) {

                    recycler_view_home_recommend.loadMoreComplete(false);
                    return;
                }

                if (!TextUtils.isEmpty(result)) {
                    RecommendSubjects recommendSubjects = JsonUtil.json2Bean(result, RecommendSubjects.class);
                    if (page == 1) {
                        colectionData = recommendSubjects.data;
                    } else {
                        if (recommendSubjects.data == null || (recommendSubjects.data).size() == 0) {
                            ToastUtil.show("没有数据了");
                        } else {
                            colectionData.addAll(recommendSubjects.data);
                        }
                    }
//                    colectionData = recommendSubjects.data;
//                    adapter.updateAdapter(false, data);
                    adapter.updateAdapter(colectionData);
                }
            }

            @Override
            public void onFailure(String message) {
                recycler_view_home_recommend.refreshComplete();
            }
        }, page + "");
    }
}
