package com.tencent.liteav.demo.superplayer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tencent.liteav.basic.log.TXCLog;
import com.tencent.liteav.demo.superplayer.model.SuperPlayer;
import com.tencent.liteav.demo.superplayer.model.SuperPlayerImpl;
import com.tencent.liteav.demo.superplayer.model.SuperPlayerObserver;
import com.tencent.liteav.demo.superplayer.model.entity.PlayImageSpriteInfo;
import com.tencent.liteav.demo.superplayer.model.entity.PlayKeyFrameDescInfo;
import com.tencent.liteav.demo.superplayer.model.entity.VideoQuality;
import com.tencent.liteav.demo.superplayer.ui.player.FullScreenPlayer;
import com.tencent.liteav.demo.superplayer.ui.player.Player;
import com.tencent.liteav.demo.superplayer.ui.player.WindowPlayer;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 *
 * 超级播放器view
 *
 * 具备播放器基本功能，此外还包括横竖屏切换、悬浮窗播放、画质切换、硬件加速、倍速播放、镜像播放、手势控制等功能，同时支持直播与点播
 * 使用方式极为简单，只需要在布局文件中引入并获取到该控件，通过 传入{@link SuperPlayerModel}即可实现视频播放
 *
 * 1、播放视频
 * 2、设置回调{@link #setPlayerViewCallback(OnSuperPlayerViewCallback)}
 * 3、controller回调实现{@link #mControllerCallback}
 * 4、退出播放释放内存{@link #resetPlayer()}
 */
public class CustomSuperPlayerView extends RelativeLayout {
    private static final String TAG = "SuperPlayerView";

    private Context mContext;

    private ViewGroup        mRootView;                                 // SuperPlayerView的根view
    private TXCloudVideoView mTXCloudVideoView;                         // 腾讯云视频播放view
    private FullScreenPlayer mFullScreenPlayer;                         // 全屏模式控制view（横屏播放模式）
    private WindowPlayer     mWindowPlayer;                             // 窗口模式控制view（竖屏播放模式）

    private ViewGroup.LayoutParams     mLayoutParamWindowMode;          // 窗口播放时SuperPlayerView的布局参数
    private ViewGroup.LayoutParams     mLayoutParamFullScreenMode;      // 全屏播放时SuperPlayerView的布局参数
    private LayoutParams               mVodControllerWindowParams;      // 窗口controller的布局参数
    private LayoutParams               mVodControllerFullScreenParams;  // 全屏controller的布局参数

    private OnSuperPlayerViewCallback mPlayerViewCallback;              // SuperPlayerView回调
    private SuperPlayer               mSuperPlayer;

    public CustomSuperPlayerView(Context context) {
        super(context);
        initialize(context);
    }

    public CustomSuperPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CustomSuperPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        mContext = context;
        initView();
        initPlayer();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mRootView = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.custom_superplayer_vod_view, null);
        mTXCloudVideoView = (TXCloudVideoView) mRootView.findViewById(R.id.superplayer_cloud_video_view);
        mFullScreenPlayer = (FullScreenPlayer) mRootView.findViewById(R.id.superplayer_controller_large);
        mWindowPlayer = (WindowPlayer) mRootView.findViewById(R.id.superplayer_controller_small);

        mVodControllerWindowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mVodControllerFullScreenParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        mFullScreenPlayer.setCallback(mControllerCallback);
        mWindowPlayer.setCallback(mControllerCallback);

        removeAllViews();
        mRootView.removeView(mTXCloudVideoView);
        mRootView.removeView(mWindowPlayer);
        mRootView.removeView(mFullScreenPlayer);

        addView(mTXCloudVideoView);
    }

    private void initPlayer() {
        mSuperPlayer = new SuperPlayerImpl(mContext, mTXCloudVideoView);
        mSuperPlayer.setObserver(mSuperPlayerObserver);

        if (mSuperPlayer.getPlayerMode() == SuperPlayerDef.PlayerMode.FULLSCREEN) {
            addView(mFullScreenPlayer);
            mFullScreenPlayer.hide();
        } else if (mSuperPlayer.getPlayerMode() == SuperPlayerDef.PlayerMode.WINDOW) {
            addView(mWindowPlayer);
            mWindowPlayer.hide();
        }

        post(new Runnable() {
            @Override
            public void run() {
                if (mSuperPlayer.getPlayerMode() == SuperPlayerDef.PlayerMode.WINDOW) {
                    mLayoutParamWindowMode = getLayoutParams();
                }
                try {
                    // 依据上层Parent的LayoutParam类型来实例化一个新的fullscreen模式下的LayoutParam
                    Class parentLayoutParamClazz = getLayoutParams().getClass();
                    Constructor constructor = parentLayoutParamClazz.getDeclaredConstructor(int.class, int.class);
                    mLayoutParamFullScreenMode = (ViewGroup.LayoutParams) constructor.newInstance(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 开始播放
     *
     * @param url 视频地址
     */
    public void play(String url) {
        mSuperPlayer.play(url);
    }

    /**
     * 更新标题
     *
     * @param title 视频名称
     */
    public void updateTitle(String title) {
        mWindowPlayer.updateTitle(title);
        mFullScreenPlayer.updateTitle(title);
    }

    /**
     * resume生命周期回调
     */
    public void onResume() {
        mSuperPlayer.resume();
    }

    /**
     * pause生命周期回调
     */
    public void onPause() {
        mSuperPlayer.pauseVod();
    }

    /**
     * 重置播放器
     */
    public void resetPlayer() {
        stopPlay();
    }

    /**
     * 停止播放
     */
    private void stopPlay() {
        mSuperPlayer.stop();
    }

    /**
     * 设置超级播放器的回掉
     *
     * @param callback
     */
    public void setPlayerViewCallback(OnSuperPlayerViewCallback callback) {
        mPlayerViewCallback = callback;
    }

    /**
     * 控制是否全屏显示
     */
    private void fullScreen(boolean isFull) {
        if (getContext() instanceof Activity) {
            Activity activity = (Activity) getContext();
            if (isFull) {
                //隐藏虚拟按键，并且全屏
                View decorView = activity.getWindow().getDecorView();
                if (decorView == null) return;
                if (Build.VERSION.SDK_INT >= 19) {
                    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                }
            } else {
                View decorView = activity.getWindow().getDecorView();
                if (decorView == null) return;
                if (Build.VERSION.SDK_INT >= 19) {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }
        }
    }

    /**
     * 初始化controller回调
     */
    private Player.Callback mControllerCallback = new Player.Callback() {
        @Override
        public void onSwitchPlayMode(SuperPlayerDef.PlayerMode playerMode) {
            if (playerMode == SuperPlayerDef.PlayerMode.FULLSCREEN) {
                fullScreen(true);
            } else {
                fullScreen(false);
            }
            mFullScreenPlayer.hide();
            mWindowPlayer.hide();
            //请求全屏模式
            if (playerMode == SuperPlayerDef.PlayerMode.FULLSCREEN) {
                if (mLayoutParamFullScreenMode == null) {
                    return;
                }
                removeView(mWindowPlayer);
                addView(mFullScreenPlayer, mVodControllerFullScreenParams);
                setLayoutParams(mLayoutParamFullScreenMode);
                rotateScreenOrientation(SuperPlayerDef.Orientation.LANDSCAPE);
                if (mPlayerViewCallback != null) {
                    mPlayerViewCallback.onStartFullScreenPlay();
                }
            } else if (playerMode == SuperPlayerDef.PlayerMode.WINDOW) {// 请求窗口模式
                if (mSuperPlayer.getPlayerMode() == SuperPlayerDef.PlayerMode.FULLSCREEN) { // 当前是全屏模式
                    if (mLayoutParamWindowMode == null) {
                        return;
                    }
                    removeView(mFullScreenPlayer);
                    addView(mWindowPlayer, mVodControllerWindowParams);
                    setLayoutParams(mLayoutParamWindowMode);
                    rotateScreenOrientation(SuperPlayerDef.Orientation.PORTRAIT);
                    if (mPlayerViewCallback != null) {
                        mPlayerViewCallback.onStopFullScreenPlay();
                    }
                }
            }
            mSuperPlayer.switchPlayMode(playerMode);
        }

        @Override
        public void onBackPressed(SuperPlayerDef.PlayerMode playMode) {
            switch (playMode) {
                case FULLSCREEN:// 当前是全屏模式，返回切换成窗口模式
                    onSwitchPlayMode(SuperPlayerDef.PlayerMode.WINDOW);
                    break;
                case WINDOW:// 当前是窗口模式，返回退出播放器
                    if (mPlayerViewCallback != null) {
                        mPlayerViewCallback.onClickSmallReturnBtn();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFloatPositionChange(int x, int y) {
        }

        @Override
        public void onPause() {
            mSuperPlayer.pause();
        }

        @Override
        public void onResume() {
            if (mSuperPlayer.getPlayerState() == SuperPlayerDef.PlayerState.END) { //重播
                mSuperPlayer.reStart();
            } else if (mSuperPlayer.getPlayerState() == SuperPlayerDef.PlayerState.PAUSE) { //继续播放
                mSuperPlayer.resume();
            }
        }

        @Override
        public void onSeekTo(int position) {
            mSuperPlayer.seek(position);
        }

        @Override
        public void onResumeLive() {
            mSuperPlayer.resumeLive();
        }

        @Override
        public void onDanmuToggle(boolean isOpen) {
        }

        @Override
        public void onSnapshot() {

        }

        @Override
        public void onQualityChange(VideoQuality quality) {
            mFullScreenPlayer.updateVideoQuality(quality);
            mSuperPlayer.switchStream(quality);
        }

        @Override
        public void onSpeedChange(float speedLevel) {
            mSuperPlayer.setRate(speedLevel);
        }

        @Override
        public void onMirrorToggle(boolean isMirror) {
            mSuperPlayer.setMirror(isMirror);
        }

        @Override
        public void onHWAccelerationToggle(boolean isAccelerate) {
            mSuperPlayer.enableHardwareDecode(isAccelerate);
        }
    };

    /**
     * 旋转屏幕方向
     */
    private void rotateScreenOrientation(SuperPlayerDef.Orientation orientation) {
        switch (orientation) {
            case LANDSCAPE:
                ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case PORTRAIT:
                ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
        }
    }

    /**
     * SuperPlayerView的回调接口
     */
    public interface OnSuperPlayerViewCallback {

        /**
         * 开始全屏播放
         */
        void onStartFullScreenPlay();

        /**
         * 结束全屏播放
         */
        void onStopFullScreenPlay();

        /**
         * 点击小播放模式的返回按钮
         */
        void onClickSmallReturnBtn();
    }

    public void release() {
        if (mWindowPlayer != null) {
            mWindowPlayer.release();
        }
        if (mFullScreenPlayer != null) {
            mFullScreenPlayer.release();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            release();
        } catch (Throwable e) {
            TXCLog.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void switchPlayMode(SuperPlayerDef.PlayerMode playerMode) {
        if (playerMode == SuperPlayerDef.PlayerMode.WINDOW) {
            if (mControllerCallback != null) {
                mControllerCallback.onSwitchPlayMode(SuperPlayerDef.PlayerMode.WINDOW);
            }
        } else if (playerMode == SuperPlayerDef.PlayerMode.FLOAT) {
            if (mControllerCallback != null) {
                mControllerCallback.onSwitchPlayMode(SuperPlayerDef.PlayerMode.FLOAT);
            }
        }
    }

    public SuperPlayerDef.PlayerMode getPlayerMode() {
        return mSuperPlayer.getPlayerMode();
    }

    public SuperPlayerDef.PlayerState getPlayerState() {
        return mSuperPlayer.getPlayerState();
    }

    private SuperPlayerObserver mSuperPlayerObserver = new SuperPlayerObserver() {
        @Override
        public void onPlayBegin(String name) {
            mWindowPlayer.updatePlayState(SuperPlayerDef.PlayerState.PLAYING);
            mFullScreenPlayer.updatePlayState(SuperPlayerDef.PlayerState.PLAYING);
            updateTitle(name);
            mWindowPlayer.hideBackground();
        }

        @Override
        public void onPlayPause() {
            mWindowPlayer.updatePlayState(SuperPlayerDef.PlayerState.PAUSE);
            mFullScreenPlayer.updatePlayState(SuperPlayerDef.PlayerState.PAUSE);
        }

        @Override
        public void onPlayStop() {
            mWindowPlayer.updatePlayState(SuperPlayerDef.PlayerState.END);
            mFullScreenPlayer.updatePlayState(SuperPlayerDef.PlayerState.END);
        }

        @Override
        public void onPlayLoading() {
            mWindowPlayer.updatePlayState(SuperPlayerDef.PlayerState.LOADING);
            mFullScreenPlayer.updatePlayState(SuperPlayerDef.PlayerState.LOADING);
        }

        @Override
        public void onPlayProgress(long current, long duration) {
            mWindowPlayer.updateVideoProgress(current, duration);
            mFullScreenPlayer.updateVideoProgress(current, duration);
        }

        @Override
        public void onSeek(int position) {
        }

        @Override
        public void onSwitchStreamStart(boolean success, SuperPlayerDef.PlayerType playerType, VideoQuality quality) {

        }

        @Override
        public void onSwitchStreamEnd(boolean success, SuperPlayerDef.PlayerType playerType, VideoQuality quality) {

        }

        @Override
        public void onPlayerTypeChange(SuperPlayerDef.PlayerType playType) {
            mWindowPlayer.updatePlayType(playType);
            mFullScreenPlayer.updatePlayType(playType);
        }

        @Override
        public void onPlayTimeShiftLive(TXLivePlayer player, String url) {
        }

        @Override
        public void onVideoQualityListChange(List<VideoQuality> videoQualities, VideoQuality defaultVideoQuality) {
            mFullScreenPlayer.setVideoQualityList(videoQualities);
            mFullScreenPlayer.updateVideoQuality(defaultVideoQuality);
        }

        @Override
        public void onVideoImageSpriteAndKeyFrameChanged(PlayImageSpriteInfo info, List<PlayKeyFrameDescInfo> list) {
            mFullScreenPlayer.updateImageSpriteInfo(info);
            mFullScreenPlayer.updateKeyFrameDescInfo(list);
        }

        @Override
        public void onError(int code, String message) {
            showToast(message);
        }
    };

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
