package com.example.ilya.checkpos.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.ilya.checkpos.BeaconInfoAdapter;
import com.example.ilya.checkpos.R;

import java.util.ArrayList;

public class BeaconListActivity extends AppCompatActivity {
    private DataBaseHelper dbHelper;

    public static Intent createIntent(@NonNull Context context){
        return new Intent(context, BeaconListActivity.class);
    }

    ListView listView;

    final ArrayList<BeaconInfo> beaconData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_list);
        listView = (ListView) findViewById(R.id.listView);


        dbHelper = new DataBaseHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DataBaseHelper.TABLE_BEACONINFO,null,null,null,null,null,null);
        database.close();

        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            beaconData.add(new BeaconInfo(cursor,this));
            cursor.moveToNext();
        }

        Log.d("mLog","Array: " + beaconData.size());

        final BeaconInfoAdapter beaconAdapter = new BeaconInfoAdapter(this, beaconData);
        listView.setAdapter(beaconAdapter);
    }
}
