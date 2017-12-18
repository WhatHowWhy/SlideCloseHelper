package com.cll.library.callbak;

import android.support.annotation.FloatRange;

/**
 * Created by cll on 2017/12/10
 */
public interface OnSlideListener {

  void onSlide(@FloatRange(from = 0.0, to = 1.0) float percent);

  void onOpen();

  void onClose();

  /**
   * 是否可以从上往下滑动
   *
   * @return true为可以
   */
  boolean getSlideTop();

  /**
   * 是否可以从下往上滑动
   *
   * @return true为可以
   */
  boolean getSlideBottom();
}
