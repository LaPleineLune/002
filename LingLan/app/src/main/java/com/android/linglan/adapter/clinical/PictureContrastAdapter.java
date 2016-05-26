package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.linglan.http.bean.PictureContrastBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ClinicalDetailsActivity;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/8 0008.
 * 图片对比的Adapter
 */
public class PictureContrastAdapter extends RecyclerView.Adapter {
    private Activity context;
    private ArrayList<PictureContrastBean.PictureContrastData> pictureContrastData;
    private PictureContrastImageAdapter imageAdapter;
    private ArrayList<String> imgUrls;

    public PictureContrastAdapter(Activity context) {
        this.context = context;
        imgUrls = new ArrayList<String>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_picture_contras, parent, false);
        return new ClinicalListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalListViewHolder)holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.pictureContrastData != null ? this.pictureContrastData.size() : 0 ;
//        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateAdapter(ArrayList<PictureContrastBean.PictureContrastData> pictureContrastData){
        this.pictureContrastData = pictureContrastData;
        imgUrls.clear();
        for(PictureContrastBean.PictureContrastData  imgs : pictureContrastData){
            for(PictureContrastBean.PictureContrastData.PictureContrastMedia img :imgs.media){
                imgUrls.add(img.mediaurl);
            }
        }
        this.notifyDataSetChanged();
    }

    class ClinicalListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private TextView tv_visit_time;
        private RecyclerView rec_img;
        public ClinicalListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            tv_visit_time = (TextView) rootView.findViewById(R.id.tv_visit_time);
            rec_img = (RecyclerView) rootView.findViewById(R.id.rec_img);

        }
        public void bindData(final int position) {
            tv_visit_time.setText(pictureContrastData.get(position).visittime);

            LinearLayoutManager linearLayoutManagerImg = new LinearLayoutManager(context);
            linearLayoutManagerImg.setOrientation(LinearLayoutManager.HORIZONTAL);
            rec_img.setLayoutManager(linearLayoutManagerImg);
            rec_img.setHasFixedSize(true);
            imageAdapter = new PictureContrastImageAdapter(context, pictureContrastData.get(position).media,imgUrls);
            rec_img.setAdapter(imageAdapter);

//            rootView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.setClass(context, ClinicalDetailsActivity.class);
//                    context.startActivity(intent);
//                }
//            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
