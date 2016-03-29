package com.android.linglan.ui.homepage;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.adapter.SubjectDetailsAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.SubjectDetails;
import com.android.linglan.ui.R;
import com.android.linglan.ui.me.NoteWritePreviewActivity;
import com.android.linglan.ui.me.RegisterActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ShareUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.SyLinearLayoutManager;
import com.android.linglan.widget.UpdateDialog;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubjectDetailsActivity extends BaseActivity implements View.OnClickListener {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    protected static final int REQUEST_LIKE = 2;
    protected static final int REQUEST_COLLECT = 3;
    private String specialname = "";
    private String logo;// 小图
    private RecyclerView rec_subject_details;
    private RelativeLayout rl_subject_details;
    private LinearLayout ll_no_network;
    private TextView subject_details_note;
    private TextView subject_details_like;
    private TextView subject_details_collect;
    private TextView subject_details_share;
    private Intent intent;
    private String specialid;
    private ArrayList<SubjectDetails.SubjectData> subjectData;
    private SubjectDetails subjectDetails;
    private String isLike = "1";// 原：1
    private String isCollect = "1";// 原：1
    private UpdateDialog exitLoginDialog;
    private SubjectDetailsAdapter subjectDetailsAdapter;
    private PtrClassicFrameLayout recycler_view_subject_details;
    private RecyclerAdapterWithHF mAdapter;
    private int page;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ArrayList<SubjectDetails.SubjectData> subjectData = (ArrayList<SubjectDetails.SubjectData>) msg.getData().getSerializable("data");
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    rl_subject_details.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    rl_subject_details.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_LIKE:
                    if (isLike.equals("0")) {
                        subject_details_like.setTextColor(getResources().getColor(R.color.gray_orange));
                        Drawable likeTopDrawable = getResources().getDrawable(R.drawable.article_write_like_ok);
                        likeTopDrawable.setBounds(0, 0, likeTopDrawable.getMinimumWidth(), likeTopDrawable.getMinimumHeight());
                        subject_details_like.setCompoundDrawables(null, likeTopDrawable, null, null);
                        isLike = "1";
                    } else if (isLike.equals("1")) {
                        subject_details_like.setTextColor(getResources().getColor(R.color.french_grey));
                        Drawable likeTopDrawable = getResources().getDrawable(R.drawable.article_write_like);
                        likeTopDrawable.setBounds(0, 0, likeTopDrawable.getMinimumWidth(), likeTopDrawable.getMinimumHeight());
                        subject_details_like.setCompoundDrawables(null, likeTopDrawable, null, null);
                        isLike = "0";
//                    } else {
//                        ToastUtil.show("没有数据，不支持点赞");
                    }

                    break;
                case REQUEST_COLLECT:
                    if (isCollect.equals("0")) {
                        subject_details_collect.setTextColor(getResources().getColor(R.color.gray_orange));
                        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.article_write_collect_ok);
                        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                        subject_details_collect.setCompoundDrawables(null, collectTopDrawable, null, null);
                        isCollect = "1";
                    } else if (isCollect.equals("1")) {
                        subject_details_collect.setTextColor(getResources().getColor(R.color.french_grey));
                        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.article_write_collect);
                        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                        subject_details_collect.setCompoundDrawables(null, collectTopDrawable, null, null);
                        isCollect = "0";
//                    } else {
//                        ToastUtil.show("没有数据，不支持收藏");
                    }
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        specialid = getIntent().getStringExtra("specialid");
        LogUtil.d("specialid=" + specialid);
        getDetailsSubject();
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_subject_details);
    }

    @Override
    protected void initView() {
        recycler_view_subject_details = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_subject_details);
        rec_subject_details = (RecyclerView) findViewById(R.id.rec_subject_details);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        rl_subject_details = (RelativeLayout) findViewById(R.id.rl_subject_details);
        subject_details_note = (TextView) findViewById(R.id.subject_details_note);
        subject_details_like = (TextView) findViewById(R.id.subject_details_like);
        subject_details_collect = (TextView) findViewById(R.id.subject_details_collect);
        subject_details_share = (TextView) findViewById(R.id.subject_details_share);
    }

    @Override
    protected void initData() {
        setTitle("专题详情", "");
        intent = new Intent();
        specialname = getIntent().getStringExtra("specialname");
        logo = getIntent().getStringExtra("logo");

        rec_subject_details.setLayoutManager(new SyLinearLayoutManager(this));
        rec_subject_details.setHasFixedSize(true);
        String description = "";
        if (!getIntent().getStringExtra("description").isEmpty()) {
            description = getIntent().getStringExtra("description");
        }
        subjectDetailsAdapter = new SubjectDetailsAdapter(this, description, getIntent().getStringExtra("photo"));
        mAdapter = new RecyclerAdapterWithHF(subjectDetailsAdapter);
        rec_subject_details.setAdapter(mAdapter);

        //设置打印友盟log日志
        com.umeng.socialize.utils.Log.LOG = true;
        //关闭友盟toast提示
        ShareUtil.mController.getConfig().closeToast();
        // 配置需要分享的相关平台
        ShareUtil.configPlatforms(SubjectDetailsActivity.this);
        // 设置分享的内容String articleORsubject, String id, String shareTitle, String imgUrl, String shareContent
//        ShareUtil.setShareContent(this, Constants.SUBJECT, specialid, specialname, logo, getIntent().getStringExtra("description"));
        ShareUtil.setShareContent(this, Constants.SUBJECT, getIntent().getStringExtra("specialid"), specialname, logo, getIntent().getStringExtra("description"));
    }

    @Override
    protected void setListener() {
        subject_details_note.setOnClickListener(this);
        subject_details_like.setOnClickListener(this);
        subject_details_collect.setOnClickListener(this);
        subject_details_share.setOnClickListener(this);
        ll_no_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
            }
        });

        //下拉刷新
        recycler_view_subject_details.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
            page = 1;
            getDetailsSubject();
            }
        });

        //上拉刷新
        recycler_view_subject_details.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
            page++;
            getDetailsSubject();
            recycler_view_subject_details.loadMoreComplete(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.subject_details_note:
                intent.putExtra("note", "write");
                intent.setClass(SubjectDetailsActivity.this, NoteWritePreviewActivity.class);
                startActivity(intent);
                break;
            case R.id.subject_details_like:
//                ToastUtil.show("我是点赞");
                if (!isLike.equals("")) {
                    if (SharedPreferencesUtil.getString("token", null) != null) {
                        getDetailsSubjectLike();
                    } else {
                        showExitDialog();
                    }
                } else {
                    ToastUtil.show("暂无数据，不支持点赞");
                }
                break;
            case R.id.subject_details_collect:
//                ToastUtil.show("我是收藏");
                if (!isCollect.equals("")) {
                    if (SharedPreferencesUtil.getString("token", null) != null) {
                        getDetailsSubjectCollect();
                    } else {
                        showExitDialog();
                    }
                } else {
                    ToastUtil.show("暂无数据，不支持收藏");
                }
                break;
            case R.id.subject_details_share:
//                ToastUtil.show("我是分享");
                ShareUtil.umengPlatforms(SubjectDetailsActivity.this);
                break;
            default:
                break;
        }
    }

    private void showExitDialog() {
        exitLoginDialog = new UpdateDialog(this, "登录后可体验更多功能", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SubjectDetailsActivity.this, RegisterActivity.class));
                exitLoginDialog.dismiss();
            }
        });
        exitLoginDialog.setTitle("提示");
        exitLoginDialog.setCancelText("我先看看");
        exitLoginDialog.setEnterText("马上登录");
        exitLoginDialog.show();
    }

    /**
     * 获取专题详情
     */
    private void getDetailsSubject() {
        NetApi.getDetailsSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getDetailsSubject=" + result);
                recycler_view_subject_details.refreshComplete();
                recycler_view_subject_details.setLoadMoreEnable(true);
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SubjectDetailsActivity.this)){
                    isLike = "";
                    isCollect = "";
                    recycler_view_subject_details.loadMoreComplete(false);
                    return;
                }
                subjectDetails = JsonUtil.json2Bean(result, SubjectDetails.class);

                isLike = subjectDetails.favouriscancel;
                handler.sendEmptyMessage(REQUEST_LIKE);
                isCollect = subjectDetails.collectiscancel;
                handler.sendEmptyMessage(REQUEST_COLLECT);

                subjectData = subjectDetails.data;

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", subjectData);
                message.setData(bundle);// bundle传值，耗时，效率低
                handler.sendMessage(message);// 发送message信息
                message.what = REQUEST_SUCCESS;// 标志是哪个线程传数据

                subjectDetailsAdapter.upDateAdapter(subjectData);

            }

            @Override
            public void onFailure(String message) {
//                LogUtil.e(message);
                recycler_view_subject_details.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, specialid);
    }

    /**
     * 专题点赞
     */
    private void getDetailsSubjectLike() {
        NetApi.getDetailsSubjectLike(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("专题点赞" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SubjectDetailsActivity.this)){
                    return;
                }

                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("code").equals("0")) {
                        handler.sendEmptyMessage(REQUEST_LIKE);
                    }
                    String msg = json.getString("msg");
                    ToastUtil.show(msg);
                    LogUtil.e("专题点赞" + msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
//                ToastUtil.show("专题点赞" + message);
                LogUtil.e("专题点赞" + message);

            }
        }, specialid, isLike);

    }

    /**
     * 专题收藏
     */
    private void getDetailsSubjectCollect() {
        NetApi.getDetailsSubjectCollect(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("专题收藏" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SubjectDetailsActivity.this)){
                    return;
                }
                try {
                    JSONObject json = new JSONObject(result);
                    if (json.getString("code").equals("0")) {
                        handler.sendEmptyMessage(REQUEST_COLLECT);
//                        article_write_collect.setTextColor(getResources().getColor(R.color.gray_orange));
//                        article_write_collect.setCompoundDrawables(null, getResources().getDrawable(R.drawable.article_write_collect_ok), null, null);
                    }
                    String msg = json.getString("msg");
                    ToastUtil.show(msg);
                    LogUtil.e("专题收藏" + msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
//                ToastUtil.show("专题收藏" + message);
                LogUtil.e("专题收藏" + message);

            }
        }, specialid, isCollect);
    }
}
