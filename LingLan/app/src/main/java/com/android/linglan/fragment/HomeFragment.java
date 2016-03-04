package com.android.linglan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.adapter.HomePageAdapter;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.RecommendArticles;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.ui.MainActivity;
import com.android.linglan.ui.homepage.SearchActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.CustomPullToRefreshRecyclerView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView rec_home_page;
    private HomePageAdapter homePageAdapter;
    private CustomPullToRefreshRecyclerView home_page_refresh;
    private View rootView;
    private ImageView iv_search;
    private TextView tv_isnetconnect;
    private RecommendArticles recommendArticles;
    private RecommendSubjects recommendSubjects;
    public ArrayList<RecommendArticles.RecommendArticle> ArticlesData;
    public ArrayList<RecommendSubjects.RecommendSubject> SubjectsData;
    private int  page ;

    private void insertData(ArrayList<RecommendArticles.RecommendArticle> ArticlesData, ArrayList<RecommendSubjects.RecommendSubject> SubjectsData) {
        if(ArticlesData != null && !ArticlesData.equals("") && SubjectsData != null && !SubjectsData.equals("")){
            homePageAdapter.insertSubjectData(SubjectsData);
            homePageAdapter.insertArticlesData(ArticlesData);
        }else{
            tv_isnetconnect.setVisibility(View.VISIBLE);
            rec_home_page.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, null);
        initView();
        page = 1;
//        rec_home_page = (RecyclerView) rootView.findViewById(R.id.rec_home_page);
        home_page_refresh = (CustomPullToRefreshRecyclerView)rootView.findViewById(R.id. home_page_refresh);
        rec_home_page = home_page_refresh.getRefreshableView();
        rec_home_page.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec_home_page.setHasFixedSize(true);
        homePageAdapter = new HomePageAdapter(getActivity());
        rec_home_page.setAdapter(homePageAdapter);
        ArticlesData = ((MainActivity) getActivity()).getArticlesData();
        SubjectsData = ((MainActivity) getActivity()).getSubjectsData();
        insertData(ArticlesData, SubjectsData);
        getRecommendArticle(page);
        home_page_refresh.setRefreshCallback(new CustomPullToRefreshRecyclerView.RefreshCallback() {
            //上拉
            @Override
            public void onPullDownToRefresh() {
                page = 1;
                getRecommendArticle(page);
            }
           //下拉
            @Override
            public void onPullUpToLoadMore() {
                page++;
                getRecommendArticle(page);
            }
        });

//        getRecommendArticle();
//        getRecommendSubject();
        if (SubjectsData != null && !SubjectsData.equals("")) {
            homePageAdapter.insertSubjectData(SubjectsData);
            for (RecommendSubjects.RecommendSubject recommendSubject : SubjectsData) {
                LogUtil.e("homefragment有没有出来了了了了了 =====" + SubjectsData.toString());
            }
        }
//
        if (ArticlesData != null && !ArticlesData.equals("")) {
            homePageAdapter.insertArticlesData(ArticlesData);
        }
//            }
//        });

        return rootView;
    }

    private void initView() {
        tv_isnetconnect = (TextView) rootView.findViewById(R.id.tv_isnetconnect);
        iv_search = (ImageView) rootView.findViewById(R.id.iv_search);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("searchEdit", Constants.HOME);
                startActivity(intent);
            }
        });

        ToastUtil.show("我来了");
    }

    /*
    * 获取推荐文章
    * */
    private void getRecommendArticle(final int page ) {
        NetApi.getRecommendArticle(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
//                LogUtil.e(result);
                home_page_refresh.onRefreshComplete();

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }

                recommendArticles = JsonUtil.json2Bean(result, RecommendArticles.class);
                if(page == 1){
                    ArticlesData = recommendArticles.data;
                }else{
                    if(recommendArticles.data == null || (recommendArticles.data).size() == 0){
                        ToastUtil.show("没有数据了");
                    }else{
                        ArticlesData.addAll(recommendArticles.data);
                    }
                }
//                    LogUtil.e(recommendArticles.data.get(0).title);
//                homePageAdapter.updateList(data);
//                if(){
//                    home_page_refresh.setVisibility(View.GONE);
//                }
//                homePageAdapter = new HomePageAdapter(getActivity(), ArticlesData);
//                rec_home_page.setAdapter(homePageAdapter);

                if (ArticlesData != null && ArticlesData.size() != 0) {
//                    homePageAdapter.insertArticlesData(ArticlesData);
                    insertData(ArticlesData, SubjectsData);
                }else{
                    home_page_refresh.setVisibility(View.GONE);
                }

                for (RecommendArticles.RecommendArticle recommendArticle : recommendArticles.data) {
                    LogUtil.e(recommendArticle.toString());
                }

//                ToastUtil.show(result1);
            }

            @Override
            public void onFailure(String message) {
                LogUtil.e(message);
                home_page_refresh.onRefreshComplete();
            }
        },page+"" );
    }

    /*
   * 获取推荐专题
   * */
    private void getRecommendSubject() {
        NetApi.getRecommendSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
//                LogUtil.e(result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }

                recommendSubjects = JsonUtil.json2Bean(result, RecommendSubjects.class);
                SubjectsData = recommendSubjects.data;

//                    LogUtil.e(recommendArticles.data.get(0).title);
//                homePageAdapter.updateList(data);
//                homePageAdapter = new HomePageAdapter(getActivity(), SubjectData);
//                rec_home_page.setAdapter(homePageAdapter);

                if (SubjectsData != null && !SubjectsData.equals("")) {
                    homePageAdapter.insertSubjectData(SubjectsData);
                }

                for (RecommendSubjects.RecommendSubject recommendSubject : SubjectsData) {
                    LogUtil.e(recommendSubject.toString());
                }

//                ToastUtil.show(result1);
            }

            @Override
            public void onFailure(String message) {
                LogUtil.e(message);
            }
        });
    }
}
