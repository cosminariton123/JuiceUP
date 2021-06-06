package com.example.juiceup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountPreferencesActivity extends AppCompatActivity {

    RadioButton yes_preference_radio_button;
    EditText editText_nr_of_parking_spots_preference;
    Switch switch_Type_preference;
    Switch switch_wall_preference;
    Switch switch_supercharger_preference;
    EditText editText_output_kWh_preference;
    EditText editText_maximum_range;
    Button set_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_preferences);
        setTitle("Account settings");

        yes_preference_radio_button = findViewById(R.id.yes_preference_radio_button);
        editText_nr_of_parking_spots_preference = findViewById(R.id.editText_nr_of_parking_spots_preference);
        switch_Type_preference = findViewById(R.id.switch_Type_preference);
        switch_wall_preference = findViewById(R.id.switch_wall_preference);
        switch_supercharger_preference = findViewById(R.id.switch_supercharger_preference);
        editText_output_kWh_preference = findViewById(R.id.editText_output_kWh_preference);
        editText_maximum_range = findViewById(R.id.editText_maximum_range);
        set_button = findViewById(R.id.set_button);




        set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer yes_preference_guard = 0;
                Integer nr_parking = 0;
                Integer type2 = 0;
                Integer wall = 0;
                Integer supercharger = 0;
                Integer kwh = 0;
                Integer max_range = 0;

                if (editText_nr_of_parking_spots_preference.getText().toString().equals("") || editText_maximum_range.getText().toString().equals("") || editText_output_kWh_preference.getText().toString().equals(""))
                    Toast.makeText(AccountPreferencesActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                else if (Integer.parseInt(editText_nr_of_parking_spots_preference.getText().toString()) < 0 || Integer.parseInt(editText_maximum_range.getText().toString()) < 0 || Integer.parseInt(editText_output_kWh_preference.getText().toString()) < 0)
                    Toast.makeText(AccountPreferencesActivity.this, "Negative values aren't allowed", Toast.LENGTH_SHORT).show();
                else{
                    if (yes_preference_radio_button.isChecked())
                        yes_preference_guard = 1;

                    nr_parking = Integer.parseInt(editText_nr_of_parking_spots_preference.getText().toString());

                    if (switch_Type_preference.isChecked())
                        type2 = 1;

                    if (switch_wall_preference.isChecked())
                        wall = 1;

                    if (switch_supercharger_preference.isChecked())
                        supercharger = 1;

                    kwh = Integer.parseInt(editText_output_kWh_preference.getText().toString());
                    max_range = Integer.parseInt(editText_maximum_range.getText().toString());



                    ConnectionDB connectionDB = ConnectionDB.getInstance();
                    Connection connection = connectionDB.getConnection();
                    CurrentUser currentUser = CurrentUser.getInstance();

                    if (connection != null){

                        try {

                            Statement statement = connection.createStatement();
                            statement.executeUpdate("UPDATE users SET guarded_place_preference = " + yes_preference_guard + ",nr_of_parking_spots_preference = " + nr_parking +
                                    ",type2_preference= " + type2 + ",wall_preference= " + wall + ",supercharger_preference= " +supercharger+ ",min_kwh_preference=" + kwh +
                                    ",car_max_km_range=" + max_range+ " WHERE email LIKE '" + currentUser.get_email() + "'");

                            currentUser.set_details(currentUser.get_email(), currentUser.get_password(), currentUser.get_salt(), currentUser.get_first_name(),
                                    currentUser.get_last_name(), max_range, yes_preference_guard, nr_parking, type2, wall, supercharger, kwh, currentUser.get_trust_score());
                            Toast.makeText(AccountPreferencesActivity.this, "All set", Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            Toast.makeText(AccountPreferencesActivity.this, "SQL statement error", Toast.LENGTH_SHORT).show();
                        }

                    }

                    else{
                        Toast.makeText(AccountPreferencesActivity.this ,"Check Internet connection",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}