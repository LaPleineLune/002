package com.android.linglan.ui.study;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.adapter.SearchAllRadioAdapter;
import com.android.linglan.adapter.SubjectDetailsAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.HomeSubjectFragment;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.RadioListBean;
import com.android.linglan.http.bean.SubjectDetailsBean;
import com.android.linglan.http.bean.SubjectInfoBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.me.NewCollectActivity;
import com.android.linglan.ui.me.NoteWritePreviewActivity;
import com.android.linglan.ui.me.RegisterActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ShareUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.android.linglan.widget.UpdateDialog;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubjectDetailsActivity extends BaseActivity implements View.OnClickListener {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    protected static final int REQUEST_LIKE = 2;
    protected static final int REQUEST_COLLECT = 3;
    protected static final int REQUEST_TYPE = 4;
    public static  int FLAG_TYPE = Constants.ALLARTICLE;//0文章,5电台
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
    private SubjectDetailsBean subjectDetails;
    private ArrayList<SubjectDetailsBean.SubjectList> subjectList;
    private  ArrayList<RadioListBean.RadioListData> fmList;
    private String isLike = "1";// 原：1
    private int like = -1;
    private String isCollect = "1";// 原：1
    private String isShare = "1";// 原：1
    private UpdateDialog exitLoginDialog;
    private SubjectDetailsAdapter subjectDetailsAdapter;

    private PtrClassicFrameLayout recycler_view_subject_details;
    private RecyclerAdapterWithHF mAdapter;
    private int pageArticle;
    private int pageFm;
    private String position;
    private String newCollectActivity;
    private String homeSubjectFragment;
    private String subjectActivity;
    private String colection;
    private boolean isLogin = false;
    private SubjectInfoBean subjectInfoBean;
    private RadioListBean radioListBean;
    private ArrayList<SubjectInfoBean.Data.typeClassisy> navi;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            subjectInfoBean = (SubjectInfoBean) msg.getData().getSerializable("data");
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
                        if (like == 0) {
                            subject_details_like.setText(subjectInfoBean.data.count_favour + "");
                        } else {
                            subject_details_like.setText(subjectInfoBean.data.count_favour + 1 + "");
                        }
//                        subject_details_like.setTextColor(getResources().getColor(R.color.carminum));
                        subject_details_like.setTextColor(ContextCompat.getColor(SubjectDetailsActivity.this, R.color.carminum));
                        Drawable likeTopDrawable = ContextCompat.getDrawable(SubjectDetailsActivity.this, R.drawable.article_write_like_ok);
//                        Drawable likeTopDrawable = getResources().getDrawable(R.drawable.article_write_like_ok);
                        likeTopDrawable.setBounds(0, 0, likeTopDrawable.getMinimumWidth(), likeTopDrawable.getMinimumHeight());
                        subject_details_like.setCompoundDrawables(null, likeTopDrawable, null, null);
                        isLike = "1";
                    } else if (isLike.equals("1")) {
                        if (like == 0) {
                            subject_details_like.setText((subjectInfoBean.data.count_favour - 1) + "");
                        } else {
                            subject_details_like.setText(subjectInfoBean.data.count_favour + "");
                        }
//                        subject_details_like.setTextColor(getResources().getColor(R.color.french_grey));
                        subject_details_like.setTextColor(ContextCompat.getColor(SubjectDetailsActivity.this, R.color.french_grey));
                        Drawable likeTopDrawable = ContextCompat.getDrawable(SubjectDetailsActivity.this, R.drawable.article_write_like);
//                        Drawable likeTopDrawable = getResources().getDrawable(R.drawable.article_write_like);
                        likeTopDrawable.setBounds(0, 0, likeTopDrawable.getMinimumWidth(), likeTopDrawable.getMinimumHeight());
                        subject_details_like.setCompoundDrawables(null, likeTopDrawable, null, null);
                        isLike = "0";
//                    } else {
//                        ToastUtil.show("没有数据，不支持点赞");
                    }

                    break;
                case REQUEST_COLLECT:
                    if (isCollect.equals("0")) {
//                        subject_details_collect.setTextColor(getResources().getColor(R.color.carminum));
                        subject_details_collect.setTextColor(ContextCompat.getColor(SubjectDetailsActivity.this, R.color.carminum));
                        Drawable collectTopDrawable = ContextCompat.getDrawable(SubjectDetailsActivity.this, R.drawable.article_write_collect_ok);
//                        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.article_write_collect_ok);
                        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                        subject_details_collect.setCompoundDrawables(null, collectTopDrawable, null, null);
                        isCollect = "1";
                    } else if (isCollect.equals("1")) {
//                        subject_details_collect.setTextColor(getResources().getColor(R.color.french_grey));
                        subject_details_collect.setTextColor(ContextCompat.getColor(SubjectDetailsActivity.this, R.color.french_grey));
                        Drawable collectTopDrawable = ContextCompat.getDrawable(SubjectDetailsActivity.this, R.drawable.article_write_collect);
//                        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.article_write_collect);
                        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                        subject_details_collect.setCompoundDrawables(null, collectTopDrawable, null, null);
                        isCollect = "0";
//                    } else {
//                        ToastUtil.show("没有数据，不支持收藏");
                    }
                    break;
                case REQUEST_TYPE:
                    navi = subjectInfoBean.data.navi;
                    if(navi != null){
                        if( navi.size() == 2){
                            getDetailsSubjectArticle(pageArticle);
                            getDetailsFmArticle(pageFm);
                        }else if(navi.size() == 1){
                            if((navi.get(0).type) == Integer.parseInt(Constants.ARTICLE)){
                                getDetailsSubjectArticle(pageArticle);
                            }else if((navi.get(0).type) == Integer.parseInt(Constants.RADIO_SINGLE)){
                                getDetailsFmArticle(pageFm);
                            }
                        }
                    }

                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
//        specialid = getIntent().getStringExtra("specialid");
//        LogUtil.d("specialid=" + specialid);
//        if (isShare.equals("1")) {
//            getDetailsSubject(page);
//        }
        if (isLogin) {
            specialid = getIntent().getStringExtra("specialid");
            getDetailsSubject();
            isLogin = false;
        }
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
        specialid = getIntent().getStringExtra("specialid");
        position = getIntent().getStringExtra("page");
        colection = getIntent().getStringExtra("colection");
        newCollectActivity = getIntent().getStringExtra("NewCollectActivity");
        homeSubjectFragment = getIntent().getStringExtra("HomeSubjectFragment");
        specialname = getIntent().getStringExtra("specialname");
        logo = getIntent().getStringExtra("logo");
        subjectActivity = getIntent().getStringExtra("subjectActivity");
        subjectList = new ArrayList<SubjectDetailsBean.SubjectList>();
        fmList = new ArrayList<RadioListBean.RadioListData>();
        navi = new ArrayList<SubjectInfoBean.Data.typeClassisy>();
        subjectInfoBean = new SubjectInfoBean();
        radioListBean = new RadioListBean();
        pageArticle = 1;
        pageFm = 1;
//        getDetailsSubjectArticle(page);
//        getDetailsFmArticle(page);
        getDetailsSubject();
        rec_subject_details.setLayoutManager(new LinearLayoutManager(this));
        rec_subject_details.setHasFixedSize(true);
        String description = "";
        if (!getIntent().getStringExtra("description").isEmpty()) {
            description = getIntent().getStringExtra("description");
        }
        subjectDetailsAdapter = new SubjectDetailsAdapter(this, description, getIntent().getStringExtra("photo"));

        mAdapter = new RecyclerAdapterWithHF(subjectDetailsAdapter);
//        mAdapter = new RecyclerAdapterWithHF(searchAllRadioAdapter);
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
        back.setOnClickListener(this);
        subject_details_note.setOnClickListener(this);
        subject_details_like.setOnClickListener(this);
        subject_details_collect.setOnClickListener(this);
        subject_details_share.setOnClickListener(this);
        ll_no_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

        //下拉刷新
        recycler_view_subject_details.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageArticle = 1;
                pageFm = 1;
                navi = subjectInfoBean.data.navi;
                if(navi != null){
                    if( navi.size() == 2){
                        if(FLAG_TYPE == Constants.ALLARTICLE){
                            getDetailsSubjectArticle(pageArticle);
                        }else if(FLAG_TYPE == Constants.ALLFM){
                            getDetailsFmArticle(pageFm);
                        }
                    }else if(navi.size() == 1){
                        if((navi.get(0).type) == Integer.parseInt(Constants.ARTICLE)){
                            getDetailsSubjectArticle(pageArticle);
                        }else if((navi.get(0).type) == Integer.parseInt(Constants.RADIO_SINGLE)){
                            getDetailsFmArticle(pageFm);
                        }
                    }
                }
            }
        });

        //上拉刷新
        recycler_view_subject_details.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                navi = subjectInfoBean.data.navi;
                if(navi != null){
                    if( navi.size() == 2){
                        if(FLAG_TYPE == Constants.ALLARTICLE){
                            pageArticle++;
                            getDetailsSubjectArticle(pageArticle);
                        }else if(FLAG_TYPE == Constants.ALLFM){
                            pageFm++;
                            getDetailsFmArticle(pageFm);
                        }
                    }else if(navi.size() == 1){
                        if((navi.get(0).type) == Integer.parseInt(Constants.ARTICLE)){
                            pageArticle++;
                            getDetailsSubjectArticle(pageArticle);
                        }else if((navi.get(0).type) == Integer.parseInt(Constants.RADIO_SINGLE)){
                            pageFm++;
                            getDetailsFmArticle(pageFm);
                        }
                    }
                }
                recycler_view_subject_details.loadMoreComplete(true);
            }

        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        isShare = "1";
        switch (v.getId()) {
            case R.id.back:
                if(Constants.isSubjectActivity){
                    Constants.isSubjectActivity = false;
                    intent.setClass(SubjectDetailsActivity.this, SubjectActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.subject_details_note:
                intent.putExtra("note", "write");
                intent.setClass(SubjectDetailsActivity.this, NoteWritePreviewActivity.class);
                startActivity(intent);
                break;
            case R.id.subject_details_like:
//                ToastUtil.show("我是点赞");
                MobclickAgent.onEvent(this, UmengBuriedPointUtil.StudySubjectClickLike);
                if (!isLike.equals("")) {
                    if (SharedPreferencesUtil.getString("token", null) != null) {
                        getDetailsSubjectLike();
                    } else {
                        showExitDialog();
                    }
                }
// else {
////                    ToastUtil.show("暂无数据，不支持点赞");
//                    ToastUtil.show("暂不支持点赞");
//                }
                break;
            case R.id.subject_details_collect:
//                ToastUtil.show("我是收藏");
                MobclickAgent.onEvent(this, UmengBuriedPointUtil.StudySubjectClickCollect);
                if (!isCollect.equals("")) {
                    if (SharedPreferencesUtil.getString("token", null) != null) {
                        getDetailsSubjectCollect();
                    } else {
                        showExitDialog();
                    }
                }
//                else {
////                    ToastUtil.show("暂无数据，不支持收藏");
//                    ToastUtil.show("暂不支持收藏");
//                }
                break;
            case R.id.subject_details_share:
//                ToastUtil.show("我是分享");
                MobclickAgent.onEvent(this, UmengBuriedPointUtil.StudySubjectClickShareCircle);
                isShare = "0";
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
                isLogin = true;
                if (subjectActivity != null && subjectActivity.equals("subjectActivity")) {
                    intent.putExtra("subjectActivity", "subjectActivity");
                }
                intent.setClass(SubjectDetailsActivity.this, RegisterActivity.class);
                startActivity(intent);

                exitLoginDialog.dismiss();
            }
        });
        exitLoginDialog.setTitle("提示");
        exitLoginDialog.setCancelText("随便看看");
        exitLoginDialog.setEnterText("马上登录");
        exitLoginDialog.show();
    }

    /**
     * 获取专题详情文章列表
     */
    private void getDetailsSubjectArticle(final int page) {
        NetApi.getDetailsSubjectArticle(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getDetailsSubjectArticle=" + result);
                recycler_view_subject_details.refreshComplete();
                recycler_view_subject_details.setLoadMoreEnable(true);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SubjectDetailsActivity.this)) {
                    recycler_view_subject_details.loadMoreComplete(false);
                    return;
                }
                subjectDetails = JsonUtil.json2Bean(result, SubjectDetailsBean.class);

                if (page == 1) {
                    subjectList.clear();
//                    subjectList = subjectDetails.data.list;
                    for (SubjectDetailsBean.SubjectList sl : subjectDetails.data) {
                        subjectList.add(sl);
                    }
                }else{
                    for (SubjectDetailsBean.SubjectList sl : subjectDetails.data) {
                        subjectList.add(sl);
                    }
                }
                handler.sendEmptyMessage(REQUEST_SUCCESS);
                subjectDetailsAdapter.updateArticleAdapter(subjectList);
            }

            @Override
            public void onFailure(String message) {
                recycler_view_subject_details.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, specialid, page + "");
    }

    /**
     * 获取专题详情音频列表
     */
    private void getDetailsFmArticle(final int page) {
        NetApi.getDetailsFmArticle(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getDetailsFmArticle getDetailsFmArticle getDetailsFmArticle=" + result);
                recycler_view_subject_details.refreshComplete();
                recycler_view_subject_details.setLoadMoreEnable(true);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SubjectDetailsActivity.this)) {
                    recycler_view_subject_details.loadMoreComplete(false);
                    return;
                }

                radioListBean = JsonUtil.json2Bean(result, RadioListBean.class);

                if (page == 1) {
                    fmList.clear();
//                    subjectList = subjectDetails.data.list;
                    for (RadioListBean.RadioListData sl : radioListBean.data) {
                        fmList.add(sl);
                    }
                }else{
                    for (RadioListBean.RadioListData sl : radioListBean.data) {
                        fmList.add(sl);
                    }
                }
                handler.sendEmptyMessage(REQUEST_SUCCESS);
                subjectDetailsAdapter.updateFmAdapter(fmList);

            }

            @Override
            public void onFailure(String message) {
                recycler_view_subject_details.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, specialid, page + "");
    }


    /**
     * 获取专题详情
     */
    private void getDetailsSubject() {
        NetApi.getDetailsSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getDetailsSubject  =" + result);
                recycler_view_subject_details.refreshComplete();
                recycler_view_subject_details.setLoadMoreEnable(true);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SubjectDetailsActivity.this)) {
                        isLike = "";
                        isCollect = "";
                    recycler_view_subject_details.loadMoreComplete(false);
                    return;
                }

                subjectInfoBean = JsonUtil.json2Bean(result, SubjectInfoBean.class);

                isLike = subjectInfoBean.data.favouriscancel +"";
                if (subjectInfoBean.data.favouriscancel == 0) {
                    like = 0;
                } else {
                    like = 1;
                }
                handler.sendEmptyMessage(REQUEST_LIKE);
                isCollect = subjectInfoBean.data.collectiscancel + "";
                handler.sendEmptyMessage(REQUEST_COLLECT);
                handler.sendEmptyMessage(REQUEST_SUCCESS);
                handler.sendEmptyMessage(REQUEST_TYPE);
                subjectDetailsAdapter.updateTypeAdapter(subjectInfoBean.data.navi);
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

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SubjectDetailsActivity.this)) {
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

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SubjectDetailsActivity.this)) {
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
                    LogUtil.e("position=" + position);

                    if ( position !=null) {
                        if (newCollectActivity != null && newCollectActivity.equals("NewCollectActivity")) {
                            NewCollectActivity.position = Integer.parseInt(position);
                            NewCollectActivity.isCancle = 1;
                        } else if (homeSubjectFragment != null && homeSubjectFragment.equals("HomeSubjectFragment")) {
                            HomeSubjectFragment.position = Integer.parseInt(position);
                            HomeSubjectFragment.isCancle = 1;
                        }

                        LogUtil.e("NewCollectActivity.isCancle = " + NewCollectActivity.isCancle);
                        LogUtil.e("HomeSubjectFragment.isCancle = " + HomeSubjectFragment.isCancle);
                    } else {

                        if (newCollectActivity != null && newCollectActivity.equals("newCollectActivity")) {
                            NewCollectActivity.isCancle = 0;
                        } else if (homeSubjectFragment != null && homeSubjectFragment.equals("HomeSubjectFragment")) {
                            HomeSubjectFragment.isCancle = 2;
                        }

                    }

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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LogUtil.d("onKeyUp keyCode: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(Constants.isSubjectActivity){
                Constants.isSubjectActivity = false;
                intent.setClass(SubjectDetailsActivity.this, SubjectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
