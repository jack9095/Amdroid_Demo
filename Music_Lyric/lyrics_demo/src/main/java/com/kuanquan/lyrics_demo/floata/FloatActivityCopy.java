package com.kuanquan.lyrics_demo.floata;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.kuanquan.lyrics.LyricsReader;
import com.kuanquan.lyrics.utils.ColorUtils;
import com.kuanquan.lyrics.utils.TimeUtils;
import com.kuanquan.lyrics.widget.AbstractLrcView;
import com.kuanquan.lyrics.widget.FloatLyricsView;
import com.kuanquan.lyrics_demo.BaseFloatActivity;
import com.kuanquan.lyrics_demo.R;
import com.zml.libs.widget.MusicSeekBar;

import java.io.InputStream;

/**
 * 双行歌词
 */
public class FloatActivityCopy extends BaseFloatActivity {

    /**
     * 双行歌词视图
     */
    private FloatLyricsView mFloatLyricsView;
    /**
     * 歌曲播放进度
     */
    private TextView mSongProgressTV;
    /**
     * 歌曲进度条
     */
    private MusicSeekBar mMusicSeekBar;

    /**
     * 歌曲长度
     */
    private TextView mSongDurationTV;
    /**
     * 音译
     */
    private Button mTransliterationBtn;
    /**
     * 翻译
     */
    private Button mTranslateBtn;

    private final String TAG = FloatActivityCopy.class.getName();

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
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

                case EXTRALRCALLBACK:

                    int extraLrcType = mFloatLyricsView.getExtraLrcType();
                    if (extraLrcType == FloatLyricsView.EXTRALRCTYPE_BOTH) {
                        //有翻译歌词和音译歌词
                        mTranslateBtn.setEnabled(true);
                        mTransliterationBtn.setEnabled(true);
                    } else if (extraLrcType == FloatLyricsView.EXTRALRCTYPE_TRANSLATELRC) {
                        //有翻译歌词
                        mTranslateBtn.setEnabled(true);
                        mTransliterationBtn.setEnabled(false);
                    } else if (extraLrcType == FloatLyricsView.EXTRALRCTYPE_TRANSLITERATIONLRC) {
                        //音译歌词
                        mTranslateBtn.setEnabled(false);
                        mTransliterationBtn.setEnabled(true);
                    } else {
                        //无翻译歌词和音译歌词
                        mTranslateBtn.setEnabled(false);
                        mTransliterationBtn.setEnabled(false);
                    }
                    //根据额外歌词的显示状态修改按钮颜色
                    if (mFloatLyricsView.getExtraLrcStatus() == FloatLyricsView.EXTRALRCSTATUS_SHOWTRANSLITERATIONLRC) {
                        mTransliterationBtn.setTextColor(Color.RED);
                    } else if (mFloatLyricsView.getExtraLrcStatus() == FloatLyricsView.EXTRALRCSTATUS_SHOWTRANSLATELRC) {
                        mTranslateBtn.setTextColor(Color.RED);
                    } else {
                        mTranslateBtn.setTextColor(Color.BLACK);
                        mTransliterationBtn.setTextColor(Color.BLACK);
                    }

                    break;

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
                    //
                    mMusicSeekBar.setProgress(0);
                    mMusicSeekBar.setMax(0);
                    mMusicSeekBar.setEnabled(false);
                    //
                    mSongDurationTV.setText(TimeUtils.parseMMSSString(0));
                    mSongProgressTV.setText(TimeUtils.parseMMSSString(0));

                    break;

                case MUSIC_RESUME:

                    if (mMediaPlayer != null && mFloatLyricsView.getLrcStatus() == FloatLyricsView.LRCSTATUS_LRC) {
                        mFloatLyricsView.resume();
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_copy);

        //
        mFloatLyricsView = findViewById(R.id.floatlyricsview);
        //默认颜色
        int[] paintColors = new int[]{
                ColorUtils.parserColor("#00348a"),
                ColorUtils.parserColor("#0080c0"),
                ColorUtils.parserColor("#03cafc")
        };
        mFloatLyricsView.setPaintColor(paintColors,false);

        //高亮颜色
        int[] paintHLColors = new int[]{
                ColorUtils.parserColor("#82f7fd"),
                ColorUtils.parserColor("#ffffff"),
                ColorUtils.parserColor("#03e9fc")
        };
        mFloatLyricsView.setPaintHLColor(paintHLColors,false);

        //设置字体文件
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/weiruanyahei14M.ttf");
        mFloatLyricsView.setTypeFace(typeFace,false);
        mFloatLyricsView.setExtraLyricsListener(new AbstractLrcView.ExtraLyricsListener() {
            @Override
            public void extraLrcCallback() {
                mHandler.sendEmptyMessage(EXTRALRCALLBACK);
            }
        });

        //
        mFloatLyricsView.setSearchLyricsListener(new AbstractLrcView.SearchLyricsListener() {
            @Override
            public void goToSearchLrc() {

            }
        });


        setViewOnClick(mHandler);

        //
        mSongProgressTV = findViewById(R.id.songProgress);
        mMusicSeekBar = findViewById(R.id.lrcseekbar);
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

        mSongDurationTV = findViewById(R.id.songDuration);

        // 字体大小按钮
        findViewById(R.id.font).setOnClickListener(view -> {

            int fontSize = mFloatLyricsView.getHeight() / 3;
            int spaceLineHeight = fontSize / 2;
            mFloatLyricsView.setSpaceLineHeight(spaceLineHeight, false);
            mFloatLyricsView.setExtraLrcSpaceLineHeight(spaceLineHeight, false);
            //有歌词，则重新分割歌词
            mFloatLyricsView.setFontSize(fontSize, true);

        });

        //翻译按钮
        mTranslateBtn = findViewById(R.id.translate);
        mTranslateBtn.setEnabled(false);
        mTranslateBtn.setOnClickListener(view -> {
            if (mFloatLyricsView.getLrcStatus() == FloatLyricsView.LRCSTATUS_LRC) {
                if (mFloatLyricsView.getExtraLrcStatus() == FloatLyricsView.EXTRALRCSTATUS_SHOWTRANSLATELRC) {
                    mTranslateBtn.setTextColor(Color.BLACK);
                    mFloatLyricsView.setExtraLrcStatus(FloatLyricsView.EXTRALRCSTATUS_NOSHOWEXTRALRC);
                } else {
                    mTransliterationBtn.setTextColor(Color.BLACK);
                    mTranslateBtn.setTextColor(Color.RED);
                    mFloatLyricsView.setExtraLrcStatus(FloatLyricsView.EXTRALRCSTATUS_SHOWTRANSLATELRC);
                }

            }
        });

        //音译按钮
        mTransliterationBtn = findViewById(R.id.transliteration);
        mTransliterationBtn.setEnabled(false);
        mTransliterationBtn.setOnClickListener(view -> {
            if (mFloatLyricsView.getLrcStatus() == FloatLyricsView.LRCSTATUS_LRC) {
                if (mFloatLyricsView.getExtraLrcStatus() == FloatLyricsView.EXTRALRCSTATUS_SHOWTRANSLITERATIONLRC) {
                    mTransliterationBtn.setTextColor(Color.BLACK);
                    mFloatLyricsView.setExtraLrcStatus(FloatLyricsView.EXTRALRCSTATUS_NOSHOWEXTRALRC);
                } else {
                    mTranslateBtn.setTextColor(Color.BLACK);
                    mTransliterationBtn.setTextColor(Color.RED);
                    mFloatLyricsView.setExtraLrcStatus(FloatLyricsView.EXTRALRCSTATUS_SHOWTRANSLITERATIONLRC);
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
        inputStream = null;
    }

    @Override
    protected void playRunnable() {
        mHandler.sendEmptyMessage(UPDATE_PROGRESS);
        mHandler.postDelayed(mPlayRunnable, 1000);
    }
}
