package com.android.linglan.ui.clinical;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.android.linglan.adapter.clinical.PhotoAdapter;
import com.android.linglan.camerautils.CameraContainer;
import com.android.linglan.camerautils.CameraView;
import com.android.linglan.camerautils.FileOperateUtil;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.UpdataPhotoBeen;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.PickerView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LinJ
 * @ClassName: CameraAty
 * @Description: 自定义照相机类
 * @date 2014-12-31 上午9:44:25
 */
public class ClinicalPhotographActivity extends Activity implements View.OnClickListener, CameraContainer.TakePictureListener {
    public final static String TAG = "CameraAty";
    private String mSaveRoot;
    private CameraContainer mContainer;
    private ImageButton mCameraShutterButton;
    private ImageView mFlashView;
    private ImageButton mSwitchModeButton;
    private ImageView mSwitchCameraView;
    private View mHeaderBar;
    private LinearLayout ll_clinical_ok,ll_clinical_cancel;


    private RecyclerView rec_image;
    private PhotoAdapter photoAdapter;
    private ArrayList<Bitmap> photoes;
    private String text = "处方";

    private File media;
    private String type;
    private String categoryid;

    private UpdataPhotoBeen updataPhotoBeen;
    private List<UpdataPhotoBeen> updataPhotoBeens;
    private List<File> files;
    private int photoCount;

    private PickerView pickerview_picture_tag;
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    Intent intent = new Intent();
                    intent.setClass(ClinicalPhotographActivity.this, ClinicalCreateActivity.class);
                    intent.putExtra("updataPhotoBeens", (Serializable) updataPhotoBeens);
                    startActivity(intent);
                    finish();
                    break;
                case REQUEST_FAILURE:
                    //原页面GONE掉，提示网络不好的页面出现
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.camera);

        mHeaderBar = findViewById(R.id.camera_header_bar);
        mContainer = (CameraContainer) findViewById(R.id.container);
        ll_clinical_ok = (LinearLayout) findViewById(R.id.ll_clinical_ok);
        ll_clinical_cancel = (LinearLayout) findViewById(R.id.ll_clinical_cancel);
        mCameraShutterButton = (ImageButton) findViewById(R.id.btn_shutter_camera);
        mSwitchCameraView = (ImageView) findViewById(R.id.btn_switch_camera);
        mFlashView = (ImageView) findViewById(R.id.btn_flash_mode);
        pickerview_picture_tag = (PickerView) findViewById(R.id.pickerview_picture_tag);

        rec_image = (RecyclerView) findViewById(R.id.rec_image);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rec_image.setLayoutManager(linearLayoutManager);
        photoAdapter = new PhotoAdapter(this);
        rec_image.setAdapter(photoAdapter);
        photoes = new ArrayList<Bitmap>();
        List<String> data = new ArrayList<String>();
        updataPhotoBeens = new ArrayList<UpdataPhotoBeen>();
        type = 0 + "";
        categoryid = 0 + "";
        photoCount = 0;

        data.add("其他");
        data.add("舌象");
        data.add("处方");
        data.add("患处");
        pickerview_picture_tag.setData(data);
        pickerview_picture_tag.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                ClinicalPhotographActivity.this.text = text;
            }
        });

        ll_clinical_ok.setOnClickListener(this);
        ll_clinical_cancel.setOnClickListener(this);
        mCameraShutterButton.setOnClickListener(this);
        mFlashView.setOnClickListener(this);
        mSwitchCameraView.setOnClickListener(this);

        mSaveRoot = "test";
        mContainer.setRootPath(mSaveRoot);

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.btn_shutter_camera:
                if(photoCount < 5){
                    mCameraShutterButton.setClickable(false);
                    mContainer.takePicture(this);
                }else{
                    ToastUtil.show("一次只能拍5张照片");
                }

                break;
            case R.id.ll_clinical_ok:
                String thumbFolder = FileOperateUtil.getFolderPath(this, FileOperateUtil.TYPE_IMAGE, mSaveRoot);
                files = FileOperateUtil.listFiles(thumbFolder, ".jpg");
                if (files == null || files.size() == 0) {
                    ToastUtil.show("没有照片");
                } else {
                    for (File media : files) {
                        saveClinicalMultiMedia(media, type, categoryid);
                    }
                }


                break;
            case R.id.ll_clinical_cancel:
              finish();
                break;
            case R.id.btn_flash_mode:
                if (mContainer.getFlashMode() == CameraView.FlashMode.ON) {
                    mContainer.setFlashMode(CameraView.FlashMode.OFF);
                    mFlashView.setImageResource(R.drawable.btn_flash_off);
                } else if (mContainer.getFlashMode() == CameraView.FlashMode.OFF) {
                    mContainer.setFlashMode(CameraView.FlashMode.AUTO);
                    mFlashView.setImageResource(R.drawable.btn_flash_auto);
                } else if (mContainer.getFlashMode() == CameraView.FlashMode.AUTO) {
                    mContainer.setFlashMode(CameraView.FlashMode.TORCH);
                    mFlashView.setImageResource(R.drawable.btn_flash_torch);
                } else if (mContainer.getFlashMode() == CameraView.FlashMode.TORCH) {
                    mContainer.setFlashMode(CameraView.FlashMode.ON);
                    mFlashView.setImageResource(R.drawable.btn_flash_on);
                }
                break;
            case R.id.btn_switch_camera:
                mContainer.switchCamera();
                break;
//            case R.id.btn_other_setting:
//                mContainer.setWaterMark();
//                break;
            default:
                break;
        }
    }


    @Override
    public void onTakePictureEnd(Bitmap thumBitmap) {
        mCameraShutterButton.setClickable(true);
    }

    @Override
    public void onAnimtionEnd(Bitmap bm, boolean isVideo) {
        if (bm != null) {
            //生成缩略图
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bm, 213, 213);
            photoes.add(thumbnail);
            photoAdapter.updateAdapter(photoes, text);
            photoCount ++;
        }
    }

    public void saveClinicalMultiMedia(File media, String type, String categoryid) {
        NetApi.saveClinicalMultiMedia(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
//                LogUtil.e(result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalPhotographActivity.this)) {
                    return;
                }
                updataPhotoBeen = JsonUtil.json2Bean(result, UpdataPhotoBeen.class);
//                LogUtil.e(updataPhotoBeen.data.toString());
                updataPhotoBeens.add(updataPhotoBeen);
                if (files.size() == updataPhotoBeens.size()) {
                    handler.sendEmptyMessage(REQUEST_SUCCESS);
                    String thumbFolder = FileOperateUtil.getFolderPath(ClinicalPhotographActivity.this, FileOperateUtil.TYPE_THUMBNAIL, mSaveRoot);
                    List<File> smallfiles = FileOperateUtil.listFiles(thumbFolder, ".jpg");
                    for (File file : smallfiles) {
                        FileOperateUtil.deleteThumbFile(file.getAbsolutePath(), ClinicalPhotographActivity.this);
                    }
                    for (File file : files) {
                        FileOperateUtil.deleteThumbFile(file.getAbsolutePath(), ClinicalPhotographActivity.this);
                    }

                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, media, type, categoryid);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}