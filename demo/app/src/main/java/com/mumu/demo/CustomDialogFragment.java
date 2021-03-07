package com.mumu.demo;

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
import com.mumu.demo.emotion.DisplayUtils;
import com.mumu.demo.emotion.EmojiData;
import com.mumu.demo.emotion.EmotionRecyclerView;

import org.jetbrains.annotations.NotNull;

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
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(windowParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setDimAmount(0f);
            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume  -> 0");
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        if (isClick) {
            Log.e(TAG, "onResume  -> 持久显示");
            isClick = false;
//            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } else {
            Log.e(TAG, "onResume  -> 计算大小");
//            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        Log.e(TAG, "onResume  -> 1");

        // 延时的目的就是为了控制 onNone() 回掉以后获取不到焦点，软键盘不弹出来
        emotion_btn.postDelayed(runnableShow,100);
    }

    Runnable runnableShow = new Runnable() {
        @Override
        public void run() {
            // 这样写就是如果是表情面板，就不获取焦点，不弹出软键盘
            if (!emotion_btn.isSelected()) {
                getFocus();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mKeyboardStateObserver != null) {
            mKeyboardStateObserver.removeOnGlobalLayoutListener();
            mKeyboardStateObserver = null;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getDialog().setOnKeyListener(this);
        edit_text = mRootView.findViewById(R.id.edit_text);
        emotion_btn = mRootView.findViewById(R.id.emotion_btn);
        mRootView.findViewById(R.id.textView).setOnClickListener(v -> {
            isClick = true;
            startActivity(new Intent(mRootView.getContext(), TestActivity.class));
        });
    }

    private boolean isClick;

    KeyboardStateObserver mKeyboardStateObserver;
    @Override
    protected void initView() {
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (null != window) {
                mKeyboardStateObserver = new KeyboardStateObserver(getActivity());
                mKeyboardStateObserver.setKeyboardVisibilityListener(new KeyboardStateObserver.OnKeyboardVisibilityListener() {
                            @Override
                            public void onKeyboardShow() {
                                Log.e(TAG, "键盘弹出 : onKeyboardShow");
                                emotion_btn.setSelected(false);
                            }

                            @Override
                            public void onKeyboardHide() {
                                Log.e(TAG, "键盘收回 : onKeyboardHide");
                                if (!emotion_btn.isSelected()) {
                                    helper.resetState();
                                }
                            }
                        });
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
                                    Log.e(TAG, "隐藏面板");
                                    emotion_btn.setSelected(false);
                                }

                                @Override
                                public void onPanel(IPanelView view) {
                                    Log.e(TAG, "唤起面板 : " + view);
                                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
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

    private void getFocus(){
        edit_text.setFocusable(true);
        edit_text.setFocusableInTouchMode(true);
        edit_text.findFocus();
        edit_text.requestFocus();
    }

    @Override
    public boolean onKey(@org.jetbrains.annotations.Nullable DialogInterface dialog, int keyCode, @NotNull KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return false;
    }

}
