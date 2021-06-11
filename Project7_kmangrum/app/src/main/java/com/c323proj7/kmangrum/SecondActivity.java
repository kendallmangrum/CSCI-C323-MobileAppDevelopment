package com.c323proj7.kmangrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SecondActivity extends AppCompatActivity {

//    Variables that will be used later
    private String genreAPIurl = "https://api.themoviedb.org/3/genre/movie/list?api_key=8cb04be6f7d005359fa7ce04601757db&language=en-US";
    private String movieListurl = "https://api.themoviedb.org/3/discover/movie?api_key=8cb04be6f7d005359fa7ce04601757db&langua%20ge=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&%20page=1";
    private String startImageURL = "https://image.tmdb.org/t/p/w500";

    ListView myListView;
    ProgressDialog progressDialog;
    String jsonMovieData;
    ArrayList<movieItem> myMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
//      Initialize myMovies arraylist
        myMovies = new ArrayList<>();

//        Get reference of the listview and make its items clickable
        myListView = findViewById(R.id.custonListview);
        myListView.setClickable(true);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                When an item is clicked, pass the information using intent putExtra to the third activity so that it can be displayed there
                movieItem selectedMovie = myMovies.get(position);

                Intent i = new Intent(SecondActivity.this, ThirdActivity.class);
                i.putExtra("mTitle", selectedMovie.getTitle());
                i.putExtra("mLang", selectedMovie.getLanguage());
                i.putExtra("mGenres", selectedMovie.getGenres());
                i.putExtra("mRelease", selectedMovie.getReleaseDate());
                i.putExtra("mDescription", selectedMovie.getDescription());
                i.putExtra("mPosterPath", startImageURL + selectedMovie.getImagePath());
                startActivity(i);
            }
        });
        getMovieListData();
    }

//  Method to get the list of movies from API
    public void getMovieListData() {
        progressDialog = ProgressDialog.show(this, "City weather", "Connecting, please wait...", true, true);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
//            Make new instance of DownloadMovieDataTask
            new DownloadMovieDataTask().execute(movieListurl);
        } else {
            Toast.makeText(this, "No network connection available", Toast.LENGTH_SHORT).show();
        }
    }

//Extend AsyncTask so that API can be used in background
    private class DownloadMovieDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            myMovies.clear();
//            Get the movieList data from the API url
            jsonMovieData = downloadMovieListFromURL(urls[0]);
            return jsonMovieData;
        }

        private String downloadMovieListFromURL(String url) {
            InputStream is = null;
            StringBuffer result = new StringBuffer();
            URL myURL = null;
            try {
//                Make connection to API url
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
                progressDialog.dismiss();;

//                Read through JSON data from API into result string
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

//            Create variable for movieArray
            JSONArray movieArray;

            try {
//                Create JSON object and array to access the information from api properly
                JSONObject jsonObject = new JSONObject(s);
                movieArray = new JSONArray(String.valueOf(jsonObject.getJSONArray("results")));

//                Loop through array and get the information of each object that is needed
                for (int i = 0; i < movieArray.length(); i++) {
                    JSONObject movieObject = movieArray.getJSONObject(i);
                    String title = movieObject.getString("title");
                    String description = movieObject.getString("overview");
                    String language = movieObject.getString("original_language");
                    String releaseDate = movieObject.getString("release_date");
                    String genres = movieObject.getString("genre_ids");
                    String imagePath = movieObject.getString("poster_path");

//                Create a new movieItem and store the information in the item
                    movieItem movie = new movieItem();
                    movie.setTitle(title);
                    movie.setLanguage(language);
                    movie.setDescription(description);
                    movie.setReleaseDate(releaseDate);
                    movie.setGenres(genres);
                    movie.setImagePath(imagePath);

//                    Add the movie item to the myMovies arraylist
                    myMovies.add(movie);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Set the custom list adapter
            MyListAdapter myListAdapter = new MyListAdapter(SecondActivity.this, myMovies);
            myListView.setAdapter(myListAdapter);
        }
    }


//  Function that inflates the menu
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