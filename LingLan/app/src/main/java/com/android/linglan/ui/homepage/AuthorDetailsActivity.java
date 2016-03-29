package com.android.linglan.ui.homepage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.adapter.AuthorArticleAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AuthorDetailsBean;
import com.android.linglan.ui.R;
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
 * Created by LeeMy on 2016/1/7 0007.
 * 作者详情页
 */
public class AuthorDetailsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private View header;
    private RelativeLayout author_back;
    private TextView author_right;
    private TextView author_attention;
    private TextView author_name;// 作者名字
    private TextView author_certification;// 认证作者
    private TextView author_resume;// 作者简介
    private TextView header_author_attention;// 关注的人数
    private TextView author_article;// 发布的文章条数
    private TextView author_page_view;// 访问量

    private SwipeMenuListView lv_author_article;
    private LinearLayout invis;
    private AuthorArticleAdapter adapter;
    private Intent intent;
    private AuthorDetailsBean authorDetailsBean;
    private ArrayList<AuthorDetailsBean.AuthorDetailsData> authorDetailsData;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_author_details);
        header = View.inflate(this, R.layout.authoe_header, null);//头部内容
    }

    @Override
    protected void initView() {
        author_attention = (TextView) findViewById(R.id.author_attention);

        lv_author_article = (SwipeMenuListView) findViewById(R.id.lv_author_article);
        invis = (LinearLayout) findViewById(R.id.invis);
        author_back = (RelativeLayout) header.findViewById(R.id.author_back);
        author_right = (TextView) header.findViewById(R.id.author_right);
        author_name = (TextView) header.findViewById(R.id.author_name);
        author_certification = (TextView) header.findViewById(R.id.author_certification);
        author_resume = (TextView) header.findViewById(R.id.author_resume);
        header_author_attention = (TextView) header.findViewById(R.id.author_attention);
        author_article = (TextView) header.findViewById(R.id.author_article);
        author_page_view = (TextView) header.findViewById(R.id.author_page_view);
    }

    @Override
    protected void initData() {
//        setTitle("作者详情页","");
//        initSlide();
        getDetailsAuthor("2");
        lv_author_article.addHeaderView(header);//添加头部
//        lv.addHeaderView(View.inflate(this, R.layout.authoe_stick_action, null));//ListView条目中的悬浮部分 添加到头部

//        author_article_lv.setAdapter(new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, strs));
        adapter = new AuthorArticleAdapter(this);
        lv_author_article.setAdapter(adapter);
//        header_author_attention.setText(getString(R.string.author_attention, "800"));
        intent = new Intent();
    }

    private void getDetailsAuthor(String authorid) {
        NetApi.getDetailsAuthor(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getDetailsAuthor=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,AuthorDetailsActivity.this)){
                    return;
                }
                authorDetailsBean = JsonUtil.json2Bean(result, AuthorDetailsBean.class);
//                authorDetailsData = new ArrayList<AuthorDetailsBean.AuthorDetailsData>();
                authorDetailsData = authorDetailsBean.data;
                if (authorDetailsData != null) {
                    for (AuthorDetailsBean.AuthorDetailsData authorDetails : authorDetailsData) {
                        author_name.setText(authorDetails.name);
                        if (authorDetails.isapprove.equals("1")) {
                            author_certification.setVisibility(View.VISIBLE);
                        }
                        author_resume.setText(authorDetails.about);
                        header_author_attention.setText(getString(R.string.author_attention, authorDetails.count_followed));// getString(R.string.author_attention, "800")
                        author_article.setText(getString(R.string.author_article, authorDetails.count_article));
                        author_page_view.setText(getString(R.string.author_page_view, authorDetails.count_followed));
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, authorid);
    }

    @Override
    protected void setListener() {
        author_back.setOnClickListener(this);
        author_right.setOnClickListener(this);
        lv_author_article.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem >= 1) {
//                    invis.setVisibility(View.VISIBLE);
//                } else {
//                    invis.setVisibility(View.GONE);
//                }
            }
        });

        lv_author_article.setOnItemClickListener(this);
        lv_author_article.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.author_back:
                finish();
                break;
            case R.id.author_right:
//                author_right.setCompoundDrawables(null, null, null, null);// android:drawableLeft的属性代码设置
//                author_right.setText("已关注");
                if (author_right.getText().toString().equals("已关注")) {// 进行取消关注操作

                } else {// 进行加关注操作
                    addFollow("作者id");
                }
                break;
        }
    }

    private void addFollow(String publisherid) {
        NetApi.addFollow(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,AuthorDetailsActivity.this)){
                    return;
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, publisherid);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ToastUtil.show(getActivity(), "点击第" + position + "个", 1);
        if (position != 0) {
            intent.setClass(AuthorDetailsActivity.this, ArticleDetailsActivity.class);
//            intent.putExtra("articleId", recommendArticle.articleid);
//            intent.putExtra("photo", recommendArticle.photo);
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            ToastUtil.show(AuthorDetailsActivity.this, "长按第" + position + "个被删除", 1);
        }
        return false;
    }

    private void initSlide() {
        // TODO Auto-generated method stub
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @SuppressLint("ResourceAsColor")
            @Override

            public void create(SwipeMenu menu) {
//                LogUtils.v("SwipeMenuCreator==create");



                SwipeMenuItem deleteItem = new SwipeMenuItem(AuthorDetailsActivity.this);
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
        lv_author_article.setMenuCreator(creator);

        // step 2. listener item click event
        lv_author_article.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                ToastUtil.show("onMenuItemClick==position="+position+"index=="+index);
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

        lv_author_article.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

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
        lv_author_article.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
//                LogUtils.v("onMenuOpen" + position);
            }

            @Override
            public void onMenuClose(int position) {
//                LogUtils.v("onMenuClose" + position);
            }
        });

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    //获取发布者发布的文章列表
    private void getUserublisheridrtiitem(String publisherid){
        NetApi.getUserublisheridrtiitem(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,AuthorDetailsActivity.this)){
                    return;
                }
            }

            @Override
            public void onFailure(String message) {

            }
        },publisherid);
    }
}
