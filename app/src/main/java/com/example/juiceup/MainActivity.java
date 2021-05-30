package com.example.juiceup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
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
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {


    Button view_map_button;
    Button sign_button;

    Button bd_button_test;
    TextView bd_text_view_test;
    TextView bd_text_view_test_2;
    TextView bd_text_view_test_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        view_map_button = findViewById(R.id.view_map_button);
        sign_button = findViewById(R.id.sign_button);

        bd_button_test = findViewById(R.id.bd_button_test);
        bd_text_view_test = findViewById(R.id.bd_text_view_test);
        bd_text_view_test_2 = findViewById(R.id.bd_text_view_test_2);
        bd_text_view_test_3 = findViewById(R.id.bd_text_view_test_3);



        bd_button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String ip = "192.168.56.1";
                String port = "1433";
                String Clases = "net.sourceforge.jtds.jdbc.Driver";
                String database = "juiceupdatabase";
                String username = "client";
                String password = "123";
                String url = "jdbc:jtds:sqlserver://" +ip + ":" + port + "/" + database;

                Connection connection = null;






                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                try {

                    Class.forName(Clases);
                    connection = DriverManager.getConnection(url, username, password);
                    bd_text_view_test.setText("Succes");
                }
                catch (ClassNotFoundException e){
                    e.printStackTrace();
                    bd_text_view_test.setText("Error");
                } catch (SQLException e) {
                    e.printStackTrace();
                    bd_text_view_test.setText("Failure");
                }







                if (connection != null){

                    try {
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
                        while (resultSet.next()){
                            bd_text_view_test.setText(resultSet.getString(1));
                        }
                    }

                    catch (SQLException e){
                        e.printStackTrace();
                    }

                }

                else{
                    bd_text_view_test.setText("Connection is null");
                }
            }
        });
    }
}