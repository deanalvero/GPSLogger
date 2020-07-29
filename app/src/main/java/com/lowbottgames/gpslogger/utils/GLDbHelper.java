package com.lowbottgames.gpslogger.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.lowbottgames.gpslogger.db.DaoMaster;
import com.lowbottgames.gpslogger.db.DaoSession;
import com.lowbottgames.gpslogger.db.GPSData;

import java.util.List;

public class GLDbHelper {
    public static SQLiteDatabase getWritableDatabase(Context context){
        return getHelper(context).getWritableDatabase();
    }

    public static SQLiteDatabase getReadableDatabase(Context context){
        return getHelper(context).getReadableDatabase();
    }

    public static DaoMaster.DevOpenHelper getHelper(Context context){
        return new DaoMaster.DevOpenHelper(context, "gpsdata-db", null);
    }

    public static long insertOrUpdate(Context context, GPSData gpsData){
        long ID = -1;
        SQLiteDatabase db = getWritableDatabase(context);
        db.beginTransaction();

        try {
            DaoSession daoSession = new DaoMaster(db).newSession();
            ID = daoSession.getGPSDataDao().insertOrReplace(gpsData);
            db.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return ID;
    }

    public static List<GPSData> loadAllGPSData(Context context){
        List<GPSData> gpsDataList = null;

        SQLiteDatabase db = getReadableDatabase(context);
        db.beginTransaction();

        try {
            DaoSession daoSession = new DaoMaster(db).newSession();
            gpsDataList = daoSession.getGPSDataDao().loadAll();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        return gpsDataList;
    }

    public static long insertOrUpdate(Context context, Location location){
        return insertOrUpdate(context, getGPSData(location));
    }

    public static GPSData getGPSData(Location location){
        GPSData gpsData = new GPSData();

        gpsData.setTime(location.getTime());
        gpsData.setLatitude(location.getLatitude());
        gpsData.setLongitude(location.getLongitude());
        gpsData.setProvider(location.getProvider());
        gpsData.setAccuracy(location.getAccuracy());
        gpsData.setBearing(location.getBearing());
        gpsData.setSpeed(location.getSpeed());
        gpsData.setAltitude(location.getAltitude());

        return gpsData;
    }
}
