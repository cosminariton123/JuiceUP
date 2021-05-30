package com.example.juiceup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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


        sign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
                databaseConnection.set_int(30);
                bd_text_view_test_2.setText(databaseConnection.test().toString());
            }
        });

        bd_button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
                bd_text_view_test.setText(databaseConnection.test().toString());
            }
        });
    }
}