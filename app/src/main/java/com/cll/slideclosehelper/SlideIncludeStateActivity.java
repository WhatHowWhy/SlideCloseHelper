package com.cll.slideclosehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.cll.library.SlideCloseHelper;
import com.cll.library.SlideConfig;
import com.cll.library.callbak.OnSlideListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 该页面滑动包括状态栏，可上下左右滑动
 */
public class SlideIncludeStateActivity extends AppCompatActivity {

  private ListView listView;
  private List<String> data = new ArrayList<String>();

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_slide_include_state);

    listView = findViewById(R.id.lv);
    listView.setAdapter(
        new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, getData()));
  }

  @Override protected void onResume() {
    super.onResume();
    SlideCloseHelper.attach(this, MyApplication.getMyApplication().getSlideActivityHelper(),
        new SlideConfig.Builder().edgeFlags(ViewDragHelper.EDGE_LEFT
            | ViewDragHelper.EDGE_RIGHT
            | ViewDragHelper.EDGE_BOTTOM
            | ViewDragHelper.EDGE_TOP)
            // 是否禁止侧滑
            .includeState(true).create(), new OnSlideListener() {
          @Override public void onSlide(float percent) {

          }

          @Override public void onOpen() {

          }

          @Override public void onClose() {

          }

          @Override public boolean getSlideTop() {
            //用于测试
            if (listView.getFirstVisiblePosition() == 0) {
              return true;
            }
            return false;
          }

          @Override public boolean getSlideBottom() {
            //用于测试,当listview滑到底部才能上拉返回
            if (listView.getFirstVisiblePosition() >= 5) {
              return true;
            }
            return false;
          }
        });
  }

  private List<String> getData() {
    for (int i = 0; i < 15; i++) {
      data.add("测试数据" + i);
    }
    return data;
  }
}
