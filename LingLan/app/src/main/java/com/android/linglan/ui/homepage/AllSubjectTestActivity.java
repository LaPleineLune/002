package com.android.linglan.ui.homepage;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.linglan.adapter.CollectSubjectAdapter;
import com.android.linglan.base.AllSubjectBaseActivity;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllSubjectClassifyListBean;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.FlowLayout;
import com.android.linglan.widget.GridViewForScrollView;

import java.util.ArrayList;

public class AllSubjectTestActivity extends AllSubjectBaseActivity {
    private ScrollView scrollview_all_subject;
    private GridViewForScrollView gv_all_subject;
    private CollectSubjectAdapter adapter;
    private LinearLayout invis, ll_order_header;
    private FlowLayout fl_default_sort;
    private FlowLayout fl_all_classify;

    private int page;//页码
    private String orderid;//排序 传参数 addtime按时间排序, count_view按统计排序，""为全部
    private String cateid;//分类 传参数分类id(subjectClassifyListBeans.cateid)，传""则返回全部
    private boolean selected;
    private Intent intent;
    private TextView textView;
    private TextView textView2;

    public ArrayList<RecommendSubjects.RecommendSubject> data;
    public ArrayList<AllSubjectClassifyListBean.SubjectClassifyListBean> subjectClassifyListBean;

    private LayoutInflater mInflater;

    private String[] mVals = new String[]
            {"时间", "阅读量"};

    @Override
    protected void setView() {
        setContentView(R.layout.activity_all_subject_test);
    }

    @Override
    protected void initView() {
        scrollview_all_subject = (ScrollView) findViewById(R.id.scrollview_all_subject);
        gv_all_subject = (GridViewForScrollView) findViewById(R.id.gv_all_subject);
        invis = (LinearLayout) findViewById(R.id.invis);
        ll_order_header = (LinearLayout) findViewById(R.id.ll_order_header);

        fl_default_sort = (FlowLayout) findViewById(R.id.fl_default_sort);
        fl_all_classify = (FlowLayout) findViewById(R.id.fl_all_classify);

    }

    @Override
    protected void initData() {
        setTitle("全部专题", "");
        intent = new Intent();

        getAllSubjectClassifyList();
        orderid = "";
        cateid = "";
        page = 1;
        selected = false;

        getAllSubject(page,orderid,cateid);

        mInflater = LayoutInflater.from(this);

        scrollview_all_subject.smoothScrollTo(0, 0);
        adapter = new CollectSubjectAdapter(AllSubjectTestActivity.this);
        gv_all_subject.setSelector(new ColorDrawable(Color.TRANSPARENT));// 设置item选中色
        gv_all_subject.setAdapter(adapter);



        for (int i = 0; i < mVals.length; i++) {
            LinearLayout ll = (LinearLayout) mInflater.inflate(R.layout.tv_order,
                    fl_default_sort, false);
            final TextView tv = (TextView) ll.findViewById(R.id.tv_default_sort_context);
            tv.setText(mVals[i]);
            tv.setTextColor(AllSubjectTestActivity.this.getResources().getColorStateList(R.drawable.subject_order_text_color));
            fl_default_sort.addView(ll);

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(tv.getText().toString().trim() + "我被点击了");
                    String orderName = tv.getText().toString().trim();
                    tv.setSelected(true);

                    if(textView2 != null){
                        textView2.setSelected(false);
                    }
                    textView2 = tv;
                    //调接口，传参数 'addtime按时间排序' ,'count_view按统计排序')，默认为orderid
                    //点击后把相应排序存到sp中，在调getAllSubject()方法
                    if(orderName.equals("时间")){
//                        SharedPreferencesUtil.saveString("orderid","addtime");
                        orderid = "addtime";
                    }else if(orderName.equals("阅读量")){
//                        SharedPreferencesUtil.saveString("orderid","count_view");
                        orderid = "count_view";
                    }
                    getAllSubject(page, orderid,cateid);
                }
            });
        }

    }

    @Override
    protected void setListener() {

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(scrollview_all_subject != null) {
//                    scrollview_all_subject.scrollTo(10, 10);
//                }
//                scrollview_all_subject.fullScroll(View.FOCUS_UP);
//                gv_all_subject.setSelection(0);
                intent.setClass(AllSubjectTestActivity.this, SearchActivity.class);
                intent.putExtra("searchEdit", Constants.ALLSUBJECT);
                startActivity(intent);
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_order_header.getVisibility() == View.GONE) {
                    ll_order_header.setVisibility(View.VISIBLE);
//                    order.setImageResource(R.drawable.order_up);
                } else {
                    ll_order_header.setVisibility(View.GONE);
//                    order.setImageResource(R.drawable.order_down);
                }
                scrollview_all_subject.fullScroll(View.FOCUS_UP);
            }
        });

        gv_all_subject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.setClass(AllSubjectTestActivity.this, SubjectDetailsActivity.class);
                intent.putExtra("specialid", data.get(position).specialid);
                startActivity(intent);
            }
        });

        scrollview_all_subject.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ll_order_header.setVisibility(View.GONE);
//                    order.setImageResource(R.drawable.order_down);
                    gv_all_subject.setSelection(View.FOCUS_UP);
                }
                return false;
            }
        });

    }

    /**
     * 获取全部专题
     */
    private void getAllSubject(int page,String addtime,String cateid) {
        NetApi.getAllSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.d("getDetailsArticle=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
                RecommendSubjects recommendSubjects = JsonUtil.json2Bean(result, RecommendSubjects.class);
                data = recommendSubjects.data;

                adapter.updateAdapter(false, data);

//                for (RecommendSubjects.RecommendSubject recommendSubject : data) {
                    LogUtil.d(data.toString());
//                }
            }

            @Override
            public void onFailure(String message) {
            }
        },page+"",addtime,cateid);
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

                for (final AllSubjectClassifyListBean.SubjectClassifyListBean subjectClassifyListBeans: subjectClassifyListBean) {
                    LinearLayout ll2 = (LinearLayout) mInflater.inflate(R.layout.tv_order, fl_all_classify, false);
                    final TextView tv2 = (TextView) ll2.findViewById(R.id.tv_default_sort_context);
                    tv2.setText(subjectClassifyListBeans.catename);
                    tv2.setTextColor(AllSubjectTestActivity.this.getResources().getColorStateList(R.drawable.subject_order_text_color));
                    fl_all_classify.addView(ll2);

                    ll2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtil.show(subjectClassifyListBeans.cateid + "被点击了");
                            if(textView != null){
                                textView.setSelected(false);
                            }
                            textView = tv2;
                            selected = true;
                            //调接口，传参数分类id(subjectClassifyListBeans.cateid)，不传则返回全部
                            //点了这个在点排序或是点击了排序在来点分类怎么办  存储在sp中，离开后清零
                            //点击后把相应分类存到sp中，在调getAllSubject()方法
                            if (tv2 != null) {
                                tv2.setSelected(selected);
                            }
                            cateid = subjectClassifyListBeans.cateid;
                            getAllSubject(page,orderid,cateid);
                        }
                    });
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
