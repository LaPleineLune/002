package com.android.linglan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.ClinicalFragment;
import com.android.linglan.fragment.MeFragment;
import com.android.linglan.fragment.StudyFragment;
import com.android.linglan.fragment.TabFragmentDelegate;
import com.android.linglan.utils.AppUpdaterUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.TestFixedTabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<TabFragmentDelegate> fragmentDelegates = new ArrayList<TabFragmentDelegate>();
    private boolean doubleBackToExitPressedOnce;
    private static TestFixedTabPageIndicator indicator;
    private ViewPager pager;
    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        indicator = new TestFixedTabPageIndicator(this);
        new AppUpdaterUtil().checkToUpdate(this);
        initFragments();

    }

    @Override
    public void onResume() {
        super.onResume();
        String clinicalDetails = getIntent().getStringExtra("clinicalDetails");
        if (clinicalDetails != null && clinicalDetails.equals("clinicalDetails")) {
            pager.setCurrentItem(1);
        }
    }

    @Override
    protected void setView() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    private void initFragments() {
        TabFragmentDelegate fragmentDelegate =
                new TabFragmentDelegate(StudyFragment.class, null, R.drawable.bottom_home_icon, R.string.study);
        fragmentDelegates.add(fragmentDelegate);

        fragmentDelegate =
                new TabFragmentDelegate(ClinicalFragment.class, null, R.drawable.bottom_consilia_icon, R.string.clinical);
        fragmentDelegates.add(fragmentDelegate);

        fragmentDelegate = new TabFragmentDelegate(MeFragment.class, null, R.drawable.bottom_me_icon, R.string.mine);
        fragmentDelegates.add(fragmentDelegate);

        adapter = new MainTabAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.content_pager);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);
        indicator = (TestFixedTabPageIndicator) findViewById(R.id.main_tab);
        indicator.setViewPager(pager);
        redirectTo();
        indicator.getMainContext(this);
    }


    class MainTabAdapter extends FragmentPagerAdapter
            implements
            TestFixedTabPageIndicator.CustomTabPagerAdapter {

        public MainTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getIconResId(int index) {
            return fragmentDelegates.get(index).getIconResId();
        }

        @Override
        public int getLabelResId(int index) {
            return fragmentDelegates.get(index).getLabelResId();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentDelegates.get(position).buildFragmentInstance(MainActivity.this);
        }

        @Override
        public int getCount() {
            return fragmentDelegates.size();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        ToastUtil.show("再次点击退出系统");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void redirectTo() {
        Intent intent = getIntent();
        int index = intent.getIntExtra("index", 0);
        if (index < fragmentDelegates.size()) {
            pager.setCurrentItem(index);
        }
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        redirectTo();
//    }
}
