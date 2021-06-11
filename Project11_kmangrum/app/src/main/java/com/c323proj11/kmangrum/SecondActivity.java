package com.c323proj11.kmangrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
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
import java.util.concurrent.Executor;

import javax.net.ssl.HttpsURLConnection;

public class SecondActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

//    Create variables that will be used later
    private InterstitialAd interstitialAd;
    private RewardedAd rewardedAd;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ProgressDialog progressDialog;
    String jsonRecipesArray;
    ListView listView;
    ArrayList<Meal> mealsList;
    String mealsURL = "https://www.themealdb.com/api/json/v1/1/filter.php?c=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

//        Call method that sets up the interstitial ad
        setupInterstitialAd();

//        Set up the drawerlayout and navigationview
        drawerLayout = findViewById(R.id.drawer_layout_id);
        navigationView = findViewById(R.id.navigationview_id);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        toogleDrawer();

        mealsList = new ArrayList<>();


//      Get data that was sent from second activity about selected movie
        Bundle foodCategory = getIntent().getExtras();
        if (foodCategory == null) {
            System.out.println("Bundle is null");
        }
        String recipesURL = mealsURL + foodCategory.getString("cTitle");

//        Call method that loads the rewarded ad
        loadRewardedAd();

//        Set up the listview and add an onclicklistener
        listView = findViewById(R.id.atListview);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Send the id of the selected item to the third activity
                Meal selectedMeal = mealsList.get(position);
                Intent i = new Intent(SecondActivity.this, ThirdActivity.class);
                i.putExtra("cID", selectedMeal.getId());
//                Show the rewarded ad before taking the user to the third activity
                showRewardedAd(i);

            }
        });
//      Call method that gets meals of category from API
        getCategoryRecipes(recipesURL);
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

//    Method that loads rewarded ad
    public void loadRewardedAd() {
        rewardedAd = new RewardedAd(this, "ca-app-pub-3940256099942544/5224354917");
        RewardedAdLoadCallback callback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                super.onRewardedAdLoaded();
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                super.onRewardedAdFailedToLoad(loadAdError);
                Toast.makeText(SecondActivity.this, "Failed to Load", Toast.LENGTH_SHORT).show();
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), callback);
    }

//    Method that shows the rewarded ad
    public void showRewardedAd(Intent i) {
        if (rewardedAd.isLoaded()) {
            RewardedAdCallback callback = new RewardedAdCallback() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    startActivity(i);
                }
            };
            rewardedAd.show(this, callback);
        }
    }

//  Method that sets up the interstitial ad
    public void setupInterstitialAd() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                interstitialAd.show();
            }
        });
    }

//    Method that connects to network and calls asynctask so api can be used in the background
    public void getCategoryRecipes(String recipesURL) {
        progressDialog = ProgressDialog.show(this, "Category Recipes", "Connecting, please wait...", true, true);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

//        See if connected to the network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
//            Make new instance of
            new DownloadCategoryRecipesTask().execute(recipesURL);
        } else {
            Toast.makeText(this, "No network connection available", Toast.LENGTH_SHORT).show();
        }
    }

//    Extend AsyncTask so that API can be used in the background
    private class DownloadCategoryRecipesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
//            Get the JSON data from api url
            jsonRecipesArray = downloadRecipesFromURL(urls[0]);
            return jsonRecipesArray;
        }

        private String downloadRecipesFromURL(String url) {
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
            JSONArray mealsArray;

            try {
//            Create JSONObject and array to access the information from the api properly
                JSONObject jsonObject = new JSONObject(s);
                mealsArray = new JSONArray(String.valueOf(jsonObject.getJSONArray("meals")));

//            Loop through array and get the information of each onject that is needed
                for (int i = 0; i < mealsArray.length(); i++) {
                    JSONObject mealObject = mealsArray.getJSONObject(i);
                    String title = mealObject.getString("strMeal");
                    String imagePath = mealObject.getString("strMealThumb");
                    String id = mealObject.getString("idMeal");

//            Create a new Meal item and store the information in the item
                    Meal mealItem = new Meal();
                    mealItem.setTitle(title);
                    mealItem.setImagePath(imagePath);
                    mealItem.setId(id);

//                Add the item to the categoryList
                    mealsList.add(mealItem);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
//        Set the adapter
            SecondActivityAdapter secondActivityAdapter = new SecondActivityAdapter(SecondActivity.this, mealsList);
            listView.setAdapter(secondActivityAdapter);

        }
    }
}