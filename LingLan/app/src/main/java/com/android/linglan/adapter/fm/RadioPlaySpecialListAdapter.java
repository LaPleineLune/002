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

import com.android.linglan.http.bean.HomepageRecommendBean;
import com.android.linglan.http.bean.RadioPlayListBean;
import com.android.linglan.ui.R;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/6/15 0015.
 * 音频专辑播放列表
 */
public class RadioPlaySpecialListAdapter extends RecyclerView.Adapter {
    private Context context;
    private Intent intent;
    private String mAudioid = "";
    public ArrayList<RadioPlayListBean.RadioPlayListData> radioPlayListData;

    public RadioPlaySpecialListAdapter(Context context, String audioid) {
        this.context = context;
        this.mAudioid = audioid;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_radio_play_special_list, parent, false);
        return new RadioPlaySpecialListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        ((RadioPlaySpecialListViewHolder) holder).bindData(homepageRecommend.get(position), position);
        ((RadioPlaySpecialListViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.radioPlayListData != null ? this.radioPlayListData.size() : 0;
//        return 10;
    }

    public void updateAdapter(ArrayList<RadioPlayListBean.RadioPlayListData> radioPlayListData) {
        this.radioPlayListData = radioPlayListData;
        this.notifyDataSetChanged();
    }

    class RadioPlaySpecialListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View rootView;

        private TextView tv_radio_title;
        private ImageView img_play_flag;
        private int index;


        public RadioPlaySpecialListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_radio_title = (TextView) rootView.findViewById(R.id.tv_radio_title);
            img_play_flag = (ImageView) rootView.findViewById(R.id.img_play_flag);
        }

        public void bindData(final int position) {
            this.index = position;
            if (mAudioid.equals(radioPlayListData.get(position).audioid)) {
                img_play_flag.setVisibility(View.VISIBLE);
                tv_radio_title.setTextColor(ContextCompat.getColor(context, R.color.carminum));
            } else {
                img_play_flag.setVisibility(View.GONE);
            }
            tv_radio_title.setText(radioPlayListData.get(position).title);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
