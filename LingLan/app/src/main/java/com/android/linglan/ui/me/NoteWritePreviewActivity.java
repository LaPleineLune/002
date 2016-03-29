package com.android.linglan.ui.me;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.android.linglan.base.BaseActivity;
import com.android.linglan.fragment.NotePreviewFragment;
import com.android.linglan.fragment.NoteWriteFragment;
import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;

/**
 * Created by LeeMy on 2016/1/16 0016.
 * 写笔记或者预览笔记
 */
public class NoteWritePreviewActivity extends BaseActivity implements View.OnClickListener {
    private FragmentManager fragmentManager;
    private NoteWriteFragment noteWriteFragment;
    private NotePreviewFragment notePreviewFragment;
    private String articleId;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_write_preview);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        String note  = getIntent().getStringExtra("note");
        articleId = getIntent().getStringExtra("articleId");
        fragmentManager = getSupportFragmentManager();
        if (note != null && !note.equals("") && note.equals("preview")) {
            setTitle("笔记预览", "修改");
            setTabSelection(1);
        } else {
            setTitle("写笔记", "保存");
            setTabSelection(0);
        }

    }

    @Override
    protected void setListener() {
        right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right:
                if (right.getText().toString().equals("修改")) {
                    setTitle("写笔记", "保存");
                    setTabSelection(0);
                } else {
                    addDetailsArticleNote(articleId, "文章内容");
                    setTitle("笔记预览", "修改");
                    setTabSelection(1);
                }
                break;
        }
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */

    private void hideFragments(FragmentTransaction transaction) {
        if (noteWriteFragment != null) {
            transaction.hide(noteWriteFragment);
        }
        if (notePreviewFragment != null) {
            transaction.hide(notePreviewFragment);
        }
    }

    private void setTabSelection(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                if (noteWriteFragment == null) {
                    noteWriteFragment = new NoteWriteFragment();
                    transaction.add(R.id.note_content, noteWriteFragment,"tag1");
                } else {
                    transaction.show(noteWriteFragment);
                }
                break;
            case 1:
                if (notePreviewFragment == null) {
                    notePreviewFragment = new NotePreviewFragment();
                    transaction.add(R.id.note_content, notePreviewFragment,"tag2");
                } else {
                    transaction.show(notePreviewFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void addDetailsArticleNote(String articleid, String notescontent){
        NetApi.addDetailsArticleNote(new PasserbyClient.HttpCallback() {

            @Override
            public void onSuccess(String result) {
                if(!HttpCodeJugementUtil.HttpCodeJugementUtil(result,NoteWritePreviewActivity.this)){
                    return;
                }
            }

            @Override
            public void onFailure(String message) {

            }
        }, articleid, notescontent);
    }
}
