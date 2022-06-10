package com.example.navigation;

import android.accounts.AbstractAccountAuthenticator;
import android.os.Handler;
import android.widget.EdgeEffect;

import com.example.navigation.My.My_Data;
import com.example.navigation.My.My_Json;
import com.example.navigation.My.My_Location;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class My_ViewModel{

    private String Search_url   = "";
    private String Location_url = "";
    private String web_text = "";
    private String type = "";
    private My_Location my_location;
    private String key = "AIzaSyBm6kC5U0Y_k3lfmggPRurC0C3o3wiUlA0";

    private String Search_url_1 = "https://maps.googleapis.com/maps/api/place/textsearch/json?location=";   //+lat
    private String Search_url_2 = ",";                                                                      //+lng
    private String Search_url_3 = "&language=zh-TW&query=";                                                                //+text
    private String Search_url_4 = "&radius=10000&key=";                                                     //+key

    private String Location_url_1 = "https://maps.googleapis.com/maps/api/directions/json?destination=";
    private String Location_url_2 = "&mode=";
    private String Location_url_3 = "&origin=";
    private String Location_url_4 = ",";
    private String Location_url_5 = "&language=zh-TW&key=";

    private String direction = "";

    private ArrayList<String> result  = new ArrayList<String>();
    private ArrayList<String> geo     = new ArrayList<>();

    private My_Data my_data = new My_Data();

    public My_ViewModel(String type, String dir,  My_Location location){
        if(type=="place_search"){
            direction = dir;
            Search_url = Search_url_1 + location.now_position.latitude + Search_url_2 + location.now_position.longitude;
            Search_url = Search_url   + Search_url_3 + direction + Search_url_4 + key;
        }
        if(type=="location_search"){
            direction = dir;
            Location_url = Location_url_1 + dir + Location_url_2 + my_data.Drive_Mode + Location_url_3;
            Location_url = Location_url + location.now_position.latitude + Location_url_4 + location.now_position.longitude;
            Location_url = Location_url + Location_url_5 + key;
        }
    }


    public void SearchData(final onDataReadyCallback callback){
        try {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    web_text = run_content(Search_url);
                    get_result(web_text);
                    callback.onDataReady(my_data.Result);
//                //callback.onDataReady("New Data");
                }
            };
            //runnable.run();
            Thread t1 = new Thread(runnable);
            t1.start();
        }
        catch(Exception e) {
            e.printStackTrace();
            my_data.Result.add("請重新搜尋");
            callback.onDataReady(my_data.Result);
        }
    }
    private void get_result(String text){
        My_Json my_json = new My_Json(direction);
        ArrayList<String> results  = new ArrayList<String>();
        ArrayList<String> formatted_address     = new ArrayList<String>();
        ArrayList<String> c_name   = new ArrayList<String>();
        ArrayList<String> geometry = new ArrayList<String>();
        ArrayList<String> location = new ArrayList<String>();
        ArrayList<String> lat      = new ArrayList<String>();
        ArrayList<String> lng      = new ArrayList<String>();
        my_json.get_json(text, results, "results");
        my_json.get_json(results, formatted_address, "formatted_address");
        my_json.get_chinese_json(results, c_name, "name");
        my_json.get_json(results, geometry, "geometry");
        my_json.get_json(geometry, location, "location");
        my_json.get_json(location, lat, "lat");
        my_json.get_json(location, lng, "lng");
        //my_json.show(location);

        my_data.Result          = c_name;
        //my_data.Geometry        = geometry;
        my_data.Location        = location;
        my_data.Lat             = lat;
        my_data.Lng             = lng;
        //my_data.Direction_text  = direction;
    }


    private String run_content(String api_url){
        String text = "";
        HttpURLConnection connection = null;
        try {
            System.out.println(api_url);
            // 初始化 URL
            URL url = new URL(api_url);
            // 取得連線物件
            connection = (HttpURLConnection) url.openConnection();
            // 設定 request timeout
            connection.setReadTimeout(1500);
            connection.setConnectTimeout(1500);
            // 模擬 Chrome 的 user agent, 因為手機的網頁內容較不完整
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");
            // 設定開啟自動轉址
            connection.setInstanceFollowRedirects(true);

            // 若要求回傳 200 OK 表示成功取得網頁內容
            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                // 讀取網頁內容
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String tempStr;
                StringBuffer stringBuffer = new StringBuffer();

                while ((tempStr = bufferedReader.readLine()) != null) {
                    stringBuffer.append(tempStr);
                }

                bufferedReader.close();
                inputStream.close();

                // 取得網頁內容類型
                String mime = connection.getContentType();
                boolean isMediaStream = false;

                // 判斷是否為串流檔案
                if (mime.indexOf("audio") == 0 || mime.indexOf("video") == 0) {
                    isMediaStream = true;
                }

                // 網頁內容字串
                String responseString = stringBuffer.toString();

                text = responseString;
                //System.out.println(web_text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 中斷連線
            if (connection != null) {
                connection.disconnect();
            }
        }
        return text;
    }
    public void SearchDirection(final onDataReadyCallback callback){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    web_text = run_content(Location_url);
                    //get_result(web_text);
                    //System.out.println(web_text);
                    get_Direction(web_text);
                    callback.onDataReady(my_data.overview);
//                //callback.onDataReady("New Data");
                }
            };
            //runnable.run();
            Thread t2 = new Thread(runnable);
            t2.start();
    }
    public void get_Direction(String text){
        My_Json my_json = new My_Json();
        ArrayList<String> routes = new ArrayList<String>();
        ArrayList<String> polyline_overview = new ArrayList<String>();
        ArrayList<String> points = new ArrayList<String>();
        ArrayList<String> legs = new ArrayList<String>();
        ArrayList<String> steps = new ArrayList<String>();
        ArrayList<String> end_location_s = new ArrayList<String>();
        ArrayList<String> html_instrustions     = new ArrayList<String>();
        ArrayList<String> html_instrustions_tmp = new ArrayList<String>();
        ArrayList<String> maneuver = new ArrayList<String>();
        ArrayList<String> lat = new ArrayList<String>();
        ArrayList<String> lng = new ArrayList<String>();
        my_json.get_json(text, routes, "routes");
        my_json.get_json(routes, polyline_overview, "overview_polyline");
        my_json.get_json(polyline_overview, points, "points");
        //get every point
        my_json.get_json(routes, legs, "legs");
        my_json.get_json(legs, steps, "steps");


        my_json.get_json(steps, end_location_s, "end_location");
        my_json.get_json(steps, html_instrustions_tmp, "html_instructions");
        //my_json.get_json(steps, maneuver, "maneuver");

        //取得Lat Lng
        //my_json.get_json(Start_location_s, lat, "lat");
        my_json.get_json(end_location_s, lat, "lat");
        my_json.get_json(end_location_s, lng, "lng");


        my_data.steps     = change_ArrayList(lat, lng);
        html_instrustions = change_text(html_instrustions_tmp);




        //my_json.show_latlng(my_data.steps);
        //my_json.show(maneuver);


        my_data.overview = points;

    }
    public ArrayList<LatLng> change_ArrayList(ArrayList<String> lat, ArrayList<String> lng){
        ArrayList<LatLng> ans = new ArrayList<LatLng>();
        for(int i=0; i<lat.size(); i++){
            double Lat = Double.parseDouble(lat.get(i));
            double Lng = Double.parseDouble(lng.get(i));
            LatLng tmp = new LatLng(Lat, Lng);
            ans.add(tmp);
        }
        return ans;
    }
    public ArrayList<String> change_text(ArrayList<String> text){
        ArrayList<String> new_text = new ArrayList<String>();
        ArrayList<String> road = new ArrayList<String>();
        ArrayList<String> road_detail = new ArrayList<String>();
        String text_tmp = "";
        for(int i=0; i<text.size(); i++){
            //System.out.println(text.get(i));
            text_tmp = text.get(i).replace("/", "");
            String[] tmp = text_tmp.split("<b>");
            String merge = "";
            for(int j=0; j<tmp.length; j++){
                //System.out.println(tmp[j]);
                merge = merge + tmp[j];
                if(tmp[j].contains("路") || tmp[j].contains("街")){
                    road.add(tmp[j]);
                }
            }
            //System.out.println("----------------------");
            System.out.println(merge);
            //System.out.println("----------------------");
            road_detail.add(merge);
        }
        my_data.Road        = road;
        my_data.Road_Detail = road_detail;
//        for(int i=0; i<text.size(); i++){
//            System.out.println(text.get(i));
//            text_tmp = text.get(i).replace("/", "");
//            text_tmp = text_tmp.replace("Turn left", "向左轉");
//            text_tmp = text_tmp.replace("Turn right", "向右轉");
//            text_tmp = text_tmp.replace(" Slight left onto", "微靠左走");
//            text_tmp = text_tmp.replace(" Slight right onto", "微靠右走");
//
//            String[] tmp = text_tmp.split("<b>");
//            String English_turn = "";
//            for(int j=0; j<tmp.length; j++){
//                System.out.println(tmp[j]);
//                if(tmp[j].contains("路") || tmp[j].contains("街") || tmp[j].contains("巷")){
//                    //System.out.println(tmp[j]);
//                    road.add(tmp[j]);
//                    //j = tmp.length;
//                }
//                else{
//                    English_turn = English_turn + tmp[j];
//                }
//            }
//            System.out.println(English_turn);
//            System.out.println("----------------------");
//        }

//        for(int i=0; i<text.size(); i++){
//            String tmp = text.get(i);
//            tmp = tmp.replace("<b>", "");
//            tmp = tmp.replace("</b>", "");
//            if(tmp.contains("Head")){
//                //System.out.println("FYBR");
//                tmp = tmp.replace("Head","往") ;
//                tmp = tmp.replace("on","在") ;
//                tmp = tmp.replace("toward","朝向") ;
//                if(tmp.contains("northwest")){ tmp = tmp.replace("northwest", "西北"); }
//                if(tmp.contains("northeast")){ tmp = tmp.replace("northeast", "東北"); }
//                if(tmp.contains("southwest")){ tmp = tmp.replace("southwest", "西南"); }
//                if(tmp.contains("southeast")){ tmp = tmp.replace("southeast", "東南"); }
//                tmp = tmp.split("<")[0];
//            }
//            else if(tmp.contains("Turn")){
//                tmp = tmp.replace("Turn","向") ;
//                tmp = tmp.replace("onto","進入") ;
//                tmp = tmp.replace("/<wbr/>"," aka.") ;
//                if(tmp.contains("right")){ tmp = tmp.replace("right", "右轉"); }
//                if(tmp.contains("left")){ tmp = tmp.replace("left", "左轉"); }
//                if(tmp.contains("to stay on")){ tmp = tmp.replace("to stay on", "繼續行走"); }
//                if(tmp.contains("toward")){ tmp = tmp.replace("toward", "朝"); }
//                tmp = tmp.split("<")[0];
//            }
//            if(tmp.contains("Continue")){
//                tmp = tmp.replace("Continue","繼續直行") ;
//                tmp = tmp.replace("onto","進入") ;
//            }
//            if(tmp.contains("Keep left to stay on")){
//                tmp = tmp.replace("Keep left to stay on", "靠左繼續沿著");
//            }
//            if(tmp.contains("Keep left to continue on")){
//                tmp = tmp.replace("Keep left to continue on", "靠左進入");
//            }
//            if(tmp.contains("straight to stay on")){
//                tmp = tmp.replace("straight to stay on", "");
//            }
//            if(tmp.contains("to follow")){
//                tmp = tmp.replace("to follow", "");
//            }
//            if(tmp.contains("Take the ramp on the left onto ")){
//                tmp = tmp.replace("Take the ramp on the left onto ", "走左邊坡道上");
//            }
//            if(tmp.contains("Take exit ")){
//                tmp = tmp.replace("Take exit ", "退出");
//            }
//            //tmp = delete_more(tmp);
//            tmp = delete_english(tmp);
//
//            new_text.add(tmp);
//        }
        return new_text;
    }
    private String delete_more(String text){
        text = text.replace("/<wbr/>", "");
        text = text.replace("<", "");
        text = text.replace(">", "");
        text = text.replace("=", "");
        text = text.replace("-", "");
        text = text.replace(":", "");
        text = text.replace(".", "");
        text = text.replace("\"", "");
        text = text.replace("(", "");
        text = text.replace(")", "");
        text = text.replace("/", "");

        return text;
    }

    private String delete_english(String text){
        String ans = text;
        //ans = text.replace("2", "");
        ans = ans.replace(" ", "");
        ans = ans.replace(direction, direction+" ");
        for(int i=0 ; i<10 ; i++){
            //System.out.println(i);
            String test = String.valueOf(i);
            ans = ans.replace(test, "");
        }
        //System.out.println(ans);
        for(int i=0 ; i<26 ; i++){
            int num = i + 65; //A
            String test = Character.toString ((char) num);
            ans = ans.replace(test, "");
        }
        for(int i=0 ; i<26 ; i++){
            int num = i + 97; //a
            String test = Character.toString ((char) num);
            ans = ans.replace(test, "");
        }
        return ans;
    }

    interface onDataReadyCallback {
        void onDataReady(ArrayList<String> data);
    }
}
