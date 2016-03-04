package com.android.linglan.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.linglan.base.BaseFragment;
import com.android.linglan.ui.R;
import com.android.linglan.ui.homepage.ArticleDetailsActivity;

/**
 * Created by LeeMy on 2016/1/16 0016.
 */
public class NotePreviewFragment extends BaseFragment {
//    textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
    private Intent intent = null;
    private View rootView;
    private TextView note_preview_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_note_preview, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView() {
        note_preview_title = (TextView) rootView.findViewById(R.id.note_preview_title);

    }

    @Override
    protected void initData() {
//        setTitle("笔记预览", "修改");
        note_preview_title.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
        note_preview_title.getPaint().setAntiAlias(true);//抗锯齿

    }

    @Override
    protected void setListener() {
        note_preview_title.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        intent = new Intent();
        switch (v.getId()) {
            case R.id.note_preview_title:
//                intent.setClass(getActivity(), ArticleDetailsActivity.class);
//                startActivity(intent);
                break;
        }
    }
}
