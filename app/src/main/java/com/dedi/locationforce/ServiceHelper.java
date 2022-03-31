package com.dedi.locationforce;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.dedi.locationforce.TMS.isServiceAlive;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class ServiceHelper {

    public ServiceHelper(){}

//    public void startServiceLocation(Context context) {
//        if (!isServiceAlive(MyPeriodicWork.class, context)) {
//            Log.e("TAG COY", "startServiceLocation: ");
//            context.startService(serviceIntent(context));
//        }
//    }

    public void runWorker(Context context, Integer time){
        // This is PeriodicWorkRequest it repeats every time.
        PeriodicWorkRequest mPeriodicWorkRequest;
        if (time <= 5){
            mPeriodicWorkRequest = new PeriodicWorkRequest.Builder(MyPeriodicWork.class,
                    time, TimeUnit.SECONDS)
                    .addTag("periodicWorkInit")
                    .build();
        }else {
            mPeriodicWorkRequest = new PeriodicWorkRequest.Builder(MyPeriodicWork.class,
                    time, TimeUnit.MINUTES)
                    .addTag("periodicWorkRequest")
                    .build();
        }
        WorkManager.getInstance(context).enqueue(mPeriodicWorkRequest);
    }


//    private Intent serviceIntent(Context context) {
//        return new Intent(context, MyPeriodicWork.class);
//    }
//
//    private boolean isServiceAlive(Class<?> serviceClass,Context context) {
//        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                Log.e("TAG coy", "isServiceAlive: run" );
//                return true;
//            }
//        }
//        Log.e("TAG coy", "isServiceAlive: NOT run" );
//        return false;
//    }
}
