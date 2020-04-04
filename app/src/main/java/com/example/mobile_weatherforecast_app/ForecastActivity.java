package com.example.mobile_weatherforecast_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

@SuppressLint("Registered")
public class ForecastActivity extends Activity {

    String CITY;
    TextView day1, day2, day3, day4, day5, minTemp1, minTemp2, minTemp3, minTemp4, minTemp5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        Intent intent = getIntent();
        CITY = intent.getStringExtra("CITY");

        day1 = findViewById(R.id.day1);
        day2 = findViewById(R.id.day2);
        day3 = findViewById(R.id.day3);
        day4 = findViewById(R.id.day4);
        day5 = findViewById(R.id.day5);
        minTemp1 = findViewById(R.id.maxTemp1);
        minTemp2 = findViewById(R.id.maxTemp2);
        minTemp3 = findViewById(R.id.maxTemp3);
        minTemp4 = findViewById(R.id.maxTemp4);
        minTemp5 = findViewById(R.id.maxTemp5);

        forecastTask ft = new forecastTask();
        ft.execute();

    }

    @SuppressLint("StaticFieldLeak")
    class forecastTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String API = "8118ed6ee68db2debfaaa5a44c832918";
            String response;
            HttpDataHandler http = new HttpDataHandler();
            String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + CITY + "&appid=" + API;
            response = http.getHttpData(url);
            return response;
        }
    }
}
