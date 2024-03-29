package com.kuanquan.projection.instance;

import android.util.Log;

import com.kuanquan.projection.event.NativeAsyncEvent;
import com.kuanquan.projection.media.MediaInfo;
import com.kuanquan.projection.media.MediaType;
import com.kuanquan.projection.utils.DLNAUtils;
import com.plutinosoft.platinum.CallbackTypes;
import com.plutinosoft.platinum.DLNACallback;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by huzongyao on 2018/6/21.
 * To receive the messages from native
 */

public enum CallbackInstance {

    INSTANCE;

    String TAG = "CallbackInstance";

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
        EventBus.getDefault().post(event);
    }

    public DLNACallback getCallback() {
        return mCallback;
    }
}
