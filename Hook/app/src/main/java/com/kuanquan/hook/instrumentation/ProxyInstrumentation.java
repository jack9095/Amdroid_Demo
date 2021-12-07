package com.kuanquan.hook.instrumentation;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Bundle;
import android.os.PersistableBundle;

public class ProxyInstrumentation extends Instrumentation {
    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        ActivityExtendKt.fixOrientation(activity);
        super.callActivityOnCreate(activity, icicle);
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
        ActivityExtendKt.fixOrientation(activity);
        super.callActivityOnCreate(activity, icicle, persistentState);
    }
}
