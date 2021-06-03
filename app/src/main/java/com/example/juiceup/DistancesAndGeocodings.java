package com.example.juiceup;

import android.location.Location;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.JsonToken;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

public class DistancesAndGeocodings {

    private String api_key;

    public DistancesAndGeocodings(){
        api_key = "AIzaSyAKJryOqQbrSookyJ2viovZ79bne-EtL4I";
    }

    public Double get_spherical_distance(LatLng from, LatLng to) {
        float[] distance = new float[1];
        Location.distanceBetween(from.latitude, from.longitude, to.latitude, to.longitude, distance);
        Float wraped_distance = distance[0] / 1000;
        return wraped_distance.doubleValue();
    }


    public Queue<Double> get_road_distance(LatLng from, Queue<LatLng>to){

        Double from_latitude = from.latitude;
        Double from_longitude = from.longitude;
        String url_string = "";

        url_string += "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + from_latitude.toString() + "," + from_longitude.toString() + "&destinations=";

        while (!to.isEmpty()){
            LatLng aux_to = to.remove();
            Double aux_to_latitude = aux_to.latitude;
            Double aux_to_longitude = aux_to.longitude;

            if (to.isEmpty()){
                url_string += aux_to_latitude.toString() + ",";
                url_string += aux_to_longitude.toString();
            }
            else{
                url_string += aux_to_latitude.toString() + ",";
                url_string += aux_to_longitude.toString() + "|";
            }
        }

        url_string = url_string +"&key=" + api_key;

        Queue <Double> queue = new LinkedList<Double>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        JsonReader jsonReader;

        try{

            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            InputStream inputStream = connection.getInputStream();
            jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

            jsonReader.setLenient(true);

            JsonParser jsonParser = new JsonParser();
            queue = jsonParser.parse_json_distances(jsonReader);



        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return queue;
    }
}
