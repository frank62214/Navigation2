package com.example.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{


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

        //init
        Main_Drive_Mode = getResources().getString(R.string.promptDrivingMode);
        Main_Direction_Mode = getResources().getString(R.string.promptDriveDriveHere);

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        my_location = new My_Location(this, mMap);
        my_location.set_location();
        my_event.InitLocation(my_location);
        my_layout.InitLocation(my_location);
        page_switch[0] = true;
        page_switch[1] = true;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latlng) {
                Check_Main_Direction_Mode(latlng);
            }
        });
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
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                my_data.page_number = 2;
                my_layout.get_page(my_data.page_number);
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
            my_location.add_Now_Mark();
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
}
