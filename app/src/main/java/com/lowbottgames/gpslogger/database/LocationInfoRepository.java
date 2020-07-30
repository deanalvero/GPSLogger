package com.lowbottgames.gpslogger.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LocationInfoRepository {

    private LocationInfoDao locationInfoDao;
    private LiveData<List<LocationInfo>> locationInfos;

    public LocationInfoRepository(Context context) {
        LocationInfoDatabase database = LocationInfoDatabase.getDatabase(context);
        locationInfoDao = database.locationInfoDao();
        locationInfos = locationInfoDao.getLocationInfos();
    }

    public LiveData<List<LocationInfo>> getLocationInfos() {
        return locationInfos;
    }

    public void insert(final LocationInfo locationInfo) {
        LocationInfoDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                locationInfoDao.insert(locationInfo);
            }
        });
    }

}
