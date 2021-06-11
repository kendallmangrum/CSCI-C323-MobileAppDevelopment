package com.c323proj7.kmangrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ThirdActivity extends AppCompatActivity {

//    Create variables that will be used later
    TextView mTitle, mLanguage, mDescription, mReleaseDate, mGenres;
    ImageView mImage;
    String jsonGenreData;
    private String genreAPIurl = "https://api.themoviedb.org/3/genre/movie/list?api_key=8cb04be6f7d005359fa7ce04601757db&language=en-US";
    ProgressDialog progressDialog;
    String dTitle, dLanguage, dDescription, dReleaseDate, dGenres, dPosterPath;
    Map<Integer, String> genres = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

//      Get data that was sent from second activity about selected movie
        Bundle details = getIntent().getExtras();
        if (details == null) {
            System.out.println("Bundle is null");
        } else {

//            Get the references to the layout elements
            mTitle = findViewById(R.id.tvTitle);
            mLanguage = findViewById(R.id.tvLanguage);
            mDescription = findViewById(R.id.tvMovieDescription);
            mReleaseDate = findViewById(R.id.tvReleaseDate);
            mImage = findViewById(R.id.ivMovie);
            mGenres = findViewById(R.id.tvGenre);

//          Get the movie details from the bundle
            dTitle = details.getString("mTitle");
            dDescription = details.getString("mDescription");
            dLanguage = details.getString("mLang");
            dReleaseDate = details.getString("mRelease");
            dGenres = details.getString("mGenres");
            dPosterPath = details.getString("mPosterPath");

//            Set the textviews to display the movie information
            mTitle.setText("Title: " + dTitle);
            mDescription.setText(dDescription);
            mReleaseDate.setText("Release Date: " + dReleaseDate);

//            Set the imageview to display movie poster image
            Picasso.get().load(dPosterPath).into(mImage);

//          Display the full name of language rather than abbreviation
            if (dLanguage.equals("en")) {
                mLanguage.setText("Language: English");
            } else if (dLanguage.equals("ja")) {
                mLanguage.setText("Language: Japanese");
            } else {
                mLanguage.setText("Language: " + dLanguage);
            }

//            Call to get the genres from the API
            getMovieGenreData();
        }
    }



// Method to get the genre data from the API
    public void getMovieGenreData() {
        progressDialog = ProgressDialog.show(this, "Movie Genre", "Connecting, please wait...", true, true);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

//        See if connected to the network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
//            Make new instance of DownloadMovieGenreDataTask
            new ThirdActivity.DownloadMovieGenreDataTask().execute(genreAPIurl);
        } else {
            Toast.makeText(this, "No network connection available", Toast.LENGTH_SHORT).show();
        }
    }

//    Class that extends AsyncTask to use API in background
    private class DownloadMovieGenreDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
//            Get the JSON data from api url
            jsonGenreData = downloadGenreFromURL(urls[0]);
            return jsonGenreData;
        }


        private String downloadGenreFromURL(String url) {
            InputStream is = null;
            StringBuffer result = new StringBuffer();
            URL myURL = null;
            try {
//                Make connection to api
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

//                Read in api data and add to empty string
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

//            Create JSON object and array to reference data from api
            JSONObject wholeGenreObject;
            JSONArray wholeGenreArray;

            try {
                wholeGenreObject = new JSONObject(s);
                wholeGenreArray = new JSONArray(String.valueOf(wholeGenreObject.getJSONArray("genres")));

//                Loop through genre array to store information into a hashmap
                for (int i = 0; i < wholeGenreArray.length(); i++) {
                    int id = wholeGenreArray.getJSONObject(i).getInt("id");
                    String genreName = wholeGenreArray.getJSONObject(i).getString("name");
                    genres.put(id, genreName);
                }

//                Get rid of brackets in movie genre array string
                dGenres = dGenres.replaceAll("[\\[\\]]","");
                String[] genreIDs = dGenres.split(",");
//                Convert string array into arraylist
                ArrayList<Integer> intGenreIDs = new ArrayList();
                for (String id : genreIDs) {
                    intGenreIDs.add(Integer.parseInt(id));
                }

//                String that will be displayed in Genre Textview
                String genreNames = "Genres: ";

//                Double for loop to compare genre ids in selected movie genre id arraylist to the genre id keys in the hashmap
                for (Integer id: genres.keySet()) {
                    for (int i = 0; i < intGenreIDs.size(); i++) {
                        if (id.equals(intGenreIDs.get(i))) {
//                            if they match, add the genre name to the string that will be displayed
                            genreNames += genres.get(id) + ", ";
                        }
                    }
                }
//                Remove the last comma for aesthetics
                genreNames = genreNames.substring(0, genreNames.length()-2);

//                Set the textview
                mGenres.setText(genreNames);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


//  Inflate the overflow menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    
    //  Function that determines which menu item is selected, which takes the user to a specific activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ConstraintLayout constraintLayout = findViewById(R.id.mainActivityLayout);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_weather:
                if (item.isCheckable()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                startActivity(new Intent(this, MainActivity.class));
                return true;

            case R.id.action_movie:
                if (item.isCheckable()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                startActivity(new Intent(this, SecondActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}