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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.io.IOException;
import java.nio.file.DirectoryStream;
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

public class MapsActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private Button add_marker_to_database_button;
    private Button go_button;
    private Button reset_map_button;

    private EditText editText_where_is_the_starting_place;
    private EditText editText_where_do_you_want_to_go;

    private Marker last_marker;

    private LatLng user_location;


    //Queue with all the charghing stations
    private ArrayList<ChargingStation> chargingStations = new ArrayList<ChargingStation>();
    private  ArrayList<Marker> chargingStations_markers = new ArrayList<Marker>();
    private ArrayList<Distance> distances = new ArrayList<Distance>();


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
        mMap.setOnInfoWindowClickListener(this);
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
        reset_map_button = findViewById(R.id.reset_map_button);
        reset_map_button.setVisibility(View.INVISIBLE);


        //Get_all_charghing_stations_from_the_db
        get_charghing_stations_from_db(chargingStations);


        for (ChargingStation elem :
                chargingStations) {
            String snipp = "";
            if (elem.get_type2() == 1)
                snipp += "Type2 ";
            if (elem.get_supercharger() == 1)
                snipp += "Supercharger ";
            if (elem.get_wall() == 1)
                snipp += "Wall";

            Marker marker = mMap.addMarker(new MarkerOptions().position(elem.get_lat_lang()).title(elem.get_name()).snippet(snipp));
            chargingStations_markers.add(marker);
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
                        ChargingStation start = new ChargingStation();
                        ChargingStation end = new ChargingStation();

                        start.set_x_coordinate(from.latitude);
                        start.set_y_coordinate(from.longitude);
                        start.set_id(-1);

                        end.set_x_coordinate(to.latitude);
                        end.set_y_coordinate(to.longitude);
                        end.set_id(0);

                        get_distances_from_db(distances);
                        AStar aStar = new AStar();
                        Queue<Integer> ids = aStar.calculate_route(start, end ,chargingStations, distances);

                        if (!ids.isEmpty())
                            draw_route(start, end , ids);
                        else
                            Toast.makeText(MapsActivity.this, "No route found with given preferences",Toast.LENGTH_SHORT).show();

                        distances = new ArrayList<Distance>(); // No need to save the distances after use
                    }
                }
            }
        });



        reset_map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
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


    private void draw_route(ChargingStation start, ChargingStation end, Queue<Integer> ids){
        chargingStations_markers = new ArrayList<>();
        mMap.clear();
        ArrayList<LatLng> locations_for_line_draw = new ArrayList<LatLng>();
        locations_for_line_draw.add(start.get_lat_lang());

        while (!ids.isEmpty()){
            Integer next_id = ids.remove();

            if (next_id == start.get_id())
                mMap.addMarker(new MarkerOptions().title("Start location").position(start.get_lat_lang()));

            if (next_id == end.get_id())
                mMap.addMarker(new MarkerOptions().title("Destination location").position(end.get_lat_lang()));

            for (ChargingStation station:
                 chargingStations) {
                if (next_id == station.get_id()) {
                    String snipp = "";
                    if (station.get_type2() == 1)
                        snipp += "Type2 ";
                    if (station.get_supercharger() == 1)
                        snipp += "Supercharger ";
                    if (station.get_wall() == 1)
                        snipp += "Wall";
                    chargingStations_markers.add(mMap.addMarker(new MarkerOptions().title(station.get_name()).position(station.get_lat_lang()).snippet(snipp)));
                    locations_for_line_draw.add(station.get_lat_lang());
                }
            }
        }
        //Zoom on a the point on the middle of the route
        LatLng point_to_zoom_on = locations_for_line_draw.get(locations_for_line_draw.size()/2);

        locations_for_line_draw.add(end.get_lat_lang());
        draw_line(locations_for_line_draw);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point_to_zoom_on,5));

        reset_map_button.setVisibility(View.VISIBLE);
        go_button.setAlpha((float)0.5);
        go_button.setEnabled(false);

    }

    public void draw_line(ArrayList<LatLng> locations_for_line_draw){

        LatLng start = locations_for_line_draw.remove(0);
        LatLng end = start;
        while (!locations_for_line_draw.isEmpty()){
            start = end;
            end = locations_for_line_draw.remove(0);
            draw_line_internal(start, end);
        }
    }

    //Wonderfull guide that I follwed here https://stackoverflow.com/questions/47492459/how-do-i-draw-a-route-along-an-existing-road-between-two-points
    public void draw_line_internal(LatLng start, LatLng end){
        GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyAKJryOqQbrSookyJ2viovZ79bne-EtL4I").build();

        List<LatLng> path = new ArrayList<>();

        String string_start = start.latitude + "," + start.longitude;
        String string_end = end.latitude + "," + end.longitude;

        DirectionsApiRequest request = DirectionsApi.getDirections(context,string_start, string_end);

        try{
            DirectionsResult result = request.await();

            if (result.routes != null && result.routes.length > 0){
                DirectionsRoute route = result.routes[0];

                if (route.legs != null){
                    for (int i = 0; i < route.legs.length; i++){
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null){
                            for (int j = 0 ; j < leg.steps.length; j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0){
                                    for (int k = 0; k < step.steps.length; k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null){
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1:
                                                 coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null){
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord:
                                             coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(MapsActivity.this, "Intrerupted, couldn't draw the line", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MapsActivity.this, "IOException, couldn't draw the line", Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(MapsActivity.this, "Api error, couldn't draw the line", Toast.LENGTH_SHORT).show();
        }

        if (path.size() > 0){
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.MAGENTA).width(10);
            mMap.addPolyline(opts);
        }

    }



    public void get_distances_from_db(ArrayList<Distance> distances){

        ConnectionDB connectionDB = ConnectionDB.getInstance();
        Connection connection = connectionDB.getConnection();

        if (connection != null){


            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT id_from, id_to, road_distance FROM distances");

                while (resultSet.next()){
                    Integer id_from = resultSet.getInt(1);
                    Integer id_to = resultSet.getInt(2);
                    Integer road_distance = resultSet.getInt(3);

                    Distance aux = new Distance(id_from, id_to, road_distance);
                    distances.add(aux);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Toast.makeText(MapsActivity.this, "SQL statement error",Toast.LENGTH_SHORT).show();
            }


        }
        else
            Toast.makeText(MapsActivity.this, "No connection", Toast.LENGTH_SHORT).show();
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


                ResultSet resultSet = statement.executeQuery("SELECT id FROM chargingstations WHERE x_coordinate like " + String.format("%.4f", chargingStation.get_x_coordinate()) + " AND y_coordinate LIKE " +
                        String.format("%.4f", chargingStation.get_x_coordinate()));

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

            Queue<Integer> road_distances = distancesAndGeocodings.get_road_distance(chargingStation, chargingStations);
            Queue<Integer> road_distances_inverse = new LinkedList<>(road_distances);

            if (!chargingStations.isEmpty()) {

                ResultSet resultSet = statement.executeQuery("SELECT id FROM chargingstations WHERE x_coordinate LIKE " + String.format("%.4f", chargingStation.get_x_coordinate()) + " AND y_coordinate LIKE " +  String.format("%.4f", chargingStation.get_y_coordinate()));

                resultSet.next();

                Integer new_station_id = resultSet.getInt(1);

                String sql = "INSERT INTO DISTANCES(id_from, id_to,  road_distance) VALUES ";

                if (chargingStations.size() > 1)
                    for (int i = 0; i < chargingStations.size() - 1; i++)
                        sql += "(" + new_station_id + "," + chargingStations.get(i).get_id()  + "," + road_distances.remove() + "),";

                sql += "(" + new_station_id + "," + chargingStations.get(chargingStations.size() - 1).get_id()  + "," + road_distances.remove() + ")";
                statement.executeUpdate(sql);


                sql = "INSERT INTO DISTANCES(id_to, id_from, road_distance) VALUES ";
                if (chargingStations.size() > 1)
                    for (int i = 0; i < chargingStations.size() - 1; i++)
                        sql += "(" + new_station_id + "," + chargingStations.get(i).get_id()+ "," + road_distances_inverse.remove() + "),";
                sql += "(" + new_station_id + "," + chargingStations.get(chargingStations.size() - 1).get_id()  + "," + road_distances_inverse.remove() + ")";
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
                Toast.makeText(MapsActivity.this, "Nothing was changed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker){

        Intent intent = new Intent(MapsActivity.this, MarkerInfoActivity.class);

        LatLng latLng = marker.getPosition();
        Integer marker_id = null;

        for (ChargingStation station:
             chargingStations) {
            if (station.get_x_coordinate() == latLng.latitude && station.get_y_coordinate() == latLng.longitude)
                marker_id = station.get_id();
        }

        if (marker_id != null){
            intent.putExtra("marker_id" , marker_id);
            startActivityForResult(intent, 2);
        }

    }
}