package com.cll.slideclosehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import com.cll.library.SlideCloseHelper;
import com.cll.library.SlideConfig;
import com.cll.library.callbak.OnSlideListener;
import com.cll.library.widget.SlideCloseLayout;

/**
 * 该页面滑动不包括状态栏，只可以左右 下拉
 */
public class SlideActivity extends AppCompatActivity {

  private SlideCloseLayout mSlideCloseLayout;
  private TextView mTxtEdgeRange;
  private SeekBar mSbEdgeRange;
  private TextView mTxtSlideOutRangeH;
  private SeekBar mSbSlideOutRangeH;
  private TextView mTxtSlideOutRangeV;
  private SeekBar mSbSlideOutRangeV;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_slide);
    initConfig();
    mTxtEdgeRange = findViewById(R.id.txt_edge_range);
    mSbEdgeRange = findViewById(R.id.sb_edge_range);
    mTxtSlideOutRangeH = findViewById(R.id.txt_slide_out_range_h);
    mSbSlideOutRangeH = findViewById(R.id.sb_slide_out_range_h);
    mTxtSlideOutRangeV = findViewById(R.id.txt_slide_out_range_v);
    mSbSlideOutRangeV = findViewById(R.id.sb_slide_out_range_v);

    mSbEdgeRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mTxtEdgeRange.setText("边缘响应的最大值:屏幕的" + progress + "%");
        if (fromUser) {
          mSlideCloseLayout.setEdgeRangePercent(progress * 1.0f / mSbEdgeRange.getMax());
        }
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    mSbSlideOutRangeH.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mTxtSlideOutRangeH.setText("非快速滑动，关闭页面的最小值:屏幕宽度的" + progress + "%");
        if (fromUser) {
          mSlideCloseLayout.setSlideOutRangeWidthPercent(
              progress * 1.0f / mSbSlideOutRangeH.getMax());
        }
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    mSbSlideOutRangeV.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mTxtSlideOutRangeV.setText("非快速滑动，关闭页面的最小值:屏幕高度的" + progress + "%");
        if (fromUser) {
          mSlideCloseLayout.setSlideOutRangeHeightPercent(
              progress * 1.0f / mSbSlideOutRangeV.getMax());
        }
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });

    mSbEdgeRange.setProgress((int) (mSlideCloseLayout.getEdgeRangePercent() * 100));
    mSbSlideOutRangeH.setProgress((int) (mSlideCloseLayout.getSlideOutRangeWidthPercent() * 100));
    mSbSlideOutRangeV.setProgress((int) (mSlideCloseLayout.getSlideOutRangeHeightPercent() * 100));
  }

  private void initConfig() {
    mSlideCloseLayout =
        SlideCloseHelper.attach(this, MyApplication.getMyApplication().getSlideActivityHelper(),
            new SlideConfig.Builder().edgeFlags(
                ViewDragHelper.EDGE_LEFT | ViewDragHelper.EDGE_RIGHT | ViewDragHelper.EDGE_TOP)
                .create(), new OnSlideListener() {
              @Override public void onSlide(float percent) {

              }

              @Override public void onOpen() {

              }

              @Override public void onClose() {

              }

              @Override public boolean getSlideTop() {
                return true;
              }

              @Override public boolean getSlideBottom() {
                return true;
              }
            });
  }

  public void enableEdgeSlide(View view) {
    mSlideCloseLayout.edgeOnly(!mSlideCloseLayout.isEdgeOnly());
    ((Button) view).setText(mSlideCloseLayout.isEdgeOnly() ? "当前边缘侧滑" : "当前全局侧滑");
  }

  public void disableSlide(View view) {
    mSlideCloseLayout.lock(!mSlideCloseLayout.isLock());
    ((Button) view).setText(mSlideCloseLayout.isLock() ? "当前滑动禁止" : "当前滑动开启");
  }
}
