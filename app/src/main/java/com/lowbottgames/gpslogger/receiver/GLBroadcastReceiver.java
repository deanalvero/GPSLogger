package com.lowbottgames.gpslogger.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.lowbottgames.gpslogger.database.LocationInfo;
import com.lowbottgames.gpslogger.database.LocationInfoRepository;

public class GLBroadcastReceiver extends BroadcastReceiver {

    public static final String EXTRA_LOCATION = "LOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        String text = "Location";

        if (intent.hasExtra(EXTRA_LOCATION)){
            Location location = intent.getParcelableExtra(EXTRA_LOCATION);

            if (location != null) {
                text = location.toString();
                LocationInfoRepository repository = new LocationInfoRepository(context);
                LocationInfo locationInfo = new LocationInfo(
                    0,
                    location.getTime(),
                    location.getLatitude(),
                    location.getLongitude(),
                    location.getProvider(),
                    location.getAccuracy(),
                    location.getBearing(),
                    location.getSpeed(),
                    location.getAltitude()
                );
                repository.insert(locationInfo);
            }

        }
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
