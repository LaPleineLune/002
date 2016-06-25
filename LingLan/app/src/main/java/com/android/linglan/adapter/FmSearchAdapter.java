package com.android.linglan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.Constants;
import com.android.linglan.http.bean.SearchArticleBean;
import com.android.linglan.http.bean.SearchFMBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.fm.RadioPlayActivity;
import com.android.linglan.ui.fm.RadioSpecialActivity;
import com.android.linglan.ui.study.ArticleDetailsActivity;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by wuiqngci on 2016/1/6 0006.
 */
public class FmSearchAdapter extends
        RecyclerView.Adapter{
    private Context context;
    private Intent intent;
    private ArrayList<SearchFMBean.FmClassifyListBean> fms;

    public void update(ArrayList<SearchFMBean.FmClassifyListBean> fms) {
        this.fms = fms;
        notifyDataSetChanged();
    }

    public FmSearchAdapter(Context context) {
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_search_radio, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ( (ArticleViewHolder)holder).bindData(fms.get(position));
    }

    @Override
    public int getItemCount() {
        return fms== null ? 0:fms.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView tv_radio_title,tv_play_count,duration,tv_publishtime,tv_album_logo;
        private ImageView img_homepage_logo;
        private SearchFMBean.FmClassifyListBean fmClassifyListBean;

        public ArticleViewHolder(View rootView) {
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
        public void bindData(final SearchFMBean.FmClassifyListBean fmClassifyListBean) {
            this.fmClassifyListBean = fmClassifyListBean;
            // 单个音频 5，专辑6
            if(fmClassifyListBean.type.equals("5")){
                tv_radio_title.setText(fmClassifyListBean.title);
                tv_play_count.setText(fmClassifyListBean.count_view);
                duration.setText(fmClassifyListBean.duration);
                tv_publishtime.setText(fmClassifyListBean.publishtime);
                img_homepage_logo.setVisibility(View.VISIBLE);
                tv_album_logo.setVisibility(View.GONE);

                rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    MobclickAgent.onEvent(context, UmengBuriedPointUtil.StudyClickCharacter);
                    intent = new Intent();
                    intent.setClass(context, RadioPlayActivity.class);
                    intent.putExtra("audioid", fmClassifyListBean.audioid);
                    context.startActivity(intent);
                }
            });
            }else if(fmClassifyListBean.type.equals("6")){
                tv_radio_title.setText(fmClassifyListBean.albumname);
                tv_play_count.setText(fmClassifyListBean.count_play);
                duration.setText(fmClassifyListBean.count_chapter + "集");
//                tv_publishtime.setText(fmClassifyListBean.publishtime);
                tv_album_logo.setVisibility(View.VISIBLE);
                img_homepage_logo.setVisibility(View.GONE);

                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent();
//                        MobclickAgent.onEvent(context, UmengBuriedPointUtil.StudySubjectContent);
                            intent.setClass(context, RadioSpecialActivity.class);
                            intent.putExtra("albumid", fmClassifyListBean.albumid);
                        LogUtil.e("albumid = " + fmClassifyListBean.albumid );
                            intent.putExtra("albumname", fmClassifyListBean.albumname);
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
