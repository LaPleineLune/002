package com.android.linglan.ui.clinical;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.PictureContrastAffectedPartFragment;
import com.android.linglan.fragment.PictureContrastPrescribedFragment;
import com.android.linglan.fragment.PictureContrastTonguePictureFragment;
import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/4/14 0014.
 * 图片对比界面
 */
public class PictureContrastActivity extends BaseActivity {
    private TextView tv_prescribed;
    private TextView tv_affected_part;
    private TextView tv_tongue_picture;

    private FragmentManager fragmentManager;
    private PictureContrastPrescribedFragment prescribedFragment;
    private PictureContrastAffectedPartFragment affectedPartFragment;
    private PictureContrastTonguePictureFragment tonguePictureFragment;
    @Override
    protected void setView() {
        setContentView(R.layout.activity_picture_contrast);
    }

    @Override
    protected void initView() {
        tv_prescribed = (TextView) findViewById(R.id.tv_prescribed);
        tv_affected_part = (TextView) findViewById(R.id.tv_affected_part);
        tv_tongue_picture = (TextView) findViewById(R.id.tv_tongue_picture);
    }

    @Override
    protected void initData() {
        setTitle("图片对比", "");
        fragmentManager = getSupportFragmentManager();
        // 第一次启动时选中第0个tab
        setTabSelection(0);

    }

    @Override
    protected void setListener() {
        tv_prescribed.setOnClickListener(this);
        tv_affected_part.setOnClickListener(this);
        tv_tongue_picture.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_prescribed:
                setTabSelection(0);
                break;
            case R.id.tv_affected_part:
                setTabSelection(1);
                break;
            case R.id.tv_tongue_picture:
                setTabSelection(2);
                break;
            default:
                break;
        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        tv_prescribed.setTextColor(getResources().getColor(R.color.no_text_color_fragment_title));
        tv_affected_part.setTextColor(getResources().getColor(R.color.no_text_color_fragment_title));
        tv_tongue_picture.setTextColor(getResources().getColor(R.color.no_text_color_fragment_title));

    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */

    private void hideFragments(FragmentTransaction transaction) {
        if (prescribedFragment != null) {
            transaction.hide(prescribedFragment);
        }
        if (affectedPartFragment != null) {
            transaction.hide(affectedPartFragment);
        }
        if (tonguePictureFragment != null) {
            transaction.hide(tonguePictureFragment);
        }
    }

    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                tv_prescribed.setTextColor(getResources().getColor(R.color.carminum));
                if (prescribedFragment == null) {
                    prescribedFragment = new PictureContrastPrescribedFragment();
                    transaction.add(R.id.picture_contrast, prescribedFragment,"tag1");
                } else {
                    transaction.show(prescribedFragment);
                }
                break;
            case 1:
                tv_affected_part.setTextColor(getResources().getColor(R.color.carminum));
                if (affectedPartFragment == null) {
                    affectedPartFragment = new PictureContrastAffectedPartFragment();
                    transaction.add(R.id.picture_contrast, affectedPartFragment,"tag2");
                } else {
                    transaction.show(affectedPartFragment);
                }
                break;
            case 2:
                tv_tongue_picture.setTextColor(getResources().getColor(R.color.carminum));
                if (tonguePictureFragment == null) {
                    tonguePictureFragment = new PictureContrastTonguePictureFragment();
                    transaction.add(R.id.picture_contrast, tonguePictureFragment,"tag2");
                } else {
                    transaction.show(tonguePictureFragment);
                }
                break;
        }
        transaction.commit();
    }
}
