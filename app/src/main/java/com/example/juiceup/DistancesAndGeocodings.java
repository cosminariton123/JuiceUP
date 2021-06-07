package com.example.juiceup;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.JsonToken;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

public class DistancesAndGeocodings {

    private String api_key;

    public DistancesAndGeocodings(){
        api_key = "AIzaSyAKJryOqQbrSookyJ2viovZ79bne-EtL4I";
    }


    public LatLng geocode(String location_name){

        String url_string = "https://maps.googleapis.com/maps/api/geocode/json?address=" + location_name +"&key=AIzaSyAKJryOqQbrSookyJ2viovZ79bne-EtL4I";
        LatLng location_of_named_place = null;
        JsonReader jsonReader;

        try{
            URL url = new URL(url_string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            InputStream inputStream = connection.getInputStream();
            jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

            jsonReader.setLenient(true);

            JsonParser jsonParser = new JsonParser();
            location_of_named_place = jsonParser.parse_json_geocode(jsonReader);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return location_of_named_place;
    }


    public Queue<Integer> get_spherical_distances(ChargingStation chargingStation_from, ArrayList<ChargingStation> chargingStations_to){
        LatLng coordinates_from = chargingStation_from.get_lat_lang();

        Queue <LatLng> coordinates = new LinkedList<LatLng>();
        for (ChargingStation elem:
             chargingStations_to) {
            coordinates.add(elem.get_lat_lang());
        }

        Queue <Integer> results = new LinkedList<Integer>();

        for (LatLng elem:
             coordinates) {
            results.add(get_spherical_distance_internal(coordinates_from, elem).intValue());
        }


        return  results;
    }

    public Queue<Integer> get_road_distance(ChargingStation chargingStation_from, ArrayList<ChargingStation> chargingStations_to){
        LatLng coordinates_from = chargingStation_from.get_lat_lang();

        Queue<LatLng> coordinates = new LinkedList<LatLng>();

        for (ChargingStation elem:
             chargingStations_to) {
            coordinates.add(elem.get_lat_lang());
        }

        int counter = 0;
        Queue<Integer> results = new LinkedList<Integer>();
        Queue<LatLng> buffer_coordinates = new LinkedList<LatLng>();
        while (!coordinates.isEmpty()){
            if (counter < 600){  //POST METHOD FOR GOOGLE MAPS API ACCEPTS MAXIMUM ~8000 CHARACTERS => buffer_size should be 600 or less
                buffer_coordinates.add(coordinates.remove());
                counter ++;
            }
            else{
                Queue<Double> aux = get_road_distance_internal(coordinates_from, buffer_coordinates);

                while (!aux.isEmpty()){
                    results.add(aux.remove().intValue());
                }

                buffer_coordinates = new LinkedList<>();
                counter = 0;
            }
        }
        if (counter != 0){
            Queue<Double> aux = get_road_distance_internal(coordinates_from, buffer_coordinates);

            while (!aux.isEmpty()){
                results.add(aux.remove().intValue());
            }
        }

        return results;
    }


    private Double get_spherical_distance_internal(LatLng from, LatLng to) {
        float[] distance = new float[1];
        Location.distanceBetween(from.latitude, from.longitude, to.latitude, to.longitude, distance);
        Float wraped_distance = distance[0] / 1000;
        return wraped_distance.doubleValue();
    }


    private Queue<Double> get_road_distance_internal(LatLng from, Queue<LatLng>to){

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
