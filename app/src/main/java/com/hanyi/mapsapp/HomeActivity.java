package com.hanyi.mapsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageButton toExploreButton = findViewById(R.id.toExploreButton);
        ImageButton toFindFriendsButton = findViewById(R.id.toFindFriendsButton);
        ImageButton toMapViewButton = findViewById(R.id.toMapViewButton);


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
    }
}
