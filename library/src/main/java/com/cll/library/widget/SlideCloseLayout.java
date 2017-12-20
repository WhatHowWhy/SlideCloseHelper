package com.cll.library.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.cll.library.SlideConfig;
import com.cll.library.callbak.OnInternalSlideListener;

/**
 * 支持左右滑动，顶部下拉
 * 暂不支持底部上拉返回上一页
 *
 * Created by cll on 2017/12/13
 */
public class SlideCloseLayout extends FrameLayout {

  private static final int MIN_FLING_VELOCITY = 400;
  private ViewDragHelper mDragHelper;
  private View mContentView;
  private CacheDrawView mCacheDrawView;
  private ShadowView mShadowView;
  private View mPreContentView;
  private Drawable mPreDecorViewDrawable;
  private SlideCallback mSlideCallback;
  private OnInternalSlideListener mOnInternalSlideListener;
  //状态栏高度
  private int mStateTop;

  private int mScreenWidth;
  private int mScreenHeight;

  private boolean mEdgeOnly = false;
  private boolean mLock = false;
  //可以滑动的方向
  private int mEdgeFlags;
  private float mSlideOutVelocity;
  //水平关闭页面的阈值
  @FloatRange(from = 0.0, to = 1.0) private float mSlideOutWidthPercent;
  //竖直关闭页面的阈值
  @FloatRange(from = 0.0, to = 1.0) private float mSlideOutHeightPercent;
  @FloatRange(from = 0.0, to = 1.0) private float mEdgeRangePercent;

  private float mSlideOutRangeWidth;
  private float mSlideOutRangeHeight;
  private float mEdgeRangeWidth;
  private float mEdgeRangeHeight;

  private float mDownX;
  private float mDownY;
  private float offsetsByX;
  private float offsetsByY;

  /**
   * 正在滑动方向
   */
  private int mTrackingEdge = -1;

  public SlideCloseLayout(int top, Context context, View contentView, View preContentView,
      Drawable preDecorViewDrawable, SlideConfig config,
      OnInternalSlideListener onInternalSlideListener) {
    super(context);
    mStateTop = top;
    mContentView = contentView;
    mPreContentView = preContentView;
    mPreDecorViewDrawable = preDecorViewDrawable;
    mOnInternalSlideListener = onInternalSlideListener;
    initConfig(config);
  }

  private void initConfig(SlideConfig config) {
    if (config == null) {
      config = new SlideConfig.Builder().create();
    }
    mEdgeOnly = config.isEdgeOnly();
    mLock = config.isLock();
    mEdgeFlags = config.getEdgeFlags();
    mSlideOutVelocity = config.getSlideOutVelocity();
    mSlideOutWidthPercent = config.getSlideOutWidthPercent();
    mSlideOutHeightPercent = config.getSlideOutHeightPercent();
    mEdgeRangePercent = config.getEdgePercent();
    if (config.isIncludeState()) {
      mStateTop = 0;
    }

    mScreenWidth = getResources().getDisplayMetrics().widthPixels;
    mScreenHeight = getResources().getDisplayMetrics().heightPixels;
    mSlideOutRangeWidth = mScreenWidth * mSlideOutWidthPercent;
    mSlideOutRangeHeight = mScreenHeight * mSlideOutHeightPercent;

    mEdgeRangeWidth = mScreenWidth * mEdgeRangePercent;
    mEdgeRangeHeight = mScreenHeight * mEdgeRangePercent;

    final float density = getResources().getDisplayMetrics().density;
    final float minVel = MIN_FLING_VELOCITY * density;

    ViewGroupCompat.setMotionEventSplittingEnabled(this, false);
    mSlideCallback = new SlideCallback();
    mDragHelper = ViewDragHelper.create(this, 1.0f, mSlideCallback);
    // 最小拖动速度
    mDragHelper.setMinVelocity(minVel);
    mDragHelper.setEdgeTrackingEnabled(mEdgeFlags);

    //画底部布局
    mCacheDrawView = new CacheDrawView(getContext());
    mCacheDrawView.setVisibility(INVISIBLE);
    addView(mCacheDrawView);

    //画阴影
    mShadowView = new ShadowView(getContext());
    mShadowView.setVisibility(INVISIBLE);
    addView(mShadowView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    addView(mContentView);
    mContentView.setPadding(0, 0, 0, mStateTop);
    mContentView.setY(mStateTop);
  }

  @Override public boolean dispatchTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mDownX = event.getRawX();//获得按下时的X坐标
        mDownY = event.getRawY();//获得按下时的Y坐标
        offsetsByX = 0;//设置X轴方向总偏移量
        offsetsByY = 0;//设置Y轴方向总偏移量
        mTrackingEdge = -1;
        break;
      case MotionEvent.ACTION_MOVE:
        offsetsByX = event.getRawX() - mDownX;//获得X轴的偏移量
        offsetsByY = event.getRawY() - mDownY;//获得Y轴的偏移量
        //若X的总偏移量大于Y的总偏移量说明是X轴方向的滑动，否则是Y轴方向的滑动
        if (Math.abs(offsetsByX) > Math.abs(offsetsByY)) {
          //若大于0则表示向右移动，若小于0则表示向左移动
          if (offsetsByX > 0) {
            checkSlideDirection(ViewDragHelper.EDGE_LEFT);
          } else {
            checkSlideDirection(ViewDragHelper.EDGE_RIGHT);
          }
        } else {
          //若大于0则表示向下移动，若小于0则表示向上移动
          if (offsetsByY > 0) {
            if (mOnInternalSlideListener != null) {
              if (mOnInternalSlideListener.getSlideTop()) {
                checkSlideDirection(ViewDragHelper.EDGE_TOP);
              }
            }
          } else {
            if (mOnInternalSlideListener != null) {
              if (mOnInternalSlideListener.getSlideBottom()) {
                checkSlideDirection(ViewDragHelper.EDGE_BOTTOM);
              }
            }
          }
        }
        break;
      default:
        break;
    }
    return super.dispatchTouchEvent(event);
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent event) {
    if (mLock) {
      return false;
    }
    return mDragHelper.shouldInterceptTouchEvent(event);
  }

  /**
   * 判断是否设置了该方向，如果没有设置，则设置正在滑动方向
   */
  private void checkSlideDirection(int edge) {
    if (mTrackingEdge == -1 && (mEdgeFlags & edge) != 0) {
      mTrackingEdge = edge;
    }
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if (mLock) {
      return super.onTouchEvent(event);
    }
    mDragHelper.processTouchEvent(event);
    return true;
  }

  @Override public void computeScroll() {
    if (mDragHelper.continueSettling(true)) {
      invalidate();
    }
  }

  private class SlideCallback extends ViewDragHelper.Callback {

    @Override public boolean tryCaptureView(View child, int pointerId) {
      if (mEdgeOnly) {
        if ((mTrackingEdge & ViewDragHelper.EDGE_LEFT) != 0) {
          if (mDownX > mEdgeRangeWidth) {
            return false;
          }
        } else if ((mTrackingEdge & ViewDragHelper.EDGE_RIGHT) != 0) {
          if (mDownX < mScreenWidth - mEdgeRangeWidth) {
            return false;
          }
        } else if ((mTrackingEdge & ViewDragHelper.EDGE_BOTTOM) != 0) {
          if (mDownY < mScreenHeight - mEdgeRangeHeight) {
            return false;
          }
        }
      }
      return mTrackingEdge != -1 && child == mContentView;
    }

    @Override public int clampViewPositionHorizontal(View child, int left, int dx) {
      int ret = 0;
      if ((mTrackingEdge & ViewDragHelper.EDGE_LEFT) != 0) {
        ret = Math.min(child.getWidth(), Math.max(left, 0));
      } else if ((mTrackingEdge & ViewDragHelper.EDGE_RIGHT) != 0) {
        ret = Math.min(0, Math.max(left, -child.getWidth()));
      }
      return ret;
    }

    @Override public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
      int ret = 0;
      if ((mTrackingEdge & ViewDragHelper.EDGE_TOP) != 0) {
        ret = Math.min(child.getHeight(), Math.max(top, 0));
      } else if ((mTrackingEdge & ViewDragHelper.EDGE_BOTTOM) != 0) {
        ret = Math.min(0, Math.max(top, -child.getHeight()));
      }
      return ret;
    }

    @Override public int getViewHorizontalDragRange(View child) {
      return mScreenWidth;
    }

    @Override public int getViewVerticalDragRange(@NonNull View child) {
      return mScreenHeight;
    }

    @Override public void onViewReleased(View releasedChild, float xvel, float yvel) {
      if (releasedChild == mContentView) {
        int left = 0, top = 0;
        if ((mTrackingEdge & ViewDragHelper.EDGE_LEFT) != 0) {
          if (xvel > mSlideOutVelocity || mContentView.getLeft() >= mSlideOutRangeWidth) {
            left = mScreenWidth;
          }
        } else if ((mTrackingEdge & ViewDragHelper.EDGE_RIGHT) != 0) {
          if (-xvel > mSlideOutVelocity
              || mScreenWidth - mContentView.getRight() >= mSlideOutRangeWidth) {
            left = -mScreenWidth;
          }
        } else if ((mTrackingEdge & ViewDragHelper.EDGE_TOP) != 0) {
          if (yvel > mSlideOutVelocity || mContentView.getTop() >= mSlideOutRangeHeight) {
            top = mScreenHeight;
          }
        } else if ((mTrackingEdge & ViewDragHelper.EDGE_BOTTOM) != 0) {
          if (-yvel > mSlideOutVelocity
              || mScreenHeight - mContentView.getBottom() >= mSlideOutRangeHeight) {
            top = -mScreenHeight;
          }
        }
        mDragHelper.settleCapturedViewAt(left, top);
        invalidate();
      }
    }

    @Override public void onViewDragStateChanged(int state) {
      switch (state) {
        case ViewDragHelper.STATE_IDLE:
          if ((mTrackingEdge & ViewDragHelper.EDGE_LEFT) != 0) {
            if (mContentView.getLeft() == 0) {
              openListener();
            } else if (mContentView.getLeft() == mScreenWidth) {
              closeListener();
            }
          } else if ((mTrackingEdge & ViewDragHelper.EDGE_RIGHT) != 0) {
            if (mContentView.getLeft() == 0) {
              openListener();
            } else if (mContentView.getLeft() == -mScreenWidth) {
              closeListener();
            }
          } else if ((mTrackingEdge & ViewDragHelper.EDGE_TOP) != 0) {
            if (mContentView.getTop() == mStateTop) {
              openListener();
            } else if (mContentView.getTop() == mScreenHeight) {
              closeListener();
            }
          } else if ((mTrackingEdge & ViewDragHelper.EDGE_BOTTOM) != 0) {
            if (mContentView.getTop() == mStateTop) {
              openListener();
            } else if (mContentView.getTop() == -mScreenHeight) {
              closeListener();
            }
          }
          break;
        default:
          break;
      }
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
      if (mCacheDrawView.getVisibility() == INVISIBLE) {
        mCacheDrawView.setBackground(mPreDecorViewDrawable);
        mCacheDrawView.setVisibility(VISIBLE);
        mCacheDrawView.drawCacheView(mPreContentView);
        mCacheDrawView.setPadding(0, 0, 0, mStateTop);
        mCacheDrawView.setY(mStateTop);
        mShadowView.setVisibility(VISIBLE);
      }
      float percent = 0;
      if ((mTrackingEdge & ViewDragHelper.EDGE_LEFT) != 0
          || (mTrackingEdge & ViewDragHelper.EDGE_RIGHT) != 0) {
        percent = Math.abs(left) * 1.0f / mScreenWidth;
        mCacheDrawView.setScaleX(
            0.95f + (0.05f / mSlideOutWidthPercent) * Math.min(percent, mSlideOutWidthPercent));
        mCacheDrawView.setScaleY(
            0.95f + (0.05f / mSlideOutWidthPercent) * Math.min(percent, mSlideOutWidthPercent));
        mShadowView.redraw(Math.min(percent, mSlideOutWidthPercent) / mSlideOutWidthPercent);
      } else if ((mTrackingEdge & ViewDragHelper.EDGE_TOP) != 0
          || (mTrackingEdge & ViewDragHelper.EDGE_BOTTOM) != 0) {
        percent = Math.abs(top) * 1.0f / mScreenHeight;
        mCacheDrawView.setScaleX(
            0.95f + (0.05f / mSlideOutHeightPercent) * Math.min(percent, mSlideOutHeightPercent));
        mCacheDrawView.setScaleY(
            0.95f + (0.05f / mSlideOutHeightPercent) * Math.min(percent, mSlideOutHeightPercent));
        mShadowView.redraw(Math.min(percent, mSlideOutHeightPercent) / mSlideOutHeightPercent);
      }
      if (mOnInternalSlideListener != null) {
        mOnInternalSlideListener.onSlide(percent);
      }
    }
  }

  /**
   * closeListener
   */
  private void closeListener() {
    // 2016/9/22 0022 结束Activity
    if (mOnInternalSlideListener != null) {
      mOnInternalSlideListener.onClose(true);
    }
  }

  /**
   * listener onOpen
   */
  private void openListener() {
    // 2016/9/22 0022 回到原处
    if (mOnInternalSlideListener != null) {
      mOnInternalSlideListener.onOpen();
    }
  }

  public void edgeOnly(boolean edgeOnly) {
    mEdgeOnly = edgeOnly;
  }

  public boolean isEdgeOnly() {
    return mEdgeOnly;
  }

  public void lock(boolean lock) {
    mLock = lock;
  }

  public boolean isLock() {
    return mLock;
  }

  public void setSlideOutRangeWidthPercent(float widthPercent) {
    mSlideOutWidthPercent = widthPercent;
    mSlideOutRangeWidth = mScreenWidth * mSlideOutWidthPercent;
  }

  public float getSlideOutRangeWidthPercent() {
    return mSlideOutWidthPercent;
  }

  public void setSlideOutRangeHeightPercent(float heightPercent) {
    mSlideOutHeightPercent = heightPercent;
    mSlideOutRangeHeight = mScreenHeight * mSlideOutHeightPercent;
  }

  public float getSlideOutRangeHeightPercent() {
    return mSlideOutHeightPercent;
  }

  public float getEdgeRangePercent() {
    return mEdgeRangePercent;
  }

  public void setEdgeRangePercent(float edgeRangePercent) {
    mEdgeRangePercent = edgeRangePercent;
    mEdgeRangeWidth = mScreenWidth * mEdgeRangePercent;
    mEdgeRangeHeight = mScreenHeight * mEdgeRangePercent;
  }
}
