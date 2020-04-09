package com.example.mobile_weatherforecast_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.time.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("Registered")
public class ForecastActivity extends Activity {

    String CITY;
    TextView[] day = new TextView[5];
    TextView[] minTemp = new TextView[5];
    TextView[] maxTemp = new TextView[5];
    ImageView[] icon = new ImageView[5];

    static class Struct {
        public String cityName;
        public String logDate;
        public int maxTemp;
        public int minTemp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        Intent intent = getIntent();
        CITY = intent.getStringExtra("CITY");

//        day[0] = findViewById(R.id.day1);
        day[1] = findViewById(R.id.day2);
        day[2] = findViewById(R.id.day3);
        day[3] = findViewById(R.id.day4);
        day[4] = findViewById(R.id.day5);
//        minTemp[0] = findViewById(R.id.minTemp1);
        minTemp[1] = findViewById(R.id.minTemp2);
        minTemp[2] = findViewById(R.id.minTemp3);
        minTemp[3] = findViewById(R.id.minTemp4);
        minTemp[4] = findViewById(R.id.minTemp5);
//        maxTemp[0] = findViewById(R.id.maxTemp1);
        maxTemp[1] = findViewById(R.id.maxTemp2);
        maxTemp[2] = findViewById(R.id.maxTemp3);
        maxTemp[3] = findViewById(R.id.maxTemp4);
        maxTemp[4] = findViewById(R.id.maxTemp5);
//        icon[0] = findViewById(R.id.imageView1);
        icon[1] = (ImageView) findViewById(R.id.imageView1);
        icon[2] = (ImageView) findViewById(R.id.imageView2);
        icon[3] = (ImageView) findViewById(R.id.imageView3);
        icon[4] =(ImageView)  findViewById(R.id.imageView4);

//        boolean isFilePresent = isFilePresent(this, "storage.json");
//        if(isFilePresent) {
//            String jsonString = read(this);
//            //do the json parsing here and do the rest of functionality of app
//        } else {
//            create(this);
//            if(isFileCreated) {
//                //proceed with storing
//            } else {
//                //show error or try again.
//            }
//        }

        forecastTask ft = new forecastTask();
        ft.execute();

    }

//    public static class WriteObjectFile {
//
//        private Context parent;
//        private ObjectInputStream objectIn;
//        private ObjectOutputStream objectOut;
//        private Object outputObject;
//        private String filePath;
//
////        WriteObjectFile(){
////            parent = this;
////        }
//
//        public Object readObject(String fileName){
//            try {
//                filePath = parent.getApplicationContext().getFilesDir().getAbsolutePath() + "/" + fileName;
//                FileInputStream fileIn = new FileInputStream(filePath);
//                objectIn = new ObjectInputStream(fileIn);
//                outputObject = objectIn.readObject();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } finally {
//                if (objectIn != null) {
//                    try {
//                        objectIn.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return outputObject;
//        }
//
//        public void writeObject(Object inputObject, String fileName){
//            try {
//                filePath = parent.getApplicationContext().getFilesDir().getAbsolutePath() + "/" + fileName;
//                FileOutputStream fileOut = new FileOutputStream(filePath);
//                objectOut = new ObjectOutputStream(fileOut);
//                objectOut.writeObject(inputObject);
//                fileOut.getFD().sync();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (objectOut != null) {
//                    try {
//                        objectOut.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }

    @SuppressLint("StaticFieldLeak")
    class forecastTask extends AsyncTask<String, Void, String> {

        List<Struct> toSave = new ArrayList<>();
        SharedPreferences sharedPref = ForecastActivity.this.getPreferences(Context.MODE_PRIVATE);

//        @Override
//        protected void onPreExecute() {
//            super .onPreExecute();
//
//            findViewById(R.id.loader).setVisibility(View.VISIBLE);
//            findViewById(R.id.mainContainer).setVisibility(View.GONE);
//            findViewById(R.id.errorText).setVisibility(View.GONE);
//        }

        @Override
        protected String doInBackground(String... strings) {
            String APIKey = "8118ed6ee68db2debfaaa5a44c832918";
            String response;
            HttpDataHandler http = new HttpDataHandler();
            String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + CITY + "&appid=" + APIKey;
            response = http.getHttpData(url);
            return response;
        }

//        public void initiateSaving() {
//            Gson gson = new Gson();
//            String data = sharedPref.getString()
//        }

        private void saveData() {
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("weatherData", new Gson().toJson(toSave));
            editor.apply();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint({"SetTextI18n", "WrongConstant"})
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                // for each 3 hours we have a different data set, cnt is the number of all data we have
//                JSONObject cnt = jsonObj.getJSONObject("cnt");
                int count = Integer.parseInt(String.valueOf(jsonObj.get("cnt")));
                // list is the list that contains all data
                JSONArray list = jsonObj.getJSONArray("list");
                String[] currentDateAndTime =
                        String.valueOf(list.getJSONObject(0).get("dt_txt")).split("\\s+");
                String currentDate = currentDateAndTime[0];
                int daysIndex = 0;
                // temporary minimum temperature
                float tempMinTemp = Float.MAX_VALUE;
                // same
                float tempMaxTemp = Float.MIN_VALUE;
                String description = "";
                int i = 0;
                while (i < count) {
                    JSONObject object = list.getJSONObject(i);
                    String[] dateAndTime =
                            String.valueOf(object.get("dt_txt")).split("\\s+");
                    String date = dateAndTime[0];
                    //if (dateAndTime[1].equals("12:00"))
                        description =
                                (String) object.getJSONArray("weather").getJSONObject(0).get("description");
                    if (date.equals(currentDate)) {
                        // i'm dying btw
                        // temporary minimum (that naming scheme tho)
                        float tempMin =
                                Float.parseFloat(String.valueOf(object.getJSONObject("main").get("temp_min")));
                        if (tempMin < tempMinTemp)
                            tempMinTemp = tempMin;
                        // same
                        float tempMax =
                                Float.parseFloat(String.valueOf(object.getJSONObject("main").get("temp_max")));
                        if (tempMax > tempMaxTemp)
                            tempMaxTemp = tempMax;
                        ++i;
                    } else {
                        if (daysIndex != 0) {
                            minTemp[daysIndex].setText(Integer.toString((int) (tempMinTemp - 273.15)) + "°C");
                            maxTemp[daysIndex].setText(Integer.toString((int) (tempMaxTemp - 273.15)) + "°C");

//                            JSONParser jsonParser = new JSONParser();
//                            FileReader reader = new FileReader("offlineBase.json");
//
//                            Object obj = jsonParser.parse(reader);
//                            JSONObject dataObj = (JSONObject) obj;
//                            JSONObject cityObj = new JSONObject();
//                            cityObj.put("city_name", CITY);
//                            cityObj.put("date", currentDate);
//                            cityObj.put("min_temp", (int) (tempMinTemp - 273.15));
//                            cityObj.put("max_temp", (int) (tempMaxTemp - 273.15));

                            Struct struct = new Struct();
                            struct.cityName = CITY;
                            struct.logDate = currentDate;
                            struct.minTemp = (int) (tempMinTemp - 273.15);
                            struct.maxTemp = (int) (tempMaxTemp - 273.15);

                            toSave.add(struct);

//                            JSONArray toWrite = new JSONArray();
//                            toWrite.put(cityObj);


                        }
                        tempMinTemp = Float.MAX_VALUE;
                        tempMaxTemp = Float.MIN_VALUE;
                        LocalDate localDate = LocalDate.parse(currentDate);
                        DayOfWeek dayOfWeek = DayOfWeek.from(localDate);
                        int currentDayOfWeek = dayOfWeek.getValue();
                        if (daysIndex != 0) {
                            switch (currentDayOfWeek) {
                                case 1:
                                    day[daysIndex].setText("MON");
                                    break;
                                case 2:
                                    day[daysIndex].setText("TUE");
                                    break;
                                case 3:
                                    day[daysIndex].setText("WED");
                                    break;
                                case 4:
                                    day[daysIndex].setText("THU");
                                    break;
                                case 5:
                                    day[daysIndex].setText("FRI");
                                    break;
                                case 6:
                                    day[daysIndex].setText("SAT");
                                    break;
                                case 7:
                                    day[daysIndex].setText("SUN");
                                    break;
                            }
                        }
                        if (daysIndex != 0) {
//                            String weatherDescription = "";
//                            int id =
//
                            System.out.println("im insiiiide here --------------------------------------------");
                            int id;
                            //-------------------------------------------------------------------------
                            // the picasso part
//
//                            String icons =  (String) object.getJSONArray("weather").getJSONObject(0).get("icon");
//                            String iconUrl = "http://openweathermap.org/img/w/" + icons + ".png";
//                            Picasso.get().load(iconUrl).into(icon[daysIndex]);

                            //-------------------------------------------------------------------------
                            
                            switch (description) {
                                case "clear sky":
                                    id = getResources().getIdentifier("clear2", "drawable", getPackageName());
                                    icon[daysIndex].setImageResource(id);
                                    break;
                                case "broken clouds":
                                    id = getResources().getIdentifier("brokenclouds2", "drawable", getPackageName());
                                    icon[daysIndex].setImageResource(id);
                                    break;
                                case "light rain":
                                    id = getResources().getIdentifier("rain2", "drawable", getPackageName());
                                    icon[daysIndex].setImageResource(id);
                                    break;
                                case "moderate rain":
                                    id = getResources().getIdentifier("showerrain2", "drawable", getPackageName());
                                    icon[daysIndex].setImageResource(id);
                                    break;
                                case "thunderstorm":
                                    id = getResources().getIdentifier("thunderstorm", "drawable", getPackageName());
                                    icon[daysIndex].setImageResource(id);
                                    break;
                                case "few clouds":
                                    id = getResources().getIdentifier("fewclouds2", "drawable", getPackageName());
                                    icon[daysIndex].setImageResource(id);
                                    break;
                                case "mist":
                                    id = getResources().getIdentifier("mist2", "drawable", getPackageName());
                                    icon[daysIndex].setImageResource(id);
                                    break;
                                case "scattered clouds":
                                    id = getResources().getIdentifier("scatteredclouds2", "drawable", getPackageName());
                                    icon[daysIndex].setImageResource(id);
                                    break;
                                case "snow":
                                    id = getResources().getIdentifier("snow2", "drawable", getPackageName());
                                    icon[daysIndex].setImageResource(id);
                                    break;
                            }
                        }
                        ++daysIndex;
                        currentDate = date;
                    }
                }
                saveData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
