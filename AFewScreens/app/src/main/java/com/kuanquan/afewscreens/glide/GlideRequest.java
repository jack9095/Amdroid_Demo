package com.kuanquan.afewscreens.glide;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.CheckResult;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import java.io.File;
import java.net.URL;

public class GlideRequest<TranscodeType> extends RequestBuilder<TranscodeType> implements Cloneable {
    GlideRequest(@NonNull Class<TranscodeType> transcodeClass, @NonNull RequestBuilder<?> other) {
        super(transcodeClass, other);
    }

    GlideRequest(@NonNull Glide glide, @NonNull RequestManager requestManager, @NonNull Class<TranscodeType> transcodeClass, @NonNull Context context) {
        super(glide, requestManager, transcodeClass, context);
    }

    @CheckResult
    @NonNull
    protected GlideRequest<File> getDownloadOnlyRequest() {
        return (new GlideRequest(File.class, this)).apply(DOWNLOAD_ONLY_OPTIONS);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> sizeMultiplier(@FloatRange(from = 0.0D,to = 1.0D) float value) {
        return (GlideRequest)super.sizeMultiplier(value);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> useUnlimitedSourceGeneratorsPool(boolean flag) {
        return (GlideRequest)super.useUnlimitedSourceGeneratorsPool(flag);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> useAnimationPool(boolean flag) {
        return (GlideRequest)super.useAnimationPool(flag);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> onlyRetrieveFromCache(boolean flag) {
        return (GlideRequest)super.onlyRetrieveFromCache(flag);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> diskCacheStrategy(@NonNull DiskCacheStrategy strategy) {
        return (GlideRequest)super.diskCacheStrategy(strategy);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> priority(@NonNull Priority priority) {
        return (GlideRequest)super.priority(priority);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> placeholder(@Nullable Drawable drawable) {
        return (GlideRequest)super.placeholder(drawable);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> placeholder(@DrawableRes int id) {
        return (GlideRequest)super.placeholder(id);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> fallback(@Nullable Drawable drawable) {
        return (GlideRequest)super.fallback(drawable);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> fallback(@DrawableRes int id) {
        return (GlideRequest)super.fallback(id);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> error(@Nullable Drawable drawable) {
        return (GlideRequest)super.error(drawable);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> error(@DrawableRes int id) {
        return (GlideRequest)super.error(id);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> theme(@Nullable Theme theme) {
        return (GlideRequest)super.theme(theme);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> skipMemoryCache(boolean skip) {
        return (GlideRequest)super.skipMemoryCache(skip);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> override(int width, int height) {
        return (GlideRequest)super.override(width, height);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> override(int size) {
        return (GlideRequest)super.override(size);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> signature(@NonNull Key key) {
        return (GlideRequest)super.signature(key);
    }

    @NonNull
    @CheckResult
    public <Y> GlideRequest<TranscodeType> set(@NonNull Option<Y> option, @NonNull Y y) {
        return (GlideRequest)super.set(option, y);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> decode(@NonNull Class<?> clazz) {
        return (GlideRequest)super.decode(clazz);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> encodeFormat(@NonNull CompressFormat format) {
        return (GlideRequest)super.encodeFormat(format);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> encodeQuality(@IntRange(from = 0L,to = 100L) int value) {
        return (GlideRequest)super.encodeQuality(value);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> frame(@IntRange(from = 0L) long value) {
        return (GlideRequest)super.frame(value);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> format(@NonNull DecodeFormat format) {
        return (GlideRequest)super.format(format);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> disallowHardwareConfig() {
        return (GlideRequest)super.disallowHardwareConfig();
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> downsample(@NonNull DownsampleStrategy strategy) {
        return (GlideRequest)super.downsample(strategy);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> timeout(@IntRange(from = 0L) int value) {
        return (GlideRequest)super.timeout(value);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> optionalCenterCrop() {
        return (GlideRequest)super.optionalCenterCrop();
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> centerCrop() {
        return (GlideRequest)super.centerCrop();
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> optionalFitCenter() {
        return (GlideRequest)super.optionalFitCenter();
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> fitCenter() {
        return (GlideRequest)super.fitCenter();
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> optionalCenterInside() {
        return (GlideRequest)super.optionalCenterInside();
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> centerInside() {
        return (GlideRequest)super.centerInside();
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> optionalCircleCrop() {
        return (GlideRequest)super.optionalCircleCrop();
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> circleCrop() {
        return (GlideRequest)super.circleCrop();
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> transform(@NonNull Transformation<Bitmap> transformation) {
        return (GlideRequest)super.transform(transformation);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> transform(@NonNull Transformation<Bitmap>... transformations) {
        return (GlideRequest)super.transform(transformations);
    }

    /** @deprecated */
    @Deprecated
    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> transforms(@NonNull Transformation<Bitmap>... transformations) {
        return (GlideRequest)super.transforms(transformations);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> optionalTransform(@NonNull Transformation<Bitmap> transformation) {
        return (GlideRequest)super.optionalTransform(transformation);
    }

    @NonNull
    @CheckResult
    public <Y> GlideRequest<TranscodeType> optionalTransform(@NonNull Class<Y> clazz, @NonNull Transformation<Y> transformation) {
        return (GlideRequest)super.optionalTransform(clazz, transformation);
    }

    @NonNull
    @CheckResult
    public <Y> GlideRequest<TranscodeType> transform(@NonNull Class<Y> clazz, @NonNull Transformation<Y> transformation) {
        return (GlideRequest)super.transform(clazz, transformation);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> dontTransform() {
        return (GlideRequest)super.dontTransform();
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> dontAnimate() {
        return (GlideRequest)super.dontAnimate();
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> apply(@NonNull BaseRequestOptions<?> options) {
        return (GlideRequest)super.apply(options);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> transition(@NonNull TransitionOptions<?, ? super TranscodeType> options) {
        return (GlideRequest)super.transition(options);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> listener(@Nullable RequestListener<TranscodeType> listener) {
        return (GlideRequest)super.listener(listener);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> addListener(@Nullable RequestListener<TranscodeType> listener) {
        return (GlideRequest)super.addListener(listener);
    }

    @NonNull
    public GlideRequest<TranscodeType> error(@Nullable RequestBuilder<TranscodeType> builder) {
        return (GlideRequest)super.error(builder);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> thumbnail(@Nullable RequestBuilder<TranscodeType> builder) {
        return (GlideRequest)super.thumbnail(builder);
    }

    @SafeVarargs
    @NonNull
    @CheckResult
    public final GlideRequest<TranscodeType> thumbnail(@Nullable RequestBuilder<TranscodeType>... builders) {
        return (GlideRequest)super.thumbnail(builders);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> thumbnail(float sizeMultiplier) {
        return (GlideRequest)super.thumbnail(sizeMultiplier);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable Object o) {
        return (GlideRequest)super.load(o);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable Bitmap bitmap) {
        return (GlideRequest)super.load(bitmap);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable Drawable drawable) {
        return (GlideRequest)super.load(drawable);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable String string) {
        return (GlideRequest)super.load(string);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable Uri uri) {
        return (GlideRequest)super.load(uri);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable File file) {
        return (GlideRequest)super.load(file);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@RawRes @DrawableRes @Nullable Integer id) {
        return (GlideRequest)super.load(id);
    }

    /** @deprecated */
    @Deprecated
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable URL url) {
        return (GlideRequest)super.load(url);
    }

    @NonNull
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable byte[] bytes) {
        return (GlideRequest)super.load(bytes);
    }

    @CheckResult
    public GlideRequest<TranscodeType> clone() {
        return (GlideRequest)super.clone();
    }
}
