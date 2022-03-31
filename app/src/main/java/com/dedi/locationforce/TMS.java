package com.dedi.locationforce;

import android.app.ActivityManager;
import android.content.Context;

public abstract class TMS {
    private static Context context;

    public TMS(Context context){
        this.context = context;
    }

    public static boolean isServiceAlive(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
