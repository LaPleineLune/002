package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.adapter.clinical.ClinicalDetailsAdapter;
import com.android.linglan.base.BaseActivity;
import com.android.linglan.ui.R;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;

/**
 * Created by LeeMy on 2016/4/7 0007.
 * 病历详情页
 */
public class ClinicalDetailsActivity extends BaseActivity {
    protected static final int REQUEST_FAILURE = 0;
    protected static final int REQUEST_SUCCESS = 1;

    private PopupWindow popupWindow;
    private View popView;
    private TextView tv_picture_contrast;

    private RelativeLayout rl_clinical_details;
    private LinearLayout ll_no_network;
    private Button bt_course_of_disease;
    private Button bt_weichat_flup;
    private PtrClassicFrameLayout recycler_view_clinical_details;
    private RecyclerView rec_clinical_details;
    private RecyclerAdapterWithHF mAdapter;
    private ClinicalDetailsAdapter clinicalDetailsAdapter;
    private Intent intent = null;
    private int page;//页码

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    rl_clinical_details.setVisibility(View.VISIBLE);
                    ll_no_network.setVisibility(View.GONE);
                    break;
                case REQUEST_FAILURE:
                    rl_clinical_details.setVisibility(View.GONE);
                    ll_no_network.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void setView() {
        setContentView(R.layout.activity_clinical_details);
        popView = LayoutInflater.from(this).inflate(R.layout.popupview_clinical_details_function, null);

    }

    @Override
    protected void initView() {
        rl_clinical_details = (RelativeLayout) findViewById(R.id.rl_clinical_details);
        ll_no_network = (LinearLayout) findViewById(R.id.ll_no_network);
        bt_course_of_disease = (Button) findViewById(R.id.bt_course_of_disease);
        bt_weichat_flup = (Button) findViewById(R.id.bt_weichat_flup);
        recycler_view_clinical_details = (PtrClassicFrameLayout) findViewById(R.id.recycler_view_clinical_details);
        rec_clinical_details = (RecyclerView) findViewById(R.id.rec_clinical_details);

        tv_picture_contrast = (TextView) popView.findViewById(R.id.tv_picture_contrast);

    }

    @Override
    protected void initData() {
        setTitle("病历详情", "");
        Drawable collectTopDrawable = getResources().getDrawable(R.drawable.function);
        collectTopDrawable.setBounds(0, 0, collectTopDrawable.getMinimumWidth(), collectTopDrawable.getMinimumHeight());
        right.setCompoundDrawables(collectTopDrawable, null, null, null);
        intent = new Intent();
        popupWindow = new PopupWindow(this);
        rec_clinical_details.setLayoutManager(new LinearLayoutManager(this));
        rec_clinical_details.setHasFixedSize(true);
        clinicalDetailsAdapter = new ClinicalDetailsAdapter(this);
        mAdapter = new RecyclerAdapterWithHF(clinicalDetailsAdapter);
        rec_clinical_details.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        right.setOnClickListener(this);
        bt_course_of_disease.setOnClickListener(this);
        bt_weichat_flup.setOnClickListener(this);
        tv_picture_contrast.setOnClickListener(this);
        ll_no_network.setOnClickListener(this);
        //下拉刷新
        recycler_view_clinical_details.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
//                getAllSubject(page, orderid, cateid);
            }
        });

        //上拉刷新
        recycler_view_clinical_details.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                page++;
//                getAllSubject(page, orderid, cateid);
                recycler_view_clinical_details.loadMoreComplete(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right:
                showPopup(v);
                break;
            case R.id.ll_no_network:
                initData();
                break;
            case R.id.bt_course_of_disease:
                intent.setClass(this, CourseOfDiseaseActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_weichat_flup:
                intent.setClass(this, WeichatFlupActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_picture_contrast:
                intent.setClass(this, PictureContrastActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
                break;
        }
    }

    private void showPopup(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        popView.measure(w, h);

        int height = popView.getMeasuredHeight();
        int width = popView.getMeasuredWidth();
        popupWindow = new PopupWindow(popView, width, (int)height);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        int x = getResources().getDimensionPixelSize(R.dimen.dp80);
        int y = getResources().getDimensionPixelSize(R.dimen.dp12);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]-x, location[1] + v.getHeight()-y);

    }
}
