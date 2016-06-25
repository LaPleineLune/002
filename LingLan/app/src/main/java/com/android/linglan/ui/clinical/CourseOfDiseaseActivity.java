package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.adapter.clinical.EditCourseOfDiseaseImageAdapter;
import com.android.linglan.adapter.clinical.EditCourseOfDiseaseMouldAdapter;
import com.android.linglan.adapter.clinical.MouldAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.ClinicalFragment;
import com.android.linglan.http.GsonTools;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalDetailsBean;
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
import com.android.linglan.widget.ListViewForScrollView;
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
 * 添加或编辑病程
 */
public class CourseOfDiseaseActivity extends BaseActivity implements AlertDialoginter {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;
    protected static final int REQUEST_PICTURE = 3;
    private Intent intent = null;
    //    private RelativeLayout rl_course_disease;
    private LinearLayout ll_course_disease, ll_item_clinical_mould, ll_clinical_mould;
    private RelativeLayout rl_clinical_mould;
    private LinearLayout ll_no_network;
    private LinearLayout ll_visit_time;
    private TextView tv_visit_time;
    private TextView tv_clinical_mould, tv_clinical_classify;
    private EditText edt_pathography;
    private RecyclerView rec_img;
    private RecyclerView rec_mould;
    private ListViewForScrollView lv_mould;
    private EditText edt_disease_generalization;
    private Button btn_clinical_delete_disease_course;
    private ClinicalDetailsBean.ClinicalDetailsData clinicalDetailsData;
    private EditCourseOfDiseaseImageAdapter imageAdapter;
    private EditCourseOfDiseaseMouldAdapter mouldAdapter;
    private MouldAdapter adapter;
    private int alertflag = 1000;
    private Integer[] idInteger;
    private String course = "";// 病程
    private String illnesscaseid = "";// 病历ID
    private String initDateTime = "";
    private ClinicalMouldBean clinicalMouldBean;
    private ArrayList<String> mSelectPath;
    private List<UpdataPhotoBeen> updataPhotoBeens;
    private List<UpdataPhotoBeen> oldUpdataPhotoBeens;
    private ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg clinicalDetailsImg;
    private int count = 0;
    private int count2 = 0;
    private int flag;
    private String clinicalCollatingDetailsActivity;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    ll_course_disease.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);

                    if (clinicalMouldBean != null && clinicalMouldBean.data != null && clinicalMouldBean.data.size() != 0) {
                        edt_pathography.setVisibility(View.GONE);
                        edt_pathography.setText("");
                        ll_clinical_mould.removeAllViews();
                        ll_clinical_mould.addView(rl_clinical_mould);
                        clinicalDetailsData.pathography = null;
                        int i = 0;
                        idInteger = new Integer[clinicalMouldBean.data.size()];
                        for (ClinicalMouldBean.ClinicalMouldData clinicalMouldData : clinicalMouldBean.data) {
                            LinearLayout clinical_mould = new LinearLayout(CourseOfDiseaseActivity.this);
                            TextView title = new TextView(CourseOfDiseaseActivity.this);
                            EditText content = new EditText(CourseOfDiseaseActivity.this);
                            content.setId(i++);
                            idInteger[i - 1] = content.getId();
//                            LogUtil.e(content.getId() + "");
//                            LogUtil.e(idInteger[i - 1] + "");

                            clinical_mould.setOrientation(LinearLayout.HORIZONTAL);
                            clinical_mould.addView(title);
                            clinical_mould.addView(content);
                            title.setText("[" + clinicalMouldData.templetname + "] :");
//                            title.setTextColor(Color.parseColor("#48558f"));
                            title.setTextColor(content.getResources().getColor(R.color.blue));
//                            int dp8 = getResources().getDimensionPixelSize(R.dimen.dp8);
                            title.setTextSize(16);
                            content.setTextSize(16);
                            content.setBackgroundResource(R.color.white);
                            String con = SharedPreferencesUtil.getString(clinicalMouldData.templetname, null);
                            if (con != null) {
                                content.setText(con);
                            } else {
                                content.setHint("                                      ");
                            }
                            ll_clinical_mould.addView(clinical_mould);
                        }
                    } else {
                        idInteger = null;
                        clinicalDetailsData.pathography = null;
//                        SharedPreferencesUtil.saveString(clinicalMouldData.templetname, null);
                        ll_clinical_mould.removeAllViews();
                        ll_clinical_mould.addView(rl_clinical_mould);
                        ll_clinical_mould.addView(edt_pathography);
                        edt_pathography.setVisibility(View.VISIBLE);
                    }
                    break;
                case REQUEST_FAILURE:
//                    rl_course_disease.setVisibility(View.GONE);
                    ll_course_disease.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;

                case REQUEST_PICTURE:
                    intent.setClass(CourseOfDiseaseActivity.this, ClinicalPictureClassifyActivity.class);
                    intent.putStringArrayListExtra("imgruls", mSelectPath);
//                    startActivity(intent);
                    startActivityForResult(intent, 101);
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.activity_course_of_disease);

    }

    @Override
    protected void initView() {
        rl_clinical_mould = (RelativeLayout) findViewById(R.id.rl_clinical_mould);
        ll_course_disease = (LinearLayout) findViewById(R.id.ll_course_disease);

        ll_clinical_mould = (LinearLayout) findViewById(R.id.ll_clinical_mould);
        ll_item_clinical_mould = (LinearLayout) findViewById(R.id.ll_item_clinical_mould);

        tv_clinical_mould = (TextView) findViewById(R.id.tv_clinical_mould);
        tv_clinical_classify = (TextView) findViewById(R.id.tv_clinical_classify);
        edt_pathography = (EditText) findViewById(R.id.edt_pathography);

        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        ll_visit_time = (LinearLayout) findViewById(R.id.ll_visit_time);
        tv_visit_time = (TextView) findViewById(R.id.tv_visit_time);
        tv_clinical_mould = (TextView) findViewById(R.id.tv_clinical_mould);
        edt_disease_generalization = (EditText) findViewById(R.id.edt_disease_generalization);
        rec_img = (RecyclerView) findViewById(R.id.rec_img);
        rec_mould = (RecyclerView) findViewById(R.id.rec_mould);
        lv_mould = (ListViewForScrollView) findViewById(R.id.lv_mould);
        btn_clinical_delete_disease_course = (Button) findViewById(R.id.btn_clinical_delete_disease_course);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (EditClinicalMouldActivity.isSave == 1) {
            EditClinicalMouldActivity.isSave = 0;
            getClinicalPersonMould();
        }


        tv_clinical_classify.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        flag = 0;
        clinicalDetailsData = (ClinicalDetailsBean.ClinicalDetailsData) getIntent().getSerializableExtra("clinicalDetailsData");
        illnesscaseid = getIntent().getStringExtra("illnesscaseid");
        clinicalCollatingDetailsActivity = getIntent().getStringExtra("clinicalCollatingDetailsActivity");

        if (clinicalCollatingDetailsActivity != null && clinicalCollatingDetailsActivity.equals("clinicalCollatingDetailsActivity")) {
            btn_clinical_delete_disease_course.setVisibility(View.GONE);
        }
        if (clinicalDetailsData != null && !clinicalDetailsData.equals("")) {

            setTitle("编辑病程", "保存");
            if (clinicalDetailsData.img.size() == 0) {
                tv_clinical_classify.setVisibility(View.GONE);
            }
            tv_visit_time.setText(clinicalDetailsData.visittime);
            edt_disease_generalization.setText(clinicalDetailsData.feature);
            edt_disease_generalization.setSelection(clinicalDetailsData.feature.length());

            //删除病程
            btn_clinical_delete_disease_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertflag = 3;
                    AlertDialogs.alert(CourseOfDiseaseActivity.this, "提示", "确认删除此病程？", "取消", "确定");
                }
            });

            LinearLayoutManager linearLayoutManagerMould = new SyLinearLayoutManager(this);
            linearLayoutManagerMould.setOrientation(LinearLayoutManager.VERTICAL);
            rec_mould.setLayoutManager(linearLayoutManagerMould);
            if (clinicalDetailsData.pathography != null && clinicalDetailsData.pathography.size() != 0) {
                if (clinicalDetailsData.pathography.size() == 1 && clinicalDetailsData.pathography.get(0).templetid.equals("1") && clinicalDetailsData.pathography.get(0).content == null) {
                    edt_pathography.setVisibility(View.VISIBLE);
//                    getClinicalPersonMould();
                } else if (clinicalDetailsData.pathography.size() == 1 && clinicalDetailsData.pathography.get(0).templetid.equals("1") && clinicalDetailsData.pathography.get(0).content != null) {
                    edt_pathography.setVisibility(View.VISIBLE);
                    edt_pathography.setText(clinicalDetailsData.pathography.get(0).content);
//                    getClinicalPersonMould();
                } else {
                    edt_pathography.setVisibility(View.GONE);
                    int i = 0;
                    rec_mould.setVisibility(View.VISIBLE);
                    mouldAdapter = new EditCourseOfDiseaseMouldAdapter(this, clinicalDetailsData.pathography);
                    rec_mould.setAdapter(mouldAdapter);
                    idInteger = new Integer[clinicalDetailsData.pathography.size()];
                    for (ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsPathography clinicalDetailsPathography : clinicalDetailsData.pathography) {
                        LinearLayout clinical_mould = new LinearLayout(this);
                        TextView title = new TextView(this);
                        EditText content = new EditText(this);
                        content.setId(i++);
                        idInteger[i - 1] = content.getId();

                        if (Build.VERSION.SDK_INT >= 16) {
                            title.setBackground(null);
                            content.setBackground(null);
                        } else {
                            title.setBackgroundDrawable(null);
                            content.setBackgroundDrawable(null);
                        }

                        clinical_mould.setOrientation(LinearLayout.HORIZONTAL);
                        clinical_mould.addView(title);
                        clinical_mould.addView(content);
//                        title.setText(clinicalDetailsPathography.templetname);
                        title.setTextSize(16);
                        content.setTextSize(16);
                        title.setText("[" + clinicalDetailsPathography.templetname + "] :");
//                        title.setTextColor(Color.parseColor("#48558f"));
                        title.setTextColor(content.getResources().getColor(R.color.blue));
                        if (clinicalDetailsPathography.content != null && !clinicalDetailsPathography.content.equals("")) {
                            content.setText(clinicalDetailsPathography.content);
//                            SharedPreferencesUtil.saveString(clinicalDetailsPathography.templetname, clinicalDetailsPathography.content);
//                            LogUtil.e("clinicalDetailsPathography.templetname = " + clinicalDetailsPathography.templetname);
//                            LogUtil.e("clinicalDetailsPathography.content = " + clinicalDetailsPathography.content);
                        } else {
                            content.setHint("                                      ");
                        }


                        ll_clinical_mould.addView(clinical_mould);
                    }
                }
            } else {
                edt_pathography.setVisibility(View.VISIBLE);
                getClinicalPersonMould();
            }

            adapter = new MouldAdapter(this, clinicalDetailsData.pathography);
            lv_mould.setAdapter(adapter);

        } else {
            setTitle("添加病程", "保存");
            clinicalDetailsData = new ClinicalDetailsBean.ClinicalDetailsData();
            clinicalDetailsData.img = new ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg>();
            tv_clinical_classify.setVisibility(View.GONE);
            edt_pathography.setVisibility(View.VISIBLE);
            Calendar c = Calendar.getInstance();
            initDateTime = c.get(Calendar.YEAR) + "." + (c.get(Calendar.MONTH) + 1) + "." + c.get(Calendar.DAY_OF_MONTH);
            tv_visit_time.setText(initDateTime);
            btn_clinical_delete_disease_course.setVisibility(View.GONE);
            getClinicalPersonMould();
        }
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.save);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();

        LinearLayoutManager linearLayoutManagerImg = new LinearLayoutManager(this);
        linearLayoutManagerImg.setOrientation(LinearLayoutManager.HORIZONTAL);
        rec_img.setLayoutManager(linearLayoutManagerImg);
        rec_img.setHasFixedSize(true);
        rec_img.setVisibility(View.VISIBLE);
        imageAdapter = new EditCourseOfDiseaseImageAdapter(this, clinicalDetailsData.img);
        rec_img.setAdapter(imageAdapter);
    }

    @Override
    protected void setListener() {
        tv_clinical_mould.setOnClickListener(this);
        ll_no_network.setOnClickListener(this);
        ll_visit_time.setOnClickListener(this);
        right.setOnClickListener(this);
        back.setOnClickListener(this);
        tv_clinical_classify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_clinical_mould:

                if (!isEmptyClinicalPathography()) {

                    intent.setClass(this, EditClinicalMouldActivity.class);
                    if (flag == 1) {
                        if (clinicalMouldBean != null) {
                            intent.putExtra("clinicalMouldBean", (Serializable) clinicalMouldBean);
                            if (idInteger != null && idInteger.length != 0) {
                                for (int i = 0; i < idInteger.length; i++) {
                                    String context = ((EditText) findViewById(idInteger[i])).getText().toString();
                                    if (context.length() != 0) {
                                        SharedPreferencesUtil.saveString(clinicalMouldBean.data.get(i).templetname, context);
                                        LogUtil.e("哈哈哈clinicalMouldBean.data.get(i).templetname = " + clinicalMouldBean.data.get(i).templetname);
                                        LogUtil.e("哈哈哈clinicalMouldBean.data.get(i).templetname = " + context);
                                    } else {
                                        SharedPreferencesUtil.saveString(clinicalMouldBean.data.get(i).templetname, null);
                                    }
                                }
                            }
                        } else {
                            clinicalMouldBean = null;
                            intent.putExtra("clinicalMouldBean", (Serializable) clinicalMouldBean);
                            startActivity(intent);
                            return;
                        }

                    } else {

                        if (clinicalDetailsData != null && !clinicalDetailsData.equals("") && clinicalDetailsData.pathography != null && clinicalDetailsData.pathography.size() != 0) {
                            if (idInteger != null && idInteger.length != 0) {
                                for (int i = 0; i < idInteger.length; i++) {
                                    String context = ((EditText) findViewById(idInteger[i])).getText().toString();
                                    if (context.length() != 0) {
                                        SharedPreferencesUtil.saveString(clinicalDetailsData.pathography.get(i).templetname, context);
                                        LogUtil.e("嘿嘿嘿clinicalDetailsData.pathography.get(i).templetname = " + clinicalDetailsData.pathography.get(i).templetname);
                                        LogUtil.e("嘿嘿嘿clinicalDetailsData.pathography.get(i).templetname = " + context);
                                    } else {
                                        SharedPreferencesUtil.saveString(clinicalDetailsData.pathography.get(i).templetname, null);
                                    }
                                }
                            }
                        }

                        intent.putExtra("pathography", (Serializable) clinicalDetailsData.pathography);
                    }
                    startActivity(intent);
                } else {
                    alertflag = 2;
                    AlertDialogs.alert(this, "提示", "若添加模版，已有内容将被清空，请慎重选择", "取消", "确定");
                }

                break;
            case R.id.tv_clinical_classify:
                LogUtil.e("clinicalDetailsImg.mediaurlclinicalDetailsImg.mediaur = l" + "分类分类");
                if (clinicalDetailsData.img != null && clinicalDetailsData.img.size() != 0) {
                    intent.setClass(CourseOfDiseaseActivity.this, EditClinicalPictureClassifyActivity.class);
                    intent.putExtra("clinicalDetailsData.img", (Serializable) clinicalDetailsData.img);
                    intent.putExtra("edit", "edit");
                    startActivityForResult(intent, 103);
                    LogUtil.e(clinicalDetailsData.img.size() + "");

                    for (ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg clinicalDetailsImg : clinicalDetailsData.img) {
                        LogUtil.e("clinicalDetailsImg.mediaurlclinicalDetailsImg.mediaurl" + clinicalDetailsImg.categoryid);
//                        ToastUtil.show("id = " + clinicalDetailsImg.mediaid);
                    }


                } else {
                    tv_clinical_classify.setOnClickListener(null);
                }
                break;
            case R.id.ll_no_network:
                initData();
                break;
            case R.id.ll_visit_time:
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        CourseOfDiseaseActivity.this, tv_visit_time.getText().toString());
                dateTimePicKDialog.dateTimePicKDialog(tv_visit_time);
                break;
            case R.id.right:
                saveData();
                if (clinicalDetailsData != null && clinicalDetailsData.courseid != null) {
                    saveDiseaseCourseInfo(clinicalDetailsData.courseid, course);
                } else {
                    if (!isEmptyClinical()) {
                        ToastUtil.show("请填写病程内容");
                    }else{
                        addDiseaseCourseInfo(illnesscaseid, course);
                    }
                }

                break;
            case R.id.back:
                if (clinicalDetailsData != null && clinicalDetailsData.courseid != null) {
                    alertflag = 1;
                    AlertDialogs.alert(this, "提示", "不保存而退出此次编辑？", "取消", "退出");
                } else {
                    if (!isEmptyClinical()) {
                        finish();
                        return;
                    } else {
                        alertflag = 1;
                        AlertDialogs.alert(this, "提示", "不保存而退出此次编辑？", "取消", "退出");
                    }
                }

                break;
        }
    }

    private boolean isEmptyClinicalPathography() {
        String pathography = edt_pathography.getText().toString().trim();
        int count3 = 0;
        if (idInteger != null && idInteger.length != 0) {
            for (int i = 0; i < idInteger.length; i++) {
                String context = ((EditText) findViewById(idInteger[i])).getText().toString();
//                    LogUtil.e("有没有取到edittext里的内容啊" + context);
                if (context.length() != 0) {
                    count3 = 1;
                    if (clinicalMouldBean != null) {
//                        SharedPreferencesUtil.saveString(clinicalMouldBean.data.get(i).templetname, context);
                    }
                }
            }
        }

        if (TextUtils.isEmpty(pathography)) {
            return false;
        }
        return true;
    }

    private boolean isEmptyClinical() {
        String diseaseGeneralization = edt_disease_generalization.getText().toString().trim();
        String pathography = edt_pathography.getText().toString().trim();

        if (clinicalDetailsData != null && clinicalDetailsData.img != null && clinicalDetailsData.img.size() != 0) {
            count2 = 1;
        }

        Map<String, Object> pathographymap = new HashMap<String, Object>();
        if (idInteger != null && idInteger.length != 0) {
            for (int i = 0; i < idInteger.length; i++) {
                String context = ((EditText) findViewById(idInteger[i])).getText().toString();
//                  LogUtil.e("有没有取到edittext里的内容啊" + context);
                if (context.length() != 0) {
                    pathographymap.put(clinicalMouldBean.data.get(i).templetid, context);
                    count = 1;
                }
            }
        } else {
            pathographymap.put(1 + "", pathography);
//              LogUtil.e("有没有取到edittext里的内容啊" + pathography);
        }

        if (TextUtils.isEmpty(diseaseGeneralization) && count2 == 0 && TextUtils.isEmpty(pathography) && count == 0) {
            return false;
        } else {
            return true;
        }

    }

    private void saveData() {
        String visitTime = TimeStampConversionUtil.date2TimeStamp(tv_visit_time.getText().toString().trim(), "yyyy.MM.dd");
        String diseaseGeneralization = edt_disease_generalization.getText().toString().trim();
        String pathography = edt_pathography.getText().toString().trim();
        // [{"feature":"病症","visittime":"就诊时间","mediaids":"多个图片以逗号隔开","pathography":{"模板ID(没有模板，默认ID为1)":"内容"，......}}]
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> coursemap = new HashMap<String, Object>();
        coursemap.put("feature", diseaseGeneralization);
        coursemap.put("visittime", visitTime);
//        LogUtil.e("时间" + visitTime);

        if (clinicalDetailsData != null && clinicalDetailsData.img != null && clinicalDetailsData.img.size() != 0) {
            StringBuilder sb = new StringBuilder();
            for (ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg clinicalDetailsImg : clinicalDetailsData.img) {

                if (!(null == clinicalDetailsImg.mediaid || clinicalDetailsImg.mediaid.equals("0"))) {
                    sb.append(clinicalDetailsImg.mediaid);
                    sb.append(",");
                }
            }
            if (sb.toString().length() > 0) {
                String mediaids = sb.toString().substring(0, sb.toString().length() - 1);
//                LogUtil.e("相片的id串=" + mediaids);
                coursemap.put("mediaids", mediaids);
            }
        } else {
            coursemap.put("mediaids", "");
        }

        Map<String, Object> pathographymap = new HashMap<String, Object>();
        if (clinicalDetailsData != null && !clinicalDetailsData.equals("") && clinicalDetailsData.pathography != null && clinicalDetailsData.pathography.size() != 0) {
            if (idInteger != null && idInteger.length != 0) {
                for (int i = 0; i < idInteger.length; i++) {
                    String context = ((EditText) findViewById(idInteger[i])).getText().toString();
//                  LogUtil.e("有没有取到edittext里的内容啊" + context);
                    if (context.length() != 0) {
                        pathographymap.put(clinicalDetailsData.pathography.get(i).templetid, context);
                    } else {
                        pathographymap.put(clinicalDetailsData.pathography.get(i).templetid, "");
                    }
                }
            } else {

                if (!pathography.equals("")) {
                    pathographymap.put(1 + "", pathography);
                }else{
                    pathographymap.put(1 + "", "");
                }
//              LogUtil.e("有没有取到edittext里的内容啊" + pathography);
            }
        } else {
            if (idInteger != null && idInteger.length != 0) {
                for (int i = 0; i < idInteger.length; i++) {
                    String context = ((EditText) findViewById(idInteger[i])).getText().toString();
//                  LogUtil.e("有没有取到edittext里的内容啊" + context);
                    if (context.length() != 0) {
                        pathographymap.put(clinicalMouldBean.data.get(i).templetid, context);
                    } else {
                        pathographymap.put(clinicalMouldBean.data.get(i).templetid, "");
                    }
                }
            } else {
                if (!pathography.equals("")) {
                    pathographymap.put(1 + "", pathography);
                }else{
                    pathographymap.put(1 + "", "");
                }
//                pathographymap.put(1 + "", pathography);
              LogUtil.e("有没有取到edittext里的内容啊" + pathography);
            }
        }

        coursemap.put("pathography", pathographymap);
        course = GsonTools.createGsonString(coursemap);

        LogUtil.e("我要看的coursemap数据11=" + course);
    }

    //上传编辑病程信息
    private void saveDiseaseCourseInfo(String courseid, String course) {
        NetApi.saveDiseaseCourseInfo(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, CourseOfDiseaseActivity.this)) {
                    return;
                }
                ToastUtil.show("已保存");
                ClinicalFragment.ISREFRESHDATA = 1;
                ClinicalCollatingActivity.ISREFRESHDATA = 1;
                if (clinicalMouldBean != null && clinicalMouldBean.data != null && clinicalMouldBean.data.size() != 0) {
                    for (int i = 0; i < clinicalMouldBean.data.size(); i++) {
                        SharedPreferencesUtil.saveString(clinicalMouldBean.data.get(i).templetname, null);
                    }
                }

                if (clinicalDetailsData.pathography != null && clinicalDetailsData.pathography.size() != 0 && !((clinicalDetailsData.pathography.size() == 1 && clinicalDetailsData.pathography.get(0).templetid.equals("1")))) {
                    for (ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsPathography clinicalDetailsPathography : clinicalDetailsData.pathography) {
                        SharedPreferencesUtil.saveString(clinicalDetailsPathography.templetname, null);
                    }
                }

                finish();

            }

            @Override
            public void onFailure(String message) {

            }
        }, courseid, course);
    }

    //添加病程信息
    private void addDiseaseCourseInfo(String illnesscaseid, String course) {
        NetApi.addDiseaseCourseInfo(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, CourseOfDiseaseActivity.this)) {
                    return;
                }
                MobclickAgent.onEvent(CourseOfDiseaseActivity.this, UmengBuriedPointUtil.ClinicalMedicalHistoryAddSucceed);
                ToastUtil.show("已保存");
                ClinicalFragment.ISREFRESHDATA = 1;
                ClinicalCollatingActivity.ISREFRESHDATA = 1;
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
        }, illnesscaseid, course);
    }


    //删除病程
    private void clinicalCourseDelete(String courseid) {
        NetApi.clinicalCourseDelete(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, CourseOfDiseaseActivity.this)) {
                    return;
                }
                ToastUtil.show("已删除");
                ClinicalFragment.ISREFRESHDATA = 1;
                finish();
            }

            @Override
            public void onFailure(String message) {

            }
        }, courseid);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LogUtil.d("onKeyUp keyCode: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (clinicalDetailsData != null && !clinicalDetailsData.equals("")) {
                alertflag = 1;
                AlertDialogs.alert(this, "提示", "不保存而退出此次编辑？", "取消", "退出");
            } else {
                if (!isEmptyClinical()) {
                    finish();
                } else {
                    alertflag = 1;
                    AlertDialogs.alert(this, "提示", "不保存而退出此次编辑？", "取消", "退出");
                }
            }

            return true;
        }
        return super.onKeyUp(keyCode, event);

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
            case 3:
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
//                ToastUtil.show("我要保存病历");

                finish();
                break;
            case 2:
                clinicalMouldBean = null;
                intent.setClass(this, EditClinicalMouldActivity.class);
                intent.putExtra("clinicalMouldBean", (Serializable) clinicalMouldBean);
                startActivity(intent);
                break;
            case 3:
                clinicalCourseDelete(clinicalDetailsData.courseid);
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
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, CourseOfDiseaseActivity.this)) {
                    clinicalMouldBean = null;
                    handler.sendEmptyMessage(REQUEST_SUCCESS);
                    return;
                }
                clinicalMouldBean = JsonUtil.json2Bean(result, ClinicalMouldBean.class);
                handler.sendEmptyMessage(REQUEST_SUCCESS);
                flag = 1;
//                LogUtil.e("走啦吗走了吗-1-1-1");
            }

            @Override
            public void onFailure(String message) {
//                handler.sendEmptyMessage(REQUEST_FAILURE);
            }
        });
    }

    private ArrayList<String> mDataMediaId = new ArrayList<String>();
    ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg> clinicalDetailsImgs = new ArrayList<ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照返回的数据
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                handler.sendEmptyMessage(REQUEST_PICTURE);
            }
        }

        //选择分类后返回的数据
        if (requestCode == 101) {

            updataPhotoBeens = (List<UpdataPhotoBeen>) data.getSerializableExtra("updataPhotoBeens");
//            if (updataPhotoBeens != null && updataPhotoBeens.size() != 0) {
                for (UpdataPhotoBeen updataPhotoBeen : updataPhotoBeens) {
                    clinicalDetailsImg = new ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg();
                    clinicalDetailsImg.categoryname = updataPhotoBeen.data.categoryname;
                    clinicalDetailsImg.mediaurl = updataPhotoBeen.data.mediaurl;
                    clinicalDetailsImg.mediaid = updataPhotoBeen.data.mediaid;
                    clinicalDetailsImg.categoryid = updataPhotoBeen.data.categoryid;
                LogUtil.e("clinicalDetailsImg.mediaurlclinicalDetailsImg.categoryid" + clinicalDetailsImg.categoryid);
                    clinicalDetailsData.img.add(clinicalDetailsImg);
                }
                tv_clinical_classify.setVisibility(View.VISIBLE);
//            }

            imageAdapter.updata(clinicalDetailsData.img);
        }
//删除照片后返回的数据
        if (requestCode == 102) {
            clinicalDetailsImgs.clear();
            clinicalDetailsImgs.addAll(clinicalDetailsData.img);
            clinicalDetailsData.img.clear();
            mDataMediaId = data.getStringArrayListExtra("mDataMediaId");
            if (clinicalDetailsImgs != null && clinicalDetailsImgs.size() != 0) {
                for (int i = 0; i < clinicalDetailsImgs.size(); i++) {
                    for (int j = 0; j < mDataMediaId.size(); j++) {
                        if ((clinicalDetailsImgs.get(i).mediaid.equals(mDataMediaId.get(j)))) {
                            clinicalDetailsData.img.add(clinicalDetailsImgs.get(i));
                        }
                    }
                }
            }

            if (mDataMediaId == null || mDataMediaId.size() == 0) {
                tv_clinical_classify.setVisibility(View.GONE);
            } else {
                tv_clinical_classify.setVisibility(View.VISIBLE);
            }
            imageAdapter.updata(clinicalDetailsData.img);
        }
//编辑分类后返回的数据
        if (requestCode == 103) {
            updataPhotoBeens = (List<UpdataPhotoBeen>) data.getSerializableExtra("updataPhotoBeens");
            if (data != null && updataPhotoBeens != null) {
                clinicalDetailsData.img.clear();
                for (UpdataPhotoBeen updataPhotoBeen : updataPhotoBeens) {
                    clinicalDetailsImg = new ClinicalDetailsBean.ClinicalDetailsData.ClinicalDetailsImg();
                    clinicalDetailsImg.categoryname = updataPhotoBeen.data.categoryname;
                    clinicalDetailsImg.mediaurl = updataPhotoBeen.data.mediaurl;
                    clinicalDetailsImg.mediaid = updataPhotoBeen.data.mediaid;
                    clinicalDetailsImg.categoryid = updataPhotoBeen.data.categoryid;
//                    LogUtil.e("clinicalDetailsImg.mediaurlclinicalDetailsImg.mediaurl" + clinicalDetailsImg.mediaurl);
                    clinicalDetailsData.img.add(clinicalDetailsImg);
                }
                imageAdapter.updata(clinicalDetailsData.img);
            }
        }
    }
}
