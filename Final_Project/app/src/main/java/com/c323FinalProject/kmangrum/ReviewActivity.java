package com.c323FinalProject.kmangrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.c323FinalProject.kmangrum.daos.FavoritesDAO;
import com.c323FinalProject.kmangrum.entities.Favorites;
import com.c323FinalProject.kmangrum.entities.Review;

import org.json.JSONException;

public class ReviewActivity extends AppCompatActivity {

//    Initialize variables that will be used later
    TextView tvReviewName, tvReviewFeatureGenreFamous, tvLangauageCuisine, tvReviewWebsite, tvReviewLocation, tvReviewReview;
    ImageView ivReviewActivity, ivFavoriteIcon;
    Button bttnLocation;
    int id;
    String category;

    AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

//        Get information passed from reviewlist activity to know when review to display
        Bundle storedReview = getIntent().getExtras();
        if (storedReview == null) {
            System.out.println("Bundle is null");
        } else {
            id = storedReview.getInt("reviewID");
            category = storedReview.getString("reviewCategory");
        }

        setupViews(id, category);

    }

    private void setupViews(int id, String category) {
//    Method that sets up the views in the layout
//        Get all of the views by their id
        tvReviewName = findViewById(R.id.tvReviewName);
        tvReviewFeatureGenreFamous = findViewById(R.id.tvReviewFeatureGenreFamous);
        tvLangauageCuisine = findViewById(R.id.tvLanguageCuisine);
        tvReviewWebsite = findViewById(R.id.tvReviewWebsite);
        tvReviewLocation = findViewById(R.id.tvReviewLocation);
        tvReviewReview = findViewById(R.id.tvReviewReview);
        ivReviewActivity = findViewById(R.id.ivReviewActivity);

//      Get an instance of the database
        database = AppDatabase.getDBInstance(this);
//      Get current review

        Review review = database.getReviewDAO().getSpecificReview(String.valueOf(id));
//        If image is not null, display image, otherwise display default image

        if(review.getImage() != null) {
            ivReviewActivity.setImageBitmap(getBitmapFromString(review.getImage()));
        }
        else{
            ivReviewActivity.setImageResource(R.drawable.placeholder_image);
        }

//        Set the textviews that apply for all categories
        tvReviewName.setText(review.getReviewName());
        tvReviewReview.setText(review.getReviewDetails());
        tvReviewLocation.setText(review.getReviewLocation());

        if (category.equals("Books")) {
            tvReviewFeatureGenreFamous.setText(review.getReviewGenre());
            tvReviewWebsite.setText(review.getReviewWebsite());
            tvLangauageCuisine.setText(review.getReviewLanguage());
        }else if (category.equals("Movies")) {
            tvReviewFeatureGenreFamous.setText(review.getReviewGenre());
            tvReviewWebsite.setText(review.getReviewWebsite());
            tvLangauageCuisine.setText(review.getReviewLanguage());
        }else if (category.equals("Restaurants")) {
            tvReviewFeatureGenreFamous.setText(review.getReviewFamousFor());;
            tvLangauageCuisine.setText(review.getReviewCuisine());
        }else if (category.equals("Electronics")) {
            tvReviewFeatureGenreFamous.setText(review.getReviewFeature());
            tvReviewWebsite.setText(review.getReviewWebsite());
        }


//      Add an onclicklistener to favorites button to add review to favorites
        ivFavoriteIcon = findViewById(R.id.ivFavoriteIcon);
        ivFavoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Set favorites entity to have same attributes of current review
                Favorites newFavorite = new Favorites();
                newFavorite.setReviewID(review.getReviewID());
                newFavorite.setReviewName(review.getReviewName());
                newFavorite.setReviewDetails(review.getReviewDetails());
                newFavorite.setImage(review.getImage());

//                Insert the favorite into the database
                FavoritesDAO favoritesDAO = database.getFavoritesDAO();
                favoritesDAO.insert(newFavorite);

                Toast.makeText(ReviewActivity.this, "Added to Favorites!", Toast.LENGTH_SHORT).show();
            }
        });

//        Onclicklistener for the location button
        bttnLocation = findViewById(R.id.bttnLocation);
        bttnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Create instances of locationmanager and locationlistener
                LocationManager locationManager;
                LocationListener locationListener;

//                Start Location manager and make sure location is enabled
                locationManager = (LocationManager) getBaseContext().getSystemService(LOCATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    locationManager.isLocationEnabled();
                } else {

                }
                locationListener = new LocationListener() {

                    @Override
                    public void onProviderDisabled(@NonNull String provider) {
                        startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

//            Function to get the location
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
//                Get the latitude and longitude of device
                        double myLat = location.getLatitude();
                        double myLong = location.getLongitude();
                        String coords = "" + myLat + "," + myLong;

                        Intent i = new Intent(ReviewActivity.this, MapsActivity.class);
//                            Store the name and location of the destination as well as the user's current location to pass to Maps activity
                        i.putExtra("NAME_LOCATION_COORDS", new String[]{review.getReviewName(), review.getReviewLocation(), coords});

//                i.putExtra("Name", review.getReviewName());
//                i.putExtra("Location", review.getReviewLocation());
                        startActivity(i);

                    }
                };

                //        Check to see if we have permission to use the devices location, if no ask for permission. Otherwise request the current location of the user
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 13);
                    return;
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);
                }
            }
        });
    }

    //    This Function converts the String back to Bitmap
    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}