package com.android.linglan.ui.clinical;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.linglan.http.NetApi;
import com.android.linglan.http.PasserbyClient;
import com.android.linglan.ui.R;
import com.android.linglan.utils.HttpCodeJugementUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.ToastUtil;
import com.bm.library.PhotoView;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class ViewPhotoActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ArrayList<String> imgUrls = new ArrayList<String>();
    private ArrayList<String> mDataMediaId = new ArrayList<String>();
    private ArrayList<String> deleteMediaId = new ArrayList<String>();
    private ArrayList<String> categoryNames = new ArrayList<String>();
    private List<PhotoView> views = new ArrayList<PhotoView>();
    private RelativeLayout back;
    private ImageView right;
    private MyAdapter myAdapter;
    private TextView tv_view_photo_classify;
    private ImageOptions options = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.CENTER_INSIDE).build();
    private int currrentItem = 0;
    Intent data = new Intent();
    private String clinicalDetailsActivity;
    private String multiImageSelectorFragment;
    private RelativeLayout ll_view_photo_classify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_clinical_view_photo);
        getIntentData();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        back = (RelativeLayout) findViewById(R.id.back);
        right = (ImageView) findViewById(R.id.right);
        ll_view_photo_classify = (RelativeLayout) findViewById(R.id.ll_view_photo_classify);

        tv_view_photo_classify = (TextView) findViewById(R.id.tv_view_photo_classify);

        if (clinicalDetailsActivity != null && clinicalDetailsActivity.equals("clinicalDetailsActivity")) {
            right.setVisibility(View.GONE);
        }

        if (multiImageSelectorFragment != null && multiImageSelectorFragment.equals("multiImageSelectorFragment")) {
            right.setVisibility(View.GONE);
            ll_view_photo_classify.setVisibility(View.GONE);
        }

        initView();
        onClick();
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imgUrls = bundle.getStringArrayList("imgUrls");
            mDataMediaId = bundle.getStringArrayList("mDataMediaId");
            currrentItem = bundle.getInt("current");
            categoryNames = bundle.getStringArrayList("categoryNames");
            clinicalDetailsActivity = bundle.getString("clinicalDetailsActivity");
            multiImageSelectorFragment = bundle.getString("multiImageSelectorFragment");
        }

    }

    private void initView() {
        views.clear();
        for (int i = 0; i < imgUrls.size(); i++) {
            PhotoView photoView = new PhotoView(this);
            photoView.enable();
            photoView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            views.add(photoView);
        }
        myAdapter = new MyAdapter();
        mViewPager.setAdapter(myAdapter);
        mViewPager.setCurrentItem(currrentItem);

        if (imgUrls != null && imgUrls.size() != 0) {
            x.image().bind(views.get(currrentItem), imgUrls.get(currrentItem), options);
            if (categoryNames != null && categoryNames.size() != 0) {
                tv_view_photo_classify.setText("分类：" + categoryNames.get(currrentItem));
            }
        }

    }

    private void onClick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.putStringArrayListExtra("mDataMediaId", mDataMediaId);
                setResult(101, data);
                finish();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClinicalMultiMedia(mDataMediaId.get(currrentItem));
                LogUtil.e(imgUrls.size() + "删除的前数组的长度1111");
                imgUrls.remove(currrentItem);
//                deleteMediaId.add(mDataMediaId.get(currrentItem));
                mDataMediaId.remove(currrentItem);
                categoryNames.remove(currrentItem);
//                LogUtil.e(currrentItem + "删除的位置删除的位置");
//                LogUtil.e(imgUrls.size() + "imgUrls删除的后数组的长度");
//                LogUtil.e(mDataMediaId.size() + "mDataMediaId删除的后数组的长度");
                if (imgUrls.size() + 1 == 1) {
                    data.putStringArrayListExtra("mDataMediaId", mDataMediaId);
                    setResult(101, data);
                    finish();
                } else {
                    if (currrentItem == 0) {
                        currrentItem = currrentItem;
                        LogUtil.e(currrentItem + "删除后变动的位置111111111");
                    } else if (currrentItem == imgUrls.size()) {
                        currrentItem = currrentItem - 1;
                        LogUtil.e(currrentItem + "删除后变动的位置2222222222");
                    } else {
                        currrentItem--;
                        LogUtil.e(currrentItem + "删除后变动的位置3333333333");
                    }
                    initView();
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

//                if (imgUrls != null && imgUrls.size() != 0) {
//                    x.image().bind(views.get(currrentItem), imgUrls.get(currrentItem), options);
//                    if (categoryNames != null && categoryNames.size() != 0) {
//                        tv_view_photo_classify.setText("分类：" + categoryNames.get(currrentItem));
//                    }
//                }

                x.image().bind(views.get(position), imgUrls.get(position), options);
                if (categoryNames != null && categoryNames.size() != 0) {
                tv_view_photo_classify.setText("分类：" + categoryNames.get(position));
                }
                currrentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            data.putStringArrayListExtra("mDataMediaId", mDataMediaId);
            setResult(101, data);
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return views.size();
        }

        @Override
        public Object instantiateItem(View arg0, final int arg1) {
            try {
                ((ViewPager) arg0).addView(views.get(arg1));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return views.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    private void deleteClinicalMultiMedia(String mediaid) {
        NetApi.deleteClinicalMultiMedia(new PasserbyClient.HttpCallback() {
            @Override
            public void onSuccess(String result) {
                if (!HttpCodeJugementUtil.HttpCodeJugementUtil(result, ViewPhotoActivity.this)) {
                    return;
                }
                ToastUtil.show("已删除");
            }

            @Override
            public void onFailure(String message) {

            }
        }, mediaid);
    }
}
