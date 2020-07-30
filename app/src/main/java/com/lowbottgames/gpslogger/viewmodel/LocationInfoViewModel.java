package com.lowbottgames.gpslogger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lowbottgames.gpslogger.database.LocationInfo;
import com.lowbottgames.gpslogger.database.LocationInfoRepository;

import java.util.List;

public class LocationInfoViewModel extends AndroidViewModel {

    private LocationInfoRepository repository;
    private LiveData<List<LocationInfo>> locationInfos;

    public LocationInfoViewModel(@NonNull Application application) {
        super(application);
        repository = new LocationInfoRepository(application);
        locationInfos = repository.getLocationInfos();
    }

    public LiveData<List<LocationInfo>> getLocationInfos() {
        return locationInfos;
    }

    public void insert(LocationInfo locationInfo) {
        repository.insert(locationInfo);
    }

}
