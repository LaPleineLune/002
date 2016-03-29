package com.android.linglan.utils;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by changyizhang on 12/10/14.
 */
public class ImageUtil {

//  try {
//                ImageUtil.loadImageAsync(控件对象,
//                        R.dimen.dp84, R.dimen.dp68,
//                        R.drawable.default_image（默认图）, url, null);
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
  public static void loadImageAsync(ImageView imageView, int targetWidthResId,
      int targetHeightResId, int placeholderResId, String url,
      Callback callback) {
    if (imageView == null) {
      return;
    }
    if (TextUtils.isEmpty(url)) {
      if (placeholderResId > 0) {
        imageView.setImageResource(placeholderResId);
        imageView.setVisibility(View.VISIBLE);
      } else {
        imageView.setVisibility(View.INVISIBLE);
      }
      return;
    }
    
    imageView.setVisibility(View.VISIBLE);
    RequestCreator requestCreator = Picasso.with(imageView.getContext()).load(url);
    if (placeholderResId >= 0) {
      requestCreator.placeholder(placeholderResId);
    }
    if (targetWidthResId >= 0 && targetHeightResId >= 0) {
      requestCreator.resizeDimen(targetWidthResId, targetHeightResId);
    }
    requestCreator.into(imageView, callback);
  }

  // ImageUtil.loadImageAsync(控件对象, url);
  public static void loadImageAsync(ImageView imageView, String url) {
    loadImageAsync(imageView, url, null);
  }

  public static void loadImageAsync(ImageView imageView, String url, Callback callback) {
    loadImageAsync(imageView, -1, -1, -1, url, callback);
  }

  // ImageUtil.loadImageAsync(控件对象, url, R.drawable.default_image（默认图）);
  public static void loadImageAsync(ImageView imageView, String url, int placeholderResId) {
    loadImageAsync(imageView, -1, -1, placeholderResId, url, null);
  }
}
