package com.kuanquan.commentbanner.v2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.blankj.utilcode.util.SizeUtils;

public class IndicatorDrawView extends LinearLayoutCompat implements Indicator {
    private Context context;

    private Drawable selectColor;
    private Drawable normalColor;
    private float alpha;

    public IndicatorDrawView(Context context) {
        super(context);
        this.context = context;
    }

    public IndicatorDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public IndicatorDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public IndicatorDrawView initView(int indicatorCount,int selectPosition,Drawable selectDrawble,Drawable normalDrawble,float alpha){
        this.selectColor = selectDrawble;
        this.normalColor = normalDrawble;
        this.alpha = alpha;
        for (int i=0;i<indicatorCount;i++){
            AppCompatImageView ivIndicator = new AppCompatImageView(context);
            LinearLayoutCompat.LayoutParams lp = new LinearLayoutCompat.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            lp.leftMargin = i == 0 ? 0 : SizeUtils.dp2px(6);
            ivIndicator.setLayoutParams(lp);
            ivIndicator.setBackgroundDrawable(i == selectPosition ? selectDrawble : normalDrawble);
            ivIndicator.setAlpha(i == selectPosition ? 1 : alpha);
            addView(ivIndicator);
        }
        return this;
    }

    public void changeIndicator(int position){
        int count = getChildCount();
        for (int i=0;i<count;i++){
            AppCompatImageView ivIndecator  = (AppCompatImageView) getChildAt(i);
            ivIndecator.setBackgroundDrawable(normalColor);
            ivIndecator.setAlpha(alpha);
        }
        AppCompatImageView ivIndecator  = (AppCompatImageView) getChildAt(position);
        ivIndecator.setBackgroundDrawable(selectColor);
        ivIndecator.setAlpha(1.0f);
    }

    private int pagerCount; // 真实的页数
    private float offset;   // 偏移的页面
    private int selectedPage; // 选中的页面
    /**
     * 控制在banner中的位置
     */
    private RelativeLayout.LayoutParams params;
    @Override
    public void initIndicatorCount(int pagerCount) {
        this.pagerCount = pagerCount;
        setVisibility(pagerCount > 1 ? VISIBLE : GONE);
        requestLayout();
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public RelativeLayout.LayoutParams getParams() {
        if (params == null) {
            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.bottomMargin = dip2px(10);
        }
        return params;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        selectedPage = position;
        offset = positionOffset;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private int dip2px(float dp) { return (int) (dp * getContext().getResources().getDisplayMetrics().density); }

    public static class Builder{
        private Context context;
        private int position;
        private int indicatorCount;
        private Drawable selectColor;
        private Drawable normalColor;
        private float alpha;

        public Builder setContext(Context context){
            this.context = context;
            return this;
        }

        public Builder setSelectPosition(int position){
            this.position = position;
            return this;
        }

        public Builder setIndicatorCount(int indicatorCount){
            this.indicatorCount = indicatorCount;
            return this;
        }

        public Builder setSelectColor(Drawable selectColor){
            this.selectColor = selectColor;
            return this;
        }

        public Builder setNormalColor(Drawable normalColor){
            this.normalColor = normalColor;
            return this;
        }

        public Builder setNormalAlpha(float alpha){
            this.alpha = alpha;
            return this;
        }

        public IndicatorDrawView builder(){
            return  new IndicatorDrawView(context).initView(indicatorCount,position,selectColor,normalColor,alpha);
        }
    }
}