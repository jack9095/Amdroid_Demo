package com.kuanquan.doyincover.publisher.chains.solirateView;


import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kuanquan.doyincover.R;
import com.kuanquan.doyincover.utils.AnimUtils;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

/**
 * Author:  lijie
 * Date:   2019/1/5
 * Email:  2607401801@qq.com
 */
public class UpTitleView extends FrameLayout implements IPagerTitleView {

  private float mMinScale = 0.7f;
  private float mMinAlpha = 0.8f;
  public SimpleDraweeView chainIv;
  public LinearLayout chainNumLayout;
  public TextView chainNum;
  private FrameLayout upViewContainer;
  public LottieAnimationView arrow;
  View view;
  public UpTitleView(Context context) {
    super(context);
    init(context);
  }

  public UpTitleView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public UpTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    view=View.inflate(context,R.layout.item_chain_indicator,this);
    chainIv=view.findViewById(R.id.chain_iv);
    chainNumLayout=view.findViewById(R.id.chain_num_layout);
    chainNum=view.findViewById(R.id.chain_num);
    upViewContainer=view.findViewById(R.id.up_view_container);
    arrow=view.findViewById(R.id.arrow);
    arrow.addAnimatorListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {
        chainNumLayout.setVisibility(View.GONE);
        arrow.setVisibility(View.VISIBLE);
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        //chainNumLayout.setVisibility(View.VISIBLE);
        AnimUtils.setAlphaAnim(chainNumLayout,0,1,800,false);
        arrow.setVisibility(View.GONE);
      }

      @Override
      public void onAnimationCancel(Animator animation) {
        chainNumLayout.setVisibility(View.VISIBLE);
        arrow.setVisibility(View.GONE);
      }

      @Override
      public void onAnimationRepeat(Animator animation) {

      }
    });
    invalidate();
  }

  @Override
  public void onSelected(int i, int i1) {
    if(!arrow.isAnimating()) {
      chainNumLayout.setVisibility(VISIBLE);
      upViewContainer.setBackgroundResource(R.drawable.up_bg);
      chainIv.setVisibility(GONE);
    }
  }

  @Override
  public void onDeselected(int i, int i1) {
    chainNumLayout.setVisibility(GONE);
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
