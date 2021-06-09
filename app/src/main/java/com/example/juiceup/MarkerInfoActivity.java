package com.example.juiceup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MarkerInfoActivity extends AppCompatActivity {

    private TextView textView_name_info;
    private TextView textView_is_guarded_info;
    private TextView textView_parking_spots_info;
    private TextView textView_type2_info;
    private TextView textView_wall_info;
    private TextView textView_supercharger_info;
    private TextView textView_output_kwh_info;
    private TextView rating;
    private TextView trust_score;
    private RadioButton yes_exist;
    private RadioButton no_exist;
    private ImageButton one_star;
    private ImageButton two_star;
    private ImageButton three_star;
    private ImageButton four_star;
    private ImageButton five_star;
    private Button submit_button;
    private TextView textView_thank_you_for_contribution;
    private LinearLayout voting_layout;

    private Integer star_number_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_info);

        Intent intent = getIntent();
        Integer marker_id = intent.getIntExtra("marker_id", 0);

        if (marker_id == 0) {
            Toast.makeText(MarkerInfoActivity.this, "Unknown error, no marker was selected", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }




        star_number_pressed = null;

        textView_name_info = findViewById(R.id.textView_name_info);
        textView_is_guarded_info = findViewById(R.id.textView_is_guarded_info);
        textView_parking_spots_info = findViewById(R.id.textView_parking_spots_info);
        textView_type2_info = findViewById(R.id.textView_type2_info);
        textView_wall_info = findViewById(R.id.textView_wall_info);
        textView_supercharger_info = findViewById(R.id.textView_supercharger_info);
        textView_output_kwh_info = findViewById(R.id.textView_output_kwh_info);

        rating = findViewById(R.id.textView_rating);
        trust_score = findViewById(R.id.textView_user_trust_score);

        yes_exist = findViewById(R.id.yes_exist_radio_button);
        no_exist = findViewById(R.id.no_exist_radio_button);
        one_star = findViewById(R.id.one_star_button);
        two_star = findViewById(R.id.two_star_button);
        three_star = findViewById(R.id.three_star_button);
        four_star = findViewById(R.id.four_star_button);
        five_star = findViewById(R.id.five_star_button);
        submit_button = findViewById(R.id.submit_button);

        textView_thank_you_for_contribution = findViewById(R.id.textView_thank_you_for_contribution);
        voting_layout = findViewById(R.id.voting_layout);


        ChargingStation chargingStation = get_charghingstation_from_db(marker_id);
        Double set_by_user_trust_score = get_user_trust_score(chargingStation);

        Boolean voted = check_if_aleready_voted(chargingStation);

        if (voted){
            voting_layout.setVisibility(View.GONE);
            textView_thank_you_for_contribution.setVisibility(View.VISIBLE);
        }
        else {
            voting_layout.setVisibility(View.VISIBLE);
            textView_thank_you_for_contribution.setVisibility(View.GONE);
        }

        textView_name_info.setText("The name is: " + chargingStation.get_name());

        if (chargingStation.get_guarded() == 1)
            textView_is_guarded_info.setText("Guarded: " + "Yes");
        else
            textView_is_guarded_info.setText("Guarded" +"No");

        textView_parking_spots_info.setText("Number of parking spots: " + chargingStation.get_parking_number_of_places().toString());

        if (chargingStation.get_type2() == 1)
            textView_type2_info.setText( "Type2: " + "Yes");
        else
            textView_type2_info.setText("Type2: " +"No");

        if (chargingStation.get_wall() == 1)
            textView_wall_info.setText("Wall: " + "Yes");
        else
            textView_wall_info.setText("Wall: " + "No");

        if (chargingStation.get_supercharger() == 1)
            textView_supercharger_info.setText("Supercharger: " + "Yes");
        else
            textView_supercharger_info.setText("Supercharger: "+"No");

        textView_output_kwh_info.setText("Output: " + chargingStation.get_outputkwh().toString() + "kW/h");

        rating.setText("Rating: " + chargingStation.get_rating() + " stars");

        if (set_by_user_trust_score < 33)
            trust_score.setText("Set by an user with low trust score");
        else if (set_by_user_trust_score > 66)
            trust_score.setText("Set by an user with high trust score");
        else
            trust_score.setText("Set by an user with medium trust score");


        one_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_number_pressed = 1;

                one_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                two_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                three_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                four_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                five_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
            }
        });

        two_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_number_pressed = 2;

                one_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                two_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                three_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                four_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                five_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));

            }
        });

        three_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_number_pressed = 3;

                one_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                two_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                three_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                four_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                five_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
            }
        });

        four_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_number_pressed = 4;

                one_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                two_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                three_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                four_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                five_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
            }
        });

        five_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_number_pressed = 5;

                one_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                two_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                three_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                four_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                five_star.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CurrentUser currentUser = CurrentUser.getInstance();

                if (currentUser.get_is_logged()) {

                    Boolean malicious_user = null;

                    if (yes_exist.isChecked())
                        malicious_user = true;

                    if (no_exist.isChecked())
                        malicious_user = false;

                    if (malicious_user == null || star_number_pressed == null) {
                        Toast.makeText(MarkerInfoActivity.this, "All fields are required for submit", Toast.LENGTH_SHORT).show();
                    } else {
                        add_rating(star_number_pressed, chargingStation);
                        add_malicious(malicious_user, chargingStation);
                        add_vote(chargingStation);
                        Toast.makeText(MarkerInfoActivity.this, "Thank you for your contribution", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    Toast.makeText(MarkerInfoActivity.this, "You have to be logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean check_if_aleready_voted(ChargingStation chargingStation){
        CurrentUser currentUser = CurrentUser.getInstance();
        if (!currentUser.get_is_logged())
            return false;

        ConnectionDB connectionDB = ConnectionDB.getInstance();
        Connection connection = connectionDB.getConnection();

        if (connection != null){
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT email_who, id_station FROM votes WHERE email_who LIKE '" + currentUser.get_email() + "' AND id_station LIKE " + chargingStation.get_id());

                    if (resultSet.next())
                        return true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Toast.makeText(MarkerInfoActivity.this, "SQL statement error", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        else {
            Toast.makeText(MarkerInfoActivity.this, "No connection", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void add_vote(ChargingStation chargingStation){
        CurrentUser currentUser = CurrentUser.getInstance();

        ConnectionDB connectionDB = ConnectionDB.getInstance();
        Connection connection = connectionDB.getConnection();

        if (connection != null){

            try{
                Statement statement = connection.createStatement();
                statement.executeUpdate("INSERT INTO votes(email_who, id_station) VALUES ('" + currentUser.get_email() + "'," + chargingStation.get_id() + ")");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Toast.makeText(MarkerInfoActivity.this, "SQL statement error", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void add_rating(Integer star_number_pressed, ChargingStation chargingStation){
        ConnectionDB connectionDB = ConnectionDB.getInstance();
        Connection connection = connectionDB.getConnection();

        if (connection != null){

            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT rating, how_many_people_rated FROM chargingstations WHERE id LIKE " + chargingStation.get_id());
                resultSet.next();

                Double rating = resultSet.getDouble(1);
                Integer how_many_people_rated = resultSet.getInt(1);

                rating = ((rating * how_many_people_rated) + star_number_pressed) / (how_many_people_rated + 1);
                how_many_people_rated += 1;

                statement.executeUpdate("UPDATE chargingstations SET rating=" + rating + ", how_many_people_rated=" + how_many_people_rated + " WHERE id LIKE " + chargingStation.get_id());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Toast.makeText(this, "SQL statement error", Toast.LENGTH_SHORT).show();
            }

        }
        else
            Toast.makeText(MarkerInfoActivity.this, "No connection", Toast.LENGTH_SHORT).show();
    }

    private void add_malicious(Boolean malicious, ChargingStation chargingStation){

        ConnectionDB connectionDB = ConnectionDB.getInstance();
        Connection connection = connectionDB.getConnection();

        if (connection != null){

            try {

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT trust_score, how_many_people_voted FROM users WHERE email LIKE '" + chargingStation.get_set_by_user() + "'");
                resultSet.next();
                Double trust_score = resultSet.getDouble(1);
                Integer how_many_people_voted = resultSet.getInt(2);

                if (malicious) {
                    trust_score = ((trust_score * how_many_people_voted) + 0) / (how_many_people_voted + 1);
                    how_many_people_voted += 1;

                }
                else{
                    trust_score = ((trust_score * how_many_people_voted) + 100) / (how_many_people_voted + 1);
                    how_many_people_voted +=1;

                }
                statement.executeUpdate("UPDATE users SET  trust_score=" + trust_score + ", how_many_people_voted=" + how_many_people_voted + " WHERE email LIKE '" + chargingStation.get_set_by_user() + "'");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Toast.makeText(MarkerInfoActivity.this, "SQL statement error", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private ChargingStation get_charghingstation_from_db(Integer searched_id){

        ConnectionDB connectionDB = ConnectionDB.getInstance();
        Connection connection = connectionDB.getConnection();
        ChargingStation aux = new ChargingStation();

        if (connection != null) {

            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT id, name, set_by_user, x_coordinate, y_coordinate, guarded, parking_number_of_places, type2, wall, supercharger, outputkwh, rating, how_many_people_rated FROM chargingstations WHERE id LIKE " + searched_id.toString());

                if (resultSet.next()) {
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
                    Double rating = resultSet.getDouble(12);
                    Integer how_many_people_rated = resultSet.getInt(13);

                    aux.set_values(id, name, set_by_user, x_coordinate, y_coodinate, guarded, parking_number_of_places, type2, wall, supercharger, outputkwh, rating, how_many_people_rated);
                }
                else
                    Toast.makeText(this, "No results (bug)", Toast.LENGTH_SHORT).show();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Toast.makeText(this, "SQL statement error", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
        }
        return aux;
    }

    private Double get_user_trust_score(ChargingStation chargingStation){
        ConnectionDB connectionDB = ConnectionDB.getInstance();
        Connection connection = connectionDB.getConnection();

        Double trust_score = 0.;

        if (connection != null){

            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT trust_score FROM chargingstations ch JOIN users us ON (ch.set_by_user = us.email) WHERE ch.set_by_user LIKE '" + chargingStation.get_set_by_user() + "'");
                resultSet.next();

                trust_score = resultSet.getDouble(1);
                return trust_score;

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Toast.makeText(this, "SQL statement error", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "No connection",Toast.LENGTH_SHORT).show();
        }

        return trust_score;
    }
}