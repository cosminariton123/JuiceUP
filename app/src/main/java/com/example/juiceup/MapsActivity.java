package com.example.juiceup;

import androidx.fragment.app.FragmentActivity;

import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.juiceup.databinding.ActivityMapsBinding;

import java.util.LinkedList;
import java.util.Queue;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    Button add_marker_to_database_button;
    Button GO_button;

    EditText editText_where_do_you_want_to_go;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // Add a marker in Sydney and move the camera

        LatLng sydney = new LatLng(44.4268, 26.1025);
        LatLng melbourne = new LatLng(30, 30);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(melbourne).title("Marker in melb"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        Queue<LatLng> queue = new LinkedList<LatLng>();

        //queue.add(new LatLng(47.4979, 19.0402));
        queue.add(new LatLng(52.3676, 4.9041));
        queue.add(new LatLng(96.3676, 36.9041));
        queue.add(new LatLng(11, 50));
        queue.add(new LatLng(97, 48));
        queue.add(new LatLng(28,54));
        queue.add(new LatLng(0, 0));
        queue.add(new LatLng(2, 2));


        for (LatLng elem:
             queue) {
            mMap.addMarker(new MarkerOptions().position(elem).title("A"));
        }

        DistancesAndGeocodings distancesAndGeocodings = new DistancesAndGeocodings();

        Double distanta  = distancesAndGeocodings.get_spherical_distance(sydney, melbourne);
        Queue<Double> distance = distancesAndGeocodings.get_road_distance(sydney, queue);


        String output = "";
        while (!distance.isEmpty()){

            output += distance.remove().toString();
            output += "\n";
        }

        Toast.makeText(this , output, Toast.LENGTH_LONG).show();

    }






}