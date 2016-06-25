package com.android.linglan.ui.clinical;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.ClinicalFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.KeyBoardUtils;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.AlertDialogs;
import com.android.linglan.widget.AlertDialoginter;

/**
 * Created by LeeMy on 2016/4/27 0027.
 * 编辑或者删除分类
 */
public class ClinicalChangeClassifyActivity extends BaseActivity implements AlertDialoginter {
    private EditText edt_change_classify;
    private Button bt_delete_classify;
    private String classifyId;
    private String classifyName;
    private String cont;
    @Override
    protected void setView() {
        setContentView(R.layout.activity_clinical_edit_classify);
    }

    @Override
    protected void initView() {
        edt_change_classify = (EditText) findViewById(R.id.edt_change_classify);
        bt_delete_classify = (Button) findViewById(R.id.bt_delete_classify);

    }

    @Override
    protected void initData() {
        classifyId = getIntent().getStringExtra("classifyId");
        classifyName = getIntent().getStringExtra("classifyName");
        cont = getIntent().getStringExtra("cont");
        if (classifyName != null) {
            setTitle("编辑分类", "保存");
        } else {
            setTitle("新建分类", "保存");
            bt_delete_classify.setVisibility(View.GONE);
        }
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.save);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);

        edt_change_classify.setText(classifyName);
        if (classifyName != null && classifyName.length() > 0 && classifyName.length() <= 6) {
            edt_change_classify.setSelection(classifyName.length());
        } else if (classifyName != null && classifyName.length() > 6) {
            edt_change_classify.setSelection(6);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(classifyName == null){
            KeyBoardUtils.openKeybord(edt_change_classify, this);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if(classifyName == null){
            KeyBoardUtils.closeKeybord(edt_change_classify, this);
        }

    }
    @Override
    protected void setListener() {
        right.setOnClickListener(this);
        bt_delete_classify.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right:
                if (!edt_change_classify.getText().toString().trim().equals("")) {
                    if (classifyName != null) {// 修改分类
                        getClinicalEditClassify(edt_change_classify.getText().toString().trim());
                    } else {// 新建分类
                        getClinicalCreateClassify(edt_change_classify.getText().toString().trim());
                    }
                } else {
                    ToastUtil.show("请输入分类");
                }
                break;
            case R.id.bt_delete_classify:
                if (!cont.equals("0")) {
                    AlertDialogs.alert(ClinicalChangeClassifyActivity.this, "提示", "该分类下有病历，是否删除？", "取消", "删除");
                } else {
                    AlertDialogs.alert(ClinicalChangeClassifyActivity.this, "提示", "是否删除此分类？", "取消", "删除");
                }
                break;
        }
    }

    @Override
    public int Altert_btleftdo() {
        return 0;
    }

    @Override
    public int Altert_btrightdo() {
        getClinicalDeleteClassify();
        return 0;
    }

    /**
     * 新建分类
     * @param classifyname
     */
    private void getClinicalCreateClassify(String classifyname) {
        NetApi.getClinicalCreateClassify(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalCreateClassify=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalChangeClassifyActivity.this)) {
                    return;
                }

                ToastUtil.show("新建分类已保存");
                finish();

            }

            @Override
            public void onFailure(String message) {

            }
        }, classifyname);
    }

    private void getClinicalEditClassify(String name) {
        NetApi.getClinicalEditClassify(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalEditClassify=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalChangeClassifyActivity.this)) {
                    return;
                }
                ToastUtil.show("已保存");
                ClinicalFragment.ISREFRESHDATA = 1;
                finish();
            }

            @Override
            public void onFailure(String message) {

            }
        }, classifyId, name);
    }

    private void getClinicalDeleteClassify() {
        NetApi.getClinicalDeleteClassify(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalDeleteClassify=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalChangeClassifyActivity.this)) {
                    return;
                }
                ToastUtil.show("已删除");
                ClinicalFragment.ISREFRESHDATA = 1;
                finish();
            }

            @Override
            public void onFailure(String message) {

            }
        }, classifyId);
    }
}
