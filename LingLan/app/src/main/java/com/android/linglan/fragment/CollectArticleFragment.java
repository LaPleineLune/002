package com.android.linglan.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.linglan.adapter.CollectArticleAdapter;
import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllArticleClassifyListBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.homepage.ArticleDetailsActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.swipemenu.SwipeMenu;
import com.android.linglan.widget.swipemenu.SwipeMenuCreator;
import com.android.linglan.widget.swipemenu.SwipeMenuItem;
import com.android.linglan.widget.swipemenu.SwipeMenuListView;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/1/13 0013.
 * 收藏的文章
 */
public class CollectArticleFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private View rootView;
    private SwipeMenuListView lv_collect_article;
    private CollectArticleAdapter adapter;
    private Intent intent;
    public boolean edit = false;
    private AllArticleClassifyListBean AllArticleClassifyList;
    private ArrayList<AllArticleClassifyListBean.ArticleClassifyListBean> articleClassifyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_collect_article, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {
        lv_collect_article = (SwipeMenuListView) rootView.findViewById(R.id.lv_collect_article);

    }

    @Override
    protected void initData() {
//        setTitle("我的收藏", "编辑");
        initSlide();
        getCollectArticle();
        adapter = new CollectArticleAdapter(getActivity());
        lv_collect_article.setSelector(new ColorDrawable(Color.TRANSPARENT));// 设置item选中色
        lv_collect_article.setAdapter(adapter);
        intent = new Intent();

    }

    @Override
    protected void setListener() {
        lv_collect_article.setOnItemClickListener(this);
        lv_collect_article.setOnItemLongClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ToastUtil.show(getActivity(), "点击第" + position + "个", 1);
        intent.setClass(getActivity(), ArticleDetailsActivity.class);
        intent.putExtra("articleId", articleClassifyList.get(position).articleid);
        intent.putExtra("photo", articleClassifyList.get(position).photo);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtil.show(getActivity(), "长按第" + position + "个被删除", 1);
        return false;
    }

    private void initSlide() {
        // TODO Auto-generated method stub
        // step 1. create a MenuCreator
//        if (!edit) {
//            return;
//        } else {
            SwipeMenuCreator creator = new SwipeMenuCreator() {
                @SuppressLint("ResourceAsColor")
                @Override

                public void create(SwipeMenu menu) {
//                LogUtils.v("SwipeMenuCreator==create");


                    SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                            0x3F, 0x25)));
                    // set item width
                    deleteItem.setWidth(dp2px(90));
                    // set a icon
                    //                deleteItem.setIcon(R.drawable.ic_delete);
                    //set a title
                    deleteItem.setTitle("删除");
                    deleteItem.setTitleSize(18);
                    deleteItem.setTitleColor(getResources().getColor(R.color.white));
                    // add to menu
                    menu.addMenuItem(deleteItem);
                }
            };
            // set creator
            lv_collect_article.setMenuCreator(creator);

            // step 2. listener item click event
            lv_collect_article.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    ToastUtil.show("onMenuItemClick==position=" + position + "index==" + index);
//                LogUtils.v("onMenuItemClick==position="+position+"index=="+index);
//                switch (index) {
//                    case 0:
//                        // open
//                        //                        open(item);
//                        caseUuid=datas.get(position).getUuid();
//                        if(Constant.JIWANGSHI.equals(caseUuid)){
//                            LogUtils.v("删除既往史");
//                            buildAlert = ECAlertDialog.buildAlert(ctx, "您确认要删除此病历吗？删除后将无法恢复！",
//                                    "取消", "确定", new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {// 取消
//                                            // TODO Auto-generated method stub
//                                            buildAlert.dismiss();
//                                        }
//
//                                    }, new DialogInterface.OnClickListener() {// 下载升级
//
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            // TODO Auto-generated method stub
//                                            buildAlert.dismiss();
//                                            delHistory();
//                                        }
//                                    });
//                            //buildAlert.setTitle(R.string.app_tip);
//                            buildAlert.setCanceledOnTouchOutside(false);
//                            buildAlert.show();
//
//
//                        }else{
//                            buildAlert = ECAlertDialog.buildAlert(ctx, "您确认要删除此病历吗？删除后将无法恢复！",
//                                    "取消", "确定", new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {// 取消
//                                            // TODO Auto-generated method stub
//                                            buildAlert.dismiss();
//                                        }
//
//                                    }, new DialogInterface.OnClickListener() {// 确定
//
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            // TODO Auto-generated method stub
//                                            buildAlert.dismiss();
//                                            delCase(caseUuid);
//                                        }
//                                    });
//                            buildAlert.setCanceledOnTouchOutside(false);
//                            buildAlert.show();
//
////						delCase(caseUuid);
//
//                        }
//                        break;
//                    case 1:
//                        //没有执行
//                        // delete
//                        //					delete(item);
//                        //                        mAppList.remove(position);
//                        //                        mAdapter.notifyDataSetChanged();
//                        //					caseUuid=datas.get(position-1).getUuid();
//                        //					delCase();
//                        break;
//                }
                    return false;
                }


            });
            // set SwipeListener

            lv_collect_article.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

                @Override
                public void onSwipeStart(int position) {
                    // swipe start
//                LogUtils.v("onSwipeEnd+"+position);
                }

                @Override
                public void onSwipeEnd(int position) {
                    // swipe end
//                LogUtils.v("onSwipeEnd"+position);
                }
            });

            // set MenuStateChangeListener
            lv_collect_article.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
                @Override
                public void onMenuOpen(int position) {
//                LogUtils.v("onMenuOpen" + position);
                }

                @Override
                public void onMenuClose(int position) {
//                LogUtils.v("onMenuClose" + position);
                }
            });

//        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void mSetVisibility(boolean edit) {
        this.edit = edit;
        adapter.updateAdapter(edit, articleClassifyList);
        if (edit) {
            lv_collect_article.setOnItemClickListener(null);
            lv_collect_article.setOnItemLongClickListener(null);
        } else {
//            initSlide();
            lv_collect_article.setOnItemClickListener(this);
            lv_collect_article.setOnItemLongClickListener(this);
        }
    }

    public void getCollectArticle() {
        NetApi.getCollectArticle(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getCollectArticle" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,getActivity())){
                    return;
                }

                if (!TextUtils.isEmpty(result)) {
                    AllArticleClassifyList = JsonUtil.json2Bean(result, AllArticleClassifyListBean.class);
                    articleClassifyList = AllArticleClassifyList.data;
                    if (articleClassifyList != null && !articleClassifyList.equals("")) {
                        adapter.updateAdapter(edit, articleClassifyList);
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
