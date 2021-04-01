package com.kuanquan.lyrics_demo.many;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kuanquan.lyrics.utils.TimeUtils;
import com.kuanquan.lyrics_demo.R;
import com.zml.libs.widget.MusicSeekBar;

public class BaseManyActivity extends AppCompatActivity {

    /**
     * 更新进度
     */
    protected final int UPDATE_PROGRESS = 0;
    /**
     * 额外歌词回调
     */
    protected final int EXTRALRCALLBACK = 1;
    /**
     * 播放歌曲
     */
    protected final int MUSIC_PLAY = 2;
    /**
     * 歌曲暂停
     */
    protected final int MUSIC_PAUSE = 3;
    /**
     * 歌曲初始
     */
    protected final int MUSIC_INIT = 4;
    /**
     * 歌曲快进
     */
    protected final int MUSIC_SEEKTO = 5;
    /**
     * 歌曲停止
     */
    protected final int MUSIC_STOP = 6;
    /**
     * 歌曲唤醒
     */
    protected final int MUSIC_RESUME = 7;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 播放器
     */
    protected MediaPlayer mMediaPlayer;
    /**
     * 歌曲播放进度
     */
    protected TextView mSongProgressTV;
    /**
     * 歌曲进度条
     */
    protected MusicSeekBar mMusicSeekBar;
    /**
     * 歌曲长度
     */
    protected TextView mSongDurationTV;

    @SuppressLint("ObsoleteSdkInt")
    protected void setViewOnClick(Handler mHandler){
        //
        mSongProgressTV = findViewById(R.id.songProgress);
        mMusicSeekBar = findViewById(R.id.lrcseekbar);
        mMusicSeekBar.setOnMusicListener(new MusicSeekBar.OnMusicListener() {
            @Override
            public String getTimeText() {
                return TimeUtils.parseMMSSString(mMusicSeekBar.getProgress());
            }

            @Override
            public String getLrcText() {
                return null;
            }

            @Override
            public void onProgressChanged(MusicSeekBar musicSeekBar) {

            }

            @Override
            public void onTrackingTouchFinish(MusicSeekBar musicSeekBar) {
                if (mMediaPlayer != null) {
                    mMediaPlayer.seekTo(mMusicSeekBar.getProgress());
                }
            }
        });

        mSongDurationTV = findViewById(R.id.songDuration);


        // 暂停按钮
        findViewById(R.id.pause).setOnClickListener(view -> {
            mMediaPlayer.pause();
            mHandler.sendEmptyMessage(MUSIC_PAUSE);
        });

        // 停止按钮
        findViewById(R.id.stop).setOnClickListener(view -> {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            mHandler.sendEmptyMessage(MUSIC_STOP);
        });
    }


    @Override
    public void onBackPressed() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onBackPressed();
    }
}
