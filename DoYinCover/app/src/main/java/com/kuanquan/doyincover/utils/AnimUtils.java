package com.kuanquan.doyincover.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import static android.view.animation.Animation.INFINITE;

/**
 * ========================================
 * <p>
 * Created by treasure on 2018/5/2.
 * <p>
 * Android 动画类 Property Animation + Frame Animation
 * <p>
 * ========================================
 */

/**
 * Interpolators相关类解释：
 * <p>
 * AccelerateDecelerateInterolator：先加速后减速。
 * <p>
 * AccelerateInterpolator：加速。
 * <p>
 * DecelerateInterpolator：减速。
 * <p>
 * AnticipateInterpolator：先向相反方向改变一段再加速播放。
 * <p>
 * AnticipateOvershootInterpolator：先向相反方向改变，再加速播放，会超出目标值然后缓慢移动至目标值，类似于弹簧回弹。
 * <p>
 * BounceInterpolator：快到目标值时值会跳跃。
 * <p>
 * CycleIinterpolator：动画循环一定次数，值的改变为一正弦函数：Math.sin(2 * mCycles * Math.PI * input)。
 * <p>
 * LinearInterpolator：线性均匀改变。
 * <p>
 * OvershottInterpolator：最后超出目标值然后缓慢改变到目标值。
 * <p>
 * TimeInterpolator：一个允许自定义Interpolator的接口，以上都实现了该接口。
 */

public class AnimUtils {

  /**
   * 通用属性动画
   */
  public static void setAllAnim(View view, int trans_x, int trans_y, int startAlpha, int endAlpha, int startRotation, int endRotation, float scaleMax, float scaleEnd, int duration) {
    ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", trans_x);
    ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", trans_y);
    ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", startAlpha, endAlpha);
    ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", startRotation, endRotation);
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, scaleMax, scaleEnd);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, scaleMax, scaleEnd);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.play(translationX).with(translationY).with(alpha).with(rotation).with(scaleX).with(scaleY);
    animatorSet.setDuration(duration);
    animatorSet.setInterpolator(new LinearInterpolator());
    animatorSet.start();
  }

  public static void setAllAnim(View view, int trans_x, int trans_y, int startAlpha, int endAlpha, int startRotation, int endRotation, float scaleMax, float scaleEnd, int duration, Interpolator interpolator) {
    ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", trans_x);
    ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", trans_y);
    ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", startAlpha, endAlpha);
    ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", startRotation, endRotation);
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, scaleMax, scaleEnd);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, scaleMax, scaleEnd);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.play(translationX).with(translationY).with(alpha).with(rotation).with(scaleX).with(scaleY);
    animatorSet.setDuration(duration);
    animatorSet.setInterpolator(interpolator);
    animatorSet.start();
  }

  /**
   * 位移动画
   */
  public static void setTransAnim(View view, int trans_x, int trans_y, int duration) {
    ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", trans_x);
    ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", trans_y);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.play(translationX).with(translationY);
    animatorSet.setDuration(duration);
    animatorSet.setInterpolator(new LinearInterpolator());
    animatorSet.start();
  }

  public static void setTransAnim(View view, int trans_x, int trans_y, int duration, Interpolator interpolator) {
    ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", trans_x);
    ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", trans_y);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.play(translationX).with(translationY);
    animatorSet.setDuration(duration);
    animatorSet.setInterpolator(interpolator);
    animatorSet.start();
  }

  /**
   * 放缩动画
   */
  public static void setScaleAnim(View view, float scaleMax, float scaleEnd, int duration,boolean ifLoop) {
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, scaleMax, scaleEnd);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, scaleMax, scaleEnd);

    if(ifLoop) {
      scaleX.setRepeatMode(ValueAnimator.REVERSE);
      scaleX.setRepeatCount(INFINITE);

      scaleY.setRepeatMode(ValueAnimator.REVERSE);
      scaleY.setRepeatCount(INFINITE);
    }

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.play(scaleX).with(scaleY);
    animatorSet.setDuration(duration);

//    animatorSet.addListener(new AnimatorListenerAdapter() {
//      @Override
//      public void onAnimationEnd(Animator animation) {
//        super.onAnimationEnd(animation);
//        if (scaleEnd == 1F) {
//          view.setVisibility(View.VISIBLE);
//        } else if (scaleEnd == 0F) {
//          view.setVisibility(View.GONE);
//        }
//      }
//    });
    animatorSet.setInterpolator(new LinearInterpolator());
    animatorSet.start();
  }

  public static void setScaleAnim(View view, float scaleMax, float scaleEnd, int duration, Interpolator interpolator) {
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, scaleMax, scaleEnd);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, scaleMax, scaleEnd);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.play(scaleX).with(scaleY);
    animatorSet.setDuration(duration);
    animatorSet.setInterpolator(interpolator);
    animatorSet.start();
  }

  /**
   * 透明度动画
   */
  public static void setAlphaAnim(View view, float startAlpha, float endAlpha, int duration,boolean repeat) {
    ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", startAlpha, endAlpha);
    alpha.setDuration(duration);
    alpha.setInterpolator(new LinearInterpolator());
    if(repeat) {
      alpha.setRepeatMode(ValueAnimator.REVERSE);
      alpha.setRepeatCount(ValueAnimator.INFINITE);
    }
    alpha.start();
  }

  public static void setAlphaAnim(View view, float startAlpha, float endAlpha, int duration, Interpolator interpolator) {
    ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", startAlpha, endAlpha);
    alpha.setDuration(duration);
    alpha.setInterpolator(interpolator);
    alpha.start();
  }

  /**
   * 旋转动画
   */
  public static void setRotationAnim(View view, int startRotation, int endRotation, int duration) {
    ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", startRotation, endRotation);
    rotation.setDuration(duration);
    rotation.setInterpolator(new LinearInterpolator());
    rotation.start();
  }

  public static void setRotationAnim(View view, int startRotation, int endRotation, int duration, Interpolator interpolator) {
    ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", startRotation, endRotation);
    rotation.setDuration(duration);
    rotation.setInterpolator(interpolator);
    rotation.start();
  }
}
