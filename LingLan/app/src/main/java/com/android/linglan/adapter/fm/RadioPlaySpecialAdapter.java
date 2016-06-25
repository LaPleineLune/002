package com.android.linglan.adapter.fm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.RadioSpecialInfoBean;
import com.android.linglan.http.bean.RadioSpecialListBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.fm.RadioBatchDownloadActivity;
import com.android.linglan.ui.fm.RadioPlayActivity;
import com.android.linglan.ui.fm.RadioSpecialActivity;
import com.android.linglan.ui.fm.RadioSpecialDescriptionActivity;
import com.android.linglan.ui.me.RegisterActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/6/15 0015.
 * 音频专辑播放列表
 */
public class RadioPlaySpecialAdapter extends RecyclerView.Adapter {
    private Context context;
    private Intent intent;
    public RadioSpecialInfoBean.RadioSpecialInfoData radioSpecialInfoData;
    public ArrayList<RadioSpecialListBean.RadioSpecialListData> radioSpecialListData;
    private String albumid;

    public RadioPlaySpecialAdapter(Context context, String albumid) {
        this.context = context;
        this.albumid = albumid;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_radio_play_special, parent, false);
        return new RadioPlaySpecialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        ((RadioPlaySpecialViewHolder) holder).bindData(homepageRecommend.get(position), position);
        ((RadioPlaySpecialViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.radioSpecialListData != null ? this.radioSpecialListData.size() + 1 : 1;
    }

    public void updateAdapterTitle(RadioSpecialInfoBean.RadioSpecialInfoData radioSpecialInfoData) {
        this.radioSpecialInfoData = radioSpecialInfoData;
        this.notifyDataSetChanged();
    }

    public void updateAdapter(ArrayList<RadioSpecialListBean.RadioSpecialListData> radioSpecialListData) {
        this.radioSpecialListData = radioSpecialListData;
//        this.edit = edit;
        this.notifyDataSetChanged();
    }

    class RadioPlaySpecialViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View rootView;

        private LinearLayout ll_item_title;
        private LinearLayout ll_description;
        private ImageView logo;
        private TextView count_chapter;
        private TextView anchor;
        private TextView author;
        private TextView description;
        private Button collect;
        private Button batch_download;

        private LinearLayout ll_item_body;
        private TextView title;
        private TextView play_num;
        private TextView duration;
        private TextView updata;
        private ImageView img_radio_play;
        private int position;
        private String isCollect = "";
        protected static final int REQUEST_COLLECT_INFO = 0;
        protected static final int REQUEST_COLLECT = 1;

        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REQUEST_COLLECT_INFO:
                        String mCollect = msg.getData().getString("isCollect");
//                        if (radioSpecialInfoData.iscollect.equals("0")) {
                        if (mCollect.equals("0")) {
                            collect.setText("收藏专辑");
                            collect.setTextColor(ContextCompat.getColor(context, R.color.blue));
                            collect.setBackgroundResource(R.drawable.radio_batch_download);
                        } else {
                            collect.setText("已收藏");
                            collect.setTextColor(ContextCompat.getColor(context, R.color.white));
                            collect.setBackgroundResource(R.drawable.radio_collect);
                        }
                        break;
                    case REQUEST_COLLECT:
                        if (isCollect.equals("0")) {
                            isCollect = "1";
                            collect.setText("已收藏");
                            collect.setTextColor(ContextCompat.getColor(context, R.color.white));
                            collect.setBackgroundResource(R.drawable.radio_collect);
                        } else if(isCollect.equals("1")) {
                            isCollect = "0";
                            collect.setText("收藏专辑");
                            collect.setTextColor(ContextCompat.getColor(context, R.color.blue));
                            collect.setBackgroundResource(R.drawable.radio_batch_download);
                        }
                        break;
                }
            }
        };

        public RadioPlaySpecialViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            ll_item_title = (LinearLayout) rootView.findViewById(R.id.ll_item_title);
            ll_description = (LinearLayout) rootView.findViewById(R.id.ll_description);
            logo = (ImageView) rootView.findViewById(R.id.img_logo);
            count_chapter = (TextView) rootView.findViewById(R.id.tv_count_chapter);
            anchor = (TextView) rootView.findViewById(R.id.tv_anchor);
            author = (TextView) rootView.findViewById(R.id.tv_author);
            description = (TextView) rootView.findViewById(R.id.tv_description);
            collect = (Button) rootView.findViewById(R.id.bt_radio_collect);
            batch_download = (Button) rootView.findViewById(R.id.bt_radio_batch_download);

            ll_item_body = (LinearLayout) rootView.findViewById(R.id.ll_item_body);
            title = (TextView) rootView.findViewById(R.id.tv_radio_title);
            play_num = (TextView) rootView.findViewById(R.id.tv_radio_play_num);
            duration = (TextView) rootView.findViewById(R.id.tv_radio_duration);
            updata = (TextView) rootView.findViewById(R.id.tv_radio_update);
            img_radio_play = (ImageView) rootView.findViewById(R.id.img_radio_play);
        }

        public void bindData(final int position) {// radioSpecialInfoData
            intent = new Intent();
            this.position = position;
            ll_description.setOnClickListener(this);
            collect.setOnClickListener(this);
            batch_download.setOnClickListener(this);
            ll_item_body.setOnClickListener(this);
            img_radio_play.setOnClickListener(this);
            if (0 == position && radioSpecialInfoData != null) {
                ll_item_title.setVisibility(View.VISIBLE);
                ll_item_body.setVisibility(View.GONE);
                try {
                    ImageUtil.loadImageAsync(logo, R.dimen.dp90, R.dimen.dp90, R.drawable.default_image, radioSpecialInfoData.photo, null);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                count_chapter.setText("集\t\t\t\t数：" + radioSpecialInfoData.count_chapter);
                anchor.setText("主\t\t\t\t播：" + radioSpecialInfoData.anchor);
                author.setText("文字作者：" + radioSpecialInfoData.author);
                description.setText(radioSpecialInfoData.description);
                isCollect = radioSpecialInfoData.iscollect;
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("isCollect", isCollect);
                message.setData(bundle);// bundle传值，耗时，效率低
                handler.sendMessage(message);// 发送message信息
                message.what = REQUEST_COLLECT_INFO;// 标志是哪个线程传数据
            } else if (radioSpecialListData != null) {
                ll_item_title.setVisibility(View.GONE);
                ll_item_body.setVisibility(View.VISIBLE);
                String oldAudioid = SharedPreferencesUtil.getString("audioid" + radioSpecialListData.get(position-1).audioid, null);
                if (oldAudioid != null && oldAudioid.equals(radioSpecialListData.get(position-1).audioid)) {
                    title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                } else {
                    title.setTextColor(ContextCompat.getColor(context, R.color.text_title_color));
                }
                title.setText(radioSpecialListData.get(position-1).title);
                play_num.setText(radioSpecialListData.get(position-1).count_view);
                duration.setText(radioSpecialListData.get(position-1).duration);
                updata.setText(radioSpecialListData.get(position-1).publishtime);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_description:
//                    ToastUtil.show("我是简介");
                    intent.setClass(context, RadioSpecialDescriptionActivity.class);
                    intent.putExtra("description", radioSpecialInfoData.description);
                    context.startActivity(intent);
                    break;
                case R.id.bt_radio_collect:
//                    ToastUtil.show("我是收藏");
                    if (NetApi.getToken() != null) {
                        getRadioSpecialCollect();
                    } else {
                        intent.setClass(context, RegisterActivity.class);
                        context.startActivity(intent);
                        ((RadioSpecialActivity) context).isLogin = true;
                        mCollectOnClickListener.collectOnClickListener(v);
                    }
                    break;
                case R.id.bt_radio_batch_download:
                    intent.setClass(context, RadioBatchDownloadActivity.class);
                    intent.putExtra("albumid",albumid);
                    context.startActivity(intent);
                    break;
                case R.id.ll_item_body:
                    intent.setClass(context, RadioPlayActivity.class);
                    if (radioSpecialListData != null)
                    intent.putExtra("audioid", radioSpecialListData.get(position -1).audioid);
                    intent.putExtra("title", radioSpecialListData.get(position -1).title);
                    context.startActivity(intent);
                    SharedPreferencesUtil.saveString("audioid" + radioSpecialListData.get(position - 1).audioid, radioSpecialListData.get(position - 1).audioid);
                    title.setTextColor(ContextCompat.getColor(context, R.color.read_text_title_color));
                    break;
                case R.id.img_radio_play:
                    ToastUtil.show("播放按钮");
                    break;
            }
        }

        private void getRadioSpecialCollect() {
            NetApi.getRadioSpecialCollect(new PasserbyClient.HttpCallback() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e("getRadioSpecialCollect=" + result);
                    if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, context)) {
                        return;
                    }
//                    handler.sendEmptyMessage(REQUEST_COLLECT);
                    try {
                        JSONObject json = new JSONObject(result);
                        if (json.getString("code").equals("0")) {
                            handler.sendEmptyMessage(REQUEST_COLLECT);
                        }
                        String msg = json.getString("msg");
                        ToastUtil.show(msg);
                        LogUtil.e("专辑点赞" + msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String message) {

                }
            }, albumid, isCollect);
        }



    }
    private CollectOnClickListener mCollectOnClickListener;
    public void setCollectOnClickListener(CollectOnClickListener listener){
        LogUtil.e("setmItemOnClickListener...");
        this.mCollectOnClickListener = listener;
    }

    public interface CollectOnClickListener{
        /**
         * 传递点击的view
         * @param view
         */
        public void collectOnClickListener(View view);
    }

}
