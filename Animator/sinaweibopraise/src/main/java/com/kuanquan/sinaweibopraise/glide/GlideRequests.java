package com.kuanquan.sinaweibopraise.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.CheckResult;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.net.URL;

public class GlideRequests extends RequestManager {
    public GlideRequests(@NonNull Glide glide, @NonNull Lifecycle lifecycle, @NonNull RequestManagerTreeNode treeNode, @NonNull Context context) {
        super(glide, lifecycle, treeNode, context);
    }

    @CheckResult
    @NonNull
    public <ResourceType> GlideRequest<ResourceType> as(@NonNull Class<ResourceType> resourceClass) {
        return new GlideRequest(this.glide, this, resourceClass, this.context);
    }

    @NonNull
    public synchronized GlideRequests applyDefaultRequestOptions(@NonNull RequestOptions options) {
        return (GlideRequests)super.applyDefaultRequestOptions(options);
    }

    @NonNull
    public synchronized GlideRequests setDefaultRequestOptions(@NonNull RequestOptions options) {
        return (GlideRequests)super.setDefaultRequestOptions(options);
    }

    @NonNull
    public GlideRequests addDefaultRequestListener(RequestListener<Object> listener) {
        return (GlideRequests)super.addDefaultRequestListener(listener);
    }

    @NonNull
    @CheckResult
    public GlideRequest<Bitmap> asBitmap() {
        return (GlideRequest)super.asBitmap();
    }

    @NonNull
    @CheckResult
    public GlideRequest<GifDrawable> asGif() {
        return (GlideRequest)super.asGif();
    }

    @NonNull
    @CheckResult
    public GlideRequest<Drawable> asDrawable() {
        return (GlideRequest)super.asDrawable();
    }

    @NonNull
    @CheckResult
    public GlideRequest<Drawable> load(@Nullable Bitmap bitmap) {
        return (GlideRequest)super.load(bitmap);
    }

    @NonNull
    @CheckResult
    public GlideRequest<Drawable> load(@Nullable Drawable drawable) {
        return (GlideRequest)super.load(drawable);
    }

    @NonNull
    @CheckResult
    public GlideRequest<Drawable> load(@Nullable String string) {
        return (GlideRequest)super.load(string);
    }

    @NonNull
    @CheckResult
    public GlideRequest<Drawable> load(@Nullable Uri uri) {
        return (GlideRequest)super.load(uri);
    }

    @NonNull
    @CheckResult
    public GlideRequest<Drawable> load(@Nullable File file) {
        return (GlideRequest)super.load(file);
    }

    @NonNull
    @CheckResult
    public GlideRequest<Drawable> load(@RawRes @DrawableRes @Nullable Integer id) {
        return (GlideRequest)super.load(id);
    }

    @Deprecated
    @CheckResult
    public GlideRequest<Drawable> load(@Nullable URL url) {
        return (GlideRequest)super.load(url);
    }

    @NonNull
    @CheckResult
    public GlideRequest<Drawable> load(@Nullable byte[] bytes) {
        return (GlideRequest)super.load(bytes);
    }

    @NonNull
    @CheckResult
    public GlideRequest<Drawable> load(@Nullable Object o) {
        return (GlideRequest)super.load(o);
    }

    @NonNull
    @CheckResult
    public GlideRequest<File> downloadOnly() {
        return (GlideRequest)super.downloadOnly();
    }

    @NonNull
    @CheckResult
    public GlideRequest<File> download(@Nullable Object o) {
        return (GlideRequest)super.download(o);
    }

    @NonNull
    @CheckResult
    public GlideRequest<File> asFile() {
        return (GlideRequest)super.asFile();
    }

    protected void setRequestOptions(@NonNull RequestOptions toSet) {
        if (toSet instanceof GlideOptions) {
            super.setRequestOptions(toSet);
        } else {
            super.setRequestOptions((new GlideOptions()).apply(toSet));
        }

    }
}
