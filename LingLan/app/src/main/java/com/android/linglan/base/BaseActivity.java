package com.android.linglan.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeMy on 2016/1/7 0007.
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {//AppCompatActivity
    private String tag = getClass().getSimpleName();
    private static List<BaseActivity> childlist;
    protected TextView title, right;
    protected RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        title = (TextView) findViewById(R.id.title);
        right = (TextView) findViewById(R.id.right);
        back = (RelativeLayout) findViewById(R.id.back);
        initView();
        initData();
        setListener();
        if (childlist == null) {
            childlist = new ArrayList<BaseActivity>();
        }
        childlist.add(this);
    }

    public void setTitle(String titleStr, String rightStr) {
        title.setText(titleStr);
        right.setText(rightStr);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doBack();
            }
        });
    }

    public void jumpActivity(Context context, Class activity) {
        Intent intent = new Intent(context,activity);
        startActivity(intent);
    }

    protected void doBack() {
        this.finish();
    }

    protected abstract void setView();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void setListener();

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(tag);
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        childlist.remove(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageStart(tag);
//        MobclickAgent.onResume(this);
    }

    protected void backTo(Class<?> cls) {
        for (int i = childlist.size() - 1; i >= 0; i--) {
            if (cls.getSimpleName().equals(
                    childlist.get(i).getClass().getSimpleName())) {
                break;
            }
            childlist.get(i).finish();
        }
    }

    protected void finishAll() {
        for (int i = childlist.size() - 1; i >= 0; i--) {
            childlist.get(i).finish();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
