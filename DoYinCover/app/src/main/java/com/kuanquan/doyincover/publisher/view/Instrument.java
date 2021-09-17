package com.kuanquan.doyincover.publisher.view;

import android.animation.ObjectAnimator;
import android.view.View;


/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/12/13
 * Description:
 */
public class Instrument {
  private static Instrument mInstrument;

  public static Instrument getInstance() {
    if (mInstrument == null) {
      mInstrument = new Instrument();
    }
    return mInstrument;
  }

  public float getTranslationY(View view) {
    if (view == null) {
      return 0;
    }
    return view.getTranslationY();
  }

  public void slidingByDelta(View view, float delta) {
    if (view == null) {
      return;
    }
    view.clearAnimation();
    view.setTranslationY(delta);
  }

  public void slidingToY(View view, float y) {
    if (view == null) {
      return;
    }
    view.clearAnimation();
    view.setY(y);
  }

  public void reset(View view, long duration) {
    if (view == null) {
      return;
    }
    view.clearAnimation();
    ObjectAnimator.ofFloat(view, "translationY", 0F).setDuration(duration).start();
  }

  public void smoothTo(View view, float y, long duration) {
    if (view == null) {
      return;
    }
    view.clearAnimation();
    ObjectAnimator.ofFloat(view, "translationY", y).setDuration(duration).start();
  }
}