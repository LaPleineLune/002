package com.android.linglan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.http.bean.SearchArticleBean;
import com.android.linglan.http.bean.SearchSubjectBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.study.SearchMoreFmActivity;
import com.android.linglan.widget.SyLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class SearchAllAdapter extends RecyclerView.Adapter {
    private Context context;
    static final int VIEW_TYPE_SUBJECT = 0;
    static final int VIEW_TYPE_ARTICLE = 1;
    static final int VIEW_TYPE_FM = 2;
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

            case VIEW_TYPE_SUBJECT:
                view = createSubjectView(parent);
                break;
            case VIEW_TYPE_ARTICLE:
                view = createArticleView(parent);
                break;
            case VIEW_TYPE_FM:
                view = createFmView(parent);
                break;
            default:
                break;
        }

        return new HomePageViewHolder(view);
    }

    //专题布局
    private View createSubjectView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_subject_layout, parent, false);
        return view;
    }


    //推荐文章布局
    private View createArticleView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_article_layout, parent, false);

        return view;
    }


    //推荐音频布局
    private View createFmView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_fm_layout, parent, false);

        return view;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((HomePageViewHolder) holder).bindData(specialItemList.get(position), ++position);
    }

    @Override
    public int getItemCount() {
        return specialItemList.size();
//        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return specialItemList.get(position).itemType;
    }

    //插入推荐专题数据
    public void insertSubjectData(ArrayList<SearchSubjectBean.SubjectClassifyListBean> special) {
        SpecialItem specialItem;
        this.special = special;
        //如果是电台页面转来或者是文章页面转来
        if (!specialItemList.isEmpty()) {
            specialItem = specialItemList.get(VIEW_TYPE_SUBJECT);
            specialItemList.remove(specialItem);
        }
        specialItem = new SpecialItem(VIEW_TYPE_SUBJECT);
        specialItem.special = special;
        specialItemList.add(VIEW_TYPE_SUBJECT, specialItem);
        notifyDataSetChanged();
    }


    //插入推荐文章数据
    public void insertArticlesData(ArrayList<SearchArticleBean.ArticleClassifyListBean> article) {
        SpecialItem specialItem;
        this.article = article;
        if ((specialItemList.size() >= 2)) {
//            specialItemList.clear();
            specialItem = specialItemList.get(VIEW_TYPE_ARTICLE);
            specialItemList.remove(specialItem);
        }

        specialItem = new SpecialItem(VIEW_TYPE_ARTICLE);
        specialItem.ArticlesData = article;
        specialItemList.add(VIEW_TYPE_ARTICLE, specialItem);

        specialItem = new SpecialItem(VIEW_TYPE_FM);
        specialItemList.add(VIEW_TYPE_FM, specialItem);
        notifyDataSetChanged();
    }



    //更新文章数据
    public void updateArticle(ArrayList<SearchArticleBean.ArticleClassifyListBean> searchArticleBean, String keyArticle) {
        this.keyArticle = keyArticle;
        insertArticlesData(searchArticleBean);

//        insertSubjectData(allSearchListBean.data.special);

    }

    //更新文章数据
    public void updateSpecial(SearchSubjectBean searchSubjectBean) {
        insertSubjectData(searchSubjectBean.list);
        this.keyArticle = keyArticle;
//        insertSubjectData(allSearchListBean.data.special);

    }


    class HomePageViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        //        private LinearLayout ll_more_subject;
//        private LinearLayout ll_more_article;
        private LinearLayout ll_search_subject;
        private TextView tv_more_search_article;
        private TextView tv_more_search_subject;
        private Intent intent;
        private RecyclerView rec_article;
        RecyclerView rec_subject;

        public HomePageViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_more_search_article = (TextView) rootView.findViewById(R.id.tv_more_search_article);
            tv_more_search_subject = (TextView) rootView.findViewById(R.id.tv_more_search_subject);
            ll_search_subject = (LinearLayout) rootView.findViewById(R.id.ll_search_subject);
            rec_article = (RecyclerView) rootView.findViewById(R.id.rec_article);
            rec_subject = (RecyclerView) rootView.findViewById(R.id.rec_subject);
        }

        public void bindData(SpecialItem specialItem, int index) {
            switch (specialItem.itemType) {

                case VIEW_TYPE_SUBJECT:
                    ViewGroup.LayoutParams lp;
                    if (special == null || (special.size() == 0)) {
                        lp = ll_search_subject.getLayoutParams();
                        lp.height = 1;
                        ll_search_subject.setLayoutParams(lp);
                        ll_search_subject.setVisibility(View.GONE);
                    } else {
                        lp = ll_search_subject.getLayoutParams();
                        lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
                        ll_search_subject.setLayoutParams(lp);
                        ll_search_subject.setVisibility(View.VISIBLE);
                        rec_subject.setLayoutManager(new SyLinearLayoutManager(context));
                        rec_subject.setHasFixedSize(true);
                        subjectSearchAdapter = new SubjectSearchAdapter(context);
                        rec_subject.setAdapter(subjectSearchAdapter);

                        subjectSearchAdapter.update(specialItem.special);
                    }

                    tv_more_search_subject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent moreIntent = new Intent(context, SearchMoreFmActivity.class);
                            moreIntent.putExtra("moreSubject", "moreSubject");
                            moreIntent.putExtra("key", keyArticle);
                            context.startActivity(moreIntent);
                        }
                    });
                    break;
                case VIEW_TYPE_ARTICLE:
                    rec_article.setLayoutManager(new SyLinearLayoutManager(context));
                    rec_article.setHasFixedSize(true);
                    articleSearchAdapter = new ArticleSearchAdapter(context);
                    rec_article.setAdapter(articleSearchAdapter);

                    articleSearchAdapter.update(specialItem.ArticlesData);

                    tv_more_search_article.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent moreIntent = new Intent(context, SearchMoreFmActivity.class);
                            moreIntent.putExtra("moreArticle", "moreArticle");
                            moreIntent.putExtra("key", keyArticle);
                            context.startActivity(moreIntent);
                        }
                    });
                    break;

                case VIEW_TYPE_FM:
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
