package com.kuanquan.videocover.entity;

import android.content.pm.ActivityInfo;
import androidx.annotation.ColorInt;
import com.kuanquan.videocover.core.PictureMimeType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author：luck
 * @date：2017-05-24 17:02
 * @describe：PictureSelector Config
 */

public final class PictureSelectionConfig implements Serializable {
    public int chooseMode;
    public boolean camera;
    public boolean isSingleDirectReturn;
    public PictureParameterStyle style;
//    public PictureCropParameterStyle cropStyle;
//    public PictureWindowAnimationStyle windowAnimationStyle;
//    public InstagramSelectionConfig instagramSelectionConfig;
    public String compressSavePath;
    public String suffixType;
    public boolean focusAlpha;
    public String renameCompressFileName;
    public String renameCropFileName;
    public String specifiedFormat;
    public int requestedOrientation;
    public boolean isCameraAroundState;
    public boolean isAndroidQTransform;
    public int selectionMode;
    public int maxSelectNum;
    public int minSelectNum;
    public int maxVideoSelectNum;
    public int minVideoSelectNum;
    public int videoQuality;
    public int cropCompressQuality;
    public int videoMaxSecond;
    public int videoMinSecond;
    public int recordVideoSecond;
    public int recordVideoMinSecond;
    public int minimumCompressSize;
    public int imageSpanCount;
    public int aspect_ratio_x;
    public int aspect_ratio_y;
    public int cropWidth;
    public int cropHeight;
    public int compressQuality;
    public float filterFileSize;
    public int language;
    public boolean isMultipleRecyclerAnimation;
    public boolean isMultipleSkipCrop;
    public boolean isWeChatStyle;
    public boolean isUseCustomCamera;
    public boolean zoomAnim;
    public boolean isCompress;
    public boolean isOriginalControl;
    public boolean isCamera;
    public boolean isGif;
    public boolean enablePreview;
    public boolean enPreviewVideo;
    public boolean enablePreviewAudio;
    public boolean checkNumMode;
    public boolean openClickSound;
    public boolean enableCrop;
    public boolean freeStyleCropEnabled;
    public boolean circleDimmedLayer;
    @ColorInt
    public int circleDimmedColor;
    @ColorInt
    public int circleDimmedBorderColor;
    public int circleStrokeWidth;
    public boolean showCropFrame;
    public boolean showCropGrid;
    public boolean hideBottomControls;
    public boolean rotateEnabled;
    public boolean scaleEnabled;
    public boolean previewEggs;
    public boolean synOrAsy;
    public boolean returnEmpty;
    public boolean isDragFrame;
    public boolean isNotPreviewDownload;
    public boolean isWithVideoImage;
//    public UCropOptions uCropOptions;
//    public static ImageEngine imageEngine;
//    public static CacheResourcesEngine cacheResourcesEngine;
//    public static OnResultCallbackListener listener;
//    public static OnVideoSelectedPlayCallback customVideoPlayCallback;
//    public static OnCustomCameraInterfaceListener onCustomCameraInterfaceListener;
    public List<LocalMedia> selectionMedias;
    public String cameraFileName;
    public boolean isCheckOriginalImage;
    @Deprecated
    public int overrideWidth;
    @Deprecated
    public int overrideHeight;
    @Deprecated
    public float sizeMultiplier;
    @Deprecated
    public boolean isChangeStatusBarFontColor;
    @Deprecated
    public boolean isOpenStyleNumComplete;
    @Deprecated
    public boolean isOpenStyleCheckNumMode;
    @Deprecated
    public int titleBarBackgroundColor;
    @Deprecated
    public int pictureStatusBarColor;
    @Deprecated
    public int cropTitleBarBackgroundColor;
    @Deprecated
    public int cropStatusBarColorPrimaryDark;
    @Deprecated
    public int cropTitleColor;
    @Deprecated
    public int upResId;
    @Deprecated
    public int downResId;
    public String outPutCameraPath;

    public String originalPath;
    public String cameraPath;
    public int cameraMimeType;
    public int pageSize;
    public boolean isPageStrategy;
    public boolean isFilterInvalidFile;
    public boolean isMaxSelectEnabledMask;
    public int animationMode;
    public boolean isAutomaticTitleRecyclerTop;
    public boolean isCallbackMode;
    public boolean isAndroidQChangeWH;
    public boolean isAndroidQChangeVideoWH;
    public boolean isQuickCapture;
    /**
     * 内测专用###########
     */
    public boolean isFallbackVersion;
    public boolean isFallbackVersion2;
    public boolean isFallbackVersion3;

    public static PictureSelectionConfig getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static final class InstanceHolder {
        private static final PictureSelectionConfig INSTANCE = new PictureSelectionConfig();
    }

    public PictureSelectionConfig() {
    }

}
