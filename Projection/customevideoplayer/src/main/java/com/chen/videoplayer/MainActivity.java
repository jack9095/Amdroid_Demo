package com.chen.videoplayer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chen.videoplayer.player.VideoPlayer;
import com.chen.videoplayer.player.VideoPlayerController;
import com.chen.videoplayer.player.VideoPlayerManager;

public class MainActivity extends AppCompatActivity {

    private VideoPlayer videoPlayer;
    private VideoPlayerController mController;

    private ImageView iv_first_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoPlayer = (VideoPlayer) findViewById(R.id.vp);
        videoPlayer.setPlayUri("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4");
        mController = new VideoPlayerController(this);
        mController.setTitle("办公室小野开番外了，居然在办公室开澡堂！老板还点赞？");
        mController.setImage("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg");
        videoPlayer.setController(mController);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mController.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mController.onRestart();
    }

    public void tinyWindowPlayer(View v){
        if(videoPlayer.isPlaying() || videoPlayer.isBufferingPlaying() || videoPlayer.isPaused() || videoPlayer.isBufferingPaused()){
            videoPlayer.enterTinyScreen();
        }else {
            Toast.makeText(this, "只有播放之后才能进入小屏播放", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

        if (VideoPlayerManager.getInstance().onBackPress()){
            return;
        }

        super.onBackPressed();
    }
}
