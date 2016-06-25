package com.android.linglan.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.Constants;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.HomepageRecommendBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.fm.RadioActivity;
import com.android.linglan.ui.fm.RadioPlayActivity;
import com.android.linglan.ui.fm.RadioSpecialActivity;
import com.android.linglan.ui.study.ArticleActivity;
import com.android.linglan.ui.study.ArticleDetailsActivity;
import com.android.linglan.ui.study.SearchActivity;
import com.android.linglan.ui.study.StudyMoreActivity;
import com.android.linglan.ui.study.SubjectActivity;
import com.android.linglan.ui.study.SubjectDetailsActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeMy on 2016/2/26 0026.
 * 学习页面
 */
public class StudyFragment extends BaseFragment {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private View rootView;
    private PtrClassicFrameLayout ptrclassicframelayout_view_study;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView rc_view_study;
    private LinearLayout ll_no_network;
    private RecycleHomeRecommendAdapter adapter;
    private ImageView img_home_search;
    public ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> data;
    private Intent intent;
    private int page;//页码
//    private CustomPullToRefreshRecyclerView refresh_more_every;

    private List<String> mData = new ArrayList<String>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_FAILURE:
                    //原页面GONE掉，提示网络不好的页面出现
                    ptrclassicframelayout_view_study.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);

                    break;
                case REQUEST_SUCCESS:
                    //原页面GONE掉，提示网络不好的页面出现
                    ptrclassicframelayout_view_study.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (ClinicalFragment.ISREFRESHDATA == 1) {
            page = 1;
            getHomeRecommend(page);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home_recommend, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {
        rc_view_study = (RecyclerView) rootView.findViewById(R.id.rc_view_study);
        ptrclassicframelayout_view_study = (PtrClassicFrameLayout) rootView.findViewById(R.id.ptrclassicframelayout_view_study);
        img_home_search = (ImageView) rootView.findViewById(R.id.img_home_search);
        ll_no_network = (LinearLayout) rootView.findViewById(R.id.ll_no_network);
    }

    @Override
    protected void initData() {
        intent = new Intent();
        data = new ArrayList<HomepageRecommendBean.HomepageRecommendBeanData>();

        page = 1;
        getHomeRecommend(page);
        adapter = new RecycleHomeRecommendAdapter(getActivity());
        mAdapter = new RecyclerAdapterWithHF(adapter);
        rc_view_study.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_view_study.setHasFixedSize(true);
        rc_view_study.setAdapter(mAdapter);

    }

    @Override
    protected void setListener() {
        //下拉刷新
        ptrclassicframelayout_view_study.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.StudyHomepageManualRefresh);
                page = 1;
                getHomeRecommend(page);

            }
        });

        //上拉刷新
        ptrclassicframelayout_view_study.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.StudyHomepageManualRefresh);
                page++;
                getHomeRecommend(page);
                ptrclassicframelayout_view_study.loadMoreComplete(true);
            }
        });


        ll_no_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

        img_home_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.StudySelectSearch);
                intent.setClass(getActivity(), SearchActivity.class);
                intent.putExtra("searchEdit", Constants.HOME);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取首页推荐
     */
    private void getHomeRecommend(final int page) {
        NetApi.getHomeRecommend(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                ptrclassicframelayout_view_study.refreshComplete();
                ptrclassicframelayout_view_study.setLoadMoreEnable(true);
                LogUtil.e("getHomeRecommend getHomeRecommend getHomeRecommend=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, getActivity())) {
                    ptrclassicframelayout_view_study.loadMoreComplete(false);
                    return;
                }

                HomepageRecommendBean homepageRecommendBean = JsonUtil.json2Bean(result, HomepageRecommendBean.class);

                if (page == 1) {
//                    data = homepageRecommendBean.data;
                    data.clear();
                    for (HomepageRecommendBean.HomepageRecommendBeanData hrd : homepageRecommendBean.data) {
                        if (hrd.type.equals(Constants.ARTICLE) || hrd.type.equals(Constants.SUBJECT)  || hrd.type.equals(Constants.RADIO_SPECIAL) || hrd.type.equals(Constants.RADIO_SINGLE)) {
                            data.add(hrd);
                        }
                    }
                } else {
                    if (homepageRecommendBean.data == null || (homepageRecommendBean.data).size() == 0) {
                        ToastUtil.show("没有数据了");
                    } else {
//                        data.addAll(homepageRecommendBean.data);
                        for (HomepageRecommendBean.HomepageRecommendBeanData hrd : homepageRecommendBean.data) {
                            if (hrd.type.equals(Constants.ARTICLE) || hrd.type.equals(Constants.SUBJECT) || hrd.type.equals(Constants.RADIO_SPECIAL) || hrd.type.equals(Constants.RADIO_SINGLE)) {
                                data.add(hrd);
                            }
                        }
                    }
                }

                if (data != null && data.size() != 0) {
                    adapter.updateAdapter(data, false);
                }

                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                ptrclassicframelayout_view_study.refreshComplete();
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, page + "");
    }


    /**
     * Created by wuiqngci on 2016/1/6 0006.
     */
    public class RecycleHomeRecommendAdapter extends RecyclerView.Adapter {
        private Context context;
        private Intent intent;
        private boolean edit = false;
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_NORMAL = 1;
        private View mHeaderView;

        public ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> homepageRecommend;

        public RecycleHomeRecommendAdapter(Context context) {
            this.context = context;
        }

        public RecycleHomeRecommendAdapter(Context context, ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> homepageRecommend, boolean edit) {
            this.context = context;
            this.homepageRecommend = homepageRecommend;
            this.edit = edit;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(context).inflate(
//                    R.layout.item_homepage_recommend, parent, false);
//            return new ArticleViewHolder(view);

            if(viewType == TYPE_HEADER){
                mHeaderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homepage_header, parent, false);
                return new ArticleViewHolder(mHeaderView);
            }else{
                View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homepage_recommend, parent, false);
                return new ArticleViewHolder(layout);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            if(getItemViewType(position) == TYPE_HEADER) return;
            if(position != 0){
                ((ArticleViewHolder) holder).bindData(homepageRecommend.get(position-1), position);
            }else{
                ((ArticleViewHolder) holder).bindData(null, position);
            }
        }


        @Override
        public int getItemCount() {
            return this.homepageRecommend != null ? this.homepageRecommend.size() + 1 : 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public void updateAdapter(ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> homepageRecommend, boolean edit) {
            this.homepageRecommend = homepageRecommend;
            this.edit = edit;
            this.notifyDataSetChanged();
        }

        class ArticleViewHolder extends RecyclerView.ViewHolder implements
                View.OnClickListener {
            private View rootView;

            private ImageView logo;
            private ImageView play_logo;

            private LinearLayout ll_homepage_article;
            private TextView article_title;
            private TextView article_date;
            private TextView article_new;

            private LinearLayout ll_homepage_subject;
            private TextView subject_title;
            private TextView subject_description;
            private ImageView subject_flag;
            private TextView subject_date;
            private TextView subject_new;
            private TextView tv_study_article,tv_study_subject,tv_study_fm,tv_study_more;
            private ImageView img_item_article_delete;
            private HomepageRecommendBean.HomepageRecommendBeanData recommendArticle;

            public ArticleViewHolder(View rootView) {
                super(rootView);
                initView(rootView);
            }

            private void initView(View rootView) {
                this.rootView = rootView;
                tv_study_article = (TextView) rootView.findViewById(R.id.tv_study_article);
                tv_study_subject = (TextView) rootView.findViewById(R.id.tv_study_subject);
                tv_study_fm = (TextView) rootView.findViewById(R.id.tv_study_fm);
                tv_study_more = (TextView) rootView.findViewById(R.id.tv_study_more);
                logo = (ImageView) rootView.findViewById(R.id.img_homepage_logo);
                play_logo = (ImageView) rootView.findViewById(R.id.img_homepage_play_logo);
                ll_homepage_article = (LinearLayout) rootView.findViewById(R.id.ll_homepage_article);
                article_title = (TextView) rootView.findViewById(R.id.tv_homepage_article_title);
                article_date = (TextView) rootView.findViewById(R.id.tv_homepage_article_date);
                article_new = (TextView) rootView.findViewById(R.id.tv_homepage_article_new);

                ll_homepage_subject = (LinearLayout) rootView.findViewById(R.id.ll_homepage_subject);
                subject_title = (TextView) rootView.findViewById(R.id.tv_homepage_subject_title);
                subject_description = (TextView) rootView.findViewById(R.id.tv_homepage_subject_description);
                subject_flag = (ImageView) rootView.findViewById(R.id.tv_homepage_subject_flag);
                subject_date = (TextView) rootView.findViewById(R.id.tv_homepage_subject_date);
                subject_new = (TextView) rootView.findViewById(R.id.tv_homepage_subject_new);

                img_item_article_delete = (ImageView) rootView.findViewById(R.id.img_item_article_delete);

            }

            public void bindData(final HomepageRecommendBean.HomepageRecommendBeanData recommendArticle, final int position) {
                this.recommendArticle = recommendArticle;
                if(position == 0) {
                    tv_study_article.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.StudySelectArticle);
                            Intent intent = new Intent(context, ArticleActivity.class);
                            context.startActivity(intent);
                        }
                    });
                    tv_study_subject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.StudySelectSubject);
                            Intent intent = new Intent(context, SubjectActivity.class);
                            context.startActivity(intent);
                        }
                    });
                    tv_study_fm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RadioActivity.class);
                            context.startActivity(intent);
                        }
                    });
                    tv_study_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.StudySelectMore);
                            Intent intent = new Intent(context, StudyMoreActivity.class);
                            context.startActivity(intent);
                        }
                    });
                    return;
                }

                if (edit) {
                    img_item_article_delete.setVisibility(View.VISIBLE);
                    rootView.setOnClickListener(null);
                    img_item_article_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                        ToastUtil.show("删除成功");
                            if (recommendArticle.type.equals(Constants.SUBJECT)) {
//                                getSubjectCancleCollect(position,recommendArticle.specialid,"1");
                            } else if (recommendArticle.type.equals(Constants.ARTICLE)) {
//                                getArticleCancleCollect(position,recommendArticle.articleid,"1");
                            }
                        }
                    });
                } else {
                    img_item_article_delete.setVisibility(View.GONE);

                    rootView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (recommendArticle.type.equals(Constants.SUBJECT)) {// 如果是专题
                                SharedPreferencesUtil.saveString("specialid" + recommendArticle.specialid, recommendArticle.specialid);
                                subject_title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                                MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.StudySubjectContent);
                                intent = new Intent(context, SubjectDetailsActivity.class);
                                intent.putExtra("specialid", recommendArticle.specialid);
                                intent.putExtra("specialname", recommendArticle.specialname);
                                intent.putExtra("logo", recommendArticle.logo);
                                intent.putExtra("photo", recommendArticle.photo);
                                intent.putExtra("description", recommendArticle.description);
                            } else if (recommendArticle.type.equals(Constants.ARTICLE)) {// 如果是文章
                                MobclickAgent.onEvent(context, UmengBuriedPointUtil.StudyClickCharacter);
                                SharedPreferencesUtil.saveString("articleid" + recommendArticle.articleid, recommendArticle.articleid);
                                article_title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                                intent = new Intent(context, ArticleDetailsActivity.class);
                                intent.putExtra("articleId", recommendArticle.articleid);
                                intent.putExtra("photo", recommendArticle.photo);
                            } else if (recommendArticle.type.equals(Constants.RADIO_SPECIAL)) {// 如果是合辑
//                                MobclickAgent.onEvent(context, UmengBuriedPointUtil.StudyClickCharacter);
                                SharedPreferencesUtil.saveString("albumid" + recommendArticle.albumid, recommendArticle.albumid);
                                subject_title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                                intent = new Intent(context, RadioSpecialActivity.class);
                                intent.putExtra("albumid", recommendArticle.albumid);
                                intent.putExtra("albumname", recommendArticle.albumname);
                            }else if (recommendArticle.type.equals(Constants.RADIO_SINGLE)) {// 如果是音频
//                                MobclickAgent.onEvent(context, UmengBuriedPointUtil.StudyClickCharacter);
                                SharedPreferencesUtil.saveString("audioid" + recommendArticle.audioid, recommendArticle.audioid);
                                subject_title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                                intent = new Intent(context, RadioPlayActivity.class);
                                intent.putExtra("audioid", recommendArticle.audioid);
                            }
                            context.startActivity(intent);
                        }
                    });
                }
                if (recommendArticle.type.equals(Constants.SUBJECT)) {// 专题
                    play_logo.setVisibility(View.GONE);
                    subject_flag.setVisibility(View.VISIBLE);
                    subject_flag.setImageResource(R.drawable.homepage_subject);
                    try {
                        ImageUtil.loadImageAsync(logo, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, recommendArticle.logo, null);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    ll_homepage_subject.setVisibility(View.VISIBLE);
                    ll_homepage_article.setVisibility(View.GONE);
                    subject_title.setText(recommendArticle.specialname);
                    String oldSpecialid = SharedPreferencesUtil.getString("specialid" + recommendArticle.specialid, null);
                    if (oldSpecialid != null && oldSpecialid.equals(recommendArticle.specialid)) {
                        subject_title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                    } else {
                        subject_title.setTextColor(ContextCompat.getColor(context, R.color.text_title_color));
                    }
                    subject_description.setText(recommendArticle.content_title);
                    if (recommendArticle.isnew != null && recommendArticle.isnew.equals("1")) {
                        subject_new.setVisibility(View.VISIBLE);
                    } else {
                        subject_new.setVisibility(View.GONE);
                    }
                } else if (recommendArticle.type.equals(Constants.ARTICLE)) {// 文章
                    try {
                        ImageUtil.loadImageAsync(logo, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, recommendArticle.photo, null);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    play_logo.setVisibility(View.GONE);
                    ll_homepage_subject.setVisibility(View.GONE);
                    ll_homepage_article.setVisibility(View.VISIBLE);
                    article_title.setText(recommendArticle.title);
                    String oldArticleid = SharedPreferencesUtil.getString("articleid" + recommendArticle.articleid, null);
                    if (oldArticleid != null && oldArticleid.equals(recommendArticle.articleid)) {
                        article_title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                    } else {
                        article_title.setTextColor(ContextCompat.getColor(context, R.color.text_title_color));
                    }
                    if (recommendArticle.isnew != null && recommendArticle.isnew.equals("1")) {
                        article_new.setVisibility(View.VISIBLE);
                    } else {
                        article_new.setVisibility(View.GONE);
                    }
                    if (recommendArticle.authornames != null && !recommendArticle.authornames.equals("")) {
                        article_date.setVisibility(View.VISIBLE);
                        Drawable collectTopDrawable = context.getResources().getDrawable(R.drawable.article);
                        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                        article_date.setCompoundDrawables(collectTopDrawable, null, null, null);
                        article_date.setCompoundDrawablePadding(12);
                        article_date.setText(recommendArticle.authornames);
                    } else {
                        article_date.setVisibility(View.GONE);
                    }
                }else if(recommendArticle.type.equals(Constants.RADIO_SPECIAL)){
                    play_logo.setVisibility(View.VISIBLE);
                    subject_flag.setVisibility(View.VISIBLE);
                    subject_flag.setImageResource(R.drawable.homepage_album);
                    try {
                        ImageUtil.loadImageAsync(logo, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, recommendArticle.logo, null);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    String oldAlbumid = SharedPreferencesUtil.getString("albumid" + recommendArticle.albumid, null);
                    if (oldAlbumid != null && oldAlbumid.equals(recommendArticle.albumid)) {
                        subject_title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                    } else {
                        subject_title.setTextColor(ContextCompat.getColor(context, R.color.text_title_color));
                    }

                    ll_homepage_subject.setVisibility(View.VISIBLE);
                    ll_homepage_article.setVisibility(View.GONE);
                    subject_title.setText(recommendArticle.albumname);
                    subject_description.setText(recommendArticle.description);

                    if (recommendArticle.isnew != null && recommendArticle.isnew.equals("1")) {
                        subject_new.setVisibility(View.VISIBLE);
                    } else {
                        subject_new.setVisibility(View.GONE);
                    }
                }else if(recommendArticle.type.equals(Constants.RADIO_SINGLE)){
                    play_logo.setVisibility(View.VISIBLE);
                    subject_flag.setVisibility(View.GONE);
                    try {
                        ImageUtil.loadImageAsync(logo, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, recommendArticle.photo, null);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    String oldAudioid = SharedPreferencesUtil.getString("audioid" + recommendArticle.audioid, null);
                    if (oldAudioid != null && oldAudioid.equals(recommendArticle.albumid)) {
                        subject_title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                    } else {
                        subject_title.setTextColor(ContextCompat.getColor(context, R.color.text_title_color));
                    }

                    ll_homepage_subject.setVisibility(View.VISIBLE);
                    ll_homepage_article.setVisibility(View.GONE);
                    subject_title.setText(recommendArticle.title);
                    subject_description.setText(recommendArticle.description);

                    if (recommendArticle.isnew != null && recommendArticle.isnew.equals("1")) {
                        subject_new.setVisibility(View.VISIBLE);
                    } else {
                        subject_new.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onClick(View v) {

            }
        }
    }
}
