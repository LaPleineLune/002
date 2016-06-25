package com.android.linglan.ui.clinical;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.UpdataPhotoBeen;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.StorageManager;
import com.android.linglan.widget.ClipView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现思路：
 * 截取屏幕的截图，然后在该截图里截取矩形框里的区域
 */
public class ClinicalClassifyPictureActivity extends Activity implements OnClickListener {
    public static final String TAG_URL = "image_url";
    public static final String TAG_CLIPED_URL = "clip_image_url";
    private static final String ROOT_DIR = "linglan";
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;

    private ImageView srcPic;
    private ClipView clipview;

    int statusBarHeight = 0;
    int titleBarHeight = 0;
    private String mImageUrl;

    // These matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    private TextView tv_bottom_ok;
    int mode = NONE;
    private Bitmap srcBitmap;
    private String type;
    private String categoryid;

    // Remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;

    private UpdataPhotoBeen updataPhotoBeen;
    private List<UpdataPhotoBeen> updataPhotoBeens;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:

                    Intent intent = new Intent();
                    intent.setClass(ClinicalClassifyPictureActivity.this, ClinicalCreateActivity.class);
                    intent.putExtra("updataPhotoBeens", (Serializable) updataPhotoBeens);
                    startActivity(intent);
                    finish();

                    break;
                case REQUEST_FAILURE:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_clinical_classify_photo);

        mImageUrl = getIntent().getStringExtra(TAG_URL);
        srcPic = (ImageView) findViewById(R.id.src_pic);
        tv_bottom_ok = (TextView) findViewById(R.id.tv_bottom_ok);

        srcBitmap = BitmapFactory.decodeFile(mImageUrl);
        srcPic.setImageBitmap(srcBitmap);

        updataPhotoBeens = new ArrayList<UpdataPhotoBeen>();

        tv_bottom_ok.setOnClickListener(this);
        type = 0 + "";
        categoryid = 0 + "";
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bottom_ok:
                uploadHeadImage(srcBitmap);
                break;

            default:
                break;
        }
    }

    private void uploadHeadImage(Bitmap avatarBitmap) {

        File croppedFile;

        OutputStream fileOutStream;
        croppedFile = new File(getExternalContentDirectory(StorageManager.getInstance(this)
                .getExternalStorageDirectory()), "cropped_avatar.jpg");
        try {
            if (avatarBitmap != null) {
                fileOutStream = new FileOutputStream(croppedFile);
                avatarBitmap.compress(Bitmap.CompressFormat.JPEG,
                        95, fileOutStream);
                fileOutStream.flush();
                fileOutStream.close();
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        saveClinicalMultiMedia(croppedFile,type, categoryid);
    }

    public void saveClinicalMultiMedia(File media, String type, String categoryid) {
        NetApi.saveClinicalMultiMedia(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
//                LogUtil.e(result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalClassifyPictureActivity.this)) {
                    return;
                }
                updataPhotoBeen = JsonUtil.json2Bean(result, UpdataPhotoBeen.class);
                updataPhotoBeens.add(updataPhotoBeen);
                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {

            }
        }, media, type, categoryid);
    }

    private static String getExternalContentDirectory(String storageDirectory) {
        if (!TextUtils.isEmpty(storageDirectory)) {
            // make the absolute path (lowercase the enum value)
            String content = storageDirectory + "/" + ROOT_DIR + "/";

            File contentFile = new File(content);
            if (!contentFile.exists()) {
                if (!contentFile.mkdirs()) {
                    return null;
                }
            }
            return content;
        } else {
            return null;
        }
    }

    private Bitmap getClipedBitmap() {
        getBarHeight();
        Bitmap screenShoot = takeScreenShot();
        clipview = (ClipView) this.findViewById(R.id.clipview);
        Bitmap finalBitmap = Bitmap.createBitmap(screenShoot, clipview.getTopX(), clipview.getTopY() + titleBarHeight + statusBarHeight, clipview.VIEW_WIDTH, clipview.VIEW_HEIGHT);

        return finalBitmap;
    }

    private void getBarHeight() {
        Rect frame = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;

        int contenttop = this.getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT).getTop();
        titleBarHeight = contenttop - statusBarHeight;

    }

    private Bitmap takeScreenShot() {
        View view = this.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

}