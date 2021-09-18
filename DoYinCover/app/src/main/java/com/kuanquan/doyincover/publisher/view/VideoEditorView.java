package com.kuanquan.doyincover.publisher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kuanquan.doyincover.R;
import com.kuanquan.doyincover.utils.AnimUtils;
//import com.qiniu.pili.droid.shortvideo.PLTextView;
//import com.shuashuakan.android.modules.publisher.IMGTextEditDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2019/01/04
 * Description:
 */
public class VideoEditorView extends FrameLayout implements MultiTouchListener.OnMultiTouchListener {
  private View contentView;
  private View deleteView;
  private Context context;

//  private IMGTextEditDialog mAddTextDialog;

  public void setContentView(View contentView) {
    this.contentView = contentView;
  }

  public void setDeleteView(View deleteView) {
    this.deleteView = deleteView;
  }

  public VideoEditorView(Context context) {
    this(context, null);
  }

  public VideoEditorView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public VideoEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context=context;
  }

  private List<View> addedViews = new ArrayList<>();


  public void addText() {
//    if (mAddTextDialog == null) {
//      mAddTextDialog = new IMGTextEditDialog(getContext());
//    }
//    mAddTextDialog.show(new IMGTextEditDialog.Callback() {
//      @Override
//      public void onText(String text, int colorCode) {
//        addText(text, colorCode);
//      }
//    });
  }

  private void addText(String text, final int colorCodeTextView) {
    addText(null, text, colorCodeTextView);
  }

  @SuppressLint("ClickableViewAccessibility")
  private void addText(@Nullable Typeface textTypeface, String text, final int colorCodeTextView) {
//    PLTextView textInputTv = new PLTextView(getContext());
//    textInputTv.setText(text);
//    textInputTv.setTextColor(colorCodeTextView);
//    if (textTypeface != null) {
//      textInputTv.setTypeface(textTypeface);
//    }
//    MultiTouchListener multiTouchListener = getMultiTouchListener();
//
//    multiTouchListener.setOnGestureControl(() -> {
//      if (mAddTextDialog == null) {
//        mAddTextDialog = new IMGTextEditDialog(getContext());
//      }
//      mAddTextDialog.setText(textInputTv.getText().toString(), textInputTv.getCurrentTextColor());
//      mAddTextDialog.show((text1, colorCode) -> {
//        textInputTv.setText(text1);
//        textInputTv.setTextColor(colorCode);
//      });
//    });
//    textInputTv.setTag(ViewType.TEXT);
//    textInputTv.setOnTouchListener(multiTouchListener);
//    addViewToParent(textInputTv, ViewType.TEXT);
  }


  /**
   * Get root view by its type i.e image,text and emoji
   *
   * @param viewType image,text or emoji
   * @return rootview
   */
  private View getLayout(final ViewType viewType) {
    View rootView = null;
    switch (viewType) {
      case TEXT:
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_video_editor_text, null);
        TextView txtText = rootView.findViewById(R.id.tvPhotoEditorText);
        if (txtText != null) {
          txtText.setGravity(Gravity.CENTER);
          txtText.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        break;
      case IMAGE:
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_video_editor_text, null);
        break;
    }

    if (rootView != null) {
      //We are setting tag as ViewType to identify what type of the view it is
      //when we remove the view from stack i.e onRemoveViewListener(ViewType viewType, int numberOfAddedViews);
      rootView.setTag(viewType);
      final ImageView imgClose = rootView.findViewById(R.id.imgPhotoEditorClose);
      final View finalRootView = rootView;
      if (imgClose != null) {
        imgClose.setOnClickListener(v -> viewUndo(finalRootView));
      }
    }
    return rootView;
  }

  /**
   * Create a new instance and scalable touchview
   *
   * @return scalable multitouch listener
   */
  @NonNull
  private MultiTouchListener getMultiTouchListener() {
    MultiTouchListener multiTouchListener = new MultiTouchListener(
        deleteView,
        contentView,
        true,
        editorListener);

    multiTouchListener.setOnMultiTouchListener(this);
    return multiTouchListener;
  }

  /**
   * Add to root view from image,emoji and text to our parent view
   *
   * @param rootView rootview of image,text and emoji
   */
  private void addViewToParent(View rootView, ViewType viewType) {
    LayoutParams params = new LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
    addView(rootView, params);
    addedViews.add(rootView);
  }

  private void viewUndo(View removedView) {
    if (addedViews.size() > 0) {
      if (addedViews.contains(removedView)) {

        shockMethod(200);
        AnimUtils.setScaleAnim(removedView,1f,0f,350,false);
        AnimUtils.setScaleAnim(deleteView,1.5f,1f,350,false);

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            removeView(removedView);
            addedViews.remove(removedView);
          }
        },350);
      }
    }
  }

  private OnVideoEditorListener editorListener;

  public void setEditorListener(OnVideoEditorListener editorListener) {
    this.editorListener = editorListener;
  }

  @Override
  public void onEditTextClickListener(String text, int colorCode) {

  }

  @Override
  public void onRemoveViewListener(View removedView) {
    viewUndo(removedView);
  }
  /*
   * 震动效果
   */
  private  void shockMethod(long time) {
    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE));
    } else {
      vibrator.vibrate(time);
    }
  }
}
