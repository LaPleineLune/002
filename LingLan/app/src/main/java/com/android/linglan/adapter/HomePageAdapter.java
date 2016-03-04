package com.android.linglan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.http.bean.RecommendArticles;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.ui.homepage.AllArticleActivity;
import com.android.linglan.ui.homepage.AllSubjectActivity;
import com.android.linglan.ui.homepage.AllSubjectTestActivity;
import com.android.linglan.widget.SyLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class HomePageAdapter extends
        RecyclerView.Adapter {
    private Context context;
    static final int VIEW_TYPE_SUBJECT = 0;
    static final int VIEW_TYPE_ARTICLE = 1;
    ArrayList<SpecialItem> specialItemList = new ArrayList<SpecialItem>();

    private SubjectAdapter subjectAdapter;
    private ArticleAdapter articleAdapter;
    private ArrayList<RecommendArticles.RecommendArticle> recommendArticles;

    public HomePageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case VIEW_TYPE_SUBJECT:
                view = createSubjectView(parent);
                break;
            case VIEW_TYPE_ARTICLE:
                view = createArticleView(parent);
                break;
            default:
                break;
        }

        return new HomePageViewHolder(view);
    }

    //专题布局
    private View createSubjectView(ViewGroup parent) {
        View view =
                LayoutInflater.from(context).inflate(R.layout.special_item_subject_layout, parent, false);

        RecyclerView rec_subject = (RecyclerView) view.findViewById(R.id.rec_subject);
        rec_subject.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        rec_subject.setHasFixedSize(true);
        subjectAdapter = new SubjectAdapter(context);
        rec_subject.setAdapter(subjectAdapter);
        return view;
    }

    //推荐文章布局
    private View createArticleView(ViewGroup parent) {
        View view =
                LayoutInflater.from(context).inflate(R.layout.special_item_article_layout, parent, false);
        RecyclerView rec_article = (RecyclerView) view.findViewById(R.id.rec_article);
        rec_article.setLayoutManager(new SyLinearLayoutManager(context));
        rec_article.setHasFixedSize(true);
        articleAdapter = new ArticleAdapter(context);
        rec_article.setAdapter(articleAdapter);
        return view;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((HomePageViewHolder) holder).bindData(specialItemList.get(position), ++position);
    }

    @Override
    public int getItemCount() {
        return specialItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return specialItemList.get(position).itemType;
    }

    //插入推荐专题数据
    public void insertSubjectData(ArrayList<RecommendSubjects.RecommendSubject> SubjectsData) {
        SpecialItem specialItem;
        if (!specialItemList.isEmpty()) {
            specialItem = specialItemList.get(VIEW_TYPE_SUBJECT);
            specialItemList.remove(specialItem);
        }
        specialItem = new SpecialItem(VIEW_TYPE_SUBJECT);
        specialItem.SubjectsData = SubjectsData;
        specialItemList.add(VIEW_TYPE_SUBJECT, specialItem);
        notifyDataSetChanged();
    }
    //插入推荐文章数据
    public void insertArticlesData(ArrayList<RecommendArticles.RecommendArticle> ArticlesData) {
        SpecialItem specialItem;
        if (specialItemList.size() >= 2) {
//            specialItemList.clear();
            specialItem = specialItemList.get(VIEW_TYPE_ARTICLE);
            specialItemList.remove(specialItem);
        }
        specialItem = new SpecialItem(VIEW_TYPE_ARTICLE);
        specialItem.ArticlesData = ArticlesData;
        recommendArticles = ArticlesData;
        specialItemList.add(VIEW_TYPE_ARTICLE, specialItem);
        notifyDataSetChanged();
    }
    //更新数据
    public void update(ArrayList<RecommendSubjects.RecommendSubject> SubjectsData,ArrayList<RecommendArticles.RecommendArticle> ArticlesData) {
        SpecialItem specialItem;
        if (specialItemList.size() >= 2) {
            specialItemList.clear();
            insertSubjectData(SubjectsData);
            insertArticlesData(ArticlesData);
        }

        }

    class HomePageViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private LinearLayout ll_more_subject;
        private LinearLayout ll_more_article;
        private Intent intent;

        public HomePageViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            initView(rootView);
        }

        private void initView(View rootView) {
            ll_more_subject = (LinearLayout) rootView.findViewById(R.id.ll_more_subject);
            ll_more_article = (LinearLayout) rootView.findViewById(R.id.ll_more_article);

        }

        public void bindData(SpecialItem specialItem,int index) {
            switch (specialItem.itemType) {
                case VIEW_TYPE_SUBJECT:
                    subjectAdapter.update(specialItem.SubjectsData);
                    ll_more_subject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            intent = new Intent(context, AllSubjectActivity.class);
                            intent = new Intent(context, AllSubjectTestActivity.class);
                            context.startActivity(intent);
                        }
                    });
                    break;
                case VIEW_TYPE_ARTICLE:
                    articleAdapter.update(specialItem.ArticlesData);
                    ll_more_article.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, AllArticleActivity.class);
                            context.startActivity(intent);
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

    static class SpecialItem {
        int itemType;
        ArrayList<RecommendArticles.RecommendArticle> ArticlesData;
        ArrayList<RecommendSubjects.RecommendSubject> SubjectsData;

        SpecialItem(int itemType) {
            this.itemType = itemType;
        }
    }
}
