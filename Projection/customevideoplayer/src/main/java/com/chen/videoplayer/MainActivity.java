package com.chen.videoplayer;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.chen.videoplayer.player.VideoPlayer;
import com.chen.videoplayer.player.VideoPlayerController;
import com.chen.videoplayer.player.VideoPlayerManager;

public class MainActivity extends AppCompatActivity {

    private VideoPlayer videoPlayer;
    private VideoPlayerController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoPlayer = (VideoPlayer) findViewById(R.id.vp);
        String url3 = "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4";
        String url4 = "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4";
        videoPlayer.setPlayUri(url3);
//        videoPlayer.setPlayUri("http://upos-sz-mirrorks3.bilivideo.com/upgcxcode/69/66/264566669/264566669_nb2-1-32.flv?e=ig8euxZM2rNcNbujhwdVhoMa7bdVhwdEto8g5X10ugNcXBlqNCNEto8g5gNvNE3DN0B5tZlqNxTEto8BTrNvN05fqx6S5ahE9IMvXBvEuENvNCImNEVEua6m2jIxux0CkF6s2JZv5x0DQJZY2F8SkXKE9IB5QK==&ua=tvproj&uipk=5&nbs=1&deadline=1618464723&gen=playurl&os=ks3bv&oi=3729269626&trid=f8e16e286cf04a82a0ed74ee3dba0e9cT&upsig=7e64278ddc2b3632ceb9285f69b5fb1c&uparams=e,ua,uipk,nbs,deadline,gen,os,oi,trid&mid=0&orderid=0,3&logo=80000000&nva_ext=%7B%22ver%22%3A1%2C%22content%22%3A%7B%22aid%22%3A970616022%2C%22cid%22%3A264566669%2C%22isOpen%22%3Atrue%7D%7D");
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
