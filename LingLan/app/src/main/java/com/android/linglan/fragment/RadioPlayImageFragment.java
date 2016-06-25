package com.android.linglan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.linglan.base.BaseFragment;
import com.android.linglan.ui.R;
import com.android.linglan.utils.LogUtil;

/**
 * Created by LeeMy on 2016/6/15 0015.
 * 播放界面的图形预览界面
 */
public class RadioPlayImageFragment extends BaseFragment {
    private View rootView;
    private String radioPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle args = getArguments();
        radioPhoto = args != null ? args.getString("radioPhoto") : "";
        LogUtil.e("radioPhoto=" + radioPhoto);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_radio_play_image, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }
}
