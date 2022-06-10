package com.example.navigation.My;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class My_Calcuate {
    public static void Polyline_decoder(ArrayList<String> list, ArrayList<LatLng> Poly_List) {
        //get all the polylines point
        for (int i = 0; i < list.size(); i++) {
            String encoded = list.get(i);
            int index = 0, len = encoded.length();
            int decoded_lat = 0;
            int decoded_lng = 0;
            //get one char in loop
            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    //get on char to calculate decoder
                    b = encoded.charAt(index++);
                    //step 1: number reduce 63
                    b = b - 63;
                    //step 2: number logic operation(AND) 0x1f and then left shift one bit
                    result |= (b & 0x1f) << shift;
                    //step 3: five bit for one block
                    shift += 5;
                } while (b >= 0x20);
                //step 4: if first bit is one need to bit upside down, and do shift on right one bit.
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                decoded_lat += dlat;
                shift = 0;
                result = 0;
                //do the same thing with lng
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                decoded_lng += dlng;
                LatLng p = new LatLng((((double) decoded_lat / 1E5)), (((double) decoded_lng / 1E5)));
                Poly_List.add(p);
            }
        }
    }
    public double camera_dis_cal(LatLng Start, LatLng End){
        double EARTH_RADIUS = 6378137.0;
        //double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (Start.latitude * Math.PI / 180.0);
        double radLat2 = (End.latitude * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (Start.longitude - End.longitude) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2)
                        * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
    public LatLng camera_center_cal(LatLng Start, LatLng End){

        double tmp_lat, tmp_lng;
        if(Start.latitude > End.latitude){
            tmp_lat = Start.latitude - (Start.latitude - End.latitude) / 2;
        }
        else{
            tmp_lat = Start.latitude + (End.latitude - Start.latitude) / 2;
        }
        if(Start.latitude > End.longitude){
            tmp_lng = Start.longitude - (Start.longitude - End.longitude) / 2;
        }
        else{
            tmp_lng = Start.longitude + (End.longitude - Start.longitude) / 2;
        }
        LatLng center = new LatLng(tmp_lat,tmp_lng);

        return center;
    }
    public float count_bearing(LatLng Start, LatLng End){
        double degress = Math.PI / 180.0;
        double phi1 = Start.latitude * degress;
        double phi2 = End.latitude * degress;
        double lam1 = Start.longitude * degress;
        double lam2 = End.longitude * degress;

        double y = Math.sin(lam2 - lam1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(lam2 - lam1);
        float bearing = (float)(((Math.atan2(y, x) * 180) / Math.PI) + 360) % 360;
        System.out.println(bearing);
        if (bearing < 0) {
            bearing = bearing + 360;
        }
        return bearing;
    }
    public void show(ArrayList<LatLng> arrayList){
        for(int i=0; i<arrayList.size();i++){
            System.out.println(arrayList.get(i));
        }
    }
}
