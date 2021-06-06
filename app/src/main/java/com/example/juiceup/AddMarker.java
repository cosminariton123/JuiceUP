package com.example.juiceup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class AddMarker extends AppCompatActivity {


    private Button add_to_db_button;
    private RadioButton yes_radio_button;
    private RadioButton no_radio_button;
    private EditText editText_nr_of_parking_spots;
    private Switch switch_Type2;
    private Switch switch_wall;
    private Switch switch_supercharger;
    private EditText editText_output_kWh;
    private EditText editText_charghing_station_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);

        add_to_db_button = findViewById(R.id.add_to_db_button);
        yes_radio_button = findViewById(R.id.yes_radio_button);
        no_radio_button = findViewById(R.id.no_radio_button);
        editText_nr_of_parking_spots = findViewById(R.id.editText_nr_of_parking_spots);
        switch_Type2 = findViewById(R.id.switch_Type2);
        switch_wall = findViewById(R.id.switch_wall);
        editText_output_kWh = findViewById(R.id.editText_output_kWh);
        editText_charghing_station_name = findViewById(R.id.editText_charghing_station_name);
        switch_supercharger = findViewById(R.id.switch_supercharger);

        Intent intent = getIntent();

        LatLng marker_position;
        Double x;
        Double y;
        x = intent.getDoubleExtra("marker_position_x", 0);
        y = intent.getDoubleExtra("marker_position_y", 0);

        add_to_db_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText_nr_of_parking_spots.getText().toString().equals("") || editText_output_kWh.getText().toString().equals("") || editText_charghing_station_name.getText().toString().equals(""))
                    Toast.makeText(AddMarker.this, "All fields are required", Toast.LENGTH_SHORT).show();
                else if (Integer.parseInt(editText_nr_of_parking_spots.getText().toString()) < 0 || Integer.parseInt(editText_output_kWh.getText().toString()) < 0)
                    Toast.makeText(AddMarker.this, "Negative values aren't allowed" ,Toast.LENGTH_SHORT).show();
                else
                    {

                    Integer guarded = 0;
                    if (yes_radio_button.isChecked())
                        guarded = 1;

                    Integer parking_spots = Integer.parseInt(editText_nr_of_parking_spots.getText().toString());
                    Integer type2 = 0;
                    Integer wall = 0;
                    Integer supercharger = 0;

                    if (switch_Type2.isChecked())
                        type2 = 1;
                    if (switch_wall.isChecked())
                        wall = 1;
                    if (switch_supercharger.isChecked())
                        supercharger = 1;

                    CurrentUser currentUser = CurrentUser.getInstance();

                    Integer outputkwh = Integer.parseInt(editText_output_kWh.getText().toString());

                    String name = editText_charghing_station_name.getText().toString();

                    ChargingStation chargingStation = new ChargingStation();
                    chargingStation.set_values(0, name, currentUser.get_email(), x, y, guarded, parking_spots, type2, wall, supercharger, outputkwh);

                    Intent intent_result = new Intent();
                    intent_result.putExtra("charghingstation", chargingStation.serialize());
                    setResult(RESULT_OK, intent_result);
                    finish();
                }
            }
        });

    }
}