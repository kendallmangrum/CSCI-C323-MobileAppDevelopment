package com.example.project1_kmangrum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** The function that converts the number of minutes input into seconds when the button is pressed **/
    public void convertToSeconds(View view) {
        /** Creating variables for the text fields of both the minutes and seconds by using the ID I gave them **/
        EditText minutes = findViewById(R.id.minText);
        TextView seconds = findViewById(R.id.secText);
        /** Use a try catch to determine if the user inputs a number, if not throw an error message **/
        try {
            /** Take the user's input and convert it to an int if possible **/
            int userNum = Integer.parseInt(minutes.getText().toString());
            /** If input was a number, multiply by 60 so that the correct number of seconds is computed **/
            int numSeconds = userNum * 60;
            /** Replace the text in the seconds view field to be the number of seconds calculated**/
            seconds.setText(numSeconds + " seconds");

            /** If the user didn't input an int, they will receive an error message in the seconds textview **/
        } catch (NumberFormatException e) {
            seconds.setText("Error!");
        }
    }
}