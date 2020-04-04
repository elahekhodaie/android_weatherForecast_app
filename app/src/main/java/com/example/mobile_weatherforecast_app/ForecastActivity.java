package com.example.mobile_weatherforecast_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("Registered")
public class ForecastActivity extends Activity {

    String CITY;
    TextView[] day = new TextView[5];
    TextView[] minTemp = new TextView[5];
    TextView[] maxTemp = new TextView[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        Intent intent = getIntent();
        CITY = intent.getStringExtra("CITY");

        day[0] = findViewById(R.id.day1);
        day[1] = findViewById(R.id.day2);
        day[2] = findViewById(R.id.day3);
        day[3] = findViewById(R.id.day4);
        day[4] = findViewById(R.id.day5);
        minTemp[0] = findViewById(R.id.minTemp1);
        minTemp[1] = findViewById(R.id.minTemp2);
        minTemp[2] = findViewById(R.id.minTemp3);
        minTemp[3] = findViewById(R.id.minTemp4);
        minTemp[4] = findViewById(R.id.minTemp5);
        maxTemp[0] = findViewById(R.id.maxTemp1);
        maxTemp[1] = findViewById(R.id.maxTemp2);
        maxTemp[2] = findViewById(R.id.maxTemp3);
        maxTemp[3] = findViewById(R.id.maxTemp4);
        maxTemp[4] = findViewById(R.id.maxTemp5);

        forecastTask ft = new forecastTask();
        ft.execute();

    }

    @SuppressLint("StaticFieldLeak")
    class forecastTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String APIKey = "8118ed6ee68db2debfaaa5a44c832918";
            String response;
            HttpDataHandler http = new HttpDataHandler();
            String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + CITY + "&appid=" + APIKey;
            response = http.getHttpData(url);
            return response;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                // for each 3 hours we have a different data set, cnt is the number of all data we have
                JSONObject cnt = jsonObj.getJSONObject("cnt");
                int count = Integer.parseInt(String.valueOf(cnt));
                // list is the list that contains all data
                JSONObject list = jsonObj.getJSONObject("list");
                String[] currentDateAndTime =
                        String.valueOf(list.getJSONObject("1").getJSONObject("dt_txt")).split("\\s+");
                String currentDate = currentDateAndTime[0];
                int daysIndex = 0;
                int numberOfTemps = 0;
                // sum of minimum temperatures
                int sumMinTemp = 0;
                // same
                int sumMaxTemp = 0;
                int i = 1;
                while (i < count) {
                    JSONObject object = list.getJSONObject(Integer.toString(i));
                    String[] dateAndTime =
                            String.valueOf(object.getJSONObject("dt_txt")).split("\\s+");
                    String date = dateAndTime[0];
                    if (date.equals(currentDate)) {
                        // i'm dying btw
                        sumMinTemp +=
                                Integer.parseInt(String.valueOf(object.getJSONObject("main").getJSONObject("temp_min")));
                        sumMaxTemp +=
                                Integer.parseInt(String.valueOf(object.getJSONObject("main").getJSONObject("temp_max")));
                        ++numberOfTemps;
                        ++i;
                    } else {
                        int tempMinTemp = sumMinTemp / numberOfTemps;
                        int tempMaxTemp = sumMaxTemp / numberOfTemps;
                        numberOfTemps = 0;
                        sumMinTemp = 0;
                        sumMaxTemp = 0;
                        minTemp[daysIndex].setText(Integer.toString(tempMinTemp));
                        maxTemp[daysIndex].setText(Integer.toString(tempMaxTemp));
                        ++daysIndex;
                        currentDate = date;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
