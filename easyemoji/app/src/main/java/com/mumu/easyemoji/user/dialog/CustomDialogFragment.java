package com.mumu.easyemoji.user.dialog;

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
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.mumu.easyemoji.EmotionInputDetector;
import com.mumu.easyemoji.KeyBoardUtils;
import com.mumu.easyemoji.R;
import com.mumu.easyemoji.WeiXinChatActivity;
import com.mumu.easyemoji.user.EmotionBean;
import com.mumu.easyemoji.user.EmotionRecyclerView;
import com.mumu.easyemoji.user.WfEmotionInputDetector;
import com.mumu.easyemoji.user.itemprovider.DisplayUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomDialogFragment extends BaseDialogFragment implements DialogInterface.OnKeyListener {

    private final static String TAG = "CustomDialogFragment";

    private SoftEditText edit_text;
    private ImageView emotion_btn;
    private EmotionRecyclerView recyclerView;
    private View contentView;
    private LinearLayout ll_face_container;
    private List<EmotionBean> emotionList = new ArrayList<>();
    private WfEmotionInputDetector mWfEmotionInputDetector;

    @Override
    protected int getLayoutId() {
        return R.layout.wf_dialog_feed_comment_layout;
    }

    public static CustomDialogFragment newInstance() {
        return new CustomDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (window != null) {
//            window.setGravity(Gravity.CENTER);
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
//            WindowManager.LayoutParams windowParams = window.getAttributes();
//            windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//            windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//            window.setAttributes(windowParams);
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            window.setDimAmount(0f);
//            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {
        super.initData();
        recyclerView.buildEmotionViews(
                edit_text,
                emotionList,
                DisplayUtils.getWidget(getContext()), DisplayUtils.getStatusBarHeight(getContext())
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        KeyBoardUtils.toggleInput(mContext);
        Log.e("onResume", "可视化");
    }

    @Override
    protected void initView() {
        getDialog().setOnKeyListener(this);
        edit_text = mRootView.findViewById(R.id.edit_text);
        emotion_btn = mRootView.findViewById(R.id.emotion_btn);
        recyclerView = mRootView.findViewById(R.id.recyclerView);
        contentView = mRootView.findViewById(R.id.contentView);
        ll_face_container = mRootView.findViewById(R.id.ll_face_container);
        mRootView.findViewById(R.id.textView).setOnClickListener(v -> {
//                emotion_btn.performClick();
            startActivity(new Intent(mRootView.getContext(), WeiXinChatActivity.class));
        });

        mWfEmotionInputDetector = WfEmotionInputDetector.with(getActivity())
                .bindToEditText(edit_text)
                .setEmotionView(ll_face_container)
                .bindToContent(contentView)
                .bindToEmotionButton(emotion_btn);
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
