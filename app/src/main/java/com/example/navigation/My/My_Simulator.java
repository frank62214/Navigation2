package com.example.navigation.My;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.google.android.gms.maps.model.LatLng;

public class My_Simulator implements Runnable {
    My_Location my_location;
    My_Data my_data = new My_Data();
    private LatLng start;
    private LatLng end;
    My_Calcuate my_calcuate = new My_Calcuate();
    public My_Simulator(My_Location location){
        my_location = location;
    }
    @Override
    public void run() {
        double lat = my_data.poly_list.get(0).latitude;
        double lng = my_data.poly_list.get(0).longitude;
        start = my_location.now_position;
        end = new LatLng(lat, lng);
        float bearing = 0;
        //bearing = my_calcuate.count_bearing(start, end);
        //change_camera(start, bearing);


        //for (int i = 1; i < my_data.poly_list.size(); i++) {
        int i = 1;
        while(i<my_data.poly_list.size()){
            start = end;
            lat = my_data.poly_list.get(i).latitude;
            lng = my_data.poly_list.get(i).longitude;
            end = new LatLng(lat, lng);
            bearing = my_calcuate.count_bearing(start, end);
            SystemClock.sleep(3000);
            change_camera(start,bearing);

            i++;
        }
        change_camera(end, bearing);
    }
    private void change_camera(LatLng position, float bearing){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                my_location.change_camera_novigation(position, 21, bearing, 90);
                //my_location.scrollBy();
            }
        });
    }
}
