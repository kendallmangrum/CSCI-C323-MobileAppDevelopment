package com.example.project2_kmangrum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FourthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
    }

//    Create a function for when the calculate BMI button is pressed that calculates the user's bmi based off of their inputs
    public void calculateBMI(View view) {
        EditText userWeight = findViewById(R.id.userWeight);                            // Get the user's weight
        EditText userHeight = findViewById(R.id.userHeight);                            // Get the user's height

        TextView bmiText = findViewById(R.id.bmiText);                                  // Get the two textviews
        TextView bmiRating = findViewById(R.id.bmiRatingText);

//        Use a try catch to make sure the user inputs numbers and not text
        try {
            Double weightNum = Double.parseDouble(userWeight.getText().toString());     // Convert weight from string to double
            Double heightNum = Double.parseDouble(userHeight.getText().toString());     // Convert height from string to double

            Double bmi = ((weightNum / (heightNum * heightNum)) * 703);                 // Calculate bmi

            bmiText.setText("" + Math.round(bmi * 100000d)/100000d);                    // Set textview for bmi to the calculated bmi rounded to 5 decimal places

//            If conditions to determine what bmi category user falls into
            if (bmi < 18.5) {                                       // If less than 18.5, set textview to say underweight
                bmiRating.setText("Underweight");
            }else if (bmi < 24.9) {                                 // If less than 18.5, set textview to say normal weight
                bmiRating.setText("Normal Weight");
            }else if (bmi < 29.9) {                                 // If less than 18.5, set textview to say overweight
                bmiRating.setText("Overweight");
            } else {                                                // If all other conditions not met, user is obese
                bmiRating.setText("Obese");
            }

//            Get shared preferences to store user bmi
            SharedPreferences userPref = getSharedPreferences("SharedAppPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = userPref.edit();

            editor.putString("BMI_VALUE", "" + bmi);            // Add bmi to shared preferences
            editor.commit();

//            If user doesn't input numbers, they are given an error message saying to enter numbers
        } catch (NumberFormatException e) {
            bmiText.setText("Error, Please enter a number.");
            bmiRating.setText("Error, make sure you enter a number.");
        }
    }
}