package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.linglan.adapter.clinical.PatientSelectAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.ClinicalFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalCollatingPatientSelectBean;
import com.android.linglan.http.bean.SortModel;
import com.android.linglan.ui.R;
import com.android.linglan.utils.CharacterParser;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.PinyinComparator;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.android.linglan.widget.UpdateDialog;
import com.android.linglan.widget.sortlistview.ClearEditText;
import com.android.linglan.widget.sortlistview.SideBar;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 患者选择
 */
public class PatientSelectActivity extends BaseActivity {
    protected static final int LOADDATA_SUCCESS = 0;
    protected static final int RELATE_SUCCESS =  1;
    protected static final int REQUEST_SUCCESS_NO_CONTENT = 2;
    protected static final int REQUEST_SUCCESS_CONTENT = 3;
    private FrameLayout fl_patient_select;
    private LinearLayout ll_patient_select_no_content;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private PatientSelectAdapter adapter;
    private Intent intent = null;
    private String nonamecaseid = "";// 病历ID
    private int mPosition;
    private ClinicalCollatingPatientSelectBean clinicalCollatingBean;
    private ClearEditText filter_edit;
    private UpdateDialog exitLoginDialog;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private ArrayList<SortModel> date;
    private List<SortModel> mSortList;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case LOADDATA_SUCCESS:

                    SourceDateList = filledData(date);
                    //Collections是一个包装类。它包含有各种有关集合操作的静态多态方法。此类不能实例化，就像一个工具类，服务于Java的Collection框架。
                    // 根据自已定义的PinyinComparator进行排序源数据
                    Collections.sort(SourceDateList, pinyinComparator);
                    adapter.updateListView(clinicalCollatingBean, SourceDateList);
                    break;
                case RELATE_SUCCESS:
                    Intent intent = new Intent();
//                    intent.putExtra("illnesscaseid", clinicalCollatingBean.data.get(mPosition).illnesscaseid);
                    intent.putExtra("illnesscaseid", mSortList.get(mPosition).getId());
                    intent.setClass(PatientSelectActivity.this, ClinicalDetailsActivity.class);
                    startActivity(intent);
                    break;
                case REQUEST_SUCCESS_NO_CONTENT:
                    fl_patient_select.setVisibility(View.GONE);
                    ll_patient_select_no_content.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_SUCCESS_CONTENT:
                    fl_patient_select.setVisibility(View.VISIBLE);
                    ll_patient_select_no_content.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_patient_select);
    }

    @Override
    protected void initView() {
        fl_patient_select = (FrameLayout) findViewById(R.id.fl_patient_select);
        ll_patient_select_no_content = (LinearLayout) findViewById(R.id.ll_patient_select_no_content);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextDialog(dialog);
        sortListView = (ListView) findViewById(R.id.lv);
        filter_edit = (ClearEditText) findViewById(R.id.filter_edit);
    }

    @Override
    protected void initData() {
        setTitle("选择已有患者", "");
        intent = new Intent();
        nonamecaseid  = getIntent().getStringExtra("illnesscaseid");
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
//        SourceDateList = filledData(date);
//        //Collections是一个包装类。它包含有各种有关集合操作的静态多态方法。此类不能实例化，就像一个工具类，服务于Java的Collection框架。
//        // 根据自已定义的PinyinComparator进行排序源数据
//        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new PatientSelectAdapter(PatientSelectActivity.this);
        sortListView.setAdapter(adapter);
        getPatientSelect(null);
    }

    @Override
    protected void setListener() {
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                exitLoginDialog = new UpdateDialog(PatientSelectActivity.this, "确定关联到【"+ mSortList.get(position).getName() +"】下？\n\n关联成功后，原未命名病历将删除。", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        getRelatePatientInfo(clinicalCollatingBean.data.get(position).illnesscaseid, nonamecaseid);
                        getRelatePatientInfo(mSortList.get(position).getId(), nonamecaseid);
                        mPosition = position;
                        exitLoginDialog.dismiss();
                    }
                });
                exitLoginDialog.setTitle("温馨提示");
                exitLoginDialog.setCancelText("取消");
                exitLoginDialog.setEnterText("确定");
                exitLoginDialog.show();

            }
        });

        filter_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getPatientSelect(filter_edit.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private List<SortModel> filledData(ArrayList<SortModel> date) {
        mSortList = new ArrayList<SortModel>();
        if(date != null && date.size() != 0){
            for (int i = 0; i < date.size(); i++) {
                SortModel sortModel = new SortModel();
                sortModel.setName(date.get(i).getName());
                sortModel.setId(date.get(i).getId());
                sortModel.setFeature(date.get(i).getFeature());
                sortModel.setLastvisittime(date.get(i).getLastvisittime());
                // 汉字转换成拼音
                String pinyin = characterParser.getSelling(date.get(i).getName());

                String sortString = "";
                if (pinyin != null && pinyin.length() > 1) {
                    sortString = pinyin.substring(0, 1).toUpperCase();
                    // 正则表达式，判断首字母是否是英文字母
                    if (sortString.matches("[A-Z]")) {
                        sortModel.setSortLetters(sortString.toUpperCase());
                    } else {
                        sortModel.setSortLetters("#");
                    }
                } else {
                    sortModel.setSortLetters("#");
                }
                mSortList.add(sortModel);
            }
        }

        return mSortList;

    }

    private void getPatientSelect(String patientname) {
        NetApi.getClinicalList(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getPatientSelect=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, PatientSelectActivity.this)) {
                    if (HttpCodeJugementUtil.code == 1) {
                        handler.sendEmptyMessage(REQUEST_SUCCESS_NO_CONTENT);
                    }
                    return;
                }
                handler.sendEmptyMessage(REQUEST_SUCCESS_CONTENT);
                clinicalCollatingBean = JsonUtil.json2Bean(result, ClinicalCollatingPatientSelectBean.class);
//                LogUtil.e("嘿嘿嘿嘿嘿clinicalCollatingBean.data.list.size() =" + clinicalCollatingBean.data.list.size());
                if (clinicalCollatingBean != null && clinicalCollatingBean.data.list.size() != 0) {
//                    date = new String[clinicalCollatingBean.data.size()];

                    date = new ArrayList<SortModel>();
                    for (int i = 0; i < clinicalCollatingBean.data.list.size(); i++) {
                        SortModel sortModel = new SortModel();
                        sortModel.setName(clinicalCollatingBean.data.list.get(i).patientname);
                        sortModel.setId(clinicalCollatingBean.data.list.get(i).illnesscaseid);
                        sortModel.setFeature(clinicalCollatingBean.data.list.get(i).feature);
                        sortModel.setLastvisittime(clinicalCollatingBean.data.list.get(i).lastvisittime);
                        date.add(sortModel);
//                        date[i] = clinicalCollatingBean.data.get(i).patientname;
//                        LogUtil.e(date[i]);
                    }
                }

//                for(SortModel sortModel : date){
//                    LogUtil.e(sortModel.getName());
//                }

                handler.sendEmptyMessage(LOADDATA_SUCCESS);

            }

            @Override
            public void onFailure(String message) {
//                LogUtil.e(message);
            }
        }, null, null, null,patientname);
    }

    //关联病程信息
    private void getRelatePatientInfo(String illnesscaseid, String nonamecaseid) {
        NetApi.getRelatePatientInfo(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, PatientSelectActivity.this)) {
                    return;
                }
                MobclickAgent.onEvent(PatientSelectActivity.this, UmengBuriedPointUtil.ClinicalUnnamedRelevanceSucceed);
                handler.sendEmptyMessage(RELATE_SUCCESS);
                ToastUtil.show("已关联");
                ClinicalFragment.ISREFRESHDATA = 1;
            }

            @Override
            public void onFailure(String message) {

            }
        }, illnesscaseid, nonamecaseid);
    }

}
