package com.c323proj11.kmangrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

//    Create variables that will be used later
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    AdView adview;
    private String foodCategoryURL = "https://www.themealdb.com/api/json/v1/1/categories.php";
    ArrayList<FoodCategory> categoryList;
    String jsonFoodCategoryData;
    ProgressDialog progressDialog;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Initialize mobile ads
        MobileAds.initialize(this);

//        Set up adview and load ad request
        adview = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adview.loadAd(adRequest);

//        Set up the drawerlayout and navigationview
        drawerLayout = findViewById(R.id.drawer_layout_id);
        navigationView = findViewById(R.id.navigationview_id);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        toogleDrawer();

        categoryList = new ArrayList<>();

//       Set up listview and add onclicklistener for listview items
        listView = findViewById(R.id.aoListview);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                When an item is clicked on, get its title and pass it to the second activity
                FoodCategory selectedCategory = categoryList.get(position);
                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                i.putExtra("cTitle", selectedCategory.getTitle());
                startActivity(i);
            }
        });

//        make call to get the category information from the api
        getFoodCategoryData();
    }


    //    Method that allows the drawer to be toggled open and closed
    private void toogleDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

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
            case R.id.categories:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.favorites:
                startActivity(new Intent(this, FragmentActivity.class));
                break;
        }
        return true;
    }

    //    Method that closes the navigation drawer
    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


//  Method that connects to network and calls asynctask so api can be used in the background
    public void getFoodCategoryData() {
        progressDialog = ProgressDialog.show(this, "Food Categories", "Connecting, please wait...", true, true);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
//            Make new instance
            new DownloadFoodCategoryTask().execute(foodCategoryURL);
        } else {
            Toast.makeText(this, "No network connection available", Toast.LENGTH_SHORT).show();
        }
    }




//    Extend AsyncTask so that API can be used in the background
    private class DownloadFoodCategoryTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
        jsonFoodCategoryData = downloadFoodCategoryFromURL(urls[0]);
        return jsonFoodCategoryData;
    }

    private String downloadFoodCategoryFromURL(String url) {
        InputStream is = null;
        StringBuffer result = new StringBuffer();
        URL myURL = null;
        try {
//            Make connection to API url
            myURL = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) myURL.openConnection();
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTPS error code " + responseCode);
            }
            is = connection.getInputStream();
            progressDialog.dismiss();

//            Read through JSON data from API into string result
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
       super.onPostExecute(s);

//       Create variable for categoryArray
        JSONArray categoryArray;

        try {
//            Create JSONObject and array to access the information from the api properly
            JSONObject jsonObject = new JSONObject(s);
            categoryArray = new JSONArray(String.valueOf(jsonObject.getJSONArray("categories")));

//            Loop through array and get the information of each onject that is needed
            for (int i = 0; i < categoryArray.length(); i++) {
                JSONObject categoryObject = categoryArray.getJSONObject(i);
                String title = categoryObject.getString("strCategory");
                String imagePath = categoryObject.getString("strCategoryThumb");

//            Create a new FoodCategory item and store the information in the item
                FoodCategory foodCategoryItem = new FoodCategory();
                foodCategoryItem.setTitle(title);
                foodCategoryItem.setImagePath(imagePath);

//                Add the item to the categoryList
                categoryList.add(foodCategoryItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Set the adapter
        MainActivityAdapter mainActivityAdapter = new MainActivityAdapter(MainActivity.this, categoryList);
        listView.setAdapter((ListAdapter) mainActivityAdapter);

        }
    }
}