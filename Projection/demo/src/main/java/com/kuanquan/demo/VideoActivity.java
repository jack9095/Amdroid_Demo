package com.kuanquan.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoControls;
import com.devbrackets.android.exomedia.ui.widget.VideoControlsCore;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

/**
 * To play video media
 * TODO 5
 */
public class VideoActivity extends AppCompatActivity implements OnPreparedListener, OnCompletionListener,
        OnBufferUpdateListener {

    private VideoView mVideoView;
    private MediaInfo mMediaInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mVideoView = findViewById(R.id.video_view);
        initVideoPlayer();
        parseIntent(getIntent());
        setCurrentMediaAndPlay();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);
        setCurrentMediaAndPlay();
    }

    private void parseIntent(Intent intent) {
        mMediaInfo = intent.getParcelableExtra("EXTRA_MEDIA_INFO");
    }

    private void initVideoPlayer() {
        mVideoView.setHandleAudioFocus(false);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnBufferUpdateListener(this);
    }

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

    @Override
    public void onBufferingUpdate(int percent) {

    }
}
