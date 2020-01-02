package com.hanyi.mapsapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {
    private Spinner spinnerSignalType, spinnerSignalLevel;
    private Location location;
    private static final int ALL_PERMISSIONS_RESULT = 1011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        initLocationManager();

        final EditText editTextPeopleNum, editTextMessage;
        editTextPeopleNum = findViewById(R.id.editTextPeopleNum);
        editTextMessage = findViewById(R.id.editTextMessage);

        spinnerSignalType = findViewById(R.id.spinnerSignalType);
        spinnerSignalLevel = findViewById(R.id.spinnerSignalLevel);
        setSpinnerSignalType();
        setSpinnerSignalLevel();

        Button buttonPostSubmit;
        buttonPostSubmit = findViewById(R.id.buttonPostSubmit);
        buttonPostSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String peopleNum = editTextPeopleNum.getText().toString();
                String message = editTextMessage.getText().toString();

                if (!peopleNum.isEmpty() && !message.isEmpty() &&
                        spinnerSignalType.getSelectedItem() != null &&
                        spinnerSignalLevel.getSelectedItem() != null) {
                    int signalType = spinnerSignalType.getSelectedItemPosition() + 1;
                    int signalLevel = spinnerSignalLevel.getSelectedItemPosition() + 1;
                    DataProvider.getInstance(getApplicationContext()).postEmergencyPost(
                            location.getLatitude(),
                            location.getLongitude(),
                            signalType,
                            signalLevel,
                            message,
                            Integer.valueOf(peopleNum),
                            new IDataCallback<Boolean>() {
                                @Override
                                public void onComplete(Boolean result) {
                                    if (result) {
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failed to post.", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailed(Exception exception) {
                                    Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                } else if (peopleNum.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "People number cannot be empty", Toast.LENGTH_LONG).show();
                } else if (message.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Message cannot be empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initLocationManager() {
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        boolean granted = true;
        final String usedPermissions[] = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        for (String permission : usedPermissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission)) {
                granted = false;
                break;
            }
        }

        if (granted) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            });
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    private void setSpinnerSignalType() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.signal_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSignalType.setAdapter(adapter);
    }

    private void setSpinnerSignalLevel() {
        ArrayAdapter<String> adapter;
        String[] spinnerItemList = {"1", "2", "3", "4"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSignalLevel.setAdapter(adapter);
    }
}
