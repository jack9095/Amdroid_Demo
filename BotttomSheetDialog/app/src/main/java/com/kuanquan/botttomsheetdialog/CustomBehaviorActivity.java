package com.kuanquan.botttomsheetdialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentTransaction;

import com.kuanquan.botttomsheetdialog.widget.BottomSheetBehavior;

public class CustomBehaviorActivity extends AppCompatActivity {
    private BottomSheetBehavior<LinearLayout> mBehavior;
    private float mSlideOffset = 0f;
    private LinearLayout mLinearLayout;
    private ImageView iv_arrow;
    private CoordinatorLayout coo_layout;
    private RelativeLayout rl_iv_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_behavior);
        mLinearLayout = findViewById(R.id.ll_behavior);
        iv_arrow = findViewById(R.id.iv_arrow);
        coo_layout = findViewById(R.id.coo_layout);
        rl_iv_arrow = findViewById(R.id.rl_iv_arrow);
        initBehavior();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initBehavior() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, new BlankFragment());
        transaction.commitAllowingStateLoss();

        mBehavior = BottomSheetBehavior.from(mLinearLayout);
        mBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED && mSlideOffset == 0) {
                    iv_arrow.setImageResource(R.mipmap.img_open);
                } else {
                    iv_arrow.setImageResource(R.mipmap.img_drag);
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                mSlideOffset = slideOffset < 0 ? 0 :slideOffset;
                coo_layout.setBackgroundColor(Color.argb((int) (128 * mSlideOffset), 11, 11, 11));
                if (mSlideOffset == 0) {
                    getWindow().setStatusBarColor(Color.WHITE);
                } else {
                    getWindow().setStatusBarColor(Color.argb((int) (128 *  mSlideOffset), 11, 11, 11));
                }
            }
        });

        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBehavior.setDraggable(false);

        rl_iv_arrow.setOnTouchListener((v, event) -> {
            mBehavior.setDraggable(true);
            return false;
        });

        mBehavior.setContentUIType(BottomSheetBehavior.ContentUIType.ALBUM);

        if (mBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) { // 缩小状态
//            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED); // 缩小
        }

        // 全屏展开
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
