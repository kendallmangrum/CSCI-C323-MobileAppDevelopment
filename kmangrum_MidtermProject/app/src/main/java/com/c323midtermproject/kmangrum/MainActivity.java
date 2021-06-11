package com.c323midtermproject.kmangrum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

//    Variables for all of the layout items I will be using
    ImageView profilePicture;
    Button addCity;
    Button addMonument;
    Button addCamping;
    Button displayFavorites;
    ConstraintLayout citiesLayout;
    ConstraintLayout monumentsLayout;
    ConstraintLayout campingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Get each of the elements by findViewById and set profile picture
        profilePicture = findViewById(R.id.ivProfilePicture);
        profilePicture.setImageResource(R.drawable.profile_picture);
        addCity = findViewById(R.id.addCityButton);
        addMonument = findViewById(R.id.addHistoricalMonumentsButton);
        addCamping = findViewById(R.id.addCampingButton);
        displayFavorites = findViewById(R.id.displayFavoritesButton);

        citiesLayout = findViewById(R.id.citiesLayout);
        monumentsLayout = findViewById(R.id.historicalMonumentsLayout);
        campingLayout = findViewById(R.id.campingLayout);

//        Set on clicklistener for each layout
        citiesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Send an intent with extra to tell which layout was clicked so proper information will be displayed in third activity
                Intent i = new Intent(MainActivity.this, ThirdActivity.class);
                i.putExtra("FRAGMENT", "cities");
                startActivity(i);
            }
        });

        monumentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Send an intent with extra to tell which layout was clicked so proper information will be displayed in third activity
                Intent i = new Intent(MainActivity.this, ThirdActivity.class);
                i.putExtra("FRAGMENT", "monuments");
                startActivity(i);
            }
        });

        campingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Send an intent with extra to tell which layout was clicked so proper information will be displayed in third activity
                Intent i = new Intent(MainActivity.this, ThirdActivity.class);
                i.putExtra("FRAGMENT", "camping");
                startActivity(i);
            }
        });

    }

//    Functions for when addCity, addMonument, and add Camping buttons are pressed
    public void addCity(View view) {
//        Send intent with extra so that the proper fragment is displayed, start second activity
        Intent iCity = new Intent(this, SecondActivity.class);
        iCity.putExtra("loadFragment", 1);
        startActivity(iCity);
    }

    public void addHistoricalMonument(View view) {
//        Send intent with extra so that the proper fragment is displayed, start second activity
        Intent iMonument = new Intent(this, SecondActivity.class);
        iMonument.putExtra("loadFragment", 2);
        startActivity(iMonument);
    }

    public void addCampingTrekking(View view) {
//        Send intent with extra so that the proper fragment is displayed, start second activity
        Intent iCamping = new Intent(this, SecondActivity.class);
        iCamping.putExtra("loadFragment", 3);
        startActivity(iCamping);
    }

//    Function for when display favorites button is clicked
    public void displayFavorites(View view) {
//        Go to the fourth activity to show users favorite locations
        startActivity(new Intent(this, FourthActivity.class));
    }
}