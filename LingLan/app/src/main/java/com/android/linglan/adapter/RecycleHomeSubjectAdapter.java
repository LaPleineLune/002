package com.android.linglan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.ui.study.SubjectDetailsActivity;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.SharedPreferencesUtil;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class RecycleHomeSubjectAdapter extends
        RecyclerView.Adapter {
    private Context context;
    private Intent intent;
    public ArrayList<RecommendSubjects.RecommendSubject> recommendSubject;

    public RecycleHomeSubjectAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_homepage_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SubjectViewHolder) holder).bindData(recommendSubject.get(position));
    }

    @Override
    public int getItemCount() {
        return this.recommendSubject != null ? this.recommendSubject.size() : 0;
    }

    public void updateAdapter(ArrayList<RecommendSubjects.RecommendSubject> recommendSubject) {
        this.recommendSubject = recommendSubject;
        this.notifyDataSetChanged();
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;

        private ImageView logo;
        private TextView title;
        private TextView description;
        private TextView subject_new;
        private TextView date;
        private RecommendSubjects.RecommendSubject subject;

        public SubjectViewHolder(View rootView) {
            super(rootView);
            initView(rootView);

        }

        private void initView(View rootView) {
            this.rootView = rootView;
            logo = (ImageView) rootView.findViewById(R.id.img_homepage_subject_logo);
            title = (TextView) rootView.findViewById(R.id.tv_homepage_subject_title);
            description = (TextView) rootView.findViewById(R.id.tv_homepage_subject_description);
            subject_new = (TextView) rootView.findViewById(R.id.tv_homepage_subject_new);
            date = (TextView) rootView.findViewById(R.id.tv_homepage_subject_date);
        }

        public void bindData(final RecommendSubjects.RecommendSubject subject) {
            this.subject = subject;
            try {
                ImageUtil.loadImageAsync(logo, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, subject.logo, null);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
//            ImageUtil.loadImageAsync(logo, subject.logo, R.drawable.default_image);
            title.setText(subject.specialname);// getItem(position).specialname
            String oldSpecialid = SharedPreferencesUtil.getString("specialid" + subject.specialid, null);
            if (oldSpecialid != null && oldSpecialid.equals(subject.specialid)) {
                title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
            } else {
                title.setTextColor(ContextCompat.getColor(context, R.color.text_title_color));
            }
            description.setText(subject.content_title);
            date.setText(subject.updatetime);
            if (subject.isnew != null && subject.isnew.equals("1")) {
                subject_new.setVisibility(View.VISIBLE);
            } else {
                subject_new.setVisibility(View.GONE);
            }

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferencesUtil.saveString("specialid" + subject.specialid, subject.specialid);
                    title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                    intent = new Intent();
                    intent.setClass(context, SubjectDetailsActivity.class);
                    intent.putExtra("specialid", subject.specialid);
                    intent.putExtra("specialname", subject.specialname);
                    intent.putExtra("photo", subject.photo);
                    intent.putExtra("logo", subject.logo);
                    intent.putExtra("description", subject.description);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
