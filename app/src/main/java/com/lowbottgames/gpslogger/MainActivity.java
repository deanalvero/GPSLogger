package com.lowbottgames.gpslogger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lowbottgames.gpslogger.adapter.LocationRecyclerViewAdapter;
import com.lowbottgames.gpslogger.database.LocationInfo;
import com.lowbottgames.gpslogger.service.GLLocationService;
import com.lowbottgames.gpslogger.utils.Utils;
import com.lowbottgames.gpslogger.viewmodel.LocationInfoViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationRecyclerViewAdapter.LocationRecyclerViewAdapterListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 10;

    private FloatingActionButton floatingActionButton;
    private LocationRecyclerViewAdapter locationRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        locationRecyclerViewAdapter = new LocationRecyclerViewAdapter();
        locationRecyclerViewAdapter.setLocationRecyclerViewAdapterListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(locationRecyclerViewAdapter);

        LocationInfoViewModel locationInfoViewModel = new ViewModelProvider(this).get(LocationInfoViewModel.class);
        locationInfoViewModel.getLocationInfos().observe(this, new Observer<List<LocationInfo>>() {
            @Override
            public void onChanged(List<LocationInfo> locationInfos) {
                locationRecyclerViewAdapter.setItemList(locationInfos);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedFloatingActionButton();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    REQUEST_CODE_PERMISSION_LOCATION
            );
        }
    }

    private void clickedFloatingActionButton() {
        if (isServiceRunning()){
            stopService(new Intent(MainActivity.this, GLLocationService.class));
        } else {
            startService(new Intent(MainActivity.this, GLLocationService.class));
        }
        refreshServiceStatus();
    }

    private void refreshServiceStatus(){
        if (isServiceRunning()){
            floatingActionButton.setImageResource(R.drawable.ic_stop_white_24dp);
            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorStop)));
        } else {
            floatingActionButton.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPlay)));
        }
    }

    private boolean isServiceRunning(){
        return Utils.isServiceRunning(this, GLLocationService.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshServiceStatus();
    }

    @Override
    public void onClickLocationInfo(LocationInfo itemObject) {
        startActivity(ItemActivity.newItemActivityIntent(this, itemObject));
    }

}