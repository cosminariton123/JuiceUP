package com.example.juiceup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private TextView create_account_TextView;
    private EditText editText_email;
    private EditText editText_password;
    private EditText editText_first_name;
    private EditText editText_last_name;
    private Button sign_up_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        create_account_TextView = findViewById(R.id.create_account_TextView);
        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        editText_first_name = findViewById(R.id.editText_first_name);
        editText_last_name = findViewById(R.id.editText_last_name);
        sign_up_button = findViewById(R.id.sign_up_button);


        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText_email.getText().toString().equals("") || editText_password.getText().toString().equals("") || editText_first_name.getText().toString().equals("") ||
                        editText_last_name.getText().toString().equals(""))
                    create_account_TextView.setText("All fields are required");
                else {


                    CriptographyService criptographyService = new CriptographyService();

                    String password = editText_password.getText().toString();
                    String salt = criptographyService.get_random_salt();
                    password = criptographyService.hash(password, salt);

                    String email = editText_email.getText().toString();
                    String first_name = editText_first_name.getText().toString();
                    String last_name = editText_last_name.getText().toString();
                    Integer trust = 50;

                    CurrentUser currentUser = CurrentUser.getInstance();
                    currentUser.set_details(email, password, salt, first_name, last_name, 0, 0, 0, 0, 0
                            , 0, 0, trust);

                    ConnectionDB database = ConnectionDB.getInstance();
                    Connection connection = database.getConnection();

                    Pattern email_pattern = Pattern.compile("^[^@]+@[^@.]+[.][^@]+$");
                    Matcher email_matcher = email_pattern.matcher(email);

                    if (email_matcher.matches()) {
                        if (connection != null) {

                            try {
                                Statement statement = connection.createStatement();
                                ResultSet resultSet = statement.executeQuery("SELECT email FROM users WHERE email like '" + email + "'");
                                if (resultSet.next()) {
                                    create_account_TextView.setText("There exists an account with this email. Try again or login.");
                                    currentUser.logout();
                                } else {

                                    statement.executeUpdate("INSERT INTO users(email, password, salt, last_name, first_name, car_max_km_range, guarded_place_preference, nr_of_parking_spots_preference, type2_preference, wall_preference, supercharger_preference, min_kwh_preference, trust_score) VALUES(" +
                                            "'" + currentUser.get_email() + "','" + currentUser.get_password() + "','" + currentUser.get_salt() +
                                            "','" + currentUser.get_last_name() + "','" + currentUser.get_first_name() + "'," + currentUser.get_car_max_km_range() + "," + currentUser.get_guarded_preference() + "," + currentUser.get_of_parking_spots_preference() + "," + currentUser.get_type2_preference() + "," + currentUser.get_wall_preference() + "," + currentUser.get_supercharger_preference() + "," + currentUser.get_min_kwh_preference() + "," +
                                            currentUser.get_trust_score() + ")");

                                    Toast.makeText(SignUpActivity.this, "Sign up succesfull", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(SignUpActivity.this, AccountPreferencesActivity.class));
                                    finish();

                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                create_account_TextView.setText("SQL statement error");
                            }

                        } else {
                            create_account_TextView.setText("Connection is null");
                        }
                    } else {
                        create_account_TextView.setText("Not a valid mail");
                    }
                }
            }
        });

    }
}