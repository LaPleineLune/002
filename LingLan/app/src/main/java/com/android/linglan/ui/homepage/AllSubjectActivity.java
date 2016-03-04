package com.android.linglan.ui.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.linglan.adapter.TestAllSubjectAdapter;
import com.android.linglan.base.AllSubjectBaseActivity;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ArticleDetails;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.FlowLayout;

import java.util.ArrayList;

public class AllSubjectActivity extends AllSubjectBaseActivity {
    private ListView lv_all_subject;
    private TestAllSubjectAdapter testAllSubjectAdapter;
    private LinearLayout invis, ll_order_header;
    private View header;
    private View header2;
    private FlowLayout fl_default_sort;
    private FlowLayout fl_all_classify;

    public ArrayList<RecommendSubjects.RecommendSubject> data;
    public RecommendSubjects.RecommendSubject recommendSubject;

    private String[] mVals = new String[]
            {"时间", "阅读量"};
    private String[] mVals2 = new String[]
            {"人物", "临床","病症","理论","针灸","砭石","刮痧"};

    @Override
    protected void setView() {
        setContentView(R.layout.test_activity_all_subject);
    }

    @Override
    protected void initView() {
        lv_all_subject = (ListView) findViewById(R.id.lv_all_subject);
        invis = (LinearLayout) findViewById(R.id.invis);
        ll_order_header = (LinearLayout) findViewById(R.id.ll_order_header);

        data = new ArrayList<RecommendSubjects.RecommendSubject>();
        recommendSubject = new RecommendSubjects.RecommendSubject();
        recommendSubject.specialname = "糖尿病";
        data.add(recommendSubject);


        header = View.inflate(this, R.layout.order_header, null);//头部内容
        header2 = View.inflate(this, R.layout.order_action, null);
        lv_all_subject.addHeaderView(header);//添加头部
        lv_all_subject.addHeaderView(header2);

        testAllSubjectAdapter = new TestAllSubjectAdapter(AllSubjectActivity.this,data);
        lv_all_subject.setAdapter(testAllSubjectAdapter);

        fl_default_sort = (FlowLayout) header.findViewById(R.id.fl_default_sort);
        fl_all_classify = (FlowLayout) header.findViewById(R.id.fl_all_classify);

        LayoutInflater mInflater = LayoutInflater.from(this);
        for (int i = 0; i < mVals.length; i++) {
            LinearLayout ll = (LinearLayout) mInflater.inflate(R.layout.tv_order,
                    fl_default_sort, false);
            TextView tv = (TextView) ll.findViewById(R.id.tv_default_sort_context);
            tv.setText(mVals[i]);
            fl_default_sort.addView(ll);

//            LinearLayout ll2 = (LinearLayout) mInflater.inflate(R.layout.tv_order,
//                    fl_all_classify, false);
//            TextView tv2 = (TextView)ll.findViewById(R.id.tv_default_sort_context);
//            tv.setText(mVals2[i]);
//            fl_all_classify.addView(ll2);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show("我被点击了");
                }
            });
        }

        for (int i = 0; i < mVals2.length; i++) {
            LinearLayout ll2 = (LinearLayout) mInflater.inflate(R.layout.tv_order,
                    fl_all_classify, false);
            TextView tv2 = (TextView) ll2.findViewById(R.id.tv_default_sort_context);
            tv2.setText(mVals2[i]);
            fl_all_classify.addView(ll2);
            ll2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show("我被点击了");
                }
            });
        }

        lv_all_subject.removeHeaderView(header);
        lv_all_subject.removeHeaderView(header2);
    }

    @Override
    protected void initData() {
        setTitle("全部专题", "");

//        getAllSubject();

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllSubjectActivity.this, SearchActivity.class);
                intent.putExtra("searchEdit", Constants.ALLSUBJECT);
                startActivity(intent);
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lv_all_subject.getHeaderViewsCount() == 0) {
                    lv_all_subject.addHeaderView(header);//添加头部
                    lv_all_subject.addHeaderView(header2);
                    invis.setVisibility(View.GONE);
                } else {
                    lv_all_subject.removeHeaderView(header);
                    lv_all_subject.removeHeaderView(header2);
                    invis.setVisibility(View.GONE);
                }
            }
        });

        lv_all_subject.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    invis.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 1) {
                    invis.setVisibility(View.VISIBLE);
                } else {
                    invis.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void setListener() {

    }

    /**
     * 获取全部专题
     */
//    private void getAllSubject() {
//        NetApi.getAllSubject(new PasserbyClient.HttpCallback() {
//            @Override
//            public void onSuccess(String result) {
//                LogUtil.d("getDetailsArticle=" + result);
//                RecommendSubjects recommendSubjects = JsonUtil.json2Bean(result, RecommendSubjects.class);
//                data = recommendSubjects.data;
//
//                testAllSubjectAdapter = new TestAllSubjectAdapter(AllSubjectActivity.this,data);
//                lv_all_subject.setAdapter(testAllSubjectAdapter);
//
//                for (RecommendSubjects.RecommendSubject recommendSubject : data) {
//                    LogUtil.e(recommendSubject.toString());
//                }
////                LogUtil.d("第一次" + RecommendSubjects.data.toString());
//            }
//
//            @Override
//            public void onFailure(String message) {
//            }
//        });
//    }
}
