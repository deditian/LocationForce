package com.dedi.locationforce;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.Executor;

public class MyPeriodicWork extends Worker {

    private Context context;

    public MyPeriodicWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }


//    @NonNull
//    @Override
//    @WorkerThread
//    public Executor getBackgroundExecutor() {
//        return runnable -> {
//
//        };
//    }

    @NonNull
    @WorkerThread
    @Override
    public Result doWork() {
        Log.e("TAG COy","location work is retry");
        ContextCompat.getMainExecutor(context).execute(()  -> {
            // This is where your UI code goes.
            new LocationHelper().Heartbeat(context);
        });
        return Result.retry();
    }
}
