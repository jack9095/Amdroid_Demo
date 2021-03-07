package com.mumu.demo.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.effective.android.panel.PanelSwitchHelper;
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener;
import com.effective.android.panel.view.panel.IPanelView;
import com.effective.android.panel.view.panel.PanelView;
import com.mumu.demo.BaseDialogFragment;
import com.mumu.demo.R;
import com.mumu.demo.TestActivity;
import com.mumu.demo.emotion.DisplayUtils;
import com.mumu.demo.emotion.EmojiData;
import com.mumu.demo.emotion.EmotionRecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * 作者的修复
 */
public class CustomDialogFragment extends BaseDialogFragment implements DialogInterface.OnKeyListener {

    private final static String TAG = "CustomDialogFragment";

    private PanelSwitchHelper helper;
    private EditText edit_text;
    private ImageView emotion_btn;

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
//        Window window = getDialog().getWindow();
//        if (window != null) {
//            window.setGravity(Gravity.CENTER);
////            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//            WindowManager.LayoutParams windowParams = window.getAttributes();
//            windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//            windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//            window.setAttributes(windowParams);
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            window.setDimAmount(0f);
//            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
//        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setOnKeyListener(this);
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getDialog().setOnKeyListener(this);
        edit_text = mRootView.findViewById(R.id.edit_text);
        emotion_btn = mRootView.findViewById(R.id.emotion_btn);
        mRootView.findViewById(R.id.textView).setOnClickListener(v -> {
            dismiss();
            startActivity(new Intent(mRootView.getContext(), TestActivity.class));
        });
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
                                    emotion_btn.setSelected(false);
                                }

                                @Override
                                public void onNone() {
                                        emotion_btn.setSelected(false);
                                        edit_text.setFocusable(true);
                                        edit_text.setFocusableInTouchMode(true);
                                        edit_text.findFocus();
                                        edit_text.requestFocus();
                                }

                                @Override
                                public void onPanel(IPanelView view) {
                                    Log.e(TAG, "唤起面板 : " + view);
                                    if (view instanceof PanelView) {
                                        emotion_btn.setSelected(((PanelView) view).getId() == R.id.panel_emotion);
                                    }
                                }


                                @Override
                                public void onPanelSizeChange(IPanelView panelView, boolean portrait, int oldWidth, int oldHeight, int width, int height) {
                                    if (panelView instanceof PanelView) {
                                        switch (((PanelView) panelView).getId()) {
                                            case R.id.panel_emotion: {
                                                EmotionRecyclerView pagerView = mRootView.findViewById(R.id.recyclerView);
                                                int viewPagerSize = height - DisplayUtils.dip2px(getContext(), 30f);
                                                pagerView.buildEmotionViews(
                                                        mRootView.findViewById(R.id.edit_text),
                                                       new EmojiData().getData(), width, viewPagerSize);
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

    @Override
    public boolean onKey(@org.jetbrains.annotations.Nullable DialogInterface dialog, int keyCode, @NotNull KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
            if (helper != null && helper.hookSystemBackByPanelSwitcher()) {
                return true;
            } else {
                dismiss();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.e(TAG,"onKey 软键盘按键");
        }
        return false;
    }

}
