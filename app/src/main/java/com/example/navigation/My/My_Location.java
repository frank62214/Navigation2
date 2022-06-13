package com.example.navigation.My;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.navigation.R;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.Executor;

public class My_Location {
    private Context context;
    public GoogleMap mMap;

    private ArrayList<LatLng> past_route = new ArrayList<LatLng>();
    private ArrayList<LatLng> points = new ArrayList<LatLng>();

    private String provider;
    private List<String> list;

    private String commandStr = LocationManager.NETWORK_PROVIDER;
    //private String commandStr = LocationManager.GPS_PROVIDER;
    private LocationManager locationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 100;
    private int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private LocationListener locationListenerGPS;
    private LocationListener locationListenerNetwork;
    private LocationCallback locationCallback;

    private FusedLocationProviderClient fusedLocationClient;

    private LocationRequest locationRequest;

    private boolean navigation = false;
    private int navigation_count = 0;
    private MarkerOptions now_marker = new MarkerOptions();
    private MarkerOptions destination_mark = new MarkerOptions();


    private CameraPosition cameraPosition;
    private Marker marker;
    private Marker destination;
    private Polyline polyline;
    private Polyline history_polyline;

    public LatLng now_position;
    public LatLng camera_position;
    public LatLng destination_position;

    public My_Data my_data;

    public GoogleMap.CancelableCallback callback;
    public GoogleMap.CancelableCallback record_callback;

    GroundOverlay UserOverlay;

    public My_GPS my_gps;
    public My_Network my_network;

    public Location now_location;

    //init
    @RequiresApi(api = Build.VERSION_CODES.N)
    public My_Location(Context Activity_context, GoogleMap Map) {
        context = Activity_context;
        mMap = Map;
        init_CallBack();
        init_Record_CallBack();
        my_gps = new My_GPS(context, this);
        my_network = new My_Network(context, this);

    }
    public void init_CallBack() {
        callback = new GoogleMap.CancelableCallback() {
            @Override
            public void onCancel() {
                System.out.println("FYBR1");
            }

            @Override
            public void onFinish() {
                System.out.println("FYBR2");
                //mMap.animateCamera(CameraUpdateFactory.scrollBy(0, -540), 1000, record_callback);
                mMap.animateCamera(CameraUpdateFactory.scrollBy(0, -540));
                //my_data.camera_position = mMap.getCameraPosition().target;
                //My_Record my_record = new My_Record(mMap);
                //Thread t = new Thread(my_record);
                //t.start();
            }
        };
    }

    public void init_Record_CallBack() {
        record_callback = new GoogleMap.CancelableCallback() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onFinish() {
                My_Record my_record = new My_Record(mMap);
                Thread t = new Thread(my_record);
                t.start();
            }
        };
    }

    public void set_now_position(LatLng point){
        now_position = point;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void set_location() {
        double lat;
        double lng;

        //------------------------------------------
        my_gps.get_gps();
        my_network.set_listener();



        //------------------------------------------


        //取得APP對手機服務的權限(GPS)
//        if (ActivityCompat.checkSelfPermission(context,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(context,
//                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((Activity) context,
//                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
//            set_location();
//        } else {
//            //設定監聽如果有新位置時所做的事情
//            locationListenerGPS = new LocationListener() {
//                @Override
//                public void onLocationChanged(@NonNull android.location.Location location) {
//                    LocationChange(location);
//                }
//            };
//
//
//            //取得系統服務(GPS)
//            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
//
//            //if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            //    commandStr = LocationManager.GPS_PROVIDER;
//            //}
//
//
//            GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
//                @Override
//                public void onGpsStatusChanged(int event) {
//                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//                    GpsStatus status = locationManager.getGpsStatus(null); //取當前狀態
//                    switch (event) {
//                        case GpsStatus.GPS_EVENT_STARTED:
//                            System.out.println("GPS_EVENT_STARTED");
//                        case GpsStatus.GPS_EVENT_STOPPED:
//                            System.out.println("GPS_EVENT_STOPPED");
//                        case GpsStatus.GPS_EVENT_FIRST_FIX:
//                            System.out.println("GPS_EVENT_FIRST_FIX");
//                        case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
//                            System.out.println("GPS_EVENT_SATELLITE_STATUS");
//                            int maxSatellites = status.getMaxSatellites();
//                            //my_data.GPS = String.valueOf(maxSatellites);
//                    }
//                }
//            };
//            locationManager.addGpsStatusListener(gpsListener);
//
//            //設定更新速度與距離
//            locationManager.requestLocationUpdates(
//                    commandStr,
//                    MIN_TIME_BW_UPDATES,
//                    MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerGPS);
//            //如果沒移動(或是沒有網路)取得先前位置，如果沒有上面刷新，就會是手機最後定位的位置
//            Location location = locationManager.getLastKnownLocation(commandStr);
////            if(location==null){
////                System.out.println("使用網路定位");
////                commandStr = LocationManager.NETWORK_PROVIDER;
////                location = locationManager.getLastKnownLocation(commandStr);
////            }
//            //取得經緯度
//            lat = location.getLatitude();
//            lng = location.getLongitude();
//            //change_map(new LatLng(lat, lng), 15);
//            //System.out.println(lat);
//            //System.out.println(lng);
//
//            now_position = new LatLng(lat, lng);
//            //my_data.Origin = now_position;
//            // Move the camera instantly to Sydney with a zoom of 15.
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(now_position));
//            //past_route.add(now_position);
//            change_map(now_position,
//                    15,
//                    mMap.getCameraPosition().bearing,
//                    mMap.getCameraPosition().tilt);
//        }
    }
    @RequiresApi(api = Build.VERSION_CODES.S)
    public void Get_Location() {
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            if (location != null) {
                now_position = new LatLng(location.getLatitude(), location.getLongitude());
                System.out.println("client Location: " + location.getLatitude() + ", " + location.getLongitude());
            }
        });
//        LocationRequest.Builder locationRequest = new LocationRequest.Builder(500);
//        locationRequest.setIntervalMillis(500);
//        LocationRequest locationRequest1 = locationRequest.build();
//        //locationRequest1.getIntervalMillis(500);
//
//        //LocationRequest locationRequest1 = locationRequest.build();
//        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
//        fusedLocationProviderClient.requestLocationUpdates(locationRequest1, locationCallback, Looper.myLooper());



        //locationRequest = LocationRequest()
        //        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        //        locationRequest.interval = 0
        //        locationRequest.fastestInterval = 0
        //        locationRequest.numUpdates = 1
        //        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        //        fusedLocationProviderClient!!.requestLocationUpdates(
        //            locationRequest, locationCallback, Looper.myLooper()
        //        )
    }
    public void LocationChange(Location location){
        //marker.remove();
        now_position = new LatLng(location.getLatitude(), location.getLongitude());
        my_data.Origin = now_position;
        //my_data.bearing = location.getBearing();
        //System.out.println(my_data.bearing);
        //my_data.camera_position = now_position;
        String msg = "New Location: " + now_position.latitude + ", " + now_position.longitude;
        System.out.println(msg);
        change_mark(now_position);
        //change_map(now_position);
    }
    public void add_Destination_Mark(LatLng point){
        destination_mark.position(point);
        //destination_mark.icon(BitmapFromVector(R.drawable.dir_destination));
        destination = mMap.addMarker(destination_mark);
        destination_position = point;
        points.clear();
    }
    public void add_Now_Mark(LatLng point){
        now_marker.icon(BitmapFromVector(R.drawable.ic_location));
        now_marker.position(point);
        marker = mMap.addMarker(now_marker);
    }
    public void change_map(LatLng point){
        change_mark(point);
        change_camera(point);
    }
    public void change_map(LatLng point, float zoom, float bearing, float tilt){
        change_mark(point);
        change_camera(point, zoom, bearing, tilt);
    }
    public void change_mark(){
        marker.setPosition(now_position);
    }
    public void change_mark(LatLng point){
        marker.setPosition(point);
    }
    public void change_camera(){
        cameraPosition = new CameraPosition.Builder()
                .target(now_position)
                .zoom(mMap.getCameraPosition().zoom)
                .bearing(mMap.getCameraPosition().bearing)
                .tilt(mMap.getCameraPosition().tilt)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    public void change_camera(LatLng point){
        cameraPosition = new CameraPosition.Builder()
                .target(point)
                .zoom(mMap.getCameraPosition().zoom)
                .bearing(mMap.getCameraPosition().bearing)
                .tilt(mMap.getCameraPosition().tilt)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    public void change_camera(LatLng point, float zoom){
        cameraPosition = new CameraPosition.Builder()
                .target(point)
                .zoom(zoom)
                .bearing(mMap.getCameraPosition().bearing)
                .tilt(mMap.getCameraPosition().tilt)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    public void change_camera(LatLng point, float zoom, float bearing, float tilt){
        cameraPosition = new CameraPosition.Builder()
                .target(point)
                .zoom(zoom)
                .bearing(bearing)
                .tilt(tilt)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    public void test_camera(){
        //my_data.camera_position = now_position;
//        cameraPosition = new CameraPosition.Builder()
//                .target(now_position)
//                .zoom(21)
//                .bearing(0)
//                .tilt(90)
//                .build();
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, callback);

        //LatLng newarkLatLng = new LatLng(40.714086, -74.228697);

//        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
//                .image(BitmapFromVector(R.drawable.mk_user_arrow))
//                .position(now_position, 15f, 15f)
//                .bearing(90);
//        mMap.addGroundOverlay(newarkMap);
//
//
//        My_Record my_record = new My_Record(mMap);
//        Thread t = new Thread(my_record);
//        t.start();

    }
    public void create_Overlay(){

        double lat = my_data.poly_list.get(0).latitude;
        double lng = my_data.poly_list.get(0).longitude;
        //start = my_location.now_position;
        LatLng point = new LatLng(lat, lng);
       float bearing = my_data.bearing;

        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapFromVector(R.drawable.mk_user_arrow))
                .position(point, 9f, 9f)
                .bearing(bearing);
        UserOverlay = mMap.addGroundOverlay(newarkMap);
    }
    public void change_camera_novigation(LatLng point){
        //remove_Overlay();
        cameraPosition = new CameraPosition.Builder()
                .target(point)
                .zoom(mMap.getCameraPosition().zoom)
                .bearing(mMap.getCameraPosition().bearing)
                .tilt(mMap.getCameraPosition().tilt)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
//                .image(BitmapFromVector(R.drawable.mk_user_arrow))
//                .position(point, 10f, 10f)
//                .bearing(bearing);
//        UserOverlay = mMap.addGroundOverlay(newarkMap);
        UserOverlay.setBearing(mMap.getCameraPosition().bearing);
        UserOverlay.setPosition(point);

        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, callback);

        //mMap.moveCamera(CameraUpdateFactory.scrollBy(0, -540));
    }
    public void change_camera_novigation(LatLng point, float zoom, float bearing, float tilt){
        //remove_Overlay();
        cameraPosition = new CameraPosition.Builder()
                .target(point)
                .zoom(zoom)
                .bearing(bearing)
                .tilt(tilt)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
//                .image(BitmapFromVector(R.drawable.mk_user_arrow))
//                .position(point, 10f, 10f)
//                .bearing(bearing);
//        UserOverlay = mMap.addGroundOverlay(newarkMap);
        UserOverlay.setBearing(bearing);
        UserOverlay.setPosition(point);

        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, callback);

        //mMap.moveCamera(CameraUpdateFactory.scrollBy(0, -540));
    }


    public float change_zoom(double dis){
        float zoom = mMap.getCameraPosition().zoom;
        System.out.println(zoom);
        if(dis>1500 && dis < 3500){  zoom = 14; }
        if(dis>3500 && dis < 5500){  zoom = 13; }
        if(dis>5500 ){  zoom = 12; }
        return zoom;
    }

    public void Focus_user(){
        change_camera(now_position);
    }
    public void Focus_user(float zoom){
        change_camera(now_position, zoom);
    }
    public void Move_Destination(float zoom){
        change_camera(my_data.Direction, zoom);
    }

//    public  void get_overview_points(String text){
//        ArrayList<String> routes   = new ArrayList<String>();
//        ArrayList<String> polyline = new ArrayList<String>();
//        ArrayList<String> overview = new ArrayList<String>();
//        My_Json my_json = new My_Json();
//        my_json.get_json(text, routes, "routes");
//        my_json.get_json(routes.get(0), polyline, "overview_polyline");
//        my_json.get_json(polyline.get(0), overview, "points");
//        My_Calcuate my_calcuate = new My_Calcuate();
//        my_calcuate.Polyline_decoder(overview, points);
//        Draw_Map(points);
//        //my_json.show(overview);
//    }
    private void Draw_Map(ArrayList<LatLng> Points){
        //mMap.clear();
        PolylineOptions polylineOptions = new PolylineOptions();
        for(int i=0; i< Points.size();i++){
            //System.out.println(Points.get(i));
            polylineOptions.add(Points.get(i));
        }
        polylineOptions.color(context.getResources().getColor(R.color.route_color));
        polylineOptions.width(9f);
        polyline = mMap.addPolyline(polylineOptions);
    }

    public void draw_direction(ArrayList<String> text){


        ArrayList<LatLng> poly_list = new ArrayList<LatLng>();
        My_Calcuate my_calcuate = new My_Calcuate();
        my_calcuate.Polyline_decoder(text, poly_list);
        my_data.poly_list = poly_list;
        double dis = my_calcuate.camera_dis_cal(my_data.Origin, my_data.Direction);
        LatLng center = my_calcuate.camera_center_cal(my_data.Origin, my_data.Direction);

        Toast.makeText(context, String.valueOf(dis), Toast.LENGTH_SHORT).show();


        float zoom = change_zoom(dis);
        change_camera(center, zoom);
        Draw_Map(poly_list);
    }
    public void Draw_History_Point(ArrayList<LatLng> Points){
        PolylineOptions polylineOptions = new PolylineOptions();
        for(int i=0; i< Points.size();i++){
            polylineOptions.add(Points.get(i));
        }
        polylineOptions.color(context.getResources().getColor(R.color.history_color));
        polylineOptions.width(9f);
        history_polyline = mMap.addPolyline(polylineOptions);
    }
    public void Novigation_Start(){
        My_Calcuate my_calcuate = new My_Calcuate();

        double lat = Double.parseDouble(my_data.Lat.get(0));
        double lng = Double.parseDouble(my_data.Lng.get(0));
        LatLng start = new LatLng(lat, lng);
        LatLng end = new LatLng(lat, lng);
        float bearing = my_calcuate.count_bearing(now_position, end);
        change_camera(now_position, 21, bearing, 90);

        for(int i=1; i<my_data.Lat.size(); i++) {
            start = end;
            lat = Double.parseDouble(my_data.Lat.get(i));
            lng = Double.parseDouble(my_data.Lng.get(i));
            end = new LatLng(lat, lng);
            bearing = my_calcuate.count_bearing(start, end);
            change_camera(now_position, 21, bearing, 90);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Register_Sensor(LinearLayout ll){
        My_Sensor my_sensor = new My_Sensor(context, ll);
        my_sensor.registerListener();
    }


    //remove----------------------------------------------------------------------------
    public void remove_destination_mark(){
        if(destination!=null){
            destination.remove();
        }

    }
    public void remove_polyline(){
        if(polyline!=null) {
            polyline.remove();
        }
    }
    public void remove_history_polyline(){
        if(history_polyline!=null){
            history_polyline.remove();
        }
    }

    public void remove_map(){
        mMap.clear();
    }
    public void remove_Overlay(){
        if(UserOverlay!=null){
            UserOverlay.remove();
        }
    }

    public void Toast(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
    public void Toast(double text){
        String text1 = String.valueOf(text);
        Toast.makeText(context, text1, Toast.LENGTH_SHORT).show();
    }

    public BitmapDescriptor BitmapFromVector(int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
