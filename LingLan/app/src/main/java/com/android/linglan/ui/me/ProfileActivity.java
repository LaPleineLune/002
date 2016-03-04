package com.android.linglan.ui.me;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ProfileBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.ActionSheetUtil;
import com.android.linglan.utils.CameraUtil;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.StorageManager;
import com.android.linglan.utils.ToastUtil;
import com.roc.actionsheet.ActionSheet;
import com.soundcloud.android.crop.Crop;

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
    public static final int AVATAR_WIDTH = 120;
    public static final int AVATAR_HEIGHT = 120;

    private static final String ROOT_DIR = "linglan";
    private String captureAvatarPath;


    private RelativeLayout nickname_item;
    private RelativeLayout name_item;
    private RelativeLayout description_item;
    private RelativeLayout company_item;
    private ImageView avatarView;
    private TextView nickname;
    private String alias = "";
    private TextView name;
    private String userName = "";
    private TextView belonging;
    private String cityName = "";
    private String about = "";
    private String companyName = "";
    private Intent intent;
//    public ArrayList<ProfileBean.ProfileData> data;
    public ProfileBean.ProfileData data;

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
//        if (data.get(0).alias.trim().isEmpty()) {
        if (data.alias.trim().isEmpty()) {
            alias = getString(R.string.default_setting);
        } else {
            alias = data.alias;
        }
        nickname.setText(alias);

//        if (data.get(0).name.trim().isEmpty()) {
        if (data.name.trim().isEmpty()) {
            userName = getString(R.string.default_setting);
        } else {
            userName = data.name;
        }
        name.setText(userName);

//        if (data.get(0).city.trim().isEmpty()) {
        if (data.city.trim().isEmpty()) {
            cityName = getString(R.string.default_setting);
        } else {
            cityName = data.city;
        }
        belonging.setText(cityName);

        if (data.about.trim().isEmpty()) {
            about = getString(R.string.default_setting);
        } else {
            about = data.about;
        }
//        belonging.setText(cityName);

        if (data.company.trim().isEmpty()) {
            companyName = getString(R.string.default_setting);
        } else {
            companyName = data.company;
        }
//        belonging.setText(cityName);

    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_profile);
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
        name = (TextView) findViewById(R.id.name);
        belonging = (TextView) findViewById(R.id.belonging);
    }

    @Override
    protected void initData() {
        setTitle("账户信息", "");
        showArea();
        getUserInfo();
    }

    @Override
    protected void setListener() {
        description_item.setOnClickListener(this);
        nickname_item.setOnClickListener(this);
        name_item.setOnClickListener(this);
        company_item.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        intent = new Intent();
        switch (v.getId()) {
            case R.id.avatar_editor:
                ActionSheetUtil.show(this, new ActionSheet.MenuItemClickListener() {
                    @Override
                    public void onItemClick(int itemPosition) {
                        switch (itemPosition) {
                            case 0:
                                dispatchTakePictureIntent();
                                break;
                            case 1:
                                if (!Crop.pickImage(ProfileActivity.this)) {
                                    ToastUtil.show("头像上传失败!");
                                }
                                break;
                        }
                    }
                }, getString(R.string.take_picture), getString(R.string.pick_from_gallery));
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
                intent.putExtra("about", (data.about.equals("") ? "" : about));
                intent.setClass(ProfileActivity.this, DescriptionActivity.class);
                startActivityForResult(intent, REQUEST_ABOUT);
                break;
            case R.id.belonging_item:
                intent.setClass(this, CityActivity.class);
                startActivityForResult(intent, REQUEST_CODE_AREA);
                break;
            case R.id.company_item:
                intent.putExtra("nameFlag", "工作单位");
                intent.putExtra("name", (data.company.equals("") ? "" : companyName));
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
                    alias =  (String) data.getSerializableExtra("nickname");
                    showArea();
                    break;
                case REQUEST_NAME:
                    userName =  (String) data.getSerializableExtra("userName");
                    showArea();
                    break;
                case REQUEST_ABOUT:
                    about = (String) data.getSerializableExtra("about");
//                    showArea();
                    break;
                case REQUEST_CODE_AREA:
                    cityName =  (String) data.getSerializableExtra("cityName");
                    showArea();
                    break;
                case REQUEST_COMPANY:
                    companyName = (String) data.getSerializableExtra("companyName");
//                    showArea();
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
                case Crop.REQUEST_PICK:
                    beginCrop(data.getData());
                    break;
                case Crop.REQUEST_CROP:
                    handleCrop(resultCode, data);
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

        if (cityName.trim().isEmpty()) {
            cityName = getString(R.string.default_setting);
        }
        belonging.setText(cityName);

//        if (companyName.trim().isEmpty()) {
//            companyName = getString(R.string.default_setting);
//        }
//        belonging.setText(companyName);
    }

    private void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        new Crop(source).output(outputUri).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Crop.getOutput(result));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (bitmap != null) {
            avatarView.setImageBitmap(bitmap);
            uploadHeadImage(bitmap);
        } else {
            ToastUtil.show("头像上传失败!");
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

//        NetApi.uploadAvatar(croppedFile, new PasserbyClient.HttpCallback() {
//            @Override
//            public void onSuccess(String result) {
//                Avatar avatar = JsonUtil.json2Bean(result, Avatar.class);
//                if (avatar != null && "1".equals(avatar.code)) {
//                    SharedPreferencesUtil.saveString("avatar",
//                            avatar.headpath.W180);
//                }
//            }
//
//            @Override
//            public void onFailure(String message) {}
//        });
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

    private void dispatchTakePictureIntent() {
        if (!verifyPermission("android.permission.CAMERA")) {
            ToastUtil.show("摄像头打开失败，请检查设备并开放权限");
            return;
        }

        Intent captureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File captureFile;
        try {
            captureFile = createImageFile();
            captureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(captureFile));
            captureAvatarPath = captureFile
                    .getPath();
            startActivityForResult(captureIntent,
                    REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
                ProfileBean profileBean = JsonUtil.json2Bean(result, ProfileBean.class);
//                if (profileBean.code.equals("0")) {
                    data = profileBean.data;
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

    /**
     * 用户信息修改
     * @param alias 用户昵称(post)
     * @param name  真实姓名(post)
     * @param about  个人简介(post)
     * @param city   所在城市(post)
     * @param company 所在单位(post)
     * @param feature 个人特征
     */
    private void getUserInfoEdit(String alias, String name, String about, String city, String company, String feature){
        NetApi.getUserInfoEdit(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
            }

            @Override
            public void onFailure(String message) {

            }
        },alias,name,about,city,company,feature);
    }
}
