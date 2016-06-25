package com.android.linglan.adapter.clinical;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.linglan.camerautils.DisplayImageOptions;
import com.android.linglan.camerautils.ImageLoader;
import com.android.linglan.camerautils.RoundedBitmapDisplayer;
import com.android.linglan.fragment.ClinicalFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.UpdataPhotoBeen;
import com.android.linglan.ui.R;
import com.android.linglan.ui.clinical.ClinicalCreateActivity;
import com.android.linglan.ui.clinical.MultiImageSelectorActivity;
import com.android.linglan.ui.clinical.ViewPhotoActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.utils.PermissionsUtils;

/**
 * Created by LeeMy on 2016/4/8 0008.
 * 临证患者列表
 */
public class CreatePhotoAdapter extends RecyclerView.Adapter {
    private Activity context;
    private ArrayList<Bitmap> photoes;
    private List<UpdataPhotoBeen> updataPhotoBeens;
    private ImageLoader mImageLoader;
    private static DisplayImageOptions mOptions;


    private PopupWindow popupWindow;
    private View popView;
    private ArrayList<String> mResults = new ArrayList<>();
    private ArrayList<String> categoryNames;
    private ArrayList<String> mDataSet;
    private ArrayList<String> mDataMediaId;

    public CreatePhotoAdapter(Activity context, List<UpdataPhotoBeen> updataPhotoBeens) {
        this.context = context;
        this.updataPhotoBeens = updataPhotoBeens;
        mDataSet = new ArrayList<String>();
        categoryNames = new ArrayList<String>();
        mDataMediaId = new ArrayList<String>();
        mDataSet.clear();
        categoryNames.clear();
        mDataMediaId.clear();
        if (updataPhotoBeens != null && updataPhotoBeens.size() != 0) {
            for (UpdataPhotoBeen updataPhotoBeen : updataPhotoBeens) {
                mDataSet.add(updataPhotoBeen.data.mediaurl);
                categoryNames.add(updataPhotoBeen.data.categoryname);
                mDataMediaId.add(updataPhotoBeen.data.mediaid);
//                LogUtil.e("" + updataPhotoBeen.data.mediaid);
            }
        }
        mImageLoader = ImageLoader.getInstance(context);
        popupWindow = new PopupWindow(context);
        //设置网络图片加载参数
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder = builder
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .displayer(new RoundedBitmapDisplayer(0));
        mOptions = builder.build();
    }

    public void updata(List<UpdataPhotoBeen> updataPhotoBeens) {
        this.updataPhotoBeens = updataPhotoBeens;
        mDataSet.clear();
        categoryNames.clear();
        mDataMediaId.clear();
        if (updataPhotoBeens != null && updataPhotoBeens.size() != 0) {
            for (UpdataPhotoBeen updataPhotoBeen : updataPhotoBeens) {
                mDataSet.add(updataPhotoBeen.data.mediaurl);
                categoryNames.add(updataPhotoBeen.data.categoryname);
                mDataMediaId.add(updataPhotoBeen.data.mediaid);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_create_photo, parent, false);
        popView = LayoutInflater.from(context).inflate(R.layout.popupview_profile, null);
        return new ClinicalListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ClinicalListViewHolder) holder).bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.updataPhotoBeens != null ? (this.updataPhotoBeens.size() + 1) : 1;
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

    public void updateAdapter(List<UpdataPhotoBeen> updataPhotoBeens) {
        this.updataPhotoBeens = updataPhotoBeens;
        this.notifyDataSetChanged();
    }


    class ClinicalListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private View rootView;
        private ImageView image_thumbnail;
        private TextView tv_item_name;

        public ClinicalListViewHolder(View rootView) {
            super(rootView);
            initView(rootView);
        }

        private void initView(View rootView) {
            this.rootView = rootView;
            image_thumbnail = (ImageView) rootView.findViewById(R.id.image_thumbnail);
            tv_item_name = (TextView) rootView.findViewById(R.id.tv_item_name);

        }

        public void bindData(final int position) {
            if (updataPhotoBeens == null || updataPhotoBeens.size() == 0) {
                image_thumbnail.setImageResource(R.drawable.add_related_pictures);
                tv_item_name.setVisibility(View.GONE);
            } else {
                if (position == 0) {
                    image_thumbnail.setImageResource(R.drawable.add_related_pictures);
                    tv_item_name.setVisibility(View.GONE);
                } else {
                    ImageUtil.loadImageAsync(image_thumbnail, R.dimen.dp76, R.dimen.dp76, R.drawable.default_image, updataPhotoBeens.get(position - 1).data.mediaurl, null);
                    tv_item_name.setText(updataPhotoBeens.get(position - 1).data.categoryname);
                }
            }

            if (updataPhotoBeens != null && updataPhotoBeens.size() != 0) {
                if (position == 0) {
                    rootView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(ClinicalFragment.MaxPictureNumber == 0){
                                getMaxPictureNumber();
                            } else{
                                if (mDataMediaId.size() < ClinicalFragment.MaxPictureNumber) {

                                    ArrayList mSelectPath = new ArrayList();
                                    mSelectPath.addAll(mDataSet);
                                    if (PermissionsUtils.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                        Intent intent = new Intent(context, MultiImageSelectorActivity.class);
                                        // 是否显示拍摄图片
                                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                                        // 最大可选择图片数量
                                        if(ClinicalFragment.MaxPictureNumber - mDataMediaId.size() < 9){
                                            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, (ClinicalFragment.MaxPictureNumber - mDataMediaId.size()));
                                        }else{
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
                            }
                        }

                    });
                } else {
                    rootView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            // 默认选择
//                            if(mDataMediaId.size() < ClinicalCreateActivity.MaxPictureNumber + 1){
                            ArrayList mSelectPath = new ArrayList();
                            mSelectPath.addAll(mDataSet);
                            Intent intent = new Intent(context, ViewPhotoActivity.class);
                            intent.putExtra("current", position - 1);
                            intent.putStringArrayListExtra("mDataMediaId", mDataMediaId);
                            intent.putStringArrayListExtra("imgUrls", mSelectPath);
                            intent.putStringArrayListExtra("categoryNames", categoryNames);
                            context.startActivityForResult(intent, 102);
//                            }else{
//                                ToastUtil.show("最多添加20张图片");
//                            }

                        }
                    });
                }
            } else {
                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 默认选择
                            ArrayList mSelectPath = new ArrayList();
                            mSelectPath.addAll(mDataSet);
                            if (mDataSet.size() == 0 || position == mDataSet.size() - 1) {
                                if (PermissionsUtils.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                    Intent intent = new Intent(context, MultiImageSelectorActivity.class);
                                    // 是否显示拍摄图片
                                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                                    // 最大可选择图片数量
                                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                                    // 选择模式
                                    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
//                                if (mSelectPath != null && mSelectPath.size() > 0) {
//                                    mSelectPath.clear();
//                                }
//                                intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
                                    context.startActivityForResult(intent, 100);

                                } else {
                                    Toast.makeText(context, "请到应用管理中授予权限", Toast.LENGTH_LONG).show();
                                }

                                return;
                            }
                    }
                });
            }
        }

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
                            if(ClinicalFragment.MaxPictureNumber - mDataMediaId.size() < 9){
                                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, (ClinicalFragment.MaxPictureNumber - mDataMediaId.size()));
                            }else{
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
