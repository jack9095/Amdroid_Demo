package com.kuanquan.sinaweibopraise.glide;

import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.CheckResult;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

public final class GlideOptions extends RequestOptions implements Cloneable {
    private static GlideOptions fitCenterTransform0;
    private static GlideOptions centerInsideTransform1;
    private static GlideOptions centerCropTransform2;
    private static GlideOptions circleCropTransform3;
    private static GlideOptions noTransformation4;
    private static GlideOptions noAnimation5;

    public GlideOptions() {
    }

    @CheckResult
    @NonNull
    public static GlideOptions sizeMultiplierOf(@FloatRange(from = 0.0D,to = 1.0D) float value) {
        return (new GlideOptions()).sizeMultiplier(value);
    }

    @CheckResult
    @NonNull
    public static GlideOptions diskCacheStrategyOf(@NonNull DiskCacheStrategy strategy) {
        return (new GlideOptions()).diskCacheStrategy(strategy);
    }

    @CheckResult
    @NonNull
    public static GlideOptions priorityOf(@NonNull Priority priority) {
        return (new GlideOptions()).priority(priority);
    }

    @CheckResult
    @NonNull
    public static GlideOptions placeholderOf(@Nullable Drawable drawable) {
        return (new GlideOptions()).placeholder(drawable);
    }

    @CheckResult
    @NonNull
    public static GlideOptions placeholderOf(@DrawableRes int id) {
        return (new GlideOptions()).placeholder(id);
    }

    @CheckResult
    @NonNull
    public static GlideOptions errorOf(@Nullable Drawable drawable) {
        return (new GlideOptions()).error(drawable);
    }

    @CheckResult
    @NonNull
    public static GlideOptions errorOf(@DrawableRes int id) {
        return (new GlideOptions()).error(id);
    }

    @CheckResult
    @NonNull
    public static GlideOptions skipMemoryCacheOf(boolean skipMemoryCache) {
        return (new GlideOptions()).skipMemoryCache(skipMemoryCache);
    }

    @CheckResult
    @NonNull
    public static GlideOptions overrideOf(int width, int height) {
        return (new GlideOptions()).override(width, height);
    }

    @CheckResult
    @NonNull
    public static GlideOptions overrideOf(int size) {
        return (new GlideOptions()).override(size);
    }

    @CheckResult
    @NonNull
    public static GlideOptions signatureOf(@NonNull Key key) {
        return (new GlideOptions()).signature(key);
    }

    @CheckResult
    @NonNull
    public static GlideOptions fitCenterTransform() {
        if (fitCenterTransform0 == null) {
            fitCenterTransform0 = (new GlideOptions()).fitCenter().autoClone();
        }

        return fitCenterTransform0;
    }

    @CheckResult
    @NonNull
    public static GlideOptions centerInsideTransform() {
        if (centerInsideTransform1 == null) {
            centerInsideTransform1 = (new GlideOptions()).centerInside().autoClone();
        }

        return centerInsideTransform1;
    }

    @CheckResult
    @NonNull
    public static GlideOptions centerCropTransform() {
        if (centerCropTransform2 == null) {
            centerCropTransform2 = (new GlideOptions()).centerCrop().autoClone();
        }

        return centerCropTransform2;
    }

    @CheckResult
    @NonNull
    public static GlideOptions circleCropTransform() {
        if (circleCropTransform3 == null) {
            circleCropTransform3 = (new GlideOptions()).circleCrop().autoClone();
        }

        return circleCropTransform3;
    }

    @CheckResult
    @NonNull
    public static GlideOptions bitmapTransform(@NonNull Transformation<Bitmap> transformation) {
        return (new GlideOptions()).transform(transformation);
    }

    @CheckResult
    @NonNull
    public static GlideOptions noTransformation() {
        if (noTransformation4 == null) {
            noTransformation4 = (new GlideOptions()).dontTransform().autoClone();
        }

        return noTransformation4;
    }

    @CheckResult
    @NonNull
    public static <T> GlideOptions option(@NonNull Option<T> option, @NonNull T t) {
        return (new GlideOptions()).set(option, t);
    }

    @CheckResult
    @NonNull
    public static GlideOptions decodeTypeOf(@NonNull Class<?> clazz) {
        return (new GlideOptions()).decode(clazz);
    }

    @CheckResult
    @NonNull
    public static GlideOptions formatOf(@NonNull DecodeFormat format) {
        return (new GlideOptions()).format(format);
    }

    @CheckResult
    @NonNull
    public static GlideOptions frameOf(@IntRange(from = 0L) long value) {
        return (new GlideOptions()).frame(value);
    }

    @CheckResult
    @NonNull
    public static GlideOptions downsampleOf(@NonNull DownsampleStrategy strategy) {
        return (new GlideOptions()).downsample(strategy);
    }

    @CheckResult
    @NonNull
    public static GlideOptions timeoutOf(@IntRange(from = 0L) int value) {
        return (new GlideOptions()).timeout(value);
    }

    @CheckResult
    @NonNull
    public static GlideOptions encodeQualityOf(@IntRange(from = 0L,to = 100L) int value) {
        return (new GlideOptions()).encodeQuality(value);
    }

    @CheckResult
    @NonNull
    public static GlideOptions encodeFormatOf(@NonNull CompressFormat format) {
        return (new GlideOptions()).encodeFormat(format);
    }

    @CheckResult
    @NonNull
    public static GlideOptions noAnimation() {
        if (noAnimation5 == null) {
            noAnimation5 = (new GlideOptions()).dontAnimate().autoClone();
        }

        return noAnimation5;
    }

    @NonNull
    @CheckResult
    public GlideOptions sizeMultiplier(@FloatRange(from = 0.0D,to = 1.0D) float value) {
        return (GlideOptions)super.sizeMultiplier(value);
    }

    @NonNull
    @CheckResult
    public GlideOptions useUnlimitedSourceGeneratorsPool(boolean flag) {
        return (GlideOptions)super.useUnlimitedSourceGeneratorsPool(flag);
    }

    @NonNull
    @CheckResult
    public GlideOptions useAnimationPool(boolean flag) {
        return (GlideOptions)super.useAnimationPool(flag);
    }

    @NonNull
    @CheckResult
    public GlideOptions onlyRetrieveFromCache(boolean flag) {
        return (GlideOptions)super.onlyRetrieveFromCache(flag);
    }

    @NonNull
    @CheckResult
    public GlideOptions diskCacheStrategy(@NonNull DiskCacheStrategy strategy) {
        return (GlideOptions)super.diskCacheStrategy(strategy);
    }

    @NonNull
    @CheckResult
    public GlideOptions priority(@NonNull Priority priority) {
        return (GlideOptions)super.priority(priority);
    }

    @NonNull
    @CheckResult
    public GlideOptions placeholder(@Nullable Drawable drawable) {
        return (GlideOptions)super.placeholder(drawable);
    }

    @NonNull
    @CheckResult
    public GlideOptions placeholder(@DrawableRes int id) {
        return (GlideOptions)super.placeholder(id);
    }

    @NonNull
    @CheckResult
    public GlideOptions fallback(@Nullable Drawable drawable) {
        return (GlideOptions)super.fallback(drawable);
    }

    @NonNull
    @CheckResult
    public GlideOptions fallback(@DrawableRes int id) {
        return (GlideOptions)super.fallback(id);
    }

    @NonNull
    @CheckResult
    public GlideOptions error(@Nullable Drawable drawable) {
        return (GlideOptions)super.error(drawable);
    }

    @NonNull
    @CheckResult
    public GlideOptions error(@DrawableRes int id) {
        return (GlideOptions)super.error(id);
    }

    @NonNull
    @CheckResult
    public GlideOptions theme(@Nullable Theme theme) {
        return (GlideOptions)super.theme(theme);
    }

    @NonNull
    @CheckResult
    public GlideOptions skipMemoryCache(boolean skip) {
        return (GlideOptions)super.skipMemoryCache(skip);
    }

    @NonNull
    @CheckResult
    public GlideOptions override(int width, int height) {
        return (GlideOptions)super.override(width, height);
    }

    @NonNull
    @CheckResult
    public GlideOptions override(int size) {
        return (GlideOptions)super.override(size);
    }

    @NonNull
    @CheckResult
    public GlideOptions signature(@NonNull Key key) {
        return (GlideOptions)super.signature(key);
    }

    @CheckResult
    public GlideOptions clone() {
        return (GlideOptions)super.clone();
    }

    @NonNull
    @CheckResult
    public <Y> GlideOptions set(@NonNull Option<Y> option, @NonNull Y y) {
        return (GlideOptions)super.set(option, y);
    }

    @NonNull
    @CheckResult
    public GlideOptions decode(@NonNull Class<?> clazz) {
        return (GlideOptions)super.decode(clazz);
    }

    @NonNull
    @CheckResult
    public GlideOptions encodeFormat(@NonNull CompressFormat format) {
        return (GlideOptions)super.encodeFormat(format);
    }

    @NonNull
    @CheckResult
    public GlideOptions encodeQuality(@IntRange(from = 0L,to = 100L) int value) {
        return (GlideOptions)super.encodeQuality(value);
    }

    @NonNull
    @CheckResult
    public GlideOptions frame(@IntRange(from = 0L) long value) {
        return (GlideOptions)super.frame(value);
    }

    @NonNull
    @CheckResult
    public GlideOptions format(@NonNull DecodeFormat format) {
        return (GlideOptions)super.format(format);
    }

    @NonNull
    @CheckResult
    public GlideOptions disallowHardwareConfig() {
        return (GlideOptions)super.disallowHardwareConfig();
    }

    @NonNull
    @CheckResult
    public GlideOptions downsample(@NonNull DownsampleStrategy strategy) {
        return (GlideOptions)super.downsample(strategy);
    }

    @NonNull
    @CheckResult
    public GlideOptions timeout(@IntRange(from = 0L) int value) {
        return (GlideOptions)super.timeout(value);
    }

    @NonNull
    @CheckResult
    public GlideOptions optionalCenterCrop() {
        return (GlideOptions)super.optionalCenterCrop();
    }

    @NonNull
    @CheckResult
    public GlideOptions centerCrop() {
        return (GlideOptions)super.centerCrop();
    }

    @NonNull
    @CheckResult
    public GlideOptions optionalFitCenter() {
        return (GlideOptions)super.optionalFitCenter();
    }

    @NonNull
    @CheckResult
    public GlideOptions fitCenter() {
        return (GlideOptions)super.fitCenter();
    }

    @NonNull
    @CheckResult
    public GlideOptions optionalCenterInside() {
        return (GlideOptions)super.optionalCenterInside();
    }

    @NonNull
    @CheckResult
    public GlideOptions centerInside() {
        return (GlideOptions)super.centerInside();
    }

    @NonNull
    @CheckResult
    public GlideOptions optionalCircleCrop() {
        return (GlideOptions)super.optionalCircleCrop();
    }

    @NonNull
    @CheckResult
    public GlideOptions circleCrop() {
        return (GlideOptions)super.circleCrop();
    }

    @NonNull
    @CheckResult
    public GlideOptions transform(@NonNull Transformation<Bitmap> transformation) {
        return (GlideOptions)super.transform(transformation);
    }

    @SafeVarargs
    @NonNull
    @CheckResult
    public final GlideOptions transform(@NonNull Transformation<Bitmap>... transformations) {
        return (GlideOptions)super.transform(transformations);
    }

    @SafeVarargs
    @Deprecated
    @NonNull
    @CheckResult
    public final GlideOptions transforms(@NonNull Transformation<Bitmap>... transformations) {
        return (GlideOptions)super.transforms(transformations);
    }

    @NonNull
    @CheckResult
    public GlideOptions optionalTransform(@NonNull Transformation<Bitmap> transformation) {
        return (GlideOptions)super.optionalTransform(transformation);
    }

    @NonNull
    @CheckResult
    public <Y> GlideOptions optionalTransform(@NonNull Class<Y> clazz, @NonNull Transformation<Y> transformation) {
        return (GlideOptions)super.optionalTransform(clazz, transformation);
    }

    @NonNull
    @CheckResult
    public <Y> GlideOptions transform(@NonNull Class<Y> clazz, @NonNull Transformation<Y> transformation) {
        return (GlideOptions)super.transform(clazz, transformation);
    }

    @NonNull
    @CheckResult
    public GlideOptions dontTransform() {
        return (GlideOptions)super.dontTransform();
    }

    @NonNull
    @CheckResult
    public GlideOptions dontAnimate() {
        return (GlideOptions)super.dontAnimate();
    }

    @NonNull
    @CheckResult
    public GlideOptions apply(@NonNull BaseRequestOptions<?> options) {
        return (GlideOptions)super.apply(options);
    }

    @NonNull
    public GlideOptions lock() {
        return (GlideOptions)super.lock();
    }

    @NonNull
    public GlideOptions autoClone() {
        return (GlideOptions)super.autoClone();
    }
}
