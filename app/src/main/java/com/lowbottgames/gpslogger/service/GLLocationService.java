package com.lowbottgames.gpslogger.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lowbottgames.gpslogger.receiver.GLBroadcastReceiver;

/**
 * Created by dean on 08/09/16.
 */
public class GLLocationService extends Service {

    private static final String TAG = GLLocationService.class.getSimpleName();
    private LocationManager mLocationManager;
    private static final long LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = Float.MIN_VALUE; //10f;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class GLLocationListener implements LocationListener {

        Location mLastLocation;

        public GLLocationListener(String s){
            Log.e(TAG, "GLLocationListener " + s);
            mLastLocation = new Location(s);
        }


        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);

            Intent intent = new Intent();
            intent.setAction("com.lowbottgames.gpslogger.LOCATION_CHANGED");
            intent.putExtra(GLBroadcastReceiver.EXTRA_LOCATION, location);
            sendBroadcast(intent);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.e(TAG, "onStatusChanged: " + s);
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.e(TAG, "onProviderEnabled: " + s);
        }

        @Override
        public void onProviderDisabled(String s) {

            Log.e(TAG, "onProviderDisabled: " + s);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new GLLocationListener(LocationManager.GPS_PROVIDER),
            new GLLocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand()");

//        return super.onStartCommand(intent, flags, startId);


//        return START_NOT_STICKY;
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e(TAG, "onCreate");


        try {
            getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[0]);
        } catch (Exception e){
            Log.e(TAG, "e requestLocationUpdates 0");
        }

        try {
            getLocationManager().requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[1]);
        } catch (Exception e){
            Log.e(TAG, "e requestLocationUpdates 1");
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");

        if (getLocationManager() != null){
            int lengthLocationListeners = mLocationListeners.length;

            for (int i = 0; i < lengthLocationListeners; i++){
                try {
                    getLocationManager().removeUpdates(mLocationListeners[i]);
                } catch (Exception e){
                    Log.e(TAG, "e removeUpdates " + i);
                }
            }
        }
        super.onDestroy();
    }

    private LocationManager getLocationManager(){
        if (mLocationManager == null){
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        }
        return mLocationManager;
    }
}
