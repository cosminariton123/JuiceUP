package com.example.juiceup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.juiceup.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.security.Permission;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

import javax.crypto.spec.GCMParameterSpec;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private Button add_marker_to_database_button;
    private Button go_button;

    private EditText editText_where_is_the_starting_place;
    private EditText editText_where_do_you_want_to_go;

    private Marker last_marker;

    private LatLng user_location;


    //Queue with all the charghing stations
    private ArrayList<ChargingStation> chargingStations = new ArrayList<ChargingStation>();
    private  ArrayList<Marker> chargingStations_markers = new ArrayList<Marker>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        last_marker = null;

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (last_marker != null)
                    last_marker.remove();   //if users selects another marker, delete the last one

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);   //set marker position
                markerOptions.title("Location of your charghing station");


                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));  //zoom the camera

                last_marker = mMap.addMarker(markerOptions);  //add marker on map
            }  });




        add_marker_to_database_button = findViewById(R.id.add_marker_to_database_button);
        editText_where_is_the_starting_place = findViewById(R.id.editText_where_is_the_starting_place);
        go_button = findViewById(R.id.GO_button);
        editText_where_do_you_want_to_go = findViewById(R.id.editText_where_do_you_want_to_go);


        //Get_all_charghin_stations_from_the_db
        get_charghing_stations_from_db(chargingStations);


        for (ChargingStation elem :
                chargingStations) {
            chargingStations_markers.add(mMap.addMarker(new MarkerOptions().position(elem.get_lat_lang()).title(elem.get_name())));
        }



        add_marker_to_database_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentUser currentUser = CurrentUser.getInstance();

                if (last_marker == null)
                    Toast.makeText(MapsActivity.this, "First place a marker on the map\nwhere the charghing station is.", Toast.LENGTH_LONG).show();
                else if (!currentUser.get_is_logged()){
                    Toast.makeText(MapsActivity.this, "You have to be logged in\nto add markers to the database", Toast.LENGTH_LONG).show();
                }
                else
                    {
                        LatLng marker_position = last_marker.getPosition();

                        Intent intent = new Intent(MapsActivity.this, AddMarker.class);
                        intent.putExtra("marker_position_x" , marker_position.latitude);
                        intent.putExtra("marker_position_y", marker_position.longitude);
                        startActivityForResult(intent, 1);
                }
            }
        });

        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng from = null;
                LatLng to = null;

                if (editText_where_do_you_want_to_go.getText().toString().equals("") && last_marker == null)
                    Toast.makeText(MapsActivity.this, "Enter a destination or set a marker",Toast.LENGTH_SHORT).show();

                else {


                    if (editText_where_is_the_starting_place.getText().toString().equals(""))
                        if (get_user_location())    //Try to get user location
                            from = new LatLng(user_location.latitude, user_location.longitude);
                        else
                            from = null;
                    else {
                        DistancesAndGeocodings distancesAndGeocodings = new DistancesAndGeocodings();
                        from = distancesAndGeocodings.geocode(editText_where_is_the_starting_place.getText().toString());
                    }

                    if (!editText_where_do_you_want_to_go.getText().toString().equals("")) {
                        DistancesAndGeocodings distancesAndGeocodings = new DistancesAndGeocodings();
                        to = distancesAndGeocodings.geocode(editText_where_do_you_want_to_go.getText().toString());
                    } else
                        to = last_marker.getPosition();

                    if (to == null || from == null) {
                        if (to == null && from == null)
                            Toast.makeText(MapsActivity.this, "We couldn't find either address", Toast.LENGTH_SHORT).show();
                        else {
                            if (to == null)
                                Toast.makeText(MapsActivity.this, "We coudn't find destination address", Toast.LENGTH_SHORT).show();

                            if (from == null)
                                Toast.makeText(MapsActivity.this, "We coudn't find origin address", Toast.LENGTH_SHORT).show();
                        }
                    } else { //Everything should be valid. Existance of a route between the places is still uncertain
                        Toast.makeText(MapsActivity.this, "ALL GOOD", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }

    //Curent user location won't be requested to gps, it will be hardcoded
    //Emulator always shows current location in USA,at Google
    //I will choose Bucharest as "current location"
    private Boolean get_user_location (){
        user_location = new LatLng(44.4268, 26.1025);
        return true;
    }


    private void get_charghing_stations_from_db(ArrayList<ChargingStation> chargingStations) {

        ConnectionDB connectionDB = ConnectionDB.getInstance();
        Connection connection = connectionDB.getConnection();

        if (connection != null) {

            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT id, name, set_by_user, x_coordinate, y_coordinate, guarded, parking_number_of_places, type2, wall, supercharger, outputkwh FROM chargingstations");

                while (resultSet.next()) {
                    Integer id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    String set_by_user = resultSet.getString(3);
                    Double x_coordinate = resultSet.getDouble(4);
                    Double y_coodinate = resultSet.getDouble(5);
                    Integer guarded = resultSet.getInt(6);
                    Integer parking_number_of_places = resultSet.getInt(7);
                    Integer type2 = resultSet.getInt(8);
                    Integer wall = resultSet.getInt(9);
                    Integer supercharger = resultSet.getInt(10);
                    Integer outputkwh = resultSet.getInt(11);

                    ChargingStation aux = new ChargingStation();
                    aux.set_values(id, name, set_by_user, x_coordinate, y_coodinate, guarded, parking_number_of_places, type2, wall, supercharger, outputkwh);
                    chargingStations.add(aux);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Toast.makeText(this, "SQL statement error", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void add_charghing_station_to_db(ChargingStation chargingStation){

        ConnectionDB connectionDB = ConnectionDB.getInstance();
        Connection connection = connectionDB.getConnection();
        CurrentUser currentUser = CurrentUser.getInstance();

        if (connection != null){

            try{
                Statement statement = connection.createStatement();


                ResultSet resultSet = statement.executeQuery("SELECT id FROM chargingstations WHERE x_coordinate like " + chargingStation.get_x_coordinate() + " AND y_coordinate LIKE " +
                        chargingStation.get_x_coordinate());

                if (resultSet.next()){
                    Toast.makeText(MapsActivity.this, "There aleready exists a charghing station at this coordinates", Toast.LENGTH_SHORT).show();
                }
                else{
                    statement.executeUpdate("INSERT INTO chargingstations(name, set_by_user  ,x_coordinate, y_coordinate, guarded, parking_number_of_places, type2, wall, supercharger, outputkwh) VALUES('" +
                            chargingStation.get_name() + "','" + currentUser.get_email() + "'," + chargingStation.get_x_coordinate() + "," +
                            chargingStation.get_y_coordinate() + "," + chargingStation.get_guarded() + "," + chargingStation.get_parking_number_of_places() + "," +
                            chargingStation.get_type2() + "," + chargingStation.get_wall() + "," + chargingStation.get_supercharger() + "," + chargingStation.get_outputkwh() + ")");
                    set_station_connections_in_db(chargingStation);
                    Toast.makeText(MapsActivity.this, "Added",Toast.LENGTH_SHORT).show();
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Toast.makeText(MapsActivity.this, "SQL statement error", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(MapsActivity.this, "No connection", Toast.LENGTH_SHORT).show();
    }

    public void set_station_connections_in_db(ChargingStation chargingStation) throws SQLException{
        ConnectionDB connectionDB = ConnectionDB.getInstance();
        Connection connection = connectionDB.getConnection();
        DistancesAndGeocodings distancesAndGeocodings = new DistancesAndGeocodings();

        if (connection != null){
            Statement statement = connection.createStatement();

            Queue<Integer> spherical_distances = distancesAndGeocodings.get_spherical_distances(chargingStation, chargingStations);
            Queue<Integer> road_distances = distancesAndGeocodings.get_road_distance(chargingStation, chargingStations);

            Queue<Integer> spherical_distances_inverse = new LinkedList<>(spherical_distances);
            Queue<Integer> road_distances_inverse = new LinkedList<>(road_distances);

            if (!chargingStations.isEmpty()) {

                ResultSet resultSet = statement.executeQuery("SELECT id FROM chargingstations WHERE x_coordinate LIKE " + chargingStation.get_x_coordinate() + " AND y_coordinate LIKE " + chargingStation.get_y_coordinate());
                resultSet.next();
                Integer new_station_id = resultSet.getInt(1);

                String sql = "INSERT INTO DISTANCES(id_from, id_to, spherical_distance, road_distance) VALUES ";

                if (chargingStations.size() > 1)
                    for (int i = 0; i < chargingStations.size() - 1; i++)
                        sql += "(" + new_station_id + "," + chargingStations.get(i).get_id() + "," + spherical_distances.remove() + "," + road_distances.remove() + "),";

                sql += "(" + new_station_id + "," + chargingStations.get(chargingStations.size() - 1).get_id() + "," + spherical_distances.remove() + "," + road_distances.remove() + ")";
                statement.executeUpdate(sql);


                sql = "INSERT INTO DISTANCES(id_to, id_from, spherical_distance, road_distance) VALUES ";
                if (chargingStations.size() > 1)
                    for (int i = 0; i < chargingStations.size() - 1; i++)
                        sql += "(" + new_station_id + "," + chargingStations.get(i).get_id() + "," + spherical_distances_inverse.remove() + "," + road_distances_inverse.remove() + "),";
                sql += "(" + new_station_id + "," + chargingStations.get(chargingStations.size() - 1).get_id() + "," + spherical_distances_inverse.remove() + "," + road_distances_inverse.remove() + ")";
                statement.executeUpdate(sql);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                ChargingStation chargingStation = new ChargingStation();
                chargingStation.deserialize_set(data.getStringExtra("charghingstation"));
                add_charghing_station_to_db(chargingStation);
                finish();
                startActivity(getIntent());
            }
            if (resultCode == RESULT_CANCELED){
                Toast.makeText(MapsActivity.this, "Nothing was added", Toast.LENGTH_SHORT).show();
            }
        }
    }
}