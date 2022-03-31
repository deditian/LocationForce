package com.dedi.locationforce;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class LocationHelper {
    private Context _context;
    String TAG = "LocationHelper";
    int LOCATION_REFRESH_TIME = 5*1000; // 000 seconds to update
    int LOCATION_REFRESH_DISTANCE = 0; // meters to update
    private LocationManager mLocationManager;
    public static boolean init;

    public LocationHelper (){}

//    public void closeServices(boolean init){
//        Log.e(TAG, "closeServices: " +init );
//        this.init = init;
//
//    }

    public void Heartbeat(Context context) {
        this._context = context;
        mLocationManager = (LocationManager) context.getApplicationContext().getSystemService(context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        boolean isGPSEnabled     = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled || isNetworkEnabled){
            if (isGPSEnabled){
                Log.i(TAG, "hasGPS");
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                        LOCATION_REFRESH_DISTANCE, mLocationListener);

            }
            if (isNetworkEnabled){
                Log.i(TAG, "hasNetwork");
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_REFRESH_TIME,
                        LOCATION_REFRESH_DISTANCE, mLocationListener);

            }
        }else {
            Log.e(TAG, "Heartbeat: PLEASE ENABLE LOCATION PERMISSION" );
        }

    }
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            String getLocation = "onLocationChanged: lat :: "+location.getLatitude() + " | long :: "+location.getLongitude();
            Log.e(TAG, "onLocationChanged: lat :: "+location.getLatitude() + " | long :: "+location.getLongitude() );
            boolean isLocation =  (location.getLongitude() != 0.0 && location.getLatitude() != 0.0);
            if (isLocation){
                // send data
                Log.e(TAG, "onLocationChanged:  INIT :: " +init );
                if (init){
                    if(mLocationManager != null){
                        mLocationManager.removeUpdates(mLocationListener);
                        mLocationManager = null;
                    }
                }
                Intent i = new Intent();
                i.putExtra("DATA", getLocation);
                i.setAction("FILTER");
                _context.sendBroadcast(i);
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
