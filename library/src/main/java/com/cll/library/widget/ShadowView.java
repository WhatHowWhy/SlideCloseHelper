package com.cll.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.view.View;

/**
 * 阴影View
 * Created by cll on 2017/12/11
 */
public class ShadowView extends View {

  private Paint mPaint;
  private RectF mRectF;
  private float mAlphaPercent = -1;

  public ShadowView(Context context) {
    super(context);
    mPaint = new Paint();
  }

  @Override protected void onDraw(Canvas canvas) {
    if (mAlphaPercent >= 0) {
      // 绘制渐变阴影
      mRectF = new RectF();
      mRectF.set(0, 0, getWidth(), getHeight());
      mPaint.setColor(Color.parseColor("#000000"));
      //透明度最大为0.5，当拉到该方向设定的关闭阈值，透明度为0
      mPaint.setAlpha((int) ((0.5 - 0.5 * mAlphaPercent) * 255));
      canvas.drawRect(mRectF, mPaint);
    }
  }

  public void redraw(@FloatRange(from = 0.0, to = 1.0) float alphaPercent) {
    mAlphaPercent = alphaPercent;
    invalidate();
  }
}
