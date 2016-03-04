package com.android.linglan.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

public class BouncyScrollView extends ScrollView {
  private View inner;
  private float y;
  private Rect normal = new Rect();
  private boolean animationFinish = true;

  public BouncyScrollView(Context context) {
    super(context);
  }

  public BouncyScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    if (getChildCount() > 0) {
      inner = getChildAt(0);
    }
  }


  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    return super.onInterceptTouchEvent(ev);
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    if (inner == null) {
      return super.onTouchEvent(ev);
    } else {
      commOnTouchEvent(ev);
    }
    return super.onTouchEvent(ev);
  }

  public void commOnTouchEvent(MotionEvent ev) {
    if (animationFinish) {
      int action = ev.getAction();
      switch (action) {
        case MotionEvent.ACTION_DOWN:
          y = ev.getY();
          super.onTouchEvent(ev);
          break;
        case MotionEvent.ACTION_UP:
          y = 0;
          if (isNeedAnimation()) {
            animation();
          }
          super.onTouchEvent(ev);
          break;
        case MotionEvent.ACTION_MOVE:
          final float preY = y == 0 ? ev.getY() : y;
          float nowY = ev.getY();
          int deltaY = (int) (preY - nowY);
          y = nowY;

          if (isNeedMove()) {
            if (normal.isEmpty()) {
              normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
            }

            inner.layout(inner.getLeft(), inner.getTop() - deltaY / 2, inner.getRight(), inner.getBottom() - deltaY / 2);
          } else {
            super.onTouchEvent(ev);
          }
          break;
        default:
          break;
      }
    }
  }

  public void animation() {
    TranslateAnimation ta = new TranslateAnimation(0, 0, 0, normal.top - inner.getTop());
    ta.setDuration(200);
    ta.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        animationFinish = false;

      }

      @Override
      public void onAnimationRepeat(Animation animation) {

      }

      @Override
      public void onAnimationEnd(Animation animation) {
        inner.clearAnimation();

        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
        animationFinish = true;
      }
    });
    inner.startAnimation(ta);
  }

  public boolean isNeedAnimation() {
    return !normal.isEmpty();
  }

  public boolean isNeedMove() {
    int offset = inner.getMeasuredHeight() - getHeight();
    int scrollY = getScrollY();
    if (scrollY == 0 || scrollY == offset) {
      return true;
    }
    return false;
  }

}  