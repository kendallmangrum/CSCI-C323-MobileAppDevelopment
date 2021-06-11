package com.c323FinalProject.kmangrum;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.c323FinalProject.kmangrum.daos.CategoryDAO;
import com.c323FinalProject.kmangrum.daos.FavoritesDAO;
import com.c323FinalProject.kmangrum.daos.ReviewDAO;
import com.c323FinalProject.kmangrum.daos.ReviewListDAO;
import com.c323FinalProject.kmangrum.daos.TrashDAO;
import com.c323FinalProject.kmangrum.entities.Category;
import com.c323FinalProject.kmangrum.entities.Favorites;
import com.c323FinalProject.kmangrum.entities.Review;
import com.c323FinalProject.kmangrum.entities.ReviewList;
import com.c323FinalProject.kmangrum.entities.Trash;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class NavDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

//    Create variables that will be used later
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    SharedPreferences userPref;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer2);

//        Call methods to set up the nav drawer and nav header
        setUpNavDrawer();
        setUpNavHeader();

    }


    public void setUpNavDrawer() {
//        Set up the drawerlayout and navigationview
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle abdt = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(abdt);
        abdt.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.openDrawer(Gravity.LEFT);
    }


    public void setUpNavHeader() {
//        Get the stored information from the login
        userPref = getSharedPreferences("SharedPref", MODE_PRIVATE);
        String userName = userPref.getString("userName", "name");
        String userEmail = userPref.getString("userEmail", "email");
        Bitmap bitmap = getBitmap(R.drawable.initial_user_profile_image);
        String defaultPic = BitMapToString(bitmap);
        String userPicture = userPref.getString("userPicture", defaultPic);

//        Load all of the shared preferences information into the navigationview header
        View headerLayout = navigationView.getHeaderView(0);
        ImageView ivProfilePicture = headerLayout.findViewById(R.id.ivProfile);
        TextView tvUserName = headerLayout.findViewById(R.id.tvUserName);
        TextView tvUserEmail = headerLayout.findViewById(R.id.tvUserEmail);

        Bitmap testPic = getBitmapFromString(userPicture);
        ivProfilePicture.setImageBitmap(getBitmapFromString(userPicture));
        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);
    }




//    Method that allows the drawer to be toggled open and closed
    private void toogleDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }


//    Override the back button so that if the drawer is open, the back button closes it
    //    Override the back button so that if the drawer is open, the back button closes it
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    Method for when the items in the navigationview are selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrameLayout, new DashboardFragment()).commit();
                closeDrawer();
                break;
            case R.id.favorites:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrameLayout, new FavoritesFragment()).commit();
                closeDrawer();
                break;
            case R.id.trash:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrameLayout, new TrashFragment()).commit();
                closeDrawer();
                break;
        }
        return true;
    }

//    This Function converts the String back to Bitmap
    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

//    Function that converts Bitmap into a string
    private String BitMapToString (Bitmap bitmap){
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

//    Converts a drawable to a bitmap
    public Bitmap getBitmap(@DrawableRes final int resId) {
        Drawable drawable = getResources().getDrawable(resId);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

//    Function to control opening and closing of navigation drawer
    public void closeDrawer() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}