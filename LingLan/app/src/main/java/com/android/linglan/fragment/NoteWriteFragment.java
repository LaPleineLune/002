package com.android.linglan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.linglan.base.BaseFragment;
import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/1/16 0016.
 */
public class NoteWriteFragment extends BaseFragment {
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_note_write, null);
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
//        setTitle("写笔记", "保存");

    }

    @Override
    protected void setListener() {

    }
}
