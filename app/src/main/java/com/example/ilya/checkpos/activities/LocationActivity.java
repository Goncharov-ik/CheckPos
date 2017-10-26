package com.example.ilya.checkpos.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilya.checkpos.R;
import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.filter.ibeacon.IBeaconFilter;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.SecureProfileListener;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.ISecureProfile;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ilya on 03.10.2017.
 */

public class LocationActivity extends AppCompatActivity{

    LinkedList<IBeaconDevice> beaconsList = new LinkedList<>();
    DataBaseHelper dbHelper;

    public static Intent createIntent(@NonNull Context context){
        return new Intent(context, LocationActivity.class);
    }

    public static final String TAG = "ProximityManager";
    private ProximityManager proximityManager;
    private DataBaseWorker dbWorker;

    private TextView iBeaconID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);

        iBeaconID = (TextView) findViewById(R.id.textView);
        iBeaconID.setText(R.string.info_beacon_text);

        dbWorker = new DataBaseWorker(this);

        //Initialize and configure proximity manager
        setupProximityManager();
        //setupFilters();
        startScanning();
    }

    private void setupProximityManager() {
        proximityManager = ProximityManagerFactory.create(this);

        //Configure proximity manager basic options
        proximityManager.configuration()
                //Using ranging for continuous scanning or MONITORING for scanning with intervals
                .scanPeriod(ScanPeriod.RANGING)
                //Using BALANCED for best performance/battery ratio
                .scanMode(ScanMode.BALANCED)
                //OnDeviceUpdate callback will be received with 100 milliseconds interval
                .deviceUpdateCallbackInterval(TimeUnit.MILLISECONDS.toMillis(10));

        //Setting up iBeacon listener
        proximityManager.setIBeaconListener(createIBeaconListener());
    }

    // Setup filters
    /*private void setupFilters(){
        //Setup sample iBeacon filter that only allows iBeacons with major and minor lower or equal 100.
        proximityManager.filters().iBeaconFilter(new IBeaconFilter() {
            @Override
            public boolean apply(IBeaconDevice iBeacon) {

                return iBeacon.getDistance() <= 1.0;
            }
        });
    }*/

    private IBeaconListener createIBeaconListener() {
        return new IBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice iBeacon, IBeaconRegion region) {
                dbHelper = new DataBaseHelper(getBaseContext());
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Log.i(TAG, "onIBeaconDiscovered: " + iBeacon.toString());
                beaconsList.add(iBeacon);
                Double beaconRssi = getNearestBeacon().getDistance();
                dbWorker.getPlace(getNearestBeacon().getUniqueId());
                dbWorker.getCoordinates(getNearestBeacon().getUniqueId());
                iBeaconID.setText(getNearestBeacon().getUniqueId()+' '+beaconRssi.toString()+" meters");
            }

            @Override
            public void onIBeaconsUpdated(List<IBeaconDevice> iBeacons, IBeaconRegion region) {
                Log.i(TAG, "onIBeaconsUpdated: " + iBeacons.size());

                beaconsList.clear();

                for(int i=0; i<iBeacons.size(); i++){
                    beaconsList.add(iBeacons.get(i));

                }
                Double beaconRssi = getNearestBeacon().getDistance();
                dbWorker.getPlace(getNearestBeacon().getUniqueId());
                dbWorker.getCoordinates(getNearestBeacon().getUniqueId());
                iBeaconID.setText(getNearestBeacon().getUniqueId()+' '+beaconRssi.toString()+" meters");
            }

            @Override
            public void onIBeaconLost(IBeaconDevice iBeacon, IBeaconRegion region) {
                Log.e(TAG, "onIBeaconLost: " + iBeacon.toString());
                beaconsList.remove(iBeacon);
            }
        };
    }

    private void startScanning() {
        //Connect to scanning service and start scanning when ready
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                //Check if proximity manager is already scanning
                if (proximityManager.isScanning()) {
                    Toast.makeText(LocationActivity.this, "Already scanning", Toast.LENGTH_SHORT).show();
                    return;
                }
                proximityManager.startScanning();
                Toast.makeText(LocationActivity.this, "Scanning started", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private IBeaconDevice getNearestBeacon(){
        double minDistance = beaconsList.get(0).getDistance();
        int index = 0;

        if(beaconsList.size() == 1){
            return beaconsList.get(index);
        }

        for(int i = 1; i<beaconsList.size(); i++){
            if(beaconsList.get(i).getDistance() < minDistance){
                minDistance = beaconsList.get(i).getDistance();
                index = i;
            }
        }

        return beaconsList.get(index);
    };

    @Override
    protected void onDestroy() {
        //Remember to disconnect when finished.
        proximityManager.disconnect();
        super.onDestroy();
    }
}
