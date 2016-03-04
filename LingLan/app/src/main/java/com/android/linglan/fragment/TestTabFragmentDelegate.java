package com.android.linglan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by changyizhang on 12/15/14.
 */
public class TestTabFragmentDelegate {

  private Class<? extends Fragment> fragmentClaz;
  private int iconResId;
  private int labelResId;
  private Bundle args;

  public TestTabFragmentDelegate(Class<? extends Fragment> fragmentClaz, Bundle args, int iconResId,
                             int labelResId) {
    this.fragmentClaz = fragmentClaz;
    this.args = args;
    this.iconResId = iconResId;
    this.labelResId = labelResId;
  }

  public Fragment buildFragmentInstance(Context context) {
    return Fragment.instantiate(context, fragmentClaz.getName(), args);
  }

  public int getIconResId() {
    return iconResId;
  }

  public int getLabelResId() {
    return labelResId;
  }
}
