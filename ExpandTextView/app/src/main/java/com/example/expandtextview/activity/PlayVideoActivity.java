package com.example.expandtextview.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.player.IjkPlayer;
import com.dueeeke.videoplayer.player.IjkVideoView;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.example.expandtextview.R;

import me.jessyan.autosize.AutoSizeCompat;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @作者: njb
 * @时间: 2020/1/2 14:02
 * @描述: 视频播放
 */
public class PlayVideoActivity extends AppCompatActivity {
    private String url;
    private IjkVideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        initData();
        initViews();
        initPlayer();
        startPlay();
    }

    private void initViews() {
        //播放view
        videoView = findViewById(R.id.video_view);
    }

    private void initData() {
        if(getIntent()!= null&& getIntent().getExtras()!= null){
            //视频url
            url = getIntent().getExtras().getString("url");
        }
    }

    /**
     * 初始化Player
     */
    private void initPlayer() {
        com.dueeeke.videocontroller.StandardVideoController controller = new StandardVideoController(this);
        IjkPlayer ijkPlayer = new IjkPlayer(this) {
            @Override
            public void setOptions() {
                super.setOptions();
                mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1);
            }
        };
        videoView.setPlayerConfig(new PlayerConfig.Builder().setCustomMediaPlayer(ijkPlayer).build());
        videoView.setVideoController(controller);
    }

    /**
     * 开始播放
     */
    private void startPlay() {
        if (url != null) {
            videoView.setUrl(url);
            videoView.start();
            videoView.isFullScreen();
        }
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //横竖屏切换
        refresh();
    }

    /**
     * 横竖屏切换刷新
     */
    private void refresh() {
        boolean isBaseOnWidth = (getResources().getDisplayMetrics().widthPixels <= getResources().getDisplayMetrics().heightPixels);
        Window window  = getWindow();
        if(window != null){
            window.getDecorView().post(() -> {
                window.getDecorView().setVisibility(View.GONE);
                AutoSizeCompat.autoConvertDensity(getResources(),420,isBaseOnWidth);
                window.getDecorView().setVisibility(View.VISIBLE);
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.pause();
        }
    }

    @Override
    public void onBackPressed() {
        if (!videoView.onBackPressed()) {
            super.onBackPressed();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放播放器
        if (videoView != null) {
            videoView.release();
            videoView = null;
        }

    }
}
