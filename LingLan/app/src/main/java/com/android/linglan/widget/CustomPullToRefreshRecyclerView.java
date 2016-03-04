package com.android.linglan.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;

import com.android.linglan.ui.R;
import com.android.linglan.utils.NetworkUtil;
import com.jingge.pulltorefreshlibrary.PullToRefreshBase;
import com.jingge.pulltorefreshlibrary.PullToRefreshRecyclerView;
import com.jingge.pulltorefreshlibrary.internal.LoadingLayout;


/**
 * Created by Changyi on 7/2/15.
 */
public class CustomPullToRefreshRecyclerView extends PullToRefreshRecyclerView {
  /* WTF: The first child item MUST NOT define top-margin, otherwise pull-to-refresh would not work.
     BATNA： Set top-padding for this container instead. */

  private AnimationStyle mStartLoadingAnimationStyle;
  private AnimationStyle mEndLoadingAnimationStyle;

  private RefreshCallback refreshCallback;

  private Mode lastMode;

  private OnHeaderVisibilityChangeListener onHeaderVisibilityChangeListener;

  public CustomPullToRefreshRecyclerView(Context context) {
    super(context);
  }

  public CustomPullToRefreshRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CustomPullToRefreshRecyclerView(Context context, Mode mode) {
    super(context, mode);
  }

  public CustomPullToRefreshRecyclerView(Context context, Mode mode, AnimationStyle style) {
    super(context, mode, style);
  }

  public void setRefreshCallback(RefreshCallback refreshCallback) {
    this.refreshCallback = refreshCallback;
    setOnRefreshListener(onRefreshListener);
  }

  @Override
  protected LoadingLayout createLoadingLayout(Context context, Mode mode, TypedArray attrs) {
    LoadingLayout loadingLayout;
    if (Mode.PULL_FROM_START.equals(mode) && mStartLoadingAnimationStyle != null) {
      loadingLayout =
          mStartLoadingAnimationStyle.createLoadingLayout(context, mode,
              getPullToRefreshScrollDirection(), attrs);
    } else if (Mode.PULL_FROM_END.equals(mode) && mEndLoadingAnimationStyle != null) {
      loadingLayout =
          mEndLoadingAnimationStyle.createLoadingLayout(context, mode,
              getPullToRefreshScrollDirection(), attrs);
    } else {
      loadingLayout = super.createLoadingLayout(context, mode, attrs);
    }

    return loadingLayout;
  }

  @Override
  protected void init(Context context, AttributeSet attrs) {
    TypedArray a =
        context.obtainStyledAttributes(attrs, R.styleable.CustomPullToRefreshRecyclerView);
    if (a.hasValue(R.styleable.CustomPullToRefreshRecyclerView_ptr_animation_style_start)) {
      mStartLoadingAnimationStyle = AnimationStyle.mapIntToValue(a.getInteger(
          R.styleable.CustomPullToRefreshRecyclerView_ptr_animation_style_start, 0));
    }

    if (a.hasValue(R.styleable.CustomPullToRefreshRecyclerView_ptr_animation_style_end)) {
      mEndLoadingAnimationStyle = AnimationStyle.mapIntToValue(a.getInteger(
          R.styleable.CustomPullToRefreshRecyclerView_ptr_animation_style_end, 0));
    }
    a.recycle();

    super.init(context, attrs);

    lastMode = getMode();
  }

  @Override
  public void onRefreshComplete() {
    if (!NetworkUtil.isNetworkConnected(getContext())) {
      new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
        @Override
        public void run() {
          CustomPullToRefreshRecyclerView.super.onRefreshComplete();
        }
      }, 1000);
    } else {
      super.onRefreshComplete();
    }
  }

  public void checkToStopLoadingMore(String responseString) {
    /*
     * CommonProtocol result = JsonUtil.json2Bean(responseString, CommonProtocol.class);
     * if (result != null && result.header != null && "2".equals(result.header.status)) {
     * // Stop loading more if the status equals 2 that means no more data returns in next page.
     * new Handler(Looper.getMainLooper()).post(new Runnable() {
     * @Override
     * public void run() {
     * ToastUtil.show("没有更多了!");
     * }
     * });
     * lastMode = getMode();
     * setMode(Mode.PULL_FROM_START);
     * }
     */
  }

  @Override
  protected void onHeaderVisibilityChanged(boolean visible) {
    if (onHeaderVisibilityChangeListener != null) {
      onHeaderVisibilityChangeListener.onHeaderVisibilityChanged(visible);
    }
  }

  private OnRefreshListener2 onRefreshListener = new OnRefreshListener2() {
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
      // Reset mode.
      setMode(lastMode);

      if (refreshCallback != null) {
        refreshCallback.onPullDownToRefresh();
      }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
      if (refreshCallback != null) {
        refreshCallback.onPullUpToLoadMore();
      }
    }
  };

  public void setOnHeaderVisibilityChangeListener(
      OnHeaderVisibilityChangeListener onHeaderVisibilityChangeListener) {
    this.onHeaderVisibilityChangeListener = onHeaderVisibilityChangeListener;
  }

  public static interface OnHeaderVisibilityChangeListener {
    public void onHeaderVisibilityChanged(boolean visible);
  }

  public static interface RefreshCallback {
    public void onPullDownToRefresh();

    public void onPullUpToLoadMore();
  }
}
