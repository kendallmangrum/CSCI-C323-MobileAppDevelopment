package com.c323midtermproject.kmangrum;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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

//        Get the information that was passed from the third activity from the popup view
        String[] passedInfo = getIntent().getStringArrayExtra("NAME_LOCATION_COORDS");

//        Get the name of the destination
        String name = passedInfo[0];
//        Get the location (city, state) of the location
        String location = passedInfo[1];
//        Get the user's current coordinates
        String coordinates = passedInfo[2];

//        Split the coordinates string and convert them into doubles
        String[] splitCoords = coordinates.split(",");
        Double lat = Double.parseDouble(splitCoords[0]);
        Double lon = Double.parseDouble(splitCoords[1]);

//        Create the latlng of the user's current location
        LatLng currentLocation = new LatLng(lat, lon);

        try {
//            Get the name of the user's current location using their coordinates
            String userLocationName = getLocationNameFromLatLng(this, lat, lon);
            LatLng userCurrentLatLng = new LatLng(lat, lon);

//            Add a marker on the map for the user's current location and give the marker a title of the name of their location
            mMap.addMarker(new MarkerOptions().position(currentLocation).title(userLocationName));
            LatLng destination = getLocationFromAddress(this, location);

//            Add a marker for the destination that the user selected and give it a title of the destination's name
            mMap.addMarker(new MarkerOptions().position(destination).title(name));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));

//            Add line between the two locations
            mMap.addPolyline(new PolylineOptions().add(userCurrentLatLng, destination));

//            Get the distance between two points in kilometers
            float results[] = new float[1];
            Location.distanceBetween(lat, lon, destination.latitude, destination.longitude, results);

            float kilometers = (results[0] / 1000);
            TextView distancetv = findViewById(R.id.tvDistance);
            distancetv.setText("Distance between locations: " + kilometers + "kilometers");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

// Function that gets the location from the name of a location
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return p1;
    }

//  Function that gets the name of a location from the latlng coordinates
    public String getLocationNameFromLatLng(Context context, Double lat, Double lng) throws IOException {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;

        addresses = geocoder.getFromLocation(lat, lng, 1); // We only want one result from getLocation
        String cityName = addresses.get(0).getLocality();                   // Use getLocality to get the city name
        String stateName = addresses.get(0).getAdminArea();                 // Use getAdminArea to get the state name

        String cityState = cityName + ", " + stateName;
        return cityState;
    }
}

