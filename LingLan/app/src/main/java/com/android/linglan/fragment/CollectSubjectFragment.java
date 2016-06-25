package com.android.linglan.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.linglan.adapter.CollectSubjectAdapter;
import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.AllSubjectClassifyListBean;
import com.android.linglan.http.bean.RecommendSubjects;
import com.android.linglan.ui.R;
import com.android.linglan.ui.study.SubjectDetailsActivity;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/1/13 0013.
 * 收藏的专题
 */
public class CollectSubjectFragment extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private View rootView;
    private GridView gv_collect_subject;
    private CollectSubjectAdapter adapter;
    private Intent intent;
    public boolean edit = false;

    public ArrayList<RecommendSubjects.RecommendSubject> data;
    public ArrayList<AllSubjectClassifyListBean.SubjectClassifyListBean> subjectClassifyListBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_collect_subject, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {
        gv_collect_subject = (GridView) rootView.findViewById(R.id.gv_collect_subject);

    }

    @Override
    protected void initData() {
//        setTitle("我的收藏", "编辑");
        getCollectSubject();
        gv_collect_subject.setSelector(new ColorDrawable(Color.TRANSPARENT));// 设置item选中色
        adapter = new CollectSubjectAdapter(getActivity());
        gv_collect_subject.setAdapter(adapter);
        intent = new Intent();

    }

    @Override
    protected void setListener() {
        gv_collect_subject.setOnItemClickListener(this);
        gv_collect_subject.setOnItemLongClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MobclickAgent.onEvent(getActivity(), UmengBuriedPointUtil.StudySubjectContent);
        intent.setClass(getActivity(), SubjectDetailsActivity.class);
        intent.putExtra("specialid", data.get(position).specialid);
        intent.putExtra("specialname", data.get(position).specialname);
        intent.putExtra("photo", data.get(position).photo);
        intent.putExtra("logo", data.get(position).logo);
        intent.putExtra("description", data.get(position).description);
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtil.show(getActivity(), "长按第" + position + "个被删除", 1);
        return false;
    }

    public void mSetVisibility(boolean edit) {
        this.edit = edit;
        adapter.updateAdapter(edit, data);
        if (edit) {
            gv_collect_subject.setOnItemClickListener(null);
            gv_collect_subject.setOnItemLongClickListener(null);
        } else {
//            initSlide();
            gv_collect_subject.setOnItemClickListener(this);
            gv_collect_subject.setOnItemLongClickListener(this);
        }
    }

    public void getCollectSubject() {
        NetApi.getCollectSubject(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getCollectSubject" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,getActivity())){
                    return;
                }
                if (!TextUtils.isEmpty(result)) {
                    RecommendSubjects recommendSubjects = JsonUtil.json2Bean(result, RecommendSubjects.class);
                    data = recommendSubjects.data;
//                    adapter.updateAdapter(false, data);
                    adapter.updateAdapter(edit, data);
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, "1");
    }
}
