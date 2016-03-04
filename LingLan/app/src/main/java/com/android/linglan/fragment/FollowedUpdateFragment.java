package com.android.linglan.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.linglan.adapter.FollowedAllAdapter;
import com.android.linglan.base.BaseFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.http.bean.FollowedListBean;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.JsonUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by LeeMy on 2016/1/15 0015.
 * 关注的最近更新
 */
public class FollowedUpdateFragment extends BaseFragment {
    private View rootView;
    private ListView lv_followed_all;
    private FollowedAllAdapter adapter;
    private FollowedListBean followedList;
    private ArrayList<FollowedListBean.FollowedListData> followedListData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_followed_all, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {
        lv_followed_all = (ListView) rootView.findViewById(R.id.lv_followed_all);

    }

    @Override
    protected void initData() {
//        setTitle("我的关注", "");
//        ToastUtil.show(getActivity(), "这是关注的最近更新页", 1);
        getFollowedList();
        lv_followed_all.setSelector(new ColorDrawable(Color.TRANSPARENT));// 设置item选中色
        adapter = new FollowedAllAdapter(getActivity());
        lv_followed_all.setAdapter(adapter);

    }

    @Override
    protected void setListener() {
//        lv_followed_all.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                // TODO Auto-generated method stub
//                // When clicked, show a toast with the TextView text
//                ToastUtil.show(getActivity(), "长按第" + arg2 + "个被删除", 1);
//                return false;
//            }
//        });

        lv_followed_all.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.show(getActivity(), "点击第" + position + "个", 1);
            }
        });

    }

    public void getFollowedList() {
        NetApi.getFollowedList(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getFollowedList=" + result);

                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result)){
                    return;
                }
                followedList = JsonUtil.json2Bean(result, FollowedListBean.class);
                followedListData = new ArrayList<FollowedListBean.FollowedListData>();
                followedListData = followedList.data;
                adapter.upDateAdapter(followedListData);
            }

            @Override
            public void onFailure(String message) {

            }
        }, "publishtime");
    }
}
