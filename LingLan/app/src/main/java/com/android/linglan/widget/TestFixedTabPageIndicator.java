package com.android.linglan.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.linglan.ui.MainActivity;
import com.android.linglan.ui.R;
import com.android.linglan.utils.GuideViewUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.UmengBuriedPointUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by changyizhang on 12/15/14.
 */
public class TestFixedTabPageIndicator extends LinearLayout implements TestPageIndicator {

    private ViewPager mViewPager;
    private int mSelectedIndex;
    private ViewPager.OnPageChangeListener mListener;
    private GuideViewUtil mGuideViewUtil;
    private MainActivity context;

    public TestFixedTabPageIndicator(Context context) {
        super(context);
    }

    public TestFixedTabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestFixedTabPageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            int index = (Integer) view.getTag();
            switch (index) {
                case 0:
                    MobclickAgent.onEvent(context, UmengBuriedPointUtil.ManualStudy);
                    break;
                case 1:
                    MobclickAgent.onEvent(context, UmengBuriedPointUtil.ManualClinical);
                    if (!SharedPreferencesUtil.getBoolean("isClinicalShowed", false)) {
                        mGuideViewUtil = new GuideViewUtil(context, R.drawable.guide_clinical_0);
                    }
                    break;
                case 2:
                    MobclickAgent.onEvent(context, UmengBuriedPointUtil.ManualMy);
                    break;
            }
//            if (index == 1) {
//                if (!SharedPreferencesUtil.getBoolean("isClinicalShowed", false)) {
//                    mGuideViewUtil = new GuideViewUtil(context, R.drawable.guide_clinical_0);
//                }
//            }
            mViewPager.setCurrentItem(index);
        }
    };

    public void getMainContext(MainActivity context) {
        this.context = context;
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedIndex = item;
        mViewPager.setCurrentItem(item);

        int tabCount = getChildCount();
        for (int i = 0; i < tabCount; i++) {
            onItemStatusChange(getChildAt(i), i == item);
        }
    }

    protected void onItemStatusChange(View itemView, boolean selected) {
        View view = itemView.findViewById(R.id.tab_icon);
        if (view != null) {
            view.setSelected(selected);
        }
        view = itemView.findViewById(R.id.tab_label);
        if (view != null) {
            view.setSelected(selected);
        }
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

//  public void showMessageIndicator(int index, int sumCount) {
//    View child = getChildAt(index);
//    TextView sumCountView = (TextView) child.findViewById(R.id.message_indicator);
//    sumCountView.setVisibility(VISIBLE);
//    sumCountView.setText(sumCount + "");
//  }
//
//  public void hideMessageIndicator(int index) {
//    View child = getChildAt(index);
//    child.findViewById(R.id.message_indicator).setVisibility(GONE);
//  }

    @Override
    public void notifyDataSetChanged() {
        removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        CustomTabPagerAdapter tabPagerAdapter = null;
        if (adapter instanceof CustomTabPagerAdapter) {
            tabPagerAdapter = (CustomTabPagerAdapter) adapter;
        }

        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            int iconResId = 0;
            int labelResId = 0;
            if (tabPagerAdapter != null) {
                iconResId = tabPagerAdapter.getIconResId(i);
                labelResId = tabPagerAdapter.getLabelResId(i);
            }
            addTab(i, iconResId, labelResId);
        }
        if (mSelectedIndex > count) {
            mSelectedIndex = count - 1;
        }
        setCurrentItem(mSelectedIndex);
        requestLayout();
    }

    private void addTab(int index, int iconResId, int labelResId) {
        View tabView = LayoutInflater.from(getContext()).inflate(R.layout.test_tab_item_layout, this, false);
        ImageView icon = (ImageView) tabView.findViewById(R.id.tab_icon);
        TextView label = (TextView) tabView.findViewById(R.id.tab_label);

        if (icon != null && iconResId > 0) {
            icon.setImageResource(iconResId);
        }

        if (label != null && labelResId > 0) {
            label.setText(labelResId);
        }

        tabView.setFocusable(true);
        tabView.setOnClickListener(mTabClickListener);
        tabView.setTag(index);
        addView(tabView, new LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
    }

    public static class TabIconStateListDrawable extends StateListDrawable {

        private int selectionColor;

        public TabIconStateListDrawable(Drawable drawable, int selectionColor) {
            super();
            this.selectionColor = selectionColor;
            addState(new int[]{android.R.attr.state_selected}, drawable);
            addState(new int[]{}, drawable);
        }

        public void setSelectionColor(int selectionColor) {
            this.selectionColor = selectionColor;
        }

        @Override
        protected boolean onStateChange(int[] states) {
            boolean isStatePressedInArray = false;
            for (int state : states) {
                if (state == android.R.attr.state_selected) {
                    isStatePressedInArray = true;
                }
            }
            if (isStatePressedInArray) {
                super.setColorFilter(selectionColor, PorterDuff.Mode.SRC_IN);
            } else {
                super.clearColorFilter();
            }
            return super.onStateChange(states);
        }

        @Override
        public boolean isStateful() {
            return true;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    public interface CustomTabPagerAdapter {
        int getIconResId(int index);

        int getLabelResId(int index);
    }
}
