package com.android.linglan.adapter.fm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.Constants;
import com.android.linglan.http.bean.RadioListBean;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.ui.fm.RadioSpecialActivity;
import com.android.linglan.ui.study.SubjectDetailsActivity;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/6/20 0020.
 * 电台列表
 */
public class RadioAdapter extends RecyclerView.Adapter {
    private Context context;
    private Intent intent;
    public ArrayList<RadioListBean.RadioListData> radioListData;

    public RadioAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_radio, parent, false);
        return new RadioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RadioViewHolder) holder).bindData(radioListData.get(position));
    }

    @Override
    public int getItemCount() {
        return this.radioListData != null ? this.radioListData.size() : 0;
    }

    public void updateAdapter(ArrayList<RadioListBean.RadioListData> radioListData) {
        this.radioListData = radioListData;
        this.notifyDataSetChanged();
    }

    class RadioViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;

        private ImageView logo;
        private TextView title;
        private TextView description;
//        private TextView subject_new;
        private TextView play_num;
        private TextView duration;
        private TextView update;
//        private RecommendSubjects.RecommendSubject subject;
        private RadioListBean.RadioListData data;

        public RadioViewHolder(View rootView) {
            super(rootView);
            initView(rootView);

        }

        private void initView(View rootView) {
            this.rootView = rootView;
            logo = (ImageView) rootView.findViewById(R.id.img_radio_logo);
            title = (TextView) rootView.findViewById(R.id.tv_radio_title);
            description = (TextView) rootView.findViewById(R.id.tv_radio_description);
//            subject_new = (TextView) rootView.findViewById(R.id.tv_homepage_subject_new);
            play_num = (TextView) rootView.findViewById(R.id.tv_radio_play_num);
            duration = (TextView) rootView.findViewById(R.id.tv_radio_duration);
            update = (TextView) rootView.findViewById(R.id.tv_radio_update);
        }

        public void bindData(final RadioListBean.RadioListData radioListData) {
            this.data = radioListData;
            if (data.type.equals(Constants.RADIO_SPECIAL)) {
                try {
                    ImageUtil.loadImageAsync(logo, R.dimen.dp84, R.dimen.dp68, R.drawable.default_image, data.logo, null);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                title.setText(data.albumname);
                String oldAlbumid = SharedPreferencesUtil.getString("albumid" + data.albumid, null);
                if (oldAlbumid != null && oldAlbumid.equals(data.albumid)) {
                    title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                } else {
                    title.setTextColor(ContextCompat.getColor(context, R.color.text_title_color));
                }
                description.setText(data.description);
                play_num.setText(data.count_play);
                duration.setText(data.count_chapter);
//                update.setText(data.updatetime);
            }


            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent();
                    if (data.type.equals(Constants.RADIO_SPECIAL)) {
                        SharedPreferencesUtil.saveString("albumid" + data.albumid, data.albumid);
                        title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
//                        MobclickAgent.onEvent(context, UmengBuriedPointUtil.StudySubjectContent);
                        intent.setClass(context, RadioSpecialActivity.class);
                        intent.putExtra("albumid", data.albumid);
                        intent.putExtra("albumname", data.albumname);
//                        intent.putExtra("specialname", subject.specialname);
//                        intent.putExtra("photo", subject.photo);
//                        intent.putExtra("logo", subject.logo);
//                        intent.putExtra("description", subject.description);
//                        intent.putExtra("page", subject.page);
//                        intent.putExtra("HomeSubjectFragment", "HomeSubjectFragment");
//                        intent.putExtra("subjectActivity", "subjectActivity");
                        context.startActivity(intent);
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
