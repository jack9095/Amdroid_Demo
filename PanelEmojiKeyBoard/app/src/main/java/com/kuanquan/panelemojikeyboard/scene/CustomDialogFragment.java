package com.kuanquan.panelemojikeyboard.scene;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.effective.android.panel.PanelSwitchHelper;
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener;
import com.effective.android.panel.view.panel.IPanelView;
import com.effective.android.panel.view.panel.PanelView;
import com.kuanquan.panelemojikeyboard.R;
import com.kuanquan.panelemojikeyboard.scene.chat.emotion.EmotionPagerView;
import com.kuanquan.panelemojikeyboard.scene.chat.emotion.Emotions;
import com.kuanquan.panelemojikeyboard.scene.feed.BaseDialogFragment;
import com.kuanquan.panelemojikeyboard.util.DisplayUtils;

public class CustomDialogFragment extends BaseDialogFragment {

    private final static String TAG = "CustomDialogFragment";

    private PanelSwitchHelper helper;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_feed_comment_layout;
    }

    public static CustomDialogFragment newInstance() {
        return new CustomDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//            windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(windowParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setDimAmount(0f);
            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView() {
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (null != window) {
                if (helper == null) {
                    helper = new PanelSwitchHelper.Builder(window, mRootView)
                            //可选
                            .addKeyboardStateListener((visible, height) -> {
                                Log.e(TAG, "系统键盘是否可见 : " + visible + " 高度为：" + height);
                            })
                            //可选
                            .addPanelChangeListener(new OnPanelChangeListener() {

                                @Override
                                public void onKeyboard() {
                                    Log.e(TAG, "唤起系统输入法");
                                    mRootView.findViewById(R.id.emotion_btn).setSelected(false);
                                }

                                @Override
                                public void onNone() {
                                    Log.e(TAG, "隐藏所有面板");
                                    mRootView.findViewById(R.id.emotion_btn).setSelected(false);
                                    dismiss();
                                }

                                @Override
                                public void onPanel(IPanelView view) {
                                    Log.e(TAG, "唤起面板 : " + view);
                                    // TODO 这行代码很关键，解决表情键盘切换的问题
                                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                                    if (view instanceof PanelView) {
                                        mRootView.findViewById(R.id.emotion_btn).setSelected(((PanelView) view).getId() == R.id.panel_emotion ? true : false);
                                    }
                                }


                                @Override
                                public void onPanelSizeChange(IPanelView panelView, boolean portrait, int oldWidth, int oldHeight, int width, int height) {
                                    if (panelView instanceof PanelView) {
                                        switch (((PanelView) panelView).getId()) {
                                            case R.id.panel_emotion: {
                                                EmotionPagerView pagerView = mRootView.findViewById(R.id.view_pager);
                                                int viewPagerSize = height - DisplayUtils.dip2px(getContext(), 30f);
                                                pagerView.buildEmotionViews(
                                                        mRootView.findViewById(R.id.edit_text),
                                                        Emotions.getEmotions(), width, viewPagerSize);
                                                break;
                                            }
                                        }
                                    }
                                }
                            })
                            .logTrack(false)
                            .build(true);
                }
            }
        }
    }
}
