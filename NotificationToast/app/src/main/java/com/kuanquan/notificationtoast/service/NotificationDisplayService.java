package com.kuanquan.notificationtoast.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.kuanquan.notificationtoast.MainActivity;
import com.kuanquan.notificationtoast.R;

public class NotificationDisplayService extends Service {

    final int NOTIFICATION_ID = 16;
    public NotificationDisplayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        displayNotification("Test-Notification", "Dies ist eine Test-Notification mit einem seeehr langen Text, den man ausklappen muss.");
        stopSelf(); // Beendet den Service nach dem Ausführen des Codes (nachträglich ergänzt)
        return super.onStartCommand(intent, flags, startId);
    }

    private void displayNotification(String title, String text){

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setLargeIcon(BitmapFactory.decodeResource(R.drawable.xyz))
                .setColor(getResources().getColor(R.color.colorAccent))
                .setVibrate(new long[]{0, 300, 300, 300})
                //.setSound()
                .setLights(Color.WHITE, 1000, 5000)
                //.setWhen(System.currentTimeMillis())
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification.build());
    }
}