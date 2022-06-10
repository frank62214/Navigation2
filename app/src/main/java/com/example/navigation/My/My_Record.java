package com.example.navigation.My;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import com.example.navigation.R;

public class My_Record implements Runnable{

    private My_Location my_location;
    GoogleMap mMap;
    private CameraPosition cameraPosition;
    private float bearing;

    private LatLng start;
    private LatLng end;
    My_Calcuate my_calcuate = new My_Calcuate();

    My_Data my_data;

    public My_Record(GoogleMap map){
        mMap = map;
    }
    public My_Record(My_Location location){
        my_location = location;
    }

    @Override
    public void run() {
        while (true){
            double lat = my_data.poly_list.get(0).latitude;
            double lng = my_data.poly_list.get(0).longitude;
            start = my_location.now_position;
            end = new LatLng(lat, lng);
            //bearing = my_calcuate.count_bearing(start, end);
            //change_camera(start, bearing);


            //for (int i = 1; i < my_data.poly_list.size(); i++) {
            start = end;
            int i = 0;
            int count = 1;
            while(i<my_data.poly_list.size()){
                //start = end;

                lat = my_data.poly_list.get(count).latitude;
                lng = my_data.poly_list.get(count).longitude;
                end = new LatLng(lat, lng);
                bearing = my_data.bearing;
                change_camera(start, bearing);
                SystemClock.sleep(1000);
                double dis = my_calcuate.camera_dis_cal(start, end);
                //System.out.println(dis);
                my_location.Toast(Double.toString(dis));
                if(dis<10){
                    i++;
                    count++;

                }
                //i++;
            }
            change_camera(end, bearing);
        }
    }
    private void change_camera(LatLng position, float bearing){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                my_location.change_camera_novigation(position, 20, bearing, 30);
                //my_location.scrollBy();
            }
        });
    }


}
