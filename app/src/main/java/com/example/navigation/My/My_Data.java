package com.example.navigation.My;

import android.content.Context;
import android.media.Image;
import android.widget.ImageButton;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class My_Data {

    //GPS
    public static String GPS;
    public static int count;
    public static boolean Refresh_Satellites = false;
    public static boolean Record_status = false;
    public static int Satellites_number;
    public static String Select_Resource = "Network";
    public static LatLng now_position = null;

    //Search JSON Tag
    public static ArrayList<String> Result   = new ArrayList<String>();
    public static ArrayList<String> Geometry = new ArrayList<String>();
    public static ArrayList<String> Location = new ArrayList<String>();
    public static ArrayList<String> Lat      = new ArrayList<String>();
    public static ArrayList<String> Lng      = new ArrayList<String>();
    public static ArrayList<String> Data     = new ArrayList<String>();
    public static ArrayList<String> overview = new ArrayList<String>();

    //Storage two location
    public static LatLng Origin           = new LatLng(0,0);
    public static LatLng Direction           = new LatLng(0,0);
    public static ArrayList<LatLng> poly_list = new ArrayList<LatLng>();  // storge overview poly point
    public static ArrayList<LatLng> steps     = new ArrayList<LatLng>(); //儲存轉彎處


    public static LatLng camera_position;
    public static float bearing = 0;
    public static boolean novigation_status;
    public static ArrayList<String> Road        = new ArrayList<String>();
    public static ArrayList<String> Road_Detail = new ArrayList<String>();


    //UI init
    public static int page_number = 0;
    public static String page_name = "";
    public static ImageButton Navigation_button;
    public static String Direction_text;
    public static String Drive_Mode="driving";

    //UI visiable
    public static boolean RelativeLayout_Search_v = false;
    public static boolean ScrollView_Result_v = false;
    public static boolean Navigation_button_v = true;
    public static boolean introduce_v = false;

    //Activity choose
    public static boolean goBackToDesktop_choose = false;

    public static int speed;

    //public My_Data(){
    //
    //}
    //public My_Data(Context context, ){
    //    Navigation_button
    //}
    public static void DataClear(){
        Data.clear();
    }
}
