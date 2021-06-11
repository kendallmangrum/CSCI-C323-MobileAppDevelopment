package com.c323FinalProject.kmangrum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.c323FinalProject.kmangrum.daos.ReviewDAO;
import com.c323FinalProject.kmangrum.daos.ReviewListDAO;
import com.c323FinalProject.kmangrum.entities.Review;
import com.c323FinalProject.kmangrum.entities.ReviewList;
import com.c323FinalProject.kmangrum.entities.Trash;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class ReviewListActivity extends AppCompatActivity implements ReviewListListener,
        AddNewElectronicsDialog.ElectronicsDialogComplete,
        AddNewMovieBookReviewDialog.BooksDialogComplete,
        AddNewRestaurantDialog.RestaurantDialogComplete {

//    Initialize variables that will be used later
    FloatingActionButton reviewlistFAB;
    RecyclerView listView;
    List<Review> reviewList;
    AppDatabase database;
    List<ReviewList> reviewListList;
    ReviewlistListviewAdapter adapter;
    Bitmap newImagePotential;
    Uri imageUri;
    ImageView currentImage;
    int categoryID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        newImagePotential = null;
        listView = findViewById(R.id.reviewActivityListview);
        new ItemTouchHelper(itemtouchHelperCallback).attachToRecyclerView(listView);

//      Get data that was sent from second activity about selected movie
        Bundle category = getIntent().getExtras();
        if (category == null) {
            System.out.println("Bundle is null");
        }
        else{
            // if we have a bundle get out the categoryID to use in creating our reviewLists
            database = AppDatabase.getDBInstance(this);
            categoryID = category.getInt("categoryID");
            reviewList = database.getReviewDAO().getCategoriedReviews(String.valueOf(categoryID));

            ReviewlistListviewAdapter myAdapter = new ReviewlistListviewAdapter(reviewList, this, this);
            listView.setLayoutManager(new LinearLayoutManager(this));
            listView.setHasFixedSize(true);
            listView.setAdapter(myAdapter);
        }

//        Set onclicklistener for floating action button
        reviewlistFAB = findViewById(R.id.reviewlistFAB);
        reviewlistFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Check which category the user clicked on so that the proper dialog box is shown
                if (category.getString("categoryTitle").equals("Books")) {
                    openBookMovieDialog();
                    reviewListList = database.getReviewListDAO().getAllBooks();
                    adapter = new ReviewlistListviewAdapter(reviewList, ReviewListActivity.this, ReviewListActivity.this);
                    listView.setAdapter(adapter);

                } else if (category.getString("categoryTitle").equals("Movies")) {
                    openBookMovieDialog();
                    reviewListList = database.getReviewListDAO().getAllMovies();
                    adapter = new ReviewlistListviewAdapter(reviewList, ReviewListActivity.this, ReviewListActivity.this);
                    listView.setAdapter(adapter);

                } else if (category.getString("categoryTitle").equals("Restaurants")) {
                    openRestaurantDialog();
                    reviewListList = database.getReviewListDAO().getAllRestaurants();
                    adapter = new ReviewlistListviewAdapter(reviewList, ReviewListActivity.this, ReviewListActivity.this);
                    listView.setAdapter(adapter);

                }  else if (category.getString("categoryTitle").equals("Electronics")) {
                    openElectronicsDialog();
                    reviewListList = database.getReviewListDAO().getAllElectronics();
                    adapter = new ReviewlistListviewAdapter(reviewList, ReviewListActivity.this, ReviewListActivity.this);
                    listView.setAdapter(adapter);

                } else {
                    openElectronicsDialog();
                }

            }
        });

    }

//    Method that shows the dialog to add an electronics dialog box
    private void openElectronicsDialog() {
        AddNewElectronicsDialog addReviewDialog = new AddNewElectronicsDialog();
        addReviewDialog.show(getSupportFragmentManager(), "Create New Review");
    }

//    Method that shows the dialog to add a restaurant dialog box
    private void openRestaurantDialog() {
        AddNewRestaurantDialog addReviewDialog = new AddNewRestaurantDialog();
        addReviewDialog.show(getSupportFragmentManager(), "Create New Review");
    }

//    Method that shows the dialog to add a book dialog box
    public void openBookMovieDialog() {
        AddNewMovieBookReviewDialog addReviewDialog = new AddNewMovieBookReviewDialog();
        addReviewDialog.show(getSupportFragmentManager(), "Create New Review");
    }

//    Method for when the camera icon is selected so that the user can choose how they want to upload an image
    public void addReviewImage(View view) {
        AddReviewImageDialog dialog = new AddReviewImageDialog();
        dialog.show(getSupportFragmentManager(), "Choose Image Upload Option");

    }

    //  Function that converts image bitmap into a base64 string that can be stored in JSONobject and saved to file
    private String getStringFromBitmap(Bitmap bitmapPicture) {
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void createChooseImageAlertDialog(int position) {
        Review review = reviewList.get(position);
        int ID = review.getReviewID();
        // create an alert builder with our custom style
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlerts);
        final View customLayout = LayoutInflater.from(this).inflate(R.layout.choose_image_input, null);
        Button bttnImageCamera = customLayout.findViewById(R.id.bttnImageCamera);
        Button bttnImageGallery = customLayout.findViewById(R.id.bttnImageGallery);
        currentImage = customLayout.findViewById(R.id.chooseImageView);

        // create the button onclick listeners
        // launching camera intent button on click listener
        bttnImageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCamera()) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, 17);
                } else {
                    view.setEnabled(false);
                }
            }
        });

        // create the gallery intent click listener
        bttnImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Select Image"), 1);
            }
        });

        // set my positive and negative dialog buttons for saving, or dismissing the dialog all together
        builder.setView(customLayout);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database = AppDatabase.getDBInstance(getBaseContext());
                ReviewDAO reviewDAO = database.getReviewDAO();
                // if the current image isn't the placeholder we'll update the review image
                Bitmap finalPotentialImage = ((BitmapDrawable)currentImage.getDrawable()).getBitmap();
                String updatedImageString = getStringFromBitmap(finalPotentialImage);
                reviewDAO.updateReviewImage(updatedImageString, String.valueOf(ID));
                // Once we've updated the Review, we'll reload the current list and reset the adapter!
                reviewList = database.getReviewDAO().getCategoriedReviews(String.valueOf(categoryID));
//            reviewList = database.getReviewDAO().getAllReviews();
                ReviewlistListviewAdapter myAdapter = new ReviewlistListviewAdapter(reviewList, ReviewListActivity.this, ReviewListActivity.this);
                listView.setAdapter(myAdapter);
            }
        });
        builder.show();
    }

    //    Function that gets the image that the user took and sets the imageview to display that image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 17:
                    Bundle extra = data.getExtras();
                    newImagePotential = (Bitmap) extra.get("data");
                    currentImage.setImageBitmap(newImagePotential);
                    break;

                case 1:
                    imageUri = data.getData();
                    try {
                        newImagePotential = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        currentImage.setImageBitmap(newImagePotential);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

//    Function to see if the user's device has a camera
    private boolean hasCamera() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
            return true;
        else
            return false;
    }

//    Method that updates the listview
    @Override
    public void ElectronicsFinished() {
        reviewList = database.getReviewDAO().getCategoriedReviews(String.valueOf(categoryID));
        ReviewlistListviewAdapter myAdapter = new ReviewlistListviewAdapter(reviewList, ReviewListActivity.this, ReviewListActivity.this);
        listView.setAdapter(myAdapter);
    }

//    Method that updates the listview
    @Override
    public void Booksfinished() {
        reviewList = database.getReviewDAO().getCategoriedReviews(String.valueOf(categoryID));
        ReviewlistListviewAdapter myAdapter = new ReviewlistListviewAdapter(reviewList, ReviewListActivity.this, ReviewListActivity.this);
        listView.setAdapter(myAdapter);
    }

//    Method that updates the listview
    @Override
    public void RestaurantsFinished() {
        reviewList = database.getReviewDAO().getCategoriedReviews(String.valueOf(categoryID));
        ReviewlistListviewAdapter myAdapter = new ReviewlistListviewAdapter(reviewList, ReviewListActivity.this, ReviewListActivity.this);
        listView.setAdapter(myAdapter);
    }

//    Methods to help with gestures
    ItemTouchHelper.SimpleCallback itemtouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

//        Method for when the user swipes to delete
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReviewListActivity.this, R.style.CustomAlerts);
//            Inflate alert layout
            final View customLayout = LayoutInflater.from(ReviewListActivity.this).inflate(R.layout.deletion_prompt, null);
            TextView message = customLayout.findViewById(R.id.tvDeletionMessage);
            message.setText("Hitting confirm will move this review to your trash list. Is this what" +
                    "you intended to do?");

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    reviewList = database.getReviewDAO().getCategoriedReviews(String.valueOf(categoryID));
                    ReviewlistListviewAdapter myAdapter = new ReviewlistListviewAdapter(reviewList, ReviewListActivity.this, ReviewListActivity.this);
                    listView.setAdapter(myAdapter);
                    dialogInterface.dismiss();
                }
            });

            builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // use the view holder to get the current Review
                    int position = viewHolder.getAdapterPosition();
                    Review toDelete = reviewList.get(position);
                    // Make a new Trash Object
                    Trash toAdd = new Trash();
                    toAdd.setCategoryID(toDelete.getCategoryID());
                    toAdd.setReviewCategory(toDelete.getReviewCategory());
                    toAdd.setReviewName(toDelete.getReviewName());
                    toAdd.setReviewFeature(toDelete.getReviewFeature());
                    toAdd.setReviewWebsite(toDelete.getReviewWebsite());
                    toAdd.setReviewLocation(toDelete.getReviewLocation());
                    toAdd.setReviewDetails(toDelete.getReviewDetails());
                    toAdd.setImage(toDelete.getImage());
                    toAdd.setReviewGenre(toDelete.getReviewGenre());
                    toAdd.setReviewFamousFor(toDelete.getReviewFamousFor());
                    toAdd.setReviewCuisine(toDelete.getReviewCuisine());
                    toAdd.setReviewLanguage(toDelete.getReviewLanguage());
                    // now that we have our new object we'll put it in the database
                    database.getTrashDAO().insert(toAdd);
                    database.getReviewDAO().delete(toDelete);
                    database.getFavoritesDAO().getReviewIDSpecificFavorite(String.valueOf(toDelete.getReviewID()));
                    // now reload our recyclerview with the udpated lists
                    reviewList = database.getReviewDAO().getCategoriedReviews(String.valueOf(categoryID));
                    ReviewlistListviewAdapter myAdapter = new ReviewlistListviewAdapter(reviewList, ReviewListActivity.this, ReviewListActivity.this);
                    listView.setAdapter(myAdapter);
                    dialogInterface.dismiss();

                }
            });
            builder.setView(customLayout);
            builder.show();

        }
    };
}