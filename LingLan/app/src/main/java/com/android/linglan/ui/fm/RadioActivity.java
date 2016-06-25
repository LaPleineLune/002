package com.android.linglan.ui.fm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.adapter.NewsFragmentPagerAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.RadioFragment;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.RadioClassifyBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.study.SearchActivity;
import com.android.linglan.utils.BaseTools;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.widget.ColumnHorizontalScrollView;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/6/08 0008.
 * 音频列表
 */
public class RadioActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_no_network;
    private LinearLayout ll_radio;
    private RadioFragment radioFragment;
    public ArrayList<RadioClassifyBean.RadioClassifyData> radioClassifyData;
    /**
     * 自定义HorizontalScrollView
     */
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    private LinearLayout mRadioGroup_content;
    private ViewPager mViewPager;
    /**
     * 左阴影部分
     */
    public ImageView shade_left;
    /**
     * 右阴影部分
     */
    public ImageView shade_right;
    private LinearLayout ll_more_columns;
    private RelativeLayout rl_column;
    private ImageView button_more_columns;
    /**
     * 当前选中的栏目
     */
    private int columnSelectIndex;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth = 0;
    private int isclick = 0;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private Intent intent;

    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_FAILURE:
                    //原页面GONE掉，提示网络不好的页面出现
                    ll_radio.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);

                    break;
                case REQUEST_SUCCESS:
                    //原页面GONE掉，提示网络不好的页面出现
                    ll_radio.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);

//                    mViewPager.setOffscreenPageLimit(subjectClassifyListBean.size());
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_radio);
    }

    @Override
    protected void initView() {
        ll_radio = (LinearLayout) findViewById(R.id.ll_radio);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
        ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
        rl_column = (RelativeLayout) findViewById(R.id.rl_column);
        button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        shade_left = (ImageView) findViewById(R.id.shade_left);
        shade_right = (ImageView) findViewById(R.id.shade_right);

    }

    @Override
    protected void initData() {
        setTitle("电台", "");
        Drawable collectTopDrawable = ContextCompat.getDrawable(this, R.drawable.search1);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
        radioClassifyData = new ArrayList<>();
        getRadioClassify();
        mScreenWidth = BaseTools.getWindowsWidth(this);
        columnSelectIndex = 0;
    }

    /**
     * 初始化Column栏目项
     */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = radioClassifyData.size();
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
        for (int i = 0; i < count; i++) {
            int dp10 = (int) getResources().getDimension(R.dimen.dp16);
            int dp16 = (int) getResources().getDimension(R.dimen.dp64);
            LinearLayout.LayoutParams params;
            if (i == 0) {
                params = new LinearLayout.LayoutParams(dp16, LinearLayout.LayoutParams.MATCH_PARENT);
            } else {
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

            }

            params.leftMargin = dp10;
            params.rightMargin = dp10;
            TextView columnTextView = new TextView(this);
            columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
            columnTextView.setGravity(Gravity.CENTER);
//            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(radioClassifyData.get(i).catename);
            columnTextView.setTextColor(ContextCompat.getColorStateList(this, R.drawable.subject_tab_text_color));
            columnTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.radio_buttong_bgs));

            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);

            }
            columnTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        View localView = mRadioGroup_content.getChildAt(i);

                        if (localView != v)
                            localView.setSelected(false);
                        else {
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                }
            });
            mRadioGroup_content.addView(columnTextView, i, params);
            isclick = 1;
        }
    }

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
        }
        //判断是否选中
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();//清空
        int count = radioClassifyData.size();
        for (int i = 0; i < count; i++) {
            Bundle data = new Bundle();
//            data.putString("orderid", subjectClassifyListBean.get(i).orderid);
            data.putString("cateid", radioClassifyData.get(i).cateid);
            radioFragment = new RadioFragment();
            radioFragment.setArguments(data);
            fragments.add(radioFragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
    }

    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };

    @Override
    protected void setListener() {

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
//                        swipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
//                        swipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(RadioActivity.this, SearchActivity.class);
                intent.putExtra("searchEdit", Constants.ALLFM);
                startActivity(intent);
            }
        });
    }

    /**
     * 全部音频分类列表
     */
    private void getRadioClassify() {
        NetApi.getRadioClassify(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getRadioClassify:" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, RadioActivity.this)) {
                    return;
                }

                RadioClassifyBean radioClassifyBean = JsonUtil.json2Bean(result, RadioClassifyBean.class);
                radioClassifyData = radioClassifyBean.data;
//                subjectClassifyListBean.add(0, new AllSubjectClassifyListBean.SubjectClassifyListBean("-1", "全部", "", ""));
//                if (SharedPreferencesUtil.getString("token", null) != null) {
//                    subjectClassifyListBean.add(subjectClassifyListBean.size(), new AllSubjectClassifyListBean.SubjectClassifyListBean("-2", "已收藏", "", ""));
//                }
                initTabColumn();
                initFragment();
                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

}
