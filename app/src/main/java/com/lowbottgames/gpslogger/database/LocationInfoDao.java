package com.lowbottgames.gpslogger.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LocationInfo locationInfo);

    @Query("SELECT * from location_info_table ORDER BY id DESC")
    LiveData<List<LocationInfo>> getLocationInfos();

}
