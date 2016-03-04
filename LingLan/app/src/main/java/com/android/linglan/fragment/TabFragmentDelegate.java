package com.android.linglan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by changyizhang on 12/15/14.
 */
public class TabFragmentDelegate {

  private Class<? extends Fragment> fragmentClaz;
  private int customViewResId;
  private Bundle args;

  public TabFragmentDelegate(Class<? extends Fragment> fragmentClaz, Bundle args, int resId) {
    this.fragmentClaz = fragmentClaz;
    this.args = args;
    this.customViewResId = resId;
  }

  public Fragment getFragmentInstance(Context context) {
    return Fragment.instantiate(context, fragmentClaz.getName(), args);
  }

  public int getCustomViewResId() {
    return customViewResId;
  }
}
