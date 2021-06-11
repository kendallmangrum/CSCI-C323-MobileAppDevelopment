package com.example.project2_kmangrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Calendar myCal = Calendar.getInstance();                // Create a calendar instance to use the time of day for greeting
        int hour = myCal.get(Calendar.HOUR_OF_DAY);             // Get what hour it is (military time)

        TextView greeting = findViewById(R.id.greetingText);    // Get greeting textview

        // If condition to determine what greeting should be based off what time it is
        if (hour >= 0 && hour < 12) {                           // If between 0 and 12, say good morning
            greeting.setText("Good Morning!");
        } else if (hour >= 12 && hour < 17) {                   // If between noon and 5pm, say good afternoon
            greeting.setText("Good Afternoon!");
        } else if (hour >= 17 && hour < 20) {                   // If between 5pm and 8, say good evening
            greeting.setText("Good Evening!");
        } else {                                                // If after 8pm, say good night
            greeting.setText("Good Night!");
        }

// Get shared preferences so that we can greet the user that is signed in
        SharedPreferences userPref = getSharedPreferences("SharedAppPref", MODE_PRIVATE);
        String name = userPref.getString("NAME", "User");   // Get user's name

        TextView welcome = findViewById(R.id.welcomeText);

        welcome.setText("Hello " + name + "!");                     // Set welcome text to greet the user


    }
// Function for when Calculate BMI button is clicked
    public void goToBMI(View view) {
        Intent goToBMI = new Intent(ThirdActivity.this, FourthActivity.class);  // Go to fourth activity where user calculates bmi
        startActivity(goToBMI);
    }
}