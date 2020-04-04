package com.example.mobile_weatherforecast_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

@SuppressLint("Registered")
public class ForecastActivity extends Activity {

    TextView day1, day2, day3, day4, day5, temp1, temp2, temp3, temp4, temp5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        day1 = findViewById(R.id.day1);
        day2 = findViewById(R.id.day2);
        day3 = findViewById(R.id.day3);
        day4 = findViewById(R.id.day4);
        day5 = findViewById(R.id.day5);
        temp1 = findViewById(R.id.temp1);
        temp2 = findViewById(R.id.temp2);
        temp3 = findViewById(R.id.temp3);
        temp4 = findViewById(R.id.temp4);
        temp5 = findViewById(R.id.temp5);

    }
}
