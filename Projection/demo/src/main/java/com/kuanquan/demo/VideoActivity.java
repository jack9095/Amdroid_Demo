package com.kuanquan.demo;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.devbrackets.android.exomedia.ui.widget.VideoControls;
import com.devbrackets.android.exomedia.ui.widget.VideoControlsCore;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.kuanquan.demo.base.BasePlayActivity;

/**
 * Created by huzongyao on 2018/6/29.
 * To play video media
 */
public class VideoActivity extends BasePlayActivity {

    VideoView mVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mVideoView = findViewById(R.id.video_view);
        initVideoPlayer();
        setCurrentMediaAndPlay();
    }

    private void initVideoPlayer() {
        mVideoView.setHandleAudioFocus(false);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnBufferUpdateListener(this);
    }

    @Override
    protected void setCurrentMediaAndPlay() {
        if (mMediaInfo != null) {
            VideoControlsCore videoControls = mVideoView.getVideoControlsCore();
            if (videoControls instanceof VideoControls) {
                ((VideoControls) videoControls).setTitle(mMediaInfo.title);
            }
            Uri uri = Uri.parse(mMediaInfo.url);
            Log.e("VideoActivity", "uri ->" + uri);
            mVideoView.setVideoURI(uri);
        }
    }

    @Override
    protected void onMediaPause() {
        if(mVideoView.isPlaying()){
            mVideoView.pause();
        }
    }

    @Override
    public void onPrepared() {
        if (!mVideoView.isPlaying()) {
            mVideoView.start();
        }
    }

    @Override
    public void onCompletion() {
        mVideoView.seekTo(0);
    }
}
