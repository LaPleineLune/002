package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.adapter.RecycleHomeRecommendAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.HomepageRecommendBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.study.SearchActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 临证参考
 */
public class ClinicalReferenceActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    protected static final int REQUEST_SUCCESS_NO_CONTENT = 2;
    protected static final int REQUEST_SUCCESS_CONTENT = 3;
    private PtrClassicFrameLayout recycler_view_home_recommend;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView lv_homepage_recommend;
    private LinearLayout ll_clinical_reference;
    private LinearLayout ll_clinical_reference_list;
    private LinearLayout ll_no_network;
    private TextView tv_clinical_classify_no_content;
    //    private RecyclerAdapter adapter;
    private RecycleHomeRecommendAdapter adapter;
    public ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> data;
    private Intent intent;
    private int page;//页码
//    private CustomPullToRefreshRecyclerView refresh_more_every;

    private List<String> mData = new ArrayList<String>();
    private String instruction = "";// 临证问号的说明  cr_info
    private String noContentInstruction = "";// 临证无内容页面的说明  cr_none

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_FAILURE:
                    //原页面GONE掉，提示网络不好的页面出现
//                    recycler_view_home_recommend.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    ll_clinical_reference.setVisibility(View.GONE);
//                    tv_clinical_classify_no_content.setVisibility(View.GONE);
                    break;
                case REQUEST_SUCCESS:
                    //原页面GONE掉，提示网络不好的页面出现
//                    recycler_view_home_recommend.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    ll_clinical_reference.setVisibility(View.VISIBLE);
                    tv_clinical_classify_no_content.setVisibility(View.GONE);
                    break;
                case REQUEST_SUCCESS_CONTENT:
                    ll_no_network.setVisibility(View.GONE);
                    ll_clinical_reference_list.setVisibility(View.VISIBLE);
                    tv_clinical_classify_no_content.setVisibility(View.GONE);
                    break;
                case REQUEST_SUCCESS_NO_CONTENT:
                    ll_no_network.setVisibility(View.GONE);
                    ll_clinical_reference.setVisibility(View.VISIBLE);
                    ll_clinical_reference_list .setVisibility(View.GONE);
                    tv_clinical_classify_no_content.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(noContentInstruction)) {
                        tv_clinical_classify_no_content.setText(noContentInstruction);
                    } else {
                        tv_clinical_classify_no_content.setText(R.string.clinical_reference_no_content);
                    }
//                    recycler_view_home_recommend .setVisibility(View.GONE);
                    break;
            }
        }
    };
    @Override
    protected void setView() {
        setContentView(R.layout.activity_clinical_reference);
    }

    @Override
    protected void initView() {
        lv_homepage_recommend = (RecyclerView) findViewById(R.id.lv_homepage_recommend);
        recycler_view_home_recommend = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_home_recommend);
        ll_clinical_reference = (LinearLayout) findViewById(R.id.ll_clinical_reference);
        ll_clinical_reference_list = (LinearLayout) findViewById(R.id.ll_clinical_reference_list);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        tv_clinical_classify_no_content = (TextView) findViewById(R.id.tv_clinical_classify_no_content);
    }

    @Override
    protected void initData() {
        setTitle("临证参考", "");

        Drawable collectTopDrawable1 = getResources().getDrawable(R.drawable.instruction);
        collectTopDrawable1.setBounds(0, 0, collectTopDrawable1.getMinimumWidth(), collectTopDrawable1.getMinimumHeight());
        title.setCompoundDrawablePadding(6);
        title.setCompoundDrawables(null, null, collectTopDrawable1, null);

        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.search1);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);

        intent = new Intent();
        getPrompt("cr_info");
        getPrompt("cr_none");
        data = new ArrayList<HomepageRecommendBean.HomepageRecommendBeanData>();
        page = 1;
        getClinicalReference(page);
        adapter = new RecycleHomeRecommendAdapter(this);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        lv_homepage_recommend.setLayoutManager(new LinearLayoutManager(this));
        lv_homepage_recommend.setHasFixedSize(true);
        lv_homepage_recommend.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        //下拉刷新
        recycler_view_home_recommend.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                getClinicalReference(page);

            }
        });

        //上拉刷新
        recycler_view_home_recommend.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
                getClinicalReference(page);
                recycler_view_home_recommend.loadMoreComplete(true);
            }
        });


        ll_no_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(ClinicalReferenceActivity.this, UmengBuriedPointUtil.ClinicalReferenceSearch);
                intent.setClass(ClinicalReferenceActivity.this, SearchActivity.class);
                intent.putExtra("clinicalReference", "clinicalReference");
                intent.putExtra("searchEdit", Constants.HOME);
                startActivity(intent);
            }
        });

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(ClinicalReferenceActivity.this, UmengBuriedPointUtil.ClinicalReferenceClickExplain);
                intent = new Intent(ClinicalReferenceActivity.this, ClinicalReferenceInstructionActivity.class);
                intent.putExtra("instruction", instruction);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取临证参考
     */
    private void getClinicalReference(final int page) {
        NetApi.getClinicalReference(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                recycler_view_home_recommend.refreshComplete();
                recycler_view_home_recommend.setLoadMoreEnable(true);
                LogUtil.e("getClinicalReference=" + result);

                LogUtil.e("page=" + page);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalReferenceActivity.this)){
                    recycler_view_home_recommend.loadMoreComplete(false);
                    if (HttpCodeJugementUtil.code == 1 && page == 1) {
                        handler.sendEmptyMessage(REQUEST_SUCCESS_NO_CONTENT);
                    }
                    return;
                }

                handler.sendEmptyMessage(REQUEST_SUCCESS_CONTENT);
                handler.sendEmptyMessage(REQUEST_SUCCESS);

                HomepageRecommendBean homepageRecommendBean = JsonUtil.json2Bean(result, HomepageRecommendBean.class);

                if (page == 1) {
//                    data = homepageRecommendBean.data;
                    data.clear();
                    for (HomepageRecommendBean.HomepageRecommendBeanData hrd : homepageRecommendBean.data) {
                        if (hrd.type.equals(Constants.ARTICLE) || hrd.type.equals(Constants.SUBJECT) || hrd.type.equals(Constants.RADIO_SPECIAL)|| hrd.type.equals(Constants.RADIO_SINGLE)) {
                            data.add(hrd);
                        }
                    }
                } else {
                    if (homepageRecommendBean.data == null || (homepageRecommendBean.data).size() == 0) {
                        ToastUtil.show("没有数据了");
                    } else {
//                        data.addAll(homepageRecommendBean.data);
                        for (HomepageRecommendBean.HomepageRecommendBeanData hrd : homepageRecommendBean.data) {
                            if (hrd.type.equals(Constants.ARTICLE) || hrd.type.equals(Constants.SUBJECT) || hrd.type.equals(Constants.RADIO_SPECIAL)|| hrd.type.equals(Constants.RADIO_SINGLE)) {
                                data.add(hrd);
                            }
                        }
                    }
                }

                if (data != null && data.size() != 0) {
                    adapter.updateAdapter(data,false);

                } else {
//                    refresh_more_every.setVisibility(View.GONE);
                }

//                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                recycler_view_home_recommend.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, page + "");
    }

    public void getPrompt(final String type) {
        NetApi.getPrompt(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getPrompt=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalReferenceActivity.this)) {
                    return;
                }
                try {
                    JSONObject json = new JSONObject(result);
                    String dataInstruction = json.getString("data");
                    if (type.equals("cr_info")) {
                        instruction = dataInstruction;
                        LogUtil.e("getPromptMsg===instruction=" + dataInstruction);
                    } else if (type.equals("cr_none")) {
                        noContentInstruction = dataInstruction;
                        LogUtil.e("getPromptMsg===noContentInstruction=" + dataInstruction);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, type);
    }

}
