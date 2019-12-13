package com.hanyi.mapsapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

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

import java.util.ArrayList;

public class MapViewActivity extends AppCompatActivity {
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    private LocationManager locationManager;
    private MapView mapView;
    private GoogleMap googleMap;
    private DataProvider dataProvider;
    private TextView cardTitle;
    private TextView cardContent;
    private CardView mapViewMessageCard;
    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyBBuLO0MI2rg0vwTomfq6_O-MOfE21uD0Y";

    private SparseArray<AlarmInfo> markerTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        dataProvider = DataProvider.getInstance(getApplicationContext());
        markerTags = new SparseArray<>();
        initCard();
        initMapView(savedInstanceState);
        initLocationManager();
    }

    private void initMapView(Bundle savedInstanceState) {
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap_) {
                googleMap = googleMap_;
                googleMap.setMinZoomPreference(5);

                try {
                    Location center = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    updateUserLocation(center);
                } catch (SecurityException e) {
                }
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
//        locationLabel.setText("lat: " + String.format("%.2f", latitude) + ", long: " + String.format("%.2f", longitude));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 5));
//        LatLng loc = new LatLng(latitude, longitude);
//        googleMap.addMarker(new MarkerOptions().position(loc).title("you are here"));
    }

//    private void initGetLocationButton() {
//        Button getLocationButton = findViewById(R.id.getLocationButton);
//        getLocationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    updateUserLocation(lastKnownLocation);
//                } catch (SecurityException e) {
//                    locationLabel.setText("permission rejected");
//                }
//            }
//        });
//    }

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
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
//                    updateUserLocation(location);
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

            putAlerts();
        }
    }

    private void putAlerts() {
        try {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                dataProvider.getAlarmInfo(
                    lastKnownLocation.getLatitude(),
                    lastKnownLocation.getLongitude(),
                    new IDataCallback<ArrayList<AlarmInfo>>() {
                        @Override
                        public void onComplete(ArrayList<AlarmInfo> alarmInfos) {
                            for (AlarmInfo alarmInfo : alarmInfos) {
                                drawCircle(alarmInfo);
                            }
                        }

                        @Override
                        public void onFailed(Exception exception) {
                        }
                    }
                );
            }
        } catch (SecurityException e){
        }
    }

    private void initCard() {
        cardTitle = findViewById(R.id.cardTitle);
        cardContent = findViewById(R.id.cardContent);
        mapViewMessageCard = findViewById(R.id.mapViewMessageCard);
        Button exitCardButton = findViewById(R.id.exitCardButton);
        exitCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapViewMessageCard.setVisibility(View.GONE);
            }
        });
    }

    private void drawCircle(AlarmInfo alarmInfo) {
        LatLng loc = new LatLng(alarmInfo.latitude, alarmInfo.longitude);
        MarkerOptions pin = new MarkerOptions().position(loc);//.title("you are here");
        Marker marker = googleMap.addMarker(pin);

        markerTags.put(alarmInfo.hashCode(), alarmInfo);
        marker.setTag(alarmInfo.hashCode());
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int tag = (int)marker.getTag();
                try {
                    AlarmInfo alarmInfo_ = markerTags.get(tag);
                    cardTitle.setText(alarmInfo_.getSignalTypeName(getApplicationContext()));
                    cardContent.setText(alarmInfo_.content);
                    mapViewMessageCard.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    mapViewMessageCard.setVisibility(View.GONE);
                }
                return false;
            }
        });

        Circle circle = googleMap.addCircle(new CircleOptions()
            .center(loc)
            .radius(100000)
            .strokeColor(Color.argb(128, 0, 0, 0))
            .strokeWidth(5)
            .fillColor(alarmInfo.getSignalLevelColor())
        );
    }
}
