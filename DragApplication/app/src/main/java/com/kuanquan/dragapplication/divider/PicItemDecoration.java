package com.kuanquan.dragapplication.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 简易分割线 test  不建议使用
 */
public class PicItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public PicItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int count = layoutManager.getItemCount();
        int position = parent.getChildAdapterPosition(view);

        outRect.left = (int) (((float) (count - position)) / count * mSpace);
        outRect.right = (int) (((float) mSpace * (count + 1) / count) - outRect.left);
    }
}
