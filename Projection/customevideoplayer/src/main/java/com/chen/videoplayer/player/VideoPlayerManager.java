package com.chen.videoplayer.player;

/**
 * Created by Administrator on 2017/6/5.
 * 单例管理视频播放  在listview或者recyclerview中保证页面只有一个播放器在播放
 */
public class VideoPlayerManager {

    private VideoPlayer mVideoPlayer;

    private VideoPlayerManager(){}

    private static VideoPlayerManager sInstance;

    public static VideoPlayerManager getInstance(){

        if(sInstance == null){
            sInstance = new VideoPlayerManager();
        }

        return sInstance;
    }

    public void releaseMediaplayer(){
        if(mVideoPlayer != null){
            mVideoPlayer.release();
            mVideoPlayer = null;
        }
    }

    public void setCurrentVideoPlayer(VideoPlayer videoPlayer){
        mVideoPlayer = videoPlayer;
    }

    /**
     *  释放资源
     */
    public boolean onBackPress(){

        if(mVideoPlayer.isFullScreen()){
            mVideoPlayer.exitFullScreen();
            return true;
        }else if(mVideoPlayer.isTinyScreen()){
            mVideoPlayer.exitTinyScreen();  //退出小屏
            return true;
        }

        if(mVideoPlayer != null){
            mVideoPlayer.release();
        }

        return false;
    }
}
