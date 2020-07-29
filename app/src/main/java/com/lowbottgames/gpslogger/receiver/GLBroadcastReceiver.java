package com.lowbottgames.gpslogger.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.lowbottgames.gpslogger.event.LocationEvent;
import com.lowbottgames.gpslogger.utils.GLDbHelper;

import org.greenrobot.eventbus.EventBus;

public class GLBroadcastReceiver extends BroadcastReceiver {

    public static final String EXTRA_LOCATION = "LOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        String text = "Location";

        if (intent.hasExtra(EXTRA_LOCATION)){
            Location location = intent.getParcelableExtra(EXTRA_LOCATION);
            text = location.toString();

            long ID = GLDbHelper.insertOrUpdate(context, location);
        }
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();

        EventBus.getDefault().post(new LocationEvent());
    }



}
