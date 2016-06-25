package com.android.linglan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.SearchSubjectBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.study.SubjectDetailsActivity;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class SubjectSearchAdapter extends
        RecyclerView.Adapter{
    private Context context;
    public ArrayList<SearchSubjectBean.SubjectClassifyListBean> RecommendSubjects;

    public void update(ArrayList<SearchSubjectBean.SubjectClassifyListBean> RecommendSubjects) {
        this.RecommendSubjects = RecommendSubjects;
        notifyDataSetChanged();
    }
    public SubjectSearchAdapter(Context context) {
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_search_more_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ( (SubjectViewHolder)holder).bindData(RecommendSubjects.get(position));
    }

    @Override
    public int getItemCount() {
//        if(RecommendSubjects == null){
//            return 0;
//        }else if(RecommendSubjects.size() < 2){
//            return RecommendSubjects.size();
//        }else{
//            return 1;
//        }
        return RecommendSubjects== null ? 0:RecommendSubjects.size();
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        private ImageView logo;
        private TextView title;
        private TextView description;
//        private TextView date;

//        private TextView tv_subject_title;
        private View rootView;
        public SubjectViewHolder(View rootView) {
            super(rootView);
            init(rootView);
        }

        public void init(View rootView){
            this.rootView = rootView;
//            tv_subject_title = (TextView)rootView.findViewById(R.id.tv_subject_title);
            logo = (ImageView) rootView.findViewById(R.id.img_homepage_subject_logo);
            title = (TextView) rootView.findViewById(R.id.tv_homepage_subject_title);
            description = (TextView) rootView.findViewById(R.id.tv_homepage_subject_description);
//            date = (TextView) rootView.findViewById(R.id.tv_homepage_subject_date);

        }
        public void bindData(final SearchSubjectBean.SubjectClassifyListBean recommendSubjects) {
//            this.recommendSubjects = recommendSubjects;
//            tv_subject_title.setText(recommendSubjects.specialname);
            try {
//                ImageUtil.loadImageAsync(logo, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, recommendSubjects.logo, null);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            title.setText(recommendSubjects.specialname);
            description.setText(recommendSubjects.content_title);
//            date.setText(recommendSubjects.updatetime);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(context, UmengBuriedPointUtil.StudySubjectContent);
                    Intent intent = new Intent(context, SubjectDetailsActivity.class);
                    intent.putExtra("specialid", recommendSubjects.specialid);
                    intent.putExtra("specialname", recommendSubjects.specialname);
                    intent.putExtra("photo", recommendSubjects.photo);
                    intent.putExtra("logo", recommendSubjects.logo);
                    intent.putExtra("description", recommendSubjects.description);
                    context.startActivity(intent);
                }
            });

//            ll_item_article_addtime.setText(recommendArticle.addtime);
//            if (recommendArticle.photo != null && recommendArticle.photo.equals("")) {
////                iv_item_article_image.setVisibility(View.GONE);
//            }
        }
        @Override
        public void onClick(View v) {

        }
    }
}
