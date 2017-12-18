package com.cll.library.callbak;

import android.support.annotation.FloatRange;

/**
 * Created by cll on 2017/12/10
 */
public interface OnInternalSlideListener {

  void onSlide(@FloatRange(from = 0.0, to = 1.0) float percent);

  void onOpen();

  void onClose(boolean finishActivity);

  boolean getSlideTop();

  boolean getSlideBottom();
}
