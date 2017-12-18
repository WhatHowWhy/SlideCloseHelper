package com.cll.library;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import com.cll.library.callbak.OnInternalSlideListener;
import com.cll.library.callbak.OnSlideListener;
import com.cll.library.widget.SlideCloseLayout;

/**
 * Created by cll on 2017/12/14
 */
public class SlideCloseHelper {

  public static ViewGroup getDecorView(Activity activity) {
    return (ViewGroup) activity.getWindow().getDecorView();
  }

  public static Drawable getDecorViewDrawable(Activity activity) {
    return getDecorView(activity).getBackground();
  }

  public static View getContentView(Activity activity) {
    return getDecorView(activity).getChildAt(0);
  }

  /**
   * 附着Activity，实现侧滑
   *
   * @param curActivity 当前Activity
   * @param helper Activity栈管理类
   * @param config 参数配置
   * @param listener 滑动的监听
   * @return 处理侧滑的布局，提高方法动态设置滑动相关参数
   */
  public static SlideCloseLayout attach(@NonNull final Activity curActivity,
      @NonNull final SlideActivityHelper helper, @Nullable final SlideConfig config,
      @Nullable final OnSlideListener listener) {
    int statusBarHeight1 = 0;
    //获取status_bar_height资源的ID
    int resourceId =
        curActivity.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      //根据资源ID获取响应的尺寸值，状态栏高度
      statusBarHeight1 = curActivity.getResources().getDimensionPixelSize(resourceId);
    }
    final ViewGroup decorView = getDecorView(curActivity);
    final ViewGroup contentView = (ViewGroup) decorView.getChildAt(0);
    //去除状态栏的content内容
    final View view = contentView.getChildAt(1);
    //与父布局解除关联
    contentView.removeViewAt(1);

    //设置背景，不然背景会是透明
    if (view.getBackground() == null) {
      view.setBackground(decorView.getBackground());
    }

    final SlideActivityHelper[] helpers = { helper };

    //上个页面content
    final Activity preActivity = helpers[0].getPreActivity();
    final View preContentView = getContentView(preActivity);

    final SlideCloseLayout slideCloseLayout;
    slideCloseLayout = new SlideCloseLayout(statusBarHeight1, curActivity, view, preContentView,
        getDecorViewDrawable(preActivity), config, new OnInternalSlideListener() {

      @Override public void onSlide(float percent) {
        if (listener != null) {
          listener.onSlide(percent);
        }
      }

      @Override public void onOpen() {
        if (listener != null) {
          listener.onOpen();
        }
      }

      @Override public void onClose(boolean finishActivity) {
        if (!finishActivity && listener != null) {
          listener.onClose();
        }

        if (finishActivity) {
          curActivity.finish();
          curActivity.overridePendingTransition(0, R.anim.anim_out_none);
        }
      }

      @Override public boolean getSlideTop() {
        if (listener != null) {
          return listener.getSlideTop();
        }
        return false;
      }

      @Override public boolean getSlideBottom() {
        if (listener != null) {
          return listener.getSlideBottom();
        }
        return false;
      }
    });
    decorView.addView(slideCloseLayout);
    return slideCloseLayout;
  }
}
