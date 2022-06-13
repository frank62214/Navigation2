package com.example.navigation.My;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.TextView;

public class My_UI implements Runnable {
    //private My_Layout my_layout;
    private My_Data my_data = new My_Data();
    TextView tv ;
    public My_UI(TextView textView){
        tv = textView;
    }

    @Override
    public void run() {
        while(my_data.Refresh_Satellites) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    tv.setText(my_data.GPS);

                }
            });
            SystemClock.sleep(1000);
        }
    }
}
