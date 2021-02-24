package com.kuanquan.emotionkeyboard.emotionkeyboardview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kuanquan.emotionkeyboard.MyLinearLayout;
import com.kuanquan.emotionkeyboard.R;
import com.kuanquan.emotionkeyboard.utils.LogUtils;

/**
 * author : kuanquan
 * time : 2016年1月5日 上午11:14:27
 * email : shinekuanquan@163.com
 * description :源码来自开源项目https://github.com/dss886/Android-EmotionInputDetector
 * 				本人仅做细微修改以及代码解析
 */
public class EmotionKeyboard {
	
    	private static final String SHARE_PREFERENCE_NAME = "EmotionKeyboard";
        private static final String SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "soft_input_height";
        private static final String HAVE_COMPUTE_HEIGHT="HAVE_COMPUTE_HEIGHT";

	 	private Activity mActivity;
	    private InputMethodManager mInputManager;//软键盘管理类
	    private SharedPreferences sp;
	    private View mEmotionLayout;//表情布局
	    private EditText mEditText;//
	    private View mContentView;//内容布局view,即除了表情布局或者软键盘布局以外的布局，用于固定bar的高度，防止跳闪
		private int preHeight;
		private boolean hasComputeRightHeight;
		private int realHeight;

	    private EmotionKeyboard(){

	    }
	    
	    /**
	     * 外部静态调用
	     * @param activity
	     * @return
	     */
	    public static EmotionKeyboard with(Activity activity) {
	    	EmotionKeyboard emotionInputDetector = new EmotionKeyboard();
	        emotionInputDetector.mActivity = activity;
	        emotionInputDetector.mInputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	        emotionInputDetector.sp = activity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
	        emotionInputDetector.hasComputeRightHeight = emotionInputDetector.sp.getBoolean(HAVE_COMPUTE_HEIGHT,false);
	        emotionInputDetector.realHeight = emotionInputDetector.sp.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT,-1);
	        return emotionInputDetector;
	    }
	    
	    /**
	     * 绑定内容view，此view用于固定bar的高度，防止跳闪
	     * @param contentView
	     * @return
	     */
	    public EmotionKeyboard bindToContent(View contentView) {
	        mContentView = contentView;
	        return this;
	    }
	    
	    /**
	     * 绑定编辑框
	     * @param editText
	     * @return
	     */
	    public EmotionKeyboard bindToEditText(EditText editText) {
	        mEditText = editText;
	        mEditText.requestFocus();
	        mEditText.setOnTouchListener(new View.OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	            	Log.d("zyp","mEditText  onTouch");
	                if (event.getAction() == MotionEvent.ACTION_UP && mEmotionLayout.isShown()) {
	                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
	                    hideEmotionLayout(true,false);//隐藏表情布局，显示软件盘
	                    //软件盘显示后，释放内容高度
	                    mEditText.postDelayed(new Runnable() {
	                        @Override
	                        public void run() {
	                            unlockContentHeightDelayed();
	                        }
	                    }, 200L);
	                }
	                notifyInputBarPop();
	                return false;
	            }
	        });
			mEditText.postDelayed(new Runnable() {
				@Override
				public void run() {
					TextView aboveText = (TextView) mActivity.findViewById(R.id.aboveText);
					aboveText.setOnTouchListener(new View.OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							Log.d("zyp","aboveText  onTouch");
							if (event.getAction() == MotionEvent.ACTION_UP && mEmotionLayout.isShown()) {
								lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
								hideEmotionLayout(true,false);//隐藏表情布局，显示软件盘
								//软件盘显示后，释放内容高度
								mEditText.postDelayed(new Runnable() {
									@Override
									public void run() {
										unlockContentHeightDelayed();
									}
								}, 200L);
							}
							return true;
						}
					});
				}
			},200);
/*	        MyLinearLayout myLinearLayout= (MyLinearLayout) mActivity.findViewById(R.id.myRoot);
	        myLinearLayout.setOnKeyboardVisibilityChangedListener(new MyLinearLayout.onKeyboardVisibilityChangedListener() {
				@Override
				public void onKeyboardVisibilityChanged(boolean keyboardShow, int keyboardHeight) {
					Log.d("zyp",keyboardShow+" onKeyboardVisibilityChanged "+keyboardHeight);
					if(keyboardShow && keyboardHeight>0){
						mEmotionLayout.getLayoutParams().height=keyboardHeight;
						Rect rect = new Rect();
						boolean visibile = mActivity.findViewById(R.id.redline).getLocalVisibleRect(rect);
						Log.d("zyp","visibile  "+visibile+"   "+rect.toShortString());
						mActivity.findViewById(R.id.containers).requestLayout();
					}
				}
			});*/
	        return this;
	    }
	    
	    /**
	     * 绑定表情按钮
	     * @param emotionButton
	     * @return
	     */
	    public EmotionKeyboard bindToEmotionButton(View emotionButton) {
	        emotionButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mEmotionLayout.isShown()) {
						Log.d("zyp","表情面板现在是显示的，我要去隐藏他");
						lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
						hideEmotionLayout(true,false);//隐藏表情布局，显示软件盘
						unlockContentHeightDelayed();//软件盘显示后，释放内容高度

					} else {
						Log.d("zyp","表情面板现在是隐藏的，我要去显示他");
						if (isSoftInputShown()) {//同上
							Log.d("zyp","表情面板现在是隐藏的，输入法是显示的");
							lockContentHeight();
							showEmotionLayout();
							unlockContentHeightDelayed();
						} else {
							Log.d("zyp","表情面板现在是隐藏的，输入法也是隐藏的");
							showEmotionLayout();//两者都没显示，直接显示表情布局

							notifyInputBarPop();
						}
					}
				}
			});
	        return this;
	    }

	    /**
	     * 设置表情内容布局
	     * @param emotionView
	     * @return
	     */
	    public EmotionKeyboard setEmotionView(View emotionView) {
	        mEmotionLayout = emotionView;
	        return this;
	    }

	    public EmotionKeyboard build(){
	        //设置软件盘的模式：SOFT_INPUT_ADJUST_RESIZE  这个属性表示Activity的主窗口总是会被调整大小，从而保证软键盘显示空间。
	        //从而方便我们计算软件盘的高度
	        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
	                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	        //隐藏软件盘
	        hideSoftInput();
	        return this;
	    }

	    /**
	     * 点击返回键时先隐藏表情布局
	     * @return
	     */
	    public boolean interceptBackPress() {
	        if (mEmotionLayout.isShown()) {
	            hideEmotionLayout(false,false);
	            return true;
	        }
	        return false;
	    }

	    private void showEmotionLayout() {
	        int softInputHeight = getSupportSoftInputHeight(true);
			Log.d("zyp","showEmotionLayout   getSupportSoftInputHeight"+softInputHeight);
	        if (softInputHeight == 0) {
	            softInputHeight = getKeyBoardHeight();
	            Log.d("zyp","没取到值，从sp文件里面获取到的值是"+softInputHeight);
	        }
	        hideSoftInput();
			Log.d("zyp","现在给表情面板设置的高度为"+softInputHeight);
	        mEmotionLayout.getLayoutParams().height = softInputHeight;

	        mEmotionLayout.setVisibility(View.VISIBLE);


			TextView aboveText = (TextView) mActivity.findViewById(R.id.aboveText);
			aboveText.setVisibility(View.VISIBLE);

	    }

	    /**
	     * 隐藏表情布局
	     * @param showSoftInput 是否显示软件盘
	     */
	    public void hideEmotionLayout(boolean showSoftInput,boolean delay) {

	        if (mEmotionLayout.isShown()) {
	            mEmotionLayout.setVisibility(View.GONE);
				TextView aboveText = (TextView) mActivity.findViewById(R.id.aboveText);
				aboveText.setVisibility(View.GONE);
	            if (showSoftInput) {
	                showSoftInput( delay);
	            }
	        }
	    }

	    /**
	     * 锁定内容高度，防止跳闪
	     */
	    private void lockContentHeight() {
	        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
	        params.height = mContentView.getHeight();
	        params.weight = 0.0F;
	    }

	    /**
	     * 释放被锁定的内容高度
	     */
	    private void unlockContentHeightDelayed() {
	        mEditText.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	                ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
	            }
	        }, 200L);
	    }

	    /**
	     * 编辑框获取焦点，并显示软件盘
	     */
	    private void showSoftInput(boolean delay) {
	    	delay = !hasComputeRightHeight;
			Runnable runnable = new Runnable() {
				@Override
				public void run() {

					mEditText.requestFocus();
					mInputManager.showSoftInput(mEditText, 0);
					if(!hasComputeRightHeight){
						mEditText.postDelayed(new Runnable() {
							@Override
							public void run() {
								getSupportSoftInputHeight(true);
							}
						},200);
					}
				}
			};
			if(delay){
				Log.d("zyp","延时之后再弹出输入法");
				mEditText.postDelayed(runnable,300);//300
			}else{
				Log.d("zyp","直接弹出输入法");
				mEditText.post(runnable);
			}


	    }

	    /**
	     * 隐藏软件盘
	     */
	    public void hideSoftInput() {
	        mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	    }

	    /**
	     * 是否显示软件盘
	     * @return
	     */
	    private boolean isSoftInputShown() {
	        return getSupportSoftInputHeight(false) != 0;
	    }

	    /**
	     * 获取软件盘的高度
	     * @return
	     */
	    private int getSupportSoftInputHeight(boolean canUseCache) {
	    	if(canUseCache && hasComputeRightHeight && realHeight>0){
	    		Log.d("zyp","已经计算过啦，取内存缓存");
	    	  return 	realHeight;
			}
	        Rect r = new Rect();
	        /**
	         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
	         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
	         */
	        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
	        //获取屏幕的高度
	        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
	        //计算软件盘的高度
	        int softInputHeight = screenHeight - r.bottom;

	        /**
	         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
	         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
	         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
	         */
	        if (Build.VERSION.SDK_INT >= 20) {
	            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
	            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
	        }

	        if (softInputHeight < 0) {
	            LogUtils.w("EmotionKeyboard--Warning: value of softInputHeight is below zero!");
	        }
	        //存一份到本地
	        if (softInputHeight > 0) {
				Log.d("zyp"," 存一份到sp文件 "+softInputHeight);
	            sp.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).apply();
	            sp.edit().putBoolean(HAVE_COMPUTE_HEIGHT,true);
	            hasComputeRightHeight = true;
	            realHeight = softInputHeight;
	        }
	        return softInputHeight;
	    }


	    /**
	     * 底部虚拟按键栏的高度
	     * @return
	     */
	    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	    private int getSoftButtonsBarHeight() {
	        DisplayMetrics metrics = new DisplayMetrics();
	        //这个方法获取可能不是真实屏幕的高度
	        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	        int usableHeight = metrics.heightPixels;
	        //获取当前屏幕的真实高度
	        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
	        int realHeight = metrics.heightPixels;
	        if (realHeight > usableHeight) {
	            return realHeight - usableHeight;
	        } else {
	            return 0;
	        }
	    }

	/**
	 * 获取软键盘高度，由于第一次直接弹出表情时会出现小问题，787是一个均值，作为临时解决方案
	 * @return
	 */
	public int getKeyBoardHeight(){
		return sp.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, 787);

	}
	private void notifyInputBarPop(){
		if(mActivity instanceof OnInputBarPopListener){
			mEmotionLayout.postDelayed(new Runnable() {
				@Override
				public void run() {
					OnInputBarPopListener onInputBarPopListener = (OnInputBarPopListener) mActivity;
					onInputBarPopListener.onInputBarPop(true);
				}
			},350);


		}
	}
	public interface OnInputBarPopListener{
		void onInputBarPop(boolean popShow);
	}

}
