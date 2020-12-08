package com.maxxipoint.animator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements View.OnClickListener{

	/**
	 * 屏幕宽度。
	 */
	private int window_width;
	/**
	 * 屏幕高度。
	 */
	private int window_height;
	/**
	 * 圆型按钮的宽度。
	 */
	private int image_width;

	private ImageView shop;
	private ImageView fans;
	private ImageView news;
	private ImageView expression;
	private ImageView trails;
	private ImageView istar;
	private ImageView headImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		window_width = 300;
		window_height = 300;
		image_width = 60;
		WindowManager wm = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
		window_width = wm.getDefaultDisplay().getWidth();
		window_height = wm.getDefaultDisplay().getHeight() - 20;
//		image_width = window_width / 4;

		shop = (ImageView) findViewById(R.id.shop); // 顶部的图片控件
		fans = (ImageView) findViewById(R.id.fans); // 右边的图片控件
		news = (ImageView) findViewById(R.id.news); // 左边的图片控件
		expression = (ImageView) findViewById(R.id.expression); // 右下角图片控件
		trails = (ImageView) findViewById(R.id.trails); // 左下角图片控件
		istar = (ImageView) findViewById(R.id.istartlogo); // 中间的图片控件
		headImage = (ImageView) findViewById(R.id.head);  // 中间头像
		headImage.setOnClickListener(this);

//		RelativeLayout.LayoutParams paras = new RelativeLayout.LayoutParams(
//				image_width, image_width);
//		// Your X coordinate  x 轴坐标
//		paras.leftMargin = window_width / 2 - image_width / 2;
//		// Your Y coordinate  y 轴坐标
//		paras.topMargin = window_height / 2 - image_width / 2;
//
//		shop.setLayoutParams(paras);
//		fans.setLayoutParams(paras);
//		news.setLayoutParams(paras);
//		expression.setLayoutParams(paras);
//		trails.setLayoutParams(paras);
//		istar.setLayoutParams(paras);
//		headImage.setLayoutParams(paras);
	}

	// 开启动画
	private void startAnimator(){
		int startangle = 54; // 角度 方向 +1 正方向，-1 负方向。

		// Math.PI 圆周率 3.141592653589793
		// 圆周率乘以72°再除以180°是:  Math.sin(72 * Math.PI / 180
		// 是72角对应的弧度值

		int length = (int) ((window_width
				/ (Math.sin(72 * Math.PI / 180)) - image_width) / 2 - image_width / 5);

		// 右下角图片设置动画
		ValueAnimator expressionanim = zhixian(expression, startangle,
				length);
		// 左下角图片设置动画
		ValueAnimator trailsanim = zhixian(trails, startangle + 72,
				length);
		// 左边图片设置动画
		ValueAnimator newsanim = zhixian(news, startangle + (72 * 2),
				length);
		// 顶部图片设置动画
		ValueAnimator shopanim = zhixian(shop, startangle + (72 * 3),
				length);
		// 右边图片设置动画
		ValueAnimator fansanim = zhixian(fans, startangle + (72 * 4),
				length);


		AnimatorSet animSet = new AnimatorSet();

		ObjectAnimator fadeIn = ObjectAnimator.ofFloat(headImage,
				"alpha", 0f, 1f);
		ObjectAnimator fadeOut = ObjectAnimator.ofFloat(istar, "alpha",
				1f, 0f);

		animSet.playTogether(fadeIn, fadeOut, shopanim, fansanim,
				newsanim, expressionanim, trailsanim);
		animSet.setDuration(1000);
		animSet.start();
	}

	/**
	 * 沿直线运动。
	 *
	 * @param view  要移动的对象。
	 * @param angle  方向 +1 正方向，-1 负方向。
	 * @param length 移动的距离
	 */
	public ValueAnimator zhixian(final View view, final int angle, final int length) {
		ValueAnimator valueAnimator = new ValueAnimator();
		valueAnimator.setObjectValues(new PointF(0, 0));
		valueAnimator.setInterpolator(new LinearInterpolator());
		valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
			// fraction = t / duration
			@Override
			public PointF evaluate(float fraction, PointF startValue,
								   PointF endValue) {
				Log.v("znz", "znz ---> " + fraction);
				PointF point = new PointF();
				point.x = (float) (fraction * length * Math.cos(angle * Math.PI
						/ 180));
				point.y = (float) (fraction * length * Math.sin(angle * Math.PI
						/ 180));
				return point;
			}
		});
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				PointF point = (PointF) animation.getAnimatedValue();
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						image_width, image_width);
				params.leftMargin = (int) point.x
						+ (window_width / 2 - image_width / 2); // Your
				// coordinate
				params.topMargin = (int) point.y
						+ (window_height / 2 - image_width / 2); // Your Y
				// coordinate
				Log.v("znz", "point.x ---> " + point.x);
				Log.v("znz", "point.y ---> " + point.y);
				view.setLayoutParams(params);
			}
		});
		return valueAnimator;
	}

	@Override
	public void onClick(View v) {
		startAnimator();
	}
}
