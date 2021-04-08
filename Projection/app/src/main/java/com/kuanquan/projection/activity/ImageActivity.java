package com.kuanquan.projection.activity;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kuanquan.projection.R;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by huzongyao on 2018/7/2.
 * To play image media
 */

public class ImageActivity extends BasePlayActivity {

    PhotoView mPhotoView;
    ProgressBar mProgressBar;

    private RequestManager mGlide;
    private PhotoViewTarget mPhotoViewTarget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mPhotoView = findViewById(R.id.photo_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mGlide = Glide.with(this);
        mPhotoViewTarget = new PhotoViewTarget(mPhotoView);
        setCurrentMediaAndPlay();
    }

    @Override
    void setCurrentMediaAndPlay() {
        if (mMediaInfo != null) {
            Uri uri = Uri.parse(mMediaInfo.url);
            mGlide.load(uri).into(mPhotoViewTarget);
        }
    }

    private class PhotoViewTarget extends DrawableImageViewTarget {
        PhotoViewTarget(ImageView view) {
            super(view);
        }

        @Override
        public void onLoadStarted(@Nullable Drawable placeholder) {
            super.onLoadStarted(placeholder);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onResourceReady(@NonNull Drawable resource,
                                    @Nullable Transition<? super Drawable> transition) {
            super.onResourceReady(resource, transition);
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
