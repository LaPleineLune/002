package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.PictureContrastBean;
import com.android.linglan.http.bean.UpdataPhotoBeen;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ViewPhotoActivity;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/30 0030.
 * 图片对比中的图片的Adapter
 */
public class PictureContrastImageAdapter extends RecyclerView.Adapter {
    private Activity context;
    private ArrayList<PictureContrastBean.PictureContrastData.PictureContrastMedia> img;
    private ArrayList<String> categoryNames;
    private ArrayList<String> mDataSet;
    private ArrayList<String> imgUrls;
    public PictureContrastImageAdapter(Activity context, ArrayList<PictureContrastBean.PictureContrastData.PictureContrastMedia> img,ArrayList<String> imgUrls) {
        this.context = context;
        this.img = img;
        this.imgUrls = imgUrls;
        mDataSet = new ArrayList<String>();
//        categoryNames = new ArrayList<String>();
        mDataSet.clear();
        if (img != null && img.size() != 0) {
            for(PictureContrastBean.PictureContrastData.PictureContrastMedia updataPhotoBeen :  img){
                mDataSet.add(updataPhotoBeen.mediaurl);
//                categoryNames.add(updataPhotoBeen.categoryname);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_picture_contras_photo, parent, false);
        return new ClinicalListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalListViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.img != null ? this.img.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateAdapter(ArrayList<PictureContrastBean.PictureContrastData.PictureContrastMedia> img) {
        this.img = img;
        this.notifyDataSetChanged();
    }

    class ClinicalListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private ImageView image_thumbnail;

        public ClinicalListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            image_thumbnail = (ImageView) rootView.findViewById(R.id.image_thumbnail);

        }

        public void bindData(final int position) {
            try {
                ImageUtil.loadImageAsync(image_thumbnail,
                        R.dimen.dp76, R.dimen.dp76,
                        R.drawable.default_image, img.get(position).mediaurl, null);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            image_thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int current = 0;
                    Intent intent = new Intent(context, ViewPhotoActivity.class);
                    intent.putStringArrayListExtra("imgUrls", imgUrls);
                    intent.putExtra("multiImageSelectorFragment", "multiImageSelectorFragment");
                    for(int i = 0;i < imgUrls.size();i++){
                        if(imgUrls.get(i).equals(img.get(position).mediaurl)){
                            current = i;
                        }
                    }
                    intent.putExtra("current", current);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}