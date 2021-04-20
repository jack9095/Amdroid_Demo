package com.tencent.liteav.demo.player.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.tencent.liteav.demo.player.R;
import com.tencent.liteav.demo.player.expand.model.VideoDataMgr;
import com.tencent.liteav.demo.superplayer.CustomSuperPlayerView;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 点播播放器主Activity
 */
public class CustomSuperPlayerActivity extends Activity implements CustomSuperPlayerView.OnSuperPlayerViewCallback {

    private static final String TAG = "SuperPlayerActivity";
    //标题
    private RelativeLayout mLayoutTitle;
    //超级播放器View
    private CustomSuperPlayerView mSuperPlayerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_superplayer_activity_supervod_player);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        checkPermission();
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), 100);
            }
        }
    }

    private void initView() {
        mLayoutTitle = (RelativeLayout) findViewById(R.id.superplayer_rl_title);
        mSuperPlayerView = (CustomSuperPlayerView) findViewById(R.id.superVodPlayerView);

        SuperPlayerModel model = new SuperPlayerModel();
        model.title = "测试数据";
        model.url = "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4";
        mSuperPlayerView.playWithModel(model);

        mSuperPlayerView.setPlayerViewCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSuperPlayerView.getPlayerState() == SuperPlayerDef.PlayerState.PLAYING
                || mSuperPlayerView.getPlayerState() == SuperPlayerDef.PlayerState.PAUSE) {
            Log.i(TAG, "onResume state :" + mSuperPlayerView.getPlayerState());
            mSuperPlayerView.onResume();
            if (mSuperPlayerView.getPlayerMode() == SuperPlayerDef.PlayerMode.FLOAT) {
                mSuperPlayerView.switchPlayMode(SuperPlayerDef.PlayerMode.WINDOW);
            }
        }
        if (mSuperPlayerView.getPlayerMode() == SuperPlayerDef.PlayerMode.FULLSCREEN) {
            //隐藏虚拟按键，并且全屏
            View decorView = getWindow().getDecorView();
            if (decorView == null) return;
            if (Build.VERSION.SDK_INT >= 19) {
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause state :" + mSuperPlayerView.getPlayerState());
        if (mSuperPlayerView.getPlayerMode() != SuperPlayerDef.PlayerMode.FLOAT) {
            mSuperPlayerView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuperPlayerView.release();
        if (mSuperPlayerView.getPlayerMode() != SuperPlayerDef.PlayerMode.FLOAT) {
            // 退出播放
            mSuperPlayerView.resetPlayer();
        }
        VideoDataMgr.getInstance().setGetVideoInfoListListener(null);
    }

    @Override
    public void onStartFullScreenPlay() {
        // 隐藏其他元素实现全屏
        mLayoutTitle.setVisibility(View.GONE);
    }

    @Override
    public void onStopFullScreenPlay() {
        // 恢复原有元素
        mLayoutTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClickFloatCloseBtn() {
        // 点击悬浮窗关闭按钮，那么结束整个播放
        mSuperPlayerView.resetPlayer();
        finish();
    }

    @Override
    public void onClickSmallReturnBtn() {
        // 点击小窗模式下返回按钮，开始悬浮播放
    }

    @Override
    public void onStartFloatWindowPlay() {
        // 开始悬浮播放后，直接返回到桌面，进行悬浮播放
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
