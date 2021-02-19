package com.bumptech.glide;

import android.content.Context;

import androidx.annotation.NonNull;


import com.kuanquan.sinaweibopraise.glide.CommentGlideModule;

import java.util.Collections;
import java.util.Set;

final class GeneratedAppGlideModuleImpl extends GeneratedAppGlideModule {
    private final CommentGlideModule appGlideModule = new CommentGlideModule();

    public GeneratedAppGlideModuleImpl(Context context) {
    }

    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        this.appGlideModule.applyOptions(context, builder);
    }

    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        this.appGlideModule.registerComponents(context, glide, registry);
    }

    public boolean isManifestParsingEnabled() {
        return this.appGlideModule.isManifestParsingEnabled();
    }

    @NonNull
    public Set<Class<?>> getExcludedModuleClasses() {
        return Collections.emptySet();
    }

    @NonNull
    GeneratedRequestManagerFactory getRequestManagerFactory() {
        return new GeneratedRequestManagerFactory();
    }
}
