package com.example.juiceup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private EditText car_max_km_range;
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
        car_max_km_range = findViewById(R.id.edit_car_max_km_range);


        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CriptographyService criptographyService = new CriptographyService();

               String password = editText_password.getText().toString();
               String salt = criptographyService.get_random_salt();
               password = criptographyService.hash(password, salt);

               String email = editText_email.getText().toString();
               String first_name = editText_first_name.getText().toString();
               String last_name = editText_last_name.getText().toString();
               Integer car_max_km_range_value = Integer.parseInt(car_max_km_range.getText().toString());
               Integer trust = 50;

               CurrentUser currentUser = CurrentUser.getInstance();
               currentUser.set_details(email, password, salt, first_name, last_name, car_max_km_range_value, trust);

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

                               statement.executeUpdate("INSERT INTO users(email, password, salt, last_name, first_name, car_max_km_range, trust_score) VALUES(" +
                                       "'" + currentUser.get_email() + "','" + currentUser.get_password() + "','" + currentUser.get_salt() +
                                       "','" + currentUser.get_last_name() + "','" + currentUser.get_first_name() + "'," + currentUser.get_car_max_km_range() + "," +
                                       currentUser.get_trust_score() + ")");

                               create_account_TextView.setText("Sign up succesfull");

                           }
                       } catch (SQLException e) {
                           e.printStackTrace();
                           create_account_TextView.setText("SQL statement error");
                       }

                   } else {
                       create_account_TextView.setText("Connection is null");
                   }
               }
               else{
                   create_account_TextView.setText("Not a valid mail");
               }
            }
        });

    }
}