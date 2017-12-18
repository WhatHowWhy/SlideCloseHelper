package com.cll.library;

import android.support.annotation.FloatRange;
import android.support.v4.widget.ViewDragHelper;

/**
 * Created by cll on 2017/12/10
 */
public class SlideConfig {

  /**
   * 方向-从左往右
   */
  public static final int DIRECTION_LEFT = ViewDragHelper.EDGE_LEFT;

  /**
   * 方向-从右往左
   */
  public static final int DIRECTION_RIGHT = ViewDragHelper.EDGE_RIGHT;

  /**
   * 方向-从上往下
   */
  public static final int DIRECTION_TOP = ViewDragHelper.EDGE_TOP;

  /**
   * 方向-从下往上
   */
  public static final int DIRECTION_BOTTOM = ViewDragHelper.EDGE_BOTTOM;

  /**
   * 是否只支持边缘侧滑，默认为false。若设置为true，
   * 需设置{@link #mSlideOutHeightPercent}和{@link #mSlideOutWidthPercent}
   */
  private boolean mEdgeOnly;

  /**
   * 是否关闭滑动，默认为false
   */
  private boolean mLock;

  /**
   * 页面是否包括状态栏，默认为false
   */
  private boolean mIncludeState = false;

  /**
   * 可滑动方向，目前支持{@link #DIRECTION_LEFT},{@link #DIRECTION_RIGHT}，{@link #DIRECTION_TOP}
   * 当{@link #mIncludeState}为true，支持{@link #DIRECTION_BOTTOM}
   */
  private int mEdgeFlags;

  /**
   * 滑动关闭速度阈值，默认2000f
   */
  private float mSlideOutVelocity;

  /**
   * 滑动边缘。当{@link #mEdgeOnly} 为true时，{@link #mEdgePercent} 才有用。
   * 一般只用于左右滑动，当{@link #mIncludeState}为true，可用于从下往上方向
   */
  @FloatRange(from = 0.0, to = 1.0) private float mEdgePercent;

  /**
   * 水平关闭页面的阈值，默认0.5f
   */
  @FloatRange(from = 0.0, to = 1.0) private float mSlideOutWidthPercent;

  /**
   * 竖直关闭页面的阈值，默认0.33f
   */
  @FloatRange(from = 0.0, to = 1.0) private float mSlideOutHeightPercent;

  public boolean isEdgeOnly() {
    return mEdgeOnly;
  }

  public boolean isLock() {
    return mLock;
  }

  public boolean isIncludeState() {
    return mIncludeState;
  }

  public int getEdgeFlags() {
    return mEdgeFlags;
  }

  public float getEdgePercent() {
    return mEdgePercent;
  }

  public float getSlideOutHeightPercent() {
    return mSlideOutHeightPercent;
  }

  public float getSlideOutWidthPercent() {
    return mSlideOutWidthPercent;
  }

  public float getSlideOutVelocity() {
    return mSlideOutVelocity;
  }

  public SlideConfig(Builder builder) {
    mEdgeOnly = builder.edgeOnly;
    mLock = builder.lock;
    mIncludeState = builder.includeState;
    mEdgeFlags = builder.edgeFlags;
    mSlideOutVelocity = builder.slideOutVelocity;
    mEdgePercent = builder.edgePercent;
    mSlideOutWidthPercent = builder.slideOutWidthPercent;
    mSlideOutHeightPercent = builder.slideOutHeightPercent;
  }

  public static class Builder {

    private boolean edgeOnly = false;
    private boolean lock = false;
    private boolean includeState = false;
    private int edgeFlags;
    private float slideOutVelocity = 2000f;
    @FloatRange(from = 0.0, to = 1.0) private float edgePercent = 0.1f;
    @FloatRange(from = 0.0, to = 1.0) private float slideOutWidthPercent = 0.5f;
    @FloatRange(from = 0.0, to = 1.0) private float slideOutHeightPercent = 0.33f;

    public Builder() {
    }

    public SlideConfig create() {
      return new SlideConfig(this);
    }

    public Builder edgeOnly(boolean edgeOnly) {
      this.edgeOnly = edgeOnly;
      return this;
    }

    public Builder lock(boolean lock) {
      this.lock = lock;
      return this;
    }

    public Builder includeState(boolean includeState) {
      this.includeState = includeState;
      return this;
    }

    public Builder edgeFlags(int edgeFlags) {
      this.edgeFlags = edgeFlags;
      return this;
    }

    public Builder slideOutVelocity(float slideOutVelocity) {
      this.slideOutVelocity = slideOutVelocity;
      return this;
    }

    public Builder edgePercent(@FloatRange(from = 0.0, to = 1.0) float edgePercent) {
      this.edgePercent = edgePercent;
      return this;
    }

    public Builder slideOutWidthPercent(
        @FloatRange(from = 0.0, to = 1.0) float slideOutWidthPercent) {
      this.slideOutWidthPercent = slideOutWidthPercent;
      return this;
    }

    public Builder slideOutHeightPercent(
        @FloatRange(from = 0.0, to = 1.0) float slideOutHeightPercent) {
      this.slideOutHeightPercent = slideOutHeightPercent;
      return this;
    }
  }
}
