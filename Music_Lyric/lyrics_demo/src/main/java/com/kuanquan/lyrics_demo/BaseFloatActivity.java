package com.kuanquan.lyrics_demo;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kuanquan.lyrics.utils.TimeUtils;
import com.zml.libs.widget.MusicSeekBar;

public abstract class BaseFloatActivity  extends AppCompatActivity implements Handler.Callback {
    protected final String TAG = FloatActivity.class.getName();
    /**
     * 播放器
     */
    protected MediaPlayer mMediaPlayer;

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

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case UPDATE_PROGRESS:
                mMusicSeekBar.setEnabled(true);
                if (mMediaPlayer != null) {
                    if (mMusicSeekBar.getMax() == 0) {
                        mMusicSeekBar.setMax(mMediaPlayer.getDuration());
                        mSongDurationTV.setText(TimeUtils.parseMMSSString(mMediaPlayer.getDuration()));
                    }
                    //
                    mMusicSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                    mSongProgressTV.setText(TimeUtils.parseMMSSString(mMediaPlayer.getCurrentPosition()));
                }
                break;
            case MUSIC_STOP:
//                mFloatLyricsView.initLrcData();
                //
                mMusicSeekBar.setProgress(0);
                mMusicSeekBar.setMax(0);
                mMusicSeekBar.setEnabled(false);
                //
                mSongDurationTV.setText(TimeUtils.parseMMSSString(0));
                mSongProgressTV.setText(TimeUtils.parseMMSSString(0));
                break;
        }
        return false;
    }

    protected Handler mHandler = new Handler(Looper.getMainLooper(), this);

    protected Runnable mPlayRunnable = new Runnable() {
        @Override
        public void run() {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                playRunnable();
            }
        }
    };

    protected abstract void playRunnable();
    protected abstract void loadLrcFile();
    /**
     * 加载歌词文件
     */
    @SuppressLint("StaticFieldLeak")
    private void loadFile() {

        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... strings) {
                loadLrcFile();
                return null;
            }
        }.execute("");
    }

    protected void setViewOnClick(Handler mHandler){
        mSongDurationTV = findViewById(R.id.songDuration);
        mSongProgressTV = findViewById(R.id.songProgress);
        mMusicSeekBar = findViewById(R.id.lrcseekbar);
        // 播放按钮
        findViewById(R.id.play).setOnClickListener(view -> {

            if (mMediaPlayer == null) {

                mHandler.sendEmptyMessage(MUSIC_INIT);

                //
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.aiqingyu);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mMediaPlayer.release();
                        mMediaPlayer = null;

                        mHandler.sendEmptyMessage(MUSIC_STOP);
                    }
                });

                //快进事件
                mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mediaPlayer) {
                        mHandler.sendEmptyMessage(MUSIC_SEEKTO);
                    }
                });

                //异步加载歌词文件
                loadFile();

                mMediaPlayer.start();
                mHandler.sendEmptyMessage(MUSIC_PLAY);
                mHandler.postDelayed(mPlayRunnable, 0);

                return;
            }

            if (mMediaPlayer.isPlaying()) return;

            mMediaPlayer.start();
            mHandler.sendEmptyMessage(MUSIC_RESUME);
            mHandler.postDelayed(mPlayRunnable, 0);
        });

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
