package com.chen.videoplayer.player;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chen.videoplayer.R;

/**
 * Created by Administrator on 2017/6/5.
 * 视频播放控制器
 */
public class VideoPlayerController extends FrameLayout implements
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        View.OnTouchListener{

    private VideoPlayerControl mVideoPlayerControl;

    private Context mContext;

    private ImageView mImage; // 封面图
    private ImageView mCenterStart;  // 中间播放按钮
    private LinearLayout mTop;  // 顶部布局
    private ImageView mBack;   // 顶部布局左上角返回按键
    private TextView mTitle;   // 视频标题
    private LinearLayout mBottom;   // 底部布局
    private ImageView mRestartPause; // 底部布局 左下角 播放、暂停控件
    private TextView mPosition;   // 当前播放时间点
    private TextView mDuration;   // 视频总时长
    private SeekBar mSeek;     // 视频播放进度拖动条
    private ImageView mFullScreen;  // 横竖屏切换控件
    private LinearLayout mLoading;  // 加载视频的布局
    private TextView mLoadText;   // 加载视频布局的文案
    private LinearLayout mError;  // 视频加载错误的布局
    private TextView mRetry;      // 点击重试
    private LinearLayout mCompleted;  // 视频播放完成现实的布局
    private TextView mReplay;  // 重新播放
    private TextView mShare;   // 分享

    private boolean topBottomVisible = false;

    private String mUrl;
    private int screenWidth;
    private int screenHeight;

    public VideoPlayerController(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.video_palyer_controller, this);
        mContext = context;
        screenWidth = VideoPlayerUtil.getScreenWidth(context);
        screenHeight = VideoPlayerUtil.getScreenHeight(context);

        mCenterStart = (ImageView) findViewById(R.id.center_start);
        mImage = (ImageView) findViewById(R.id.image);

        mTop = (LinearLayout) findViewById(R.id.top);
        mBack = (ImageView) findViewById(R.id.back);
        mTitle = (TextView) findViewById(R.id.title);

        mBottom = (LinearLayout) findViewById(R.id.bottom);
        mRestartPause = (ImageView) findViewById(R.id.restart_or_pause);
        mPosition = (TextView) findViewById(R.id.position);
        mDuration = (TextView) findViewById(R.id.duration);
        mSeek = (SeekBar) findViewById(R.id.seek);
        mFullScreen = (ImageView) findViewById(R.id.full_screen);

        mLoading = (LinearLayout) findViewById(R.id.loading);
        mLoadText = (TextView) findViewById(R.id.load_text);

        mError = (LinearLayout) findViewById(R.id.error);
        mRetry = (TextView) findViewById(R.id.retry);

        mCompleted = (LinearLayout) findViewById(R.id.completed);
        mReplay = (TextView) findViewById(R.id.replay);
        mShare = (TextView) findViewById(R.id.share);

        mCenterStart.setOnClickListener(this);
        this.setOnClickListener(this);
        mReplay.setOnClickListener(this);
        mSeek.setOnSeekBarChangeListener(this);
        mRestartPause.setOnClickListener(this);
        mFullScreen.setOnClickListener(this);
        mBack.setOnClickListener(this);
        setOnTouchListener(this);
    }

    public void setVideoPlayer(VideoPlayerControl videoPlayerControl){
        mVideoPlayerControl = videoPlayerControl;
    }

    public void setTitle(String title){
        mTitle.setText(title);
    }

    public void setImage(String url){
        mUrl = url;
        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.img_default)
                .into(mImage);
    }

    @Override
    public void onClick(View v) {
        if(v == mCenterStart){
//            if(mVideoPlayerControl.isIdle()){
                mVideoPlayerControl.start();
//            }
        }else if(v == this){
            if(mVideoPlayerControl.isPlaying()
                    || mVideoPlayerControl.isPaused()
                    || mVideoPlayerControl.isBufferingPlaying()
                    || mVideoPlayerControl.isBufferingPaused()){
                setTopBottomVisible(!topBottomVisible);
            }
        }else if(v == mReplay){
            mVideoPlayerControl.release();
            mVideoPlayerControl.start();
            mCompleted.setVisibility(GONE);
        }else if(v == mShare){
            Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show();
        }else if(v == mRestartPause){  //暂停或者播放
            if(mVideoPlayerControl.isPlaying() || mVideoPlayerControl.isBufferingPlaying()){
                mVideoPlayerControl.pause();
            }else if (mVideoPlayerControl.isPaused() || mVideoPlayerControl.isBufferingPaused()){
                mVideoPlayerControl.restart();
            }
        }else if(v == mFullScreen){
            if(mVideoPlayerControl.isNormalScreen()){
                mVideoPlayerControl.enterFullScreen();
            }else if(mVideoPlayerControl.isFullScreen()){
                mVideoPlayerControl.exitFullScreen();
            }
        }else if(v == mBack){
            if(mVideoPlayerControl.isFullScreen()){
                mVideoPlayerControl.exitFullScreen();
            }
        }
    }

    /**
     * 设置头部和底部布局是否隐藏
     */
    private void setTopBottomVisible(boolean topBottomVisible) {
        mTop.setVisibility(topBottomVisible ? VISIBLE : INVISIBLE);
        mBottom.setVisibility(topBottomVisible ? VISIBLE : INVISIBLE);
        this.topBottomVisible = topBottomVisible;

        //3秒显示还没有关闭就自动关闭
        if(topBottomVisible){
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTop.setVisibility(INVISIBLE);
                    mBottom.setVisibility(INVISIBLE);
                    VideoPlayerController.this.topBottomVisible = false;
                }
            }, 8000);
        }
    }

    /**
     * 设置播放器工作状态
     */
    public void setControllerState(int playState, int windowState){
        switch (playState){
            case VideoPlayer.STATE_IDLE:
                mBottom.setVisibility(GONE);
                break;
            case VideoPlayer.STATE_PREPARING:
                mImage.setVisibility(GONE);
                mLoading.setVisibility(VISIBLE);
                mLoadText.setText("正在准备...");
                mCenterStart.setVisibility(GONE);
                mBack.setVisibility(GONE);
                mTop.setVisibility(GONE);
                mBottom.setVisibility(GONE);
                break;
            case VideoPlayer.STATE_PREPARED: // 播放准备就绪
                startUpdateProgress();
                break;
            case VideoPlayer.STATE_BUFFERING_PLAYING: //播放时缓冲
                mLoading.setVisibility(VISIBLE);
                mLoadText.setText("正在缓冲");
                mRestartPause.setImageResource(R.drawable.ic_player_pause);
                break;
            case VideoPlayer.STATE_BUFFERING_PAUSED:  //暂停时缓冲
                mLoading.setVisibility(VISIBLE);
                mLoadText.setText("正在缓冲");
                mRestartPause.setImageResource(R.drawable.ic_player_start);
                break;
            case VideoPlayer.STATE_PLAYING:
                mLoading.setVisibility(GONE);
                mRestartPause.setImageResource(R.drawable.ic_player_pause);
                break;
            case VideoPlayer.STATE_PAUSED:
                mLoading.setVisibility(GONE);
                mRestartPause.setImageResource(R.drawable.ic_player_start);
                break;
            case VideoPlayer.STATE_COMPLETED:
                cancelUpdateProgress();
                mCompleted.setVisibility(VISIBLE);
                mImage.setVisibility(VISIBLE);
                break;
        }

        switch (windowState){
            case VideoPlayer.PLAYER_FULL_SCREEN:
                mBack.setVisibility(VISIBLE);
                mFullScreen.setVisibility(VISIBLE);
                mFullScreen.setImageResource(R.drawable.ic_player_shrink);
                break;
            case VideoPlayer.PLAYER_NORMAL:
                mBack.setVisibility(GONE);
                mFullScreen.setVisibility(VISIBLE);
                mFullScreen.setImageResource(R.drawable.ic_player_enlarge);
                break;
            case VideoPlayer.PLAYER_TINY_WINDOW:
                mBack.setVisibility(GONE);
                mFullScreen.setVisibility(GONE);
                break;
        }
    }

    private final static int MSG_ID = 0x01;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            updateProgress();
            handler.sendEmptyMessageDelayed(MSG_ID, 300);
        }
    };
    /**
     * 更新进度
     */
    private void startUpdateProgress(){
        handler.sendEmptyMessageDelayed(MSG_ID, 300);
    }

    /**
     * 取消更新
     */
    private void cancelUpdateProgress(){
        if(handler != null){
            handler.removeMessages(MSG_ID);
        }
    }

    private void updateProgress(){
        int duration = mVideoPlayerControl.getDuration();
        int currentPosition = mVideoPlayerControl.getCurrentProgress();
        mSeek.setSecondaryProgress(mVideoPlayerControl.getBufferPercent());
        mSeek.setProgress((int) (currentPosition*1.0f/duration*100));
        mPosition.setText(VideoPlayerUtil.formatTime(currentPosition));
        mDuration.setText(VideoPlayerUtil.formatTime(duration));
    }

    /**
     *  暂停播放器
     */
    public void onPause(){

        if(mVideoPlayerControl.isIdle()) return;

        mVideoPlayerControl.pause();
    }

    /**
     *  播放器继续播放
     */
    public void onRestart(){

        if(mVideoPlayerControl.isIdle()) return;   //播放器还只是空闲状态就不继续播放视频了

        mVideoPlayerControl.restart();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        cancelUpdateProgress();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = (int) (mVideoPlayerControl.getDuration() * seekBar.getProgress() / 100f);
        mVideoPlayerControl.seekTo(progress);
        startUpdateProgress();
    }

    public void reset() {
        mError.setVisibility(GONE);
        mLoading.setVisibility(GONE);
        mCompleted.setVisibility(GONE);
    }

    private float startX;
    private float startY;
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // 不是小窗口
        if(!mVideoPlayerControl.isTinyScreen()) return super.onTouchEvent(event);

        // 是小窗口  拖动小窗口的代码
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                float endX = event.getRawX();
                float endY = event.getRawY();

                float dx = endX - startX;
                float dy = endY - startY;

                LayoutParams params = (LayoutParams) mVideoPlayerControl.getContainer().getLayoutParams();

                int left = (int) (params.leftMargin + dx);
                int top = (int) (params.topMargin + dy);
                int viewHeight = mVideoPlayerControl.getContainer().getHeight() + 50;
                int viewWidth = mVideoPlayerControl.getContainer().getWidth();

                if(left < -1){
                    left = 0;
                }else if(left > screenWidth - viewWidth){
                    left = screenWidth - viewWidth;
                }

                if(top < -1){
                    top = 0;
                }else if(top > screenHeight - viewHeight){
                    top = screenHeight - viewHeight;
                }

                params.leftMargin = left;
                params.topMargin = top;
                mVideoPlayerControl.getContainer().setLayoutParams(params);

                startX = endX;
                startY = endY;
                break;
        }

        return super.onTouchEvent(event);
    }
}
