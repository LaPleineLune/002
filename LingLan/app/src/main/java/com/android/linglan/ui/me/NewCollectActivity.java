package com.android.linglan.ui.me;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.linglan.adapter.RecycleHomeRecommendAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.CollectArticleFragment;
import com.android.linglan.fragment.CollectSubjectFragment;
import com.android.linglan.fragment.CollectTestFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.HomepageRecommendBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.CustomPullToRefreshRecyclerView;
import com.android.linglan.widget.SyLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/1/7 0007.
 * 我的收藏
 */
public class NewCollectActivity extends BaseActivity implements View.OnClickListener {
    private CustomPullToRefreshRecyclerView refresh_more_every;
    private RecyclerView RecycleCollection;
    private RecycleHomeRecommendAdapter adapter;
    private View rootView;
    private RecyclerView lv_homepage_recommend;
    public ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> data;
    private Intent intent;
    private int page;//页码

    public boolean edit = false;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_new_collect);
    }

    @Override
    protected void initView() {
        refresh_more_every = (CustomPullToRefreshRecyclerView)findViewById(R.id.refresh_more_every);
        RecycleCollection = refresh_more_every.getRefreshableView();
        RecycleCollection.setLayoutManager(new SyLinearLayoutManager(this));
        RecycleCollection.setHasFixedSize(true);
    }

    @Override
    protected void initData() {
        setTitle("我的收藏", "编辑");
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.edit);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
        page = 1;
        getHomeRecommend(page);
        adapter = new RecycleHomeRecommendAdapter(this);
        RecycleCollection.setAdapter(adapter);
    }


    @Override
    protected void setListener() {
        right.setOnClickListener(this);
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
        NetApi.getCollectAll(new PasserbyClient.HttpCallback() {
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
                    adapter.updateAdapter(data,edit);
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right:
                if (right.getText().toString().equals("编辑")) {
                    setTitle("我的收藏", "完成");
                    Drawable collectTopDrawable = getResources().getDrawable(R.drawable.edit_ok);
                    collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                    right.setCompoundDrawables(collectTopDrawable, null, null, null);
                    edit = true;
                    adapter.updateAdapter(data,edit);
//                    if (collectArticleFragment != null) {
//                        collectArticleFragment.mSetVisibility(true);
//                    }
//                    if (collectSubjectFragment != null) {
//                        collectSubjectFragment.mSetVisibility(true);
//                    }
//                    collect_article.setOnClickListener(null);
//                    collect_subject.setOnClickListener(null);
                } else {
                    setTitle("我的收藏", "编辑");
                    Drawable collectTopDrawable = getResources().getDrawable(R.drawable.edit);
                    collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                    right.setCompoundDrawables(collectTopDrawable, null, null, null);
                    edit = false;
                    adapter.updateAdapter(data,edit);
//                    if (collectArticleFragment != null) {
//                        collectArticleFragment.mSetVisibility(false);
//                    }
//                    if (collectSubjectFragment != null) {
//                        collectSubjectFragment.mSetVisibility(false);
//                    }
//                    collect_article.setOnClickListener(this);
//                    collect_subject.setOnClickListener(this);
                }
                break;
            default:
                break;
        }
    }

}
