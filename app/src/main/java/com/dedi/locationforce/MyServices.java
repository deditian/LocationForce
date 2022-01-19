package com.dedi.locationforce;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class MyServices extends Service {
    String TAG = "MyServices";
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 3000;
    private LocationManager mLocationManager;

    int LOCATION_REFRESH_TIME = 0; // 000 seconds to update
    int LOCATION_REFRESH_DISTANCE = 0; // meters to update

    @Override
    public void onCreate() {
        super.onCreate();
        runEveryTime();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeServices();
    }

    private void closeServices(){
        mLocationManager.removeUpdates(mLocationListener);
        mLocationManager = null;
        handler.removeCallbacks(runnable);
    }

    private void runEveryTime() {
        handler.postDelayed(runnable = () -> {
            handler.postDelayed(runnable, delay);
            Heartbeat();
            Log.w(TAG, "Heartbeat Running");
        }, delay);
    }

    private void Heartbeat() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            String getLocation = "onLocationChanged: lat :: "+location.getLatitude() + " | long :: "+location.getLongitude();
            Log.e(TAG, "onLocationChanged: lat :: "+location.getLatitude() + " | long :: "+location.getLongitude() );
            boolean isLocation =  (location.getLongitude() != 0.0 && location.getLatitude() != 0.0);
            if (isLocation){
                // send data and close service
                closeServices();
                Intent i = new Intent();
                i.putExtra("DATA", getLocation);
                i.setAction("FILTER");
                sendBroadcast(i);
                Log.e(TAG, "onLocationChanged: CLOSE");
            }
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };
}
