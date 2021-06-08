package com.example.juiceup;

import android.view.LayoutInflater;

import com.google.android.gms.maps.model.LatLng;

import java.util.Vector;

public class ChargingStation {

    private Integer id;
    private String name;
    private String set_by_user;
    private Double x_coordinate;
    private Double y_coordinate;
    private Integer guarded;
    private Integer praking_number_of_places;
    private Integer type2;
    private Integer wall;
    private Integer supercharger;
    private Integer outputkwh;


    public ChargingStation(){
        id = 0;
        x_coordinate = 0.;
        y_coordinate = 0.;
        guarded = 0;
        praking_number_of_places = 0;
        type2 = 0;
        wall = 0;
        supercharger = 0;
        outputkwh = 0;
    }


    public void set_values(Integer i, String nam, String set_by, Double x, Double y, Integer guard, Integer parking, Integer type, Integer wal, Integer superch, Integer output){

        //Only 4 decimals allowed
        int aux = (int)(x * 10000.0);
        x = ((double)aux) / 10000.0;

        aux = (int)(y * 10000.0);
        y = ((double)aux) / 10000.0;

        id = i;
        name = nam;
        set_by_user = set_by;
        x_coordinate = x;
        y_coordinate = y;
        guarded = guard;
        praking_number_of_places = parking;
        type2 = type;
        wall = wal;
        supercharger = superch;
        outputkwh = output;
    }

    public Integer get_id(){
        return id;
    }

    public String get_name(){
        return name;
    }

    public String get_set_by_user(){
        return set_by_user;
    }

    public Double get_x_coordinate(){
        return x_coordinate;
    }

    public Double get_y_coordinate(){
        return  y_coordinate;
    }

    public Integer get_guarded(){
        return guarded;
    }

    public Integer get_parking_number_of_places(){
        return praking_number_of_places;
    }

    public Integer get_type2(){
        return type2;
    }

    public Integer get_wall(){
        return wall;
    }

    public Integer get_supercharger(){
        return  supercharger;
    }

    public Integer get_outputkwh(){
        return  outputkwh;
    }

    public LatLng get_lat_lang(){
        LatLng latLng = new LatLng(x_coordinate, y_coordinate);
        return  latLng;
    }

    public void set_x_coordinate(Double x){
        x_coordinate = x;
    }

    public void set_y_coordinate(Double y){
        y_coordinate = y;
    }

    public void set_id(Integer idd){
        id = idd;
    }

    public String serialize(){
        String serialized = "";

        serialized += id.toString() + ",";
        serialized += name + ",";
        serialized += set_by_user + ",";
        serialized += x_coordinate.toString() + ",";
        serialized += y_coordinate.toString() + ",";
        serialized += guarded.toString() + ",";
        serialized += praking_number_of_places.toString() + ",";
        serialized += type2.toString() + ",";
        serialized += wall.toString() + ",";
        serialized += supercharger.toString() + ",";
        serialized += outputkwh.toString();

        return serialized;
    }

    public void deserialize_set(String serialized_data){
        String[] vec = serialized_data.split(",", 1000);

        set_values(Integer.parseInt(vec[0]) , vec[1], vec[2], Double.parseDouble(vec[3]), Double.parseDouble(vec[4]),
                Integer.parseInt(vec[5]), Integer.parseInt(vec[6]), Integer.parseInt(vec[7]), Integer.parseInt(vec[8]), Integer.parseInt(vec[9]), Integer.parseInt(vec[10]));
    }

}
