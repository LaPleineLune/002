package com.android.linglan.adapter.clinical;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.camerautils.FileOperateUtil;
import com.android.linglan.ui.R;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/4/8 0008.
 * 临证患者列表
 */
public class PhotoAdapter extends RecyclerView.Adapter {
    private Activity context;
    private ArrayList<Bitmap> photoes;
    private ArrayList<String> strs;

    public PhotoAdapter(Activity context) {
        this.context = context;
        strs = new ArrayList<String>();
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
        return this.photoes != null ? this.photoes.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateAdapter(ArrayList<Bitmap> photoes, String text) {
        this.photoes = photoes;
        this.strs.add(text);
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

        }

        public void bindData(final int position) {
            image_thumbnail.setImageBitmap(photoes.get(photoes.size() - 1 - position));
            tv_item_name.setText(strs.get(strs.size() - 1 - position));
//            if (text.size() > 0) {
//                tv_item_name.setText(text.get(text.size() - 1));
//            }
            img_item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoes.remove(photoes.size() - 1 - position);
                    strs.remove(strs.size() - 1 - position);
                    notifyDataSetChanged();
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
