package com.hanyi.mapsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class EventInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
//Type object = (Type) bundle.getSerializable("KEY");
        String value = (String) bundle.getSerializable("KEY");

        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
    }
}
