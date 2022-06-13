package com.example.navigation.My;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

public class My_Network {
    private Context context;

    private LatLng now_position;

    private String commandStr = LocationManager.NETWORK_PROVIDER;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 100;
    private int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private LocationListener locationListenerNetwork;
    private LocationManager locationManager;
    public My_Network(Context con){
        context = con;
    }
    public void set_listener(){
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
            set_listener();
        } else {
            //設定監聽如果有新位置時所做的事情
            locationListenerNetwork = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull android.location.Location location) {
                    LocationChange(location);
                }
            };


            //取得系統服務(GPS)
            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

            //if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //    commandStr = LocationManager.GPS_PROVIDER;
            //}


            GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
                @Override
                public void onGpsStatusChanged(int event) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    GpsStatus status = locationManager.getGpsStatus(null); //取當前狀態
                    switch (event) {
                        case GpsStatus.GPS_EVENT_STARTED:
                            System.out.println("GPS_EVENT_STARTED");
                        case GpsStatus.GPS_EVENT_STOPPED:
                            System.out.println("GPS_EVENT_STOPPED");
                        case GpsStatus.GPS_EVENT_FIRST_FIX:
                            System.out.println("GPS_EVENT_FIRST_FIX");
                        case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                            System.out.println("GPS_EVENT_SATELLITE_STATUS");
                            int maxSatellites = status.getMaxSatellites();
                            //my_data.GPS = String.valueOf(maxSatellites);
                    }
                }
            };
            locationManager.addGpsStatusListener(gpsListener);

            //設定更新速度與距離
            locationManager.requestLocationUpdates(
                    commandStr,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);
            //如果沒移動(或是沒有網路)取得先前位置，如果沒有上面刷新，就會是手機最後定位的位置
            Location location = locationManager.getLastKnownLocation(commandStr);
//            if(location==null){
//                System.out.println("使用網路定位");
//                commandStr = LocationManager.NETWORK_PROVIDER;
//                location = locationManager.getLastKnownLocation(commandStr);
//            }
            //取得經緯度
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            now_position = new LatLng(lat, lng);
        }
    }
    private void LocationChange(Location location){
        now_position = new LatLng(location.getLatitude(), location.getLongitude());
        String msg = "New Position from Network:" + now_position;
        System.out.println(msg);
    }
}
