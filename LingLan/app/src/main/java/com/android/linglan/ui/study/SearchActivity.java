package com.android.linglan.ui.study;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.adapter.ArticleSearchAdapter;
import com.android.linglan.adapter.FmSearchAdapter;
import com.android.linglan.adapter.SearchAdapter;
import com.android.linglan.adapter.SubjectSearchAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllSearchListBean;
import com.android.linglan.http.bean.HistorySearcherBean;
import com.android.linglan.http.bean.HotAndHistorySearcherBean;
import com.android.linglan.http.bean.HotSearcherBean;
import com.android.linglan.http.bean.SearchArticleBean;
import com.android.linglan.http.bean.SearchFMBean;
import com.android.linglan.http.bean.SearchSubjectBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.TCMSearchRequestResultActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.KeyBoardUtils;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.android.linglan.widget.SyLinearLayoutManager;
import com.android.linglan.widget.sortlistview.ClearEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    protected static final int ALL_REQUEST_SUCCESS = 2;
    private int sourcepage = -1;
    private LinearLayout ll_no_network, ll_search;
    private ClearEditText filter_edit;
    private RecyclerView rec_search;
    private SearchAdapter searchAdapter;
    private HotAndHistorySearcherBean hotAndHistorySearcherBean;
    private String[] hotsearch;
    public String[] historysearch;
    private AllSearchListBean allSearchListBean;
    private ArrayList<SearchArticleBean.ArticleClassifyListBean> article;
    private ArrayList<SearchSubjectBean.SubjectClassifyListBean> special;
    private SearchArticleBean searchArticleBean;
    private SearchSubjectBean searchSubjectBean;
    private SearchFMBean searchFMBean;
    private HistorySearcherBean historySearcherBean;
    private HotSearcherBean hotSearcherBean;

    private SearchAllAdapter allArticleAdapter;
    private RecyclerView rec_all_search;
    private Button btn_search;

    private TextView tv_clinical_reference_instruction;
    private String clinicalReference;

    private int page;
    private String key;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ALL_REQUEST_SUCCESS:
                    tv_clinical_reference_instruction.setVisibility(View.GONE);
                    rec_search.setVisibility(View.GONE);
                    rec_all_search.setVisibility(View.VISIBLE);
                    if ((filter_edit.getText().toString().trim()) == null || (filter_edit.getText().toString().trim()).equals("")) {
                        if (key != null && key.length() != 0) {
                            filter_edit.setText(key);
                            filter_edit.setSelection(key.length());
                        }
                    }

                    ll_search.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_SUCCESS:
//                    LogUtil.d("第二次" + data.toString());
                    ll_search.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    ll_search.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        KeyBoardUtils.closeKeybord(filter_edit, this);
    }

    @Override
    protected void setView() {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initView() {
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        tv_clinical_reference_instruction = (TextView) findViewById(R.id.tv_clinical_reference_instruction);
        filter_edit = (ClearEditText) findViewById(R.id.filter_edit);
        btn_search = (Button) findViewById(R.id.btn_search);
        rec_search = (RecyclerView) findViewById(R.id.rec_search);
        rec_all_search = (RecyclerView) findViewById(R.id.rec_all_search);
        rec_all_search.setLayoutManager(new SyLinearLayoutManager(this));
        rec_all_search.setHasFixedSize(true);
        allArticleAdapter = new SearchAllAdapter(this);
        rec_all_search.setAdapter(allArticleAdapter);
        page = 1;
    }

    @Override
    protected void initData() {
        setTitle("搜索", "");
        searchFMBean = new SearchFMBean();
        clinicalReference = getIntent().getStringExtra("clinicalReference");
        sourcepage = (int) getIntent().getExtras().get("searchEdit");
        if (clinicalReference != null && !clinicalReference.equals("")) {
            tv_clinical_reference_instruction.setVisibility(View.VISIBLE);
            rec_search.setVisibility(View.GONE);
            rec_all_search.setVisibility(View.GONE);
        } else {
            tv_clinical_reference_instruction.setVisibility(View.GONE);
            rec_search.setVisibility(View.VISIBLE);
            rec_all_search.setVisibility(View.GONE);
        }
        filter_edit.setHint("请输入要搜索的关键字");
        //热门搜索，历史搜索，填充数据
        getHistoryHotSearchKey();

        rec_search.setLayoutManager(new SyLinearLayoutManager(this));
        rec_search.setHasFixedSize(true);
        searchAdapter = new SearchAdapter(this);
        rec_search.setAdapter(searchAdapter);
        KeyBoardUtils.openKeybord(filter_edit, this);

    }

    @Override
    protected void setListener() {

        ll_no_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(SearchActivity.this, UmengBuriedPointUtil.StudySearchClickSearch);
                KeyBoardUtils.closeKeybord(filter_edit, SearchActivity.this);
                String key = filter_edit.getText().toString().trim();
                if (key == null || key.equals("")) {
                    ToastUtil.show("请输入要查询的内容");
                } else {
//                    getSearchAll(key, page);
                    getNewSearchAll(key, page, sourcepage + "");
                }
            }
        });

        filter_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_edit.setTextIsSelectable(false);
            }
        });

        filter_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String key = filter_edit.getText().toString().trim();
                if(key != null &&!key.equals("")){
                    getNewSearchAll(key, page, sourcepage + "");
                }else {
                    rec_search.setVisibility(View.VISIBLE);
                    rec_all_search.setVisibility(View.GONE);
                }


            }
        });
    }

    //获取全局历史，热门搜索的字段
    private void getHistoryHotSearchKey() {
        NetApi.getHistoryHotSearchKey(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("url=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SearchActivity.this)) {
                    return;
                }
                //解析数据，先填充热门搜索数据
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray hot = data.getJSONArray("hotsearch");
                    JSONArray history = data.getJSONArray("historysearch");

                    if ((hot.toString()) != null && !(hot.toString()).equals("")) {
                        hotSearcherBean = JsonUtil.json2Bean(data.toString(), HotSearcherBean.class);
                        hotsearch = hotSearcherBean.hotsearch;
                    }

                    if ((history.toString()) != null && !(history.toString()).equals("")) {
                        historySearcherBean = JsonUtil.json2Bean(data.toString(), HistorySearcherBean.class);
                        historysearch = historySearcherBean.historysearch;
                    }

                    searchAdapter.upDate(hotsearch, historysearch);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                hotAndHistorySearcherBean = JsonUtil.json2Bean(result, HotAndHistorySearcherBean.class);
//
                handler.sendEmptyMessage(REQUEST_SUCCESS);

            }

            @Override
            public void onFailure(String message) {
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        });
    }

    public void getKey(String key) {
        this.key = key;
        getNewSearchAll(key, page, sourcepage + "");
    }

    //全局搜索
    public void getNewSearchAll(final String key, int page, String sourcepage) {
        NetApi.getNewSearchAll(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getNewSearchAll=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, SearchActivity.this)) {
                    if (HttpCodeJugementUtil.code == 1) {
                        ToastUtil.show("抱歉，暂未搜到匹配的内容");
                    }
                    return;
                }

                allArticleAdapter.update(result, key);
                handler.sendEmptyMessage(ALL_REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                LogUtil.e("getNewSearchAll=" + message);
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        }, key, page + "", sourcepage);
    }


    public class SearchAllAdapter extends RecyclerView.Adapter {
        private Context context;
        private final int VIEW_TYPE_ARTICLE = 0;
        private final int VIEW_TYPE_BOOK = 1;
        private final int VIEW_TYPE_VIDEO = 2;
        private final int VIEW_TYPE_CLNICAL = 3;
        private final int VIEW_TYPE_SUBJECT = 4;
        private final int VIEW_TYPE_FM = 5;
        private final int VIEW_TYPE_ALBUM = 6;       // 0文章 1图书 2视频 3医案 4专题5音频6专辑
        private String keyArticle;
        private String keySpecial;
        private ArrayList<SpecialItem> specialItemList = new ArrayList<SpecialItem>();

        private SubjectSearchAdapter subjectSearchAdapter;
        private ArticleSearchAdapter articleSearchAdapter;
        private FmSearchAdapter fmSearchAdapter;
        private ArrayList<SearchArticleBean.ArticleClassifyListBean> article;
        private ArrayList<SearchSubjectBean.SubjectClassifyListBean> special;
        private ArrayList<SearchFMBean.FmClassifyListBean> fms;
        private String data;
        private JSONArray typeArray = new JSONArray();

        public SearchAllAdapter(Context context) {
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
//            if (viewType == typeArray.length()) {
//                view = createZhongyiSearchView(parent);
//            } else {
                view = createView(parent);
//            }
            return new HomePageViewHolder(view);
        }

        //布局
        private View createView(ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.search_fm_layout, parent, false);
            return view;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((HomePageViewHolder) holder).bindData(typeArray, position);
        }

        @Override
        public int getItemCount() {
            return typeArray.length();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        //插入全部数据
        public void update(String data, String key) {
            this.data = data;
            this.keyArticle = key;
            try {
                JSONObject allData = new JSONObject(data);
                typeArray = (JSONArray) allData.get("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            notifyDataSetChanged();
        }

        class HomePageViewHolder extends RecyclerView.ViewHolder implements
                View.OnClickListener {
            private View rootView;
            private TextView tv_title_search;
            private Intent intent;
            private RecyclerView rec_item;
            private LinearLayout ll_zhongyi_search;
            private RelativeLayout rl_more_search;
            int itemType = 0;

            public HomePageViewHolder(View rootView) {
                super(rootView);
                initView(rootView);
            }

            private void initView(View rootView) {
                this.rootView = rootView;
                tv_title_search = (TextView) rootView.findViewById(R.id.tv_title_search);
                rec_item = (RecyclerView) rootView.findViewById(R.id.rec_item);
                ll_zhongyi_search = (LinearLayout) rootView.findViewById(R.id.ll_zhongyi_search);
                rl_more_search = (RelativeLayout) rootView.findViewById(R.id.rl_more_search);
            }

            public void bindData(JSONArray specialItem, int index) {
                LogUtil.e("typeArray.length() = " + typeArray.length());
                LogUtil.e("index = " + index);

                if (index != typeArray.length() - 1) {
                    ll_zhongyi_search.setVisibility(View.GONE);
                } else {
                    ll_zhongyi_search.setVisibility(View.VISIBLE);
                    ll_zhongyi_search.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, TCMSearchRequestResultActivity.class);
                            intent.putExtra("key",keyArticle);
                            startActivity(intent);
                        }
                    });
                }

                    JSONObject typeObject = new JSONObject();
                    JSONArray itemArray = new JSONArray();
                    try {

                        typeObject = (JSONObject) specialItem.get(index);
                        if (typeObject.get("listtitle") != null && tv_title_search != null) {
                            tv_title_search.setText((String) typeObject.get("listtitle"));
                        }

                        itemType = Integer.parseInt((String) typeObject.get("type"));
                        itemArray = typeObject.getJSONArray("list");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rec_item.setLayoutManager(new SyLinearLayoutManager(context));
                    rec_item.setHasFixedSize(true);

                    switch (itemType) {
                        case VIEW_TYPE_ARTICLE:
                            if ((itemArray.toString()) != null && !(itemArray.toString()).equals("")) {
                                searchArticleBean = JsonUtil.json2Bean(typeObject.toString(), SearchArticleBean.class);
                                article = searchArticleBean.list;
                            }
                            articleSearchAdapter = new ArticleSearchAdapter(context);
                            rec_item.setAdapter(articleSearchAdapter);
                            articleSearchAdapter.update(article);

                            rl_more_search.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent moreIntent = new Intent(context, SearchMoreArticleActivity.class);
                                    moreIntent.putExtra("moreArticle", "moreArticle");
                                    moreIntent.putExtra("key", keyArticle);
                                    context.startActivity(moreIntent);
                                }
                            });
                            break;
                        case VIEW_TYPE_SUBJECT:
                            if ((itemArray.toString()) != null && !(itemArray.toString()).equals("")) {
                                searchSubjectBean = JsonUtil.json2Bean(typeObject.toString(), SearchSubjectBean.class);
                                special = searchSubjectBean.list;
                            }
                            subjectSearchAdapter = new SubjectSearchAdapter(context);
                            rec_item.setAdapter(subjectSearchAdapter);
                            subjectSearchAdapter.update(special);

                            rl_more_search.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent moreIntent = new Intent(context, SearchMoreSubjectActivity.class);
                                    moreIntent.putExtra("moreArticle", "moreArticle");
                                    moreIntent.putExtra("key", keyArticle);
                                    context.startActivity(moreIntent);
                                }
                            });
                            break;

                        case VIEW_TYPE_FM:
                            if ((itemArray.toString()) != null && !(itemArray.toString()).equals("")) {
                                searchFMBean = JsonUtil.json2Bean(typeObject.toString(), SearchFMBean.class);
                                fms = searchFMBean.list;
                            }
                            fmSearchAdapter = new FmSearchAdapter(context);
                            rec_item.setAdapter(fmSearchAdapter);
//
                            fmSearchAdapter.update(fms);
//
                            rl_more_search.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent moreIntent = new Intent(context, SearchMoreFmActivity.class);
                                    moreIntent.putExtra("moreArticle", "moreArticle");
                                    moreIntent.putExtra("key", keyArticle);
                                    context.startActivity(moreIntent);
                                }
                            });
                            break;

                        default:
                            break;
                    }

            }

            @Override
            public void onClick(View v) {
            }
        }

        class SpecialItem {
            int itemType;
            ArrayList<SearchArticleBean.ArticleClassifyListBean> ArticlesData;
            ArrayList<SearchSubjectBean.SubjectClassifyListBean> special;

            SpecialItem(int itemType) {
                this.itemType = itemType;
            }
        }
    }
}
