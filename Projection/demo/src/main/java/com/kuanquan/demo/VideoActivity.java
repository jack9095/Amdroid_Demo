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
            String str = "http://upos-sz-mirrorkodo.bilivideo.com/upgcxcode/46/57/241775746/241775746_nb2-1-64.flv?e=ig8euxZM2rNcNbRzhWdVhwdlhWh1hwdVhoNvNC8BqJIzNbfqXBvEuENvNC8aNEVEtEvE9IMvXBvE2ENvNCImNEVEIj0Y2J_aug859r1qXg8gNEVE5XREto8z5JZC2X2gkX5L5F1eTX1jkXlsTXHeux_f2o859IB_&ua=tvproj&uipk=5&nbs=1&deadline=1622122624&gen=playurlv2&os=kodobv&oi=1039254948&trid=4cc35a5448564a15ad2eed83266162c5T&upsig=dad2ab2a5b196b3d4e63c6557cbb1ed8&uparams=e,ua,uipk,nbs,deadline,gen,os,oi,trid&mid=602485009&orderid=0,3&logo=80000000&nva_ext=%7B%22ver%22%3A2%2C%22content%22%3A%7B%22sessionId%22%3A%2277780bee-d509-4237-b625-d3dbc4e6ec13%22%2C%22mobileVersion%22%3A6250300%2C%22aid%22%3A414859846%2C%22cid%22%3A241775746%2C%22seasonId%22%3A34684%2C%22epId%22%3A343358%2C%22quality%22%3A64%2C%22contentType%22%3A1%2C%22autoNext%22%3Afalse%2C%22isOpen%22%3Atrue%2C%22seekTs%22%3A4%2C%22accessKey%22%3A%227317e55011809327a6a2d4fbf42dec51%22%7D%7D";
//            mVideoView.setVideoURI(str);
            mVideoView.setVideoPath(str);
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
