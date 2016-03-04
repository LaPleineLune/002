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
import android.widget.Toast;

import com.android.linglan.http.bean.AllSearchListBean;
import com.android.linglan.http.bean.RecommendArticles;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.http.bean.SearchArticleBean;
import com.android.linglan.http.bean.SearchSubjectBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.StartActivity;
import com.android.linglan.ui.homepage.AllArticleActivity;
import com.android.linglan.ui.homepage.AllSubjectTestActivity;
import com.android.linglan.ui.homepage.SearchMoreActivity;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.SyLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class SearchAllAdapter extends
        RecyclerView.Adapter {
    private Context context;
    static final int VIEW_TYPE_ARTICLE = 0;
    static final int VIEW_TYPE_SUBJECT = 1;
    private String keyArticle;
    private String keySpecial;
    ArrayList<SpecialItem> specialItemList = new ArrayList<SpecialItem>();

    private SubjectSearchAdapter subjectSearchAdapter;
    private ArticleSearchAdapter articleSearchAdapter;
    private ArrayList<SearchArticleBean.ArticleClassifyListBean> article;
    private ArrayList<SearchSubjectBean.SubjectClassifyListBean> special;

    public SearchAllAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case VIEW_TYPE_ARTICLE:
                view = createArticleView(parent);
                break;
            case VIEW_TYPE_SUBJECT:
                view = createSubjectView(parent);
                break;
            default:
                break;
        }

        return new HomePageViewHolder(view);
    }
    //推荐文章布局
    private View createArticleView(ViewGroup parent) {
        View view =
                LayoutInflater.from(context).inflate(R.layout.search_article_layout, parent, false);
        RecyclerView rec_article = (RecyclerView) view.findViewById(R.id.rec_article);
        rec_article.setLayoutManager(new SyLinearLayoutManager(context));
        rec_article.setHasFixedSize(true);
        articleSearchAdapter = new ArticleSearchAdapter(context);
        rec_article.setAdapter(articleSearchAdapter);
        return view;
    }

    //专题布局
    private View createSubjectView(ViewGroup parent) {
        View view =
                LayoutInflater.from(context).inflate(R.layout.search_subject_layout, parent, false);

        RecyclerView rec_subject = (RecyclerView) view.findViewById(R.id.rec_subject);
        rec_subject.setLayoutManager(new SyLinearLayoutManager(context));
        rec_subject.setHasFixedSize(true);
        subjectSearchAdapter = new SubjectSearchAdapter(context);
        rec_subject.setAdapter(subjectSearchAdapter);
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

    //插入推荐文章数据
    public void insertArticlesData(ArrayList<SearchArticleBean.ArticleClassifyListBean> article) {
        SpecialItem specialItem;
        if ((!specialItemList.isEmpty())){
//            specialItemList.clear();
            specialItem = specialItemList.get(VIEW_TYPE_ARTICLE);
            specialItemList.remove(specialItem);
        }
        specialItem = new SpecialItem(VIEW_TYPE_ARTICLE);
        specialItem.ArticlesData = article;
        this.article = article;
        specialItemList.add(VIEW_TYPE_ARTICLE, specialItem);
        notifyDataSetChanged();
    }

    //插入推荐专题数据
    public void insertSubjectData(ArrayList<SearchSubjectBean.SubjectClassifyListBean> special) {
        SpecialItem specialItem;
        this.special = special;
        if (specialItemList.size() >= 2) {
            specialItem = specialItemList.get(VIEW_TYPE_SUBJECT);
            specialItemList.remove(specialItem);
        }
        specialItem = new SpecialItem(VIEW_TYPE_SUBJECT);
        specialItem.special = special;
        specialItemList.add(VIEW_TYPE_SUBJECT, specialItem);
        notifyDataSetChanged();
    }

    //更新文章数据
    public void updateArticle(SearchArticleBean searchArticleBean,String keyArticle){
        insertArticlesData(searchArticleBean.article);
        this.keyArticle = keyArticle;
//        insertSubjectData(allSearchListBean.data.special);

    }
    //更新文章数据
    public void updateSpecial(SearchSubjectBean searchSubjectBean){
        insertSubjectData(searchSubjectBean.special);
        this.keyArticle = keyArticle;
//        insertSubjectData(allSearchListBean.data.special);

    }
//    public void update(ArrayList<AllSearchListBean.SubjectClassifyListBean> SubjectsData,ArrayList<AllSearchListBean.ArticleClassifyListBean> ArticlesData) {
//        SpecialItem specialItem;
//        if (specialItemList.size() >= 2) {
//            specialItemList.clear();
//            insertSubjectData(SubjectsData);
//            insertArticlesData(ArticlesData);
//        }

//        }

    class HomePageViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
//        private LinearLayout ll_more_subject;
//        private LinearLayout ll_more_article;
        private LinearLayout ll_search_subject;
        private TextView tv_more_search_article;
        private TextView tv_more_search_subject;
        private Intent intent;

        public HomePageViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_more_search_article = (TextView) rootView.findViewById(R.id.tv_more_search_article);
            tv_more_search_subject = (TextView) rootView.findViewById(R.id.tv_more_search_subject);
            ll_search_subject = (LinearLayout) rootView.findViewById(R.id.ll_search_subject);
        }

        public void bindData(SpecialItem specialItem,int index) {
            switch (specialItem.itemType) {
                case VIEW_TYPE_ARTICLE:
                    articleSearchAdapter.update(specialItem.ArticlesData);

                    tv_more_search_article.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent moreIntent = new Intent(context, SearchMoreActivity.class);
                            moreIntent.putExtra("moreArticle", "moreArticle");
                            moreIntent.putExtra("key", keyArticle);
                            context.startActivity(moreIntent);
                        }
                    });

                    break;
                case VIEW_TYPE_SUBJECT:
                    subjectSearchAdapter.update(specialItem.special);

                    if(special == null || (special.size() == 0)){
                        ll_search_subject.setVisibility(View.GONE);
                    }else{
                        ll_search_subject.setVisibility(View.VISIBLE);
                    }

                    tv_more_search_subject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent moreIntent = new Intent(context, SearchMoreActivity.class);
                            moreIntent.putExtra("moreSubject", "moreSubject");
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

    static class SpecialItem {
        int itemType;
        ArrayList<SearchArticleBean.ArticleClassifyListBean> ArticlesData;
        ArrayList<SearchSubjectBean.SubjectClassifyListBean> special;

        SpecialItem(int itemType) {
            this.itemType = itemType;
        }
    }
}