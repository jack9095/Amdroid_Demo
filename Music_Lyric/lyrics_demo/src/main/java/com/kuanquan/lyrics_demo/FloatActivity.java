package com.kuanquan.lyrics_demo;

import android.os.*;
import android.util.Log;
import androidx.annotation.NonNull;

import com.kuanquan.lyrics.LyricsReader;
import com.kuanquan.lyrics.utils.*;
import com.kuanquan.lyrics.widget.FloatLyricsView;
import com.zml.libs.widget.MusicSeekBar;

import java.io.InputStream;

/**
 * MV双行歌词
 */
public class FloatActivity extends BaseFloatActivity {

    // 双行歌词视图
    private FloatLyricsView mFloatLyricsView;

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case MUSIC_PLAY:
                if (mMediaPlayer != null && mMediaPlayer.isPlaying() && mFloatLyricsView.getLrcStatus() == FloatLyricsView.LRCSTATUS_LRC && mFloatLyricsView.getLrcPlayerStatus() != FloatLyricsView.LRCPLAYERSTATUS_PLAY) {
                    mFloatLyricsView.play(mMediaPlayer.getCurrentPosition());
                }
                break;

            case MUSIC_PAUSE:
                if (mMediaPlayer != null && mFloatLyricsView.getLrcStatus() == FloatLyricsView.LRCSTATUS_LRC) {
                    mFloatLyricsView.pause();
                }
                break;

            case MUSIC_INIT:
                mFloatLyricsView.initLrcData();
                //加载中
                mFloatLyricsView.setLrcStatus(FloatLyricsView.LRCSTATUS_LOADING);
                break;

            case MUSIC_SEEKTO:
                if (mMediaPlayer != null && mFloatLyricsView.getLrcStatus() == FloatLyricsView.LRCSTATUS_LRC) {
                    mFloatLyricsView.seekto(mMediaPlayer.getCurrentPosition());
                }
                break;

            case MUSIC_STOP:
                mFloatLyricsView.initLrcData();
                break;

            case MUSIC_RESUME:
                if (mMediaPlayer != null && mFloatLyricsView.getLrcStatus() == FloatLyricsView.LRCSTATUS_LRC) {
                    mFloatLyricsView.resume();
                }
                break;
        }
        return super.handleMessage(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float);

        setViewOnClick(mHandler);
        mFloatLyricsView = findViewById(R.id.floatlyricsview);
        // 默认颜色
        mFloatLyricsView.setPaintColor(ColorUtils.parserColor("#666666"),false);
        // 高亮颜色
        mFloatLyricsView.setPaintHLColor(ColorUtils.parserColor("#ff0000"),false);
        // 设置文字大小
        mFloatLyricsView.setFontSize(60);

        mMusicSeekBar.setOnMusicListener(new MusicSeekBar.OnMusicListener() {
            @Override
            public String getTimeText() {
                if (mFloatLyricsView.getLrcStatus() == FloatLyricsView.LRCSTATUS_LRC) {
                    if (mFloatLyricsView.getExtraLrcStatus() == FloatLyricsView.EXTRALRCSTATUS_NOSHOWEXTRALRC)
                        //不显示额外歌词
                        return TimeUtils.parseMMSSString(Math.max(0, mFloatLyricsView.getSplitLineLrcStartTime(mMusicSeekBar.getProgress())));
                    else
                        return TimeUtils.parseMMSSString(Math.max(0, mFloatLyricsView.getLineLrcStartTime(mMusicSeekBar.getProgress())));
                }
                return TimeUtils.parseMMSSString(mMusicSeekBar.getProgress());
            }

            @Override
            public String getLrcText() {
                if (mFloatLyricsView.getLrcStatus() == FloatLyricsView.LRCSTATUS_LRC) {
                    if (mFloatLyricsView.getExtraLrcStatus() == FloatLyricsView.EXTRALRCSTATUS_NOSHOWEXTRALRC)
                        //不显示额外歌词
                        return mFloatLyricsView.getSplitLineLrc(mMusicSeekBar.getProgress());
                    else
                        return mFloatLyricsView.getLineLrc(mMusicSeekBar.getProgress());
                }
                return null;
            }

            @Override
            public void onProgressChanged(MusicSeekBar musicSeekBar) {

            }

            @Override
            public void onTrackingTouchFinish(MusicSeekBar musicSeekBar) {
                if (mMediaPlayer != null) {
                    if (mFloatLyricsView.getLrcStatus() == FloatLyricsView.LRCSTATUS_LRC) {
                        int seekToTime;
                        if (mFloatLyricsView.getExtraLrcStatus() == FloatLyricsView.EXTRALRCSTATUS_NOSHOWEXTRALRC)
                            //不显示额外歌词
                            seekToTime = mFloatLyricsView.getSplitLineLrcStartTime(mMusicSeekBar.getProgress());
                        else
                            seekToTime = mFloatLyricsView.getLineLrcStartTime(mMusicSeekBar.getProgress());
                        if (seekToTime != -1 && seekToTime <= mMusicSeekBar.getMax()) {
                            mMediaPlayer.seekTo(seekToTime);
                            mMusicSeekBar.setProgress(seekToTime);
                        }
                    } else {
                        mMediaPlayer.seekTo(mMusicSeekBar.getProgress());
                    }
                }
            }
        });
    }

    /**
     * 加载歌词文件
     */
    @Override
    protected void loadLrcFile() {
        InputStream inputStream = getResources().openRawResource(R.raw.aiqingyu_krc);
        try {
            LyricsReader lyricsReader = new LyricsReader();
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            lyricsReader.loadLrc(data, null, "aiqingyu_krc.krc");
            mFloatLyricsView.setLyricsReader(lyricsReader);
            //
            if (mMediaPlayer != null && mMediaPlayer.isPlaying() && mFloatLyricsView.getLrcStatus() == FloatLyricsView.LRCSTATUS_LRC && mFloatLyricsView.getLrcPlayerStatus() != FloatLyricsView.LRCPLAYERSTATUS_PLAY) {
                mFloatLyricsView.play(mMediaPlayer.getCurrentPosition());
            }
            inputStream.close();
        } catch (Exception e) {
            mFloatLyricsView.setLrcStatus(FloatLyricsView.LRCSTATUS_ERROR);
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void playRunnable() {
        mHandler.sendEmptyMessage(UPDATE_PROGRESS);
        mHandler.postDelayed(mPlayRunnable, 1000);
    }
}
