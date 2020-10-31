package com.kuanquan.dragapplication.test;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.kuanquan.dragapplication.R;
import com.kuanquan.dragapplication.divider.BaseItemDecoration;
import com.kuanquan.dragapplication.divider.GridItemDecoration;
import com.kuanquan.dragapplication.divider.PicItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PicMgrAdapter adapter;

    private View delArea; // 删除区域
    private AppCompatImageView delIcon; // 删除图标

    int count = 9;

    private Handler mHandler = new Handler();

    private AnimationSet mShowAction;  // 开始拖拽显示的动画
    private AnimationSet mHideAction; // 拖拽结束显示的动画

//    private ScaleAnimation mDelShowScaleAnim;  // 删除区域现实的动画
//    private ScaleAnimation mDelHideScaleAnim;  // 删除区域隐藏的动画

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

//        mDelShowScaleAnim = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        mDelShowScaleAnim.setFillAfter(true);
//        mDelShowScaleAnim.setDuration(150);
//
//        mDelHideScaleAnim = new ScaleAnimation(1.3f, 1.0f, 1.3f, 1.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        mDelHideScaleAnim.setDuration(150);

        ScaleAnimation showScaleAnim = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

//        ScaleAnimation hideScaleAnim = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        AlphaAnimation showAlphaAnim = new AlphaAnimation(0.0f, 1.0f);
//        AlphaAnimation hideAlphaAnim = new AlphaAnimation(1.0f, 0.0f);

        mShowAction = new AnimationSet(true);
        mShowAction.addAnimation(showScaleAnim);
        mShowAction.addAnimation(showAlphaAnim);

//        mHideAction = new AnimationSet(true);
//        mHideAction.addAnimation(hideScaleAnim);
//        mHideAction.addAnimation(hideAlphaAnim);

        mShowAction.setDuration(150);
//        mHideAction.setDuration(150);

        mShowAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //delArea.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

//        mHideAction.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                delArea.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });

        recyclerView = (RecyclerView) findViewById(R.id.recy);
        delArea = findViewById(R.id.delete_area);
        delIcon = (AppCompatImageView) findViewById(R.id.delete_icon);

        delIcon.setImageResource(R.drawable.ic_edit_delete);

        adapter = new PicMgrAdapter(this, 240);
        adapter.setProportion(1.0f);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

//        recyclerView.addItemDecoration(new PicItemDecoration(30));

        recyclerView.addItemDecoration(
                new GridItemDecoration.Builder(this, OrientationHelper.VERTICAL)
                        .setDividerWidthPx(10) // 分割线的宽度 单位px
                        .setFooterViewCount(0) // 设置尾布局的个数 默认为0 尾布局之间没有分割线
                        .setHeadViewCount(0) // 设置头布局的个数 默认为0 头布局之间没有分割线 以及头布局与第一条数据之间也是没有分割线
                        .setDividerMarginPx(0, 0, 0, 0) // 设置分割线距离item的间隔
                        .setDividerColorProvider(new BaseItemDecoration.DividerColorProvider() {
                            @Override
                            public int getDividerColor(int position, @NotNull RecyclerView parent) {
                                return Color.parseColor("#ffffff");
                            }
                        })
                        .build()
        );

        ArrayList<Pic> list = new ArrayList<>();
        Pic pic;
        for (int i = 0; i < 9; i++) {
            pic = new Pic();
            pic.id = i;
            pic.path = "";
            list.add(pic);
        }

        recyclerView.setAdapter(adapter);
        adapter.setList(list);

        adapter.setPicClickListener(new PicMgrAdapter.PicClickListener() {
            @Override
            public void onPicClick(View view, int pos) {
                Toast.makeText(getApplicationContext(), "pos:" + pos + " id:" + adapter.getList().get(pos).id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAddClick(View view) {
                Pic pic = new Pic();
                pic.id = count;
                adapter.addItem(pic);
                count++;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    }
                }, 300);
            }
        });

        PicDragHelperCallback picDragHelperCallback = new PicDragHelperCallback(adapter, delArea);
        picDragHelperCallback.setScale(1.3f);//1.3f
        picDragHelperCallback.setAlpha(0.9f);
        ItemTouchHelper helper = new ItemTouchHelper(picDragHelperCallback);
        helper.attachToRecyclerView(recyclerView);

        picDragHelperCallback.setDragListener(new PicDragHelperCallback.DragListener() {
            @Override
            public void onDragStart() {
//                delArea.clearAnimation();
                delArea.setVisibility(View.VISIBLE);
//                delArea.startAnimation(mShowAction);
            }

            @Override
            public void onDragFinish(boolean isInside) {
//                delArea.startAnimation(mHideAction);
                delArea.setVisibility(View.INVISIBLE);
                delIcon.setImageResource(R.drawable.ic_edit_delete);
                delArea.setBackgroundColor(0x0dffffff);
            }

            @Override
            public void onDragAreaChange(boolean isInside, boolean isIdle) {
                //Log.d("jiabin", "isInside:" + isInside + " | isIdle:" + isIdle);
                if (isIdle) {
                    return;
                }
                if (isInside) {
                    delIcon.setImageResource(R.drawable.ic_edit_deleted);
                    delArea.setBackgroundColor(0x19ffffff);
//                    delArea.startAnimation(mDelShowScaleAnim);

                    // 震动效果
//                    ShakeUtil.vibrator(MainActivity.this, 100);
                } else {
                    delIcon.setImageResource(R.drawable.ic_edit_delete);
                    delArea.setBackgroundColor(0x0dffffff);
//                    delArea.startAnimation(mDelHideScaleAnim);
                }
            }
        });



        adapter.setEmptyAnimatorListener(new PicMgrAdapter.EmptyAnimatorListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation, PicMgrAdapter.PicAddViewHolder holder) {
                float value = (float) animation.getAnimatedValue();
                int color = ColorUtils.setAlphaComponent(0xffffffff, (int) (255 * value));
                holder.itemView.setBackgroundColor(color);
            }
        });

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.startEmptyAnimator(recyclerView, 1000L, 0.1f, 0.2f, 0.1f, 0.2f, 0.1f);
            }
        });
    }
}
