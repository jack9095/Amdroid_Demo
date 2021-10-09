package com.kuanquan.videocover.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;

import com.kuanquan.videocover.core.LifecycleCallBack;
import com.kuanquan.videocover.core.PictureMimeType;
import com.kuanquan.videocover.core.PictureSelector;
import com.kuanquan.videocover.core.ProcessStateCallBack;
import com.kuanquan.videocover.entity.LocalMedia;
import com.kuanquan.videocover.entity.PictureConfig;
import com.kuanquan.videocover.entity.PictureSelectionConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * Created by JessYan on 2020/5/29 11:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class InstagramMediaProcessActivity extends PictureBaseActivity {
    public static final String EXTRA_ASPECT_RATIO = "extra_aspect_ratio";
    public static final String EXTRA_SINGLE_IMAGE_FILTER = "extra_single_image_filter";
    public static final String EXTRA_SINGLE_IMAGE_SELECTION_FILTER = "extra_single_image_selection_filter";
    public static final int REQUEST_SINGLE_IMAGE_PROCESS = 339;
    public static final int REQUEST_MULTI_IMAGE_PROCESS = 440;
    public static final int REQUEST_SINGLE_VIDEO_PROCESS = 441;
    public static final int RESULT_MEDIA_PROCESS_CANCELED = 501;
    private List<LocalMedia> mSelectMedia;
    private MediaType mMediaType;
    private boolean isAspectRatio;

    public enum MediaType {
        SINGLE_IMAGE, SINGLE_VIDEO, MULTI_IMAGE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSelectMedia = PictureSelector.obtainSelectorList(savedInstanceState);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (container != null && ((ViewGroup)container).getChildAt(0) instanceof LifecycleCallBack) {
            ((LifecycleCallBack)((ViewGroup)container).getChildAt(0)).onStart(InstagramMediaProcessActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (container != null && ((ViewGroup)container).getChildAt(0) instanceof LifecycleCallBack) {
            ((LifecycleCallBack)((ViewGroup)container).getChildAt(0)).onResume(InstagramMediaProcessActivity.this);
        }
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
        if (container != null && ((ViewGroup)container).getChildAt(0) instanceof LifecycleCallBack) {
            ((LifecycleCallBack)((ViewGroup)container).getChildAt(0)).onPause(InstagramMediaProcessActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (container != null && ((ViewGroup)container).getChildAt(0) instanceof LifecycleCallBack) {
            ((LifecycleCallBack)((ViewGroup)container).getChildAt(0)).onDestroy(InstagramMediaProcessActivity.this);
        }
    }

    @Override
    protected void initWidgets() {
        if (mSelectMedia == null && getIntent() != null) {
            mSelectMedia = getIntent().getParcelableArrayListExtra(PictureConfig.EXTRA_SELECT_LIST);
        }

        if (mSelectMedia == null || mSelectMedia.isEmpty()) {
            finish();
        }

        FrameLayout contentView = new FrameLayout(this) {

            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int width = MeasureSpec.getSize(widthMeasureSpec);
                int height = MeasureSpec.getSize(heightMeasureSpec);
                getChildAt(0).measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                setMeasuredDimension(width, height);
            }

            @Override
            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
                View child = getChildAt(0);
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        };
        container = contentView;
        setContentView(contentView);

        if (PictureMimeType.isHasVideo(mSelectMedia.get(0).getMimeType())) {
            mMediaType = MediaType.SINGLE_VIDEO;
            createSingleVideoContainer(contentView, mSelectMedia);
        } else if (mSelectMedia.size() > 1) {
            mMediaType = MediaType.MULTI_IMAGE;
            createMultiImageContainer(contentView, mSelectMedia);
        } else {
            mMediaType = MediaType.SINGLE_IMAGE;
            createSingleImageContainer(contentView);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSelectMedia.size() > 0) {
            PictureSelector.saveSelectorList(outState, mSelectMedia);
        }
    }

    @Override
    public void onBackPressed() {
        if (container != null && ((ViewGroup)container).getChildAt(0) instanceof ProcessStateCallBack) {
            ((ProcessStateCallBack)((ViewGroup)container).getChildAt(0)).onBack(InstagramMediaProcessActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (container != null && ((ViewGroup)container).getChildAt(0) instanceof ProcessStateCallBack) {
            ((ProcessStateCallBack)((ViewGroup)container).getChildAt(0)).onActivityResult(InstagramMediaProcessActivity.this, requestCode, resultCode, data);
        }
    }

    private void createMultiImageContainer(FrameLayout contentView, List<LocalMedia> selectMedia) {
        if (getIntent() != null) {
            isAspectRatio = getIntent().getBooleanExtra(EXTRA_ASPECT_RATIO, false);
        }
        InstagramMediaMultiImageContainer multiImageContainer = new InstagramMediaMultiImageContainer(this, config, selectMedia, isAspectRatio);
        contentView.addView(multiImageContainer, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    private void createSingleVideoContainer(FrameLayout contentView, List<LocalMedia> selectMedia) {
        if (getIntent() != null) {
            isAspectRatio = getIntent().getBooleanExtra(EXTRA_ASPECT_RATIO, false);
        }
        InstagramMediaSingleVideoContainer singleVideoContainer = new InstagramMediaSingleVideoContainer(this, config, selectMedia.get(0), isAspectRatio);
        contentView.addView(singleVideoContainer, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    private void createSingleImageContainer(FrameLayout contentView) {
        int selectionFilter = -1;
        if (getIntent() != null) {
            isAspectRatio = getIntent().getBooleanExtra(EXTRA_ASPECT_RATIO, false);
            selectionFilter = getIntent().getIntExtra(EXTRA_SINGLE_IMAGE_FILTER, -1);
        }

        try {
            Uri uri;
            LocalMedia media = mSelectMedia.get(0);
            if (media.isCut()) {
                uri = Uri.fromFile(new File(media.getCutPath()));
            } else {
                uri = PictureMimeType.isContent(media.getPath()) ? Uri.parse(media.getPath()) : Uri.fromFile(new File(media.getPath()));
            }

//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//            InstagramMediaSingleImageContainer singleImageContainer = new InstagramMediaSingleImageContainer(this, config, bitmap, isAspectRatio, selectionFilter);
//            contentView.addView(singleImageContainer, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void launchActivity(Activity activity, PictureSelectionConfig config, List<LocalMedia> images, Bundle extras, int requestCode) {
        Intent intent = new Intent(activity.getApplicationContext(), InstagramMediaProcessActivity.class);
        intent.putExtra(PictureConfig.EXTRA_CONFIG, config);
        intent.putParcelableArrayListExtra(PictureConfig.EXTRA_SELECT_LIST,
                (ArrayList<? extends Parcelable>) images);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(0, 0);
    }

    public void showLoadingView(boolean isShow) {
        if (isShow) {
            showPleaseDialog();
        } else {
            dismissDialog();
        }
    }
}
