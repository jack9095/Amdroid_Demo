package com.kuanquan.demo.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.kuanquan.demo.MediaInfo;
//import com.kuanquan.demo.event.NativeAsyncEvent;
import com.plutinosoft.platinum.CallbackTypes;

//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by huzongyao on 2018/7/4.
 */
@SuppressLint("Registered")
public abstract class BasePlayActivity extends AppCompatActivity
        implements OnPreparedListener, OnCompletionListener,
        OnBufferUpdateListener {

    protected MediaInfo mMediaInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent(getIntent());
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
//        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);
        setCurrentMediaAndPlay();
    }

    private void parseIntent(Intent intent) {
        mMediaInfo = intent.getParcelableExtra("EXTRA_MEDIA_INFO");
    }

//    @SuppressWarnings("UnusedDeclaration")
//    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
//    public void onServerStateChange(NativeAsyncEvent event) {
//        switch (event.type) {
//            case CallbackTypes.CALLBACK_EVENT_ON_PAUSE:
//                onMediaPause();
//                break;
//            case CallbackTypes.CALLBACK_EVENT_ON_PLAY:
//                break;
//            case CallbackTypes.CALLBACK_EVENT_ON_SET_VOLUME:
//                Log.e("TAG", "" + event.param1);
//                break;
//            default:
//                break;
//        }
//    }

    protected void onMediaPause() {
    }

    @Override
    public void onBufferingUpdate(int percent) {
    }

    @Override
    public void onCompletion() {
    }

    @Override
    public void onPrepared() {
    }

    /**
     * Set current media source and start to play
     */
    protected abstract void setCurrentMediaAndPlay();
}
