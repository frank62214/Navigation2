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
    private My_Location my_location;
    private My_Data my_data = new My_Data();

    private LatLng now_position;

    private String commandStr = LocationManager.NETWORK_PROVIDER;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 100;
    private int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private LocationListener locationListenerNetwork;
    private LocationManager locationManager;
    public My_Network(Context con, My_Location location){
        context = con;
        my_location = location;
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

            //設定更新速度與距離
            locationManager.requestLocationUpdates(
                    commandStr,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);
            //如果沒移動(或是沒有網路)取得先前位置，如果沒有上面刷新，就會是手機最後定位的位置
            Location location = locationManager.getLastKnownLocation(commandStr);
            //取得經緯度
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            now_position = new LatLng(lat, lng);
            my_data.now_position = now_position;
            my_data.Origin = now_position;
            //first time add now marker and move camera
            my_location.change_camera(now_position,15);
            my_location.add_Now_Mark(now_position);
        }
    }
    private void LocationChange(Location location){
        if(my_data.Select_Resource.equals("Network")) {
            now_position = new LatLng(location.getLatitude(), location.getLongitude());
            String msg = "New Position from Network:" + now_position;
            System.out.println(msg);
            my_location.set_now_position(now_position);
            my_location.change_camera();
            my_location.change_mark();
            my_data.Origin = now_position;
        }
    }
//    public void getLocation(final onLocationChangeCallBack callBack){
//        callBack.LocationChange(now_position);
//    }
//
//    interface onLocationChangeCallBack{
//        void LocationChange(LatLng position);
//    }
}
