//package com.kuanquan.panelemojikeyboard.scene.chat;
//
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import androidx.core.content.ContextCompat;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.databinding.DataBindingUtil;
//import androidx.fragment.app.DialogFragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import com.effective.android.panel.PanelSwitchHelper;
//import com.effective.android.panel.interfaces.listener.OnPanelChangeListener;
//import com.effective.android.panel.view.panel.IPanelView;
//import com.effective.android.panel.view.panel.PanelView;
//import com.kuanquan.panelemojikeyboard.R;
//import com.kuanquan.panelemojikeyboard.databinding.CommonChatWithTitlebarLayoutBinding;
//import com.kuanquan.panelemojikeyboard.scene.chat.adapter.ChatAdapter;
//import com.kuanquan.panelemojikeyboard.scene.chat.adapter.ChatInfo;
//import com.kuanquan.panelemojikeyboard.scene.chat.emotion.EmotionPagerView;
//import com.kuanquan.panelemojikeyboard.scene.chat.emotion.Emotions;
//import com.kuanquan.panelemojikeyboard.util.DisplayUtils;
//
//public class ChatDialogFragment extends DialogFragment implements DialogInterface.OnKeyListener {
//
//    private CommonChatWithTitlebarLayoutBinding mBinding;
//    private PanelSwitchHelper mHelper;
//    private ChatAdapter mAdapter;
//    private LinearLayoutManager mLinearLayoutManager;
//    private static final String TAG = "ChatDialogFragment";
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.common_chat_with_titlebar_layout, container, false);
//        mBinding.statusBar.setVisibility(View.GONE);
//        mBinding.titleBar.setVisibility(View.VISIBLE);
//        mBinding.titleBar.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
//        mBinding.title.setText(R.string.dialog_fragment_name);
//        initView();
//        return mBinding.getRoot();
//    }
//
//    /**
//     * dialogfragment基于dialog实现，需要设置以下代码
//     */
//    @Override
//    public void onStart() {
//        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            dialog.setOnKeyListener(this);
//            int width = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = ViewGroup.LayoutParams.MATCH_PARENT;
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.common_page_bg_color)));
//            dialog.getWindow().setLayout(width, height);
//        }
//    }
//
//    private void initView() {
//        mLinearLayoutManager = new LinearLayoutManager(getContext());
//        mBinding.recyclerView.setLayoutManager(mLinearLayoutManager);
//        mAdapter = new ChatAdapter(getContext(), 50);
//        mBinding.recyclerView.setAdapter(mAdapter);
//        mBinding.send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String content = mBinding.editText.getText().toString();
//                if (TextUtils.isEmpty(content)) {
//                    Toast.makeText(getContext(), "当前没有输入", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                mAdapter.insertInfo(ChatInfo.CREATE(content));
//                mBinding.editText.setText(null);
//                scrollToBottom();
//            }
//        });
//    }
//
//    private void scrollToBottom() {
//        mLinearLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (mHelper == null) {
//            mHelper = new PanelSwitchHelper.Builder(this)
//                    //可选
//                    .addKeyboardStateListener((visible, height) -> Log.d(TAG, "系统键盘是否可见 : " + visible + " 高度为：" + height))
//                    //可选
//                    .addEditTextFocusChangeListener((view, hasFocus) -> {
//                        Log.d(TAG, "输入框是否获得焦点 : " + hasFocus);
//                        if(hasFocus){
//                            scrollToBottom();
//                        }
//                    })
//                    //可选
//                    .addViewClickListener(view -> {
//                        switch (view.getId()){
//                            case R.id.edit_text:
//                            case R.id.add_btn:
//                            case R.id.emotion_btn:{
//                                scrollToBottom();
//                            }
//                        }
//                        Log.d(TAG, "点击了View : " + view);
//                    })
//                    //可选
//                    .addPanelChangeListener(new OnPanelChangeListener() {
//
//                        @Override
//                        public void onKeyboard() {
//                            Log.d(TAG, "唤起系统输入法");
//                            mBinding.emotionBtn.setSelected(false);
//                        }
//
//                        @Override
//                        public void onNone() {
//                            Log.d(TAG, "隐藏所有面板");
//                            mBinding.emotionBtn.setSelected(false);
//                        }
//
//                        @Override
//                        public void onPanel(IPanelView view) {
//                            Log.d(TAG, "唤起面板 : " + view);
//                            if(view instanceof PanelView){
//                                mBinding.emotionBtn.setSelected(((PanelView)view).getId() == R.id.panel_emotion ? true : false);
//                            }
//                        }
//
//                        @Override
//                        public void onPanelSizeChange(IPanelView panelView, boolean portrait, int oldWidth, int oldHeight, int width, int height) {
//                            if(panelView instanceof PanelView){
//                                switch (((PanelView)panelView).getId()) {
//                                    case R.id.panel_emotion: {
//                                        EmotionPagerView pagerView = mBinding.getRoot().findViewById(R.id.view_pager);
//                                        int viewPagerSize = height - DisplayUtils.dip2px(getContext(), 30f);
//                                        pagerView.buildEmotionViews(
//                                                mBinding.editText,
//                                                Emotions.getEmotions(), width, viewPagerSize);
//                                        break;
//                                    }
//                                    case R.id.panel_addition: {
//                                        //auto center,nothing to do
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    })
//                    .logTrack(true)             //output log
//                    .build();
//        }
//        mBinding.recyclerView.setPanelSwitchHelper(mHelper);
//    }
//
//
//    @Override
//    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//            if (mHelper != null && mHelper.hookSystemBackByPanelSwitcher()) {
//                return true;
//            } else {
//                dismiss();
//                return true;
//            }
//        }
//        return false;
//    }
//
//}
