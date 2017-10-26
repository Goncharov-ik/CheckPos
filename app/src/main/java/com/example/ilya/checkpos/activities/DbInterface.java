package com.example.ilya.checkpos.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.ilya.checkpos.R;

/**
 * Created by ar_no on 24.10.2017.
 */

public class DbInterface extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnRead, btnClear, btnShow;
    EditText etUniqueId, etPlace, etXcoord, etYcoord;
    DataBaseHelper dbHelper;


    public static Intent createIntent(@NonNull Context context){
        return new Intent(context, DbInterface.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_interface);
        setupButtons();
    }

    private void setupButtons() {

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        btnShow = (Button) findViewById(R.id.btnShowAllBeacons);
        btnShow.setOnClickListener(this);

        etUniqueId = (EditText) findViewById(R.id.UniqueId);
        etPlace = (EditText) findViewById(R.id.Place);
        etXcoord = (EditText) findViewById(R.id.x_coordinate);
        etYcoord = (EditText) findViewById(R.id.y_coordinate);

        dbHelper = new DataBaseHelper(this);

    }

    private void clearEdText() {

        etUniqueId.setText("");
        etPlace.setText("");
        etXcoord.setText("");
        etYcoord.setText("");

    }

    @Override
    public void onClick(View view) {
        double Xcoord = 0;
        double Ycoord = 0;

        String UniqueId = etUniqueId.getText().toString();
        String Place = etPlace.getText().toString();

        String XcoordStr = etXcoord.getText().toString();

        if(XcoordStr == null || XcoordStr.isEmpty()) Xcoord = 0.0;
        else {

            Xcoord = Double.parseDouble(etXcoord.getText().toString());
        }

        String YcoordStr = etYcoord.getText().toString();

        if(YcoordStr == null || YcoordStr.isEmpty()) Ycoord = 0.0;
        else {

            Ycoord = Double.parseDouble(etYcoord.getText().toString());
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        switch (view.getId()) {

            case R.id.btnAdd:

                contentValues.put(DataBaseHelper.KEY_UNIQUEID, UniqueId);
                contentValues.put(DataBaseHelper.KEY_PLACE, Place);
                contentValues.put(DataBaseHelper.KEY_COORDx, Xcoord);
                contentValues.put(DataBaseHelper.KEY_COORDy, Ycoord);

                database.insert(DataBaseHelper.TABLE_BEACONINFO,null,contentValues);
                clearEdText();

                break;

            case R.id.btnRead:

                Cursor cursor = database.query(DataBaseHelper.TABLE_BEACONINFO,null,null,null,null,null,null);

                if (cursor.moveToFirst()) {
                    int IdIndex = cursor.getColumnIndex(DataBaseHelper.KEY_ID);
                    int UniqueIdIndex = cursor.getColumnIndex(DataBaseHelper.KEY_UNIQUEID);
                    int PlaceIndex = cursor.getColumnIndex(DataBaseHelper.KEY_PLACE);
                    int XcoordIndex = cursor.getColumnIndex(DataBaseHelper.KEY_COORDx);
                    int YcoordIndex = cursor.getColumnIndex(DataBaseHelper.KEY_COORDy);

                    do {

                        Log.d("mLog","ID = " + cursor.getInt(IdIndex) +
                                ", UniqueID = " + cursor.getString(UniqueIdIndex) +
                                ", Place = " + cursor.getString(PlaceIndex) +
                                ", Xcoord = " + cursor.getDouble(XcoordIndex) +
                                ", Ycoord = " + cursor.getDouble(YcoordIndex));

                    } while (cursor.moveToNext());

                } else
                    Log.d("mLog", "0 rows in Table");

                cursor.close();
                break;

            case R.id.btnClear:

                database.delete(DataBaseHelper.TABLE_BEACONINFO, null, null);
                break;

            case R.id.btnShowAllBeacons:

                startActivity(BeaconListActivity.createIntent(this));

                break;
        }
        dbHelper.close();
    }
}
