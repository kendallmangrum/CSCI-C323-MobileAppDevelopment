package com.example.project6_kmangrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

//    Kendall Mangrum (kmangrum)

// Create locationManager and Location Listener
    LocationManager myLocationManager;
    LocationListener myLocationListener;

//    Create variables for the textview and edit text fields for use later
    TextView tvCurrentLocation;
    EditText etEventDate;
    EditText etEventInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Get the textview and editTexts from the layout
        tvCurrentLocation = findViewById(R.id.tvCurrentLocation);
        etEventDate = findViewById(R.id.etEventDate);
        etEventInformation = findViewById(R.id.etEventInformation);

    }


// Onclick function to save event information
    public void saveNewEvent(View view) throws IOException {

//        Get the text from the textview and edittexts
        String eventInformation = etEventInformation.getText().toString();
        String eventLocation = tvCurrentLocation.getText().toString();
        String eventDate = etEventDate.getText().toString();

//      Store the contents of the variables into one string and append a newline to make it easier to read later
        String allEventInformation = eventInformation + " " + eventLocation + " " + eventDate + "\n";

//      If the event date field or the event information field is left empty, display a toast message to alert the user
        if (eventDate.equals("") || eventInformation.equals("")) {
            Toast.makeText(this, "One of the fields is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

//      Otherwise, prepare to write to a file called myEvents
        FileOutputStream fos = openFileOutput("myEvents.txt", MODE_APPEND);
//        Write the contents of teh allEventInformation variable to the file using bytes
        fos.write(allEventInformation.getBytes());
        fos.close();
//        Display message to say that the event has been saved
        Toast.makeText(this, "Event has been saved.", Toast.LENGTH_SHORT).show();

//      Set the editTexts to be empty again
        etEventInformation.setText("");
        etEventDate.setText("");
    }




// Onclick function to view all saved events
    public void viewAllEvents(View view) {
        startActivity(new Intent(MainActivity.this, SecondActivity.class));
    }


//  Function that gets the current location
    public void getCurrentLocation(View view) {
//        Make sure that that locationManager provider and location is enabled
        myLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            myLocationManager.isLocationEnabled();
        }

//        Create locationListener
        myLocationListener = new LocationListener() {

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

//              Store coordinates in a string and add special character tht will be used to split the string later
                String coordinates = "" + myLat + "," + myLong + "^";

//              Write the coordinate string to the myEvents file
                FileOutputStream fOs = null;
                try {
                    fOs = openFileOutput("myEvents.txt", MODE_APPEND);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    fOs.write(coordinates.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fOs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }




//                Introduce geocoder so that we can use it to get the city and state based off of the latitude and longitude
                Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocation(myLat, myLong, 1);     // We only want one result from getLocation
                    String cityName = addresses.get(0).getLocality();                       // Use getLocality to get the city name
                    String stateName = addresses.get(0).getAdminArea();                     // Use getAdminArea to get the state name


                    tvCurrentLocation.setText(cityName + ", " + stateName);

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
            myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, myLocationListener);
        }
    }


}

