package com.lowbottgames.gpslogger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lowbottgames.gpslogger.database.LocationInfo;

public class ItemActivity extends AppCompatActivity {

    private TextView textView_id;
    private TextView textView_time;
    private TextView textView_latitude;
    private TextView textView_longitude;
    private TextView textView_provider;
    private TextView textView_accuracy;
    private TextView textView_bearing;
    private TextView textView_speed;
    private TextView textView_altitude;

    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_TIME = "EXTRA_TIME";
    private static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";
    private static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";
    private static final String EXTRA_PROVIDER = "EXTRA_PROVIDER";
    private static final String EXTRA_ACCURACY = "EXTRA_ACCURACY";
    private static final String EXTRA_BEARING = "EXTRA_BEARING";
    private static final String EXTRA_SPEED = "EXTRA_SPEED";
    private static final String EXTRA_ALTITUDE = "EXTRA_ALTITUDE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        textView_id = (TextView) findViewById(R.id.textView_id);
        textView_time = (TextView) findViewById(R.id.textView_time);
        textView_latitude = (TextView) findViewById(R.id.textView_latitude);
        textView_longitude = (TextView) findViewById(R.id.textView_longitude);
        textView_provider = (TextView) findViewById(R.id.textView_provider);
        textView_accuracy = (TextView) findViewById(R.id.textView_accuracy);
        textView_bearing = (TextView) findViewById(R.id.textView_bearing);
        textView_speed = (TextView) findViewById(R.id.textView_speed);
        textView_altitude = (TextView) findViewById(R.id.textView_altitude);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_item);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        loadData();
    }


    public static Intent newItemActivityIntent(Context context, LocationInfo locationInfo){
        Intent intent = new Intent(context, ItemActivity.class);

        intent.putExtra(EXTRA_ID, locationInfo.getId());
        intent.putExtra(EXTRA_TIME, locationInfo.getTime());
        intent.putExtra(EXTRA_LATITUDE, locationInfo.getLatitude());
        intent.putExtra(EXTRA_LONGITUDE, locationInfo.getLongitude());
        intent.putExtra(EXTRA_PROVIDER, locationInfo.getProvider());
        intent.putExtra(EXTRA_ACCURACY, locationInfo.getAccuracy());
        intent.putExtra(EXTRA_BEARING, locationInfo.getBearing());
        intent.putExtra(EXTRA_SPEED, locationInfo.getSpeed());
        intent.putExtra(EXTRA_ALTITUDE, locationInfo.getAltitude());

        return intent;
    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            textView_id.setText(getString(R.string.id_format, bundle.getLong(EXTRA_ID)));
            textView_time.setText(getString(R.string.time_format, bundle.getLong(EXTRA_TIME)));
            textView_latitude.setText(getString(R.string.latitude_format, bundle.getDouble(EXTRA_LATITUDE)));
            textView_longitude.setText(getString(R.string.longitude_format, bundle.getDouble(EXTRA_LONGITUDE)));
            textView_provider.setText(getString(R.string.provider_format, bundle.getString(EXTRA_PROVIDER)));
            textView_accuracy.setText(getString(R.string.accuracy_format, bundle.getFloat(EXTRA_ACCURACY)));
            textView_bearing.setText(getString(R.string.bearing_format, bundle.getFloat(EXTRA_BEARING)));
            textView_speed.setText(getString(R.string.speed_format, bundle.getFloat(EXTRA_SPEED)));
            textView_altitude.setText(getString(R.string.altitude_format, bundle.getDouble(EXTRA_ALTITUDE)));

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
