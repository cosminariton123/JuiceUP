package com.example.juiceup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        textView_name_info = findViewById(R.id.textView_name_info);
        textView_is_guarded_info = findViewById(R.id.textView_is_guarded_info);
        textView_parking_spots_info = findViewById(R.id.textView_parking_spots_info);
        textView_type2_info = findViewById(R.id.textView_type2_info);
        textView_wall_info = findViewById(R.id.textView_wall_info);
        textView_supercharger_info = findViewById(R.id.textView_supercharger_info);
        textView_output_kwh_info = findViewById(R.id.textView_output_kwh_info);


        ChargingStation chargingStation = get_charghingstation_from_db(marker_id);

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
    }


    private ChargingStation get_charghingstation_from_db(Integer searched_id){

        ConnectionDB connectionDB = ConnectionDB.getInstance();
        Connection connection = connectionDB.getConnection();
        ChargingStation aux = new ChargingStation();

        if (connection != null) {

            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT id, name, set_by_user, x_coordinate, y_coordinate, guarded, parking_number_of_places, type2, wall, supercharger, outputkwh FROM chargingstations WHERE id LIKE " + searched_id.toString());

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

                    aux.set_values(id, name, set_by_user, x_coordinate, y_coodinate, guarded, parking_number_of_places, type2, wall, supercharger, outputkwh);
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
}