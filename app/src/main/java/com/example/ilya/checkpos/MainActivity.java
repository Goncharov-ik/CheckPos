package com.example.ilya.checkpos;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ilya.checkpos.activities.DbInterface;
import com.example.ilya.checkpos.activities.LocationActivity;
import com.kontakt.sdk.android.common.KontaktSDK;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int REQUEST_CODE_PERMISSIONS = 100;
    public static final int REQUEST_ENABLE_BT= 110;

    private LinearLayout buttonsLayout;
    private Button findMyLocationButton;
    private Button openDbInterfaceButton;

    private BluetoothAdapter bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        KontaktSDK.initialize(this);
        setContentView(R.layout.activity_main);

        setupButtons();

        checkPermissions();

        // Включаем Bluetooth
        bluetooth = BluetoothAdapter.getDefaultAdapter();

        if (!bluetooth.isEnabled()) {
            // Bluetooth выключен. Предложим пользователю включить его.
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void setupButtons(){
        buttonsLayout = (LinearLayout) findViewById(R.id.btnsLayoutOnMainActivity);
        //Кнопка поиска местоположения
        findMyLocationButton = (Button) findViewById(R.id.btnFindLocation);
        findMyLocationButton.setOnClickListener(this);
        //Кнопка вызова интерфейса для работы с базой
        openDbInterfaceButton = (Button) findViewById(R.id.btnOpenDbInterface);
        openDbInterfaceButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnFindLocation:
                startActivity(LocationActivity.createIntent(this));
                break;
            case R.id.btnOpenDbInterface:
                startActivity(DbInterface.createIntent(this));
                break;
            default:
                break;
        }
    }

    private void disableButtons() {
        for (int i = 0; i < buttonsLayout.getChildCount(); i++) {
            buttonsLayout.getChildAt(i).setEnabled(false);
        }
    }

    //Since Android Marshmallow starting a Bluetooth Low Energy scan requires permission from location group.
    private void checkPermissions() {
        int checkSelfPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (PackageManager.PERMISSION_GRANTED != checkSelfPermissionResult) {
            //Permission not granted so we ask for it. Results are handled in onRequestPermissionsResult() callback.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSIONS);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_CODE_PERMISSIONS == requestCode) {
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
            }
        } else {
            disableButtons();
            Toast.makeText(this, "Location permissions are mandatory to use BLE features on Android 6.0 or higher", Toast.LENGTH_LONG).show();
        }
    }
}
