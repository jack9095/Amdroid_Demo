package com.kuanquan.test.player;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.liteav.basic.log.TXCLog;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.io.File;

/**
 * 播放器的实现类
 */
public class SuperPlayerImpl implements SuperPlayer, ITXVodPlayListener {

    private static final String TAG = "SuperPlayerImpl";
    private static final int SUPPORT_MAJOR_VERSION = 8;
    private static final int SUPPORT_MINOR_VERSION = 5;

    private TXCloudVideoView mVideoView;        // 腾讯云视频播放view

    private TXVodPlayer       mVodPlayer;       // 点播播放器

    private SuperPlayerModel    mCurrentModel;  // 当前播放的model
    private SuperPlayerObserver mObserver;

    private SuperPlayerDef.PlayerMode mCurrentPlayMode   = SuperPlayerDef.PlayerMode.WINDOW;    // 当前播放模式
    private SuperPlayerDef.PlayerState mCurrentPlayState = SuperPlayerDef.PlayerState.LOADING;  // 当前播放状态

    private String mCurrentPlayVideoURL;    // 当前播放的URL

    private int mSeekPos;                   // 记录切换硬解时的播放时间

    private boolean mChangeHWAcceleration;  // 切换硬解后接收到第一个关键帧前的标记位

    public SuperPlayerImpl(Context context, TXCloudVideoView videoView) {
        initialize(context, videoView);
    }

    /**
     * 点播播放器事件回调
     */
    @Override
    public void onPlayEvent(TXVodPlayer player, int event, Bundle param) {
        if (event != TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            String playEventLog = "TXVodPlayer onPlayEvent event: " + event + ", " + param.getString(TXLiveConstants.EVT_DESCRIPTION);
            TXCLog.d(TAG, playEventLog);
        }
        switch (event) {
            case TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED://视频播放开始
                updatePlayerState(SuperPlayerDef.PlayerState.PLAYING);
                break;
            case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME:
                if (mChangeHWAcceleration) { //切换软硬解码器后，重新seek位置
                    TXCLog.i(TAG, "seek pos:" + mSeekPos);
                    seek(mSeekPos);
                    mChangeHWAcceleration = false;
                }
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_END:
                updatePlayerState(SuperPlayerDef.PlayerState.END);
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS_MS); // 当前播放进度 毫秒
                int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION_MS); // 视频总时长 毫秒
                updatePlayProgress(progress / 1000, duration / 1000);
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN:
                updatePlayerState(SuperPlayerDef.PlayerState.PLAYING);
                break;
            default:
                break;
        }
        if (event < 0) {  // 播放点播文件失败
            mVodPlayer.stopPlay(true);
            updatePlayerState(SuperPlayerDef.PlayerState.PAUSE);
            onError(SuperPlayerCode.VOD_PLAY_FAIL, param.getString(TXLiveConstants.EVT_DESCRIPTION));
        }
    }

    /**
     * 点播播放器网络状态回调
     */
    @Override
    public void onNetStatus(TXVodPlayer player, Bundle bundle) {

    }

    private void initialize(Context context, TXCloudVideoView videoView) {
        mVideoView = videoView;
        initVodPlayer(context);
    }

    /**
     * 初始化点播播放器
     */
    private void initVodPlayer(Context context) {
        mVodPlayer = new TXVodPlayer(context);
        SuperPlayerGlobalConfig config = SuperPlayerGlobalConfig.getInstance();
        // 点播播放器配置
        TXVodPlayConfig mVodPlayConfig = new TXVodPlayConfig();

        File sdcardDir = context.getExternalFilesDir(null);
        if (sdcardDir != null) {
            mVodPlayConfig.setCacheFolderPath(sdcardDir.getPath() + "/txcache");
        }
        mVodPlayConfig.setMaxCacheItems(config.maxCacheItem);
        mVodPlayer.setConfig(mVodPlayConfig);
        mVodPlayer.setRenderMode(config.renderMode);
        mVodPlayer.setVodListener(this);
        mVodPlayer.enableHardwareDecode(config.enableHWAcceleration);
    }

    /**
     * 播放视频
     */
    public void playWithModel(SuperPlayerModel model) {
        mCurrentModel = model;
        stop();
        // 根据URL播放
            String videoURL = model.url; // 传统URL模式播放
            if (TextUtils.isEmpty(videoURL)) {
                onError(SuperPlayerCode.PLAY_URL_EMPTY, "播放视频失败，播放链接为空");
                return;
            }
            // 点播播放器：播放点播文件
            mVodPlayer.setPlayerView(mVideoView);
            playVodURL(videoURL);

            updatePlayProgress(0, 0);
    }

    /**
     * TODO 播放点播url
     */
    private void playVodURL(String url) {
        if (url == null || "".equals(url)) {
            return;
        }
        mCurrentPlayVideoURL = url;
        if (mVodPlayer != null) {
            mVodPlayer.setStartTime(0);
            mVodPlayer.setAutoPlay(true);
            mVodPlayer.setVodListener(this);
            String drmType = "plain";
            mVodPlayer.setToken(null);

            int ret;
            if (isVersionSupportAppendUrl()) {
                Uri uri = Uri.parse(url);
                String query = uri.getQuery();
                if(query==null || query.isEmpty()) {
                    query = "";
                } else {
                    query = query + "&";
                    if (query.contains("spfileid") || query.contains("spdrmtype") || query.contains("spappid")) {
                        TXCLog.e(TAG, "url contains superplay key. " + query);
                    }
                }
                query += "spfileid=" + "" + "&spdrmtype=" + drmType + "&spappid=" + "";
                Uri newUri = uri.buildUpon().query(query).build();
                TXCLog.i(TAG, "playVodURL: newurl = " + Uri.decode(newUri.toString()) + " ;url= " + url);
                ret = mVodPlayer.startPlay(Uri.decode(newUri.toString()));
            } else {
                ret = mVodPlayer.startPlay(url);
            }

            if (ret == 0) {
                updatePlayerState(SuperPlayerDef.PlayerState.PLAYING);
            }
        }
    }

    private boolean isVersionSupportAppendUrl() {
        String strVersion = TXLiveBase.getSDKVersionStr();
        String[] strVers = strVersion.split("\\.");
        if (strVers.length <= 1) {
            return false;
        }
        int majorVer;
        int minorVer;
        try{
            majorVer = Integer.parseInt(strVers[0]);
            minorVer = Integer.parseInt(strVers[1]);
        }
        catch (NumberFormatException e){
            TXCLog.e(TAG, "parse version failed.", e);
            majorVer = 0;
            minorVer = 0;
        }
        Log.i(TAG, strVersion + " , " + majorVer + " , " + minorVer);
        return majorVer > SUPPORT_MAJOR_VERSION || (majorVer == SUPPORT_MAJOR_VERSION && minorVer >= SUPPORT_MINOR_VERSION) ;
    }

    /**
     * 更新播放进度
     *
     * @param current  当前播放进度(秒)
     * @param duration 总时长(秒)
     */
    private void updatePlayProgress(long current, long duration) {
        if (mObserver != null) {
            mObserver.onPlayProgress(current, duration);
        }
    }

    /**
     * 更新播放状态
     */
    private void updatePlayerState(SuperPlayerDef.PlayerState playState) {
        mCurrentPlayState = playState;
        if (mObserver == null) {
            return;
        }
        switch (playState) {
            case PLAYING:
                mObserver.onPlayBegin(getPlayName());
                break;
            case PAUSE:
                mObserver.onPlayPause();
                break;
            case LOADING:
                mObserver.onPlayLoading();
                break;
            case END:
                mObserver.onPlayStop();
                break;
        }
    }

    private void onError(int code, String message) {
        if (mObserver != null) {
            mObserver.onError(code, message);
        }
    }

    private String getPlayName() {
        String title = "";
        if (mCurrentModel != null && !TextUtils.isEmpty(mCurrentModel.title)) {
            title = mCurrentModel.title;
        }
        return title;
    }

    @Override
    public void play(String url) {
        SuperPlayerModel model = new SuperPlayerModel();
        model.url = url;
        playWithModel(model);
    }

    @Override
    public void reStart() {
        playVodURL(mCurrentPlayVideoURL);
    }

    @Override
    public void pause() {
        mVodPlayer.pause();
        updatePlayerState(SuperPlayerDef.PlayerState.PAUSE);
    }

    @Override
    public void resume() {
        mVodPlayer.resume();
        updatePlayerState(SuperPlayerDef.PlayerState.PLAYING);
    }

    @Override
    public void stop() {
        if (mVodPlayer != null) {
            mVodPlayer.stopPlay(false);
        }
        mVideoView.removeVideoView();
        updatePlayerState(SuperPlayerDef.PlayerState.END);
    }

    @Override
    public void destroy() {
        mObserver = null;
    }

    @Override
    public void switchPlayMode(SuperPlayerDef.PlayerMode playerMode) {
        if (mCurrentPlayMode == playerMode) {
            return;
        }
        mCurrentPlayMode = playerMode;
    }

    /**
     * @param enable true 是硬件加速， false 是软解
     */
    @Override
    public void enableHardwareDecode(boolean enable) {
            mChangeHWAcceleration = true;
            mVodPlayer.enableHardwareDecode(enable);
            mSeekPos = (int) mVodPlayer.getCurrentPlaybackTime();
            TXCLog.i(TAG, "save pos:" + mSeekPos);
            stop();
        playVodURL(mCurrentModel.url);
    }

    @Override
    public void setPlayerView(TXCloudVideoView videoView) {
            mVodPlayer.setPlayerView(videoView);
    }

    @Override
    public void seek(int position) {
            if (mVodPlayer != null) {
                mVodPlayer.seek(position);
            }
        if (mObserver != null) {
            mObserver.onSeek(position);
        }
    }

    @Override
    public void setRate(float speedLevel) {
        mVodPlayer.setRate(speedLevel); // 倍速
    }

    @Override
    public void setMirror(boolean isMirror) {
        mVodPlayer.setMirror(isMirror);
    }

    @Override
    public String getPlayURL() {
        return mCurrentPlayVideoURL;
    }

    @Override
    public SuperPlayerDef.PlayerMode getPlayerMode() {
        return mCurrentPlayMode;
    }

    @Override
    public SuperPlayerDef.PlayerState getPlayerState() {
        return mCurrentPlayState;
    }

    @Override
    public void setObserver(SuperPlayerObserver observer) {
        mObserver = observer;
    }

    @Override
    public void setAudioPlayoutVolume(int volume) {
        if (mVodPlayer != null) {
            mVodPlayer.setAudioPlayoutVolume(volume);
        }
    }

}
