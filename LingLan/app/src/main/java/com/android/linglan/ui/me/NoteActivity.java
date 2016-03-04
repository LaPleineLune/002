package com.android.linglan.ui.me;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;

import com.android.linglan.adapter.NoteAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.ui.R;
import com.android.linglan.utils.ToastUtil;
import com.android.linglan.widget.swipemenu.SwipeMenu;
import com.android.linglan.widget.swipemenu.SwipeMenuCreator;
import com.android.linglan.widget.swipemenu.SwipeMenuItem;
import com.android.linglan.widget.swipemenu.SwipeMenuListView;

/**
 * Created by LeeMy on 2016/1/7 0007.
 * 我的笔记
 */
public class NoteActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private SwipeMenuListView lv_note;
    private NoteAdapter adapter;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_note);
    }

    @Override
    protected void initView() {
        lv_note = (SwipeMenuListView) findViewById(R.id.lv_note);

    }

    @Override
    protected void initData() {
        setTitle("我的笔记", "删除");
        initSlide();
        adapter = new NoteAdapter(NoteActivity.this);
        lv_note.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        right.setOnClickListener(this);
        lv_note.setOnItemClickListener(this);
        lv_note.setOnItemLongClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right:
                if (right.getText().toString().equals("删除")) {
                    setTitle("我的笔记", "确认");
//                    right.setText("确认");
                    lv_note.setOnItemClickListener(null);
                    lv_note.setOnItemLongClickListener(null);
                    adapter.updateAdapter(true);
                } else {
                    setTitle("我的笔记", "删除");
//                    right.setText("删除");
                    lv_note.setOnItemClickListener(this);
                    lv_note.setOnItemLongClickListener(this);
                    adapter.updateAdapter(false);
                }
                break;
            default:
                break;
        }

}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtil.show(NoteActivity.this, "点击第" + position + "个", 1);
        Intent intent = new Intent();
        intent.putExtra("note", "preview");
        intent.setClass(NoteActivity.this, NoteWritePreviewActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtil.show(NoteActivity.this, "长按第" + position + "个被删除", 1);
        return false;
    }

    private void initSlide() {
        // TODO Auto-generated method stub
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @SuppressLint("ResourceAsColor")
            @Override

            public void create(SwipeMenu menu) {
//                LogUtils.v("SwipeMenuCreator==create");



                SwipeMenuItem deleteItem = new SwipeMenuItem(NoteActivity.this);
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                //                deleteItem.setIcon(R.drawable.ic_delete);
                //set a title
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(getResources().getColor(R.color.white));
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        lv_note.setMenuCreator(creator);

        // step 2. listener item click event
        lv_note.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                ToastUtil.show("onMenuItemClick==position="+position+"index=="+index);
//                LogUtils.v("onMenuItemClick==position="+position+"index=="+index);
//                switch (index) {
//                    case 0:
//                        // open
//                        //                        open(item);
//                        caseUuid=datas.get(position).getUuid();
//                        if(Constant.JIWANGSHI.equals(caseUuid)){
//                            LogUtils.v("删除既往史");
//                            buildAlert = ECAlertDialog.buildAlert(ctx, "您确认要删除此病历吗？删除后将无法恢复！",
//                                    "取消", "确定", new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {// 取消
//                                            // TODO Auto-generated method stub
//                                            buildAlert.dismiss();
//                                        }
//
//                                    }, new DialogInterface.OnClickListener() {// 下载升级
//
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            // TODO Auto-generated method stub
//                                            buildAlert.dismiss();
//                                            delHistory();
//                                        }
//                                    });
//                            //buildAlert.setTitle(R.string.app_tip);
//                            buildAlert.setCanceledOnTouchOutside(false);
//                            buildAlert.show();
//
//
//                        }else{
//                            buildAlert = ECAlertDialog.buildAlert(ctx, "您确认要删除此病历吗？删除后将无法恢复！",
//                                    "取消", "确定", new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {// 取消
//                                            // TODO Auto-generated method stub
//                                            buildAlert.dismiss();
//                                        }
//
//                                    }, new DialogInterface.OnClickListener() {// 确定
//
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            // TODO Auto-generated method stub
//                                            buildAlert.dismiss();
//                                            delCase(caseUuid);
//                                        }
//                                    });
//                            buildAlert.setCanceledOnTouchOutside(false);
//                            buildAlert.show();
//
////						delCase(caseUuid);
//
//                        }
//                        break;
//                    case 1:
//                        //没有执行
//                        // delete
//                        //					delete(item);
//                        //                        mAppList.remove(position);
//                        //                        mAdapter.notifyDataSetChanged();
//                        //					caseUuid=datas.get(position-1).getUuid();
//                        //					delCase();
//                        break;
//                }
                return false;
            }


        });
        // set SwipeListener

        lv_note.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
//                LogUtils.v("onSwipeEnd+"+position);
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
//                LogUtils.v("onSwipeEnd"+position);
            }
        });

        // set MenuStateChangeListener
        lv_note.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
//                LogUtils.v("onMenuOpen" + position);
            }

            @Override
            public void onMenuClose(int position) {
//                LogUtils.v("onMenuClose" + position);
            }
        });

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
