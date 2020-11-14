package com.example.expandtextview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.expandtextview.R;

/**
 * @作者: njb
 * @时间: 2019/7/25 11:31
 * @描述: 自定义分割线
 */
public class SpaceDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;   //上下文
    private int dividerHeight;  //分割线的高度
    private Paint mPaint;   //画笔

    //自定义构造方法，在构造方法中初始化一些变量
    public SpaceDecoration(Context context){
        mContext = context;
        dividerHeight = 20;   //context.getResources().getDimensionPixelSize(R.dimen.divider_height);
        mPaint = new Paint();
        mPaint.setColor(context.getResources().getColor(R.color.cd8d8d8));  //设置颜色
    }

    //设置padding
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //outRect.bottom、left,right,top设置为int值，设置每一项的padding
        outRect.bottom =dividerHeight ;
    }

    //画图
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        //获取item个数
        int childCount = parent.getChildCount();
        //左右是固定的
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight() ;
        //高度
        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            //画图
            c.drawRect(left, top, right, bottom, mPaint);
        }

    }
}
