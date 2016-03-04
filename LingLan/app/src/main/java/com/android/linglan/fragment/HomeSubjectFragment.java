package com.android.linglan.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.adapter.HomepageSubjectAdapter;
import com.android.linglan.adapter.HomepageSubjectTypeAdapter;
import com.android.linglan.adapter.RecycleHomeSubjectAdapter;
import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllSubjectClassifyListBean;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.ui.homepage.SubjectDetailsActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.CustomPullToRefreshRecyclerView;
import com.android.linglan.widget.SyLinearLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

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
    private Intent intent;
    private int page;//页码
    private String orderid;//排序 传参数 addtime按时间排序, count_view按统计排序，""为全部
    private String cateid;//分类 传参数分类id(subjectClassifyListBeans.cateid)，传""则返回全部
    private HomepageSubjectTypeAdapter typeAdapter;
    private RecycleHomeSubjectAdapter adapter;
    public ArrayList<RecommendSubjects.RecommendSubject> data;
    public ArrayList<AllSubjectClassifyListBean.SubjectClassifyListBean> subjectClassifyListBean;

    private CustomPullToRefreshRecyclerView refresh_more_every;
    private RecyclerView recy_homepage_subject;
    private RecommendSubjects recommendSubjects;

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
        refresh_more_every = (CustomPullToRefreshRecyclerView) rootView.findViewById(R.id.refresh_more_every);
        bt_homepage_subject_type = (Button) rootView.findViewById(R.id.bt_homepage_subject_type);
        lv_homepage_subject_type = (ListView) popView.findViewById(R.id.lv_homepage_subject_type);
        recy_homepage_subject = refresh_more_every.getRefreshableView();
        recy_homepage_subject.setLayoutManager(new SyLinearLayoutManager(getActivity()));
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
        getAllSubject(page,orderid,cateid);

        typeAdapter = new HomepageSubjectTypeAdapter(getActivity());
        lv_homepage_subject_type.setAdapter(typeAdapter);

        adapter = new RecycleHomeSubjectAdapter(getActivity());
        recy_homepage_subject.setAdapter(adapter);

    }

    @Override
    protected void setListener() {
        bt_homepage_subject_type.setOnClickListener(this);

        lv_homepage_subject_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (0 == position) {
                    orderid = "";
                    cateid = "";
                    page = 1;
                    getAllSubject(page, orderid, cateid);
                } else if (SharedPreferencesUtil.getString("token", null) != null && 1 == position) {
                    getCollectSubject();
                } else {
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
                    case MotionEvent.ACTION_UP:
                        popupWindow.dismiss();
                        bt_homepage_subject_type.setVisibility(View.VISIBLE);
                        break;
                }
                return false;
            }
        });

        refresh_more_every.setRefreshCallback(new CustomPullToRefreshRecyclerView.RefreshCallback() {
            //上拉
            @Override
            public void onPullDownToRefresh() {
                page = 1;
                getAllSubject(page, orderid, cateid);
            }

            //下拉
            @Override
            public void onPullUpToLoadMore() {
                page++;
                getAllSubject(page, orderid, cateid);
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt_homepage_subject_type:
                showPopUp(v);
                bt_homepage_subject_type.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void showPopUp(View v) {

        popupWindow = new PopupWindow(popView,320,740);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.getHeight());

    }

    /**
     * 获取全部专题
     */
    private void getAllSubject(final int page,String addtime,String cateid) {
        NetApi.getAllSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                refresh_more_every.onRefreshComplete();
                LogUtil.d("getDetailsArticle=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
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
                    refresh_more_every.setVisibility(View.GONE);
                }

                for (RecommendSubjects.RecommendSubject recommendSubject : data) {
                LogUtil.e("是我是我还是我"+recommendSubject.toString());
                }
            }

            @Override
            public void onFailure(String message) {
                refresh_more_every.onRefreshComplete();
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

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
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
     * 获取我的收藏-专题
     */
    public void getCollectSubject() {
        NetApi.getCollectSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getCollectSubject" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }

                if (!TextUtils.isEmpty(result)) {
                    RecommendSubjects recommendSubjects = JsonUtil.json2Bean(result, RecommendSubjects.class);
                    data = recommendSubjects.data;
//                    adapter.updateAdapter(false, data);
                    adapter.updateAdapter(data);
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
