package com.android.linglan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.linglan.adapter.AllArticleAdapter;
import com.android.linglan.adapter.HomepageArticleClassifyAdapter;
import com.android.linglan.adapter.HomepageArticleListAdapter;
import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllArticleClassifyBean;
import com.android.linglan.http.bean.AllArticleClassifyListBean;
import com.android.linglan.ui.MainActivity;
import com.android.linglan.ui.R;
import com.android.linglan.ui.homepage.AllArticleClassifyActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.CustomPullToRefreshRecyclerView;
import com.android.linglan.widget.HomepageListView;
import com.android.linglan.widget.SyLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/2/26 0026.
 * 首页文章
 */
public class HomeArticleFragment extends BaseFragment {
    private View rootView;
    private ListView lv_article_classify;
    private TextView tv_classify_order;
    private HomepageArticleClassifyAdapter articleClassifyAdapter;
    private CustomPullToRefreshRecyclerView article_refresh;
    private RecyclerView rec_all_article;
    private AllArticleAdapter allArticleAdapter;
    private int page;
    private String order;//排序方式('addtime按时间排序' ,'count_view按统计排序')

    private AllArticleClassifyBean AllArticleClassify;
    public ArrayList<AllArticleClassifyBean.ArticleClassifyBean> ArticleClassify;

    private AllArticleClassifyListBean AllArticleClassifyList;
    private ArrayList<AllArticleClassifyListBean.ArticleClassifyListBean> ArticleClassifyList;

    protected static final int REQUEST_SUCCESS = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                   selectDefult();// 刚刚进来时的默认加载
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
        lv_article_classify = (ListView) rootView.findViewById(R.id.lv_article_classify);
//        tv_classify_order = (TextView) rootView.findViewById(R.id.tv_classify_order);
    }

    @Override
    protected void initData() {
        getAllArticleClassify();
        articleClassifyAdapter = new HomepageArticleClassifyAdapter(getActivity());
        lv_article_classify.setAdapter(articleClassifyAdapter);
        lv_article_classify.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                final int location = position;
                articleClassifyAdapter.setSelectedPosition(position);
                articleClassifyAdapter.notifyDataSetInvalidated();
                getAllArticleClassifyList(ArticleClassify.get(location).cateid, page, order);

                allArticleAdapter = new AllArticleAdapter(getActivity());
                rec_all_article.setAdapter(allArticleAdapter);
                article_refresh.setRefreshCallback(new CustomPullToRefreshRecyclerView.RefreshCallback() {
                    @Override
                    public void onPullDownToRefresh() {
                        page = 1;
                        getAllArticleClassifyList(ArticleClassify.get(location).cateid, page, order);
                    }

                    @Override
                    public void onPullUpToLoadMore() {
                        page++;
                        LogUtil.e("页面数：" + page);
                        getAllArticleClassifyList(ArticleClassify.get(location).cateid, page, order);
                    }
                });
            }
        });

    }

    @Override
    protected void setListener() {
//        tv_classify_order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.show("文章分类排序");
////                Intent orderIntent = new Intent(getActivity(), AllArticleClassifyActivity.class);
////                startActivity(orderIntent);
//            }
//        });
    }

    private void selectDefult(){
        final int location=0;
        articleClassifyAdapter.setSelectedPosition(0);
        articleClassifyAdapter.notifyDataSetInvalidated();
        order = "";
        page =1;

        article_refresh =  (CustomPullToRefreshRecyclerView)rootView.findViewById(R.id. article_refresh);
//		RecyclerView rec_all_article = (RecyclerView) rootView.findViewById(R.id.rec_all_article_page);
        rec_all_article = article_refresh.getRefreshableView();
        rec_all_article.setLayoutManager(new SyLinearLayoutManager(getActivity()));
        rec_all_article .setHasFixedSize(true);

        getAllArticleClassifyList(ArticleClassify.get(location).cateid, page, order);

        allArticleAdapter = new AllArticleAdapter(getActivity());
        rec_all_article.setAdapter(allArticleAdapter);

        article_refresh.setRefreshCallback(new CustomPullToRefreshRecyclerView.RefreshCallback() {
            @Override
            public void onPullDownToRefresh() {
                page = 1;
                getAllArticleClassifyList(ArticleClassify.get(location).cateid, page, order);
            }

            @Override
            public void onPullUpToLoadMore() {
                page++;
                LogUtil.e("页面数：" + page);
                getAllArticleClassifyList(ArticleClassify.get(location).cateid, page, order);
            }
        });
    }

    private void getAllArticleClassify() {
        NetApi.getAllArticleClassify(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
                AllArticleClassify = JsonUtil.json2Bean(result, AllArticleClassifyBean.class);
                ArticleClassify = AllArticleClassify.data;
//                LogUtil.d("全部文章分类" + ArticleClassify.toString());
                if (ArticleClassify != null && !ArticleClassify.equals("")) {
                    ArticleClassify.get(0).cateid = "";
                    articleClassifyAdapter.update(ArticleClassify);
                    handler.sendEmptyMessage(REQUEST_SUCCESS);
                    for(AllArticleClassifyBean.ArticleClassifyBean article :  ArticleClassify) {
                        LogUtil.e("全部文章分类" + article.toString());
                    }
                }
            }

            @Override
            public void onFailure(String message) {
            }
        });
    }

    //得到所有的文章分类
    private void getAllArticleClassifyList(String cateid, final int page,String order) {
        LogUtil.e("全部文章分类列表id" + cateid);
        NetApi.getAllArticleClassifyList(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                article_refresh.onRefreshComplete();

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
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
                LogUtil.d("全部文章分类列表" + ArticleClassifyList.toString());
                if (ArticleClassifyList != null && !ArticleClassifyList.equals("")) {
                    allArticleAdapter.updateAdapter(ArticleClassifyList);
                }
            }

            @Override
            public void onFailure(String message) {
                article_refresh.onRefreshComplete();
            }
        }, cateid, page + "", order);
    }
}
