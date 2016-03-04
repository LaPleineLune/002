package com.android.linglan.widget;

import android.content.Context;
import android.content.res.Resources;
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

import com.android.linglan.ui.R;

/**
 * Created by changyizhang on 12/15/14.
 */
public class FixedTabPageIndicator extends LinearLayout implements PageIndicator {

  private ViewPager mViewPager;
  private int mSelectedIndex;
  private ViewPager.OnPageChangeListener mListener;

  public FixedTabPageIndicator(Context context) {
    super(context);
  }

  public FixedTabPageIndicator(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public FixedTabPageIndicator(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  private final OnClickListener mTabClickListener = new OnClickListener() {
    public void onClick(View view) {
      int index = (Integer) view.getTag();
      mViewPager.setCurrentItem(index);
    }
  };

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

  public void showMessageIndicator(int index) {
    View child = getChildAt(index);

  }

  public void hideMessageIndicator(int index) {
    View child = getChildAt(index);

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
      View child = getChildAt(i);
      boolean isSelected = (i == item);
      ImageView icon = (ImageView) child.findViewById(R.id.tab_icon);
      icon.setSelected(isSelected);

      // No scroll animation for now.
      /*if (isSelected) {
        animateToIcon(item);
      }*/
    }
  }

  @Override
  public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
    mListener = listener;
  }

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
      if (tabPagerAdapter != null) {
        iconResId = tabPagerAdapter.getIconResId(i);
      }
      addTab(i, iconResId);
    }
    if (mSelectedIndex > count) {
      mSelectedIndex = count - 1;
    }
    setCurrentItem(mSelectedIndex);
    requestLayout();
  }

  private void addTab(int index, int iconResId) {
    View tabView = LayoutInflater.from(getContext()).inflate(R.layout.tab_item_layout, this, false);
    ImageView icon = (ImageView) tabView.findViewById(R.id.tab_icon);

    // Drawable drawable = getResources().getDrawable(iconResId);
    Resources resources = getResources();
    Drawable drawable =
        new TabIconStateListDrawable(resources.getDrawable(iconResId),
            resources.getColor(R.color.base_color_orange));

    int sdk = android.os.Build.VERSION.SDK_INT;
    if(sdk >= android.os.Build.VERSION_CODES.JELLY_BEAN){
      icon.setBackground(drawable);
    }else {
      icon.setBackgroundDrawable(drawable);
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
      addState(new int[] { android.R.attr.state_selected}, drawable);
      addState(new int[] {}, drawable);
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
//      mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }
  }

  @Override
  public void onPageSelected(int position) {
    setCurrentItem(position);
    if (mListener != null) {
//      mListener.onPageSelected(position);
    }
  }

  @Override
  public void onPageScrollStateChanged(int state) {
    if (mListener != null) {
//      mListener.onPageScrollStateChanged(state);
    }
  }

  public interface CustomTabPagerAdapter {
    int getIconResId(int index);
  }
}
