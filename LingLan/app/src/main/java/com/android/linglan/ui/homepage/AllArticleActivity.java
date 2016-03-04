package com.android.linglan.ui.homepage;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.adapter.DragAdapter;
import com.android.linglan.base.AllSubjectBaseActivity;
import com.android.linglan.fragment.NewsFragment;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllArticleClassifyBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.CategoryTabStrip;
import com.android.linglan.widget.DragGrid;

import java.util.ArrayList;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;

public class AllArticleActivity extends AllSubjectBaseActivity implements AdapterView.OnItemClickListener {
    private CategoryTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private ImageView icon_category;
    private View popView;
    private View popView_sort;
    private PopupWindowHelper popupWindowHelper;
    private PopupWindowHelper popupWindowHelper2;
    /** 用户栏目的GRIDVIEW */
    private DragGrid userGridView;
    /** 用户栏目对应的适配器，可以拖动 */
    private DragAdapter userAdapter;

    private AllArticleClassifyBean AllArticleClassify;
    public ArrayList<AllArticleClassifyBean.ArticleClassifyBean> ArticleClassify;

    private TextView pop_title;
    private TextView tv_default_order,tv_time_order,tv_conunt_order;
    /** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
    boolean isMove = false;

    private String order_style = "";//排序方式('addtime按时间排序' ,'count_view按统计排序')

    @Override
    protected void setView() {
        setContentView(R.layout.test_all_article_actiity);
        popView = LayoutInflater.from(this).inflate(R.layout.channel, null);
        popView_sort =  LayoutInflater.from(this).inflate(R.layout.sort_article, null);
    }

    @Override
    protected void initView() {
        tabs = (CategoryTabStrip) findViewById(R.id.category_strip);
        pager = (ViewPager) findViewById(R.id.view_pager);
        icon_category = (ImageView) findViewById(R.id.icon_category);

        userGridView = (DragGrid)popView.findViewById(R.id.userGridView);

        popView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowHelper.dismiss();
            }
        });

        pop_title = (TextView)popView.findViewById(R.id.title);
        pop_title.setText("全部频道");

        tv_default_order = (TextView) popView_sort.findViewById(R.id.tv_default_order);
        tv_time_order = (TextView) popView_sort.findViewById(R.id.tv_time_order);
        tv_conunt_order = (TextView) popView_sort.findViewById(R.id.tv_conunt_order);
    }

    @Override
    protected void initData() {
        setTitle("全部文章", "");

        getAllArticleClassify();

        popupWindowHelper = new PopupWindowHelper(popView);
        popupWindowHelper2 = new PopupWindowHelper(popView_sort);
        userAdapter = new DragAdapter(this, ArticleClassify);
        userGridView.setAdapter(userAdapter);
//        ArticleClassify = new ArrayList<AllArticleClassifyBean.ArticleClassifyBean>();
//            AllArticleClassifyBean.ArticleClassifyBean item = new AllArticleClassifyBean.ArticleClassifyBean("13", "医路", "0");
//            ArticleClassify.add(item);
        adapter = new MyPagerAdapter(getSupportFragmentManager(), ArticleClassify);
//        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

    }

    @Override
    protected void setListener() {
        userGridView.setOnItemClickListener(this);
        icon_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出频道弹窗
                ToastUtil.show("弹窗出来啦啦啦");
                popupWindowHelper.showFromTop(v);

            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllArticleActivity.this, SearchActivity.class);
                intent.putExtra("searchEdit", Constants.ALLARTICLE);
                startActivity(intent);
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowHelper2.showAsDropDown(v, -v.getWidth() * 2 - 18, 0);
//                popupWindowHelper2.showAsDropDown(v,-120,0);
            }
        });

        tv_default_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("默认排序啦啦");
                order_style = "";
                adapter.updateAdapter(ArticleClassify,order_style);
            }
        });

        tv_time_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("时间排序啦啦");
                order_style = "addtime";
                adapter.updateAdapter(ArticleClassify,order_style);
            }
        });

        tv_conunt_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("点击数量排序啦啦");
                order_style = "count_view";
                adapter.updateAdapter(ArticleClassify,order_style);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if(isMove){
            return;
        }
        //position为 0，1 的不可以进行任何操作
//        if (position != 0 && position != 1) {
//            final ImageView moveImageView = getView(view);
//            if (moveImageView != null) {
//                TextView newTextView = (TextView) view.findViewById(R.id.text_item);
//                final int[] startLocation = new int[2];
//                newTextView.getLocationInWindow(startLocation);
//                final AllArticleClassifyBean.ArticleClassifyBean channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
////                otherAdapter.setVisible(false);
////                //添加到最后一个
////                otherAdapter.addItem(channel);
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        try {
//                            int[] endLocation = new int[2];
//                            //获取终点的坐标
////                            otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
//                            MoveAnim(moveImageView, startLocation , endLocation, channel,userGridView);
//                            userAdapter.setRemove(position);
//                        } catch (Exception localException) {
//                        }
//                    }
//                }, 50L);
//            }
//        }

        pager.setCurrentItem(position);
        popupWindowHelper.dismiss();

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

//        private final List<String> catalogs = new ArrayList<String>();
        private ArrayList<AllArticleClassifyBean.ArticleClassifyBean> articleClassify = new ArrayList<AllArticleClassifyBean.ArticleClassifyBean>();
        private String order_style;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public MyPagerAdapter(FragmentManager fm, ArrayList<AllArticleClassifyBean.ArticleClassifyBean> articleClassify) {
            super(fm);
            this.articleClassify = articleClassify;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            return catalogs.get(position);
//            return userChannelList.get(position).getName();
            return articleClassify.get(position).catename;
        }

        @Override
        public int getCount() {
//            return userChannelList.size();
            return articleClassify == null ? 0 : articleClassify.size();
        }

        @Override
        public Fragment getItem(int position) {
//            NewsFragment newsFragment = new NewsFragment(articleClassify);
            LogUtil.e("文章分类id" + articleClassify.get(position).cateid);
            return NewsFragment.newInstance(position, articleClassify.get(position).cateid,order_style);
        }

        public void updateAdapter(ArrayList<AllArticleClassifyBean.ArticleClassifyBean> articleClassify,String order_style) {
            this.articleClassify = articleClassify;
            this.order_style = order_style;
            notifyDataSetChanged();
        }

    }

    /**
     * 点击ITEM移动动画
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation,int[] endLocation, final AllArticleClassifyBean.ArticleClassifyBean moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGrid) {
//                    otherAdapter.setVisible(true);
//                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                }else{
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
//                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }
    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
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
                LogUtil.d("全部文章分类" + ArticleClassify.toString());
                if (ArticleClassify != null && !ArticleClassify.equals("")) {
                    userAdapter.setListDate(ArticleClassify);

//                    adapter = new MyPagerAdapter(getSupportFragmentManager(), ArticleClassify);
//                    adapter = new MyPagerAdapter(getSupportFragmentManager());
//
//                    pager.setAdapter(adapter);
//
//                    tabs.setViewPager(pager);
                    adapter.updateAdapter(ArticleClassify,order_style);
                    pager.setAdapter(adapter);
                    tabs.setViewPager(pager);
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

}
