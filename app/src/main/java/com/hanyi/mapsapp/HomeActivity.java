package com.hanyi.mapsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity {
    private static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageButton toExploreButton = findViewById(R.id.toExploreButton);
        ImageButton toFindFriendsButton = findViewById(R.id.toFindFriendsButton);
        ImageButton toMapViewButton = findViewById(R.id.toMapViewButton);
        ImageButton toCameraButton = findViewById(R.id.toCameraButton);
        ImageButton toEmergencyCallButton = findViewById(R.id.toEmergencyCallButton);


        toMapViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MapViewActivity.class);
                startActivity(intent);
            }
        });

        toFindFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchFriendsActivity.class);
                startActivity(intent);
            }
        });

        toExploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ExploreActivity.class);
                startActivity(intent);
            }
        });

        toCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        toEmergencyCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_PHONE_STATE) !=
                    PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(HomeActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }

                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:110"));
                try {
                    startActivity(phoneIntent);
                } catch (SecurityException e) {
                }
            }
        });
    }
}
