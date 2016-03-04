package com.android.linglan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.Constants;
import com.android.linglan.ui.R;
import com.android.linglan.ui.homepage.SearchActivity;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/2/26 0026.
 * 新首页
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private ViewPager pager_home;
    private ArrayList<Fragment> fragList = new ArrayList<Fragment>();
    private RelativeLayout rl_home_recommend, rl_home_article, rl_home_subject;
    private TextView tv_home_recommend, tv_home_article, tv_home_subject;
    private ImageView img_home_recommend, img_home_article, img_home_subject;
    private ImageView img_home_search;
    private Intent intent;
    private HomeRecommendFragment homeRecommendFragment;
    private HomeArticleFragment homeArticleFragment;
    private HomeSubjectFragment homeSubjectFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home_page, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {
        pager_home = (ViewPager) rootView.findViewById(R.id.pager_home);
        rl_home_recommend = (RelativeLayout) rootView.findViewById(R.id.rl_home_recommend);
        tv_home_recommend = (TextView) rootView.findViewById(R.id.tv_home_recommend);
        img_home_recommend = (ImageView) rootView.findViewById(R.id.img_home_recommend);
        rl_home_article = (RelativeLayout) rootView.findViewById(R.id.rl_home_article);
        tv_home_article = (TextView) rootView.findViewById(R.id.tv_home_article);
        img_home_article = (ImageView) rootView.findViewById(R.id.img_home_article);
        rl_home_subject = (RelativeLayout) rootView.findViewById(R.id.rl_home_subject);
        tv_home_subject = (TextView) rootView.findViewById(R.id.tv_home_subject);
        img_home_subject = (ImageView) rootView.findViewById(R.id.img_home_subject);
        img_home_search = (ImageView) rootView.findViewById(R.id.img_home_search);

    }

    @Override
    protected void initData() {
        intent = new Intent();
        initFragment();
        pager_home.setOffscreenPageLimit(2);
        MyFragmentPagerAdapter fAdapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        pager_home.setAdapter(fAdapter);

    }

    @Override
    protected void setListener() {
        rl_home_recommend.setOnClickListener(this);
        rl_home_article.setOnClickListener(this);
        rl_home_subject.setOnClickListener(this);
        img_home_search.setOnClickListener(this);

        pager_home.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                clearSelection();
                switch (arg0) {
                    case 0:
                        tv_home_recommend.setTextColor(getResources().getColor(R.color.carminum));
                        img_home_recommend.setBackgroundResource(R.color.carminum);
                        break;
                    case 1:
                        tv_home_article.setTextColor(getResources().getColor(R.color.carminum));
                        img_home_article.setBackgroundResource(R.color.carminum);
                        break;
                    case 2:
                        tv_home_subject.setTextColor(getResources().getColor(R.color.carminum));
                        img_home_subject.setBackgroundResource(R.color.carminum);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_home_recommend:
                pager_home.setCurrentItem(0,false);
                break;
            case R.id.rl_home_article:
                pager_home.setCurrentItem(1,false);
                break;
            case R.id.rl_home_subject:
                pager_home.setCurrentItem(2,false);
                break;
            case R.id.img_home_search:
                intent.setClass(getActivity(), SearchActivity.class);
                intent.putExtra("searchEdit", Constants.HOME);
                startActivity(intent);
                break;
        }
    }

    private void initFragment() {
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    if (homeRecommendFragment == null) {
                        homeRecommendFragment = new HomeRecommendFragment();
                        fragList.add(homeRecommendFragment);
                    }else{
                        fragList.add(homeRecommendFragment);
                    }

//                    HomeRecommendFragment homeRecommendFragment = new HomeRecommendFragment();
//                    fragList.add(frag);
                    break;
                case 1:
                    if (homeArticleFragment == null) {
                        homeArticleFragment = new HomeArticleFragment();
                        fragList.add(homeArticleFragment);
                    }else{
                        fragList.add(homeArticleFragment);
                    }

//                    HomeArticleFragment frag1 = new HomeArticleFragment();
//                    fragList.add(frag1);
                    break;
                case 2:
                    if (homeSubjectFragment == null) {
                        homeSubjectFragment = new HomeSubjectFragment();
                        fragList.add(homeSubjectFragment);
                    }else{
                        fragList.add(homeSubjectFragment);
                    }

//                    HomeSubjectFragment frag2 = new HomeSubjectFragment();
//                    fragList.add(frag2);
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        tv_home_recommend.setTextColor(getResources().getColor(R.color.gray));
        img_home_recommend.setBackgroundResource(R.color.light_gray);
        tv_home_article.setTextColor(getResources().getColor(R.color.gray));
        img_home_article.setBackgroundResource(R.color.light_gray);
        tv_home_subject.setTextColor(getResources().getColor(R.color.gray));
        img_home_subject.setBackgroundResource(R.color.light_gray);

    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragList.get(position);
        }

        @Override
        public int getCount() {
            return fragList.size();
        }
    }

}
