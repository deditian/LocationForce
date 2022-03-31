package com.dedi.locationforce;

import static com.dedi.locationforce.LocationHelper.init;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private PeriodicWorkRequest mPeriodicWorkRequest;
    LocationHelper locationHelper;
    boolean isInitLocation;
    boolean isHeartbeat;

    TextView showLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showLocation = (TextView) findViewById(R.id.txtStartForce);
        Button button = (Button) findViewById(R.id.button);
        locationHelper = new LocationHelper();

//        new ServiceHelper().getInstance(MainActivity.this);
//        new MyServices().isInitial(true);
//        new ServiceHelper().startServiceLocation(MainActivity.this);

        button.setOnClickListener(view -> {
            showLocation.setText("wait gps");
            isInitLocation = true;
            init = true;
            new ServiceHelper().runWorker(MainActivity.this,4 );
//            locationHelper.Heartbeat(this);
//            locationHelper.runForce(this);
//            showLocation.setText("location");
//            new MyServices().isInitial(false);
//            new ServiceHelper().runWorker(MainActivity.this);

        });



    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str = (String) intent.getExtras().get("DATA").toString();
            if (str != null){
                showLocation.setText(str);
                Log.e(TAG, "onReceive: isInitLocation :: "+isInitLocation );
                if (isInitLocation){
                    isInitLocation = false;
                    init = false;
//                    locationHelper.closeServices(true);// close services locationManager
                    WorkManager.getInstance(MainActivity.this).cancelAllWorkByTag("periodicWorkInit");
                    Log.e(TAG, "onReceive: CLOSE ");
                    isHeartbeat = true;
//                    new ServiceHelper().runWorker(MainActivity.this,5 ); // run service locationManager heartbeat with workManager
                }
//                if (isHeartbeat){
//                    new ServiceHelper().runWorker(MainActivity.this,10 );
//                }
            }else {
                Log.e(TAG, "onReceive: NULL");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filt = new IntentFilter("FILTER");
        this.registerReceiver(br, filt);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

}