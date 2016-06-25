package com.android.linglan.adapter.clinical;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.linglan.fragment.ClinicalFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalDetailsBean;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ClinicalCreateActivity;
import com.android.linglan.ui.clinical.MultiImageSelectorActivity;
import com.android.linglan.ui.clinical.ViewPhotoActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.nereo.multi_image_selector.utils.PermissionsUtils;

/**
 * Created by LeeMy on 2016/4/25 0025.
 * 病程列表中的Image的Adapter
 */
public class EditCourseOfDiseaseImageAdapter extends RecyclerView.Adapter {
    private Activity context;
    private ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg> img;
    private ArrayList<String> mDataSet;
    private ArrayList<String> categoryNames;
    private ArrayList<String> mDataMediaId;

    public EditCourseOfDiseaseImageAdapter(Activity context, ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg> img) {
        this.context = context;
        this.img = img;
        mDataSet = new ArrayList<String>();
        categoryNames = new ArrayList<String>();
        mDataMediaId = new ArrayList<String>();
        mDataSet.clear();
        categoryNames.clear();
        mDataMediaId.clear();
        if (img != null && img.size() != 0) {
            for (ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg updataPhotoBeen : img) {
                mDataSet.add(updataPhotoBeen.mediaurl);
                categoryNames.add(updataPhotoBeen.categoryname);
                mDataMediaId.add(updataPhotoBeen.mediaid);
            }
        }
    }

    public void updata(ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg> img) {
        this.img = img;
        mDataSet = new ArrayList<String>();
        categoryNames = new ArrayList<String>();
        mDataMediaId = new ArrayList<String>();
        mDataSet.clear();
        categoryNames.clear();
        mDataMediaId.clear();
        if (img != null && img.size() != 0) {
            for (ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg updataPhotoBeen : img) {
                mDataSet.add(updataPhotoBeen.mediaurl);
                categoryNames.add(updataPhotoBeen.categoryname);
                mDataMediaId.add(updataPhotoBeen.mediaid);
            }
        }
        notifyDataSetChanged();
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
        return this.img != null ? (this.img.size() + 1) : 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

//    public void updateAdapter(ArrayList<Bitmap> photoes, String text) {
//        this.photoes = photoes;
//        this.strs.add(text);
//        this.notifyDataSetChanged();
//    }

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
            //如果没有数据，只显示添加图片的加号图片
            if (img == null || img.size() == 0) {
                image_thumbnail.setImageResource(R.drawable.add_related_pictures);
                tv_item_name.setVisibility(View.GONE);
            } else {
                //如果有数据，添加图片的加号图片显示在第一个位置
                if (position == 0) {
                    image_thumbnail.setImageResource(R.drawable.add_related_pictures);
                    tv_item_name.setVisibility(View.GONE);
                } else {
                    ImageUtil.loadImageAsync(image_thumbnail, R.dimen.dp76, R.dimen.dp76, R.drawable.default_image, img.get(position - 1).mediaurl, null);
                    tv_item_name.setText(img.get(position - 1).categoryname);
                }
            }

            if (img != null && img.size() != 0) {
                LogUtil.e("img.size() = " + img.size());
                if (position == 0) {
                    rootView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(ClinicalFragment.MaxPictureNumber == 0){
                                getMaxPictureNumber();
                            } else{
                                if (img.size() < ClinicalFragment.MaxPictureNumber) {
                                    ArrayList mSelectPath = new ArrayList();
                                    mSelectPath.addAll(mDataSet);
                                    if (PermissionsUtils.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                        Intent intent = new Intent(context, MultiImageSelectorActivity.class);
                                        // 是否显示拍摄图片
                                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                                        // 最大可选择图片数量
                                        if (ClinicalFragment.MaxPictureNumber - img.size() < 9) {
                                            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, (ClinicalFragment.MaxPictureNumber - img.size()));
                                        } else {
                                            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                                        }
//                                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                                        // 选择模式
                                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                                        context.startActivityForResult(intent, 100);
                                    } else {
                                        Toast.makeText(context, "请到应用管理中授予权限", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    ToastUtil.show("最多添加20张图片");
                                }
                            }
                        }
                    });
                } else {
                    rootView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ToastUtil.show("我出来了");
                            ArrayList mSelectPath = new ArrayList();
                            mSelectPath.addAll(mDataSet);
                            Intent intent = new Intent(context, ViewPhotoActivity.class);
                            intent.putExtra("current", position - 1);
                            intent.putStringArrayListExtra("imgUrls", mSelectPath);
                            intent.putStringArrayListExtra("categoryNames", categoryNames);
                            intent.putStringArrayListExtra("mDataMediaId", mDataMediaId);
                            context.startActivityForResult(intent, 102);
                        }
                    });
                }
            } else {
                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            ArrayList mSelectPath = new ArrayList();
                            mSelectPath.addAll(mDataSet);
                            if (PermissionsUtils.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                Intent intent = new Intent(context, MultiImageSelectorActivity.class);
                                // 是否显示拍摄图片
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                                // 最大可选择图片数量
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                                // 选择模式
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                                context.startActivityForResult(intent, 100);
                            } else {
                                Toast.makeText(context, "请到应用管理中授予权限", Toast.LENGTH_LONG).show();
                            }
                    }
                });
            }
        }


//            try {
//                ImageUtil.loadImageAsync(image_thumbnail,
//                        R.dimen.dp76, R.dimen.dp76,
//                        R.drawable.default_image, img.get(position).mediaurl, null);
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//            tv_item_name.setText(img.get(position).categoryname);
//        }

        @Override
        public void onClick(View v) {

        }
    }

    public void getMaxPictureNumber(){
        NetApi.getMaxPictureNumber(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, context)) {
                    return;
                }
                try {

                    JSONObject data = new JSONObject(result);
                    ClinicalFragment.MaxPictureNumber = data.getInt("data");

                    if (mDataMediaId.size() < ClinicalFragment.MaxPictureNumber) {

                        ArrayList mSelectPath = new ArrayList();
                        mSelectPath.addAll(mDataSet);
                        if (PermissionsUtils.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Intent intent = new Intent(context, MultiImageSelectorActivity.class);
                            // 是否显示拍摄图片
                            intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                            // 最大可选择图片数量
                            if (ClinicalFragment.MaxPictureNumber - mDataMediaId.size() < 9) {
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, (ClinicalFragment.MaxPictureNumber - mDataMediaId.size()));
                            } else {
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                            }

                            // 选择模式
                            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
//                                    if (mSelectPath != null && mSelectPath.size() > 0) {
//                                        intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
//                                    }
                            context.startActivityForResult(intent, 100);
                        } else {
                            Toast.makeText(context, "请到应用管理中授予权限", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        ToastUtil.show("最多添加20张图片");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                ToastUtil.show("MaxPictureNumber = " + MaxPictureNumber);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
