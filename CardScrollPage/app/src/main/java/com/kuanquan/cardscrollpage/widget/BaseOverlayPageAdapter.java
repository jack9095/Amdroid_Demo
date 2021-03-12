package com.kuanquan.cardscrollpage.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.*;
import android.widget.ImageView;

import androidx.annotation.*;
import androidx.viewpager.widget.*;

import com.bumptech.glide.*;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Queue;
import com.kuanquan.cardscrollpage.R;

public abstract class BaseOverlayPageAdapter extends PagerAdapter {
    private Context context;
    private String[] imgUrls;
    private WeakReference<Bitmap>[] bitmaps;
    protected RequestOptions mRequestOptions;

    private static final int NUM_SONGS = 10;

    // 优化内存
    private Queue<View> mReusableViews;

    public BaseOverlayPageAdapter(Context context, @NonNull RequestOptions imageOptions) {
        this.context = context;
        this.mRequestOptions = imageOptions;
        mReusableViews = new ArrayDeque<>(NUM_SONGS);
    }

    /**
     * item布局
     *
     * @return
     */
    protected abstract View itemView();

    public void setImgUrlsAndBindViewPager(ViewPager vp, String[] imgUrls, int layerAmount) {
        setImgUrlsAndBindViewPager(vp, imgUrls, layerAmount, -1, -1);
    }

    /**
     * @param vp
     * @param imgUrls
     * @param layerAmount 显示层数
     */
    public void setImgUrlsAndBindViewPager(ViewPager vp, String[] imgUrls, int layerAmount, float scaleOffset, float transOffset) {
        this.imgUrls = imgUrls;
        if (imgUrls != null && imgUrls.length > 0) {
            bitmaps = new WeakReference[imgUrls.length];
            vp.setOffscreenPageLimit(layerAmount);
            OverlayTransformer transformer = new OverlayTransformer(layerAmount, scaleOffset, transOffset);
            vp.setPageTransformer(true, transformer);
        }
    }

    @Override
    public int getCount() {
        if (null == imgUrls)
            return 0;
        if (imgUrls.length <= 1)
            return imgUrls.length;
        return Integer.MAX_VALUE;
//        return NUM_SONGS;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    protected ImageView findImageView(View rootView) {
        ImageView iv = rootView.findViewById(R.id.card_iv);
        if (null != iv)
            return iv;
        if (rootView instanceof ImageView) {
            return (ImageView) itemView();
        }
        throw new RuntimeException("you should set one of ImageViews id=card_iv or rootView=ImageView");
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final int p = position % imgUrls.length;
        final String imgUrl = imgUrls[p];
        View view = mReusableViews.poll();
        if (view == null) {
            view = itemView();
            if (null == view) {
                throw new RuntimeException("you should set a item layout");
            }
        }

        final ImageView iv = findImageView(view);
        if (null == iv) {
            throw new RuntimeException("you should set a item layout");
        }
        if (null != bitmaps && null != bitmaps[p] && null != bitmaps[p].get()) {
            iv.setImageBitmap(bitmaps[p].get());
        }
        Glide.with(context).asBitmap().load(imgUrl).apply(mRequestOptions).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                bitmaps[p] = new WeakReference<Bitmap>(resource);
                iv.setImageBitmap(resource);
            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                super.onLoadStarted(placeholder);
                iv.setImageDrawable(placeholder);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                iv.setImageDrawable(errorDrawable);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
//        mReusableViews.add((View) object);
    }
}
