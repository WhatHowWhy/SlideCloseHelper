package com.cll.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * 最顶层页面滑动过程中 底部显示的画面
 * Created by cll on 2017/12/11
 */
public class CacheDrawView extends View {

  private View mCacheView;

  public CacheDrawView(Context context) {
    super(context);
  }

  public void drawCacheView(View cacheView) {
    mCacheView = cacheView;
    invalidate();
  }

  @Override protected void onDraw(Canvas canvas) {
    if (mCacheView != null) {
      mCacheView.draw(canvas);
    }
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    mCacheView = null;
  }
}
