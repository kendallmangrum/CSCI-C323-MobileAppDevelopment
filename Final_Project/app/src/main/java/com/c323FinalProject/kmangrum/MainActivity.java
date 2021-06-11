package com.c323FinalProject.kmangrum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

//    Initialize variables that will be used later
    EditText username, email;
    TextView error;
    CircleImageView userIcon;
    private static final int PICK_IMAGE = 1;
    Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create references to our 2 inputs
        username = findViewById(R.id.edInputName);
        email = findViewById(R.id.edInputEmail);
        userIcon = findViewById(R.id.ivProfileImage);
        error = findViewById(R.id.tvLoginError);

        // If we already have user credentials saved, use the bitmap related to the user
        // as the circle image view source
        SharedPreferences userPref = getSharedPreferences("SharedPref", MODE_PRIVATE);
        if(userPref.contains("userPicture")){
            userIcon.setImageBitmap(getBitmapFromString(userPref.getString("userPicture", "NONE")));
        }

        // Create an onclick listener for the profile picture circle image view
        // This will enable an intent that allows users to pick their own profile image
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pictureGallery = new Intent();
                pictureGallery.setType("image/*");
                pictureGallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(pictureGallery, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageURI = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                userIcon.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void userSignIn(View view) {
        /**
         * When we hit this signup/signin button we need to consider various scenarios:
         *      - Missing data, or no data was entered correctly
         *      - No credentials currently exist within our applications shared prefs
         *      - If the credentials entered match the currently saved credentials
         */

        // First we need to create a reference to our shared prefs and the editor so we can make changes
        SharedPreferences appPrefs = getSharedPreferences("SharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = appPrefs.edit();

        // First we'll need to check and see if the user has actually input credential information
        if(username.length() == 0 || email.length() == 0){
            setError("Please enter a username and email");
        }
        // Next we check and see if there are currently no credentials within our shared prefs
        // If no credentials currently exist we'll set the entered credentials for future use
        else if(!appPrefs.contains("userName") || !appPrefs.contains("userEmail")){
            // make the error message disappear
            error.setVisibility(View.INVISIBLE);
            // save those credentials to the sharedprefs
            editor.putString("userName", username.getText().toString());
            editor.putString("userEmail", email.getText().toString());
            BitmapDrawable profileImage = (BitmapDrawable) userIcon.getDrawable();
            Bitmap userProfilePicture = profileImage.getBitmap();
            editor.putString("userPicture", BitMapToString(userProfilePicture));
            editor.apply();
            // create intent to send user to the next activity
            Intent toNavDrawerActivity = new Intent(MainActivity.this, NavDrawerActivity.class);
            startActivity(toNavDrawerActivity);
        }
        else{
            // Define our current credentials and the input credentials
            String savedUsername = appPrefs.getString("userName", "None");
            String savedEmail = appPrefs.getString("userEmail", "None");
            String enteredUsername = username.getText().toString();
            String enteredEmail = email.getText().toString();
            // We have two cases, the credentials don't match, or they do
            // Case#1: THe credentials don't match those saved on file, prompt the user to try
            //         again with an error message
            if(!enteredUsername.equals(savedUsername) || !enteredEmail.equals(savedEmail)){
                setError("Credentials don't match those saved on file, try again!");
            }
            // Case #2: The credentials match and we can send the user to the next activity
            else{
                // create intent to send user to the next activity
                Intent toNavDrawerActivity = new Intent(MainActivity.this, NavDrawerActivity.class);
                startActivity(toNavDrawerActivity);
            }
        }

    }

//    Method that displays an error message if user doesn't sign in correctly
    public void setError(String errorMessage){
        error.setVisibility(View.VISIBLE);
        error.setText(errorMessage);
    }

//    This Function converts the String back to Bitmap
    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

//    Method that converts a bitmap to a string
    private String BitMapToString (Bitmap bitmap){
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}