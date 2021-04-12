package com.kuanquan.demo.copy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import com.kuanquan.demo.ServerInstance;
import com.plutinosoft.platinum.ServerParams;

/**
 * Created by huzongyao on 2018/6/7.
 * The service that manage the server instance
 * 管理服务器实例的服务
 */
public class DLNAServiceCopy extends Service {

    public static final String EXTRA_SERVER_PARAMS = "EXTRA_SERVER_PARAMS";

    private static final String TAG = "DLNAService";
    /**
     * 组播功能
     * 在Manifest文件中加入：android.permission.CHANGE_WIFI_MULTICAST_STATE，这个权限
     * 获取到MulticastLock对象，这个对象不能直接实例化，要通过WifiManager间接得到，工厂模式
     * 调用MulticastLock对象的acquire方法，获取到组播锁
     * 相应的，用完组播，为了不浪费电力，要调用MulticastLock的release方法释放锁
     */
    private WifiManager.MulticastLock mMulticastLock;
//    private Notification mNotification;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        acquireMulticastLock();
        buildNotification();
//        EventBus.getDefault().register(this);
    }

    private void buildNotification() {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        mNotification = NotificationHelper.INSTANCE
//                .getNotification(intent, getString(R.string.server_notification_title),
//                        getString(R.string.server_notification_text));
    }

    private void acquireMulticastLock() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            mMulticastLock = wifiManager.createMulticastLock(TAG);
            mMulticastLock.acquire();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            ServerParams params = intent.getParcelableExtra(EXTRA_SERVER_PARAMS);
            if (params != null) {
                Log.e("DLNAService", "开启服务");
                ServerInstance.INSTANCE.start(params);
//                NotificationHelper.INSTANCE.notify(mNotification);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

//    @SuppressWarnings("UnusedDeclaration")
//    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
//    public void onServerStateChange(@NotNull NativeAsyncEvent event) {
//        switch (event.type) {
//            case CallbackTypes.CALLBACK_EVENT_ON_PLAY:
//                Log.e("DLNAService", event.mediaInfo.toString());
//                Log.e("MediaUtils", "跳转视频播放页面");
//                Intent intent = new Intent();
//                intent.setClass(this, VideoActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("EXTRA_MEDIA_INFO", event.mediaInfo);
//                startActivity(intent);
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    public void onDestroy() {
        if (mMulticastLock != null) {
            mMulticastLock.release();
            mMulticastLock = null;
        }
//        EventBus.getDefault().unregister(this);
        ServerInstance.INSTANCE.stop();
//        NotificationHelper.INSTANCE.cancel();
        super.onDestroy();
    }
}
