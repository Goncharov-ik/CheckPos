package com.example.ilya.checkpos.activities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ar_no on 26.10.2017.
 */

public class BeaconInfo {

    private String uniqueId;
    private String place;
    private double coordX;
    private double coordY;

    public BeaconInfo(Cursor cursor, Context context) {


        int IdIndex = cursor.getColumnIndex(DataBaseHelper.KEY_ID);
        int UniqueIdIndex = cursor.getColumnIndex(DataBaseHelper.KEY_UNIQUEID);
        int PlaceIndex = cursor.getColumnIndex(DataBaseHelper.KEY_PLACE);
        int XcoordIndex = cursor.getColumnIndex(DataBaseHelper.KEY_COORDx);
        int YcoordIndex = cursor.getColumnIndex(DataBaseHelper.KEY_COORDy);

        uniqueId = cursor.getString(UniqueIdIndex);
        place = cursor.getString(PlaceIndex);
        coordX = cursor.getDouble(XcoordIndex);
        coordY = cursor.getDouble(YcoordIndex);

    }


    public String getUniqueId() {
        return uniqueId;
    }

    public String getPlace() {
        return place;
    }

    public double getCoordX() {
        return coordX;
    }

    public double getCoordY() {
        return coordY;
    }
}
