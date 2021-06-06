package com.example.juiceup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.CryptoPrimitive;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {


    private Button login_register;
    private EditText edit_text_email;
    private EditText edit_text_password;
    private TextView enter_credentials_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        login_register = findViewById(R.id.login_button);
        edit_text_email = findViewById(R.id.editTextEmail);
        edit_text_password = findViewById(R.id.editTextPassword);
        enter_credentials_textview = findViewById(R.id.enter_credentials_TextView);


        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_address = edit_text_email.getText().toString();
                String password = edit_text_password.getText().toString();


                ConnectionDB database = ConnectionDB.getInstance();
                Connection connection = database.getConnection();


                if (connection != null){
                    try {

                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT email FROM users WHERE email LIKE '" + email_address + "'");

                        if (resultSet.next()) {
                            resultSet = statement.executeQuery("SELECT password, salt FROM users WHERE email LIKE '" + email_address + "'");
                            resultSet.next();

                            String stored_password = resultSet.getString(1);
                            String stored_salt = resultSet.getString(2);

                            CriptographyService criptographyService = new CriptographyService();
                            password = criptographyService.hash(password, stored_salt);

                            if (password.equals(stored_password)) {
                                enter_credentials_textview.setText("Authentification succesful!");

                                //Taking the rest of the information from the server
                                resultSet = statement.executeQuery("SELECT last_name, first_name, trust_score, car_max_km_range, guarded_place_preference, nr_of_parking_spots_preference, type2_preference, wall_preference, supercharger_preference, min_kwh_preference FROM users WHERE email LIKE '" + email_address + "'");
                                resultSet.next();

                                String last_name = resultSet.getString(1);
                                String first_name = resultSet.getString(2);
                                Integer trust_score = resultSet.getInt(3);
                                Integer car_max_km_range = resultSet.getInt(4);
                                Integer guarded_place_preference = resultSet.getInt(5);
                                Integer nr_of_parking_spots_preference = resultSet.getInt(6);
                                Integer type2_preference = resultSet.getInt(7);
                                Integer wall_preference = resultSet.getInt(8);
                                Integer supercharger_preference = resultSet.getInt(9);
                                Integer min_kwh_preference = resultSet.getInt(10);

                                CurrentUser currentUser = CurrentUser.getInstance();
                                currentUser.set_details(email_address, password, stored_salt, first_name, last_name, car_max_km_range, guarded_place_preference, nr_of_parking_spots_preference,
                                        type2_preference, wall_preference, supercharger_preference, min_kwh_preference, trust_score);
                                Toast.makeText(LoginActivity.this, "Authentification succesfull", Toast.LENGTH_SHORT).show();
                                finish();
                            } else
                                enter_credentials_textview.setText("Authentification failed(password). Try again");
                        }
                        else
                            enter_credentials_textview.setText("Authentification failed(username). Try again");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        enter_credentials_textview.setText("SQL statement error");
                    }
                }

                else{
                    enter_credentials_textview.setText("Check Internet connection");
                }
            }
        });
    }
}