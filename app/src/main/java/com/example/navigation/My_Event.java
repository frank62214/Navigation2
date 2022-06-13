package com.example.navigation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.navigation.My.My_Data;
import com.example.navigation.My.My_Layout;
import com.example.navigation.My.My_Location;
import com.example.navigation.My.My_Navigation;
import com.example.navigation.My.My_Record;
import com.example.navigation.My.My_Sensor;
import com.example.navigation.My.My_UI;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class My_Event {

    private Button button;
    private ArrayList<Button> button_group;
    private boolean page_switch[] = new boolean[2];
    private My_Data my_data = new My_Data();
    private My_Layout my_layout;
    private My_Location my_location;
    public My_Event(Button bt){
        button = bt;
    }
    public My_Event(ArrayList<Button> btg){
        button_group = btg;
    }
    public My_Event(My_Layout layout){
        my_layout = layout;
        button_group = my_layout.button_group;
    }
    public void InitLocation(My_Location location){
        my_location = location;
    }
    public void set_event(Context context){
        //my_location = location;
        for(int i =0; i < button_group.size(); i++) {
            button = button_group.get(i);
            button.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {

                    close_keyboard(view);

                    my_location.remove_destination_mark();
                    my_location.remove_polyline();

                    Button b = (Button) view;
                    int id = b.getId();
                    //System.out.println(my_data.Location.get(b.getId()));
                    System.out.println(my_data.Lat.get(id) + " " + my_data.Lng.get(id));
                    Toast.makeText(context, b.getText().toString(), Toast.LENGTH_SHORT).show();

                    double lat = Double.parseDouble(my_data.Lat.get(id));
                    double lng = Double.parseDouble(my_data.Lng.get(id));
                    LatLng dir_point = new LatLng(lat, lng);
                    my_data.Direction_text = b.getText().toString();
                    my_data.page_number++;
                    my_layout.get_page(my_data.page_number);
                    //System.out.println(my_data.Result);
                    set_route_event();
                    set_Navigation_event();
//                    my_layout.rlSearch.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//                    my_layout.search_result.setVisibility(View.GONE);
//                    my_layout.navigation.setVisibility(View.GONE);
//                    my_layout.introduce.setVisibility(View.VISIBLE);
//                    my_data.introduce_v = true;
//                    my_layout.check_introduce_visible();
                    my_location.add_Destination_Mark(dir_point);
                    my_location.change_camera(dir_point);
                    my_data.Direction = new LatLng(lat, lng);
                }
            });
        }
    }
    public void set_Drive_Mode(){
        //set drive mode dialog and get the mode--------------------------
        my_layout.Drive_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View drive_mode_view = my_layout.drive_mode_view;
                RadioGroup RG = (RadioGroup) drive_mode_view.findViewById(R.id.rgDrive_mode);
                if(page_switch[1]) {
                    page_switch[1] = false;
                    drive_mode_view.setVisibility(View.VISIBLE);
                    Button ok_button = (Button) drive_mode_view.findViewById(R.id.btnDriveOk);
                    Button cal_button = (Button) drive_mode_view.findViewById(R.id.btnDriveCancel);
                    ok_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            page_switch[1] = true;
                            RadioButton RB = (RadioButton) drive_mode_view.findViewById(RG.getCheckedRadioButtonId());
                            my_data.Drive_Mode = RB.getText().toString();
                            my_layout.Change_Icon(RB.getText().toString());
                            drive_mode_view.setVisibility(View.INVISIBLE);
                            String text = my_data.Direction.latitude + "," + my_data.Direction.longitude;
                            set_ViewModel_event(text);
                        }
                    });
                    cal_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            page_switch[1] = true;
                            drive_mode_view.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                else{
                    page_switch[1] = true;
                    my_layout.resetRadio(my_data.Drive_Mode);
                    drive_mode_view.setVisibility(view.INVISIBLE);
                }

            }

        });
        //----------------------------------------------------------------
    }
    public boolean goBackToDesktop(Context context, final onListenerCallBack callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("確定要退出?");
        builder.setPositiveButton("現在就去", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //System.out.println("FYBR1");
               callback.onListener(true);
            }
        });
        builder.setNegativeButton("稍後", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                //System.out.println("FYBR2");
                callback.onListener(false);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return my_data.goBackToDesktop_choose;
    }
    public void set_route_event(){
        ImageButton imageButton = my_layout.route;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = my_data.Direction.latitude + "," + my_data.Direction.longitude;
                set_ViewModel_event(text);
            }
        });
    }
    //執行搜尋功能
    public void set_ViewModel_event(String text){
        My_ViewModel my_viewModel = new My_ViewModel("location_search", text, my_location);
        my_viewModel.SearchDirection(new My_ViewModel.onDataReadyCallback() {
            @Override
            public void onDataReady(ArrayList<String> data) {
                //my_location.draw_direction(data);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        my_location.draw_direction(data);
                        my_data.page_number++;
                        my_layout.get_page(my_data.page_number);

                    }
                });
            }
        });
    }

    public void set_Navigation_event(){
        ImageButton imageButton = my_layout.pg4_navigation;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                //my_data.page_number = 5;
                //my_layout.get_page(my_data.page_number);

                //my_location.create_Overlay();

                //My_Simulator my_simulator = new My_Simulator(my_location);
                //Thread t = new Thread(my_simulator);
                //t.start();

                //my_location.Novigation_Start();

                //---------------------------------------------------------------------------
                my_data.page_name = "Navigation_Page";
                my_layout.get_page(my_data.page_name);
                my_location.create_Overlay();
                //RegisterListener();
                My_Navigation my_novigation = new My_Navigation(my_location, my_layout);
                Thread t = new Thread(my_novigation);
                t.start();
                //----------------------------------------------------------------------------
//                if(my_data.Record_status) {
//                    my_data.Record_status = false;
//                }
//                else{
//                    my_data.Record_status = true;
//                }
                //----------------------------------------------------------------------------


            }
        });
    }
    public void set_Record_event(){
        ImageButton imageButton = my_layout.btnRouteRecord;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
//                System.out.println("FYBR");
                //my_layout.tv_GPS_Count.setText(my_data.GPS);
                //my_data.count++;

//                my_data.page_name = "Record";
//                my_layout.get_page(my_data.page_name);
//
//                my_location.test_camera();
//
//                my_location.create_Overlay();


//                My_Record my_record = new My_Record(my_location);
//                Thread t = new Thread(my_record);
//                t.start();
                if(my_data.Record_status && my_data.page_name.equals("Navigation_Page")) {
                    my_data.Record_status = false;
                    my_layout.tv_Now_Record_History.setText("歷史行走位置已取消");
                }
                else{
                    my_data.Record_status = true;
                    my_layout.tv_Now_Record_History.setText("歷史行走位置已開啟");
                }

            }
        });
    }
    public void setResource_event(){
        ImageButton imageButton = my_layout.btnSelectResource;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                System.out.println("now status:" + my_data.Select_Resource);
                if(my_data.Select_Resource.equals("Network")){
                    my_data.Select_Resource = "GPS";
//                    my_data.Refresh_Satellites = true;
//                    My_UI my_ui = new My_UI(my_layout.tv_Now_Satellite);
//                    Thread t = new Thread(my_ui);
//                    t.start();
                }
                else if(my_data.Select_Resource.equals("GPS")){
                    my_data.Select_Resource = "Network";
                }
                my_layout.changeResourceIcon();
                System.out.println("Change to " + my_data.Select_Resource);

                my_layout.tv_GPS_Count.setText(my_data.GPS);


            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void close_keyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager)view.getContext().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onBackPressed(){
        System.out.println("FYBR");
    }

    interface onListenerCallBack{
        void onListener(boolean choose);
    }
}
