package com.lowbottgames.gpslogger.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "location_info_table")
public class LocationInfo {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long time;
    private double latitude;
    private double longitude;

    @NonNull
    private String provider;

    private float accuracy;
    private float bearing;
    private float speed;
    private double altitude;

    public LocationInfo(
            int id,
            long time,
            double latitude,
            double longitude,
            @NonNull String provider,
            float accuracy,
            float bearing,
            float speed,
            double altitude
    ) {
        this.id = id;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.provider = provider;
        this.accuracy = accuracy;
        this.bearing = bearing;
        this.speed = speed;
        this.altitude = altitude;
    }

    public int getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @NonNull
    public String getProvider() {
        return provider;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public float getBearing() {
        return bearing;
    }

    public float getSpeed() {
        return speed;
    }

    public double getAltitude() {
        return altitude;
    }

}
