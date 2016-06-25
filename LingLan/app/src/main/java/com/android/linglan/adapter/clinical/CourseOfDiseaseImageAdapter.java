package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.bean.ClinicalDetailsBean;
import com.android.linglan.http.bean.UpdataPhotoBeen;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ViewPhotoActivity;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/25 0025.
 * 病程列表中的Image的Adapter
 */
public class CourseOfDiseaseImageAdapter extends RecyclerView.Adapter {
    private Activity context;
    private ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg> img;
    private ArrayList<String> mDataSet;
    private ArrayList<String> mDataMediaId;
    public String courseid;// 病程id
    private ArrayList<String> categoryNames;

    public CourseOfDiseaseImageAdapter(Activity context, ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg> img, String courseid) {
        this.context = context;
        this.img = img;
        this.courseid = courseid;
        mDataSet = new ArrayList<String>();
        mDataMediaId = new ArrayList<String>();
        categoryNames = new ArrayList<String>();
        if (img != null && img.size() != 0) {
            for (ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg updataPhotoBeen : img) {
                mDataSet.add(updataPhotoBeen.mediaurl);
                mDataMediaId.add(updataPhotoBeen.mediaid);
                categoryNames.add(updataPhotoBeen.categoryname);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_photo, parent, false);
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

    public void updateAdapter(Activity context, ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg> img) {
        this.context = context;
        this.img = img;
        mDataSet.clear();
        categoryNames.clear();
        if (img != null && img.size() != 0) {
            for(ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg updataPhotoBeen :  img){
                mDataSet.add(updataPhotoBeen.mediaurl);
                categoryNames.add(updataPhotoBeen.categoryname);
            }
        }
        this.notifyDataSetChanged();
    }

    class ClinicalListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private ImageView image_thumbnail;
        private TextView tv_item_name;
        private ImageView img_item_delete;

        public ClinicalListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            image_thumbnail = (ImageView) rootView.findViewById(R.id.image_thumbnail);
            tv_item_name = (TextView) rootView.findViewById(R.id.tv_item_name);
            img_item_delete = (ImageView) rootView.findViewById(R.id.img_item_delete);
            img_item_delete.setVisibility(View.GONE);

        }

        public void bindData(final int position) {
            try {
                ImageUtil.loadImageAsync(image_thumbnail,
                        R.dimen.dp76, R.dimen.dp76,
                        R.drawable.default_image, img.get(position).mediaurl, null);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            tv_item_name.setText(img.get(position).categoryname);

            image_thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList mSelectPath = new ArrayList();
                    mSelectPath.addAll(mDataSet);
                    Intent intent = new Intent(context, ViewPhotoActivity.class);
                    intent.putExtra("current", position);
                    intent.putStringArrayListExtra("imgUrls", mSelectPath);
                    intent.putStringArrayListExtra("mDataMediaId", mDataMediaId);
                    intent.putStringArrayListExtra("categoryNames", categoryNames);
                    intent.putExtra("courseid", courseid);
                    intent.putExtra("clinicalDetailsActivity","clinicalDetailsActivity");
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}
