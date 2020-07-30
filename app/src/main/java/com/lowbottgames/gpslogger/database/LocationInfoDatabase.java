package com.lowbottgames.gpslogger.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {LocationInfo.class}, version = 1, exportSchema = false)
public abstract class LocationInfoDatabase extends RoomDatabase {

    public abstract LocationInfoDao locationInfoDao();

    private static volatile LocationInfoDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static LocationInfoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(
                            context.getApplicationContext(),
                            LocationInfoDatabase.class,
                            "location_database"
                    )
//                    .addCallback(roomDatabaseCallback)
                    .build();
        }
        return INSTANCE;
    }

//    private static RoomDatabase.Callback roomDatabaseCallback = new Callback() {
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//        }
//    };

}
