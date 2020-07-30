package com.lowbottgames.gpslogger.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.lowbottgames.gpslogger.receiver.GLBroadcastReceiver;

public class GLLocationService extends Service {

    private static final String TAG = GLLocationService.class.getSimpleName();
    private LocationManager locationManager;
    private static final long LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = Float.MIN_VALUE; //10f;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class GLLocationListener implements LocationListener {

        Location lastLocation;

        public GLLocationListener(String s) {
            Log.e(TAG, "GLLocationListener " + s);
            lastLocation = new Location(s);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            lastLocation.set(location);

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

    LocationListener[] locationListeners = new LocationListener[]{
            new GLLocationListener(LocationManager.GPS_PROVIDER),
            new GLLocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand()");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager locationManager = getLocationManager();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListeners[0]);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListeners[1]);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");

        if (getLocationManager() != null){
            int lengthLocationListeners = locationListeners.length;

            for (int i = 0; i < lengthLocationListeners; i++){
                try {
                    getLocationManager().removeUpdates(locationListeners[i]);
                } catch (Exception e){
                    Log.e(TAG, "e removeUpdates " + i);
                }
            }
        }
        super.onDestroy();
    }

    private LocationManager getLocationManager(){
        if (locationManager == null){
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        }
        return locationManager;
    }
}
