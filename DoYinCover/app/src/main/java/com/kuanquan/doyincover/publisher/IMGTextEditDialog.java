//package com.kuanquan.doyincover.publisher;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager.LayoutParams;
//import android.widget.EditText;
//import android.widget.RadioGroup;
//
//import com.kuanquan.doyincover.R;
//import com.kuanquan.doyincover.publisher.view.IMGColorGroup;
//
//
///**
// * Created by felix on 2017/12/1 上午11:21.
// */
//
//public class IMGTextEditDialog extends Dialog implements View.OnClickListener,
//    RadioGroup.OnCheckedChangeListener {
//  private EditText mEditText;
//
//  private Callback mCallback;
//
//  private String mDefaultText;
//
//  private int mColorCode;
//
//  private IMGColorGroup mColorGroup;
//
//  public IMGTextEditDialog(Context context) {
//    super(context, R.style.ImageTextDialog);
//    setContentView(R.layout.dialog_video_edit_text);
//    Window window = getWindow();
//    if (window != null) {
//      window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//    }
//    setOnShowListener(dialog -> {
//      mEditText.setFocusable(true);
//      mEditText.setFocusableInTouchMode(true);
//      mEditText.requestFocus();
//    });
//  }
//
//  @Override
//  protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//
//    mColorGroup = findViewById(R.id.cg_colors);
//    mColorGroup.setOnCheckedChangeListener(this);
//    mEditText = findViewById(R.id.et_text);
//
//    findViewById(R.id.cancel).setOnClickListener(this);
//    findViewById(R.id.done).setOnClickListener(this);
//  }
//
//  @Override
//  protected void onStart() {
//    super.onStart();
//    if (mDefaultText != null) {
//      mEditText.setText(mDefaultText);
//      mEditText.setTextColor(mColorCode);
//      if (!mDefaultText.isEmpty()) {
//        mEditText.setSelection(mEditText.length());
//      }
//      mDefaultText = null;
//    } else mEditText.setText("");
//    mColorGroup.setCheckColor(mEditText.getCurrentTextColor());
//  }
//
//  public void setText(String text, int colorCode) {
//    mDefaultText = text;
//    mColorCode = colorCode;
//  }
//
//  public void reset() {
//    setText(null, Color.WHITE);
//  }
//
//  @Override
//  public void onClick(View v) {
//    int vid = v.getId();
//    if (vid == R.id.done) {
//      onDone();
//    } else if (vid == R.id.cancel) {
//      dismiss();
//    }
//  }
//
//  public void show(Callback callback) {
//    mCallback = callback;
//    show();
//  }
//
//  private void onDone() {
//    String text = mEditText.getText().toString();
//    if (!TextUtils.isEmpty(text) && mCallback != null) {
//      mCallback.onText(text, mEditText.getCurrentTextColor());
//    }
//    dismiss();
//  }
//
//  @Override
//  public void onCheckedChanged(RadioGroup group, int checkedId) {
//    mEditText.setTextColor(mColorGroup.getCheckColor());
//  }
//
//  public interface Callback {
//    void onText(String text, int colorCode);
//  }
//}
