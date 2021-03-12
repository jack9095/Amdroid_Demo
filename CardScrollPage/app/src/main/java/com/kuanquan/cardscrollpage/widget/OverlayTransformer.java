package com.kuanquan.cardscrollpage.widget;

import android.util.Log;
import android.view.View;
import androidx.viewpager.widget.ViewPager;

public class OverlayTransformer implements ViewPager.PageTransformer {
    private float scaleOffset = 50;
    private float transOffset = 50;
    private int overlayCount;

    public OverlayTransformer(int overlayCount) {
        this.overlayCount = overlayCount;
    }

    public OverlayTransformer(int overlayCount, float scaleOffset, float transOffset) {
        this.overlayCount = overlayCount;
        if (Float.compare(scaleOffset, -1) != 0)
            this.scaleOffset = scaleOffset;
        if (Float.compare(transOffset, -1) != 0)
            this.transOffset = transOffset;
    }

    public int getOverlayCount() {
        return overlayCount;
    }

    @Override
    public void transformPage(View page, float position) {
        if (position <= 0.0f) { // 当前页
            page.setTranslationX(0f);
//            page.setAlpha(1 - 0.5f * Math.abs(position));
            page.setClickable(true);
        } else {
            otherTrans(page, position);
            page.setClickable(false);
        }
    }

    private void otherTrans(View page, float position) {
        // 缩放比例
//        float scale = (page.getWidth() - scaleOffset * (position % overlayCount)) / (float) (page.getWidth());
        float scaleX = (page.getWidth() - scaleOffset * position) / (float) (page.getWidth());
        float scaleY = (page.getHeight() - scaleOffset * position) / (float) (page.getHeight());
        Log.e("OverlayTransformer ","scaleX = " + scaleX);
        Log.e("OverlayTransformer ","scaleY = " + scaleY);
        if (scaleX <= -3.3f) {
            scaleX = -3.3f;
        }
        if (scaleY <= -3.3f) {
            scaleY = -3.3f;
        }
        page.setScaleX(scaleX);
        page.setScaleY(scaleY);

        page.setAlpha(1f);
        if (position > overlayCount - 1 && position < overlayCount) { // 当前页向右滑动时,最右面第四个及以后页面应消失
            float curPositionOffset = transOffset * (float) Math.floor(position);  // 向下取整
            float lastPositionOffset = transOffset * (float) Math.floor(position - 1);  // 上一个卡片的偏移量
            float singleOffset = 1 - Math.abs(position % (int) position);
            float transX = (-page.getWidth() * position) + (lastPositionOffset + singleOffset * (curPositionOffset - lastPositionOffset));
            page.setTranslationX(transX);
        } else if (position <= overlayCount - 1) {
            float transX = (-page.getWidth() * position) + (transOffset * position);
            page.setTranslationX(transX);
        } else {
            page.setAlpha(0f);
//            page.setTranslationX(0);  // 不必要的隐藏在下面
        }
    }
}
