package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GPSLoggerGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir");

    public static void main(String[] args){
        Schema schema = new Schema(1, "com.lowbottgames.gpslogger.db");
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema, PROJECT_DIR + "/app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void addTables(Schema schema) {
        Entity gpsData = schema.addEntity("GPSData");
        gpsData.addIdProperty().primaryKey().autoincrement();

        gpsData.addLongProperty("time");
        gpsData.addDoubleProperty("latitude");
        gpsData.addDoubleProperty("longitude");
        gpsData.addStringProperty("provider");
        gpsData.addFloatProperty("accuracy");
        gpsData.addFloatProperty("bearing");
        gpsData.addFloatProperty("speed");
        gpsData.addDoubleProperty("altitude");
    }
}
