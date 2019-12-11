package com.hanyi.mapsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class MapViewActivity extends AppCompatActivity {
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    private TextView locationLabel;
    private LocationManager locationManager;
    private MapView mapView;
    private GoogleMap googleMap;
    private DataProvider dataProvider;
    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyBBuLO0MI2rg0vwTomfq6_O-MOfE21uD0Y";

    private HashMap<Integer, String> markerTags;

    private void initBottomNavigationView() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_search) {
                    dataProvider.getAlarmInfo(new IDataCallback<AlarmInfo>() {
                        @Override
                        public void onComplete(AlarmInfo result) {
                            Toast.makeText(getApplicationContext(), result.content, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(Exception exception) {

                        }
                    });
                    return true;
                }
                return false;
            }
        });
    }

    private void initDrawCircle() {
        markerTags = new HashMap<>();

        Button drawCircleButton = findViewById(R.id.drawCircleButton);
        drawCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng loc = new LatLng(25.033710, 121.564718);

                int tagNumber = 1111;
                markerTags.put(tagNumber, "aaaa");
                MarkerOptions pin = new MarkerOptions().position(loc).title("you are here");
                Marker marker = googleMap.addMarker(pin);
                marker.setTag(tagNumber);
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        int tagNumber = (int)marker.getTag();
                        String value = markerTags.get(tagNumber);

                        // EventInfoActivity
                        Intent intent = new Intent(MapViewActivity.this, EventInfoActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("KEY", value);
                        intent.putExtras(bundle);

                        startActivity(intent);
                        return false;
                    }
                });


                Circle circle = googleMap.addCircle(new CircleOptions()
                    .center(loc)
                    .radius(10000)
                    .strokeColor(Color.RED)
                    .fillColor(Color.argb(128, 0, 0, 255))
                );

                //circle.remove();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        locationLabel = findViewById(R.id.locationLabel);
        initMapView(savedInstanceState);
        initLocationManager();
        initGetLocationButton();
        dataProvider = DataProvider.getInstance(getApplicationContext());

        // test
        initDrawCircle();
        initBottomNavigationView();

        // test
        Gson g = new Gson();
        System.err.println(g.toJson(User.getLoggedInUser()));
    }

    private void initMapView(Bundle savedInstanceState) {
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap_) {
                googleMap = googleMap_;
                googleMap.setMinZoomPreference(12);
                LatLng center = new LatLng(25.033710, 121.564718);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(center));
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void updateUserLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        locationLabel.setText("lat: " + String.format("%.2f", latitude) + ", long: " + String.format("%.2f", longitude));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
        LatLng loc = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(loc).title("you are here"));
    }

    private void initGetLocationButton() {
        Button getLocationButton = findViewById(R.id.getLocationButton);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    updateUserLocation(lastKnownLocation);
                } catch (SecurityException e) {
                    locationLabel.setText("permission rejected");
                }
            }
        });
    }

    private void initLocationManager() {
        ArrayList<String> permissions = new ArrayList();
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
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // updateUserLocation(location);
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
        }
    }
}
