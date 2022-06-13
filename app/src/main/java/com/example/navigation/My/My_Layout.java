package com.example.navigation.My;

import com.example.navigation.MainActivity;
import com.example.navigation.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.navigation.My.My_Location;
import com.google.android.gms.maps.GoogleMap;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.NavigableMap;

public class My_Layout extends RelativeLayout {
    //Layout
    public RelativeLayout rlSearch;
    public LinearLayout llNavButtons;
    public LinearLayout introduce;
    public LinearLayout llsearch_result;
    public RelativeLayout rl_route;
    public RelativeLayout rl_route_page;
    public LinearLayout llUserArrow;
    public LinearLayout Next_Turn;

    //ImageButton
    public ImageButton focus;
    public ImageButton navigation;
    public ImageButton Search;
    public ImageButton route;
    public ImageButton Drive_mode;
    public ImageButton pg4_navigation;
    public ImageButton btnRouteRecord;
    public ImageButton btnSelectResource;

    //dialog
    public View search_view;
    public View Route_view;
    public View direction_view;
    public View drive_mode_view;
    public View Sensor_view;

    //EditText
    public EditText direction_text;
    public EditText destination;
    //ScrollView
    public ScrollView search_result;
    //TextView
    public TextView tv_place_name;
    public TextView tv_Next_Road;
    public TextView tv_Next_Road_Detail;
    public TextView tv_Now_Position;
    public TextView tv_Next_Dis;
    public TextView tv_Now_Satellite;
    public TextView tv_GPS_Count;
    public TextView tv_Now_Record_History;
    public TextView tv_Now_Location_Status;

    //process bar
    private ProgressBar progressBar;

    //Search_button
    public ArrayList<Button> button_group = new ArrayList<Button>();

    public My_Data my_data = new My_Data();
    public My_Location my_location;

    public ImageView ic_Next_Turn;

    public My_Layout(Context context) {
        super(context);
        //my_location = location;
        //context = context1;
        //---------------------------------------------------------------------------------------------------------------------------------
        //Here to add the maps to view
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.activity_maps, null);
        this.addView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //---------------------------------------------------------------------------------------------------------------------------------
        //Here to add the button layout
        LayoutInflater layout = ((Activity) context).getLayoutInflater();
        View layout_view = layout.inflate(R.layout.navigation_layout, null);
        this.addView(layout_view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //---------------------------------------------------------------------------------------------------------------------------------
        //Here to add the maps to view
//        LayoutInflater bar_layout = ((Activity) context).getLayoutInflater();
//        View bar_view = bar_layout.inflate(R.layout.navigation_bar, null);
//        this.addView(bar_view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        bar_view.setVisibility(View.INVISIBLE);
        //---------------------------------------------------------------------------------------------------------------------------------
        //Here to add the search to view
        LayoutInflater search_layout = ((Activity) context).getLayoutInflater();
        search_view = search_layout.inflate(R.layout.navigation_search, null);
        rlSearch = (RelativeLayout) search_view.findViewById(R.id.rlSearch);
        this.addView(search_view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //search_view.setVisibility(View.INVISIBLE);
        //---------------------------------------------------------------------------------------------------------------------------------
        //Here to add the maps to view
        LayoutInflater mode_layout = ((Activity) context).getLayoutInflater();
        drive_mode_view = mode_layout.inflate(R.layout.dialog_drive_mode, null);
        this.addView(drive_mode_view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        drive_mode_view.setVisibility(View.INVISIBLE);
        //---------------------------------------------------------------------------------------------------------------------------------
        //Here to add the maps to view
//        LayoutInflater direction_layout = ((Activity) context).getLayoutInflater();
//        direction_view = direction_layout.inflate(R.layout.direction_mode, null);
//        this.addView(direction_view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        direction_view.setVisibility(View.INVISIBLE);
        //---------------------------------------------------------------------------------------------------------------------------------
        //Here to add the maps to view
        LayoutInflater Route_layout = ((Activity) context).getLayoutInflater();
        Route_view = Route_layout.inflate(R.layout.navigation_route, null);
        this.addView(Route_view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Route_view.setVisibility(View.INVISIBLE);
        //---------------------------------------------------------------------------------------------------------------------------------
        //Here to add the sensor xml
        LayoutInflater Sensor_layout = ((Activity)context).getLayoutInflater();
        Sensor_view = Sensor_layout.inflate(R.layout.navigation_sensor, null);
        this.addView(Sensor_view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Sensor_view.setVisibility(View.INVISIBLE);
        //---------------------------------------------------------------------------------------------------------------------------------
        //usd id find the button
        focus           = (ImageButton) layout_view.findViewById(R.id.btnFocusUser);
        navigation      = (ImageButton) layout_view.findViewById(R.id.btnNavigation);
        btnRouteRecord  = (ImageButton) layout_view.findViewById(R.id.btnRouteRecord);
        btnSelectResource = (ImageButton) layout_view.findViewById(R.id.btnSelectResource);
        llNavButtons    = (LinearLayout) layout_view.findViewById(R.id.llNavButtonsCanvas);
        introduce       = (LinearLayout) layout_view.findViewById(R.id.introduce);


        Next_Turn       = (LinearLayout) layout_view.findViewById(R.id.Next_Turn);
        ic_Next_Turn           = (ImageView) Next_Turn.findViewById(R.id.ic_Next_Turn);
        tv_Next_Road           = (TextView) Next_Turn.findViewById(R.id.tv_Next_Road);
        tv_Next_Road_Detail    = (TextView) Next_Turn.findViewById(R.id.tv_Next_Road_Detail);
        tv_Now_Position        = (TextView) Next_Turn.findViewById(R.id.tv_Now_Position);
        tv_Next_Dis            = (TextView) Next_Turn.findViewById(R.id.tv_Next_Dis);
        tv_Now_Satellite       = (TextView) Next_Turn.findViewById(R.id.tv_Now_Satellite);
        tv_Now_Record_History  = (TextView) Next_Turn.findViewById(R.id.tv_Now_Record_History);
        tv_Now_Location_Status = (TextView) Next_Turn.findViewById(R.id.tv_Now_Location_Status);

        tv_place_name   = (TextView) introduce.findViewById(R.id.place_name);
        //my_data.Navigation_button = (ImageButton) layout_view.findViewById(R.id.btnNavigation);
        direction_text  = (EditText)search_view.findViewById(R.id.tvTbtNbDistance);
        Search          = (ImageButton) search_view.findViewById(R.id.btnViewSearch);
        search_result   = (ScrollView) search_view.findViewById(R.id.sv_search_result);
        llsearch_result = (LinearLayout) search_view.findViewById(R.id.llsearch_result);
        progressBar     = (ProgressBar) search_view.findViewById(R.id.progressBar);
        tv_GPS_Count    = (TextView) search_view.findViewById(R.id.tv_GPS_Count);

        route           = (ImageButton) introduce.findViewById(R.id.btn_Introduce_Route);
        pg4_navigation  = (ImageButton) introduce.findViewById(R.id.btn_Introduce_Route2);
        rl_route        = (RelativeLayout) introduce.findViewById(R.id.rl_route);


        rl_route_page  = (RelativeLayout) Route_view.findViewById(R.id.rlSearchRoute);
        destination    = (EditText) Route_view.findViewById(R.id.tvTbtNbDestination);

        //Direction_mode  = (ImageButton) layout_view.findViewById(R.id.btnDirectionMode);
        Drive_mode      = (ImageButton) layout_view.findViewById(R.id.btnDriving_Mode);
        Drive_mode.setVisibility(View.GONE);

        llUserArrow    = (LinearLayout) layout_view.findViewById(R.id.llUserArrow);

        /*Button button = (Button)view1.findViewById(R.id.Start_navigation);
        EditText editText = (EditText) view1.findViewById(R.id.destination);
        imageView = (ImageView) view1.findViewById(R.id.imageView);
        //add the event
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText editText = (EditText) view1.findViewById(R.id.destination);
                String destination = editText.getText().toString();
                //System.out.println(editText.getText().toString());
                String search_url_1 = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
                String search_url_2 = "&key=AIzaSyCq17YBT9HYp9xLM2Ib5KYxJCWKstWldSk";
                String search_url   = "";
                search_url = search_url_1 + destination + search_url_2;
                System.out.println(search_url);
                get_http(search_url, "place_search");
                imageView.setVisibility(View.INVISIBLE);
            }
        });*/
    }
    public void InitLocation(My_Location location){
        my_location = location;
    }
    public void createText(Context context){

        LinearLayout ll = (LinearLayout) search_view.findViewById(R.id.llsearch_result);
        for(int i=0; i<10; i++){
            TextView tv = new TextView(context);

            tv.setText(String.valueOf(i));
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setHeight(dp2px(50));
            tv.setTextSize(10);
            ll.addView(tv);
        }
    }
    public void setText(Context context, String text){
        //LinearLayout ll = (LinearLayout) search_view.findViewById(R.id.llsearch_result);

        Button bt = new Button(context);
        bt.setText(text);
        bt.setTextColor(getResources().getColor(R.color.black));
        bt.setBackgroundColor(getResources().getColor(R.color.white));
        bt.setHeight(dp2px(50));
        bt.setTextSize(20);
        //ll.addView(bt);
        llsearch_result.addView(bt);
    }
    public void setText(Context context, ArrayList<String> result){
        //ArrayList<Button> btg = new ArrayList<Button>();


        for(int i=0; i<result.size();i++){
            //System.out.println(result.get(i));
            Button bt = new Button(context);
            bt.setText(result.get(i));
            bt.setTextColor(getResources().getColor(R.color.black));
            bt.setBackgroundColor(getResources().getColor(R.color.white));
            bt.setHeight(dp2px(50));
            bt.setTextSize(20);
            bt.setId(i);
            //ll.addView(bt);
            llsearch_result.addView(bt);
            button_group.add(bt);
            //My_Event my_event = new My_Event(bt);
            //my_event.set_event(context);
        }
    }
    public void setIntroduction(){
        tv_place_name.setText(my_data.Direction_text);
    }
    public void Change_Icon(String name){
//        if(name.equals(getResources().getString(R.string.promptDriveDriveHere))){
//            Direction_mode.setImageResource(R.drawable.ic_mode_tbt);
//        }
//        if(name.equals(getResources().getString(R.string.promptDriveSearch))){
//            Direction_mode.setImageResource(R.drawable.ic_search);
//        }


        if(name.equals(getResources().getString(R.string.promptDrivingMode))){
            Drive_mode.setImageResource(R.drawable.car);
        }
        if(name.equals(getResources().getString(R.string.promptBicyclingMode))){
            Drive_mode.setImageResource(R.drawable.bicycle);
        }
        if(name.equals(getResources().getString(R.string.promptWalkingMode))){
            Drive_mode.setImageResource(R.drawable.walk);
        }
    }
    public void changeResourceIcon(){
        if(my_data.Select_Resource.equals("Network")){
            btnSelectResource.setImageResource(R.drawable.ic_wifi);
        }
        if(my_data.Select_Resource.equals("GPS")){
            btnSelectResource.setImageResource(R.drawable.ic_satellite);
        }
    }
    public void resetRadio(String name){
        //------------------------------------------------------------------------------------
        //reset direction mode radio
        if(name.equals(getResources().getString(R.string.promptDriveDriveHere))){
            RadioGroup  RG = (RadioGroup) direction_view.findViewById(R.id.rgDirectionMode);
            RG.check(R.id.rbDriveHere);
        }
        if(name.equals(getResources().getString(R.string.promptDriveSearch))){
            RadioGroup  RG = (RadioGroup) direction_view.findViewById(R.id.rgDirectionMode);
            RG.check(R.id.rbDriveSearch);
        }
        //------------------------------------------------------------------------------------
        //reset driving mode radio
        if(name.equals(getResources().getString(R.string.promptDrivingMode))){
            RadioGroup  RG = (RadioGroup) drive_mode_view.findViewById(R.id.rgDrive_mode);
            RG.check(R.id.rbDriving);
        }
        if(name.equals(getResources().getString(R.string.promptBicyclingMode))){
            RadioGroup  RG = (RadioGroup) drive_mode_view.findViewById(R.id.rgDrive_mode);
            RG.check(R.id.rbBicycling);
        }
        if(name.equals(getResources().getString(R.string.promptWalkingMode))){
            RadioGroup  RG = (RadioGroup) drive_mode_view.findViewById(R.id.rgDrive_mode);
            RG.check(R.id.rbWalking);
        }
        //------------------------------------------------------------------------------------

    }
    public String get_mode(String name){
        String mode = "";
        //------------------------------------------------------------------------------------
        //reset direction mode radio
        /*if(name.equals(getResources().getString(R.string.promptDriveDriveHere))){
            RadioGroup  RG = (RadioGroup) direction_view.findViewById(R.id.rgDirectionMode);
            RG.check(R.id.rbDriveHere);
        }
        if(name.equals(getResources().getString(R.string.promptDriveSearch))){
            RadioGroup  RG = (RadioGroup) direction_view.findViewById(R.id.rgDirectionMode);
            RG.check(R.id.rbDriveSearch);
        }*/
        //------------------------------------------------------------------------------------
        //reset driving mode radio
        if(name.equals(getResources().getString(R.string.promptDrivingMode))){
            mode = "driving";
        }
        if(name.equals(getResources().getString(R.string.promptBicyclingMode))){
            mode = "bicycling";
        }
        if(name.equals(getResources().getString(R.string.promptWalkingMode))){
            mode = "walking";
        }
        //------------------------------------------------------------------------------------
        return mode;
    }
    public void ProgressBarOn(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void ProgressBarOff(){
        progressBar.setVisibility(View.GONE);
    }
    private int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public void check_button_visiable(ImageButton btn){
        if(my_data.Navigation_button_v){
            btn.setVisibility(View.VISIBLE);
        }
        else{
            btn.setVisibility(View.INVISIBLE);
        }
    }
    public void check_scrollview_visiable(){
        if(my_data.ScrollView_Result_v){
            search_result.setVisibility(View.VISIBLE);

        }
        else{
            search_result.setVisibility(View.INVISIBLE);

        }
    }
    public void check_rlSearch_backgroundColor(){
        if(my_data.RelativeLayout_Search_v){
            //System.out.println("FYBR1");
            rlSearch.setBackgroundColor(getResources().getColor(R.color.white));
        }
        else {
            //System.out.println("FYBR2");
            rlSearch.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    public void get_page(int number){
        if(number==1){ page_1(); }
        if(number==2){ page_2(); }
        if(number==3){ page_3(); }
        if(number==4){ page_4(); }
        if(number==5){ page_5(); }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void get_page(String name){
        if(name.equals("Record")){
            Record_Page();
        }
        if(name.equals("Navigation_Page")){
            Navigation_Page();
        }
    }
    private void page_1(){
        rlSearch.setVisibility(View.VISIBLE);
        //direction_text.setText("");
        Search.setEnabled(false);
        my_data.RelativeLayout_Search_v = false;
        check_rlSearch_backgroundColor();
        my_data.ScrollView_Result_v = false;
        check_scrollview_visiable();
        check_introduce_visible(false);
        navigation.setVisibility(View.VISIBLE);
        Clear_Result_ll();
        my_location.remove_destination_mark();
        my_location.Focus_user();
        Clear_Result_ll();
    }
    private void page_2(){
        my_data.ScrollView_Result_v = true;
        check_scrollview_visiable();
        Search.setEnabled(true);
        my_data.RelativeLayout_Search_v = true;
        check_rlSearch_backgroundColor();
        my_location.remove_destination_mark();

    }
    private void page_3(){
        rl_route.setVisibility(View.VISIBLE);
        rlSearch.setVisibility(View.VISIBLE);
        Drive_mode.setVisibility(View.GONE);
        Route_view.setVisibility(View.GONE);
        my_location.Move_Destination(15);
        my_location.remove_polyline();
        Search.setEnabled(false);
        rlSearch.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
        search_result.setVisibility(View.GONE);
        navigation.setVisibility(View.GONE);
        check_introduce_visible(true);
        setIntroduction();
    }
    private void page_4(){
        rl_route.setVisibility(View.GONE);
        rlSearch.setVisibility(View.GONE);
        Route_view.setVisibility(View.VISIBLE);
        destination.setText(my_data.Direction_text);
        Drive_mode.setVisibility(View.VISIBLE);
        //rl_route_page.setVisibility(View.VISIBLE);
        //rl_route_page.setBackgroundColor(getContext().getResources().getColor(R.color.white));
    }
    private void page_5(){
        Route_view.setVisibility(View.GONE);
        Drive_mode.setVisibility(View.GONE);
        check_introduce_visible(false);
        llUserArrow.setVisibility(View.VISIBLE);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void Record_Page(){
        my_data.novigation_status = true;


        rlSearch.setVisibility(View.GONE);
        navigation.setVisibility(View.GONE);
        check_introduce_visible(false);

        Sensor_view.setVisibility(View.VISIBLE);
        //llUserArrow.setVisibility(View.VISIBLE);
        LinearLayout ll = Sensor_view.findViewById(R.id.llsensor);
        my_location.Register_Sensor(ll);
        //----------------------------------------------------------
        //float bearing = my_sensor.bearing;
        //change_camera(now_position, 21, bearing, 90);
        //My_Record my_record = new My_Record(my_location, my_data.bearing);
        //Thread t = new Thread(my_record);
        //t.run();
        //----------------------------------------------------------
        //my_location.Record(ll);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void Navigation_Page(){
        my_data.novigation_status = true;

        Next_Turn.setVisibility(View.VISIBLE);

        //btnRouteRecord.setVisibility(View.GONE);
        Drive_mode.setVisibility(View.GONE);
        Route_view.setVisibility(View.GONE);
        rlSearch.setVisibility(View.GONE);
        navigation.setVisibility(View.GONE);
        check_introduce_visible(false);

        Sensor_view.setVisibility(View.INVISIBLE);
        //llUserArrow.setVisibility(View.VISIBLE);
        LinearLayout ll = Sensor_view.findViewById(R.id.llsensor);
        my_location.Register_Sensor(ll);
        //----------------------------------------------------------
        //float bearing = my_sensor.bearing;
        //change_camera(now_position, 21, bearing, 90);
        //My_Record my_record = new My_Record(my_location, my_data.bearing);
        //Thread t = new Thread(my_record);
        //t.run();
        //----------------------------------------------------------
        //my_location.Record(ll);
    }
    public void check_introduce_visible(boolean select){
        my_data.introduce_v = select;
        if(my_data.introduce_v){
            //layout visible
            introduce.setVisibility(View.VISIBLE);
//            int height = introduce.getHeight();
//            System.out.println(height);
            //move imagebutton
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llNavButtons.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ABOVE, R.id.introduce);
            layoutParams.bottomMargin = dp2px( 10);
            //layoutParams.setMargins(0,0,dp2px(150), dp2px(10));
            llNavButtons.setLayoutParams(layoutParams);
        }
        else{
            //move imagebutton
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llNavButtons.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ABOVE, R.id.viewnone);
            layoutParams.bottomMargin = dp2px(10);
            //layoutParams.setMargins(0,0,dp2px(150), dp2px(10));
            llNavButtons.setLayoutParams(layoutParams);

            //layout invisible
            introduce.setVisibility(View.GONE);
        }

    }
    public void setNextRoadText(String text){
        tv_Next_Road.setText(text);
    }
    public void setNextRoadDetailText(String text){
        tv_Next_Road_Detail.setText(text);
    }
    public void setNowPosition(String text){
        tv_Now_Position.setText(text);
    }
    public void setNextDis(String text){
        tv_Next_Dis.setText(text);
    }
    public void setNowSatellite(String text){ tv_Now_Satellite.setText(text);}
    public void setNowRecordHistory(String text){ tv_Now_Record_History.setText(text);}
    public void setNowLocationStatus() {
        if(my_data.Satellites_number>10){
            tv_Now_Location_Status.setText("現在使用GPS定位");
        }else{
            tv_Now_Location_Status.setText("現在使用網路定位");
        }
    }
    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void Set_Turn_Pic(String turn){
        if(turn.contains("向右轉")){ic_Next_Turn.setImageResource(R.drawable.dir_turnright);}
        if(turn.contains("向左轉")){ic_Next_Turn.setImageResource(R.drawable.dir_turnleft);}
    }
    public void Clear_Result_ll(){
        llsearch_result.removeAllViews();
    }

}

