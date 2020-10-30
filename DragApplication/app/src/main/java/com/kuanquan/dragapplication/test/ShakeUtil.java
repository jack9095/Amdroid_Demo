package com.kuanquan.dragapplication.test;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * 震动效果
 */
public class ShakeUtil {

    public static void vibrator(Context context, long duration) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(duration);
    }
}
