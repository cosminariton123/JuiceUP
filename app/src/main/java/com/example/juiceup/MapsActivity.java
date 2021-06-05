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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

import javax.crypto.spec.GCMParameterSpec;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private Button add_marker_to_database_button;
    private Button GO_button;

    private EditText editText_where_do_you_want_to_go;

    private Marker last_marker;


    //Queue with all the charghing stations
    private Queue<ChargingStation> chargingStations = new LinkedList<ChargingStation>();


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
                markerOptions.draggable(true);
                markerOptions.title("Location of your charghing station");


                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));  //zoom the camera

                last_marker = mMap.addMarker(markerOptions);  //add marker on map
            }  });




        add_marker_to_database_button = findViewById(R.id.add_marker_to_database_button);
        GO_button = findViewById(R.id.GO_button);
        editText_where_do_you_want_to_go = findViewById(R.id.editText_where_do_you_want_to_go);
        //editText_where_do_you_want_to_go.getBackground().setAlpha(100);


        //Get_all_charghin_stations_from_the_db
        get_charghing_stations_from_db(chargingStations);


        for (ChargingStation elem :
                chargingStations) {
            mMap.addMarker(new MarkerOptions().position(elem.get_lat_lang()).title(elem.get_name()));
        }



        //Curent user location won't be requested by gps, it will be hardcoded
        //Emulator always shows current location in USA,at Google
        LatLng curent_user_location = new LatLng(44.4268, 26.1025);



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
        // Add a marker in Sydney and move the camera

        LatLng sydney = new LatLng(44.4268, 26.1025);
        LatLng melbourne = new LatLng(30, 30);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(melbourne).title("Marker in melb"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        Queue<LatLng> queue = new LinkedList<LatLng>();

        //queue.add(new LatLng(47.4979, 19.0402));
        queue.add(new LatLng(52.3676, 4.9041));
        queue.add(new LatLng(96.3676, 36.9041));
        queue.add(new LatLng(11, 50));
        queue.add(new LatLng(97, 48));
        queue.add(new LatLng(28, 54));
        queue.add(new LatLng(0, 0));
        queue.add(new LatLng(2, 2));


        for (LatLng elem :
                queue) {
            mMap.addMarker(new MarkerOptions().position(elem).title("A"));
        }

        DistancesAndGeocodings distancesAndGeocodings = new DistancesAndGeocodings();

        Double distanta = distancesAndGeocodings.get_spherical_distance(sydney, melbourne);
        Queue<Double> distance = distancesAndGeocodings.get_road_distance(sydney, queue);


        String output = "";
        while (!distance.isEmpty()) {

            output += distance.remove().toString();
            output += "\n";
        }

        Toast.makeText(this, output, Toast.LENGTH_LONG).show();





    }


    private void get_charghing_stations_from_db(Queue<ChargingStation> chargingStations) {

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

                statement.executeUpdate("INSERT INTO chargingstations(name, set_by_user  ,x_coordinate, y_coordinate, guarded, parking_number_of_places, type2, wall, supercharger, outputkwh) VALUES('" +
                        chargingStation.get_name() + "','" + currentUser.get_email() + "'," + chargingStation.get_x_coordinate() + "," +
                        chargingStation.get_y_coordinate() + "," + chargingStation.get_guarded() + "," + chargingStation.get_parking_number_of_places() + "," +
                        chargingStation.get_type2() + "," + chargingStation.get_wall() + "," + chargingStation.get_supercharger() + "," + chargingStation.get_outputkwh() + ")");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Toast.makeText(MapsActivity.this, "SQL statement error", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(MapsActivity.this, "No connection", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                ChargingStation chargingStation = new ChargingStation();
                chargingStation.deserialize_set(data.getStringExtra("charghingstation"));
                add_charghing_station_to_db(chargingStation);
                Toast.makeText(MapsActivity.this, "Added",Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
            if (resultCode == RESULT_CANCELED){
                Toast.makeText(MapsActivity.this, "Nothing was modified", Toast.LENGTH_SHORT).show();
            }
        }
    }
}