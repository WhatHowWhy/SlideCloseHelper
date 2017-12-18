package com.cll.slideclosehelper;

import android.app.Application;
import com.cll.library.SlideActivityHelper;

public class MyApplication extends Application {

  private SlideActivityHelper mSlideActivityHelper;
  private static MyApplication sMyApplication;

  @Override public void onCreate() {
    super.onCreate();
    mSlideActivityHelper = new SlideActivityHelper();
    registerActivityLifecycleCallbacks(mSlideActivityHelper);
    sMyApplication = this;
  }

  public static MyApplication getMyApplication() {
    return sMyApplication;
  }

  public SlideActivityHelper getSlideActivityHelper() {
    return mSlideActivityHelper;
  }
}
