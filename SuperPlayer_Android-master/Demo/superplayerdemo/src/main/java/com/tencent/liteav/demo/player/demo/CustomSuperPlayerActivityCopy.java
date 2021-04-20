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
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerGlobalConfig;
import com.tencent.liteav.demo.superplayer.SuperPlayerModel;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.rtmp.TXLiveConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 点播播放器主Activity  保留了 悬浮窗播放
 */
public class CustomSuperPlayerActivityCopy extends Activity implements View.OnClickListener,
        SuperPlayerView.OnSuperPlayerViewCallback {

    private static final String TAG = "SuperPlayerActivity";
    //标题
    private RelativeLayout mLayoutTitle;
    //超级播放器View
    private SuperPlayerView mSuperPlayerView;

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
        findViewById(R.id.superplayer_iv_back).setOnClickListener(this);
        mSuperPlayerView = (SuperPlayerView) findViewById(R.id.superVodPlayerView);

        SuperPlayerModel model = new SuperPlayerModel();
        model.title = "测试数据";
        model.url = "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4";
        mSuperPlayerView.playWithModel(model);

        mSuperPlayerView.setPlayerViewCallback(this);

        initSuperVodGlobalSetting();
    }

    /**
     * 初始化超级播放器全局配置
     */
    private void initSuperVodGlobalSetting() {
        SuperPlayerGlobalConfig prefs = SuperPlayerGlobalConfig.getInstance();
        // 开启悬浮窗播放
        prefs.enableFloatWindow = true;
        // 设置悬浮窗的初始位置和宽高
        SuperPlayerGlobalConfig.TXRect rect = new SuperPlayerGlobalConfig.TXRect();
        rect.x = 0;
        rect.y = 0;
        rect.width = 810;
        rect.height = 540;
        prefs.floatViewRect = rect;
        // 播放器默认缓存个数
        prefs.maxCacheItem = 5;
        // 设置播放器渲染模式
        prefs.enableHWAcceleration = true;
        prefs.renderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
        //需要修改为自己的时移域名
        prefs.playShiftDomain = "liteavapp.timeshift.qcloud.com";
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
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.superplayer_iv_back) {    // 悬浮窗播放
            showFloatWindow();
        }
    }

    /**
     * 悬浮窗播放
     */
    private void showFloatWindow() {
        if (mSuperPlayerView.getPlayerState() == SuperPlayerDef.PlayerState.PLAYING) {
            mSuperPlayerView.switchPlayMode(SuperPlayerDef.PlayerMode.FLOAT);
        } else {
            mSuperPlayerView.resetPlayer();
            finish();
        }
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
        showFloatWindow();
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
