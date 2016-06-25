package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.linglan.adapter.clinical.CreatePhotoAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.camerautils.DisplayImageOptions;
import com.android.linglan.camerautils.ImageLoader;
import com.android.linglan.camerautils.RoundedBitmapDisplayer;
import com.android.linglan.fragment.ClinicalFragment;
import com.android.linglan.http.GsonTools;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalMouldBean;
import com.android.linglan.http.bean.UpdataPhotoBeen;
import com.android.linglan.ui.R;
import com.android.linglan.utils.DateTimePickDialogUtil;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.TimeStampConversionUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.android.linglan.widget.AlertDialoginter;
import com.android.linglan.widget.AlertDialogs;
import com.android.linglan.widget.SyLinearLayoutManager;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 新建病历
 */
public class ClinicalCreateActivity extends BaseActivity implements AlertDialoginter {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    private static final int REQUEST_PICTURE = 3;
    private static final int REQUEST_SUCCESS_EMPTY = 4;
    private Intent intent = null;
    private LinearLayout ll_clinical_create;
    private LinearLayout ll_no_network;
    private LinearLayout ll_visit_time;
    private LinearLayout ll_create_patient;
    private LinearLayout ll_clinical_mould_details;
    private LinearLayout ll_clinical_mould;
    private Button bt_patient_select;
    private TextView tv_visit_time;
    private TextView tv_create_spread;
    private TextView tv_create_retract;
    private TextView tv_clinical_mould, tv_clinical_classify;
    private RadioGroup rdg_patient_sex;
    private RadioButton rdb_patient_boy;
    private RadioButton rdb_patient_girl;
    private String sexType = "";
    private EditText edt_patient_name;
    private EditText edt_patient_age;
    private EditText edt_patient_age_month;
    private EditText edt_patient_phone;
    private EditText edt_disease_generalization;
    private EditText edt_pathography;
    private String mSaveRoot;
    private ImageLoader mImageLoader;
    private static DisplayImageOptions mOptions;
    private RecyclerView rec_create_image;
    private CreatePhotoAdapter createPhotoAdapter;
    private String initDateTime = "";
    private String patient = "";// 患者信息
    private String course = "";// 病程
    private List<UpdataPhotoBeen> updataPhotoBeens;
    public List<UpdataPhotoBeen> oldUpdataPhotoBeens = new ArrayList<UpdataPhotoBeen>();
    private int alertflag = 1000;
    private String[] nameString;
    private Integer[] idInteger;
    private ClinicalMouldBean clinicalMouldBean;
    private ArrayList<String> mSelectPath;
    private StringBuilder sb = new StringBuilder();
    private int flag;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ll_clinical_create.setVisibility(View.VISIBLE);
                    ll_clinical_mould_details.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    edt_pathography.setVisibility(View.GONE);

                    if (clinicalMouldBean != null && clinicalMouldBean.data != null && clinicalMouldBean.data.size() != 0) {
                        edt_pathography.setVisibility(View.GONE);
                        edt_pathography.setText(null);
                        int i = 0;
                        idInteger = new Integer[clinicalMouldBean.data.size()];
                        ll_clinical_mould_details.removeAllViews();
                        for (ClinicalMouldBean.ClinicalMouldData clinicalMouldData : clinicalMouldBean.data) {
                            LinearLayout clinical_mould = new LinearLayout(ClinicalCreateActivity.this);
                            TextView title = new TextView(ClinicalCreateActivity.this);
                            EditText content = new EditText(ClinicalCreateActivity.this);
                            content.setId(i++);
                            idInteger[i - 1] = content.getId();
                            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    50
                            );

                            content.setBackgroundResource(R.color.white);

                            clinical_mould.setOrientation(LinearLayout.HORIZONTAL);
                            clinical_mould.addView(title);
                            clinical_mould.addView(content);
                            title.setText("[" + clinicalMouldData.templetname + "] :");
//                            title.setTextColor(Color.parseColor("#48558f"));
                            title.setTextColor(content.getResources().getColor(R.color.blue));

                            title.setTextSize(16);
                            content.setTextSize(16);
//                            if (flag == 0) {
//                                SharedPreferencesUtil.saveString(clinicalMouldBean.data.get(i - 1).templetname, null);
//                            }

                            if (SharedPreferencesUtil.getString(clinicalMouldBean.data.get(i - 1).templetname, null) != null) {
                                content.setText(SharedPreferencesUtil.getString(clinicalMouldBean.data.get(i - 1).templetname, null));
                            } else {
                                content.setHint("                                      ");
                            }
                            flag = 1;
                            ll_clinical_mould_details.addView(clinical_mould);
                        }
                    }
                    break;
                case REQUEST_FAILURE:
                    ll_clinical_create.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_PICTURE:
                    intent.setClass(ClinicalCreateActivity.this, ClinicalPictureClassifyActivity.class);
                    intent.putStringArrayListExtra("imgruls", mSelectPath);
//                    startActivity(intent);
                    startActivityForResult(intent, 101);
                    break;
                case REQUEST_SUCCESS_EMPTY:
                    edt_pathography.setVisibility(View.VISIBLE);
                    ll_clinical_mould_details.setVisibility(View.GONE);
                    break;
            }
        }
    };


    @Override
    protected void setView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.activity_clinical_create);

    }

    @Override
    protected void initView() {
        ll_clinical_create = (LinearLayout) findViewById(R.id.ll_clinical_create);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        ll_visit_time = (LinearLayout) findViewById(R.id.ll_visit_time);
        ll_create_patient = (LinearLayout) findViewById(R.id.ll_create_patient);
        ll_clinical_mould = (LinearLayout) findViewById(R.id.ll_clinical_mould);
        ll_clinical_mould_details = (LinearLayout) findViewById(R.id.ll_clinical_mould_details);
        tv_visit_time = (TextView) findViewById(R.id.tv_visit_time);
        bt_patient_select = (Button) findViewById(R.id.bt_patient_select);
        tv_create_spread = (TextView) findViewById(R.id.tv_create_spread);
        tv_create_retract = (TextView) findViewById(R.id.tv_create_retract);
        tv_clinical_mould = (TextView) findViewById(R.id.tv_clinical_mould);
        tv_clinical_classify = (TextView) findViewById(R.id.tv_clinical_classify);

        rdg_patient_sex = (RadioGroup) findViewById(R.id.rdg_patient_sex);
        rdb_patient_boy = (RadioButton) findViewById(R.id.rdb_patient_boy);
        rdb_patient_girl = (RadioButton) findViewById(R.id.rdb_patient_girl);
        edt_patient_name = (EditText) findViewById(R.id.edt_patient_name);
        edt_patient_age = (EditText) findViewById(R.id.edt_patient_age);
        edt_patient_age_month = (EditText) findViewById(R.id.edt_patient_age_month);
        edt_patient_phone = (EditText) findViewById(R.id.edt_patient_phone);
        edt_disease_generalization = (EditText) findViewById(R.id.edt_disease_generalization);
        edt_pathography = (EditText) findViewById(R.id.edt_pathography);
        rec_create_image = (RecyclerView) findViewById(R.id.rec_create_image);
    }

    @Override
    protected void initData() {
        setTitle("新建病历", "保存");
        flag = 0;
        intent = new Intent();

//        oldUpdataPhotoBeens = new ArrayList<UpdataPhotoBeen>();
        updataPhotoBeens = new ArrayList<UpdataPhotoBeen>();
//        oldUpdataPhotoBeens = (List<UpdataPhotoBeen>) getIntent().getSerializableExtra("updataPhotoBeens");
//        for (UpdataPhotoBeen updataPhotoBeen : updataPhotoBeens) {
//            LogUtil.e("updataPhotoBeens= " + updataPhotoBeen.toString());
//        }
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.save);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        Calendar c = Calendar.getInstance();
        initDateTime = c.get(Calendar.YEAR) + "." + (c.get(Calendar.MONTH) + 1) + "." + c.get(Calendar.DAY_OF_MONTH);
        tv_visit_time.setText(initDateTime);

        LinearLayoutManager linearLayoutManager = new SyLinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rec_create_image.setLayoutManager(linearLayoutManager);
        createPhotoAdapter = new CreatePhotoAdapter(this, oldUpdataPhotoBeens);
        rec_create_image.setAdapter(createPhotoAdapter);
        mSaveRoot = "test";

        mImageLoader = ImageLoader.getInstance(this);
        //设置网络图片加载参数
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder = builder
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .displayer(new RoundedBitmapDisplayer(0));
        mOptions = builder.build();
        getClinicalPersonMould();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ClinicalMouldActivity.isSave == 1) {
            ClinicalMouldActivity.isSave = 0;
            getClinicalPersonMould();
        }

        tv_clinical_classify.setOnClickListener(this);

    }

    @Override
    protected void setListener() {
        right.setOnClickListener(this);
        back.setOnClickListener(this);
        bt_patient_select.setOnClickListener(this);
        tv_create_spread.setOnClickListener(this);
        tv_create_retract.setOnClickListener(this);
        tv_clinical_mould.setOnClickListener(this);
        tv_clinical_classify.setOnClickListener(this);
        ll_no_network.setOnClickListener(this);
        ll_visit_time.setOnClickListener(this);
        rdg_patient_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rdb_patient_boy) {
//                    sexType = rdb_patient_boy.getText().toString();
                    sexType = "0";
                } else if (i == R.id.rdb_patient_girl) {
//                    sexType = rdb_patient_girl.getText().toString();
                    sexType = "1";
                } else {
                    sexType = "";
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right:
                MobclickAgent.onEvent(this, UmengBuriedPointUtil.ClinicalNewMedicalHistorySave);
                String patientAgeMonth = edt_patient_age_month.getText().toString().trim();
                if (!isEmptyContent()) {
                    ToastUtil.show("请填写病历内容");
                    return;
                } else if (!patientAgeMonth.equals("") && Integer.parseInt(patientAgeMonth) >= 13) {
                    ToastUtil.show("月份不能大于12个月");
                    return;
                } else {
                    LogUtil.e("patient" + patient + "course" + course);
                    clinicalCreate(patient, course);
                }
                break;
            case R.id.back:
                if (!isEmptyContent()) {
                    finish();
                    return;
                } else {
//                    ToastUtil.show("我要出来弹窗");
                    alertflag = 1;
                    AlertDialogs.alert(this, "提示", "不保存而退出此次编辑？", "取消", "退出");
//                    clinicalCreate(patient, course);
                }
                break;
            case R.id.bt_patient_select:
                if (isEmptyClinical()) {
                    intent.putExtra("course", course);
                }
                intent.setClass(this, PatientSelectActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_create_spread:
                ll_create_patient.setVisibility(View.VISIBLE);
                tv_create_spread.setVisibility(View.GONE);
                break;
            case R.id.tv_create_retract:
                ll_create_patient.setVisibility(View.GONE);
                tv_create_spread.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_clinical_classify:

                if (oldUpdataPhotoBeens != null && oldUpdataPhotoBeens.size() != 0) {
                    intent.setClass(ClinicalCreateActivity.this, EditClinicalPictureClassifyActivity.class);
                    intent.putExtra("oldUpdataPhotoBeens", (Serializable) oldUpdataPhotoBeens);
                    startActivityForResult(intent, 103);
                    LogUtil.e(oldUpdataPhotoBeens.size() + "");
                } else {
                    tv_clinical_classify.setOnClickListener(null);
//                    tv_clinical_classify.setTextColor(Color.parseColor("#666666"));
                }

                break;
            case R.id.tv_clinical_mould:
                if (!isEmptyClinicalPathography()) {
                    flag = 1;
                    intent.setClass(this, ClinicalMouldActivity.class);
//                    intent.putExtra("clinicalMouldBean", (Serializable) clinicalMouldBean);
                    startActivity(intent);
                } else {
                    alertflag = 2;
                    AlertDialogs.alert(this, "提示", "若添加模版，已有内容将被清空，请慎重选择", "取消", "确定");
                }

                break;
            case R.id.ll_no_network:
                initData();
                break;
            case R.id.ll_visit_time:
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        ClinicalCreateActivity.this, tv_visit_time.getText().toString());
                dateTimePicKDialog.dateTimePicKDialog(tv_visit_time);
                break;
        }
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LogUtil.d("onKeyUp keyCode: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isEmptyContent()) {
                finish();
            } else {
//                ToastUtil.show("我要出来弹窗");
                alertflag = 1;
                AlertDialogs.alert(this, "提示", "不保存而退出此次编辑？", "取消", "退出");
                //                    clinicalCreate(patient, course);
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);

    }

    private boolean isEmptyClinicalPathography() {
        String pathography = edt_pathography.getText().toString().trim();
        int count3 = 0;
        if (idInteger != null && idInteger.length != 0) {
            for (int i = 0; i < idInteger.length; i++) {
                String context = ((EditText) findViewById(idInteger[i])).getText().toString();
//                    LogUtil.e("有没有取到edittext里的内容啊" + context);
                if (context.length() != 0) {
                    SharedPreferencesUtil.saveString(clinicalMouldBean.data.get(i).templetname, context);
                    count3 = 1;
                }
            }
        }

//        if (TextUtils.isEmpty(pathography) && count3 == 0) {
        if (TextUtils.isEmpty(pathography)) {
            return false;
        }
        return true;
    }

    private boolean isEmptyClinical() {
        String visitTime = TimeStampConversionUtil.date2TimeStamp(tv_visit_time.getText().toString().trim(), "yyyy.MM.dd");
        String diseaseGeneralization = edt_disease_generalization.getText().toString().trim();
        String pathography = edt_pathography.getText().toString().trim();
        if (TextUtils.isEmpty(diseaseGeneralization) && TextUtils.isEmpty(pathography)) {
            return false;
        } else {
            // [{"feature":"病症","visittime":"就诊时间","mediaids":"多个图片以逗号隔开","pathography":{"模板ID(没有模板，默认ID为1)":"内容"，......}}]
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            Map<String, Object> coursemap = new HashMap<String, Object>();
            coursemap.put("feature", diseaseGeneralization);
            coursemap.put("visittime", visitTime);
//            LogUtil.e("时间" + visitTime);
            if (updataPhotoBeens != null) {
                StringBuilder sb = new StringBuilder();
                for (UpdataPhotoBeen updataPhotoBeen : updataPhotoBeens) {
                    if (!(null == updataPhotoBeen.data.mediaid || updataPhotoBeen.data.mediaid.equals("0"))) {
                        sb.append(updataPhotoBeen.data.mediaid);
                        sb.append(",");
                    }
                }
                if (sb.toString().length() > 0) {
                    String mediaids = sb.toString().substring(0, sb.toString().length() - 1);
//                    LogUtil.e("相片的id串=" + mediaids);
                    coursemap.put("mediaids", mediaids);
                }
            }
            Map<String, Object> pathographymap = new HashMap<String, Object>();

            pathographymap.put("1", "rose");
            pathographymap.put("2", "154546");
            coursemap.put("pathography", pathographymap);
            list.add(coursemap);
//                    list.add(map2);
            course = GsonTools.createGsonString(list);
//            LogUtil.e("我要看的coursemap数据11=" + course);
            return true;
        }
    }

    private boolean isEmptyContent() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> coursemap = new HashMap<String, Object>();
        Map<String, Object> pathographymap = new HashMap<String, Object>();
//        sb = new StringBuilder();
        int count = 0;

        String patientName = edt_patient_name.getText().toString().trim();
        String patientAge = edt_patient_age.getText().toString().trim();
        String patientAgeMonth = edt_patient_age_month.getText().toString().trim();
        String patientPhone = edt_patient_phone.getText().toString().trim();

        String visitTime = TimeStampConversionUtil.date2TimeStamp(tv_visit_time.getText().toString().trim(), "yyyy.MM.dd");
        String diseaseGeneralization = edt_disease_generalization.getText().toString().trim();
        String pathography = edt_pathography.getText().toString().trim();

        if (updataPhotoBeens != null) {
//            sb = new StringBuilder();
            for (UpdataPhotoBeen updataPhotoBeen : oldUpdataPhotoBeens) {
                if (!(null == updataPhotoBeen.data.mediaid || updataPhotoBeen.data.mediaid.equals("0"))) {
                    sb.append(updataPhotoBeen.data.mediaid);
                    sb.append(",");
                }
            }

            if (sb.toString().length() > 0) {
                String mediaids = sb.toString().substring(0, sb.toString().length() - 1);
                LogUtil.e("相片的id串=" + mediaids);
                coursemap.put("mediaids", mediaids);
            }
        }


        if (idInteger != null && idInteger.length != 0) {
            for (int i = 0; i < idInteger.length; i++) {
                String context = ((EditText) findViewById(idInteger[i])).getText().toString();
//                    LogUtil.e("有没有取到edittext里的内容啊" + context);
                if (context.length() != 0) {
                    pathographymap.put(clinicalMouldBean.data.get(i).templetid, context);
                    count = 1;
                } else {
                    pathographymap.put(clinicalMouldBean.data.get(i).templetid, "");
                }
            }
        } else {
            pathographymap.put(1 + "", pathography);
//                LogUtil.e("有没有取到edittext里的内容啊" + pathography);
        }
        coursemap.put("pathography", pathographymap);

        if (TextUtils.isEmpty(patientName) && TextUtils.isEmpty(patientAge) && TextUtils.isEmpty(patientAgeMonth)
                && TextUtils.isEmpty(patientPhone) && TextUtils.isEmpty(diseaseGeneralization) && TextUtils.isEmpty(pathography)
                && TextUtils.isEmpty(sexType) && count == 0 && sb.toString().length() == 0) {
            return false;
        } else {
            // {"patientname":"","sex":"","ageyear":"",agemonth":"","phone":""}
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("patientname", patientName);
            map.put("ageyear", patientAge);
            map.put("agemonth", patientAgeMonth);
            map.put("phone", patientPhone);
            map.put("sex", sexType);
            patient = GsonTools.createGsonString(map);
            LogUtil.e("我要看的数据=" + patient);

            // [{"feature":"病症","visittime":"就诊时间","mediaids":"多个图片以逗号隔开","pathography":{"模板ID(没有模板，默认ID为1)":"内容"，......}}]

            coursemap.put("feature", diseaseGeneralization);
            coursemap.put("visittime", visitTime);
            LogUtil.e("时间" + visitTime);

            list.add(coursemap);
//                    list.add(map2);
            course = GsonTools.createGsonString(list);
            LogUtil.e("我要看的coursemap数据11=" + course);
            return true;
        }
    }

    private void clinicalCreate(String patient, String course) {
        new NetApi().clinicalCreate(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalCreateActivity.this)) {
                    return;
                }
                ToastUtil.show("已保存");
                ClinicalFragment.ISREFRESHDATA = 1;
                if (clinicalMouldBean != null && clinicalMouldBean.data != null && clinicalMouldBean.data.size() != 0) {
                    for (int i = 0; i < clinicalMouldBean.data.size(); i++) {
                        SharedPreferencesUtil.saveString(clinicalMouldBean.data.get(i).templetname, null);
                    }
                }
                finish();
            }

            @Override
            public void onFailure(String message) {

            }
        }, patient, course);
    }

    @Override
    public int Altert_btleftdo() {
        switch (alertflag) {
            case 1:
                AlertDialogs.aDialog.dismiss();
                break;
            case 2:
                AlertDialogs.aDialog.dismiss();
                break;
            default:
                break;
        }
        return 0;
    }

    @Override
    public int Altert_btrightdo() {
        switch (alertflag) {
            case 1:
                if (clinicalMouldBean != null && clinicalMouldBean.data != null && clinicalMouldBean.data.size() != 0) {
                    for (int i = 0; i < clinicalMouldBean.data.size(); i++) {
                        SharedPreferencesUtil.saveString(clinicalMouldBean.data.get(i).templetname, null);
                    }
                }

                finish();
                break;
            case 2:
                flag = 1;
                intent.setClass(this, ClinicalMouldActivity.class);
//                intent.putExtra("clinicalMouldBean", (Serializable) clinicalMouldBean);
                startActivity(intent);
                break;
            default:
                break;
        }
        return 0;
    }

    //获取个人模板
    public void getClinicalPersonMould() {
        NetApi.getClinicalPersonMould(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalMould" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalCreateActivity.this)) {
                    clinicalMouldBean = null;
                    idInteger = null;
                    handler.sendEmptyMessage(REQUEST_SUCCESS_EMPTY);
                    return;
                }
                clinicalMouldBean = JsonUtil.json2Bean(result, ClinicalMouldBean.class);
                handler.sendEmptyMessage(REQUEST_SUCCESS);
            }

            @Override
            public void onFailure(String message) {
                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        });
    }
//

    private ArrayList<String> mDataMediaId = new ArrayList<String>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

                intent.setClass(ClinicalCreateActivity.this, ClinicalPictureClassifyActivity.class);
                intent.putStringArrayListExtra("imgruls", mSelectPath);
                startActivityForResult(intent, 101);

            }
        }

        if (requestCode == 101) {
            if (data != null) {
                updataPhotoBeens = (List<UpdataPhotoBeen>) data.getSerializableExtra("updataPhotoBeens");
            }
            if (updataPhotoBeens != null && updataPhotoBeens.size() != 0) {
                oldUpdataPhotoBeens.addAll(updataPhotoBeens);
                tv_clinical_classify.setVisibility(View.VISIBLE);
            }

            createPhotoAdapter.updata(oldUpdataPhotoBeens);
        }

        if (requestCode == 102) {
            updataPhotoBeens.clear();
            updataPhotoBeens.addAll(oldUpdataPhotoBeens);
            oldUpdataPhotoBeens.clear();
            mDataMediaId = data.getStringArrayListExtra("mDataMediaId");
            if (updataPhotoBeens != null && updataPhotoBeens.size() != 0) {
                for (int i = 0; i < updataPhotoBeens.size(); i++) {
                    for (int j = 0; j < mDataMediaId.size(); j++) {
                        if ((updataPhotoBeens.get(i).data.mediaid.equals(mDataMediaId.get(j)))) {
                            oldUpdataPhotoBeens.add(updataPhotoBeens.get(i));
                        }
                    }
                }
            }

            if(mDataMediaId == null || mDataMediaId.size() == 0){
                tv_clinical_classify.setVisibility(View.GONE);
            }else{
                tv_clinical_classify.setVisibility(View.VISIBLE);
            }

            createPhotoAdapter.updata(oldUpdataPhotoBeens);
        }

        if (requestCode == 103) {
            updataPhotoBeens = (List<UpdataPhotoBeen>) data.getSerializableExtra("updataPhotoBeens");
            if (data != null && updataPhotoBeens != null) {
                createPhotoAdapter.updata(updataPhotoBeens);
                oldUpdataPhotoBeens = updataPhotoBeens;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
