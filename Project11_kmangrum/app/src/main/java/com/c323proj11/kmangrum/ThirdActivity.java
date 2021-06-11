package com.c323proj11.kmangrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class ThirdActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

//    Create variables that will be used later
    TextView tvMealName, tvMealCategory, tvMealArea, tvMealInstructions, tv, tv1, tv2, tv3;
    ImageView ivMeal, ivFavorite;
    String baseMealURL = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ProgressDialog progressDialog;
    String jsonRecipeArray;
    AppDatabase database;
    RecipeDao recipeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

//        Call method to set up views in layout
        setupViews();

//        Get data from previous activity
        Bundle mealID = getIntent().getExtras();
        String mealURL = baseMealURL + mealID.getString("cID");

//        Set up drawerlayout and navigationview
        drawerLayout = findViewById(R.id.drawer_layout_id);
        navigationView = findViewById(R.id.navigationview_id);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        toogleDrawer();

//         Call method that gets the information about the meal
        getMealInformation(mealURL);

//        Build database
        database = Room.databaseBuilder(ThirdActivity.this, AppDatabase.class, "favoriteRecipes").allowMainThreadQueries().build();
        recipeDao = database.getRecipeDao();
    }

//    Method that sets up the views in the layout
    private void setupViews() {
        tvMealName = findViewById(R.id.tvRArea);
        tvMealCategory = findViewById(R.id.tvRCategory);
        tvMealArea = findViewById(R.id.tvRName);
        tvMealInstructions = findViewById(R.id.tvRInstructions);
        ivMeal = findViewById(R.id.ivRecipe);
        ivFavorite = findViewById(R.id.ivFavorite);

        tv = findViewById(R.id.tv);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);

//        Set an onclick listener for favorite image
        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe = new Recipe();
                recipe.setName(tvMealName.getText().toString());
                recipe.setCategory(tvMealCategory.getText().toString());
                recipe.setArea(tvMealArea.getText().toString());
                recipe.setInstructions(tvMealInstructions.getText().toString());
                recipe.setImagePath(ivMeal.getContentDescription().toString());

//      Add recipe to database
                recipeDao.insert(recipe);
                Toast.makeText(ThirdActivity.this, "Recipe added to your favorites!", Toast.LENGTH_SHORT).show();
            }
        });
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

//      Method that connects to network and calls asynctask so api can be used in the background
    private void getMealInformation(String mealURL) {
        progressDialog = ProgressDialog.show(this, "Category Recipes", "Connecting, please wait...", true, true);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

//        See if connected to the network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
//            Make new instance of
            new DownloadRecipeTask().execute(mealURL);
        } else {
            Toast.makeText(this, "No network connection available", Toast.LENGTH_SHORT).show();
        }

    }

    //    Extend AsyncTask so that API can be used in the background
    private class DownloadRecipeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            //            Get the JSON data from api url
            jsonRecipeArray = downloadRecipeFromURL(urls[0]);
            return jsonRecipeArray;
        }

        private String downloadRecipeFromURL(String url) {
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

                JSONObject mealObject = mealsArray.getJSONObject(0);
                String title = mealObject.getString("strMeal");
                String category = mealObject.getString("strCategory");
                String area = mealObject.getString("strArea");
                String instructions = mealObject.getString("strInstructions");
                String imagePath = mealObject.getString("strMealThumb");
                String id = mealObject.getString("idMeal");

                Meal mealItem = new Meal();
                mealItem.setTitle(title);

                tvMealName.setText(title);
                tvMealCategory.setText(category);
                tvMealArea.setText(area);
                tvMealInstructions.setText(instructions);
                Picasso.get().load(imagePath).into(ivMeal);
                ivMeal.setContentDescription(imagePath);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}