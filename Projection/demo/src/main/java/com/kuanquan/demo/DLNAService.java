package com.kuanquan.demo;

import android.app.Service;
import android.content.*;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import com.plutinosoft.platinum.ServerParams;

/**
 * The service that manage the server instance
 * 管理服务器实例的服务
 * TODO 2
 */
public class DLNAService extends Service {

    public static final String EXTRA_SERVER_PARAMS = "EXTRA_SERVER_PARAMS";

    private static final String TAG = "DLNAService";
    private WifiManager.MulticastLock mMulticastLock;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        acquireMulticastLock();
        buildNotification();
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

    @Override
    public void onDestroy() {
        if (mMulticastLock != null) {
            mMulticastLock.release();
            mMulticastLock = null;
        }
        ServerInstance.INSTANCE.stop();
//        NotificationHelper.INSTANCE.cancel();
        super.onDestroy();
    }
}
