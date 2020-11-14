package com.example.expandtextview.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.security.MessageDigest;


/**
 * 作者： njb
 * 时间： 2018/5/15 0015-下午 1:54
 * 描述： glide工具类
 * 来源：
 */
public class GlideUtils {

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadFile(Context context, Object url, ImageView imageView) {
        if (context == null || imageView == null) {
            return;
        }
        Glide.with(context).load(url).apply(new RequestOptions().centerCrop()).into(imageView);
    }

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImg(Context context, String url, ImageView imageView) {
        if (context == null || imageView == null) {
            return;
        }
        Glide.with(context).load(url).apply(new RequestOptions().centerCrop()).into(imageView);
    }

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImg(Context context, String url, ImageView imageView, int resDrawable) {
        if (context == null || imageView == null) {
            return;
        }

        Glide.with(context).load(url).apply(new RequestOptions().centerCrop().error(resDrawable).placeholder(resDrawable)).into(imageView);
    }

    public static void loadTargetImg(Context context, String url, final ImageView imageView, int resDrawable) {
        if (context == null || imageView == null) {
            return;
        }

        Glide.with(context).load(url).apply(new RequestOptions().centerCrop().error(resDrawable).placeholder(resDrawable)).into(new SimpleTarget<Drawable>(60,60) {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (resource != null) {
                    imageView.setImageDrawable(resource);
                }
            }
        });
    }


    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param width
     * @param heigth
     */
    public static void loadImg(Context context, String url, ImageView imageView, int width, int heigth) {
        if (context == null || imageView == null) {
            return;
        }
        Glide.with(context).load(url).apply(new RequestOptions().centerCrop().override(width, heigth)).into(imageView);
    }

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param width
     * @param heigth
     */
    public static void loadImg(Context context, String url, ImageView imageView, int resDrawable, int width, int heigth) {
        if (context == null || imageView == null) {
            return;
        }
        Glide.with(context).load(url).apply(new RequestOptions().centerCrop().error(resDrawable).placeholder(resDrawable).override(width, heigth)).into(imageView);
    }

    /**
     * 自适应
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImgByWrap(Context context, String url, final ImageView imageView) {
        loadImgByWrap(context, url, imageView, 2);
    }

    /**
     * 自适应
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImgByWrap(Context context, String url, final ImageView imageView, final int scale) {
        final int width = ScreenUtils.getScreenWidth(context);

        if (context == null || imageView == null) {
            return;
        }

        Glide.with(context).load(url).apply(new RequestOptions().centerCrop()).into(new SimpleTarget<Drawable>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (resource != null) {
                    float resize = resource.getIntrinsicWidth() / (float) width;
                    if (imageView.getParent() instanceof LinearLayout) {
                        //修改尺寸为原来的一半
                        resource.setBounds(0, 0, resource.getIntrinsicWidth() / scale, resource.getIntrinsicHeight() / scale);
                        imageView.setBackground(resource);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, (int) (resource.getIntrinsicHeight() / resize));
                        imageView.setLayoutParams(params);
                    } else if (imageView.getParent() instanceof ConstraintLayout) {
                        //修改尺寸为原来的一半
                        resource.setBounds(0, 0, resource.getIntrinsicWidth() / scale, resource.getIntrinsicHeight() / scale);
                        imageView.setBackground(resource);
                        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width, (int) (resource.getIntrinsicHeight() / resize));
                        imageView.setLayoutParams(params);
                    }
                }
            }
        });

    }


    public static void loadRound(Context context, String url, ImageView imageView) {
        loadRoundImg(context, url, imageView, 0);
    }

    public static void loadRound(Context context, String url, ImageView imageView, int resDrawable) {
        if (resDrawable == 0) {
            Glide.with(context).load(url).apply(new RequestOptions().centerCrop().circleCrop()).into(imageView);
        } else {
            Glide.with(context).load(url).apply(new RequestOptions().centerCrop().circleCrop()
                    .error(resDrawable).placeholder(resDrawable)).into(imageView);
        }
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadRoundImg(Context context, String url, ImageView imageView) {
        loadRoundImg(context, url, imageView, 0);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param resDrawable
     */
    public static void loadRoundImg(Context context, String url, ImageView imageView, int resDrawable) {
        if (context == null || imageView == null) {
            return;
        }
        if (resDrawable == 0) {
            Glide.with(context).load(url).apply(new RequestOptions().centerCrop().circleCrop()).into(imageView);
        } else {
            Glide.with(context).load(url).apply(new RequestOptions().centerCrop().circleCrop()
                    .error(resDrawable).placeholder(resDrawable)).into(imageView);
        }

    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param resDrawable
     */
    public static void loadRoundImg(Context context, String url, ImageView imageView, int resDrawable, int width, int height) {
        if (context == null || imageView == null) {
            return;
        }
        if (resDrawable == 0) {
            Glide.with(context).load(url).apply(new RequestOptions().centerCrop().circleCrop().override(width, height)).into(imageView);
        } else {
            Glide.with(context).load(url).apply(new RequestOptions().centerCrop().circleCrop()
                    .error(resDrawable).placeholder(resDrawable).override(width, height)).into(imageView);
        }

    }


    /**
     * 加载圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param resDrawable
     */
    public static void loadRadiusImg(Context context, String url, ImageView imageView, int resDrawable) {
        loadRadiusImg(context, url, imageView, resDrawable, 4);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param resDrawable
     * @param px
     */
    public static void loadRadiusImg(Context context, String url, ImageView imageView, int resDrawable, int px) {

        if (context == null || imageView == null) {
            return;
        }

        Glide.with(context).load(url).apply(new RequestOptions().centerCrop()
                .transform(new GlideRoundTransform(px)).error(resDrawable).placeholder(resDrawable))
                .into(imageView);
    }





    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;

    }


    public static class GlideRoundTransform extends BitmapTransformation {
        private float radius = 0f;

        public GlideRoundTransform() {
            this(4);
        }

        public GlideRoundTransform(int px) {
            super();
            this.radius = Resources.getSystem().getDisplayMetrics().density * px;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null)
                return null;
            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }


        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }


}
