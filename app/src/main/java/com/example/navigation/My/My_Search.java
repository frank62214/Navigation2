package com.example.navigation.My;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PreciseDataConnectionState;

import com.example.navigation.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class My_Search implements Runnable{


    private String Search_url = "";
    private String web_text = "";
    private String type = "";
    private My_Location my_location;
    private String key = "AIzaSyBm6kC5U0Y_k3lfmggPRurC0C3o3wiUlA0";

    private String Search_url_1 = "https://maps.googleapis.com/maps/api/place/textsearch/json?location=";   //+lat
    private String Search_url_2 = ",";                                                                      //+lng
    private String Search_url_3 = "&language=zh-TW&query=";                                                                //+text
    private String Search_url_4 = "&radius=10000&key=";                                                     //+key

    private String direction = "";

    private ArrayList<String> name    = new ArrayList<String>();

    public My_Search(String type, String dir,  My_Location location){
        if(type=="place_search"){
            direction = dir;
            Search_url = Search_url_1 + location.now_position.latitude + Search_url_2 + location.now_position.longitude;
            Search_url = Search_url   + Search_url_3 + direction + Search_url_4 + key;
        }
        //System.out.println(Search_url);
        //Search_url = input_url + key;
        //my_location = location;
        //type = input_type;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        try {
            System.out.println(Search_url);
            // 初始化 URL
            URL url = new URL(Search_url);
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

                web_text = responseString;
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
        get_result(web_text);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                //MapsActivity.get_text(web_text, type);
                //System.out.println(web_text);
                //my_location.get_overview_points(web_text);
                //Bundle bundle = new Bundle();
                //bundle.putStringArrayList("name", name);
                //MainActivity.show_text();
            }
        });
    }
    private void get_result(String text){
        My_Json my_json = new My_Json(direction);
        ArrayList<String> results = new ArrayList<String>();
        //ArrayList<String> name    = new ArrayList<String>();
        my_json.get_json(text, results, "results");
        //System.out.println(results);
        //my_json.get_json(results.get(0), name, "name");
        my_json.get_json(results, name, "name");
        //my_json.show(name);
    }
    interface onDataReadyCallBack{
        void onDataReady(String data);
    }

}
