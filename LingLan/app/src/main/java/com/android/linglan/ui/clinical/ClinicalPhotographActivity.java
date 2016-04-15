package com.android.linglan.ui.clinical;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.camerautils.CameraContainer;
import com.android.linglan.camerautils.CameraView;
import com.android.linglan.camerautils.FileOperateUtil;
import com.android.linglan.camerautils.FilterImageView;
import com.android.linglan.ui.R;

import java.io.File;
import java.util.List;

/**
 * @author LinJ
 * @ClassName: CameraAty
 * @Description: 自定义照相机类
 * @date 2014-12-31 上午9:44:25
 */
public class ClinicalPhotographActivity extends Activity implements View.OnClickListener, CameraContainer.TakePictureListener {
    public final static String TAG = "CameraAty";
    private boolean mIsRecordMode = false;
    private String mSaveRoot;
    private CameraContainer mContainer;
    private FilterImageView mThumbView;
    private ImageButton mCameraShutterButton;
    private ImageButton mRecordShutterButton;
    private ImageView mFlashView;
    private ImageButton mSwitchModeButton;
    private ImageView mSwitchCameraView;
    private ImageView mSettingView;
    private ImageView mVideoIconView;
    private View mHeaderBar;
    private TextView tv_prescription,tv_tongue_picture,tv_affected_part,tv_other;
    private boolean isRecording = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.camera);

        mHeaderBar = findViewById(R.id.camera_header_bar);
        mContainer = (CameraContainer) findViewById(R.id.container);
//        mThumbView = (FilterImageView) findViewById(R.id.btn_thumbnail);
//        mVideoIconView = (ImageView) findViewById(R.id.videoicon);
        mCameraShutterButton = (ImageButton) findViewById(R.id.btn_shutter_camera);
//        mRecordShutterButton = (ImageButton) findViewById(R.id.btn_shutter_record);
        mSwitchCameraView = (ImageView) findViewById(R.id.btn_switch_camera);
        mFlashView = (ImageView) findViewById(R.id.btn_flash_mode);
//        mSwitchModeButton = (ImageButton) findViewById(R.id.btn_switch_mode);
//        mSettingView = (ImageView) findViewById(R.id.btn_other_setting);
        tv_prescription = (TextView) findViewById(R.id.tv_prescription);
        tv_tongue_picture = (TextView) findViewById(R.id.tv_tongue_picture);
        tv_affected_part = (TextView) findViewById(R.id.tv_affected_part);
        tv_other = (TextView) findViewById(R.id.tv_other);


//        mThumbView.setOnClickListener(this);
        mCameraShutterButton.setOnClickListener(this);
//        mRecordShutterButton.setOnClickListener(this);
        mFlashView.setOnClickListener(this);
//        mSwitchModeButton.setOnClickListener(this);
        mSwitchCameraView.setOnClickListener(this);
//        mSettingView.setOnClickListener(this);

        tv_prescription.setOnClickListener(this);
        tv_tongue_picture.setOnClickListener(this);
        tv_affected_part.setOnClickListener(this);
        tv_other.setOnClickListener(this);

        mSaveRoot = "test";
        mContainer.setRootPath(mSaveRoot);
//        initThumbnail();

        tv_prescription.setTextColor(this.getResources().getColor(R.color.blue));

//        tv_prescription.setTextSize(this.getResources().getDimension(R.dimen.sp18));
        tv_prescription.setTextSize(18);

        Drawable rightDrawable = getResources().getDrawable(R.drawable.point);
        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
        tv_prescription.setCompoundDrawables(null, rightDrawable, null, null);

    }


    /**
     * 加载缩略图
     */
    private void initThumbnail() {
        String thumbFolder = FileOperateUtil.getFolderPath(this, FileOperateUtil.TYPE_THUMBNAIL, mSaveRoot);
        List<File> files = FileOperateUtil.listFiles(thumbFolder, ".jpg");
        if (files != null && files.size() > 0) {
            Bitmap thumbBitmap = BitmapFactory.decodeFile(files.get(0).getAbsolutePath());
            if (thumbBitmap != null) {
//                mThumbView.setImageBitmap(thumbBitmap);
                //视频缩略图显示播放图案
                if (files.get(0).getAbsolutePath().contains("video")) {
                    mVideoIconView.setVisibility(View.VISIBLE);
                } else {
                    mVideoIconView.setVisibility(View.GONE);
                }
            }
        } else {
//            mThumbView.setImageBitmap(null);
            mVideoIconView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.btn_shutter_camera:
                mCameraShutterButton.setClickable(false);
                mContainer.takePicture(this);
                break;
//            case R.id.btn_thumbnail:
////                startActivity(new Intent(this, AlbumAty.class));
//                break;
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
//            case R.id.btn_switch_mode:
//                if (mIsRecordMode) {
//                    mSwitchModeButton.setImageResource(R.drawable.ic_switch_camera);
//                    mCameraShutterButton.setVisibility(View.VISIBLE);
//                    mRecordShutterButton.setVisibility(View.GONE);
//                    //拍照模式下显示顶部菜单
//                    mHeaderBar.setVisibility(View.VISIBLE);
//                    mIsRecordMode = false;
//                    mContainer.switchMode(0);
//                    stopRecord();
//                } else {
//                    mSwitchModeButton.setImageResource(R.drawable.ic_switch_video);
//                    mCameraShutterButton.setVisibility(View.GONE);
//                    mRecordShutterButton.setVisibility(View.VISIBLE);
//                    //录像模式下隐藏顶部菜单
//                    mHeaderBar.setVisibility(View.GONE);
//                    mIsRecordMode = true;
//                    mContainer.switchMode(5);
//                }
//                break;
//            case R.id.btn_shutter_record:
//                if (!isRecording) {
//                    isRecording = mContainer.startRecord();
//                    if (isRecording) {
//                        mRecordShutterButton.setBackgroundResource(R.drawable.btn_shutter_recording);
//                    }
//                } else {
//                    stopRecord();
//                }
//                break;
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


    private void stopRecord() {
        mContainer.stopRecord(this);
        isRecording = false;
//        mRecordShutterButton.setBackgroundResource(R.drawable.btn_shutter_record);
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
//            mThumbView.setImageBitmap(thumbnail);
//            if (isVideo)
//                mVideoIconView.setVisibility(View.VISIBLE);
//            else {
//                mVideoIconView.setVisibility(View.GONE);
//            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}