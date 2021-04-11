package com.kuanquan.demo;

import android.content.Intent;
import android.util.Log;

import com.kuanquan.demo.event.NativeAsyncEvent;
import com.plutinosoft.platinum.CallbackTypes;
import com.plutinosoft.platinum.DLNACallback;

//import org.greenrobot.eventbus.EventBus;

/**
 * Created by huzongyao on 2018/6/21.
 * 接收 native 传递上来的消息
 * 枚举单例
 */
public enum CallbackInstance {

    INSTANCE;

    String TAG = "CallbackInstance";

    // c++ 层检测到的投屏数据回掉类
    private final DLNACallback mCallback;

    CallbackInstance() {
        mCallback = (type, param1, param2, param3) -> {
            Log.d(TAG, "type: " + type
                    + "\nparam1: " + param1
                    + "\nparam2: " + param2
                    + "\nparam3: " + param3);
            switch (type) {
                case CallbackTypes.CALLBACK_EVENT_ON_PLAY:
                    Log.e("CallbackInstance", "开启 startAsync");
                    startPlayMedia(type, param1, param2);
                    break;
                case CallbackTypes.CALLBACK_EVENT_ON_PAUSE:
                    break;
                case CallbackTypes.CALLBACK_EVENT_ON_SEEK:
                    break;
            }
        };
    }

    private void startPlayMedia(int type, String url, String meta) {
        MediaInfo mediaInfo = DLNAUtils.getMediaInfo(url, meta);
        if (mediaInfo.mediaType == MediaType.TYPE_UNKNOWN) {
            Log.e(TAG, "Media Type Unknown!");
            return;
        }
        NativeAsyncEvent event = new NativeAsyncEvent(type, mediaInfo);
        Log.e("CallbackInstance", "开启 startAsync  -- 》 发送EventBus");
//        EventBus.getDefault().post(event);
        Log.e("DLNAService", event.mediaInfo.toString());
        Log.e("MediaUtils", "跳转视频播放页面");
        Intent intent = new Intent();
        intent.setClass(WorkApplication.currentActivity, VideoActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXTRA_MEDIA_INFO", event.mediaInfo);
        WorkApplication.currentActivity.startActivity(intent);
    }

    public DLNACallback getCallback() {
        return mCallback;
    }
}
