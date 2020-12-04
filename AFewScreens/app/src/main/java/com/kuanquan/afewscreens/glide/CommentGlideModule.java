package com.kuanquan.afewscreens.glide;

import android.content.Context;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import com.bumptech.glide.load.model.GlideUrl;

import java.io.InputStream;


@GlideModule
public class CommentGlideModule extends AppGlideModule {

    // 图片缓存最大容量，150M，根据自己的需求进行修改
    private static final int GLIDE_CATCH_SIZE = 500 * 1000 * 1000;

    // 图片缓存子目录，可随意设置
    private static final String GLIDE_CACHE_DIR = "image-cache";

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {

        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context
                , GLIDE_CACHE_DIR
                , GLIDE_CATCH_SIZE));
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.replace(GlideUrl.class, InputStream.class, new HttpUrlLoader.Factory());
    }
}
