package com.chen.videoplayer.player;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

/**
 * Created by Administrator on 2017/6/5.
 * https://blog.csdn.net/qq_18242391/article/details/73201570
 * 视频播放
 */
public class VideoPlayer extends FrameLayout implements TextureView.SurfaceTextureListener,
                                     VideoPlayerControl{

    public static final int STATE_ERROR = -1;          // 播放错误
    public static final int STATE_IDLE = 0;            // 播放未开始
    public static final int STATE_PREPARING = 1;       // 播放准备中
    public static final int STATE_PREPARED = 2;        // 播放准备就绪
    public static final int STATE_PLAYING = 3;         // 正在播放
    public static final int STATE_PAUSED = 4;          // 暂停播放
    /**
     * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
     **/
    public static final int STATE_BUFFERING_PLAYING = 5;
    /**
     * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停)
     **/
    public static final int STATE_BUFFERING_PAUSED = 6;
    public static final int STATE_COMPLETED = 7;       // 播放完成

    public static final int PLAYER_NORMAL = 10;        // 普通播放器
    public static final int PLAYER_FULL_SCREEN = 11;   // 全屏播放器
    public static final int PLAYER_TINY_WINDOW = 12;   // 小窗口播放器

    private int mCurrentState = STATE_IDLE; //播放状态
    private int mWindowState = PLAYER_NORMAL;  //视频窗口态度

    /**
     * 视频播放控制器
     */
    private VideoPlayerController mController;
    /**
     * 视频播放容器
     */
    private FrameLayout mContainer;

    private SurfaceTexture mSurfaceTexture;

    private MediaPlayer mMediaPlayer;

    private TextureView mTextureView;

    private Context mContext;

    private String mUri;
    /**
    * 缓冲进度
    */
    private int mBufferPercent;

    public VideoPlayer(@NonNull Context context) {
        this(context, null);
    }

    public VideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mContainer = new FrameLayout(context);
        mContainer.setBackgroundColor(Color.BLACK);
        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(mContainer, lp);
    }

    /**
     * 关联视频控制器
     */
    public void setController(VideoPlayerController controller){
        mController = controller;
        mController.setVideoPlayer(this);
        updateVideoPlayerState();
        mContainer.removeView(mController);
        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(mController, lp);
    }

    public void setPlayUri(String uri){
        mUri = uri;
    }

    /**
     * 初始化mediaPlayer
     */
    private void mediaPlayerInit() {
        if(mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();

            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);  //在播放时屏幕一直开启着

            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangeListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        }
    }

    /**
     * 让mediaPlayer播放
     */
    private void mediaPlayerStart(){
        try {
            //设置数据源
            mMediaPlayer.setDataSource(mContext.getApplicationContext(), Uri.parse(mUri));
            //设置surface
            mMediaPlayer.setSurface(new Surface(mSurfaceTexture));
            //异步网络准备
            mMediaPlayer.prepareAsync();

            mCurrentState = STATE_PREPARING;
            updateVideoPlayerState();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void textureViewInit() {
        if(mTextureView == null) {
            mTextureView = new TextureView(mContext);
            mTextureView.setSurfaceTextureListener(this);
        }
    }

    private void textureViewAdd(){
        mContainer.removeView(mTextureView);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        mContainer.addView(mTextureView, 0, params); //mController在mTextureView上面
    }

    /**
     *
     * @param surface  从图像流捕获帧作为OpenGL ES纹理。
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if(mSurfaceTexture == null){
            mSurfaceTexture = surface;
            mediaPlayerStart();
        }else {
            mTextureView.setSurfaceTexture(mSurfaceTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void start() {
        VideoPlayerManager.getInstance().releaseMediaplayer();
        VideoPlayerManager.getInstance().setCurrentVideoPlayer(this); //将当前的VideoPlayer交给管理器类管理
        if(mCurrentState == STATE_IDLE ||
                mCurrentState == STATE_COMPLETED ||
                mCurrentState == STATE_ERROR){
            mediaPlayerInit();
            textureViewInit();
            textureViewAdd();
        }
    }

    @Override
    public void seekTo(int pos) {
        mMediaPlayer.seekTo(pos);
    }

    @Override
    public void pause() {
        if(mCurrentState == STATE_PLAYING || mCurrentState == STATE_BUFFERING_PLAYING){
            if(mMediaPlayer != null) {
                mMediaPlayer.pause();
                mCurrentState = mCurrentState == STATE_PLAYING?STATE_PAUSED:STATE_BUFFERING_PAUSED;
                updateVideoPlayerState();
            }
        }
    }

    @Override
    public void restart() {
        if(mCurrentState == STATE_PAUSED || mCurrentState == STATE_BUFFERING_PAUSED){
            mMediaPlayer.start();
            mCurrentState = mCurrentState == STATE_PAUSED?STATE_PLAYING:STATE_BUFFERING_PLAYING;
            updateVideoPlayerState();
        }
    }

    @Override
    public boolean isIdle() {
        return mCurrentState == STATE_IDLE;
    }

    @Override
    public boolean isError() {
     return mCurrentState == STATE_ERROR;
    }

    @Override
    public boolean isPreparing() {
     return mCurrentState == STATE_PREPARING;
    }

    @Override
    public boolean isPrepared() {
     return mCurrentState == STATE_PREPARED;
    }

    @Override
    public boolean isBufferingPlaying() {
     return mCurrentState == STATE_BUFFERING_PLAYING;
    }

    @Override
    public boolean isBufferingPaused() {
     return mCurrentState == STATE_BUFFERING_PAUSED;
    }

    @Override
    public boolean isPlaying() {
     return mCurrentState == STATE_PLAYING;
    }

    @Override
    public boolean isPaused() {
     return mCurrentState == STATE_PAUSED;
    }

    @Override
    public boolean isCompleted() {
     return mCurrentState == STATE_COMPLETED;
    }

    @Override
    public int getDuration() {
     return mMediaPlayer != null? mMediaPlayer.getDuration():0;
    }

    @Override
    public int getCurrentProgress() {
     return mMediaPlayer != null? mMediaPlayer.getCurrentPosition():0;
    }

    @Override
    public int getBufferPercent() {
        return mBufferPercent;
    }

    @Override
    public FrameLayout getContainer() {
        return mContainer;
    }

    @Override
    public boolean isFullScreen() {
     return mWindowState == PLAYER_FULL_SCREEN;
    }

    @Override
    public boolean isNormalScreen() {
     return mWindowState == PLAYER_NORMAL;
    }

    @Override
    public boolean isTinyScreen() {
     return mWindowState == PLAYER_TINY_WINDOW;
    }

    @Override
    public void enterFullScreen() {
        if(mWindowState == PLAYER_FULL_SCREEN) return;  //已经是全屏就不需要操作了

        VideoPlayerUtil.hideActionBar(mContext); //隐藏状态栏
        VideoPlayerUtil.scanForActivity(mContext).setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  //设置屏幕方向 横向

        this.removeView(mContainer);
        ViewGroup contentView = (ViewGroup) VideoPlayerUtil.scanForActivity(mContext)
                .findViewById(android.R.id.content);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.addView(mContainer, params);

        mWindowState = PLAYER_FULL_SCREEN;
        updateVideoPlayerState();
    }

    /**
    * 退出全屏，移除mTextureView和mController，并添加到非全屏的容器中。
    * 切换竖屏时需要在manifest的activity标签下添加
    * android:configChanges="orientation|keyboardHidden|screenSize"配置，
    * 以避免Activity重新走生命周期.
    *
    * @return true退出全屏成功.
    */
    @Override
    public boolean exitFullScreen() {
        if(mWindowState == PLAYER_FULL_SCREEN){

            VideoPlayerUtil.showActionBar(mContext);  //显示状态栏
            VideoPlayerUtil.scanForActivity(mContext).setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //设置屏幕方向 竖向

            ViewGroup contentView = (ViewGroup) VideoPlayerUtil.scanForActivity(mContext)
                    .findViewById(android.R.id.content);
            contentView.removeView(mContainer);
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            this.addView(mContainer, params);

            mWindowState = PLAYER_NORMAL;
            updateVideoPlayerState();

            return true;
        }
        return false;
    }

    private void updateVideoPlayerState() {
       mController.setControllerState(mCurrentState, mWindowState);
    }

    @Override
    public void enterTinyScreen() {
        if(mWindowState == PLAYER_TINY_WINDOW) return;

        this.removeView(mContainer);
        ViewGroup contentView = (ViewGroup) VideoPlayerUtil.scanForActivity(mContext)
                .findViewById(android.R.id.content);

        LayoutParams params = new LayoutParams(
                (int)(VideoPlayerUtil.getScreenWidth(mContext)*0.6),
                (int)(VideoPlayerUtil.getScreenWidth(mContext)*0.6/16*9));
        contentView.addView(mContainer, params);

        mWindowState = PLAYER_TINY_WINDOW;
        updateVideoPlayerState();
    }

    @Override
    public boolean exitTinyScreen() {
        if(mWindowState == PLAYER_TINY_WINDOW){
            ViewGroup contentView = (ViewGroup) VideoPlayerUtil.scanForActivity(mContext)
                    .findViewById(android.R.id.content);
            contentView.removeView(mContainer);

            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            this.addView(mContainer, params);

            mWindowState = PLAYER_NORMAL;
            updateVideoPlayerState();

            return true;
        }
        return false;
    }

    /**
     * MediaPlayer准备好播放监听
     */
    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            mCurrentState = STATE_PREPARED;
            updateVideoPlayerState();
        }
    };

    /**
     *  Video播放尺寸大小改变监听
     */
    private MediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangeListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

        }
    };

    /**
     * mediaPlayer播放完成监听
     */
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mCurrentState = STATE_COMPLETED; //播放完成
            updateVideoPlayerState();
        }
    };

    /**
     *  Mediaplayer播放错误监听
     */
    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return true;
        }
    };

    /**
     * MediaPlayer缓冲状态改变监听
     */
    private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            mBufferPercent = percent;
        }
    };

    private MediaPlayer.OnInfoListener mOnInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            if(what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
                //播放器开始渲染
                mCurrentState = STATE_PLAYING;
                updateVideoPlayerState();
            }else if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START){
                if(mCurrentState == STATE_PAUSED || mCurrentState == STATE_BUFFERING_PAUSED){
                    mCurrentState = STATE_BUFFERING_PAUSED;
                    updateVideoPlayerState();
                }else {
                    mCurrentState = STATE_BUFFERING_PLAYING;
                    updateVideoPlayerState();
                }
            }else if(what == MediaPlayer.MEDIA_INFO_BUFFERING_END){
                if(mCurrentState == STATE_BUFFERING_PAUSED){
                    mCurrentState = STATE_PAUSED;
                    updateVideoPlayerState();
                }else if(mCurrentState == STATE_BUFFERING_PLAYING){
                    mCurrentState = STATE_PLAYING;
                    updateVideoPlayerState();
                }
            }else {

            }
            return true;
        }
    };

    /**
     * 释放资源
     */
    @Override
    public void release() {

        if(mController != null){
            mController.reset();
        }

        if(mMediaPlayer != null){
            mMediaPlayer.release();  //释放资源
            mMediaPlayer = null;
        }
        mContainer.removeView(mTextureView);
        if(mSurfaceTexture != null){
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
    }

}
