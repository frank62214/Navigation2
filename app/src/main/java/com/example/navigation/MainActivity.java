package com.example.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.health.SystemHealthManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigation.My.My_Data;
import com.example.navigation.My.My_Layout;
import com.example.navigation.My.My_Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    // 定義這個權限要求的編號
    private final int REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION = 100;

    private static My_Layout my_layout;
    private GoogleMap mMap;
    private My_Location my_location;
    //private My_Layout my_layout;

    private String Main_Drive_Mode = "";
    private String Main_Direction_Mode = "";

    private boolean page_switch[] = new boolean[2];

    private ArrayList<String> result = new ArrayList<String>();

    private My_Data my_data = new My_Data();
    private My_Event my_event;

    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationManager mLocationMgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        my_data.page_number = 1;
        my_layout = new My_Layout(this);
        my_event = new My_Event(my_layout);
        set_Listener();
        setContentView(my_layout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        // 建立一個GoogleApiClient物件。
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null)
                    return;
                Toast.makeText(MainActivity.this, "更新位置",
                        Toast.LENGTH_LONG).show();
                Location location = locationResult.getLastLocation();
                updateMapLocation(location);
            }
        };

        mLocationMgr = (LocationManager) getSystemService(LOCATION_SERVICE);

        //init
        Main_Drive_Mode = getResources().getString(R.string.promptDrivingMode);
        Main_Direction_Mode = getResources().getString(R.string.promptDriveDriveHere);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // 啟動 Google API。
        mGoogleApiClient.connect();
    }
    @Override
    protected void onPause() {
        super.onPause();

        // 停止定位
        enableLocation(false);
    }
    @Override
    protected void onStop() {
        super.onStop();

        // 停用 Google API
        Toast.makeText(MainActivity.this, "停用Google API", Toast.LENGTH_LONG)
                .show();
        mGoogleApiClient.disconnect();
    }

    private void enableLocation(boolean on) {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // 這項功能尚未取得使用者的同意
            // 開始執行徵詢使用者的流程
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    MainActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder altDlgBuilder =
                        new AlertDialog.Builder(MainActivity.this);
                altDlgBuilder.setTitle("提示");
                altDlgBuilder.setMessage("App需要啟動定位功能。");
                altDlgBuilder.setIcon(android.R.drawable.ic_dialog_info);
                altDlgBuilder.setCancelable(false);
                altDlgBuilder.setPositiveButton("確定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface,
                                                int i) {
                                // 顯示詢問使用者是否同意功能權限的對話盒
                                // 使用者答覆後會執行onRequestPermissionsResult()
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{
                                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);
                            }
                        });
                altDlgBuilder.show();

                return;
            } else {
                // 顯示詢問使用者是否同意功能權限的對話盒
                // 使用者答覆後會執行callback方法onRequestPermissionsResult()
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);

                return;
            }
        }

        // 這項功能之前已經取得使用者的同意，可以直接使用
        if (on) {
            // 取得上一次定位資料
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!=null) {
                        Toast.makeText(MainActivity.this, "成功取得上一次定位",
                                Toast.LENGTH_LONG).show();
                        updateMapLocation(location);
                    } else {
                        Toast.makeText(MainActivity.this, "沒有上一次定位的資料",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

            // 準備一個LocationRequest物件，設定定位參數，在啟動定位時使用
            LocationRequest locationRequest = LocationRequest.create();
            // 設定二次定位之間的時間間隔，單位是千分之一秒。
            locationRequest.setInterval(5000);
            // 二次定位之間的最大距離，單位是公尺。
            locationRequest.setSmallestDisplacement(5);

            // 啟動定位，如果GPS功能有開啟，優先使用GPS定位，否則使用網路定位。
            if (mLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationRequest.setPriority(
                        LocationRequest.PRIORITY_HIGH_ACCURACY);
                Toast.makeText(MainActivity.this, "使用GPS定位",
                        Toast.LENGTH_LONG).show();
            } else if (mLocationMgr.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER)) {
                locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                Toast.makeText(MainActivity.this, "使用網路定位",
                        Toast.LENGTH_LONG).show();
            }

            // 啟動定位功能
            mFusedLocationClient.requestLocationUpdates(
                    locationRequest, mLocationCallback, Looper.myLooper());
        } else {
            // 停止定位功能
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Toast.makeText(MainActivity.this, "停止定位", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void updateMapLocation(Location location) {
        // location物件中包含定位的經緯度資料
        // 移動地圖到新位置
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 15));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 檢查收到的權限要求編號是否和我們送出的相同
        if (requestCode == REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION) {
            if (grantResults.length != 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 再檢查一次，就會進入同意的狀態，並且順利啟動。
                enableLocation(true);
                return;
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        UiSettings UI  = mMap.getUiSettings();
        UI.setMyLocationButtonEnabled(false);
//        my_location = new My_Location(this, mMap);
//        my_location.set_location();
//        my_event.InitLocation(my_location);
//        my_layout.InitLocation(my_location);
//        page_switch[0] = true;
//        page_switch[1] = true;
//        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(@NonNull LatLng latlng) {
//                Check_Main_Direction_Mode(latlng);
//            }
//        });
    }
    private void set_Listener(){
        ImageButton focus = my_layout.focus;
        //ImageButton mode  = my_layout.Direction_mode;
        //ImageButton drive = my_layout.Drive_mode;
        focus.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                my_location.Focus_user();
            }
        });

        EditText dir = my_layout.direction_text;
        dir.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                //my_data.page_number = 2;
                //my_layout.get_page(my_data.page_number);
                System.out.println("FYBR");
                my_data.page_name = "Search";
                my_layout.get_page(my_data.page_name);
            }
        });
        //設定鍵盤確認按鈕的監聽事件
        dir.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //my_layout.createText(MainActivity.this);
                return false;
            }
        });
        //my_event.set_Search_event(this);

        my_layout.Search.setEnabled(false);
        my_layout.Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //點選完搜尋自動將鍵盤縮起來
                InputMethodManager inputMethodManager = (InputMethodManager)view.getContext().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);



                //自製ViewModel，做一半
                my_layout.ProgressBarOn();
                My_ViewModel my_viewModel = new My_ViewModel("place_search", my_layout.direction_text.getText().toString() , my_location);
                my_viewModel.SearchData(new My_ViewModel.onDataReadyCallback() {
                    @Override
                    public void onDataReady(ArrayList<String> data) {
                        setText(data);
                    }
                });
            }
        });

        //set direction mode dialog and get the mode
//        mode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                View direction_view = my_layout.direction_view;
//                if(page_switch[0]) {
//                    page_switch[0] = false;
//                    direction_view.setVisibility(View.VISIBLE);
//                    Button ok_button = (Button) direction_view.findViewById(R.id.btnDirectionOk);
//                    Button cal_button = (Button) direction_view.findViewById(R.id.btnDirectionCancel);
//                    ok_button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            page_switch[0] = true;
//                            RadioGroup RG = (RadioGroup) direction_view.findViewById(R.id.rgDirectionMode);
//                            RadioButton RB = (RadioButton) direction_view.findViewById(RG.getCheckedRadioButtonId());
//                            Main_Direction_Mode = RB.getText().toString();
//                            checkSearch(Main_Direction_Mode);
//                            my_layout.Change_Icon(Main_Direction_Mode);
//                            direction_view.setVisibility(view.INVISIBLE);
//                        }
//                    });
//                    cal_button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            page_switch[0] = true;
//                            direction_view.setVisibility(view.INVISIBLE);
//                        }
//                    });
//                }
//                else{
//                    page_switch[0] = true;
//                    my_layout.resetRadio(Main_Direction_Mode);
//                    direction_view.setVisibility(view.INVISIBLE);
//
//                }
//            }
//        });
        my_event.set_Drive_Mode();
        my_event.set_Record_event();
        my_event.setResource_event();
    }
    private void setText(ArrayList<String> data){
//        my_layout.setText(MainActivity.this, data);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                if(data.size()!=0){
                    //my_data.ScrollView_Result_v = true;
                    //my_layout.check_scrollview_visiable();
                    my_layout.setText(MainActivity.this, data);
                    //my_layout.search_view.setVisibility(View.GONE);
                    //my_event.set_event(MainActivity.this, my_location);
                    my_event.set_event(MainActivity.this);
                }
                else{
                    my_layout.setText(MainActivity.this , "查無資料，請重新搜尋");
                }
                my_layout.ProgressBarOff();
            }
        });
    }
    private void checkSearch(String mode){
        View search_view = my_layout.search_view;
        if(mode.equals(getResources().getString(R.string.promptDriveDriveHere))){
            search_view.setVisibility(View.INVISIBLE);
        }
        if(mode.equals(getResources().getString(R.string.promptDriveSearch))){
            my_location.remove_destination_mark();
            //my_location.remove_map();
            my_location.remove_polyline();
            //my_location.Focus_user();
            search_view.setVisibility(View.VISIBLE);
            ImageButton search = (ImageButton) search_view.findViewById(R.id.btnViewSearch);
            EditText destination = (EditText)search_view.findViewById(R.id.tvTbtNbDistance);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(destination.getText().toString());
                }
            });

        }
    }
    private void Check_Main_Direction_Mode(LatLng point){
        if(Main_Direction_Mode.equals(getResources().getString(R.string.promptDriveDriveHere))){
            my_location.remove_destination_mark();
            my_location.remove_map();
            //my_location.add_Now_Mark();
            my_location.add_Destination_Mark(point);
            Toast.makeText(this, "終點已設置", Toast.LENGTH_LONG).show();
            String url = "https://maps.googleapis.com/maps/api/directions/json" +
                    "?destination=" + Double.toString(point.latitude) + "," + Double.toString(point.longitude) +
                    "&mode=" + my_layout.get_mode(Main_Drive_Mode) +
                    "&origin=" + Double.toString(my_location.now_position.latitude) + "," + Double.toString(my_location.now_position.longitude) +
                    "&key=" ;
            System.out.println(url);
            //My_Search my_search = new My_Search(url, my_location);
            //Thread t1 = new Thread(my_search);
            //t1.start();
        }
    }
    public void show_text(){
        //Bundle bundle = this.getIntent().getExtras();
        //my_layout.show_result(name);
    }
    @Override
    public void onBackPressed(){
        if(my_data.page_number>1){my_data.page_number--;}
        else{ goBackToDesktop(); }
        my_layout.get_page(my_data.page_number);
    }
    private void goBackToDesktop(){
        my_event.goBackToDesktop(MainActivity.this, new My_Event.onListenerCallBack() {
            @Override
            public void onListener(boolean choose) {
                if(choose){ finish(); }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Google API 連線成功時會執行這個方法
        Toast.makeText(MainActivity.this, "Google API 連線成功",
                Toast.LENGTH_LONG).show();

        // 啟動定位
        enableLocation(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Google API 無故斷線時，才會執行這個方法
        // 程式呼叫disconnect()時不會執行這個方法
        switch (i) {
            case CAUSE_NETWORK_LOST:
                Toast.makeText(MainActivity.this, "網路斷線，無法定位",
                        Toast.LENGTH_LONG).show();
                break;
            case CAUSE_SERVICE_DISCONNECTED:
                Toast.makeText(MainActivity.this, "Google API 異常，無法定位",
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // 和 Google API 連線失敗時會執行這個方法
        Toast.makeText(MainActivity.this, "Google API 連線失敗",
                Toast.LENGTH_LONG).show();
    }
}
