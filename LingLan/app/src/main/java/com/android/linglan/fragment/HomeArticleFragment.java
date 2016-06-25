package com.android.linglan.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.linglan.adapter.AllArticleAdapter;
import com.android.linglan.adapter.HomepageArticleClassifyAdapter;
import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllArticleClassifyBean;
import com.android.linglan.http.bean.AllArticleClassifyListBean;
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
 * Created by LeeMy on 2016/2/26 0026.
 * 首页文章
 */
public class HomeArticleFragment extends BaseFragment {
    private View rootView;
    private ListView lv_article_classify;
    private TextView tv_classify_order;
    private LinearLayout ll_all_article;
    private LinearLayout ll_no_network;
    private HomepageArticleClassifyAdapter articleClassifyAdapter;
    private PtrClassicFrameLayout recycler_view_home_recommend;
    private RecyclerView rec_all_article;
    private AllArticleAdapter allArticleAdapter;
    private RecyclerAdapterWithHF mAdapter;
    private int page;
    private int location;
    private String order;//排序方式('addtime按时间排序' ,'count_view按统计排序')

    private AllArticleClassifyBean AllArticleClassify;
    public ArrayList<AllArticleClassifyBean.ArticleClassifyBean> ArticleClassify;

    private AllArticleClassifyListBean AllArticleClassifyList;
    private ArrayList<AllArticleClassifyListBean.ArticleClassifyListBean> ArticleClassifyList;
    private int oldPosition = -1;

//    protected static final int REQUEST_SUCCESS = 0;
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                   selectDefult();// 刚刚进来时的默认加载\
                    ll_all_article.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    //原页面GONE掉，提示网络不好的页面出现
                    ll_all_article.setVisibility(View.GONE);
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
            rootView = inflater.inflate(R.layout.fragment_home_article, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }


    @Override
    protected void initView() {
        ll_no_network = (LinearLayout) rootView.findViewById(R.id.ll_no_network);
        ll_all_article = (LinearLayout) rootView.findViewById(R.id.ll_all_article);
        lv_article_classify = (ListView) rootView.findViewById(R.id.lv_article_classify);

        recycler_view_home_recommend = (PtrClassicFrameLayout) rootView.findViewById(R.id. recycler_view_home_recommend);
        rec_all_article = (RecyclerView) rootView.findViewById(R.id.rec_all_article);
        rec_all_article.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec_all_article .setHasFixedSize(true);
        allArticleAdapter = new AllArticleAdapter(getActivity());
        mAdapter = new RecyclerAdapterWithHF(allArticleAdapter);
        rec_all_article.setAdapter(mAdapter);

        articleClassifyAdapter = new HomepageArticleClassifyAdapter(getActivity());
        lv_article_classify.setAdapter(articleClassifyAdapter);

    }

    @Override
    protected void initData() {
        getAllArticleClassify();
    }

    @Override
    protected void setListener() {
        //下拉刷新
        recycler_view_home_recommend.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                if ((ArticleClassify == null || ArticleClassify.size() == 0)) {
                    getAllArticleClassify();
                }else{
                    getAllArticleClassifyList(ArticleClassify.get(location).cateid, page, order);
                }

            }
        });

        //上拉刷新
        recycler_view_home_recommend.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                if ((ArticleClassify == null || ArticleClassify.size() == 0)) {
                    getAllArticleClassify();
                }else{
                    getAllArticleClassifyList(ArticleClassify.get(location).cateid, page, order);
                }
                recycler_view_home_recommend.loadMoreComplete(true);
            }
        });

        lv_article_classify.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                location = position;
                page = 1;
                if (oldPosition != position) {
                    articleClassifyAdapter.setSelectedPosition(position);
                    articleClassifyAdapter.notifyDataSetInvalidated();

                    getAllArticleClassifyList(ArticleClassify.get(location).cateid, page, order);

                    oldPosition = position;
                }
            }
        });

        ll_no_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    private void selectDefult(){
        articleClassifyAdapter.setSelectedPosition(0);
        articleClassifyAdapter.notifyDataSetInvalidated();
        order = "";
        page =1;

        getAllArticleClassifyList(ArticleClassify.get(location).cateid, page, order);
    }

    private void getAllArticleClassify() {
        NetApi.getAllArticleClassify(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.d(getActivity().getPackageName(), "getAllArticleClassify=" + result);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,getActivity())){
                    return;
                }
                AllArticleClassify = JsonUtil.json2Bean(result, AllArticleClassifyBean.class);
                ArticleClassify = AllArticleClassify.data;
                if (ArticleClassify != null && !ArticleClassify.equals("")) {
                    ArticleClassify.get(0).cateid = "";
                    articleClassifyAdapter.update(ArticleClassify);
                    handler.sendEmptyMessage(REQUEST_SUCCESS);
                }
            }

            @Override
            public void onFailure(String message) {
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        });
    }

    //得到所有的文章分类
    private void getAllArticleClassifyList(String cateid, final int page,String order) {
        NetApi.getAllArticleClassifyList(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
            LogUtil.d(getActivity().getPackageName(), "getAllArticleClassifyList=" + result);

                recycler_view_home_recommend.refreshComplete();
                recycler_view_home_recommend.setLoadMoreEnable(true);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,getActivity())){
                    recycler_view_home_recommend.loadMoreComplete(false);
                    return;
                }

                AllArticleClassifyList = JsonUtil.json2Bean(result, AllArticleClassifyListBean.class);
                if (page == 1) {
                    ArticleClassifyList = AllArticleClassifyList.data;
                } else {
                    if (AllArticleClassifyList.data == null || (AllArticleClassifyList.data).size() == 0) {
                        ToastUtil.show("没有数据了");
                    } else {
                        ArticleClassifyList.addAll(AllArticleClassifyList.data);
                    }
                }
//
                if (ArticleClassifyList != null && !ArticleClassifyList.equals("")) {
                    allArticleAdapter.updateAdapter(ArticleClassifyList);
                }
            }

            @Override
            public void onFailure(String message) {
                recycler_view_home_recommend.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, cateid, page + "", order);
    }
}
