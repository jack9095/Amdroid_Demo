package com.kuanquan.lyrics_demo;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kuanquan.lyrics.LyricsReader;
import com.kuanquan.lyrics.utils.ColorUtils;
import com.kuanquan.lyrics.utils.TimeUtils;
import com.kuanquan.lyrics.widget.AbstractLrcView;
import com.kuanquan.lyrics.widget.ManyLyricsView;
import com.zml.libs.widget.MusicSeekBar;

import java.io.InputStream;

/**
 * 多行歌词
 */
public class ManyActivity extends AppCompatActivity {


    /**
     * 多行歌词视图
     */
    private ManyLyricsView mManyLyricsView;
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
     * 播放按钮
     */
    private Button mPlayBtn;
    /**
     * 暂停按钮
     */
    private Button mPauseBtn;
    /**
     * 停止按钮
     */
    private Button mStopBtn;
    /**
     * 字体大小按钮
     */
    private Button mFontBtn;
    /**
     * 音译
     */
    private Button mTransliterationBtn;
    /**
     * 翻译
     */
    private Button mTranslateBtn;
    /**
     * 播放器
     */
    private MediaPlayer mMediaPlayer;

    /**
     * 更新进度
     */
    private final int UPDATE_PROGRESS = 0;

    /**
     * 额外歌词回调
     */
    private final int EXTRALRCALLBACK = 1;

    /**
     * 播放歌曲
     */
    private final int MUSIC_PLAY = 2;

    /**
     * 歌曲暂停
     */
    private final int MUSIC_PAUSE = 3;

    /**
     * 歌曲初始
     */
    private final int MUSIC_INIT = 4;
    /**
     * 歌曲快进
     */
    private final int MUSIC_SEEKTO = 5;
    /**
     * 歌曲停止
     */
    private final int MUSIC_STOP = 6;
    /**
     * 歌曲唤醒
     */
    private final int MUSIC_RESUME = 7;

    private final String TAG = FloatActivity.class.getName();

    private Handler mHandler = new Handler() {
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

                    int extraLrcType = mManyLyricsView.getExtraLrcType();
                    if (extraLrcType == ManyLyricsView.EXTRALRCTYPE_BOTH) {
                        //有翻译歌词和音译歌词
                        mTranslateBtn.setEnabled(true);
                        mTransliterationBtn.setEnabled(true);
                    } else if (extraLrcType == ManyLyricsView.EXTRALRCTYPE_TRANSLATELRC) {
                        //有翻译歌词
                        mTranslateBtn.setEnabled(true);
                        mTransliterationBtn.setEnabled(false);
                    } else if (extraLrcType == ManyLyricsView.EXTRALRCTYPE_TRANSLITERATIONLRC) {
                        //音译歌词
                        mTranslateBtn.setEnabled(false);
                        mTransliterationBtn.setEnabled(true);
                    } else {
                        //无翻译歌词和音译歌词
                        mTranslateBtn.setEnabled(false);
                        mTransliterationBtn.setEnabled(false);
                    }
                    //根据额外歌词的显示状态修改按钮颜色
                    if (mManyLyricsView.getExtraLrcStatus() == ManyLyricsView.EXTRALRCSTATUS_SHOWTRANSLITERATIONLRC) {
                        mTransliterationBtn.setTextColor(Color.RED);
                    } else if (mManyLyricsView.getExtraLrcStatus() == ManyLyricsView.EXTRALRCSTATUS_SHOWTRANSLATELRC) {
                        mTranslateBtn.setTextColor(Color.RED);
                    } else {
                        mTranslateBtn.setTextColor(Color.BLACK);
                        mTransliterationBtn.setTextColor(Color.BLACK);
                    }


                    break;

                case MUSIC_PLAY:
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying() && mManyLyricsView.getLrcStatus() == ManyLyricsView.LRCSTATUS_LRC && mManyLyricsView.getLrcPlayerStatus() != ManyLyricsView.LRCPLAYERSTATUS_PLAY) {
                        mManyLyricsView.play(mMediaPlayer.getCurrentPosition());
                    }
                    break;

                case MUSIC_PAUSE:

                    if (mMediaPlayer != null && mManyLyricsView.getLrcStatus() == ManyLyricsView.LRCSTATUS_LRC) {
                        mManyLyricsView.pause();
                    }
                    break;

                case MUSIC_INIT:
                    mManyLyricsView.initLrcData();
                    //加载中
                    mManyLyricsView.setLrcStatus(ManyLyricsView.LRCSTATUS_LOADING);
                    break;

                case MUSIC_SEEKTO:
                    if (mMediaPlayer != null && mManyLyricsView.getLrcStatus() == ManyLyricsView.LRCSTATUS_LRC) {
                        mManyLyricsView.seekto(mMediaPlayer.getCurrentPosition());
                    }

                    break;

                case MUSIC_STOP:

                    mManyLyricsView.initLrcData();
                    //
                    mMusicSeekBar.setProgress(0);
                    mMusicSeekBar.setMax(0);
                    mMusicSeekBar.setEnabled(false);
                    //
                    mSongDurationTV.setText(TimeUtils.parseMMSSString(0));
                    mSongProgressTV.setText(TimeUtils.parseMMSSString(0));

                    break;

                case MUSIC_RESUME:

                    if (mMediaPlayer != null && mManyLyricsView.getLrcStatus() == ManyLyricsView.LRCSTATUS_LRC) {
                        mManyLyricsView.resume();
                    }

                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_many);

        //
        mManyLyricsView = findViewById(R.id.manyLyricsView);
        //默认颜色
        int[] paintColors = new int[]{
                ColorUtils.parserColor("#ffffff"),
                ColorUtils.parserColor("#ffffff")
        };
        mManyLyricsView.setPaintColor(paintColors, false);

        //高亮颜色
        int[] paintHLColors = new int[]{
                ColorUtils.parserColor("#fada83"),
                ColorUtils.parserColor("#fada83")
        };
        mManyLyricsView.setPaintHLColor(paintHLColors, false);

        mManyLyricsView.setExtraLyricsListener(new AbstractLrcView.ExtraLyricsListener() {
            @Override
            public void extraLrcCallback() {
                mHandler.sendEmptyMessage(EXTRALRCALLBACK);
            }
        });

        //
        mManyLyricsView.setSearchLyricsListener(new AbstractLrcView.SearchLyricsListener() {
            @Override
            public void goToSearchLrc() {

            }
        });
        mManyLyricsView.setOnLrcClickListener(new ManyLyricsView.OnLrcClickListener() {
            @Override
            public void onLrcPlayClicked(int progress) {
                if (mMediaPlayer != null) {
                    if (progress <= mMediaPlayer.getDuration()) {
                        mMediaPlayer.seekTo(progress);
                    }
                }

            }
        });
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

        //
        mPlayBtn = findViewById(R.id.play);
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    loadLrcFile();

                    mMediaPlayer.start();
                    mHandler.sendEmptyMessage(MUSIC_PLAY);
                    mHandler.postDelayed(mPlayRunnable, 0);

                    return;
                }

                if (mMediaPlayer.isPlaying()) return;

                mMediaPlayer.start();
                mHandler.sendEmptyMessage(MUSIC_RESUME);
                mHandler.postDelayed(mPlayRunnable, 0);
            }
        });

        mPauseBtn = findViewById(R.id.pause);
        mPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer.pause();
                mHandler.sendEmptyMessage(MUSIC_PAUSE);
            }
        });

        mStopBtn = findViewById(R.id.stop);
        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMediaPlayer != null) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }

                mHandler.sendEmptyMessage(MUSIC_STOP);

            }
        });

        //字体大小
        mFontBtn = findViewById(R.id.font);
        mFontBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int fontSize = 40;
                //有歌词，则重新分割歌词
                mManyLyricsView.setFontSize(fontSize, true);

            }
        });

        //翻译按钮
        mTranslateBtn = findViewById(R.id.translate);
        mTranslateBtn.setEnabled(false);
        mTranslateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mManyLyricsView.getLrcStatus() == ManyLyricsView.LRCSTATUS_LRC) {
                    if (mManyLyricsView.getExtraLrcStatus() == ManyLyricsView.EXTRALRCSTATUS_SHOWTRANSLATELRC) {
                        mTranslateBtn.setTextColor(Color.BLACK);
                        mManyLyricsView.setExtraLrcStatus(ManyLyricsView.EXTRALRCSTATUS_NOSHOWEXTRALRC);
                    } else {
                        mTransliterationBtn.setTextColor(Color.BLACK);
                        mTranslateBtn.setTextColor(Color.RED);
                        mManyLyricsView.setExtraLrcStatus(ManyLyricsView.EXTRALRCSTATUS_SHOWTRANSLATELRC);
                    }

                }
            }
        });

        //音译按钮
        mTransliterationBtn = findViewById(R.id.transliteration);
        mTransliterationBtn.setEnabled(false);
        mTransliterationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mManyLyricsView.getLrcStatus() == ManyLyricsView.LRCSTATUS_LRC) {
                    if (mManyLyricsView.getExtraLrcStatus() == ManyLyricsView.EXTRALRCSTATUS_SHOWTRANSLITERATIONLRC) {
                        mTransliterationBtn.setTextColor(Color.BLACK);
                        mManyLyricsView.setExtraLrcStatus(ManyLyricsView.EXTRALRCSTATUS_NOSHOWEXTRALRC);
                    } else {
                        mTranslateBtn.setTextColor(Color.BLACK);
                        mTransliterationBtn.setTextColor(Color.RED);
                        mManyLyricsView.setExtraLrcStatus(ManyLyricsView.EXTRALRCSTATUS_SHOWTRANSLITERATIONLRC);
                    }
                }
            }
        });
    }

    /**
     * 加载歌词文件
     */
    private void loadLrcFile() {

        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... strings) {
                InputStream inputStream = getResources().openRawResource(R.raw.aiqingyu_krc);
                try {
                    //延迟看一下加载效果
                    Thread.sleep(500);

                    LyricsReader lyricsReader = new LyricsReader();
                    byte[] data = new byte[inputStream.available()];
                    inputStream.read(data);
                    lyricsReader.loadLrc(data, null, "aiqingyu_krc.krc");
                    mManyLyricsView.setLyricsReader(lyricsReader);
                    //
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying() && mManyLyricsView.getLrcStatus() == ManyLyricsView.LRCSTATUS_LRC && mManyLyricsView.getLrcPlayerStatus() != ManyLyricsView.LRCPLAYERSTATUS_PLAY) {
                        mManyLyricsView.play(mMediaPlayer.getCurrentPosition());
                    }

                    inputStream.close();
                } catch (Exception e) {

                    mManyLyricsView.setLrcStatus(ManyLyricsView.LRCSTATUS_ERROR);
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                }
                inputStream = null;

                return null;
            }
        }.execute("");
    }

    private Runnable mPlayRunnable = new Runnable() {
        @Override
        public void run() {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mHandler.sendEmptyMessage(UPDATE_PROGRESS);

                mHandler.postDelayed(mPlayRunnable, 1000);
            }
        }
    };

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
