# SlideCloseHelper

![slide_main](https://github.com/WhatHowWhy/SlideCloseHelper/blob/master/screenshot/slide_close.jpg)

## 概述
  iOS系统自带有向右侧滑返回上一页的功能，这是一个良好的用户体验，但是Android官方并没有这个功能。现在这个库封装了这一功能。
  使用这个这个库，只需要一句话就可以轻松设置指定的Activity上下左右滑动，且滑动过程中底部阴影透明度与缩放程度与关闭阈值相关。
  
## 功能
  - 可以同时支持上下左右滑动（包括状态栏），左右下滑动（不包括状态栏）
  - 不用设置Activity背景为透明
  - 可以设置是全局滑动还是边缘侧滑，默认为全局滑动
  - 可以设置水平及垂直方向关闭的阈值
  
## 使用 
### step1.设置Application
   在onCreate里创建SlideActivityHelper，并且注册生命周期
     
    mSlideActivityHelper = new SlideActivityHelper();
    registerActivityLifecycleCallbacks(mSlideActivityHelper);
 
### step2.设置参数
   在需要滑动返回的页面进行设置,见[sample](https://github.com/WhatHowWhy/SlideCloseHelper/blob/master/app/src/main/java/com/cll/slideclosehelper/SlideActivity.java)
   
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
    
 ## 技术支持
   - 通过提交issue来寻求帮助
   
 
