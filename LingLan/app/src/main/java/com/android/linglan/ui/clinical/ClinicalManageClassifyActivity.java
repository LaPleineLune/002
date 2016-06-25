package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.linglan.adapter.clinical.ClinicalManageClassifyAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.ClinicalClassifyBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeMy on 2016/4/26 0026.
 * 管理/新建分类界面
 */
public class ClinicalManageClassifyActivity extends BaseActivity {
    private RecyclerView rec_manage_classify;
    private ClinicalManageClassifyAdapter adapter;
    private List<ClinicalClassifyBean.ClinicalClassifyData> clinicalClassifyData;
    private List<ClinicalClassifyBean.ClinicalClassifyData> data = new ArrayList<ClinicalClassifyBean.ClinicalClassifyData>();
    private Intent intent = null;

    @Override
    public void onResume() {
        super.onResume();
        getClinicalClassify();
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_manage_classify);

    }

    @Override
    protected void initView() {
        rec_manage_classify = (RecyclerView) findViewById(R.id.rec_manage_classify);
    }

    @Override
    protected void initData() {
        setTitle("管理分类", "");
//        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.save);
//        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
//        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
        getClinicalClassify();
        rec_manage_classify.setLayoutManager(new LinearLayoutManager(this));
        rec_manage_classify.setHasFixedSize(true);
        adapter = new ClinicalManageClassifyAdapter(this);
        rec_manage_classify.setAdapter(adapter);

    }

    @Override
    protected void setListener() {
        right.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right:
                intent.setClass(this, ClinicalChangeClassifyActivity.class);
//                intent.putExtra("classifyId", clinicalClassifyData.get(position).tagid);
//                intent.putExtra("classifyName", clinicalClassifyData.get(position).tagname);
                startActivity(intent);
                break;
        }
    }

    private void getClinicalClassify() {
        NetApi.getClinicalClassify(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getClinicalClassify=" + result);
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ClinicalManageClassifyActivity.this)) {
                    if (HttpCodeJugementUtil.code == 1) {
                        adapter.updateAdapter(null);
                    }
                    return;
                }

                ClinicalClassifyBean clinicalClassifyBean = JsonUtil.json2Bean(result, ClinicalClassifyBean.class);

                clinicalClassifyData = clinicalClassifyBean.data;

                data.clear();
                for(ClinicalClassifyBean.ClinicalClassifyData datas : clinicalClassifyData) {
                    if (!datas.userid.equals("0")) {
                        data.add(datas);
                    }
                }

                adapter.updateAdapter(data);
            }

            @Override
            public void onFailure(String message) {
            }
        });
    }

}
