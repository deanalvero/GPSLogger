package com.lowbottgames.gpslogger;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.lowbottgames.gpslogger.adapter.LocationRecyclerViewAdapter;
import com.lowbottgames.gpslogger.db.GPSData;
import com.lowbottgames.gpslogger.event.LocationEvent;
import com.lowbottgames.gpslogger.service.GLLocationService;
import com.lowbottgames.gpslogger.utils.GLDbHelper;
import com.lowbottgames.gpslogger.utils.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationRecyclerViewAdapter.LocationRecyclerViewAdapterListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FloatingActionButton mFloatingActionButton;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private LocationRecyclerViewAdapter mLocationRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupOnCreate();
    }

    private void refreshLocationList() {
        List<GPSData> gpsDataList = GLDbHelper.loadAllGPSData(this);
        Collections.reverse(gpsDataList);
        getLocationRecyclerViewAdapter().setItemList(gpsDataList);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setupOnCreate() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        setSupportActionBar(mToolbar);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedFloatingActionButton();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(getLocationRecyclerViewAdapter());

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                refreshLocationList();
            }
        });
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
            mFloatingActionButton.setImageResource(R.drawable.ic_stop_white_24dp);
            mFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorStop)));
        } else {
            mFloatingActionButton.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            mFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPlay)));
        }
    }

    private boolean isServiceRunning(){
        return Utility.isServiceRunning(this, GLLocationService.class);
    }

    private LocationRecyclerViewAdapter getLocationRecyclerViewAdapter(){
        if (this.mLocationRecyclerViewAdapter == null){
            this.mLocationRecyclerViewAdapter = new LocationRecyclerViewAdapter();
            this.mLocationRecyclerViewAdapter.setLocationRecyclerViewAdapterListener(this);
        }
        return this.mLocationRecyclerViewAdapter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

        refreshServiceStatus();
        refreshLocationList();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void onEvent(LocationEvent event){
        refreshLocationList();
    }

    @Override
    public void onClickGPSDataItem(GPSData itemObject) {
        startActivity(ItemActivity.newItemActivityIntent(this, itemObject));
    }
}