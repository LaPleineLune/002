package com.android.linglan.adapter;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.R;
import com.android.linglan.ui.study.SearchActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.SyLinearLayoutManager;
import com.android.linglan.widget.flowlayout.TagAdapter;
import com.android.linglan.widget.flowlayout.TagFlowLayout;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class SearchAdapter extends RecyclerView.Adapter {
    private SearchActivity context;
    static final int VIEW_TYPE_SUBJECT = 0;
    static final int VIEW_TYPE_ARTICLE = 1;

    private String[] hotsearch = new String[]{};
    public String[] historysearch = new String[]{};

    private NewHistorySearchAdapter historySearchAdapter;

    public SearchAdapter(SearchActivity context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case VIEW_TYPE_SUBJECT:
                view = createHotSearchView(parent);
                break;
            case VIEW_TYPE_ARTICLE:
                view = createHistorySearchView(parent);
                break;
            default:
                break;
        }

        return new SearchViewHolder(view);
    }

    //热门搜索
    private View createHotSearchView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hot_search, parent, false);
        return view;
    }

    //历史搜索
    private View createHistorySearchView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_search, parent, false);
        return view;
    }

    public void upDate(String[] hotsearch,String[] historysearch) {
        this.hotsearch = hotsearch;
        this.historysearch = historysearch;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SearchViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class SpecialItem {
        int itemType;

        SpecialItem(int itemType) {
            this.itemType = itemType;
        }
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private TagFlowLayout mFlowLayout;
        private RelativeLayout rl_history_search;
        private LinearLayout ll_no_history_search,ll_history_search;
        private View rootView;

        public SearchViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            initView(rootView);
        }

        private void initView(View rootView) {
            mFlowLayout = (TagFlowLayout) rootView.findViewById(R.id.id_flowlayout);
            ll_no_history_search = (LinearLayout) rootView.findViewById(R.id.ll_no_history_search);
            ll_history_search = (LinearLayout) rootView.findViewById(R.id.ll_history_search);
            rl_history_search = (RelativeLayout) rootView.findViewById(R.id.rl_history_search);
        }

        TextView tv;
        private void bindData(int index) {
            switch (index) {
                case VIEW_TYPE_SUBJECT:
                    final LayoutInflater mInflater = LayoutInflater.from(context);
                    mFlowLayout.setAdapter(new TagAdapter<String>(hotsearch) {

                        @Override
                        public View getView(com.android.linglan.widget.flowlayout.FlowLayout parent, int position, String s) {
                            LinearLayout ll = (LinearLayout) mInflater.inflate(R.layout.search_tv,
                                    mFlowLayout, false);
                            tv = (TextView) ll.findViewById(R.id.tv_hot_search_context);
                            tv.setText(s);
                            tv.setTextSize(16);
                            Resources resource = (Resources) context.getResources();
                            ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.no_text_color_fragment_title);
                            tv.setTextColor(csl);
                            return ll;
                        }
                    });

                    mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int position, com.android.linglan.widget.flowlayout.FlowLayout parent) {
//                            Toast.makeText(context, hotsearch[position], Toast.LENGTH_SHORT).show();
                            context.getKey(hotsearch[position]);
                            view.setBackgroundResource(R.drawable.bg_bottom_textview);

                            return true;
                        }
                    });
                    break;
                case VIEW_TYPE_ARTICLE:

                    RecyclerView rec_history_search = (RecyclerView) rootView.findViewById(R.id.rec_history_search);
                    rec_history_search.setLayoutManager(new SyLinearLayoutManager(context));
                    rec_history_search.setHasFixedSize(true);

                    historySearchAdapter = new NewHistorySearchAdapter(context,historysearch);
                    rec_history_search.setAdapter(historySearchAdapter);

                    rootView.findViewById(R.id.tv_delete_history_search).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NetApi.clearHistory(new PasserbyClient.HttpCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result,context)) {
                                        return;
                                    }
                                    ll_no_history_search.setVisibility(View.VISIBLE);
                                    rl_history_search.setVisibility(View.GONE);
                                    ToastUtil.show("历史记录清除成功");
                                }

                                @Override
                                public void onFailure(String message) {

                                }
                            });

                        }
                    });

                    if (SharedPreferencesUtil.getString("token", null) == null) {
                        ll_history_search.setVisibility(View.GONE);
                    }else{
                        if(historysearch ==null || historysearch.length == 0){
                            ll_no_history_search.setVisibility(View.VISIBLE);
                            rl_history_search.setVisibility(View.GONE);
                        }else{
                            ll_no_history_search.setVisibility(View.GONE);
                            rl_history_search.setVisibility(View.VISIBLE);
                            historySearchAdapter.updateAdapter(historysearch);
                        }
                    }
                    break;
                default:
                    break;
            }


        }

        @Override
        public void onClick(View v) {

        }
    }
}