package com.android.linglan.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.linglan.ui.R;
import com.android.linglan.ui.study.SearchActivity;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class NewHistorySearchAdapter extends RecyclerView.Adapter{
    private SearchActivity context;
    private Intent intent;
//    public ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> homepageRecommend;
    public String[] historysearch = new String[]{};

    public NewHistorySearchAdapter(SearchActivity context,String[] historysearch) {
        this.context = context;
        this.historysearch = historysearch;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_history_search_context, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ( (ArticleViewHolder)holder).bindData(historysearch[position]);
    }

    @Override
    public int getItemCount() {
        return this.historysearch != null ? this.historysearch.length : 0 ;
//        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateAdapter(String[] historysearch){
        this.historysearch = historysearch;
        this.notifyDataSetChanged();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView tv_item_history_search;
        public ArticleViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_item_history_search = (TextView) rootView.findViewById(R.id.tv_item_history_search);

        }
        public void bindData(final String key) {
            tv_item_history_search.setText(key);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.getKey(key);
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }

}
