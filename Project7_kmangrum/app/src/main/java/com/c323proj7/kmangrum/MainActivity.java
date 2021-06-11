package com.c323proj7.kmangrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

//    Create variables that will be used later
//    This includes the layout elements, Location listener & manager, progressDialog box, and the apiUrl
    EditText edLocationInput;
    TextView tvTodaysWeather, tvWeatherDescription, tvTemperature, tvFeelsLike;
    ImageView ivWeather;
    Button getWeatherButton, movieButton;
    LocationManager locationManager;
    LocationListener locationListener;
    ProgressDialog progressDialog;
    String jsonData;
    String apiURL = "https://api.openweathermap.org/data/2.5/weather?q={cityname}&appid=dc37290843d51945d55cae5113ef391a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Set the layout variables to the corresponding layout items
        edLocationInput = findViewById(R.id.edLocation);
        tvTodaysWeather = findViewById(R.id.tvTodayWeather);
        tvWeatherDescription = findViewById(R.id.tvDescription);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvFeelsLike = findViewById(R.id.tvFeelsLike);
        ivWeather = findViewById(R.id.ivWeatherIcon);
        getWeatherButton = findViewById(R.id.weatherButton);
        movieButton = findViewById(R.id.movieButton);

    }

//  function that retrieves the weather information for either the city name the user input or based on the location of the device
    public void getWeatherData(View view) {
//        Get text input from user
        String cityName = edLocationInput.getText().toString();

//        If the input isn't missing, connect to the network and modify the api url to include the name of the city
        if (!cityName.equals("")) {
            progressDialog = ProgressDialog.show(this, "City weather", "Connecting, please wait...", true, true);
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            String apiURLcityName = apiURL.replaceAll("\\{cityname\\}", cityName);

            if (networkInfo != null && networkInfo.isConnected()) {
//                make use of class that extends Asynctask to use the weather API
                new DownloadDataTask().execute(apiURLcityName);
            } else {
                Toast.makeText(this, "No network connection available", Toast.LENGTH_SHORT).show();
            }
        } else {
            getCurrentLocation(view);
        }
    }

//    Create class that extends AsyncTask to get information from weather API
    private class DownloadDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
//            Get the data from the API
            jsonData = downloadFromURL(urls[0]);
            return jsonData;
        }

        private String downloadFromURL(String url) {
            InputStream is = null;
            StringBuffer result = new StringBuffer();
            URL myURL = null;
            try {
//                Make a connection to the api link
                myURL = new URL(url);
                HttpsURLConnection connection = (HttpsURLConnection) myURL.openConnection();
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTPS error code: " + responseCode);
                }
                is = connection.getInputStream();
//                Once connected, dismiss the progressDialog box
                progressDialog.dismiss();

//                Read in the information from the api that is in JSON format in as a string
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String line = "";
                while ((line  = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        }

//        For after the api connection is made
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Create variables to store information and modify its format to meet my needs
            JSONArray weatherArray;
            JSONObject weatherObject;
            JSONObject mainObject;
            String weather = "";
            String weatherDescription;
            String temperature;
            long convertedTemp;
            long convertedFeelsLike;
            String feelsLike;

//            Convert the data from the api into JSON objects and JSON array
            try {
                JSONObject jsonObject = new JSONObject(s);
                weatherArray = new JSONArray(String.valueOf(jsonObject.getJSONArray("weather")));
                weatherObject = weatherArray.getJSONObject(0);
                weather = weatherObject.optString("main");
                weatherDescription = weatherObject.optString("description");

//                Get the information I need out of the JSON objects
                mainObject = new JSONObject(jsonObject.optString("main"));
                temperature = mainObject.optString("temp");
//                Convert temperatures from kelvin to fahrenheit
                convertedTemp = Math.round((Double.parseDouble(temperature) - 273.15) * (9/5) + 32);
                feelsLike = mainObject.optString("feels_like");
                convertedFeelsLike = Math.round((Double.parseDouble(feelsLike) - 273.15)* (9/5) + 32);

//              Set the text of the textviews in the layout to display the weather information
                tvTodaysWeather.setText("Today's Weather: " + weather);
                tvWeatherDescription.setText("Description: " + weatherDescription);
                tvTemperature.setText("Temperature: " + convertedTemp + "°F");
                tvFeelsLike.setText("Feels like: " + convertedFeelsLike + "°F");

//                Based on the weather description, set the imageview to have a related image
                if (weatherDescription.contains("clear")) {
                    ivWeather.setImageResource(R.drawable.clear);
                } else if (weatherDescription.contains("cloud")) {
                    ivWeather.setImageResource(R.drawable.cloud);
                } else if (weatherDescription.contains("rain") || weatherDescription.contains("drizzle")) {
                    ivWeather.setImageResource(R.drawable.rain);
                } else if (weatherDescription.contains("snow")) {
                    ivWeather.setImageResource(R.drawable.snow);
                } else if (weatherDescription.contains("storm")) {
                    ivWeather.setImageResource(R.drawable.storm);
                } else if (weatherDescription.contains("sun")) {
                    ivWeather.setImageResource(R.drawable.sun);
                } else if (weatherDescription.contains("wind")) {
                    ivWeather.setImageResource(R.drawable.windy);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


//  button click function that takes the user to the second (movie list) activity
    public void goToMovieActivity(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

//    Override method to create the overflow menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

//    Method that determines which menu option is chosen and starts the corresponding activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ConstraintLayout constraintLayout = findViewById(R.id.mainActivityLayout);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_weather:
                if (item.isCheckable()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                startActivity(new Intent(this, MainActivity.class));
                return true;

            case R.id.action_movie:
                if (item.isCheckable()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                startActivity(new Intent(this, SecondActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    //  Function that gets the current location
    public void getCurrentLocation(View view) {
//        Make sure that that locationManager provider and location is enabled
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            locationManager.isLocationEnabled();
        }

//        Create locationListener
        locationListener = new LocationListener() {

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            //            Function to get the location
            @Override
            public void onLocationChanged(@NonNull Location location) {
//                Get the latitude and longitude of device
                double myLat = location.getLatitude();
                double myLong = location.getLongitude();


//                Introduce geocoder so that we can use it to get the city and state based off of the latitude and longitude
                Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocation(myLat, myLong, 1);     // We only want one result from getLocation
                    String cityName = addresses.get(0).getLocality();                       // Use getLocality to get the city name

                    cityName = cityName.replaceAll("\\s+", " ");

                    edLocationInput.setText(cityName);

                    progressDialog = ProgressDialog.show(MainActivity.this, "City weather", "Connecting, please wait...", true, true);
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    String apiURLcityName = apiURL.replaceAll("\\{cityname\\}", cityName);
                    if (networkInfo != null && networkInfo.isConnected()) {
                        new DownloadDataTask().execute(apiURLcityName);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

//        Check to see if we have permission to use the devices location, if no ask for permission. Otherwise request the current location of the user
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 13);
            return;
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);
        }
    }



}