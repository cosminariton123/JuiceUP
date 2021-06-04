package com.example.juiceup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Currency;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {


    private Button view_map_button;
    private Button sign_in_button;
    private Button logout_button;

    private TextView sign_up_text_view;
    private TextView welcome_text_view;
    private TextView dont_have_account_text_view;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        view_map_button = findViewById(R.id.view_map_button);
        welcome_text_view = findViewById(R.id.welcome_text);
        sign_in_button = findViewById(R.id.sign_in_button);
        sign_up_text_view = findViewById(R.id.sign_up_text_view);
        dont_have_account_text_view = findViewById(R.id.dont_have_account_text_view);
        logout_button = findViewById(R.id.logout_button);



        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });


        sign_up_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });


        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentUser currentUser = CurrentUser.getInstance();
                currentUser.logout();

                onResume();
            }
        });

        view_map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        CurrentUser currentUser = CurrentUser.getInstance();
        if (currentUser.get_is_logged()){
            welcome_text_view.setText("Welcome to JuiceUP, " + currentUser.get_first_name());
            dont_have_account_text_view.setVisibility(View.GONE);
            sign_up_text_view.setVisibility(View.GONE);
            sign_in_button.setVisibility(View.GONE);
            logout_button.setVisibility(View.VISIBLE);
        }
        else{
            welcome_text_view.setText("Welcome to JuiceUP");
            dont_have_account_text_view.setVisibility(View.VISIBLE);
            sign_up_text_view.setVisibility(View.VISIBLE);
            sign_in_button.setVisibility(View.VISIBLE);
            logout_button.setVisibility(View.GONE);
        }
    }
}