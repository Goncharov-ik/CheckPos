package com.example.ilya.checkpos.activities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ilya.checkpos.MainActivity;

/**
 * Created by ar_no on 26.10.2017.
 */

public class DataBaseWorker {

    private DataBaseHelper dataBaseHelper;
    private Context context;

    public DataBaseWorker(Context context) {
        this.context = context;
    }


    public String getPlace(String uniqueId) {
        String place;
        dataBaseHelper = new DataBaseHelper(this.context);
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("select " + dataBaseHelper.KEY_PLACE +  " from " + dataBaseHelper.TABLE_BEACONINFO +
                " where " + dataBaseHelper.KEY_UNIQUEID + " = ? ", new String[] { uniqueId.toUpperCase() });

        cursor.moveToNext();
        int PlaceIndex = cursor.getColumnIndex(DataBaseHelper.KEY_PLACE);
        place = cursor.getString(PlaceIndex);

        Log.d("mLog","Place: " + place);

        database.close();

        return place;
    }


    public double [] getCoordinates(String uniqueId) {
        dataBaseHelper = new DataBaseHelper(this.context);
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("select " + dataBaseHelper.KEY_COORDx + ", " + dataBaseHelper.KEY_COORDy  +
                " from " + dataBaseHelper.TABLE_BEACONINFO +
                " where " + dataBaseHelper.KEY_UNIQUEID + " = ? ", new String[] { uniqueId.toUpperCase() });

        cursor.moveToNext();
        int CoordIndex = cursor.getColumnIndex(DataBaseHelper.KEY_COORDx);
        int CoordIndey = cursor.getColumnIndex(DataBaseHelper.KEY_COORDy);

        double coordnates [] = {cursor.getDouble(CoordIndex),cursor.getDouble(CoordIndey)};

        Log.d("mLog","Place: " + coordnates[0] + "  " + coordnates[1]);

        database.close();

        return coordnates;
    }




}
