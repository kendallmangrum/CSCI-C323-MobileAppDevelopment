package com.example.project2_kmangrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

//    Create function for when sign in button is pressed that checks if user already has an account
    public void signInFunction2(View view) {
        Intent goToThird = new Intent(SecondActivity.this, ThirdActivity.class);

        SharedPreferences userInfo = getSharedPreferences("SharedAppPref", MODE_PRIVATE);   // Access the shared preferences
        String storedUsername = userInfo.getString("USERNAME", "None");                     // Get stored username
        String storedPassword = userInfo.getString("PASSWORD", "None");                     // Get stored password

        EditText inputName = findViewById(R.id.userName2);                                          // Get username user inputted
        EditText inputPassword = findViewById(R.id.userPW2);                                        // Get password user inputted

        String username = inputName.getText().toString();                                           // Convert to string
        String password = inputPassword.getText().toString();                                       // Convert to string

        if (username.equals(storedUsername) && password.equals(storedPassword)) {                   // Check to see if username and password matches the stored username and password
            startActivity(goToThird);                                                               // If so, go to third activity
        } else {
            Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();         // If not, give toast message saying user was not found
        }


    }
}