package com.c323midtermproject.kmangrum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;

public class SecondActivity extends AppCompatActivity {

//    Create variables that I will be using later
    FragmentTransaction fragmentTransaction;
    private CitiesFragment citiesFragment;
    private MonumentFragment monumentFragment;
    private CampingFragment campingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

//        Start fragmentTransaction that will allow the proper fragment to be displayed
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

//      Get intent will extra that says which fragment to display
        int intentFragment = getIntent().getExtras().getInt("loadFragment");


        switch (intentFragment){
            case 1:
//                Load in cities fragment
                citiesFragment = new CitiesFragment();
                fragmentTransaction.replace(R.id.citiesFragment, citiesFragment);
                fragmentTransaction.commit();
                break;
            case 2:
//                Load in monuments fragment
                monumentFragment = new MonumentFragment();
                fragmentTransaction.replace(R.id.monumentFragment, monumentFragment);
                fragmentTransaction.commit();
                break;
            case 3:
//                Load in camping fragment
                campingFragment = new CampingFragment();
                fragmentTransaction.replace(R.id.campingFragment, campingFragment);
                fragmentTransaction.commit();
                break;
        }
    }


}