package com.example.project6_kmangrum;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

//    Create variables that will be used later
    private GoogleMap mMap;
    String[] separateEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        Instantiate geocoder so that we can again get city name and state
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());

//        Read in string from file
        String events = readFromFile(this);

//        If file is empty, just display default map
        if (events.isEmpty()) {
            return;

        }else {
//            Split the string by newline
            separateEvents = events.split("\n");

//            Create a list so that we can remove the empty item that always appears for whatever reason (bug that Vijeet and I couldn't figure out)
            List<String> tempList = new ArrayList<>(Arrays.asList(separateEvents));
            tempList.remove(0);

//            Convert the list back to string array
            separateEvents = tempList.toArray(new String[0]);

//          Loop through string array and get coordinates for each event
            for (int i = 0; i < separateEvents.length; i++) {
                int index = separateEvents[i].indexOf("^");

                separateEvents[i] = separateEvents[i].substring(0, index - 1);


                String[] latLng = separateEvents[i].split(",");
//                Store the coordinates in variables lat and lng
                String lat = latLng[0];
                String lng = latLng[1];

//                Get the city name and state again using coordinated
                try {
                    List<Address> address = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
                    String cityName = address.get(0).getLocality();                   // Use getLocality to get the city name
                    String stateName = address.get(0).getAdminArea();


                    LatLng location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

//                    Add a marker at the event coordinates and move camera there
                    mMap.addMarker(new MarkerOptions().position(location).title(cityName + ", " + stateName));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

// readFromFile function that is used in the other activities
    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("myEvents.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}