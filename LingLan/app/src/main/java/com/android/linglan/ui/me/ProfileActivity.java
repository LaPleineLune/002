package com.android.linglan.ui.me;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ProfileBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.CameraUtil;
import com.android.linglan.utils.FaceFileUtil;
import com.android.linglan.utils.FaceImageUtil;
import com.android.linglan.utils.FaceUIUtil;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.ImageUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.StorageManager;
import com.android.linglan.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by LeeMy on 2016/1/6 0006.
 * 个人信息
 */
public class ProfileActivity extends BaseActivity {
    protected static final int REQUEST_SUCCESS = 0;
    protected static final int REQUEST_FAIL = 1;

    private static final int REQUEST_CODE_AREA = 2;
    private static final int REQUEST_CROP_FINISH = 3;
    private static final int REQUEST_TAKE_PHOTO = 4;
    private static final int REQUEST_NICK_NAME = 5;
    private static final int REQUEST_NAME = 6;
    private static final int REQUEST_ABOUT = 7;
    private static final int REQUEST_COMPANY = 8;
    private static final int REQUEST_PICTURE = 9;
    private static final int REQUEST_GALLERY = 10;
    private static final int REQUEST_CLIP_PIC_RESULT = 11;
    public static final int AVATAR_WIDTH = 120;
    public static final int AVATAR_HEIGHT = 120;

    private static final String ROOT_DIR = "linglan";
    private String captureAvatarPath;

    private RelativeLayout nickname_item;
    private RelativeLayout name_item;
    private RelativeLayout description_item;
    private RelativeLayout company_item;
    private ImageView avatarView;
    private TextView nickname, description, company;
    private String alias = "";
    private TextView name;
    private String userName = "";
    private TextView belonging;
    private String cityName = "";
    private String about = "";
    private String companyName = "";
    private Intent intent;
    //    public ArrayList<ProfileBean.ProfileData> data;
    public ProfileBean.ProfileData data = null;

    private PopupWindow popupWindow;
    private View popView;
    private RelativeLayout rl_profile;
    private Button bt_profile_picture;
    private Button bt_profile_gallery;
    private Button bt_profile_cancel;
    private File mImageFile;
    private String mClipImagePath;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProfileBean.ProfileData data = (ProfileBean.ProfileData) msg.getData().getSerializable("data");
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    setData(data);
                    break;
                case REQUEST_FAIL:

                    break;
            }
        }
    };

    private void setData(ProfileBean.ProfileData data) {
        if (!TextUtils.isEmpty(data.face)) {// !data.face.trim().isEmpty() && !data.face.trim().equals("")
            SharedPreferencesUtil.saveString("face", data.face);// 头像
            ImageUtil.loadImageAsync(avatarView, data.face);
        }

        if (TextUtils.isEmpty(data.alias)) {// data.alias.trim().isEmpty()
            SharedPreferencesUtil.removeValue("alias");// 用户昵称
            alias = getString(R.string.default_setting);
        } else {
            SharedPreferencesUtil.saveString("alias", data.alias);// 用户昵称
            alias = data.alias;
        }
        nickname.setText(alias);

        if (TextUtils.isEmpty(data.name)) {// data.name.trim().isEmpty()
            userName = getString(R.string.default_setting);
        } else {
            userName = data.name;
        }
        name.setText(userName);

        if (TextUtils.isEmpty(data.city)) {// data.city.trim().isEmpty()
            cityName = getString(R.string.default_setting);
        } else {
            cityName = data.city;
        }
        belonging.setText(cityName);

        if (TextUtils.isEmpty(data.about)) {// data.about.trim().isEmpty()
            about = getString(R.string.default_setting);
        } else {
            about = data.about;
        }
        description.setText(about);

        if (TextUtils.isEmpty(data.company)) {// data.company.trim().isEmpty()
            companyName = getString(R.string.default_setting);
        } else {
            companyName = data.company;
        }
        company.setText(companyName);

    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_profile);
        popView = LayoutInflater.from(this).inflate(R.layout.popupview_profile, null);
    }

    @Override
    protected void initView() {
        findViewById(R.id.avatar_editor).setOnClickListener(this);
        description_item = (RelativeLayout) findViewById(R.id.description_item);
        nickname_item = (RelativeLayout) findViewById(R.id.nickname_item);
        name_item = (RelativeLayout) findViewById(R.id.name_item);
        company_item = (RelativeLayout) findViewById(R.id.company_item);
        avatarView = (ImageView) findViewById(R.id.avatar);
        findViewById(R.id.belonging_item).setOnClickListener(this);
        nickname = (TextView) findViewById(R.id.nickname);
        description = (TextView) findViewById(R.id.description);
        company = (TextView) findViewById(R.id.company);
        name = (TextView) findViewById(R.id.name);
        belonging = (TextView) findViewById(R.id.belonging);
        rl_profile = (RelativeLayout) popView.findViewById(R.id.rl_profile);
        bt_profile_picture = (Button) popView.findViewById(R.id.bt_profile_picture);
        bt_profile_gallery = (Button) popView.findViewById(R.id.bt_profile_gallery);
        bt_profile_cancel = (Button) popView.findViewById(R.id.bt_profile_cancel);
    }

    @Override
    protected void initData() {
        setTitle("账户信息", "");
        popupWindow = new PopupWindow(this);
        showArea();
        getUserInfo();
    }

    @Override
    protected void setListener() {
        description_item.setOnClickListener(this);
        nickname_item.setOnClickListener(this);
        name_item.setOnClickListener(this);
        company_item.setOnClickListener(this);
        rl_profile.setOnClickListener(this);
        bt_profile_picture.setOnClickListener(this);
        bt_profile_gallery.setOnClickListener(this);
        bt_profile_cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        intent = new Intent();
        switch (v.getId()) {
            case R.id.avatar_editor:
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setWidth(getWindowManager().getDefaultDisplay().getWidth());
                popupWindow.setHeight(getWindowManager().getDefaultDisplay().getHeight());
//                popupWindow.setAnimationStyle(R.style.AnimationPreview);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setFocusable(true);// 响应回退按钮事件
                popupWindow.setContentView(popView);

                int[] location = new int[2];
                v.getLocationOnScreen(location);
                popupWindow.showAtLocation(v, Gravity.BOTTOM, location[0], location[1] - popupWindow.getHeight());

                break;
            case R.id.bt_profile_picture:// 拍照
                popupWindow.dismiss();
                takePhoto();
                break;
            case R.id.bt_profile_gallery:// 图库
                popupWindow.dismiss();
                openPhotoLib();
                break;
            case R.id.rl_profile:
            case R.id.bt_profile_cancel:
                popupWindow.dismiss();
                break;
            case R.id.nickname_item:
                intent.putExtra("nameFlag", "用户昵称");
                intent.putExtra("name", (nickname.getText().toString().equals("未设置") ? "" : nickname.getText().toString()));
                intent.setClass(ProfileActivity.this, ChangeNameActivity.class);
                startActivityForResult(intent, REQUEST_NICK_NAME);
                break;
            case R.id.name_item:
                intent.putExtra("nameFlag", "真实姓名");
                intent.putExtra("name", (name.getText().toString().equals("未设置") ? "" : name.getText().toString()));
                intent.setClass(ProfileActivity.this, ChangeNameActivity.class);
                startActivityForResult(intent, REQUEST_NAME);
                break;
            case R.id.description_item:
                intent.putExtra("about", description.getText().toString().equals("未设置") ? "" : description.getText().toString());
                intent.setClass(ProfileActivity.this, DescriptionActivity.class);
                startActivityForResult(intent, REQUEST_ABOUT);
                break;
            case R.id.belonging_item:
//                intent.setClass(this, CityActivity.class);
                intent.setClass(this, CountryActivity.class);
                startActivityForResult(intent, REQUEST_CODE_AREA);
                break;
            case R.id.company_item:
                intent.putExtra("nameFlag", "工作单位");
                intent.putExtra("name", (company.getText().toString().equals("未设置") ? "" : company.getText().toString()));
                intent.setClass(ProfileActivity.this, ChangeNameActivity.class);
                startActivityForResult(intent, REQUEST_COMPANY);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(data == null)
//            return;

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case REQUEST_NICK_NAME:
                    alias = (String) data.getSerializableExtra("nickname");
                    showArea();
                    break;
                case REQUEST_NAME:
                    userName = (String) data.getSerializableExtra("userName");
                    showArea();
                    break;
                case REQUEST_ABOUT:
                    about = (String) data.getSerializableExtra("about");
                    showArea();
                    break;
                case REQUEST_CODE_AREA:
                    cityName = (String) data.getSerializableExtra("cityName");
                    showArea();
                    break;
                case REQUEST_COMPANY:
                    companyName = (String) data.getSerializableExtra("companyName");
                    showArea();
                    break;
                case REQUEST_TAKE_PHOTO:
                    if (!TextUtils.isEmpty(captureAvatarPath)) {
                        File photoFile = new File(captureAvatarPath);
                        if (photoFile != null) {
                            startPhotoZoom(Uri.fromFile(photoFile));
                        }
                    }
                    break;
                case REQUEST_CROP_FINISH:
                    if (data == null) {
                        return;
                    }
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        if (photo != null) {
                            avatarView.setImageBitmap(photo);
                            uploadHeadImage(photo);
                        }
                    }
                    break;
                case REQUEST_PICTURE:// 拍照
                    takePhotoResult();
                    break;
                case REQUEST_GALLERY:// 图库
                    picLibResult(data);
                    break;
                case REQUEST_CLIP_PIC_RESULT:// 剪切
                    clipPicResult(data);
                    break;

                default:
                    break;
            }
        }
    }

    private void showArea() {

        if (alias.trim().isEmpty()) {
            alias = getString(R.string.default_setting);
        }
        nickname.setText(alias);

        if (userName.trim().isEmpty()) {
            userName = getString(R.string.default_setting);
        }
        name.setText(userName);

        if (about.trim().isEmpty()) {
            about = getString(R.string.default_setting);
        }
        description.setText(about);

        if (cityName.trim().isEmpty()) {
            cityName = getString(R.string.default_setting);
        }
        belonging.setText(cityName);

        if (companyName.trim().isEmpty()) {
            companyName = getString(R.string.default_setting);
        }
        company.setText(companyName);
    }


    private void takePhotoResult() {
        //图片是否需要旋转
        mImageFile = new File(Environment.getExternalStorageDirectory()
                + "/head.jpg");

        int degree = FaceImageUtil.getBitmapDegree(mImageFile.getAbsolutePath());
        if (degree != 0) {
            Bitmap bitmap = FaceImageUtil.getScaledBitmap(mImageFile.getAbsolutePath(), FaceUIUtil.getScreenWidth(), FaceUIUtil.getScreenHeight());
            bitmap = FaceImageUtil.rotateBitmapByDegree(bitmap, degree);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(mImageFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(this,
                ClipPictureActivity.class);
        intent.putExtra(ClipPictureActivity.TAG_URL, mImageFile.getAbsolutePath());
        startActivityForResult(intent, REQUEST_CLIP_PIC_RESULT);
    }

    private void picLibResult(Intent data) {
        Uri selectedImage = data.getData();
        String picturePath;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
        } else {
            picturePath = selectedImage.toString();
        }
        if (picturePath.startsWith("file://")) {
            picturePath = picturePath.substring(7);
        }
        Intent intent = new Intent(this, ClipPictureActivity.class);
        intent.putExtra(ClipPictureActivity.TAG_URL, picturePath);
        startActivityForResult(intent, REQUEST_CLIP_PIC_RESULT);
    }

    private void clipPicResult(Intent data) {
        if (data == null) {
            return;
        }
        mClipImagePath = data.getStringExtra(ClipPictureActivity.TAG_CLIPED_URL);
        if (mClipImagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(mClipImagePath);
            if (bitmap != null) {
                avatarView.setImageBitmap(bitmap);
                uploadHeadImage(bitmap);
            } else {
                ToastUtil.show("头像上传失败!");
            }
        }
    }

    private void takePhoto() {

        if (!verifyPermission("android.permission.CAMERA")) {
            ToastUtil.show("摄像头打开失败，请检查设备并开放权限");
            return;
        }

        mImageFile = FaceFileUtil.getImageFile();
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                "head.jpg")));

        startActivityForResult(intent, REQUEST_PICTURE);
    }

    private void openPhotoLib() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_GALLERY);
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
        NetApi.getUserPhotoUpdate(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getUserPhotoUpdate????????????result=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ProfileActivity.this)) {
                    return;
                }

                try {
                    JSONObject json = new JSONObject(result);
                    JSONObject data = json.getJSONObject("data");
                    String url = data.getString("url");
                    SharedPreferencesUtil.saveString("face", url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                SharedPreferencesUtil.saveString("avatar",
//                        avatar.headpath.W180);
//                        LogUtil.e("getUserPhotoUpdate????????????result=" + result);
//                Avatar avatar = JsonUtil.json2Bean(result, Avatar.class);
//                if (avatar != null && "1".equals(avatar.code)) {
//                    SharedPreferencesUtil.saveString("avatar",
//                            avatar.headpath.W180);
//                }
            }

            @Override
            public void onFailure(String message) {
            }
        }, croppedFile);

    }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", AVATAR_WIDTH);
        intent.putExtra("outputY", AVATAR_HEIGHT);
        intent.putExtra("return-data", true);
        try {
            startActivityForResult(intent, REQUEST_CROP_FINISH);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean verifyPermission(String permission) {
        return CameraUtil.isCameraEnable()
                && PackageManager.PERMISSION_GRANTED == checkCallingOrSelfPermission(permission);
    }

//    private void dispatchTakePictureIntent() {
//        if (!verifyPermission("android.permission.CAMERA")) {
//            ToastUtil.show("摄像头打开失败，请检查设备并开放权限");
//            return;
//        }
//
//        Intent captureIntent = new Intent(
//                MediaStore.ACTION_IMAGE_CAPTURE);
//        File captureFile;
//        try {
//            captureFile = createImageFile();
//            captureIntent.putExtra(
//                    MediaStore.EXTRA_OUTPUT,
//                    Uri.fromFile(captureFile));
//            captureAvatarPath = captureFile
//                    .getPath();
//            startActivityForResult(captureIntent,
//                    REQUEST_TAKE_PHOTO);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "avatar";
        File image =
                File.createTempFile(
                        imageFileName,
                        "jpg",
                        new File(getExternalContentDirectory(StorageManager.getInstance(this)
                                .getExternalStorageDirectory()))
                );
        return image;
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

    //获取个人信息
    private void getUserInfo() {
        NetApi.getUserInfo(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getUserInfo=" + result);

                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ProfileActivity.this)) {
                    return;
                }
                ProfileBean profileBean = JsonUtil.json2Bean(result, ProfileBean.class);
//                if (profileBean.code.equals("0")) {
                data = profileBean.data;
                String face = profileBean.data.face;
                LogUtil.e("face =====" + face);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data);
                message.setData(bundle);// bundle传值，耗时，效率低
                handler.sendMessage(message);// 发送message信息
                message.what = REQUEST_SUCCESS;// 标志是哪个线程传数据
//                    handler.sendEmptyMessage(REQUEST_SUCCESS);
//                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

}
