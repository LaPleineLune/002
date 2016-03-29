package com.android.linglan.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.ui.R;
import com.umeng.analytics.MobclickAgent;
//import com.umeng.message.PushAgent;

/**
 * Created by LeeMy on 2016/1/6 0006.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    private String tag = getClass().getSimpleName();
    protected TextView title,right;
    protected RelativeLayout back;
    protected View rootView;

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        title = (TextView) rootView.findViewById(R.id.title);
//        right = (TextView) rootView.findViewById(R.id.right);
//        back = (RelativeLayout) rootView.findViewById(R.id.back);
        initView();
        initData();
        setListener();

//        PushAgent.getInstance(getActivity()).onAppStart();
    }
//    public void setTitle(String titleStr, String rightStr){
//        title.setText(titleStr);
//        right.setText(rightStr);
//        back.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });
//    }
    protected abstract void initView();

    protected abstract void initData();

    protected abstract void setListener();

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(tag);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(tag);
    }

}
