package com.android.linglan.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.linglan.adapter.HomepageRecommendAdapter;
import com.android.linglan.adapter.HomepageSubjectAdapter;
import com.android.linglan.adapter.RecycleHomeRecommendAdapter;
import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllSubjectClassifyListBean;
import com.android.linglan.http.bean.HomepageRecommendBean;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.ui.homepage.ArticleDetailsActivity;
import com.android.linglan.ui.homepage.SubjectDetailsActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.CustomPullToRefreshRecyclerView;
import com.android.linglan.widget.SyLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/2/26 0026.
 * 首页推荐
 */
public class HomeRecommendFragment extends BaseFragment {
    private View rootView;
    private RecyclerView lv_homepage_recommend;
    private RecycleHomeRecommendAdapter adapter;
    public ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> data;
    private Intent intent;
    private int page;//页码
    private CustomPullToRefreshRecyclerView refresh_more_every;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home_recommend, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {
//        lv_homepage_recommend = (ListView) rootView.findViewById(R.id.lv_homepage_recommend);
        refresh_more_every = (CustomPullToRefreshRecyclerView) rootView.findViewById(R.id.refresh_more_every);
        lv_homepage_recommend = refresh_more_every.getRefreshableView();
        lv_homepage_recommend.setLayoutManager(new SyLinearLayoutManager(getActivity()));
        lv_homepage_recommend.setHasFixedSize(true);
    }

    @Override
    protected void initData() {
        intent = new Intent();
        page = 1;
        getHomeRecommend(page);
        adapter = new RecycleHomeRecommendAdapter(getActivity());
        lv_homepage_recommend.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        refresh_more_every.setRefreshCallback(new CustomPullToRefreshRecyclerView.RefreshCallback() {
            //上拉
            @Override
            public void onPullDownToRefresh() {
                page = 1;
                getHomeRecommend(page);
            }

            //下拉
            @Override
            public void onPullUpToLoadMore() {
                page++;
                getHomeRecommend(page);
            }
        });
    }

    /**
     * 获取首页推荐
     */
    private void getHomeRecommend(final int page) {
        NetApi.getHomeRecommend(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                refresh_more_every.onRefreshComplete();
                LogUtil.d("getHomeRecommend=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }

                HomepageRecommendBean homepageRecommendBean = JsonUtil.json2Bean(result, HomepageRecommendBean.class);

                if (page == 1) {
                    data = homepageRecommendBean.data;
                } else {
                    if (homepageRecommendBean.data == null || (homepageRecommendBean.data).size() == 0) {
                        ToastUtil.show("没有数据了");
                    } else {
                        data.addAll(homepageRecommendBean.data);
                    }
                }

                if (data != null && data.size() != 0) {
                    adapter.updateAdapter(data,false);
                } else {
                    refresh_more_every.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String message) {
                refresh_more_every.onRefreshComplete();
            }
        }, page + "");
    }
}
