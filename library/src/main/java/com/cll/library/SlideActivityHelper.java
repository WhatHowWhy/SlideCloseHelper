package com.cll.library;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.util.Stack;

/**
 * Created by cll on 2017/12/14
 */
public class SlideActivityHelper implements Application.ActivityLifecycleCallbacks {

  private static Stack<Activity> mActivityStack;

  public SlideActivityHelper() {
  }

  @Override public void onActivityCreated(Activity activity, Bundle bundle) {
    if (mActivityStack == null) {
      mActivityStack = new Stack<>();
    }
    mActivityStack.add(activity);
  }

  @Override public void onActivityStarted(Activity activity) {

  }

  @Override public void onActivityResumed(Activity activity) {

  }

  @Override public void onActivityPaused(Activity activity) {

  }

  @Override public void onActivityStopped(Activity activity) {

  }

  @Override public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

  }

  @Override public void onActivityDestroyed(Activity activity) {

    if (mListener != null) {
      mListener.onDestroy(activity);
    }

    mActivityStack.remove(activity);
  }

  public Activity getPreActivity() {
    if (mActivityStack == null) {
      return null;
    }
    int size = mActivityStack.size();
    if (size < 2) {
      return null;
    }
    return mActivityStack.elementAt(size - 2);
  }

  public void finishAllActivity() {
    if (mActivityStack == null) {
      return;
    }
    for (Activity activity : mActivityStack) {
      activity.finish();
    }
  }

  void setOnActivityDestroyListener(OnActivityDestroyListener listener) {
    mListener = listener;
  }

  private OnActivityDestroyListener mListener;

  interface OnActivityDestroyListener {
    void onDestroy(Activity activity);
  }
}
