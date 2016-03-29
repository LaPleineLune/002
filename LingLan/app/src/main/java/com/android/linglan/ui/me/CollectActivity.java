package com.android.linglan.ui.me;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.CollectArticleFragment;
import com.android.linglan.fragment.CollectSubjectFragment;
import com.android.linglan.ui.R;

/**
 * Created by LeeMy on 2016/1/7 0007.
 * 我的收藏
 */
public class CollectActivity extends BaseActivity implements View.OnClickListener {
    private TextView collect_article;
    private TextView collect_subject;
    private TextView collect_test;

    private FragmentManager fragmentManager;
    private CollectArticleFragment collectArticleFragment;
    private CollectSubjectFragment collectSubjectFragment;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_collect);
    }

    @Override
    protected void initView() {
        collect_article = (TextView) findViewById(R.id.collect_article);
        collect_subject = (TextView) findViewById(R.id.collect_subject);
        collect_test = (TextView) findViewById(R.id.collect_test);
    }

    @Override
    protected void initData() {
        setTitle("我的收藏", "编辑");
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.edit);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        fragmentManager = getSupportFragmentManager();
        // 第一次启动时选中第0个tab
        setTabSelection(0);

    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        collect_article.setTextColor(getResources().getColor(R.color.no_text_color_fragment_title));
        collect_subject.setTextColor(getResources().getColor(R.color.no_text_color_fragment_title));
        collect_test.setTextColor(getResources().getColor(R.color.no_text_color_fragment_title));

    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */

    private void hideFragments(FragmentTransaction transaction) {
        if (collectArticleFragment != null) {
            transaction.hide(collectArticleFragment);
        }
        if (collectSubjectFragment != null) {
            transaction.hide(collectSubjectFragment);
        }
    }

    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                collect_article.setTextColor(getResources().getColor(R.color.text_color_fragment_title));
                if (collectArticleFragment == null) {
                    collectArticleFragment = new CollectArticleFragment();
                    transaction.add(R.id.collect_content, collectArticleFragment,"tag1");
                } else {
                    transaction.show(collectArticleFragment);
                }
                break;
            case 1:
                collect_subject.setTextColor(getResources().getColor(R.color.text_color_fragment_title));
                if (collectSubjectFragment == null) {
                    collectSubjectFragment = new CollectSubjectFragment();
                    transaction.add(R.id.collect_content, collectSubjectFragment,"tag2");
                } else {
                    transaction.show(collectSubjectFragment);
                }
                break;
        }
        transaction.commit();
    }

    @Override
    protected void setListener() {
        collect_article.setOnClickListener(this);
        collect_subject.setOnClickListener(this);
        collect_test.setOnClickListener(this);
        right.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right:
                if (right.getText().toString().equals("编辑")) {
                    setTitle("我的收藏", "完成");
                    Drawable collectTopDrawable = getResources().getDrawable(R.drawable.edit_ok);
                    collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                    right.setCompoundDrawables(collectTopDrawable, null, null, null);
                    if (collectArticleFragment != null) {
                        collectArticleFragment.mSetVisibility(true);
                    }
                    if (collectSubjectFragment != null) {
                        collectSubjectFragment.mSetVisibility(true);
                    }
                    collect_article.setOnClickListener(null);
                    collect_subject.setOnClickListener(null);
                } else {
                    setTitle("我的收藏", "编辑");
                    Drawable collectTopDrawable = getResources().getDrawable(R.drawable.edit);
                    collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
                    right.setCompoundDrawables(collectTopDrawable, null, null, null);
                    if (collectArticleFragment != null) {
                        collectArticleFragment.mSetVisibility(false);
                    }
                    if (collectSubjectFragment != null) {
                        collectSubjectFragment.mSetVisibility(false);
                    }
                    collect_article.setOnClickListener(this);
                    collect_subject.setOnClickListener(this);
                }
                break;
            case R.id.collect_article:
                setTabSelection(0);
                break;
            case R.id.collect_subject:
                setTabSelection(1);
                break;
            case R.id.collect_test:
                setTabSelection(2);
                break;
            default:
                break;
        }
    }
}
