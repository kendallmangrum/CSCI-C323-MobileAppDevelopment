package com.example.project2_kmangrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//    Sign in function for when the sign in button is clicked
    public void signInFunction(View view) {
//      Take the user to the second screen so that they can sign in with an existing account
        Intent goToSecond = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(goToSecond);
    }

//    Sign up function for when the sign up button is clicked
    public void signUpFunction(View view) {

        EditText getName = findViewById(R.id.Name);                         // Get the name edittext field
        String name = getName.getText().toString();                         // Get user input for name as string

        EditText getUsername = findViewById(R.id.userName);                 // Get the username edittext field
        String username = getUsername.getText().toString();                 // Get user input for username as string

        EditText getPassword = findViewById(R.id.userPW);                   // Get the password edittext field
        String password = getPassword.getText().toString();                 // Get user input for password as string

        EditText getConfirmPassword = findViewById(R.id.confirmPW);         // Get the confirm edittext field
        String confirmPassword = getConfirmPassword.getText().toString();   // Get user input for confirm password as string

//        if condition to see if the password and confirm password are the same
        if (name.matches("") || username.matches("") ||
               password.matches("") || confirmPassword.matches("")) {                       // If any of the fields are blank
            Toast.makeText(this, "A field is empty", Toast.LENGTH_LONG).show();             // Show toast saying a field is empty
        }

        else if (confirmPassword.equals(password)) {
            SharedPreferences userPref = getSharedPreferences("SharedAppPref", MODE_PRIVATE);        // Create sharedpreferences instance
            SharedPreferences.Editor editor = userPref.edit();                                             // Create editor so user information can be saved

            editor.putString("NAME", name);                                                             // Store the user's name
            editor.putString("USERNAME", username);                                                     // Store the user's username
            editor.putString("PASSWORD", password);                                                     // store the user's password
            editor.commit();                                                                                // Save the information

            Intent goToThirdAct = new Intent(MainActivity.this, ThirdActivity.class);           // Go to the welcome page, my third activity
            startActivity(goToThirdAct);
        }
        else {
            Toast.makeText(this, "Passwords do no match.", Toast.LENGTH_SHORT).show();          // If the passwords don't match, show toast message saying so
        }


    }
}