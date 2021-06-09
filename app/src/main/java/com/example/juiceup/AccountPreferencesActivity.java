package com.example.juiceup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountPreferencesActivity extends AppCompatActivity {

    private RadioButton yes_preference_radio_button;
    private RadioButton no_preference_radio_button;
    private EditText editText_nr_of_parking_spots_preference;
    private Switch switch_Type_preference;
    private Switch switch_wall_preference;
    private Switch switch_supercharger_preference;
    private EditText editText_output_kWh_preference;
    private EditText editText_maximum_range;
    private Button set_button;
    private ImageButton one_star_preference;
    private ImageButton two_star_preference;
    private ImageButton three_star_preference;
    private ImageButton four_star_preference;
    private ImageButton five_star_preference;
    private RadioButton low_trust_score;
    private RadioButton medium_trust_score;
    private RadioButton high_trust_score;
    private Integer star_number_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_preferences);
        setTitle("Preferences used in calculating route");

        yes_preference_radio_button = findViewById(R.id.yes_preference_radio_button);
        editText_nr_of_parking_spots_preference = findViewById(R.id.editText_nr_of_parking_spots_preference);
        switch_Type_preference = findViewById(R.id.switch_Type_preference);
        switch_wall_preference = findViewById(R.id.switch_wall_preference);
        switch_supercharger_preference = findViewById(R.id.switch_supercharger_preference);
        editText_output_kWh_preference = findViewById(R.id.editText_output_kWh_preference);
        editText_maximum_range = findViewById(R.id.editText_maximum_range);
        set_button = findViewById(R.id.set_button);
        no_preference_radio_button = findViewById(R.id.no_preference_radio_button);
        one_star_preference = findViewById(R.id.one_star_preference);
        two_star_preference = findViewById(R.id.two_star_preference);
        three_star_preference = findViewById(R.id.three_star_preference);
        four_star_preference = findViewById(R.id.four_star_preference);
        five_star_preference = findViewById(R.id.five_star_preference);
        low_trust_score = findViewById(R.id.low_trust_score_preference_radio_button);
        medium_trust_score = findViewById(R.id.medium_trust_score_preference_radio_button);
        high_trust_score = findViewById(R.id.high_trust_preference_radio_button);

        star_number_pressed = null;

        one_star_preference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_number_pressed = 1;

                one_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                two_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                three_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                four_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                five_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
            }
        });

        two_star_preference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_number_pressed = 2;

                one_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                two_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                three_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                four_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                five_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
            }
        });

        three_star_preference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_number_pressed = 3;

                one_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                two_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                three_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                four_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                five_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
            }
        });

        four_star_preference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_number_pressed = 4;

                one_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                two_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                three_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                four_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                five_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
            }
        });

        five_star_preference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_number_pressed = 5;

                one_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                two_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                three_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                four_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                five_star_preference.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
            }
        });

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
                Integer min_rating_preference = 1;
                Integer min_trust_preference = 1;

                if (editText_nr_of_parking_spots_preference.getText().toString().equals("") || editText_maximum_range.getText().toString().equals("") || editText_output_kWh_preference.getText().toString().equals("") || (!no_preference_radio_button.isChecked() && !yes_preference_radio_button.isChecked())
                || star_number_pressed == null || (!low_trust_score.isChecked() && !medium_trust_score.isChecked() && !high_trust_score.isChecked()))
                    Toast.makeText(AccountPreferencesActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                else if (Integer.parseInt(editText_nr_of_parking_spots_preference.getText().toString()) < 0 || Integer.parseInt(editText_maximum_range.getText().toString()) < 0 || Integer.parseInt(editText_output_kWh_preference.getText().toString()) < 0)
                    Toast.makeText(AccountPreferencesActivity.this, "Negative values aren't allowed", Toast.LENGTH_SHORT).show();
                else{
                    min_rating_preference = star_number_pressed;

                    if (low_trust_score.isChecked())
                        min_trust_preference = 1;

                    if (medium_trust_score.isChecked())
                        min_trust_preference = 2;

                    if (high_trust_score.isChecked())
                        min_trust_preference = 3;

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
                                    ",car_max_km_range=" + max_range+ ",min_rating_preference=" + min_rating_preference + ",min_trust_preference=" + min_trust_preference+ " WHERE email LIKE '" + currentUser.get_email() + "'");

                            currentUser.set_details(currentUser.get_email(), currentUser.get_password(), currentUser.get_salt(), currentUser.get_first_name(),
                                    currentUser.get_last_name(), max_range, yes_preference_guard, nr_parking, type2, wall, supercharger, kwh, min_rating_preference, min_trust_preference , currentUser.get_trust_score());
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