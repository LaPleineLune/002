package com.android.linglan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.linglan.http.bean.HomepageRecommendBean;
import com.android.linglan.ui.R;
import com.android.linglan.widget.SyLinearLayoutManager;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class RecycleStudyAdapter extends
        RecyclerView.Adapter {

    static final int VIEW_STUDY_GIRDVIEW = 0;
    static final int VIEW_STUDY_RECOMMEND = 1;
    private RecycleHomeRecommendAdapter adapter;

    private Context context;
    public ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> homepageRecommend;

    public RecycleStudyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case VIEW_STUDY_GIRDVIEW:
                view = createStudyGridView(parent);
                break;
            case VIEW_STUDY_RECOMMEND:
                view = createStudyRecommendView(parent);
                break;
            default:
                break;
        }
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ArticleViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void updateAdapter(ArrayList<HomepageRecommendBean.HomepageRecommendBeanData> homepageRecommend) {
        this.homepageRecommend = homepageRecommend;
        this.notifyDataSetChanged();
    }

    // 学习的头部
    private View createStudyGridView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_study_gridview, parent, false);
        return view;
    }

    // 学习的热点推荐
    private View createStudyRecommendView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_study_recommendview, parent, false);
        return view;
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private RecyclerView rec_homepage_recommend;

        public ArticleViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            rec_homepage_recommend = (RecyclerView) rootView.findViewById(R.id.rec_homepage_recommend);
        }

        public void bindData( final int position) {
//            this.recommendArticle = recommendArticle;

            switch (position) {
                case VIEW_STUDY_GIRDVIEW:

                    break;
                case VIEW_STUDY_RECOMMEND:
                    adapter = new RecycleHomeRecommendAdapter(context);
                    rec_homepage_recommend.setLayoutManager(new SyLinearLayoutManager(context));
                    rec_homepage_recommend.setHasFixedSize(true);
                    rec_homepage_recommend.setAdapter(adapter);
                    adapter.updateAdapter(homepageRecommend, false);
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
