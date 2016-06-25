package com.android.linglan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.RadioListBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.fm.RadioPlayActivity;
import com.android.linglan.ui.fm.RadioSpecialActivity;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/6/20 0020.
 *
 */
public class SearchAllRadioAdapter extends RecyclerView.Adapter {
    private Context context;
    private Intent intent;
    private ArrayList<RadioListBean.RadioListData> radioListData;

    public SearchAllRadioAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_search_radio, parent, false);
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
        private TextView tv_radio_title,tv_play_count,duration,tv_publishtime,tv_album_logo;
        private ImageView img_homepage_logo;
        private RadioListBean.RadioListData radioListData;

        public RadioViewHolder(View rootView) {
            super(rootView);
            initView(rootView);

        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_radio_title = (TextView)rootView.findViewById(R.id.tv_radio_title);
            tv_play_count = (TextView)rootView.findViewById(R.id.tv_play_count);
            duration = (TextView) rootView.findViewById(R.id.duration);
            tv_publishtime = (TextView) rootView.findViewById(R.id.tv_publishtime);
            img_homepage_logo = (ImageView) rootView.findViewById(R.id.img_homepage_logo);
            tv_album_logo = (TextView) rootView.findViewById(R.id.tv_album_logo);
        }

        public void bindData(final RadioListBean.RadioListData radioListData) {
            this.radioListData = radioListData;
            // 单个音频 5，专辑6
            if(radioListData.type.equals("5")){
                tv_radio_title.setText(radioListData.title);
                tv_play_count.setText(radioListData.count_view);
                duration.setText(radioListData.duration);
                tv_publishtime.setText(radioListData.publishtime);
                img_homepage_logo.setVisibility(View.VISIBLE);
                tv_album_logo.setVisibility(View.GONE);

                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    MobclickAgent.onEvent(context, UmengBuriedPointUtil.StudyClickCharacter);
                        intent = new Intent();
                        intent.setClass(context, RadioPlayActivity.class);
                        intent.putExtra("audioid", radioListData.audioid);
                        context.startActivity(intent);
                    }
                });
            }else if(radioListData.type.equals("6")){
                tv_radio_title.setText(radioListData.albumname);
                tv_play_count.setText(radioListData.count_play);
                duration.setText(radioListData.count_chapter + "集");
//                tv_publishtime.setText(fmClassifyListBean.publishtime);
                tv_album_logo.setVisibility(View.VISIBLE);
                img_homepage_logo.setVisibility(View.GONE);

                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent();
//                        MobclickAgent.onEvent(context, UmengBuriedPointUtil.StudySubjectContent);
                        intent.setClass(context, RadioSpecialActivity.class);
                        intent.putExtra("albumid", radioListData.albumid);
                        intent.putExtra("albumname", radioListData.albumname);
                        context.startActivity(intent);
                    }
                });
            }

        }

        @Override
        public void onClick(View v) {

        }
    }
}
