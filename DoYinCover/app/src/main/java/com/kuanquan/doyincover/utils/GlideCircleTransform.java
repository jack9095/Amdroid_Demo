//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kuanquan.doyincover.utils;
 
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.security.MessageDigest;

/**
 * 集圆角/圆形/蒙层一体
 */
public class GlideCircleTransform implements Transformation<Bitmap> {
    private BitmapPool mBitmapPool;
    private  int coverColor = -1;
    private float radius;
    private boolean exceptLeftTop, exceptRightTop, exceptLeftBottom, exceptRightBotoom;
    /**
     * 除了那几个角不需要圆角的
     *
     * @param leftTop
     * @param rightTop
     * @param leftBottom
     * @param rightBottom
     */
    public void setExceptCorner(boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
        this.exceptLeftTop = leftTop;
        this.exceptRightTop = rightTop;
        this.exceptLeftBottom = leftBottom;
        this.exceptRightBotoom = rightBottom;
    }
    public GlideCircleTransform(Context context) {
        this(Glide.get(context).getBitmapPool());
    }
 
    public GlideCircleTransform(Context context, int coverColor) {
        this(Glide.get(context).getBitmapPool());
        this.coverColor = coverColor;
    }
 
    public GlideCircleTransform(Context context, int coverColor, float radius) {
        this(Glide.get(context).getBitmapPool());
        this.radius = radius;
        this.coverColor = coverColor;
    }
 
    public GlideCircleTransform(BitmapPool pool) {
        this.radius = 0.0F;
        this.mBitmapPool = pool;
    }

    @NonNull
    @Override
    public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();
        Bitmap bitmap = null;
        int width;
        int height;
        int afterWidth;
        if(this.radius > 0.0F) { //设置圆角
            int afterHeight;
            float bili;
            float bili1;
            if(outWidth > outHeight) {
                bili = (float)outHeight / (float)outWidth;
                afterWidth = source.getWidth();
                afterHeight = (int)((float)source.getWidth() * bili);
                if(afterHeight > source.getHeight()) {
                    bili1 = (float)outWidth / (float)outHeight;
                    afterHeight = source.getHeight();
                    afterWidth = (int)((float)source.getHeight() * bili1);
                }
            } else if(outWidth < outHeight) {
                bili = (float)outWidth / (float)outHeight;
                afterHeight = source.getHeight();
                afterWidth = (int)((float)source.getHeight() * bili);
                if(afterWidth > source.getWidth()) {
                    bili1 = (float)outHeight / (float)outWidth;
                    afterWidth = source.getWidth();
                    afterHeight = (int)((float)source.getWidth() * bili1);
                }
            } else {
                afterHeight = source.getHeight();
                afterWidth = afterHeight;
            }
            width = (source.getWidth() - afterWidth) / 2;
            height = (source.getHeight() - afterHeight) / 2;
            //修正圆角
            this.radius *= (float)afterHeight / (float)outHeight;
            bitmap = this.mBitmapPool.get(afterWidth, afterHeight, Config.ARGB_8888);
            if(bitmap == null) {
                bitmap = Bitmap.createBitmap(afterWidth, afterHeight, Config.ARGB_8888);
            }
 
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(source, TileMode.CLAMP, TileMode.CLAMP);
            if(width != 0 || height != 0) {
                Matrix matrix = new Matrix();
                matrix.setTranslate((float)(-width), (float)(-height));
                shader.setLocalMatrix(matrix);
            }
 
            paint.setShader(shader);
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0.0F, 0.0F, (float)canvas.getWidth(), (float)canvas.getHeight());
            canvas.drawRoundRect(rectF, this.radius, this.radius, paint);
            
            //设置蒙层
            Paint coverPaint = new Paint();
            coverPaint.setAntiAlias(true);
            coverPaint.setColor(coverColor);
            if(coverColor > -1) {
                canvas.drawRoundRect(rectF, this.radius, this.radius, coverPaint);
            }
 
 
            if (exceptLeftTop) { //左上角不为圆角
                canvas.drawRect(0, 0, radius, radius, paint);
                if (coverColor > -1) {
                    canvas.drawRect(0, 0, radius, radius, coverPaint);
                }
            }
            if (exceptRightTop) {//右上角不为圆角
                canvas.drawRect(canvas.getWidth() - radius, 0, canvas.getWidth(), radius, paint);
                if (coverColor > -1) {
                    canvas.drawRect(canvas.getWidth() - radius, 0, canvas.getWidth(), radius, coverPaint);
                }
            }
 
            if (exceptLeftBottom) {//左下角不为圆角
                canvas.drawRect(0, canvas.getHeight() - radius, radius, canvas.getHeight(), paint);
                if (coverColor > -1) {
                    canvas.drawRect(0, canvas.getHeight() - radius, radius, canvas.getHeight(), coverPaint);
                }
            }
 
            if (exceptRightBotoom) {//右下角不为圆角
                canvas.drawRect(canvas.getWidth() - radius, canvas.getHeight() - radius, canvas.getWidth(), canvas.getHeight(), paint);
                if (coverColor > -1) {
                    canvas.drawRect(canvas.getWidth() - radius, canvas.getHeight() - radius, canvas.getWidth(), canvas.getHeight(), coverPaint);
                }
            }
 
 
 
        } else { //设置圆形
            width = Math.min(source.getWidth(), source.getHeight());
            height = (source.getWidth() - width) / 2;
            afterWidth = (source.getHeight() - width) / 2;
            bitmap = this.mBitmapPool.get(width, width, Config.ARGB_8888);
            if(bitmap == null) {
                bitmap = Bitmap.createBitmap(width, width, Config.ARGB_8888);
            }
 
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(source, TileMode.CLAMP, TileMode.CLAMP);
            if(height != 0 || afterWidth != 0) {
                Matrix matrix = new Matrix();
                matrix.setTranslate((float)(-height), (float)(-afterWidth));
                shader.setLocalMatrix(matrix);
            }
 
            paint.setShader(shader);
            paint.setAntiAlias(true);
            float r = (float)width / 2.0F;
            canvas.drawCircle(r, r, r, paint);
            //设置蒙层
            if(coverColor != -1) {
                Paint paint1 = new Paint();
                paint1.setAntiAlias(true);
                paint1.setColor(coverColor);
                canvas.drawCircle(r, r, r, paint1);
            }
        }
 
        return BitmapResource.obtain(bitmap, this.mBitmapPool);
    }
 
    public String getId() {
        return this.getClass().getName();
    }



    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
