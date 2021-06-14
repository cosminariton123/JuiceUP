package com.example.juiceup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ViewPreferencesActivity extends AppCompatActivity {

    private TextView textView_max_km_pref;
    private TextView textView_nr_of_parking_spots_pref;
    private TextView textView_guarded_pref;
    private TextView textView_type2_pref;
    private TextView textView_wall_pref;
    private TextView textView_supercharger_pref;
    private TextView textView_min_kwh_pref;
    private TextView textView_min_rating_pref;
    private TextView textView_min_trust_score_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_preferences);
        setTitle("Your preferences");

        textView_max_km_pref = findViewById(R.id.textView_max_km_pref);
        textView_nr_of_parking_spots_pref = findViewById(R.id.textView_nr_of_parking_spots_pref);
        textView_guarded_pref = findViewById(R.id.textView_guarded_pref);
        textView_type2_pref = findViewById(R.id.textView_type2_pref);
        textView_wall_pref = findViewById(R.id.textView_wall_pref);
        textView_supercharger_pref = findViewById(R.id.textView_supercharger_pref);
        textView_min_kwh_pref = findViewById(R.id.textView_min_kwh_pref);
        textView_min_rating_pref = findViewById(R.id.textView_min_rating_pref);
        textView_min_trust_score_pref = findViewById(R.id.textView_min_trust_score_pref);

        CurrentUser currentUser = CurrentUser.getInstance();

        textView_max_km_pref.setText("Maximum distance between stations:\n" + currentUser.get_car_max_km_range().toString() + "km");
        textView_nr_of_parking_spots_pref.setText("Minimum number of parking spots:\n" + currentUser.get_of_parking_spots_preference().toString());
        textView_guarded_pref.setText("Guarded:\n" + convert_integer_into_true_false_string(currentUser.get_guarded_preference()).toString());
        textView_type2_pref.setText("Must have type2:\n" + convert_integer_into_true_false_string(currentUser.get_type2_preference()).toString());
        textView_wall_pref.setText("Must have wall:\n" + convert_integer_into_true_false_string(currentUser.get_wall_preference()).toString());
        textView_supercharger_pref.setText("Must have supercharger:\n" + convert_integer_into_true_false_string(currentUser.get_supercharger_preference()).toString());
        textView_min_kwh_pref.setText("Minimum charghing speed:\n" + currentUser.get_min_kwh_preference().toString() + " kwh");
        textView_min_rating_pref.setText("Minimum rating:\n " + currentUser.get_min_rating_preference() + " stars");

        if (currentUser.get_min_trust_preference() == 1)
            textView_min_trust_score_pref.setText("Minimum trust:\n" + "Low trusted sources");

        if (currentUser.get_min_trust_preference() == 2)
            textView_min_trust_score_pref.setText("Minimum trust:\n" + "Medium trusted sources");

        if (currentUser.get_min_trust_preference() == 3 )
            textView_min_trust_score_pref.setText("Minimum trust\n" + "High trusted sources");
    }

    public String convert_integer_into_true_false_string(Integer input){
        if (input == 1)
            return "Yes";
        return "No";
    }
}