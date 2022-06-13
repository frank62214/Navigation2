package com.example.navigation.My;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class My_Navigation implements Runnable {

    private My_Location my_location;
    private My_Layout my_layout;
    private My_Calcuate my_calcuate = new My_Calcuate();
    private My_Data my_data = new My_Data();

    private ArrayList<LatLng> history_points = new ArrayList<LatLng>();

    private LatLng now_position;
    private float  now_bearing;
    private ArrayList<LatLng> points = new ArrayList<LatLng>();
    private double distance = 0;
    private String road;
    private String road_detail;
    private String turn;

    private My_GPS my_gps;

    public My_Navigation(My_Location location, My_Layout layout){
        my_location = location;
        my_gps = location.my_gps;
        my_layout = layout;

        //my_location.Register_Sensor();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void run() {
        //init_camera();
        get_parameter();
        int count = 1;
        while (true){
            refresh_parameter();
            distance = my_calcuate.camera_dis_cal(now_position, points.get(count - 1));
            if(points.size()>count) {
                road = my_data.Road.get(count);
                road_detail = my_data.Road_Detail.get(count);
                Set_Turn_Pic(road_detail);
                if (distance < 15) {
                    count++;
                }
                if (my_data.Record_status) {
                    System.out.println("Record Start");
                    history_points.add(now_position);
                } else {
                    System.out.println("Record End");
                    history_points.removeAll(history_points);
                    clear_history_points();
                }
            }
            else{
                road = "即將抵達目的地";
                road_detail = "";
            }
            get_main_thread();
            SystemClock.sleep(1000);
        }
    }

    private void init_camera(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                System.out.println("FYBR");
                my_location.change_camera_novigation(now_position, 20, 0, 30);
            }
        });
    }

    private void get_main_thread(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                //my_location.change_camera_novigation();
                //my_location.scrollBy();
                System.out.println("現在位置" + now_position);
                my_location.change_camera_novigation(now_position,20,now_bearing,30);
                my_location.Draw_History_Point(history_points);
                my_layout.setNextRoadText(road);
                my_layout.setNextRoadDetailText(road_detail);
                my_layout.setNowPosition(now_position.toString());
                my_layout.setNowSatellite(my_data.GPS);
                String d = "距離: " + String.valueOf(distance);
                my_layout.setNextDis(d);
                my_layout.setNowLocationStatus();
                //my_location.Toast(distance);
            }
        });
    }
    private void clear_history_points(){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                my_location.remove_history_polyline();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void Set_Turn_Pic(String text){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                my_layout.Set_Turn_Pic(text);
            }
        });
    }
    private void get_parameter(){
        points = my_data.steps;
    }
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void refresh_parameter(){
        //my_location.Get_Location();
        //now_position = my_location.now_position;
        my_data.Satellites_number = my_gps.getSatellites_number();
        if(my_data.Satellites_number>10){
            now_position = my_gps.getNow_position();
        }else{
            now_position = my_location.now_position;
        }
        //now_position = new LatLng(my_location.now_location.getLatitude(), my_location.now_location.getLongitude());
        now_bearing  = my_data.bearing;
    }

}
