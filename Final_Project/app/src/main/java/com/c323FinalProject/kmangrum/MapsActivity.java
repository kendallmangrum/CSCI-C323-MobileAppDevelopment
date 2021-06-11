package com.c323FinalProject.kmangrum;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GnssAntennaInfo;
import android.location.Location;
import android.os.Bundle;
import android.renderscript.Double4;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

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

//        Check the location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 13);
            return;
        }
        mMap.setMyLocationEnabled(true);


//        Get the information that was passed from the Review activity
        String[] passedInfo = getIntent().getStringArrayExtra("NAME_LOCATION_COORDS");

//        Get the Name for the review
        String reviewName = passedInfo[0];
//        Get the location (city, state) for the review
        String reviewLocation = passedInfo[1];
//        Get the user's current coordinates
        String coordinates = passedInfo[2];

//        Split the coordinates and convert them into doubles.
        String[] splitCoords = coordinates.split(",");
        Double lat = Double.parseDouble(splitCoords[0]);
        Double lon = Double.parseDouble(splitCoords[1]);

//        Create a latlng of the user's current location
        LatLng currentLocation = new LatLng(lat, lon);

        try {
//            Get the name of the user's current location using their coordinates
            String userLocationName = getLocationNameFromLatLng(this, lat, lon);
            LatLng userCurrentLatLng = new LatLng(lat, lon);

            LatLng destination = getLocationFromAddress(this, reviewLocation);

//            Add a marker for the destination that the user selected and give it a title of the destination's name
            mMap.addMarker(new MarkerOptions().position(destination).title(reviewName)).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));

//            Get the distance between the two points in kilometers
            float results[] = new float[2];
            Location.distanceBetween(lat, lon, destination.latitude, destination.longitude, results);

//            Convert to miles
            float kilometers = (results[0] / 1000);
            double miles = kilometers / 1.609;

//            Add a marker on the map for the user's current location and give the marker a title of the name of their location
            mMap.addMarker(new MarkerOptions().position(currentLocation).title(userLocationName));

//            Add a marker for the destination that the user selected and give it a title of the destination's name
            mMap.addMarker(new MarkerOptions().position(destination).title(reviewName).snippet("Distance: " + miles + " miles")).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));

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