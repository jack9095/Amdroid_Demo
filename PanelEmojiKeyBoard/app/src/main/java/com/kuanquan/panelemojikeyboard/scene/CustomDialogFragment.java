package com.kuanquan.panelemojikeyboard.scene;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.effective.android.panel.PanelSwitchHelper;
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener;
import com.effective.android.panel.view.panel.IPanelView;
import com.effective.android.panel.view.panel.PanelView;
import com.kuanquan.panelemojikeyboard.MainActivity;
import com.kuanquan.panelemojikeyboard.R;
import com.kuanquan.panelemojikeyboard.scene.chat.emotion.EmotionPagerView;
import com.kuanquan.panelemojikeyboard.scene.chat.emotion.Emotions;
import com.kuanquan.panelemojikeyboard.scene.feed.BaseDialogFragment;
import com.kuanquan.panelemojikeyboard.util.DisplayUtils;
import com.kuanquan.panelemojikeyboard.view.SoftEditText;

import org.jetbrains.annotations.NotNull;

public class CustomDialogFragment extends BaseDialogFragment implements DialogInterface.OnKeyListener {

    private final static String TAG = "CustomDialogFragment";

    private PanelSwitchHelper helper;
    private SoftEditText edit_text;
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
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
    protected void initData() {
        super.initData();

        edit_text = mRootView.findViewById(R.id.edit_text);
        emotion_btn = mRootView.findViewById(R.id.emotion_btn);
        mRootView.findViewById(R.id.textView).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                emotion_btn.performClick();
                isClickGoOtherPage = true;
                getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                startActivity(new Intent(mRootView.getContext(), MainActivity.class));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        getDialog().
    }

    private boolean isClickGoOtherPage;

    @Override
    public void onStart() {
        super.onStart();
        getDialog().setOnKeyListener(this);
        Window window = getDialog().getWindow();
        if (window != null) {
            Log.e("onStart", "清除");
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("onResume", "可视化");
        if (isClickGoOtherPage) {
            isClickGoOtherPage = false;
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            edit_text.setFocusable(true);
            edit_text.setFocusableInTouchMode(true);
            edit_text.findFocus();
            edit_text.requestFocus();

            if (!emotion_btn.isSelected()) {
//                helper.showKeyboard();
                InputMethodManager inputManager = (InputMethodManager)edit_text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(edit_text,InputMethodManager.RESULT_SHOWN);
                inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }

    private boolean softKeyBoardVisible;
    @Override
    protected void initView() {
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (null != window) {
                if (helper == null) {
                    helper = new PanelSwitchHelper.Builder(window, mRootView)
                            //可选
                            .addKeyboardStateListener((visible, height) -> {
                                softKeyBoardVisible = visible;
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
                                    Log.e(TAG, "onNone -> 隐藏所有面板");
                                    if (!isOnPause) {
                                        Log.e(TAG, "隐藏所有面板");
                                        emotion_btn.setSelected(false);
//                                        dismiss();
                                    } else {
                                        Log.e(TAG, "强制弹出键盘  == " + softKeyBoardVisible);
//                                        helper.showKeyboard();
//                                        emotion_btn.performClick();
                                        edit_text.setFocusable(true);
                                        edit_text.setFocusableInTouchMode(true);
                                        edit_text.findFocus();
                                        edit_text.requestFocus();
                                    }

//                                    if (!softKeyBoardVisible && !isOnPause) {
//                                        edit_text.setFocusable(true);
//                                        edit_text.setFocusableInTouchMode(true);
//                                        edit_text.findFocus();
//                                        edit_text.requestFocus();
//                                    }


                                    isOnPause = false;
                                }

                                @Override
                                public void onPanel(IPanelView view) {
                                    Log.e(TAG, "唤起面板 : " + view);
                                    // TODO 这行代码很关键，解决表情键盘切换的问题
//                                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                                    if (view instanceof PanelView) {
                                        emotion_btn.setSelected(((PanelView) view).getId() == R.id.panel_emotion);
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

    private boolean isOnPause;
    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG,"onPause 软键盘按键");
        isOnPause = true;
        helper.resetState();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG,"onStop 软键盘按键");
//        helper.resetState();
    }

    @Override
    public boolean onKey(@org.jetbrains.annotations.Nullable DialogInterface dialog, int keyCode, @NotNull KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.e(TAG,"onKey 软键盘按键");
        }
        return false;
    }

}
