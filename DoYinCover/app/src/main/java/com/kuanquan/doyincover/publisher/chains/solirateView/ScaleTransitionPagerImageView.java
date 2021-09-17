package com.kuanquan.doyincover.publisher.chains.solirateView;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.view.SimpleDraweeView;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/11/30
 * Description:
 */
public class ScaleTransitionPagerImageView extends SimpleDraweeView implements IPagerTitleView {
  private float mMinScale = 0.7f;
  private float mMinAlpha = 0.8f;

  public ScaleTransitionPagerImageView(Context context) {
    super(context);
  }

  public ScaleTransitionPagerImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ScaleTransitionPagerImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }


  @Override
  public void onSelected(int i, int i1) {

  }

  @Override
  public void onDeselected(int i, int i1) {

  }

  @Override
  public void onLeave(int i, int i1, float leavePercent, boolean b) {
    setScaleX(1.0f + (mMinScale - 1.0f) * leavePercent);
    setScaleY(1.0f + (mMinScale - 1.0f) * leavePercent);
    setAlpha(1.0f + (mMinAlpha - 1.0f) * leavePercent);

  }

  @Override
  public void onEnter(int i, int i1, float enterPercent, boolean b) {
    setScaleX(mMinScale + (1.0f - mMinScale) * enterPercent);
    setScaleY(mMinScale + (1.0f - mMinScale) * enterPercent);
    setAlpha(mMinAlpha + (1.0f - mMinAlpha) * enterPercent);
  }
}
