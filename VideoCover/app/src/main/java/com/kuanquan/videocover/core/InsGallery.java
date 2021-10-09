package com.kuanquan.videocover.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.kuanquan.videocover.callback.OnResultCallbackListener;
import com.kuanquan.videocover.engine.CacheResourcesEngine;
import com.kuanquan.videocover.engine.ImageEngine;
import com.kuanquan.videocover.entity.LocalMedia;
import com.kuanquan.videocover.entity.PictureConfig;

import java.util.List;

/**
 * ================================================
 * 此类只是作为 InsGallery 的快捷入口, 帮助开发者快速上手,
 * 如果你需要更多的自定义功能还是需要调用 PictureSelector 的接口传入需要自定义的参数,
 * <p>
 * 你可以理解为 InsGallery 就是依赖于 PictureSelector 创建的一套皮肤, 所以我不会大改他的代码, 尽量沿用他本身的设计
 * 依赖于强大的开源社区后, InsGallery 就可以在保证稳定性的情况下, 拥有更多的精力去实现 UI 效果, 尽量还原一个产品级的 Instagram Gallery
 * InsGallery 使用纯编码, 0 xml 的方式构建, 所有父容器都是使用 FrameLayout 自定义完成, 在保证效率的同时，拥有高度的灵活性。
 * <p>
 * Created by JessYan on 2020/4/27 18:07
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public final class InsGallery {
    public static final int THEME_STYLE_DEFAULT = 0;
    public static final int THEME_STYLE_DARK = 1;
    public static final int THEME_STYLE_DARK_BLUE = 2;
    public static int currentTheme = THEME_STYLE_DEFAULT;

    private InsGallery() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public static void openGallery(Activity activity, ImageEngine engine, OnResultCallbackListener listener) {
        openGallery(activity, engine, null, null, listener);
    }

    public static void openGallery(Activity activity, ImageEngine engine, CacheResourcesEngine cacheResourcesEngine, OnResultCallbackListener listener) {
        openGallery(activity, engine, cacheResourcesEngine, null, listener);
    }

    public static void openGallery(Activity activity, ImageEngine engine, CacheResourcesEngine cacheResourcesEngine, List<LocalMedia> selectionMedia, OnResultCallbackListener listener) {
        applyInstagramOptions(activity.getApplicationContext(), PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofAll()))// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(engine)// 外部传入图片加载引擎，必传项
                .loadCacheResourcesCallback(cacheResourcesEngine)// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                .selectionData(selectionMedia)// 是否传入已选图片
                .forResult(listener);
    }

    public static void openGallery(Activity activity, ImageEngine engine, CacheResourcesEngine cacheResourcesEngine, List<LocalMedia> selectionMedia, InstagramSelectionConfig instagramConfig, OnResultCallbackListener listener) {
        applyInstagramOptions(activity.getApplicationContext(), instagramConfig, PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofAll()))// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(engine)// 外部传入图片加载引擎，必传项
                .loadCacheResourcesCallback(cacheResourcesEngine)// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                .selectionData(selectionMedia)// 是否传入已选图片
                .forResult(listener);
    }

    public static void openGallery(Activity activity, ImageEngine engine, CacheResourcesEngine cacheResourcesEngine, List<LocalMedia> selectionMedia) {
        applyInstagramOptions(activity.getApplicationContext(), PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofAll()))// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(engine)// 外部传入图片加载引擎，必传项
                .loadCacheResourcesCallback(cacheResourcesEngine)// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                .selectionData(selectionMedia)// 是否传入已选图片
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    public static void openGallery(Activity activity, ImageEngine engine, CacheResourcesEngine cacheResourcesEngine, List<LocalMedia> selectionMedia, InstagramSelectionConfig instagramConfig) {
        applyInstagramOptions(activity.getApplicationContext(), instagramConfig, PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofAll()))// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(engine)// 外部传入图片加载引擎，必传项
                .loadCacheResourcesCallback(cacheResourcesEngine)// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                .selectionData(selectionMedia)// 是否传入已选图片
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    public static void openGallery(Activity activity, ImageEngine engine, CacheResourcesEngine cacheResourcesEngine, List<LocalMedia> selectionMedia, int requestCode) {
        applyInstagramOptions(activity.getApplicationContext(), PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofAll()))// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(engine)// 外部传入图片加载引擎，必传项
                .loadCacheResourcesCallback(cacheResourcesEngine)// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                .selectionData(selectionMedia)// 是否传入已选图片
                .forResult(requestCode);//结果回调onActivityResult code
    }


    public static PictureSelectionModel applyInstagramOptions(Context context, PictureSelectionModel selectionModel) {
        return applyInstagramOptions(context, InstagramSelectionConfig.createConfig().setCurrentTheme(currentTheme), selectionModel);
    }

    public static void setCurrentTheme(int currentTheme) {
        InsGallery.currentTheme = currentTheme;
    }
}
