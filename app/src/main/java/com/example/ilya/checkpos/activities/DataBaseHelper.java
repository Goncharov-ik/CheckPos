package com.example.ilya.checkpos.activities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;

/**
 * Created by ar_no on 22.10.2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "BeaconDb";
    public static final String TABLE_BEACONINFO = "BeaconInfo";

    public static final String KEY_ID = "_id";
    public static final String KEY_UNIQUEID = "unique_id";
    public static final String KEY_PLACE = "place";
    public static final String KEY_COORDx = "coord_x";
    public static final String KEY_COORDy = "coord_y";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_BEACONINFO + " (" +
                 KEY_ID + " integer primary key, "
                + KEY_UNIQUEID + " text, "  + KEY_PLACE + " text, "  +
                        KEY_COORDx + " real, " + KEY_COORDy + " real" + ")" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Сбросим старую таблицу и вызовем метод для создания новой с обновленными параметрами
        db.execSQL("drop table if exists " + TABLE_BEACONINFO);
        onCreate(db);

    }
}
