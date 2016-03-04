package com.android.linglan.base;

import android.support.v4.app.FragmentActivity;

/**
 * Created by LeeMy on 2016/1/6 0006.
 */
public class BaseFragmentActivity extends FragmentActivity {
    @Override
    protected void onResume() {
        super.onResume();
//    MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//    MobclickAgent.onPause(this);
    }
}
