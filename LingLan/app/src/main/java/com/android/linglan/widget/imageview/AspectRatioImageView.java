package com.android.linglan.widget.imageview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by changyizhang on 11/25/14.
 */
public class AspectRatioImageView extends ImageView {

  public AspectRatioImageView(Context context) {
    super(context);
  }

  public AspectRatioImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (getDrawable() == null) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      return;
    }
    int width = MeasureSpec.getSize(widthMeasureSpec);
    int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
    setMeasuredDimension(width, height);
  }
}
